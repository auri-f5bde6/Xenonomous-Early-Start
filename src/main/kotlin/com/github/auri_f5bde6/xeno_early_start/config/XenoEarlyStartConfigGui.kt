package com.github.auri_f5bde6.xeno_early_start.config

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.block.RawBrickBlock
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraftforge.fml.loading.FMLPaths

object XenoEarlyStartConfigGui {
    val configBuilder: ConfigBuilder
        get() {
            val config = ConfigWrapper(XenoEarlyStart.MODID)
            addConfigs(config)
            config.configBuilder.setSavingRunnable {
                val tomlWriter = TomlWriter()
                tomlWriter.write(
                    XenoEarlyStartConfig.config,
                    FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
                )
            }
            return config.configBuilder
        }

    private fun addConfigs(config: ConfigWrapper) {
        if (!XenoEarlyStartConfig.config.disableNonClientConfig) {
            config.newCategory("general") { category ->
                category.addSubCategory("ores", ::addOreChangesEntries)
                category.addSubCategory("mobs", ::addMobChangesEntries)
                category.addSubCategory("gameplay", ::addGameplayEntries)
                category.addSubCategory("blocks", ::addBlockChangesEntries)
                category.addBooleanToggle(
                    "disableNonClientConfig",
                    XenoEarlyStartConfig.config::disableNonClientConfig,
                    false,
                    tooltip = ConfigWrapper.DefaultTooltip()
                )
            }
        }
        config.newCategory("client", ::addClientEntries)
    }

    private fun addOreChangesEntries(category: ConfigWrapper.Category) {
        category.addSubCategory("copper") { copper ->
            copper.addBooleanToggle(
                "vanillaCopperLootTable",
                XenoEarlyStartConfig.config.oreChanges::vanillaCopperLootTable,
                false
            )
            copper.addIntSlider(
                "rawCopperNuggetDropMin",
                XenoEarlyStartConfig.config.oreChanges::rawCopperNuggetDropMin,
                1,
                1,
                9
            )
            copper.addIntSlider(
                "rawCopperNuggetDropMax",
                XenoEarlyStartConfig.config.oreChanges::rawCopperNuggetDropMax,
                3,
                1,
                9
            )
        }

        category.addSubCategory("iron") { iron ->
            iron.addBooleanToggle(
                "vanillaIronLootTable",
                XenoEarlyStartConfig.config.oreChanges::vanillaIronLootTable,
                false
            )
            iron.addIntSlider(
                "rawIronNuggetDrop",
                XenoEarlyStartConfig.config.oreChanges::rawIronNuggetDrop,
                1,
                1,
                9
            )
        }

        category.addSubCategory("gold") { gold ->
            gold.addBooleanToggle(
                "vanillaGoldLootTable",
                XenoEarlyStartConfig.config.oreChanges::vanillaGoldLootTable,
                false
            )
            gold.addIntSlider(
                "rawGoldNuggetDrop",
                XenoEarlyStartConfig.config.oreChanges::rawGoldNuggetDrop,
                1,
                1,
                9
            )
        }

        category.addSubCategory("diamond") { diamond ->
            diamond.addBooleanToggle(
                "vanillaDiamondLootTable",
                XenoEarlyStartConfig.config.oreChanges::vanillaDiamondLootTable,
                false
            )
            diamond.addIntSlider(
                "rawDiamondFragmentDrop",
                XenoEarlyStartConfig.config.oreChanges::diamondFragmentDrop,
                1,
                1,
                9
            )
            diamond.addIntSlider(
                "goldenPickDiamondFragmentBuff",
                XenoEarlyStartConfig.config.oreChanges::goldenPickDiamondFragmentBuff,
                2,
                1,
                9
            )
        }

        category.addSubCategory("coalDust") { coalDust ->
            coalDust.addIntSlider(
                "coalDustExplosionClusterSize",
                XenoEarlyStartConfig.config.oreChanges::coalDustExplosionClusterSize,
                6,
                5,
                50
            )
            coalDust.addIntSlider(
                "coalDustExplosionBlockLimit",
                XenoEarlyStartConfig.config.oreChanges::coalDustExplosionBlockLimit,
                256,
                10,
                1024
            )
        }

        category.addSubCategory("misc") { misc ->
            misc.addBooleanToggle(
                "oreToStone",
                XenoEarlyStartConfig.config.oreChanges::oreToStone,
                false,
                tooltip = ConfigWrapper.DefaultTooltip()
            )
        }
    }

