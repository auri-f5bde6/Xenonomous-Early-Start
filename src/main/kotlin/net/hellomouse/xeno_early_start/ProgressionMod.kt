package net.hellomouse.xeno_early_start

import com.mojang.logging.LogUtils
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml
import net.hellomouse.xeno_early_start.registries.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.common.TierSortingRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.io.FileNotFoundException
import java.util.function.Supplier

@Mod(ProgressionMod.MODID)
class ProgressionMod {
    init {
        LOGGER.info("Loading Progression Mod Config...")

        ProgressionModBlockRegistry.DEF_REG.register(MOD_BUS)
        ProgressionModItemRegistry.VANILLA_ITEMS.register(MOD_BUS)
        ProgressionModItemRegistry.DEF_REG.register(MOD_BUS)
        ProgressionModEntityRegistry.DEF_REG.register(MOD_BUS)
        ProgressionModScreenHandlerRegistry.DEF_REG.register(MOD_BUS)
        ProgressionModBlockEntityRegistry.DEF_REG.register(MOD_BUS)
        ProgressionModLootTypeRegistry.FUNC_DEF_REG.register(MOD_BUS)
        ProgressionModLootTypeRegistry.COND_DEF_REG.register(MOD_BUS)
        ProgressionModRecipeRegistry.DEF_REG.register(MOD_BUS)
        ProgressionModRecipeRegistry.TYPE_DEF_REG.register(MOD_BUS)
        XenoProgressionModParticleRegistry.DEF_REG.register(MOD_BUS)
        XenoEarlyStartStatusEffectRegistry.DEF_REG.register(MOD_BUS)
        CREATIVE_TAB_REG.register(MOD_BUS)
        TierSortingRegistry.registerTier(
            ProgressionModToolMaterials.COPPER,
            ProgressionModToolMaterials.COPPER_ID,
            listOf<Any?>(ofMinecraft("stone")),
            listOf<Any?>(ofMinecraft("iron"))
        )
        TierSortingRegistry.registerTier(
            ProgressionModToolMaterials.FLINT,
            ProgressionModToolMaterials.FLINT_ID,
            mutableListOf<Any?>(),
            listOf<Any?>(ofMinecraft("wood"))
        )
        TierSortingRegistry.registerTier(
            ProgressionModToolMaterials.BONE,
            ProgressionModToolMaterials.BONE_ID,
            mutableListOf<Any?>(),
            listOf<Any?>(ofMinecraft("wood"))
        )
        try {
            val file = FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
            ProgressionModConfig.config =
                Toml().read(file.reader())
                    .to(ProgressionModConfig.config.javaClass)
        } catch (_: FileNotFoundException) {
            val tomlWriter = me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter()
            tomlWriter.write(
                ProgressionModConfig.config,
                FMLPaths.CONFIGDIR.get().resolve("xeno-early-start.toml").toFile()
            )
        }
        LOADING_CONTEXT.registerExtensionPoint(
            ConfigScreenFactory::class.java,
            Supplier {
                ConfigScreenFactory { mc: MinecraftClient?, prevScreen: Screen? ->
                    XenoEarlyStartConfigGui.configBuilder.build()
                }
            }
        )


    }


    companion object {
        const val MODID: String = "xeno_early_start"
        val LOGGER: Logger = LogUtils.getLogger()
        val CREATIVE_TAB_REG: DeferredRegister<ItemGroup> =
            DeferredRegister.create<ItemGroup>(RegistryKeys.ITEM_GROUP, MODID)

        @Suppress("UNUSED")
        val CREATIVE_TAB: RegistryObject<ItemGroup> = CREATIVE_TAB_REG.register<ItemGroup>(MODID, Supplier {
            ItemGroup.builder()
                .displayName(Text.translatable("itemGroup.$MODID.creative_tab"))
                .icon { ItemStack(ProgressionModItemRegistry.COPPER_PICKAXE.get()) }
                .withTabsBefore(ItemGroups.SPAWN_EGGS)
                .entries { enabledFeatures: ItemGroup.DisplayContext?, output: ItemGroup.Entries ->
                    ProgressionModItemRegistry.addItemToCreativeTab(output)
                }
                .build()
        })

        fun of(path: String): Identifier {
            return Identifier.of(MODID, path) ?: throw IllegalArgumentException("$MODID:$path is not valid")
        }

        fun ofMinecraft(path: String): Identifier {
            return Identifier.of("minecraft", path) ?: throw IllegalArgumentException("$MODID:$path is not valid")
        }
    }
}
