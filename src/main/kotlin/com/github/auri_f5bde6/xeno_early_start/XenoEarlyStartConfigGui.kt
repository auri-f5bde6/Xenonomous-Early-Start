package com.github.auri_f5bde6.xeno_early_start

import com.github.auri_f5bde6.xeno_early_start.config.ConfigWrapper
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraftforge.fml.loading.FMLPaths

object XenoEarlyStartConfigGui {
    val configBuilder: ConfigBuilder
        get() {
            val config = ConfigWrapper(XenoEarlyStart.MODID)
            addOreChangesEntries(config)
            addEarlyGameEntries(config)
            addMobChangesEntries(config)
            addBlockChangesEntries(config)
            addHungerChangesEntries(config)
            addClientEntries(config)
            config.configBuilder.setSavingRunnable {
                val tomlWriter = TomlWriter()
                tomlWriter.write(
                    XenoEarlyStartConfig.config,
                    FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
                )
            }
            return config.configBuilder
        }

    private fun addEarlyGameEntries(config: ConfigWrapper) {
        val category = config.newCategory("earlyGameChanges")

        category.addPercentageSlider(
            "plantFiberDropProbability",
            XenoEarlyStartConfig.config.earlyGameChanges::plantFiberDropProbability,
            0.05f
        )

        category.addBooleanToggle(
            "overridePebbleDropProbability",
            XenoEarlyStartConfig.config.earlyGameChanges::overridePebbleDropProbability,
            false
        )

        category.addPercentageSlider(
            "pebbleDropProbability",
            XenoEarlyStartConfig.config.earlyGameChanges::pebbleDropProbability,
            0.4f
        )
        val primitiveFireCategory = category.newSubCategory("primitiveFire", expanded = true)
        primitiveFireCategory.addPercentageSlider(
            "percentageRequiredForMaxBrightness",
            XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::percentageRequiredForMaxBrightness,
            0.25f
        )
        primitiveFireCategory.addIntSlider(
            "maxBurnTime",
            XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::maxBurnTime,
            5 * 60 * 20,
            1 * 60 * 60,
            60 * 60 * 20,
            { i: Int -> i / 60 / 20 },
            { i: Int -> i * 60 * 20 },
            tooltip = ConfigWrapper.DefaultTooltip()
        )
        primitiveFireCategory.addPercentageSlider(
            "fuelTimeMultiplier",
            XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::fuelTimeMultiplier,
            1.0f,
            1.0f,
            2.0f
        )
        category.addSubCategory(primitiveFireCategory)
    }

    private fun addMobChangesEntries(config: ConfigWrapper) {
        val category = config.newCategory("mobChanges")
        category.addPercentageSlider(
            "flatAdditiveMobSpawnWithEquipment",
            XenoEarlyStartConfig.config.mobChanges::flatAdditiveMobSpawnWithEquipment,
            0.05f
        )
        category.addPercentageSlider(
            "replaceEntityCopperArmourProbability",
            XenoEarlyStartConfig.config.mobChanges::replaceEntityCopperArmourProbability,
            0.05f,
            tooltip = ConfigWrapper.DefaultTooltip()
        )
        category.addPercentageSlider(
            "entitySpawnWithCopperToolProbability",
            XenoEarlyStartConfig.config.mobChanges::entitySpawnWithCopperToolProbability,
            0.05f,
        )
        category.addBooleanToggle(
            "polarBearAlwaysAggressive",
            XenoEarlyStartConfig.config.mobChanges::polarBearAlwaysAggressive,
            true
        )
        category.addFloatField(
            "polarBearSpeed",
            XenoEarlyStartConfig.config.mobChanges::polarBearSpeed,
            0.35f,
            tooltip = ConfigWrapper.CustomTooltip("onlyApplyOnNewlySpawnedMob"),
            requireRestart = true,
        )
        category.addIntSlider(
            "polarBearRange",
            XenoEarlyStartConfig.config.mobChanges::polarBearRange,
            40,
            1,
            64,
            tooltip = ConfigWrapper.CustomTooltip("onlyApplyOnNewlySpawnedMob"),
            requireRestart = true,
        )
        category.addBooleanToggle(
            "mobAttackWeakPlayer",
            XenoEarlyStartConfig.config.mobChanges::mobAttackWeakPlayer,
            true
        )
        category.addBooleanToggle(
            "wolfAggressiveAtNight",
            XenoEarlyStartConfig.config.mobChanges::wolfAggressiveAtNight,
            true
        )
        category.addBooleanToggle(
            "batGivesPlayerNausea",
            XenoEarlyStartConfig.config.mobChanges::batGivesPlayerNausea,
            true
        )
    }