    private fun addMobChangesEntries(category: ConfigWrapper.Category) {
        category.addSubCategory("vanilla") { vanilla ->
            vanilla.addPercentageSlider(
                "flatAdditiveMobSpawnWithEquipment",
                XenoEarlyStartConfig.config.mobChanges::flatAdditiveMobSpawnWithEquipment,
                0.05f
            )
            vanilla.addPercentageSlider(
                "replaceEntityCopperArmourProbability",
                XenoEarlyStartConfig.config.mobChanges::replaceEntityCopperArmourProbability,
                0.05f,
                tooltip = ConfigWrapper.DefaultTooltip()
            )
            vanilla.addPercentageSlider(
                "entitySpawnWithCopperToolProbability",
                XenoEarlyStartConfig.config.mobChanges::entitySpawnWithCopperToolProbability,
                0.05f,
            )
            vanilla.addBooleanToggle(
                "polarBearAlwaysAggressive",
                XenoEarlyStartConfig.config.mobChanges::polarBearAlwaysAggressive,
                true
            )
            vanilla.addFloatField(
                "polarBearSpeed",
                XenoEarlyStartConfig.config.mobChanges::polarBearSpeed,
                0.35f,
                tooltip = ConfigWrapper.CustomTooltip("onlyApplyOnNewlySpawnedMob"),
                requireRestart = true,
            )
            vanilla.addIntSlider(
                "polarBearRange",
                XenoEarlyStartConfig.config.mobChanges::polarBearRange,
                40,
                1,
                64,
                tooltip = ConfigWrapper.CustomTooltip("onlyApplyOnNewlySpawnedMob"),
                requireRestart = true,
            )
            vanilla.addBooleanToggle(
                "mobAttackWeakPlayer",
                XenoEarlyStartConfig.config.mobChanges::mobAttackWeakPlayer,
                true
            )
            vanilla.addBooleanToggle(
                "wolfAggressiveAtNight",
                XenoEarlyStartConfig.config.mobChanges::wolfAggressiveAtNight,
                true
            )
            vanilla.addBooleanToggle(
                "batGivesPlayerNausea",
                XenoEarlyStartConfig.config.mobChanges::batGivesPlayerNausea,
                true
            )
        }
        category.addSubCategory("xeno_early_start") { xenoEarlyStart ->
            xenoEarlyStart.addBooleanToggle(
                "prowlerCanSpawn",
                XenoEarlyStartConfig.config.mobChanges::prowlerCanSpawn,
                true
            )
        }
    }

