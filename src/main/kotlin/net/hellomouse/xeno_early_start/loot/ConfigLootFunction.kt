package net.hellomouse.xeno_early_start.loot;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.hellomouse.xeno_early_start.registries.ProgressionModLootTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.JsonSerializer;

/// Make it so the loot table drop amount of raw nuggets as configured in UI
public class ConfigLootFunction implements LootFunction {
    private static final Gson GSON = new GsonBuilder().create();
    private final DropType dropType;

    public ConfigLootFunction(DropType dropType) {
        this.dropType = dropType;
    }

    @Override
    public LootFunctionType getType() {
        return ProgressionModLootTypeRegistry.ConfigLootFunction.get();
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        if (dropType == DropType.Copper) {
            itemStack.setCount(ProgressionModConfig.oreDropChanges.rawCopperNuggetDrop);
        } else if (dropType == DropType.Iron) {
            itemStack.setCount(ProgressionModConfig.oreDropChanges.rawIronNuggetDrop);
        } else if (dropType == DropType.Gold) {
            itemStack.setCount(ProgressionModConfig.oreDropChanges.rawGoldNuggetDrop);
        } else if (dropType == DropType.Diamond) {
            itemStack.setCount(ProgressionModConfig.oreDropChanges.diamondFragmentDrop);
        }
        return itemStack;
    }

    enum DropType {
        @SerializedName("iron")
        Iron,
        @SerializedName("copper")
        Copper,
        @SerializedName("gold")
        Gold,
        @SerializedName("diamond")
        Diamond
    }

    public static class Serializer implements JsonSerializer<ConfigLootFunction> {

        @Override
        public void toJson(JsonObject json, ConfigLootFunction object, JsonSerializationContext context) {
            json.addProperty("drop_type", GSON.toJson(object.dropType));
        }

        @Override
        public ConfigLootFunction fromJson(JsonObject json, JsonDeserializationContext context) {
            return new ConfigLootFunction(GSON.fromJson(json.get("drop_type"), DropType.class));
        }
    }

}
