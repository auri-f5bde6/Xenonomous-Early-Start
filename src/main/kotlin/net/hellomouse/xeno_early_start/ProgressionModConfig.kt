package net.hellomouse.xeno_early_start

import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.text.Text

object ProgressionModConfig {
    @JvmField
    var oreDropChanges: OreDropChanges = OreDropChanges()

    @JvmField
    var earlyGameChanges: EarlyGameChanges = EarlyGameChanges()

    @JvmField
    var mobChanges: MobChanges = MobChanges()

    @JvmField
    var blockChanges: BlockChanges = BlockChanges()

    class OreDropChanges {
        var moddedPickaxeWorkaround: Boolean =
            true // if a mod didn't registerTier to TierSortingRegistry, check the mining level int instead
        var rawCopperNuggetDrop: Int = 1
        var rawIronNuggetDrop: Int = 1
        var rawGoldNuggetDrop: Int = 1
        var diamondFragmentDrop: Int = 1
        var oreToStone: Boolean = false
    }

    class EarlyGameChanges {
        var overridePlantFiberProbability: Boolean = false
        var plantFiberDropProbability: Int = 5
        var overridePebbleDropProbability: Boolean = false
        var pebbleDropProbability: Int = 40
    }

    class MobChanges {
        var flatAdditiveMobSpawnWithEquipment: Float = 0.05f
        var replaceEntityCopperArmourProbability: Float = 0.05f
        var entitySpawnWithCopperToolProbability: Float = 0.05f
    }

    class BlockChanges {
        enum class FixThinBlockStepSound {
            True,
            False,
            OnlyThisMod
        }

        var stonecutterDamage: Float = 3f
        var fixThinBlockStepSound: FixThinBlockStepSound = FixThinBlockStepSound.True
    }

    object Gui {
        private fun getTranslatableText(name: String?): Text {
            return Text.translatable("text.config.${ProgressionMod.Companion.MODID}.$name")
        }

        private fun getTranslatableTextOption(name: String?): Text {
            return getTranslatableText("option.$name")
        }

        val configBuilder: ConfigBuilder
            get() {
                val builder = ConfigBuilder.create()
                    .setTitle(getTranslatableText("title"))
                    .transparentBackground()  // heh, rewriting the entire config just for this
                val entryBuilder = ConfigEntryBuilder.create()
                addOreDropChangesEntries(builder, entryBuilder)
                addEarlyGameEntries(builder, entryBuilder)
                addMobChangesEntries(builder, entryBuilder)
                addBlockChangesEntries(builder, entryBuilder)
                return builder
            }

