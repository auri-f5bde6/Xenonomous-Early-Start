package com.github.auri_f5bde6.xeno_early_start

import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.text.Text
import net.minecraftforge.fml.loading.FMLPaths

object XenoEarlyStartConfigGui {
    private fun getTranslatableText(name: String?): Text {
        return Text.translatable("text.config.${XenoEarlyStart.MODID}.$name")
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
            addOreChangesEntries(builder, entryBuilder)
            addEarlyGameEntries(builder, entryBuilder)
            addMobChangesEntries(builder, entryBuilder)
            addBlockChangesEntries(builder, entryBuilder)
            builder.setSavingRunnable {
                val tomlWriter = TomlWriter()
                tomlWriter.write(
                    XenoEarlyStartConfig.config,
                    FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
                )
            }
            return builder
        }

    private fun addEarlyGameEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("earlyGameChanges"))
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("earlyGameChanges.plantFiberDropProbability"),
                (XenoEarlyStartConfig.config.earlyGameChanges.plantFiberDropProbability * 100).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.earlyGameChanges.plantFiberDropProbability = aInt / 100.0f
            }
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("earlyGameChanges.overridePebbleDropProbability"),
                XenoEarlyStartConfig.config.earlyGameChanges.overridePebbleDropProbability
            ).setSaveConsumer { aBoolean: Boolean ->
                XenoEarlyStartConfig.config.earlyGameChanges.overridePebbleDropProbability = aBoolean
            }
                .setDefaultValue(false)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("earlyGameChanges.pebbleDropProbability"),
                XenoEarlyStartConfig.config.earlyGameChanges.pebbleDropProbability,
                1,
                100
            ).setSaveConsumer { aInt: Int -> XenoEarlyStartConfig.config.earlyGameChanges.pebbleDropProbability = aInt }
                .setDefaultValue(40)
                .build()
        )
        val primitiveFireCategory = entryBuilder
            .startSubCategory(getTranslatableTextOption("earlyGameChanges.primitiveFire"))
            .setExpanded(true)
        primitiveFireCategory.add(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness"),
                (XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness * 100).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.percentageRequiredForMaxBrightness =
                    aInt / 100f
            }
                .setDefaultValue(25)
                .build()
        )
        primitiveFireCategory.add(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("earlyGameChanges.primitiveFire.maxBurnTime"),
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime / 60 / 20,
                1,
                60,
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.maxBurnTime = aInt * 60 * 20
            }
                .setTooltip(getTranslatableTextOption("earlyGameChanges.primitiveFire.maxBurnTime.tooltip"))
                .setDefaultValue(5)
                .build()
        )
        primitiveFireCategory.add(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("earlyGameChanges.primitiveFire.fuelTimeMultiplier"),
                (XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.fuelTimeMultiplier * 100).toInt(),
                1,
                200
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire.fuelTimeMultiplier = aInt / 100f
            }
                .setDefaultValue(100)
                .build()
        )
        category.addEntry(primitiveFireCategory.build())
    }

    private fun addMobChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("mobChanges"))
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.flatAdditiveMobSpawnWithEquipment"),
                (XenoEarlyStartConfig.config.mobChanges.flatAdditiveMobSpawnWithEquipment * 100f).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.mobChanges.flatAdditiveMobSpawnWithEquipment = (aInt / 100f)
            }
                .setTooltip(getTranslatableTextOption("mobChanges.flatAdditiveMobSpawnWithEquipment.tooltip"))
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.replaceEntityCopperArmourProbability"),
                (XenoEarlyStartConfig.config.mobChanges.replaceEntityCopperArmourProbability * 100f).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.mobChanges.replaceEntityCopperArmourProbability = (aInt / 100f)
            }
                .setTooltip(getTranslatableTextOption("mobChanges.replaceEntityCopperArmourProbability.tooltip"))
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.entitySpawnWithCopperToolProbability"),
                (XenoEarlyStartConfig.config.mobChanges.entitySpawnWithCopperToolProbability * 100f).toInt(),
                1,
                100
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.mobChanges.entitySpawnWithCopperToolProbability = (aInt / 100f)
            }
                .setDefaultValue(5)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("mobChanges.polarBearAlwaysAggressive"),
                XenoEarlyStartConfig.config.mobChanges.polarBearAlwaysAggressive
            ).setSaveConsumer { aBool: Boolean ->
                XenoEarlyStartConfig.config.mobChanges.polarBearAlwaysAggressive = aBool
            }
                .setDefaultValue(true)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("mobChanges.polarBearSpeed"),
                XenoEarlyStartConfig.config.mobChanges.polarBearSpeed
            ).setSaveConsumer { aFloat: Float ->
                XenoEarlyStartConfig.config.mobChanges.polarBearSpeed = aFloat
            }
                .setDefaultValue(0.35f)
                .setTooltip(getTranslatableTextOption("mobChanges.onlyApplyOnNewlySpawnedMob.tooltip"))
                .requireRestart()
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("mobChanges.polarBearRange"),
                XenoEarlyStartConfig.config.mobChanges.polarBearRange,
                1,
                64
            ).setSaveConsumer { aInt: Int ->
                XenoEarlyStartConfig.config.mobChanges.polarBearRange = aInt
            }
                .setDefaultValue(40)
                .setTooltip(getTranslatableTextOption("mobChanges.onlyApplyOnNewlySpawnedMob.tooltip"))
                .requireRestart()
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("mobChanges.mobAttackWeakPlayer"),
                XenoEarlyStartConfig.config.mobChanges.mobAttackWeakPlayer
            ).setSaveConsumer { aBool: Boolean ->
                XenoEarlyStartConfig.config.mobChanges.mobAttackWeakPlayer = aBool
            }
                .setDefaultValue(true)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("mobChanges.wolfAggressiveAtNight"),
                XenoEarlyStartConfig.config.mobChanges.wolfAggressiveAtNight
            ).setSaveConsumer { aBool: Boolean ->
                XenoEarlyStartConfig.config.mobChanges.wolfAggressiveAtNight = aBool
            }
                .setDefaultValue(true)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("mobChanges.batGivesPlayerNausea"),
                XenoEarlyStartConfig.config.mobChanges.batGivesPlayerNausea
            ).setSaveConsumer { aBool: Boolean ->
                XenoEarlyStartConfig.config.mobChanges.batGivesPlayerNausea = aBool
            }
                .setDefaultValue(true)
                .build()
        )
    }

    private fun addOreChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("oreChanges"))
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawCopperNuggetDropMin"),
                XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMin,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMin = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawCopperNuggetDropMax"),
                XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMax,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> XenoEarlyStartConfig.config.oreChanges.rawCopperNuggetDropMax = aInt }
                .setDefaultValue(3)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawIronNuggetDrop"),
                XenoEarlyStartConfig.config.oreChanges.rawIronNuggetDrop,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> XenoEarlyStartConfig.config.oreChanges.rawIronNuggetDrop = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawGoldNuggetDrop"),
                XenoEarlyStartConfig.config.oreChanges.rawGoldNuggetDrop,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> XenoEarlyStartConfig.config.oreChanges.rawGoldNuggetDrop = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.rawDiamondFragmentDrop"),
                XenoEarlyStartConfig.config.oreChanges.diamondFragmentDrop,
                1,
                9
            )
                .setSaveConsumer { aInt: Int -> XenoEarlyStartConfig.config.oreChanges.diamondFragmentDrop = aInt }
                .setDefaultValue(1)
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("oreChanges.oreToStone"),
                XenoEarlyStartConfig.config.oreChanges.oreToStone
            )
                .setSaveConsumer { aBoolean: Boolean ->
                    XenoEarlyStartConfig.config.oreChanges.oreToStone = aBoolean
                }
                .setTooltip(getTranslatableTextOption("oreChanges.oreToStone.tooltip"))
                .setDefaultValue(false)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.coalDustExplosionClusterSize"),
                XenoEarlyStartConfig.config.oreChanges.coalDustExplosionClusterSize,
                5,
                50
            )
                .setSaveConsumer { aInt: Int ->
                    XenoEarlyStartConfig.config.oreChanges.coalDustExplosionClusterSize = aInt
                }
                .setDefaultValue(6)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(
                getTranslatableTextOption("oreChanges.coalDustExplosionBlockLimit"),
                XenoEarlyStartConfig.config.oreChanges.coalDustExplosionBlockLimit,
                10,
                1024
            )
                .setSaveConsumer { aInt: Int ->
                    XenoEarlyStartConfig.config.oreChanges.coalDustExplosionBlockLimit = aInt
                }
                .setDefaultValue(256)
                .build()
        )
    }

    private fun addBlockChangesEntries(configBuilder: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = configBuilder.getOrCreateCategory(getTranslatableTextOption("blockChanges"))
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.stonecutterDamage"),
                XenoEarlyStartConfig.config.blockChanges.stonecutterDamage,
            ).setSaveConsumer { aFloat: Float -> XenoEarlyStartConfig.config.blockChanges.stonecutterDamage = aFloat }
                .setDefaultValue(3f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.amethystFallDamageMultiplier"),
                XenoEarlyStartConfig.config.blockChanges.amethystFallDamageMultiplier,
            ).setSaveConsumer { aFloat: Float ->
                XenoEarlyStartConfig.config.blockChanges.amethystFallDamageMultiplier = aFloat
            }
                .setDefaultValue(1.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.cookerDamage"),
                XenoEarlyStartConfig.config.blockChanges.cookerDamage,
            ).setSaveConsumer { aFloat: Float ->
                XenoEarlyStartConfig.config.blockChanges.cookerDamage = aFloat
            }
                .setDefaultValue(0.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.brickFurnaceDamage"),
                XenoEarlyStartConfig.config.blockChanges.brickFurnaceDamage,
            ).setSaveConsumer { aFloat: Float ->
                XenoEarlyStartConfig.config.blockChanges.brickFurnaceDamage = aFloat
            }
                .setDefaultValue(1f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.furnaceDamage"),
                XenoEarlyStartConfig.config.blockChanges.furnaceDamage,
            ).setSaveConsumer { aFloat: Float ->
                XenoEarlyStartConfig.config.blockChanges.furnaceDamage = aFloat
            }
                .setDefaultValue(1.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )
        category.addEntry(
            entryBuilder.startFloatField(
                getTranslatableTextOption("blockChanges.blastFurnaceDamage"),
                XenoEarlyStartConfig.config.blockChanges.blastFurnaceDamage,
            ).setSaveConsumer { aFloat: Float ->
                XenoEarlyStartConfig.config.blockChanges.blastFurnaceDamage = aFloat
            }
                .setDefaultValue(2.5f)
                .setMin(0f)
                .setMax(5f)
                .build()
        )

        category.addEntry(
            entryBuilder.startBooleanToggle(
                getTranslatableTextOption("blockChanges.blastFurnaceSetNearbyBlockOnFire"),
                XenoEarlyStartConfig.config.blockChanges.blastFurnaceSetNearbyBlockOnFire
            )
                .setSaveConsumer { aBoolean: Boolean ->
                    XenoEarlyStartConfig.config.blockChanges.blastFurnaceSetNearbyBlockOnFire = aBoolean
                }
                .setDefaultValue(true)
                .build()
        )

        category.addEntry(
            entryBuilder.startEnumSelector(
                getTranslatableTextOption("blockChanges.fixThinBlockStepSound"),
                XenoEarlyStartConfig.BlockChanges.FixThinBlockStepSound::class.java,
                XenoEarlyStartConfig.config.blockChanges.fixThinBlockStepSound
            ).setSaveConsumer { enum: XenoEarlyStartConfig.BlockChanges.FixThinBlockStepSound ->
                XenoEarlyStartConfig.config.blockChanges.fixThinBlockStepSound = enum
            }
                .setDefaultValue(XenoEarlyStartConfig.BlockChanges.FixThinBlockStepSound.True)
                .build()
        )
    }
}