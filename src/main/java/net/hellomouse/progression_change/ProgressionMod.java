package net.hellomouse.progression_change;

import com.mojang.logging.LogUtils;
import net.hellomouse.progression_change.registries.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.List;

@Mod(ProgressionMod.MODID)
public class ProgressionMod {
    public static final String MODID = "progression_change";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<ItemGroup> CREATIVE_TAB_REG = DeferredRegister.create(RegistryKeys.ITEM_GROUP, MODID);
    public static final RegistryObject<ItemGroup> CREATIVE_TAB = CREATIVE_TAB_REG.register(MODID, () -> ItemGroup.builder()
            .displayName(Text.translatable("itemGroup." + MODID + ".creative_tab"))
            .icon(() -> new ItemStack(ProgressionModItemRegistry.COPPER_PICKAXE.get()))
            .withTabsBefore(ItemGroups.SPAWN_EGGS)
            .entries((enabledFeatures, output) -> {
                ProgressionModItemRegistry.addItemToCreativeTab(output);
            })
            .build());

    public ProgressionMod(FMLJavaModLoadingContext context) {
        LOGGER.info("Loading Progression Mod Config...");

        IEventBus modEventBus = context.getModEventBus();
        ProgressionModBlockRegistry.DEF_REG.register(modEventBus);
        ProgressionModItemRegistry.VANILLA_ITEMS.register(modEventBus);
        ProgressionModItemRegistry.DEF_REG.register(modEventBus);
        ProgressionModEntityRegistry.DEF_REG.register(modEventBus);
        ProgressionModScreenHandlerRegistry.DEF_REG.register(modEventBus);
        ProgressionModBlockEntityRegistry.DEF_REG.register(modEventBus);
        ProgressionModLootTypeRegistry.FUNC_DEF_REG.register(modEventBus);
        ProgressionModLootTypeRegistry.COND_DEF_REG.register(modEventBus);
        ProgressionModRecipeRegistry.DEF_REG.register(modEventBus);
        ProgressionModRecipeRegistry.TYPE_DEF_REG.register(modEventBus);
        CREATIVE_TAB_REG.register(modEventBus);
        TierSortingRegistry.registerTier(ProgressionModToolMaterials.COPPER,
                ProgressionModToolMaterials.COPPER_ID,
                List.of(new Identifier("stone")),
                List.of(new Identifier("iron"))
        );
        TierSortingRegistry.registerTier(ProgressionModToolMaterials.FLINT,
                ProgressionModToolMaterials.FLINT_ID,
                List.of(),
                List.of(new Identifier("wood"))
        );
        TierSortingRegistry.registerTier(ProgressionModToolMaterials.BONE,
                ProgressionModToolMaterials.BONE_ID,
                List.of(),
                List.of(new Identifier("wood"))
        );
    }


    public static Identifier of(String path) {
        return Identifier.of(MODID, path);
    }
}
