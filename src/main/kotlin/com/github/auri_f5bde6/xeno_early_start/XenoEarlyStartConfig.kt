package com.github.auri_f5bde6.xeno_early_start

import kotlin.math.min

object XenoEarlyStartConfig {
    class Config {
        @JvmField
        var oreChanges: OreChanges = OreChanges()

        @JvmField
        var earlyGameChanges: EarlyGameChanges = EarlyGameChanges()

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
        var rawCopperNuggetDropMin: Int = 1
        var rawCopperNuggetDropMax: Int = 3

        var rawIronNuggetDrop: Int = 1

        var rawGoldNuggetDrop: Int = 1

        var diamondFragmentDrop: Int = 1

        var oreToStone: Boolean = false

        var coalDustExplosionClusterSize: Int = 6

        var coalDustExplosionBlockLimit: Int = 256
    }

    class EarlyGameChanges {
        var plantFiberDropProbability: Float = 0.05f

        var overridePebbleDropProbability: Boolean = false

        var pebbleDropProbability: Float = 0.4f

        // 15/(20*60*5)=0.025
        var primitiveFire: PrimitiveFire = PrimitiveFire()

        class PrimitiveFire {
            var maxBurnTime: Int = 5 * 60 * 20
            var percentageRequiredForMaxBrightness: Float = 0.25f
            var fuelTimeMultiplier: Float = 1f
            var fuelStarterRelightFuelTime: Int = 3 * 60 * 20
                set(value) {
                    field = min(value, maxBurnTime)
                }
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