    private fun addGameplayEntries(category: ConfigWrapper.Category) {
        category.addBooleanToggle(
            "stationsUnusableUntilFirstCraft",
            XenoEarlyStartConfig.config.earlyGameChanges::stationsUnusableUntilFirstCraft,
            true
        )

        category.addBooleanToggle(
            "removePickaxeFromAllLootTable",
            XenoEarlyStartConfig.config.earlyGameChanges::removePickaxeFromAllLootTable,
            true
        )

        category.addSubCategory("rawBrick") { rawBrick ->
            rawBrick.addIntSlider(
                "rawBrickDryingLength",
                XenoEarlyStartConfig.config.earlyGameChanges::rawBrickDryingLength,
                9,
                0,
                RawBrickBlock.MAX_DRY_LEVEL,
                tooltip = ConfigWrapper.DefaultTooltip()
            )
        }
        category.addSubCategory("dropRate") { dropRate ->
            dropRate.addPercentageSlider(
                "plantFiberDropProbability",
                XenoEarlyStartConfig.config.earlyGameChanges::plantFiberDropProbability,
                0.05f
            )

            dropRate.addBooleanToggle(
                "overridePebbleDropProbability",
                XenoEarlyStartConfig.config.earlyGameChanges::overridePebbleDropProbability,
                false
            )

            dropRate.addPercentageSlider(
                "pebbleDropProbability",
                XenoEarlyStartConfig.config.earlyGameChanges::pebbleDropProbability,
                0.4f
            )
        }

        category.addSubCategory("primitiveFire") { primitiveFire ->
            val inMinuteTooltip = ConfigWrapper.CustomTooltip("inMinute")
            primitiveFire.addPercentageSlider(
                "percentageRequiredForMaxBrightness",
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::percentageRequiredForMaxBrightness,
                0.25f
            )
            primitiveFire.addSlider(
                "maxBurnTime",
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::maxBurnTime,
                5 * 60 * 20,
                1 * 60 * 60,
                60 * 60 * 20,
                { i: Int -> i / 60 / 20 },
                { i: Int -> i * 60 * 20 },
                tooltip = inMinuteTooltip
            )
            primitiveFire.addPercentageSlider(
                "fuelTimeMultiplier",
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::fuelTimeMultiplier,
                1.0f,
                1.0f,
                2.0f
            )
            primitiveFire.addSlider(
                "fuelStarterRelightFuelTime",
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::fuelStarterRelightFuelTime,
                3 * 60 * 20,
                1 * 60 * 60,
                60 * 60 * 20,
                { i: Int -> i / 60 / 20 },
                { i: Int -> i * 60 * 20 },
                tooltip = inMinuteTooltip
            )
            primitiveFire.addPercentageSlider(
                "cookingTimeMultiplier",
                XenoEarlyStartConfig.config.earlyGameChanges.primitiveFire::cookingTimeMultiplier,
                2f,
                1f,
                4f
            )
        }
        category.addSubCategory("brickFurnace") { brickFurnace ->
            brickFurnace.addPercentageSlider(
                "cookingTimeMultiplier",
                XenoEarlyStartConfig.config.earlyGameChanges::brickFurnaceCookingTimeMultiplier,
                2f,
                3f,
                4f
            )
        }
        category.addSubCategory("hunger") { hunger ->
            hunger.addSlider(
                "wakingUpExhaustion",
                XenoEarlyStartConfig.config.hungerChanges::wakingUpExhaustion,
                40f,
                0f,
                40f,
                { f: Float -> f.toInt() },
                { i: Int -> i.toFloat() }
            )
            hunger.addFloatField(
                "boatRowingExhaustion",
                XenoEarlyStartConfig.config.hungerChanges::boatRowingExhaustion,
                0.05f,
                Pair(0f, 40f)
            )
        }
        category.addSubCategory("recipe") { recipe ->
            recipe.addBooleanToggle(
                "harderBrickFurnaceRecipe",
                XenoEarlyStartConfig.config.earlyGameChanges.recipes::harderBrickFurnaceRecipe,
                true,
                requireRestart = true
            )
            recipe.addBooleanToggle(
                "vanillaFurnaceRecipe",
                XenoEarlyStartConfig.config.earlyGameChanges.recipes::vanillaFurnaceRecipe,
                false,
                requireRestart = true
            )
            recipe.addBooleanToggle(
                "vanillaCraftingTableRecipe",
                XenoEarlyStartConfig.config.earlyGameChanges.recipes::vanillaCraftingTableRecipe,
                false,
                requireRestart = true
            )
            recipe.addBooleanToggle(
                "vanillaStoneToolRecipe",
                XenoEarlyStartConfig.config.earlyGameChanges.recipes::vanillaStoneToolRecipe,
                false,
                requireRestart = true
            )

        }
    }


    private fun addBlockChangesEntries(category: ConfigWrapper.Category) {
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

    private fun addClientEntries(category: ConfigWrapper.Category) {
        category.addSubCategory("tooltips", expanded = true) { tooltips ->
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
        }

    }
}