    private fun addOreChangesEntries(config: ConfigWrapper) {
        val category = config.newCategory("oreChanges")
        category.addIntSlider(
            "rawCopperNuggetDropMin",
            XenoEarlyStartConfig.config.oreChanges::rawCopperNuggetDropMin,
            1,
            1,
            9
        )
        category.addIntSlider(
            "rawCopperNuggetDropMax",
            XenoEarlyStartConfig.config.oreChanges::rawCopperNuggetDropMax,
            3,
            1,
            9
        )
        category.addIntSlider(
            "rawIronNuggetDrop",
            XenoEarlyStartConfig.config.oreChanges::rawIronNuggetDrop,
            1,
            1,
            9
        )
        category.addIntSlider(
            "rawGoldNuggetDrop",
            XenoEarlyStartConfig.config.oreChanges::rawGoldNuggetDrop,
            1,
            1,
            9
        )
        category.addIntSlider(
            "rawDiamondFragmentDrop",
            XenoEarlyStartConfig.config.oreChanges::diamondFragmentDrop,
            1,
            1,
            9
        )
        category.addBooleanToggle(
            "oreToStone",
            XenoEarlyStartConfig.config.oreChanges::oreToStone,
            false,
            tooltip = ConfigWrapper.DefaultTooltip()
        )
        category.addIntSlider(
            "coalDustExplosionClusterSize",
            XenoEarlyStartConfig.config.oreChanges::coalDustExplosionClusterSize,
            6,
            5,
            50
        )
        category.addIntSlider(
            "coalDustExplosionBlockLimit",
            XenoEarlyStartConfig.config.oreChanges::coalDustExplosionBlockLimit,
            256,
            10,
            1024
        )
    }

    private fun addBlockChangesEntries(config: ConfigWrapper) {
        val category = config.newCategory("blockChanges")
        category.addFloatField(
            "stonecutterDamage",
            XenoEarlyStartConfig.config.blockChanges::stonecutterDamage,
            3f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "amethystFallDamageMultiplier",
            XenoEarlyStartConfig.config.blockChanges::amethystFallDamageMultiplier,
            1.5f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "cookerDamage",
            XenoEarlyStartConfig.config.blockChanges::cookerDamage,
            0.5f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "brickFurnaceDamage",
            XenoEarlyStartConfig.config.blockChanges::brickFurnaceDamage,
            1f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "furnaceDamage",
            XenoEarlyStartConfig.config.blockChanges::furnaceDamage,
            1.5f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "blastFurnaceDamage",
            XenoEarlyStartConfig.config.blockChanges::blastFurnaceDamage,
            2.5f,
            Pair(0f, 5f)
        )
        category.addBooleanToggle(
            "blastFurnaceSetNearbyBlockOnFire",
            XenoEarlyStartConfig.config.blockChanges::blastFurnaceSetNearbyBlockOnFire,
            true
        )
        category.addEnumSelector(
            "fixThinBlockStepSound",
            XenoEarlyStartConfig.config.blockChanges::fixThinBlockStepSound,
            XenoEarlyStartConfig.BlockChanges.FixThinBlockStepSound::class.java,
            XenoEarlyStartConfig.BlockChanges.FixThinBlockStepSound.True
        )
    }

    private fun addHungerChangesEntries(config: ConfigWrapper) {
        val category = config.newCategory("hungerChanges")
        category.addIntSlider(
            "wakingUpExhaustion",
            XenoEarlyStartConfig.config.hungerChanges::wakingUpExhaustion,
            40f,
            0f,
            40f,
            { f: Float -> f.toInt() },
            { i: Int -> i.toFloat() }
        )
        category.addFloatField(
            "boatRowingExhaustion",
            XenoEarlyStartConfig.config.hungerChanges::boatRowingExhaustion,
            0.05f,
            Pair(0f, 40f)
        )
    }

    private fun addClientEntries(config: ConfigWrapper) {
        val category = config.newCategory("client")
        val tooltips = category.newSubCategory("tooltips")
        val tutorialTooltips = ConfigWrapper.CustomTooltip("tutorialTooltips")
        tooltips.addBooleanToggle(
            "disableAllTooltip",
            XenoEarlyStartConfig.config.client.tooltips::disableAllTooltips,
            false,
            tooltip = tutorialTooltips
        )
        tooltips.addBooleanToggle(
            "disableFoodWarningTooltips",
            XenoEarlyStartConfig.config.client.tooltips::disableFoodWarningTooltips,
            false
        )
        tooltips.addBooleanToggle(
            "disableTutorialTooltips",
            XenoEarlyStartConfig.config.client.tooltips::disableTutorialTooltips,
            false,
            tooltip = tutorialTooltips
        )
        tooltips.addBooleanToggle(
            "disableItemDescriptionTooltips",
            XenoEarlyStartConfig.config.client.tooltips::disableItemDescriptionTooltips,
            false,
            tooltip = tutorialTooltips
        )
        category.addSubCategory(tooltips)
    }
}