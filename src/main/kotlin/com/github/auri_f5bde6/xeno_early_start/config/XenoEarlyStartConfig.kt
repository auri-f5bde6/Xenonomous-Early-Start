package com.github.auri_f5bde6.xeno_early_start.config

import kotlin.math.min

object XenoEarlyStartConfig {
    class Config {
        var disableNonClientConfig: Boolean = false

        @JvmField
        var oreChanges: OreChanges = OreChanges()

        @JvmField
        var earlyGameChanges: Gameplay = Gameplay()

        @JvmField
        var mobChanges: MobChanges = MobChanges()

        @JvmField
        var blockChanges: BlockChanges = BlockChanges()

        @JvmField
        var hungerChanges: HungerChanges = HungerChanges()

        @JvmField
        var client: ClientConfig = ClientConfig()
    }

    @JvmField
    var config: Config = Config()
    class OreChanges {
        var vanillaCopperLootTable: Boolean = false
        var rawCopperNuggetDropMin: Int = 1
        var rawCopperNuggetDropMax: Int = 3

        var vanillaIronLootTable: Boolean = false

        var rawIronNuggetDrop: Int = 1

        var vanillaGoldLootTable: Boolean = false

        var rawGoldNuggetDrop: Int = 1

        var vanillaDiamondLootTable: Boolean = false

        var diamondFragmentDrop: Int = 1
        var goldenPickDiamondFragmentBuff: Int = 2

        var oreToStone: Boolean = false

        var coalDustExplosionClusterSize: Int = 6

        var coalDustExplosionBlockLimit: Int = 256
    }

    class Gameplay {
        var plantFiberDropProbability: Float = 0.05f

        var overridePebbleDropProbability: Boolean = false

        var pebbleDropProbability: Float = 0.4f

        var rawBrickDryingLength: Int = 9

        var removePickaxeFromAllLootTable: Boolean = true

        var stationsUnusableUntilFirstCraft: Boolean = true

        var bonusStickDropProbability: Float = 0.15f

        var brickFurnaceCookingTimeMultiplier: Float = 3f

        var recipes = Recipes()

        class Recipes {
            var harderBrickFurnaceRecipe: Boolean = true

            var vanillaFurnaceRecipe: Boolean = false

            var vanillaStoneToolRecipe: Boolean = false

            var vanillaCraftingTableRecipe: Boolean = false
        }

        var primitiveFire: PrimitiveFire = PrimitiveFire()

        class PrimitiveFire {
            var maxBurnTime: Int = 5 * 60 * 20
            var percentageRequiredForMaxBrightness: Float = 0.25f
            var fuelTimeMultiplier: Float = 1f
            var fuelStarterRelightFuelTime: Int = 3 * 60 * 20
                set(value) {
                    field = min(value, maxBurnTime)
                }
            var cookingTimeMultiplier: Float = 2f
        }
    }

    class MobChanges {
        var flatAdditiveMobSpawnWithEquipment: Float = 0.05f

        var replaceEntityCopperArmourProbability: Float = 0.05f

        var entitySpawnWithCopperToolProbability: Float = 0.05f

        var polarBearAlwaysAggressive: Boolean = true

        var polarBearSpeed: Float = 0.35f

        var polarBearRange: Int = 40

        var mobAttackWeakPlayer: Boolean = true

        var wolfAggressiveAtNight: Boolean = true

        var batGivesPlayerNausea: Boolean = true

        var prowlerCanSpawn: Boolean = true

        var angerableSquid: Boolean = true

        var angerablePig: Boolean = false

        var pigRunAwayFromPlayerUntilFed: Boolean = true

        var chickenRunAwayFromPlayerUntilFed: Boolean = true

        var sheepRunAwayFromPlayerUntilFed: Boolean = true

        var customPhantomSpawn: Boolean = true

        var phantomSpawnLevel: Int = 180

        var overworldPhantomMaxGroupSize: Int = 4

        var maxPhantomSize: Int = 10
    }

    class BlockChanges {
        enum class FixThinBlockStepSound {
            True, False, OnlyThisMod
        }

        var stonecutterDamage: Float = 3f
        var coralDamage: Float = 1.5f
        var amethystFallDamageMultiplier: Float = 1.5f
        var cookerDamage: Float = 0.5f
        var brickFurnaceDamage: Float = 1f
        var furnaceDamage: Float = 1.5f
        var blastFurnaceDamage: Float = 2.5f
        var blastFurnaceSetNearbyBlockOnFire: Boolean = true
        var fixThinBlockStepSound: FixThinBlockStepSound = FixThinBlockStepSound.True
    }

    class HungerChanges {
        var wakingUpExhaustion: Float = 40.0f
        var boatRowingExhaustion: Float = 0.05f
    }

    class ClientConfig {
        class Tooltips {
            var disableAllTooltips: Boolean = false

            var disableFoodWarningTooltips: Boolean = false

            var disableTutorialTooltips: Boolean = false

            var disableItemDescriptionTooltips: Boolean = false
        }

        var tooltips: Tooltips = Tooltips()
    }
}
