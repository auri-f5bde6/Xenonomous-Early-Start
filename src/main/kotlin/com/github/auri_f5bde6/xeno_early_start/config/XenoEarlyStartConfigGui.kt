package com.github.auri_f5bde6.xeno_early_start.config

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.block.RawBrickBlock
import com.github.auri_f5bde6.xeno_early_start.config.wrapper.Category
import com.github.auri_f5bde6.xeno_early_start.config.wrapper.ConfigWrapper
import com.github.auri_f5bde6.xeno_early_start.config.wrapper.TooltipText
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraftforge.fml.loading.FMLPaths

object XenoEarlyStartConfigGui {

    fun getConfigBuilder(mc: MinecraftClient, prevScreen: Screen): ConfigBuilder {
        val connectedToServer = prevScreen.minecraft.currentServerEntry != null
        val config = XenoEarlyStartConfig.config
        val configWrapper = ConfigWrapper(XenoEarlyStart.MODID)

        if (!config.disableNonClientConfig) {
            configWrapper.newCategory("general", config, ::addGeneralConfigs)
        }
        if (connectedToServer) {
            // todo: receive the data from server
            configWrapper.newCategory("general", "dedicated_server", XenoEarlyStartConfig.Config(), ::addGeneralConfigs)
        }
        configWrapper.newCategory("client", config, ::addClientEntries)
        configWrapper.configBuilder.setSavingRunnable {
            val tomlWriter = TomlWriter()
            tomlWriter.write(
                config,
                FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
            )
            if (connectedToServer) {
                // todo: send the config over to server
            }
        }
        return configWrapper.configBuilder
    }

    private fun addGeneralConfigs(category: Category, config: XenoEarlyStartConfig.Config) {
        category.addSubCategory("ores", config, ::addOreChangesEntries)
        category.addSubCategory("mobs", config, ::addMobChangesEntries)
        category.addSubCategory("gameplay", config, ::addGameplayEntries)
        category.addSubCategory("blocks", config, ::addBlockChangesEntries)
        category.addBooleanToggle(
            "disableNonClientConfig",
            config::disableNonClientConfig,
            false,
            tooltip = TooltipText.DefaultTooltip()
        )
    }

    private fun addOreChangesEntries(category: Category, config: XenoEarlyStartConfig.Config) {
        category.addSubCategory("copper") { copper ->
            copper.addBooleanToggle(
                "vanillaCopperLootTable",
                config.oreChanges::vanillaCopperLootTable,
                false
            )
            copper.addIntSlider(
                "rawCopperNuggetDropMin",
                config.oreChanges::rawCopperNuggetDropMin,
                1,
                1,
                9
            )
            copper.addIntSlider(
                "rawCopperNuggetDropMax",
                config.oreChanges::rawCopperNuggetDropMax,
                3,
                1,
                9
            )
        }

        category.addSubCategory("iron") { iron ->
            iron.addBooleanToggle(
                "vanillaIronLootTable",
                config.oreChanges::vanillaIronLootTable,
                false
            )
            iron.addIntSlider(
                "rawIronNuggetDrop",
                config.oreChanges::rawIronNuggetDrop,
                1,
                1,
                9
            )
        }

        category.addSubCategory("gold") { gold ->
            gold.addBooleanToggle(
                "vanillaGoldLootTable",
                config.oreChanges::vanillaGoldLootTable,
                false
            )
            gold.addIntSlider(
                "rawGoldNuggetDrop",
                config.oreChanges::rawGoldNuggetDrop,
                1,
                1,
                9
            )
        }

        category.addSubCategory("diamond") { diamond ->
            diamond.addBooleanToggle(
                "vanillaDiamondLootTable",
                config.oreChanges::vanillaDiamondLootTable,
                false
            )
            diamond.addIntSlider(
                "rawDiamondFragmentDrop",
                config.oreChanges::diamondFragmentDrop,
                1,
                1,
                9
            )
            diamond.addIntSlider(
                "goldenPickDiamondFragmentBuff",
                config.oreChanges::goldenPickDiamondFragmentBuff,
                2,
                1,
                9
            )
        }

        category.addSubCategory("coalDust") { coalDust ->
            coalDust.addIntSlider(
                "coalDustExplosionClusterSize",
                config.oreChanges::coalDustExplosionClusterSize,
                6,
                5,
                50
            )
            coalDust.addIntSlider(
                "coalDustExplosionBlockLimit",
                config.oreChanges::coalDustExplosionBlockLimit,
                256,
                10,
                1024
            )
        }

        category.addSubCategory("misc") { misc ->
            misc.addBooleanToggle(
                "oreToStone",
                config.oreChanges::oreToStone,
                false,
                tooltip = TooltipText.DefaultTooltip()
            )
        }
    }

