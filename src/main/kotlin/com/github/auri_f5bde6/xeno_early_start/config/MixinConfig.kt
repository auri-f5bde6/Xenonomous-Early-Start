package com.github.auri_f5bde6.xeno_early_start.config

import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter
import net.minecraftforge.fml.loading.FMLPaths
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import java.io.FileNotFoundException


class MixinConfig : IMixinConfigPlugin {
    override fun onLoad(p0: String?) {
        try {
            val file = FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
            XenoEarlyStartConfig.config =
                Toml().read(file.reader())
                    .to(XenoEarlyStartConfig.config.javaClass)
        } catch (_: FileNotFoundException) {
            val tomlWriter = TomlWriter()
            tomlWriter.write(
                XenoEarlyStartConfig.config,
                FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
            )
        }
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        if (targetClassName == null || mixinClassName == null) return true // probably shouldn't happen but eh
        return when (targetClassName to mixinClassName.removePrefix("com.github.auri_f5bde6.xeno_early_start.mixins.")) {
            "net.minecraft.entity.passive.SquidEntity" to "entity.angerable_changes.SharedMixin" -> XenoEarlyStartConfig.config.mobChanges.angerableSquid
            "net.minecraft.entity.passive.SquidEntity" to "entity.angerable_changes.SquidEntityMixin" -> XenoEarlyStartConfig.config.mobChanges.angerableSquid
            "net.minecraft.entity.passive.ChickenEntity" to "entity.neutral_till_fed.ChickenEntityMixin" ->
                XenoEarlyStartConfig.config.mobChanges.chickenRunAwayFromPlayerUntilFed

            "net.minecraft.entity.passive.PigEntity" to "entity.neutral_till_fed.NeutralTilFedPigEntityMixin" -> XenoEarlyStartConfig.config.mobChanges.pigRunAwayFromPlayerUntilFed
            "net.minecraft.entity.passive.PigEntity" to "entity.neutral_till_fed.AngerablePigEntityMixin" -> XenoEarlyStartConfig.config.mobChanges.angerablePig
            "net.minecraft.entity.passive.PigEntity" to "entity.angerable_changes.SharedMixin" -> XenoEarlyStartConfig.config.mobChanges.angerablePig
            "net.minecraft.entity.passive.PigEntity" to "entity.neutral_till_fed.NeutralTilFedAngerablePigEntityMixin" -> XenoEarlyStartConfig.config.mobChanges.angerablePig && XenoEarlyStartConfig.config.mobChanges.pigRunAwayFromPlayerUntilFed
            "net.minecraft.entity.passive.SheepEntity" to "entity.neutral_till_fed.SheepEntityMixin" -> XenoEarlyStartConfig.config.mobChanges.sheepRunAwayFromPlayerUntilFed
            else -> true
        }
    }

    override fun acceptTargets(
        p0: Set<String?>?,
        p1: Set<String?>?
    ) {
    }

    override fun getMixins(): List<String?>? {
        return null
    }

    override fun preApply(
        p0: String?,
        p1: ClassNode?,
        p2: String?,
        p3: IMixinInfo?
    ) {
    }

    override fun postApply(
        p0: String?,
        p1: ClassNode?,
        p2: String?,
        p3: IMixinInfo?
    ) {
    }

}