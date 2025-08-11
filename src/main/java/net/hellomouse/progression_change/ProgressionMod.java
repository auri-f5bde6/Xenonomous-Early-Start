package net.hellomouse.progression_change;

import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ProgressionMod.MODID)
public class ProgressionMod {
    public static final String MODID = "progression_change";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ProgressionModConfig CONFIG;

    public ProgressionMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        init();
    }

    static void init() {
        LOGGER.info("Loading Progression Mod Config...");
        AutoConfig.register(ProgressionModConfig.class, Toml4jConfigSerializer::new);
        CONFIG=AutoConfig.getConfigHolder(ProgressionModConfig.class).getConfig();
    }
}
