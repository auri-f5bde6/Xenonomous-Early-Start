package com.github.auri_f5bde6.xeno_early_start.block.block_entity

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockEntityRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CampfireBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
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
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import java.util.*
import java.util.function.Function
import kotlin.math.floor
import kotlin.math.min

class PrimitiveFireBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(
    XenoEarlyStartBlockEntityRegistry.PRIMITIVE_FIRE.get(), pos, state
), Clearable {
    private val itemsBeingCooked: DefaultedList<ItemStack> = DefaultedList.ofSize(2, ItemStack.EMPTY)
    private val cookingTimes = IntArray(2)
    private val cookingTotalTimes = IntArray(2)
    var burnTime: Int = 0
        set(value) {
            field = min(value, XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime)
        }
    private val campfireMatchGetter: MatchGetter<Inventory, CampfireCookingRecipe> =
        RecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING)

    companion object {
        val ITEM_UUID = UUID.randomUUID()

        // Copied off ItemScatterer.spawn
        private fun spawn(world: World, x: Double, y: Double, z: Double, stack: ItemStack) {
            val d = EntityType.ITEM.width.toDouble()
            val e = 1.0 - d
            val f = d / 2.0
            val g = floor(x) + world.random.nextDouble() * e + f
            val h = floor(y) + world.random.nextDouble() * e
            val i = floor(z) + world.random.nextDouble() * e + f

            while (!stack.isEmpty) {
                val itemEntity = ItemEntity(world, g, h, i, stack.split(world.random.nextInt(21) + 10))
                itemEntity.setThrower(ITEM_UUID)
                itemEntity.setVelocity(
                    world.random.nextTriangular(0.0, 0.11485000171139836),
                    world.random.nextTriangular(0.2, 0.11485000171139836),
                    world.random.nextTriangular(0.0, 0.11485000171139836)
                )
                world.spawnEntity(itemEntity)
            }
        }

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
                            / (XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime.toFloat() * XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness)
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
                        val campfireRecipeResult =
                            primitiveFire.campfireMatchGetter.getFirstMatch(inventory, world)
                                .map(Function { recipe: CampfireCookingRecipe ->
                                    recipe.craft(inventory, world.registryManager)
                                })
                        var spawnItem: ItemStack = itemStack
                        if (campfireRecipeResult.isPresent) {
                            spawnItem = campfireRecipeResult.get()
                        }
                        if (spawnItem.isItemEnabled(world.enabledFeatures)) {
                            spawn(
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
            val b = this.campfireMatchGetter.getFirstMatch(SimpleInventory(stack), this.world)
            if (b.isPresent) {
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