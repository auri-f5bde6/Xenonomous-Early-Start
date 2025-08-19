package net.hellomouse.progression_change;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ProgressionModConfig {

    public static OreDropChanges oreDropChanges = new OreDropChanges();
    public static EarlyGameChanges earlyGameChanges = new EarlyGameChanges();

    public static class OreDropChanges {
        public boolean moddedPickaxeWorkaround = false; // if a mod didn't registerTier to TierSortingRegistry, check the mining level int instead
        public int rawCopperNuggetDrop = 1;
        public int rawIronNuggetDrop = 1;
        public int rawGoldNuggetDrop = 1;
        public int rawDiamondFragmentDrop = 1;
    }

    public static class EarlyGameChanges {
        public int plantFiberDropProbability = 5;
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
                    entryBuilder.startIntSlider(
                            getTranslatableTextOption("earlyGameChanges.plantFiberDropProbability"),
                            0,
                            100,
                            1
                    ).setSaveConsumer(aInt -> earlyGameChanges.plantFiberDropProbability = aInt).build()
            );
        }

        private static void addOreDropChangesEntries(ConfigBuilder configBuilder, ConfigEntryBuilder entryBuilder) {
            ConfigCategory category = configBuilder.getOrCreateCategory(getTranslatableTextOption("oreDropChanges"));
            category.addEntry(
                    entryBuilder.startBooleanToggle(
                                    getTranslatableTextOption("oreDropChanges.moddedPickaxeWorkaround"), false)
                            .setSaveConsumer(aBoolean -> oreDropChanges.moddedPickaxeWorkaround = aBoolean)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawCopperNuggetDrop"), 1, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.rawCopperNuggetDrop = aInt)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawIronNuggetDrop"), 1, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.rawIronNuggetDrop = aInt)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawGoldNuggetDrop"), 1, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.rawGoldNuggetDrop = aInt)
                            .build());
            category.addEntry(
                    entryBuilder.startIntSlider(
                                    getTranslatableTextOption("oreDropChanges.rawDiamondFragmentDrop"), 1, 1, 9)
                            .setSaveConsumer(aInt -> oreDropChanges.rawDiamondFragmentDrop = aInt)
                            .build());
        }
    }
}
