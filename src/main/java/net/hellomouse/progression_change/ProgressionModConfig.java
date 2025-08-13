package net.hellomouse.progression_change;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ProgressionMod.MODID)
public class ProgressionModConfig implements ConfigData {
    @ConfigEntry.Category("ore_drop_changes")
    @ConfigEntry.Gui.TransitiveObject
    public OreDropChanges oreDropChanges = new OreDropChanges();

    public static class OreDropChanges {
        @ConfigEntry.BoundedDiscrete(min = 0, max = 9)
        public int rawCopperNuggetDrop = 1;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 9)
        public int rawIronNuggetDrop = 1;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 9)
        public int rawGoldNuggetDrop = 1;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 9)
        public int rawDiamondFragmentDrop = 1;
    }
}