        private fun addEarlyGameEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
            val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("earlyGameChanges"))
            category.addEntry(
                entryBuilder.startBooleanToggle(
                    getTranslatableTextOption("earlyGameChanges.overridePlantFiberDropProbability"),
                    earlyGameChanges.overridePlantFiberProbability
                ).setSaveConsumer { aBoolean: Boolean ->
                    earlyGameChanges.overridePlantFiberProbability = aBoolean
                }
                    .setDefaultValue(false)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("earlyGameChanges.plantFiberDropProbability"),
                    earlyGameChanges.plantFiberDropProbability,
                    1,
                    100
                ).setSaveConsumer { aInt: Int -> earlyGameChanges.plantFiberDropProbability = aInt }
                    .setDefaultValue(5)
                    .build()
            )
            category.addEntry(
                entryBuilder.startBooleanToggle(
                    getTranslatableTextOption("earlyGameChanges.overridePebbleDropProbability"),
                    earlyGameChanges.overridePebbleDropProbability
                ).setSaveConsumer { aBoolean: Boolean ->
                    earlyGameChanges.overridePebbleDropProbability = aBoolean
                }
                    .setDefaultValue(false)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("earlyGameChanges.pebbleDropProbability"),
                    earlyGameChanges.pebbleDropProbability,
                    1,
                    100
                ).setSaveConsumer { aInt: Int -> earlyGameChanges.pebbleDropProbability = aInt }
                    .setDefaultValue(40)
                    .build()
            )
        }

        private fun addMobChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
            val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("mobChanges"))
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("mobChanges.flatAdditiveMobSpawnWithEquipment"),
                    (mobChanges.flatAdditiveMobSpawnWithEquipment * 100f).toInt(),
                    1,
                    100
                ).setSaveConsumer { aInt: Int ->
                    mobChanges.flatAdditiveMobSpawnWithEquipment = (aInt / 100f)
                }
                    .setTooltip(getTranslatableTextOption("mobChanges.flatAdditiveMobSpawnWithEquipment.tooltip"))
                    .setDefaultValue(5)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("mobChanges.replaceEntityCopperArmourProbability"),
                    (mobChanges.replaceEntityCopperArmourProbability * 100f).toInt(),
                    1,
                    100
                ).setSaveConsumer { aInt: Int ->
                    mobChanges.replaceEntityCopperArmourProbability = (aInt / 100f)
                }
                    .setTooltip(getTranslatableTextOption("mobChanges.replaceEntityCopperArmourProbability.tooltip"))
                    .setDefaultValue(5)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("mobChanges.entitySpawnWithCopperToolProbability"),
                    (mobChanges.entitySpawnWithCopperToolProbability * 100f).toInt(),
                    1,
                    100
                ).setSaveConsumer { aInt: Int ->
                    mobChanges.entitySpawnWithCopperToolProbability = (aInt / 100f)
                }
                    .setDefaultValue(5)
                    .build()
            )
        }

        private fun addOreDropChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
            val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("oreDropChanges"))
            category.addEntry(
                entryBuilder.startBooleanToggle(
                    getTranslatableTextOption("oreDropChanges.moddedPickaxeWorkaround"),
                    oreDropChanges.moddedPickaxeWorkaround
                )
                    .setSaveConsumer { aBoolean: Boolean ->
                        oreDropChanges.moddedPickaxeWorkaround = aBoolean
                    }
                    .setTooltip(getTranslatableTextOption("oreDropChanges.moddedPickaxeWorkaround.tooltip"))
                    .setDefaultValue(true)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("oreDropChanges.rawCopperNuggetDrop"),
                    oreDropChanges.rawCopperNuggetDrop,
                    1,
                    9
                )
                    .setSaveConsumer { aInt: Int -> oreDropChanges.rawCopperNuggetDrop = aInt }
                    .setDefaultValue(1)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("oreDropChanges.rawIronNuggetDrop"),
                    oreDropChanges.rawIronNuggetDrop,
                    1,
                    9
                )
                    .setSaveConsumer { aInt: Int -> oreDropChanges.rawIronNuggetDrop = aInt }
                    .setDefaultValue(1)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("oreDropChanges.rawGoldNuggetDrop"),
                    oreDropChanges.rawGoldNuggetDrop,
                    1,
                    9
                )
                    .setSaveConsumer { aInt: Int -> oreDropChanges.rawGoldNuggetDrop = aInt }
                    .setDefaultValue(1)
                    .build()
            )
            category.addEntry(
                entryBuilder.startIntSlider(
                    getTranslatableTextOption("oreDropChanges.rawDiamondFragmentDrop"),
                    oreDropChanges.diamondFragmentDrop,
                    1,
                    9
                )
                    .setSaveConsumer { aInt: Int -> oreDropChanges.diamondFragmentDrop = aInt }
                    .setDefaultValue(1)
                    .build()
            )
            category.addEntry(
                entryBuilder.startBooleanToggle(
                    getTranslatableTextOption("oreDropChanges.oreToStone"), oreDropChanges.oreToStone
                )
                    .setSaveConsumer { aBoolean: Boolean -> oreDropChanges.oreToStone = aBoolean }
                    .setTooltip(getTranslatableTextOption("oreDropChanges.oreToStone.tooltip"))
                    .setDefaultValue(false)
                    .build()
            )
        }

        private fun addBlockChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
            val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("blockChanges"))
            category.addEntry(
                entryBuilder.startFloatField(
                    getTranslatableTextOption("blockChanges.stonecutterDamage"),
                    blockChanges.stonecutterDamage,
                ).setSaveConsumer { aFloat: Float -> blockChanges.stonecutterDamage = aFloat }
                    .setDefaultValue(3f)
                    .setMin(0f)
                    .setMax(5f)
                    .build()
            )
            category.addEntry(
                entryBuilder.startEnumSelector<BlockChanges.FixThinBlockStepSound>(
                    getTranslatableTextOption("blockChanges.fixThinBlockStepSound"),
                    BlockChanges.FixThinBlockStepSound::class.java,
                    blockChanges.fixThinBlockStepSound
                ).setSaveConsumer { enum: BlockChanges.FixThinBlockStepSound ->
                    blockChanges.fixThinBlockStepSound = enum
                }
                    .setDefaultValue(BlockChanges.FixThinBlockStepSound.True)
                    .build()
            )
        }
    }
}
