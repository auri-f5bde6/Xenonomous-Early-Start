package net.hellomouse.progression_change;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ProgressionModConfig {

    public static OreDropChanges oreDropChanges = new OreDropChanges();
    public static EarlyGameChanges earlyGameChanges = new EarlyGameChanges();

    public static class OreDropChanges {
        public boolean moddedPickaxeWorkaround = true; // if a mod didn't registerTier to TierSortingRegistry, check the mining level int instead
        public int rawCopperNuggetDrop = 1;
        public int rawIronNuggetDrop = 1;
        public int rawGoldNuggetDrop = 1;
        public int diamondFragmentDrop = 1;
        public boolean oreToStone = false;
    }

    public static class EarlyGameChanges {
        public boolean overridePlantFiberProbability = false;
        public int plantFiberDropProbability = 5;
        public boolean overridePebbleDropProbability = false;
        public int pebbleDropProbability = 40;
    }

    public static class Gui {
        private static Text getTranslatableText(String name) {
            return Text.translatable("text.config." + ProgressionMod.MODID + "." + name);
        }

        private static Text getTranslatableTextOption(String name) {
            return getTranslatableText("option." + name);
        }

        public static ConfigBuilder getConfigBuilder() {
            ConfigBuilder builder = ConfigBuilder.create().setTitle(getTranslatableText("title")).transparentBackground();
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
            addOreDropChangesEntries(builder, entryBuilder);
            addEarlyGameEntries(builder, entryBuilder);
            return builder; // heh, rewriting the entire config just for this
        }

        private static void addEarlyGameEntries(ConfigBuilder configBuilder, ConfigEntryBuilder entryBuilder) {
            ConfigCategory category = configBuilder.getOrCreateCategory(getTranslatableTextOption("earlyGameChanges"));
            category.addEntry(
                    entryBuilder.startBooleanToggle(
                                    getTranslatableTextOption("earlyGameChanges.overridePlantFiberDropProbability"),
                                    earlyGameChanges.overridePlantFiberProbability
                            ).setSaveConsumer(aBoolean -> earlyGameChanges.overridePlantFiberProbability = aBoolean)
                            .setDefaultValue(false)
                            .build()
            );
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("earlyGameChanges.plantFiberDropProbability"),
                                    earlyGameChanges.plantFiberDropProbability,
                                    1,
                                    100
                            ).setSaveConsumer(aInt -> earlyGameChanges.plantFiberDropProbability = aInt)
                            .setDefaultValue(5)
                            .build()
            );
            category.addEntry(
                    entryBuilder.startBooleanToggle(
                                    getTranslatableTextOption("earlyGameChanges.overridePebbleDropProbability"),
                                    earlyGameChanges.overridePebbleDropProbability
                            ).setSaveConsumer(aBoolean -> earlyGameChanges.overridePebbleDropProbability = aBoolean)
                            .setDefaultValue(false)
                            .build()
            );
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("earlyGameChanges.pebbleDropProbability"),
                                    earlyGameChanges.pebbleDropProbability,
                                    1,
                                    100
                            ).setSaveConsumer(aInt -> earlyGameChanges.pebbleDropProbability = aInt)
                            .setDefaultValue(40)
                            .build()
            );
        }

        private static void addOreDropChangesEntries(ConfigBuilder configBuilder, ConfigEntryBuilder entryBuilder) {
            ConfigCategory category = configBuilder.getOrCreateCategory(getTranslatableTextOption("oreDropChanges"));
            category.addEntry(
                    entryBuilder.startBooleanToggle(
                                    getTranslatableTextOption("oreDropChanges.moddedPickaxeWorkaround"), oreDropChanges.moddedPickaxeWorkaround)
                            .setSaveConsumer(aBoolean -> oreDropChanges.moddedPickaxeWorkaround = aBoolean)
                            .setTooltip(getTranslatableTextOption("oreDropChanges.moddedPickaxeWorkaround.tooltip"))
                            .setDefaultValue(true)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawCopperNuggetDrop"), oreDropChanges.rawCopperNuggetDrop, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.rawCopperNuggetDrop = aInt)
                            .setDefaultValue(1)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawIronNuggetDrop"), oreDropChanges.rawIronNuggetDrop, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.rawIronNuggetDrop = aInt)
                            .setDefaultValue(1)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawGoldNuggetDrop"), oreDropChanges.rawGoldNuggetDrop, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.rawGoldNuggetDrop = aInt)
                            .setDefaultValue(1)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawDiamondFragmentDrop"), oreDropChanges.diamondFragmentDrop, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.diamondFragmentDrop = aInt)
                            .setDefaultValue(1)
                            .build());
            category.addEntry(
                    entryBuilder.startBooleanToggle(
                                    getTranslatableTextOption("oreDropChanges.oreToStone"), oreDropChanges.oreToStone)
                            .setSaveConsumer(aBoolean -> oreDropChanges.oreToStone = aBoolean)
                            .setTooltip(getTranslatableTextOption("oreDropChanges.oreToStone.tooltip"))
                            .setDefaultValue(false)
                            .build());
        }
    }
}
