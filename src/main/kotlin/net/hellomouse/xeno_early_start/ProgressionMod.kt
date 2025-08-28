package net.hellomouse.xeno_early_start

import com.mojang.logging.LogUtils
import net.hellomouse.xeno_early_start.registries.*
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraftforge.common.TierSortingRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.function.Supplier

@Mod(ProgressionMod.Companion.MODID)
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
        CREATIVE_TAB_REG.register(MOD_BUS)
        TierSortingRegistry.registerTier(
            ProgressionModToolMaterials.COPPER,
            ProgressionModToolMaterials.COPPER_ID,
            listOf<Any?>(Identifier("stone")),
            listOf<Any?>(Identifier("iron"))
        )
        TierSortingRegistry.registerTier(
            ProgressionModToolMaterials.FLINT,
            ProgressionModToolMaterials.FLINT_ID,
            mutableListOf<Any?>(),
            listOf<Any?>(Identifier("wood"))
        )
        TierSortingRegistry.registerTier(
            ProgressionModToolMaterials.BONE,
            ProgressionModToolMaterials.BONE_ID,
            mutableListOf<Any?>(),
            listOf<Any?>(Identifier("wood"))
        )
    }


    companion object {
        const val MODID: String = "xeno_early_start"
        val LOGGER: Logger = LogUtils.getLogger()
        val CREATIVE_TAB_REG: DeferredRegister<ItemGroup?> =
            DeferredRegister.create<ItemGroup?>(RegistryKeys.ITEM_GROUP, MODID)
        val CREATIVE_TAB: RegistryObject<ItemGroup?>? = CREATIVE_TAB_REG.register<ItemGroup?>(MODID, Supplier {
            ItemGroup.builder()
                .displayName(Text.translatable("itemGroup.$MODID.creative_tab"))
                .icon(Supplier { ItemStack(ProgressionModItemRegistry.COPPER_PICKAXE.get()) })
                .withTabsBefore(ItemGroups.SPAWN_EGGS)
                .entries { enabledFeatures: ItemGroup.DisplayContext?, output: ItemGroup.Entries ->
                    ProgressionModItemRegistry.addItemToCreativeTab(output)
                }
                .build()
        })

        fun of(path: String): Identifier {
            return Identifier.of(MODID, path) ?: throw IllegalArgumentException("$MODID:$path is not valid")
        }
    }
}