    private fun addMobChangesEntries(category: Category, config: XenoEarlyStartConfig.Config) {
        category.addSubCategory("vanilla") { vanilla ->
            vanilla.addSubCategory("hostileGear") { hostileGear ->
                hostileGear.addPercentageSlider(
                    "flatAdditiveMobSpawnWithEquipment",
                    config.mobChanges::flatAdditiveMobSpawnWithEquipment,
                    0.05f
                )
                hostileGear.addPercentageSlider(
                    "replaceEntityCopperArmourProbability",
                    config.mobChanges::replaceEntityCopperArmourProbability,
                    0.05f,
                    tooltip = TooltipText.DefaultTooltip()
                )
                hostileGear.addPercentageSlider(
                    "entitySpawnWithCopperToolProbability",
                    config.mobChanges::entitySpawnWithCopperToolProbability,
                    0.05f,
                )
            }
            vanilla.addSubCategory("polarBear") { polarBear ->
                polarBear.addBooleanToggle(
                    "polarBearAlwaysAggressive",
                    config.mobChanges::polarBearAlwaysAggressive,
                    true
                )
                polarBear.addFloatField(
                    "polarBearSpeed",
                    config.mobChanges::polarBearSpeed,
                    0.35f,
                    tooltip = TooltipText.CustomTooltip("onlyApplyOnNewlySpawnedMob"),
                    requireRestart = true,
                )
                polarBear.addIntSlider(
                    "polarBearRange",
                    config.mobChanges::polarBearRange,
                    40,
                    1,
                    64,
                    tooltip = TooltipText.CustomTooltip("onlyApplyOnNewlySpawnedMob"),
                    requireRestart = true,
                )
            }
            vanilla.addSubCategory("weakPlayer") { weakPlayer ->
                weakPlayer.addBooleanToggle(
                    "mobAttackWeakPlayer",
                    config.mobChanges::mobAttackWeakPlayer,
                    true
                )
            }
            vanilla.addSubCategory("wolf") { wolf ->
                wolf.addBooleanToggle(
                    "wolfAggressiveAtNight",
                    config.mobChanges::wolfAggressiveAtNight,
                    true
                )
            }
            vanilla.addSubCategory("bat") { bat ->
                bat.addBooleanToggle(
                    "batGivesPlayerNausea",
                    config.mobChanges::batGivesPlayerNausea,
                    true
                )
            }
            vanilla.addSubCategory("pig") { pig ->
                pig.addBooleanToggle(
                    "pigRunAwayFromPlayerUntilFed",
                    config.mobChanges::pigRunAwayFromPlayerUntilFed,
                    true,
                    requireRestart = true
                )
                pig.addBooleanToggle(
                    "angerablePig",
                    config.mobChanges::angerablePig,
                    true,
                    requireRestart = true
                )
            }
            vanilla.addSubCategory("chicken") { chicken ->
                chicken.addBooleanToggle(
                    "chickenRunAwayFromPlayerUntilFed",
                    config.mobChanges::chickenRunAwayFromPlayerUntilFed,
                    true,
                    requireRestart = true
                )
            }
            vanilla.addSubCategory("sheep") { sheep ->
                sheep.addBooleanToggle(
                    "sheepRunAwayFromPlayerUntilFed",
                    config.mobChanges::sheepRunAwayFromPlayerUntilFed,
                    true,
                    requireRestart = true
                )
            }
        }
        category.addSubCategory("xeno_early_start") { xenoEarlyStart ->
            xenoEarlyStart.addBooleanToggle(
                "prowlerCanSpawn",
                config.mobChanges::prowlerCanSpawn,
                true
            )
        }
    }

