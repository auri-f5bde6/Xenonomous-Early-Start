package net.hellomouse.progression_change;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ProgressionMod.MODID)
public class ProgressionModConfig implements ConfigData {
    @ConfigEntry.BoundedDiscrete(min=0, max=9)
    public int ironNuggetDrop=1;
    @ConfigEntry.BoundedDiscrete(min = 0, max = 9)
    public int goldNuggetDrop = 1;
}