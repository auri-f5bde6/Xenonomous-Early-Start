package net.hellomouse.progression_change.event;

import net.fabricmc.yarn.constants.MiningLevels;
import net.hellomouse.progression_change.ProgressionMod;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProgressionMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BreakEventHandler {
    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event) {
        var eventState = event.getState();
        var level = event.getLevel();
        var pos = event.getPos();
        var player = event.getPlayer();
        var toolStack = player.getMainHandStack();
        if (eventState.canHarvestBlock(level, pos, player)) {
            if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_IRON)) {
                if (toolStack.getItem() instanceof MiningToolItem diggerItem) {
                    var material = diggerItem.getMaterial();
                    if (material.getMiningLevel() < MiningLevels.IRON) {
                        level.breakBlock(pos, false, player);
                        level.spawnEntity(new ItemEntity((World) level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.IRON_NUGGET, ProgressionMod.CONFIG.ironNuggetDrop)));
                        toolStack.damage(1, player, (playerEntity) -> {
                        });
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
