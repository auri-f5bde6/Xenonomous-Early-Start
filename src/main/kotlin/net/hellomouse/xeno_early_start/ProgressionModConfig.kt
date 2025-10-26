package net.hellomouse.xeno_early_start

object ProgressionModConfig {
    class Config {
        @JvmField
        var oreDropChanges: OreDropChanges = OreDropChanges()

        @JvmField
        var earlyGameChanges: EarlyGameChanges = EarlyGameChanges()

        @JvmField
        var mobChanges: MobChanges = MobChanges()

        @JvmField
        var blockChanges: BlockChanges = BlockChanges()
    }

    @JvmField
    var config: Config = Config()

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

        var polarBearAlwaysAggressive: Boolean = true

        var polarBearSpeed: Float = 0.35f

        var polarBearRange: Int = 40

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

}