    private fun addGameplayEntries(category: Category, config: XenoEarlyStartConfig.Config) {
        category.addBooleanToggle(
            "stationsUnusableUntilFirstCraft",
            config.earlyGameChanges::stationsUnusableUntilFirstCraft,
            true
        )

        category.addBooleanToggle(
            "removePickaxeFromAllLootTable",
            config.earlyGameChanges::removePickaxeFromAllLootTable,
            true
        )

        category.addSubCategory("rawBrick") { rawBrick ->
            rawBrick.addIntSlider(
                "rawBrickDryingLength",
                config.earlyGameChanges::rawBrickDryingLength,
                9,
                0,
                RawBrickBlock.MAX_DRY_LEVEL,
                tooltip = TooltipText.DefaultTooltip()
            )
        }
        category.addSubCategory("dropRate") { dropRate ->
            dropRate.addPercentageSlider(
                "bonusStickDropProbability",
                config.earlyGameChanges::bonusStickDropProbability,
                0.15f
            )
            dropRate.addPercentageSlider(
                "plantFiberDropProbability",
                config.earlyGameChanges::plantFiberDropProbability,
                0.05f
            )
            dropRate.addBooleanToggle(
                "overridePebbleDropProbability",
                config.earlyGameChanges::overridePebbleDropProbability,
                false
            )
            dropRate.addPercentageSlider(
                "pebbleDropProbability",
                config.earlyGameChanges::pebbleDropProbability,
                0.4f
            )
        }

        category.addSubCategory("primitiveFire") { primitiveFire ->
            val inMinuteTooltip = TooltipText.CustomTooltip("inMinute")
            primitiveFire.addPercentageSlider(
                "percentageRequiredForMaxBrightness",
                config.earlyGameChanges.primitiveFire::percentageRequiredForMaxBrightness,
                0.25f
            )
            primitiveFire.addSlider(
                "maxBurnTime",
                config.earlyGameChanges.primitiveFire::maxBurnTime,
                5 * 60 * 20,
                1 * 60 * 60,
                60 * 60 * 20,
                { i: Int -> i / 60 / 20 },
                { i: Int -> i * 60 * 20 },
                tooltip = inMinuteTooltip
            )
            primitiveFire.addPercentageSlider(
                "fuelTimeMultiplier",
                config.earlyGameChanges.primitiveFire::fuelTimeMultiplier,
                1.0f,
                1.0f,
                2.0f
            )
            primitiveFire.addSlider(
                "fuelStarterRelightFuelTime",
                config.earlyGameChanges.primitiveFire::fuelStarterRelightFuelTime,
                3 * 60 * 20,
                1 * 60 * 60,
                60 * 60 * 20,
                { i: Int -> i / 60 / 20 },
                { i: Int -> i * 60 * 20 },
                tooltip = inMinuteTooltip
            )
            primitiveFire.addPercentageSlider(
                "cookingTimeMultiplier",
                config.earlyGameChanges.primitiveFire::cookingTimeMultiplier,
                2f,
                1f,
                4f
            )
        }
        category.addSubCategory("brickFurnace") { brickFurnace ->
            brickFurnace.addPercentageSlider(
                "cookingTimeMultiplier",
                config.earlyGameChanges::brickFurnaceCookingTimeMultiplier,
                2f,
                3f,
                4f
            )
        }
        category.addSubCategory("hunger") { hunger ->
            hunger.addSlider(
                "wakingUpExhaustion",
                config.hungerChanges::wakingUpExhaustion,
                40f,
                0f,
                40f,
                { f: Float -> f.toInt() },
                { i: Int -> i.toFloat() }
            )
            hunger.addFloatField(
                "boatRowingExhaustion",
                config.hungerChanges::boatRowingExhaustion,
                0.05f,
                Pair(0f, 40f)
            )
        }
        category.addSubCategory("recipe") { recipe ->
            recipe.addBooleanToggle(
                "harderBrickFurnaceRecipe",
                config.earlyGameChanges.recipes::harderBrickFurnaceRecipe,
                true,
                requireRestart = true
            )
            recipe.addBooleanToggle(
                "vanillaFurnaceRecipe",
                config.earlyGameChanges.recipes::vanillaFurnaceRecipe,
                false,
                requireRestart = true
            )
            recipe.addBooleanToggle(
                "vanillaCraftingTableRecipe",
                config.earlyGameChanges.recipes::vanillaCraftingTableRecipe,
                false,
                requireRestart = true
            )
            recipe.addBooleanToggle(
                "vanillaStoneToolRecipe",
                config.earlyGameChanges.recipes::vanillaStoneToolRecipe,
                false,
                requireRestart = true
            )

        }
    }


    private fun addBlockChangesEntries(category: Category, config: XenoEarlyStartConfig.Config) {
        category.addFloatField(
            "stonecutterDamage",
            config.blockChanges::stonecutterDamage,
            3f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "amethystFallDamageMultiplier",
            config.blockChanges::amethystFallDamageMultiplier,
            1.5f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "cookerDamage",
            config.blockChanges::cookerDamage,
            0.5f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "brickFurnaceDamage",
            config.blockChanges::brickFurnaceDamage,
            1f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "furnaceDamage",
            config.blockChanges::furnaceDamage,
            1.5f,
            Pair(0f, 5f)
        )
        category.addFloatField(
            "blastFurnaceDamage",
            config.blockChanges::blastFurnaceDamage,
            2.5f,
            Pair(0f, 5f)
        )
        category.addBooleanToggle(
            "blastFurnaceSetNearbyBlockOnFire",
            config.blockChanges::blastFurnaceSetNearbyBlockOnFire,
            true
        )
        category.addEnumSelector(
            "fixThinBlockStepSound",
            config.blockChanges::fixThinBlockStepSound,
            XenoEarlyStartConfig.BlockChanges.FixThinBlockStepSound::class.java,
            XenoEarlyStartConfig.BlockChanges.FixThinBlockStepSound.True
        )
    }

    private fun addClientEntries(category: Category, config: XenoEarlyStartConfig.Config) {
        category.addSubCategory("tooltips", expanded = true) { tooltips ->
            val tutorialTooltips = TooltipText.CustomTooltip("tutorialTooltips")
            tooltips.addBooleanToggle(
                "disableAllTooltip",
                config.client.tooltips::disableAllTooltips,
                false,
                tooltip = tutorialTooltips
            )
            tooltips.addBooleanToggle(
                "disableFoodWarningTooltips",
                config.client.tooltips::disableFoodWarningTooltips,
                false
            )
            tooltips.addBooleanToggle(
                "disableTutorialTooltips",
                config.client.tooltips::disableTutorialTooltips,
                false,
                tooltip = tutorialTooltips
            )
            tooltips.addBooleanToggle(
                "disableItemDescriptionTooltips",
                config.client.tooltips::disableItemDescriptionTooltips,
                false,
                tooltip = tutorialTooltips
            )
        }

    }
}