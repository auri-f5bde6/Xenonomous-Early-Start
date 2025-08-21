package net.hellomouse.progression_change.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.registries.ProgressionModLootTypeRegistry;
import net.hellomouse.progression_change.utils.MiningLevel;
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

import static net.hellomouse.progression_change.utils.MiningLevel.getTierName;

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


    @Override
    public LootConditionType getType() {
        return ProgressionModLootTypeRegistry.PickaxeTier.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        var itemStack = lootContext.get(LootContextParameters.TOOL);
        return MiningLevel.IsToolLowerThanTier(itemStack, lowerThanTier);
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
