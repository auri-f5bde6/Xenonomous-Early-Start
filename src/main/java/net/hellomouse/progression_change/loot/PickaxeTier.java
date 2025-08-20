package net.hellomouse.progression_change.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.ProgressionModConfig;
import net.hellomouse.progression_change.registries.ProgressionModLootTypeRegistry;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.JsonSerializer;
import net.minecraftforge.common.TierSortingRegistry;

import javax.annotation.Nullable;

public class PickaxeTier implements LootCondition {
    ToolMaterial lowerThanTier;

    public PickaxeTier(@Nullable ToolMaterial belowTierMaterial) {
        this.lowerThanTier = belowTierMaterial;
    }

    public static PickaxeTier from_identifier(Identifier belowTierIdentifier) {
        var mat = TierSortingRegistry.byName(belowTierIdentifier);
        if (mat == null) {
            ProgressionMod.LOGGER.error("{} is not registered in TierSortingRegistry", belowTierIdentifier);
        }
        return new PickaxeTier(mat);
    }

    public static PickaxeTier from_string(String belowTier) {
        var id = Identifier.tryParse(belowTier);
        if (id == null) {
            ProgressionMod.LOGGER.error("{} is not a valid identifier", belowTier, new InvalidIdentifierException(belowTier + " is not a valid identifier"));
        }
        return from_identifier(id);
    }

    private static String getTierName(ToolMaterial material) {
        var name = TierSortingRegistry.getName(material);
        if (name == null) {
            ProgressionMod.LOGGER.warn("Pickaxe Tier " + material + " is missing from tierSortingRegistry! (pickaxe_tier's below_tier will fallback to minecraft:wood)");
            return "minecraft:wood";
        } else {
            return name.toString();
        }
    }

    @Override
    public LootConditionType getType() {
        return ProgressionModLootTypeRegistry.PickaxeTier.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        var itemStack = lootContext.get(LootContextParameters.TOOL);
        if (itemStack == null) {
            return false;
        } else if (itemStack.getItem() instanceof MiningToolItem miningToolItem) {
            var toolMaterial = miningToolItem.getMaterial();
            if (TierSortingRegistry.isTierSorted(toolMaterial)) {
                return TierSortingRegistry.getTiersLowerThan(lowerThanTier).contains(toolMaterial);
            } else {
                return toolMaterial.getMiningLevel() < lowerThanTier.getMiningLevel() && ProgressionModConfig.oreDropChanges.moddedPickaxeWorkaround;
            }
        }
        return false;
    }

    public static class Serializer implements JsonSerializer<PickaxeTier> {
        @Override
        public void toJson(JsonObject jsonObject, PickaxeTier pickaxeTier, JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("below_tier", getTierName(pickaxeTier.lowerThanTier));
        }

        @Override
        public PickaxeTier fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return from_string(jsonObject.get("below_tier").getAsString());
        }
    }
}
