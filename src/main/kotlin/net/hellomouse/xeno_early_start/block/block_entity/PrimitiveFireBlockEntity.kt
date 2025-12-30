package net.hellomouse.xeno_early_start.block.block_entity

import net.hellomouse.xeno_early_start.ProgressionModConfig
import net.hellomouse.xeno_early_start.block.PrimitiveFireBlock
import net.hellomouse.xeno_early_start.recipe.PrimitiveFireRecipe
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockEntityRegistry
import net.hellomouse.xeno_early_start.registries.ProgressionModRecipeRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CampfireBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.recipe.AbstractCookingRecipe
import net.minecraft.recipe.CampfireCookingRecipe
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeManager.MatchGetter
import net.minecraft.recipe.RecipeType
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Clearable
import net.minecraft.util.ItemScatterer
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import java.util.*
import java.util.function.Function
import kotlin.math.min

class PrimitiveFireBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(
    ProgressionModBlockEntityRegistry.PRIMITIVE_FIRE.get(), pos, state
), Clearable {
    private val itemsBeingCooked: DefaultedList<ItemStack> = DefaultedList.ofSize(2, ItemStack.EMPTY)
    private val cookingTimes = IntArray(2)
    private val cookingTotalTimes = IntArray(2)
    var burnTime = 0
    private val primitiveFireMatchGetter: MatchGetter<Inventory, PrimitiveFireRecipe> =
        RecipeManager.createCachedMatchGetter(ProgressionModRecipeRegistry.PRIMITIVE_FIRE_TYPE.get())
    private val campfireMatchGetter: MatchGetter<Inventory, CampfireCookingRecipe> =
        RecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING)

    companion object {
        fun litServerTick(world: World, pos: BlockPos, state: BlockState, primitiveFire: PrimitiveFireBlockEntity) {

            if (world.hasRain(pos) || primitiveFire.burnTime <= 0) {
                if (!world.isClient()) {
                    world.playSound(
                        null as PlayerEntity?,
                        pos,
                        SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                    )
                }
                CampfireBlock.extinguish(null, world, pos, state)
                world.setBlockState(pos, state.with(PrimitiveFireBlock.LIT, false), Block.NOTIFY_ALL)
                return
            } else {
                val newLightLevel = (min(
                    1.0f, primitiveFire.burnTime
                            / (ProgressionModConfig.config.earlyGameChanges.primitiveFire.maxBurnTime.toFloat() * ProgressionModConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness)
                ) * 15f).toInt()
                if (newLightLevel != state.get(PrimitiveFireBlock.LIGHT_LEVEL)) {
                    world.setBlockState(
                        pos,
                        state.with(PrimitiveFireBlock.LIGHT_LEVEL, newLightLevel),
                        Block.NOTIFY_ALL
                    )
                }
            }
            if (world.random.nextDouble() < (1f / 20f) && primitiveFire.burnTime > 0) {
                primitiveFire.burnTime -= 20
            }
            var bl = false

            for (i in primitiveFire.itemsBeingCooked.indices) {
                val itemStack = primitiveFire.itemsBeingCooked[i]
                if (!itemStack.isEmpty) {
                    bl = true
                    primitiveFire.cookingTimes[i]++
                    if (primitiveFire.cookingTimes[i] >= primitiveFire.cookingTotalTimes[i]) {
                        val inventory: Inventory = SimpleInventory(itemStack)
                        val primitiveRecipeResult = primitiveFire.primitiveFireMatchGetter
                            .getFirstMatch(inventory, world)
                            .map(Function { recipe: PrimitiveFireRecipe ->
                                recipe.craft(
                                    inventory,
                                    world.registryManager
                                )
                            })
                        val campfireRecipeResult =
                            primitiveFire.campfireMatchGetter.getFirstMatch(inventory, world)
                                .map(Function { recipe: CampfireCookingRecipe ->
                                    recipe.craft(inventory, world.registryManager)
                                })
                        var spawnItem: ItemStack = itemStack
                        if (primitiveRecipeResult.isPresent) {
                            spawnItem = primitiveRecipeResult.get()
                        } else if (campfireRecipeResult.isPresent) {
                            spawnItem = campfireRecipeResult.get()
                        }
                        if (spawnItem.isItemEnabled(world.enabledFeatures)) {
                            ItemScatterer.spawn(
                                world,
                                pos.x.toDouble(),
                                pos.y.toDouble(),
                                pos.z.toDouble(),
                                spawnItem
                            )
                            primitiveFire.itemsBeingCooked[i] = ItemStack.EMPTY
                            world.updateListeners(pos, state, state, Block.NOTIFY_ALL)
                            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state))
                        }
                    }
                }
            }

            if (bl) {
                markDirty(world, pos, state)
            }
        }

        fun unlitServerTick(world: World, pos: BlockPos?, state: BlockState, primitiveFire: PrimitiveFireBlockEntity) {
            var bl = false

            for (i in primitiveFire.itemsBeingCooked.indices) {
                if (primitiveFire.cookingTimes[i] > 0) {
                    bl = true
                    primitiveFire.cookingTimes[i] =
                        MathHelper.clamp(primitiveFire.cookingTimes[i] - 4, 0, primitiveFire.cookingTotalTimes[i])
                }
            }

            if (bl) {
                markDirty(world, pos, state)
            }
        }

        fun clientTick(world: World, pos: BlockPos, state: BlockState, primitiveFire: PrimitiveFireBlockEntity) {
            val random = world.random
            if (random.nextFloat() < 0.11f) {
                for (i in 0..<random.nextInt(2) + 2) {
                    PrimitiveFireBlock.spawnSmokeParticle(world, pos, false)
                }
            }
        }

    }

    fun getItemsBeingCooked(): DefaultedList<ItemStack> {
        return this.itemsBeingCooked
    }

    fun writeCustomNbt(nbt: NbtCompound) {
        nbt.putInt("BurnTime", this.burnTime)
    }

    fun readCustomNbt(nbt: NbtCompound) {
        this.burnTime = nbt.getInt("BurnTime")
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        this.itemsBeingCooked.clear()
        Inventories.readNbt(nbt, this.itemsBeingCooked)
        if (nbt.contains("CookingTimes", NbtElement.INT_ARRAY_TYPE.toInt())) {
            val array = nbt.getIntArray("CookingTimes")
            System.arraycopy(array, 0, this.cookingTimes, 0, min(this.cookingTotalTimes.size, array.size))
        }

        if (nbt.contains("CookingTotalTimes", NbtElement.INT_ARRAY_TYPE.toInt())) {
            val array = nbt.getIntArray("CookingTotalTimes")
            System.arraycopy(array, 0, this.cookingTotalTimes, 0, min(this.cookingTotalTimes.size, array.size))
        }
        readCustomNbt(nbt)
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        Inventories.writeNbt(nbt, this.itemsBeingCooked, true)
        nbt.putIntArray("CookingTimes", this.cookingTimes)
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes)
        writeCustomNbt(nbt)
    }


    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket? {
        val nbt = NbtCompound()
        writeCustomNbt(nbt)
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        val nbtCompound = NbtCompound()
        Inventories.writeNbt(nbtCompound, this.itemsBeingCooked, true)
        writeCustomNbt(nbtCompound)
        return nbtCompound
    }


    fun getRecipeFor(stack: ItemStack): Optional<AbstractCookingRecipe> {
        return if (this.itemsBeingCooked.stream().noneMatch { obj: ItemStack -> obj.isEmpty }) {
            Optional.empty()
        } else {
            val a = this.primitiveFireMatchGetter.getFirstMatch(SimpleInventory(stack), this.world)
            val b = this.campfireMatchGetter.getFirstMatch(SimpleInventory(stack), this.world)
            if (a.isPresent) {
                Optional.of(a.get() as AbstractCookingRecipe)
            } else if (b.isPresent) {
                Optional.of(b.get() as AbstractCookingRecipe)
            } else {
                Optional.empty()
            }
        }
    }

    fun addItem(user: Entity?, stack: ItemStack, cookTime: Int): Boolean {
        for (i in this.itemsBeingCooked.indices) {
            val itemStack = this.itemsBeingCooked[i]
            if (itemStack.isEmpty) {
                this.cookingTotalTimes[i] = cookTime
                this.cookingTimes[i] = 0
                this.itemsBeingCooked[i] = stack.split(1)
                this.world!!.emitGameEvent(
                    GameEvent.BLOCK_CHANGE,
                    this.getPos(),
                    GameEvent.Emitter.of(user, this.cachedState)
                )
                this.updateListeners()
                return true
            }
        }

        return false
    }

    private fun updateListeners() {
        this.markDirty()
        this.getWorld()!!.updateListeners(this.getPos(), this.cachedState, this.cachedState, Block.NOTIFY_ALL)
    }

    override fun clear() {
        this.itemsBeingCooked.clear()
    }

    fun spawnItemsBeingCooked() {
        if (this.world != null) {
            this.updateListeners()
        }
    }
}