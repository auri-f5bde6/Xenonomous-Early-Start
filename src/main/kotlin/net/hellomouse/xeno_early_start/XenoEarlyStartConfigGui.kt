package net.hellomouse.xeno_early_start

import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.text.Text
import net.minecraftforge.fml.loading.FMLPaths

object XenoEarlyStartConfigGui {
    private fun getTranslatableText(name: String?): Text {
        return Text.translatable("text.config.${ProgressionMod.MODID}.$name")
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
            addoreChangesEntries(builder, entryBuilder)
            addEarlyGameEntries(builder, entryBuilder)
            addMobChangesEntries(builder, entryBuilder)
            addBlockChangesEntries(builder, entryBuilder)
            builder.setSavingRunnable {
                val tomlWriter = me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter()
                tomlWriter.write(
                    ProgressionModConfig.config,
                    FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
                )
            }
            return builder
        }

    private fun addEarlyGameEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("earlyGameChanges"))
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("earlyGameChanges.overridePlantFiberDropProbability"),
                ProgressionModConfig.config.earlyGameChanges.overridePlantFiberProbability
            ).setSaveConsumer { aBoolean: Boolean ->
                ProgressionModConfig.config.earlyGameChanges.overridePlantFiberProbability = aBoolean
            }
                .setDefaultValue(false)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("earlyGameChanges.plantFiberDropProbability"),
                ProgressionModConfig.config.earlyGameChanges.plantFiberDropProbability,
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                ProgressionModConfig.config.earlyGameChanges.plantFiberDropProbability = aInt
            }
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("earlyGameChanges.overridePebbleDropProbability"),
                ProgressionModConfig.config.earlyGameChanges.overridePebbleDropProbability
            ).setSaveConsumer { aBoolean: Boolean ->
                ProgressionModConfig.config.earlyGameChanges.overridePebbleDropProbability = aBoolean
            }
                .setDefaultValue(false)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("earlyGameChanges.pebbleDropProbability"),
                ProgressionModConfig.config.earlyGameChanges.pebbleDropProbability,
                1,
                100
            ).setSaveConsumer { aInt: Int -> ProgressionModConfig.config.earlyGameChanges.pebbleDropProbability = aInt }
                .setDefaultValue(40)
                .build()
        )
    }

    private fun addMobChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("mobChanges"))
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.flatAdditiveMobSpawnWithEquipment"),
                (ProgressionModConfig.config.mobChanges.flatAdditiveMobSpawnWithEquipment * 100f).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                ProgressionModConfig.config.mobChanges.flatAdditiveMobSpawnWithEquipment = (aInt / 100f)
            }
                .setTooltip(getTranslatableTextOption("mobChanges.flatAdditiveMobSpawnWithEquipment.tooltip"))
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.replaceEntityCopperArmourProbability"),
                (ProgressionModConfig.config.mobChanges.replaceEntityCopperArmourProbability * 100f).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                ProgressionModConfig.config.mobChanges.replaceEntityCopperArmourProbability = (aInt / 100f)
            }
                .setTooltip(getTranslatableTextOption("mobChanges.replaceEntityCopperArmourProbability.tooltip"))
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.entitySpawnWithCopperToolProbability"),
                (ProgressionModConfig.config.mobChanges.entitySpawnWithCopperToolProbability * 100f).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                ProgressionModConfig.config.mobChanges.entitySpawnWithCopperToolProbability = (aInt / 100f)
            }
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("mobChanges.polarBearAlwaysAggressive"),
                ProgressionModConfig.config.mobChanges.polarBearAlwaysAggressive
            ).setSaveConsumer { aBool: Boolean ->
                ProgressionModConfig.config.mobChanges.polarBearAlwaysAggressive = aBool
            }
                .setDefaultValue(true)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("mobChanges.polarBearSpeed"),
                ProgressionModConfig.config.mobChanges.polarBearSpeed
            ).setSaveConsumer { aFloat: Float ->
                ProgressionModConfig.config.mobChanges.polarBearSpeed = aFloat
            }
                .setDefaultValue(0.35f)
                .setTooltip(getTranslatableTextOption("mobChanges.onlyApplyOnNewlySpawnedMob.tooltip"))
                .requireRestart()
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.polarBearRange"),
                ProgressionModConfig.config.mobChanges.polarBearRange,
                1,
                64
            ).setSaveConsumer { aInt: Int ->
                ProgressionModConfig.config.mobChanges.polarBearRange = aInt
            }
                .setDefaultValue(40)
                .setTooltip(getTranslatableTextOption("mobChanges.onlyApplyOnNewlySpawnedMob.tooltip"))
                .requireRestart()
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("mobChanges.mobAttackWeakPlayer"),
                ProgressionModConfig.config.mobChanges.mobAttackWeakPlayer
            )
                .setDefaultValue(true)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("mobChanges.wolfAggressiveAtNight"),
                ProgressionModConfig.config.mobChanges.wolfAggressiveAtNight
            )
                .setDefaultValue(true)
                .build()
        )
    }

    private fun addoreChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("oreChanges"))
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawCopperNuggetDrop"),
                ProgressionModConfig.config.oreChanges.rawCopperNuggetDrop,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> ProgressionModConfig.config.oreChanges.rawCopperNuggetDrop = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawIronNuggetDrop"),
                ProgressionModConfig.config.oreChanges.rawIronNuggetDrop,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> ProgressionModConfig.config.oreChanges.rawIronNuggetDrop = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawGoldNuggetDrop"),
                ProgressionModConfig.config.oreChanges.rawGoldNuggetDrop,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> ProgressionModConfig.config.oreChanges.rawGoldNuggetDrop = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawDiamondFragmentDrop"),
                ProgressionModConfig.config.oreChanges.diamondFragmentDrop,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> ProgressionModConfig.config.oreChanges.diamondFragmentDrop = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("oreChanges.oreToStone"),
                ProgressionModConfig.config.oreChanges.oreToStone
            )
                .setSaveConsumer { aBoolean: Boolean ->
                    ProgressionModConfig.config.oreChanges.oreToStone = aBoolean
                }
                .setTooltip(getTranslatableTextOption("oreChanges.oreToStone.tooltip"))
                .setDefaultValue(false)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.coalDustExplosionClusterSize"),
                ProgressionModConfig.config.oreChanges.coalDustExplosionClusterSize,
                5,
                50
            )
                .setSaveConsumer { aInt: Int ->
                    ProgressionModConfig.config.oreChanges.coalDustExplosionClusterSize = aInt
                }
                .setDefaultValue(6)
                .build()
        )
    }

    private fun addBlockChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("blockChanges"))
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.stonecutterDamage"),
                ProgressionModConfig.config.blockChanges.stonecutterDamage,
            ).setSaveConsumer { aFloat: Float -> ProgressionModConfig.config.blockChanges.stonecutterDamage = aFloat }
                .setDefaultValue(3f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.amethystFallDamageMultiplier"),
                ProgressionModConfig.config.blockChanges.amethystFallDamageMultiplier,
            ).setSaveConsumer { aFloat: Float ->
                ProgressionModConfig.config.blockChanges.amethystFallDamageMultiplier = aFloat
            }
                .setDefaultValue(1.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.cookerDamage"),
                ProgressionModConfig.config.blockChanges.cookerDamage,
            ).setSaveConsumer { aFloat: Float ->
                ProgressionModConfig.config.blockChanges.cookerDamage = aFloat
            }
                .setDefaultValue(0.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.brickFurnaceDamage"),
                ProgressionModConfig.config.blockChanges.brickFurnaceDamage,
            ).setSaveConsumer { aFloat: Float ->
                ProgressionModConfig.config.blockChanges.brickFurnaceDamage = aFloat
            }
                .setDefaultValue(1f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.furnaceDamage"),
                ProgressionModConfig.config.blockChanges.furnaceDamage,
            ).setSaveConsumer { aFloat: Float ->
                ProgressionModConfig.config.blockChanges.furnaceDamage = aFloat
            }
                .setDefaultValue(1.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.blastFurnaceDamage"),
                ProgressionModConfig.config.blockChanges.blastFurnaceDamage,
            ).setSaveConsumer { aFloat: Float ->
                ProgressionModConfig.config.blockChanges.blastFurnaceDamage = aFloat
            }
                .setDefaultValue(2.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )

        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("blockChanges.blastFurnaceSetNearbyBlockOnFire"),
                ProgressionModConfig.config.blockChanges.blastFurnaceSetNearbyBlockOnFire
            )
                .setSaveConsumer { aBoolean: Boolean ->
                    ProgressionModConfig.config.blockChanges.blastFurnaceSetNearbyBlockOnFire = aBoolean
                }
                .setDefaultValue(true)
                .build()
        )

        category.addEntry(
            entryBuilder.startEnumSelector(
                getTranslatableTextOption("blockChanges.fixThinBlockStepSound"),
                ProgressionModConfig.BlockChanges.FixThinBlockStepSound::class.java,
                ProgressionModConfig.config.blockChanges.fixThinBlockStepSound
            ).setSaveConsumer { enum: ProgressionModConfig.BlockChanges.FixThinBlockStepSound ->
                ProgressionModConfig.config.blockChanges.fixThinBlockStepSound = enum
            }
                .setDefaultValue(ProgressionModConfig.BlockChanges.FixThinBlockStepSound.True)
                .build()
        )
    }
}