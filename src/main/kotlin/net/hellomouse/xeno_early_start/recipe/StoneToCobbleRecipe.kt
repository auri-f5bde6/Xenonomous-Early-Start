package net.hellomouse.xeno_early_start.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.hellomouse.xeno_early_start.registries.ProgressionModRecipeRegistry;
import net.hellomouse.xeno_early_start.utils.MiningLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class StoneToCobbleRecipe implements Recipe<SimpleInventory> {
    Identifier id;
    Identifier minedBlock;
    boolean minedBlockIsTag;
    Identifier resultingBlock;
    ArrayList<DroppedItem> droppedItems;
    ArrayList<Identifier> matchHeldItems;
    ArrayList<Boolean> matchHeldItemsIsTag;
    boolean isOreToStone;
    boolean anyTier;
    Identifier miningTierLowerThan;
    boolean dropBlockLootTable;

    public StoneToCobbleRecipe(Identifier id, Identifier minedBlock, Identifier resultingBlock, ArrayList<DroppedItem> droppedItemList, Identifier miningTierLowerThan, boolean dropBlockLootTable, boolean isOreToStone, boolean minedBlockIsTag, boolean anyTier, ArrayList<Identifier> matchHeldItems, ArrayList<Boolean> matchHeldItemsIsTag) {
        this.id = id;
        this.minedBlock = minedBlock;
        this.resultingBlock = resultingBlock;
        this.droppedItems = droppedItemList;
        this.miningTierLowerThan = miningTierLowerThan;
        this.dropBlockLootTable = dropBlockLootTable;
        this.isOreToStone = isOreToStone;
        this.minedBlockIsTag = minedBlockIsTag;
        this.anyTier = anyTier;
        this.matchHeldItems = matchHeldItems;
        this.matchHeldItemsIsTag = matchHeldItemsIsTag;
    }

    public boolean isAnyTier() {
        return anyTier;
    }

    public ToolMaterial getMiningTierLowerThan() {
        return TierSortingRegistry.byName(miningTierLowerThan);
    }

    public boolean isDropBlockLootTable() {
        return dropBlockLootTable;
    }

    public boolean isOreToStone() {
        return isOreToStone;
    }

    public boolean matches(@NotNull BlockState state, ItemStack itemStack) {
        for (int i = 0; i < matchHeldItems.size(); i++) {
            if ((!matchHeldItemsIsTag.get(i) && matchHeldItems.get(i) == (ForgeRegistries.ITEMS.getKey(itemStack.getItem()))) || (matchHeldItemsIsTag.get(i) && itemStack.isIn(TagKey.of(ForgeRegistries.ITEMS.getRegistryKey(), matchHeldItems.get(i))))) {
                return true;
            }
        }
        if (!matchHeldItems.isEmpty()) {
            return false;
        }
        if ((this.isAnyTier() || MiningLevel.IsToolLowerThanTier(itemStack, this.getMiningTierLowerThan())) && (!this.isOreToStone() || (this.isOreToStone() && ProgressionModConfig.oreDropChanges.oreToStone))) {
            if (minedBlockIsTag) {
                TagKey<Block> tag = TagKey.of(ForgeRegistries.BLOCKS.getRegistryKey(), minedBlock);
                return state.isIn(tag);
            } else {
                return state.isOf(ForgeRegistries.BLOCKS.getValue(minedBlock));
            }
        } else {
            return false;
        }
    }

    public void maybeDropItemsInList(World level, BlockPos pos) {
        for (var i : droppedItems) {
            var probability = i.getProbability();
            if (ProgressionModConfig.earlyGameChanges.overridePebbleDropProbability && i.isPebble()) {
                probability = ProgressionModConfig.earlyGameChanges.pebbleDropProbability / 100f;
            } else if (ProgressionModConfig.earlyGameChanges.overridePlantFiberProbability && i.isPlantFiber()) {
                probability = ProgressionModConfig.earlyGameChanges.plantFiberDropProbability / 100f;
            }
            if (level.random.nextFloat() < probability) {
                Block.dropStack(level, pos, i.getItem().getDefaultStack());
            }
        }
    }

    public Block getResultingBlock() {
        return ForgeRegistries.BLOCKS.getValue(resultingBlock);
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return getResultingBlock().asItem().getDefaultStack();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return ProgressionModRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get();
    }

    // These methods aren't used here but must be implemented
    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    public static class DroppedItem {
        Identifier item;
        float probability;
        boolean affectedByFortune;
        boolean pebble;

        boolean plantFiber;

        public DroppedItem(Identifier item, float probability, boolean affectedByFortune, boolean pebble, boolean plantFiber) {
            this.item = item;
            this.probability = probability;
            this.affectedByFortune = affectedByFortune;
            this.pebble = pebble;
            this.plantFiber = plantFiber;
        }

        public static @NotNull DroppedItem read(PacketByteBuf buf) {
            var item = buf.readIdentifier();
            var probability = buf.readFloat();
            var affectedByFortune = buf.readBoolean();
            var pebble = buf.readBoolean();
            var plantFiber = buf.readBoolean();
            return new DroppedItem(item, probability, affectedByFortune, pebble, plantFiber);
        }

        public static DroppedItem from_json(JsonObject obj) {
            Identifier item = Identifier.parse(obj.get("item").getAsString());
            float probability = 1;
            if (obj.get("probability") instanceof JsonPrimitive p) {
                probability = p.getAsFloat();
            }
            var affectedByFortune = false;
            if (obj.get("affected_by_fortune") instanceof JsonPrimitive a) {
                affectedByFortune = a.getAsBoolean();
            }
            var pebble = false;
            if (obj.get("pebble") instanceof JsonPrimitive a) {
                pebble = a.getAsBoolean();
            }
            var plant_fiber = false;
            if (obj.get("plant_fiber") instanceof JsonPrimitive a) {
                plant_fiber = a.getAsBoolean();
            }
            return new DroppedItem(item, probability, affectedByFortune, pebble, plant_fiber);
        }

        public boolean isPlantFiber() {
            return plantFiber;
        }

        public Item getItem() {
            return ForgeRegistries.ITEMS.getValue(item);
        }

        public float getProbability() {
            return probability;
        }

        public boolean isAffectedByFortune() {
            return affectedByFortune;
        }

        public boolean isPebble() {
            return pebble;
        }

        public void write(PacketByteBuf buf) {
            buf.writeIdentifier(this.item);
            buf.writeFloat(this.probability);
            buf.writeBoolean(this.affectedByFortune);
            buf.writeBoolean(this.pebble);
            buf.writeBoolean(this.plantFiber);
        }
    }

    public static class Serializer implements RecipeSerializer<StoneToCobbleRecipe> {

        @Override
        public StoneToCobbleRecipe read(Identifier id, JsonObject json) {
            var minedBlockIsTag = false;
            var minedBlockStr = json.get("mined_block").getAsString();
            if (minedBlockStr.charAt(0) == '#') {
                minedBlockIsTag = true;
                minedBlockStr = minedBlockStr.substring(1);
            }
            Identifier minedBlock = Identifier.parse(minedBlockStr);
            Identifier resultingBlock = Identifier.parse(json.get("resulting_block").getAsString());
            var droppedItems = new ArrayList<DroppedItem>();
            if (json.get("dropped_items") instanceof JsonArray obj) {
                for (var i : obj.getAsJsonArray()) {
                    droppedItems.add(DroppedItem.from_json(i.getAsJsonObject()));
                }
            }
            var miningTierLowerThan = Identifier.of("minecraft", "wood");
            if (json.get("mining_tier_lower_than") instanceof JsonPrimitive obj) {
                miningTierLowerThan = Identifier.parse(obj.getAsString());
            }
            boolean dropBlockLootTable = false;
            if (json.get("drop_block_loot_table") instanceof JsonPrimitive obj) {
                dropBlockLootTable = obj.getAsBoolean();
            }
            var isOreToStone = false;
            if (json.get("ore_to_stone") instanceof JsonPrimitive obj) {
                isOreToStone = obj.getAsBoolean();
            }
            var anyTier = false;
            if (json.get("any_tier") instanceof JsonPrimitive obj) {
                anyTier = obj.getAsBoolean();
            }
            var matchHeldItems = new ArrayList<Identifier>();
            var matchHeldItemsIsTag = new ArrayList<Boolean>();
            if (json.get("held_item_match_any") instanceof JsonArray obj) {
                for (var i : obj.getAsJsonArray()) {
                    var val = i.getAsString();
                    if (val.charAt(0) == '#') {
                        matchHeldItems.add(Identifier.parse(val.substring(1)));
                        matchHeldItemsIsTag.add(true);
                    } else {
                        matchHeldItems.add(Identifier.parse(val));
                        matchHeldItemsIsTag.add(false);
                    }

                }
            }
            return new StoneToCobbleRecipe(id, minedBlock, resultingBlock, droppedItems, miningTierLowerThan, dropBlockLootTable, isOreToStone, minedBlockIsTag, anyTier, matchHeldItems, matchHeldItemsIsTag);
        }

        @Override
        public @Nullable StoneToCobbleRecipe read(Identifier id, PacketByteBuf buf) {
            Identifier minedBlock = buf.readIdentifier();
            Identifier resultingBlock = buf.readIdentifier();
            var droppedItemSize = buf.readInt();
            var droppedItems = new ArrayList<DroppedItem>();
            for (int i = 0; i < droppedItemSize; i++) {
                droppedItems.add(DroppedItem.read(buf));
            }
            Identifier miningTierLowerThan = buf.readIdentifier();
            boolean dropBlockLootTable = buf.readBoolean();
            boolean isOreToStone = buf.readBoolean();
            boolean minedBlockIsTag = buf.readBoolean();
            boolean anyTier = buf.readBoolean();
            var matchHeldItemSize = buf.readInt();
            var matchHeldItem = new ArrayList<Identifier>();
            var matchHeldItemIsTag = new ArrayList<Boolean>();
            for (int i = 0; i < matchHeldItemSize; i++) {
                matchHeldItem.add(buf.readIdentifier());
                matchHeldItemIsTag.add(buf.readBoolean());
            }
            return new StoneToCobbleRecipe(id, minedBlock, resultingBlock, droppedItems, miningTierLowerThan, dropBlockLootTable, isOreToStone, minedBlockIsTag, anyTier, matchHeldItem, matchHeldItemIsTag);
        }

        @Override
        public void write(PacketByteBuf buf, StoneToCobbleRecipe recipe) {
            buf.writeIdentifier(recipe.minedBlock);
            buf.writeIdentifier(recipe.resultingBlock);
            buf.writeInt(recipe.droppedItems.size());
            for (var i : recipe.droppedItems) {
                i.write(buf);
            }
            buf.writeIdentifier(recipe.miningTierLowerThan);
            buf.writeBoolean(recipe.dropBlockLootTable);
            buf.writeBoolean(recipe.isOreToStone);
            buf.writeBoolean(recipe.minedBlockIsTag);
            buf.writeBoolean(recipe.anyTier);
            buf.writeInt(recipe.matchHeldItems.size());
            for (var i = 0; i < recipe.matchHeldItemsIsTag.size(); i++) {
                buf.writeIdentifier(recipe.matchHeldItems.get(i));
                buf.writeBoolean(recipe.matchHeldItemsIsTag.get(i));
            }
        }
    }
}
