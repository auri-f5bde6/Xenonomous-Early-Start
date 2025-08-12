package net.hellomouse.progression_change.event;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.ProgressionModItemRegistry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
            if (toolStack.getItem() instanceof MiningToolItem) {
                if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_COPPER)) {
                    maybeReplaceDrop(
                            event, toolStack, level, player, pos,
                            ToolMaterials.IRON, new ItemStack(ProgressionModItemRegistry.RAW_COPPER_NUGGET.get(), ProgressionMod.CONFIG.ironNuggetDrop)
                    );
                } else if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_IRON)) {
                    maybeReplaceDrop(
                            event, toolStack, level, player, pos,
                            ToolMaterials.IRON, new ItemStack(ProgressionModItemRegistry.RAW_IRON_NUGGET.get(), ProgressionMod.CONFIG.ironNuggetDrop)
                    );
                } else if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_GOLD)) {
                    maybeReplaceDrop(
                            event, toolStack, level, player, pos,
                            ToolMaterials.IRON, new ItemStack(ProgressionModItemRegistry.RAW_GOLD_NUGGET.get(), ProgressionMod.CONFIG.goldNuggetDrop)
                    );
                }
            }
        }
    }

    public static void maybeReplaceDrop(BlockEvent.BreakEvent event, ItemStack toolStack, WorldAccess level, PlayerEntity player, BlockPos pos, ToolMaterial tierOrBelow, ItemStack toDrop) {
        var blockState = level.getBlockState(pos);
        var toolMaterial = ((MiningToolItem) toolStack.getItem()).getMaterial();
        if (TierSortingRegistry.getTiersLowerThan(tierOrBelow).contains(toolMaterial) || toolMaterial.equals(tierOrBelow)) {
            level.breakBlock(pos, false, player);
            level.spawnEntity(new ItemEntity((World) level, pos.getX(), pos.getY(), pos.getZ(), toDrop));
            toolStack.postMine((World) level, blockState, pos, player);
            event.setCanceled(true);
        }
    }
}
