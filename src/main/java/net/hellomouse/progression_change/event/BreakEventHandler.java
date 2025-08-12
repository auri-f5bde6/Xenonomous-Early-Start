package net.hellomouse.progression_change.event;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.ProgressionModItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.world.ServerWorld;
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
        var state = event.getState();
        if (eventState.canHarvestBlock(level, pos, player)) {
            if (toolStack.getItem() instanceof MiningToolItem) {
                if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_COPPER)) {
                    maybeReplaceDrop(
                            event, toolStack, level, player, state, pos,
                            ToolMaterials.DIAMOND, new ItemStack(ProgressionModItemRegistry.RAW_COPPER_NUGGET.get(), ProgressionMod.CONFIG.rawCopperNuggetDrop)
                    );
                } else if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_IRON)) {
                    maybeReplaceDrop(
                            event, toolStack, level, player, state, pos,
                            ToolMaterials.DIAMOND, new ItemStack(ProgressionModItemRegistry.RAW_IRON_NUGGET.get(), ProgressionMod.CONFIG.rawIronNuggetDrop)
                    );
                } else if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_GOLD)) {
                    maybeReplaceDrop(
                            event, toolStack, level, player, state, pos,
                            ToolMaterials.DIAMOND, new ItemStack(ProgressionModItemRegistry.RAW_GOLD_NUGGET.get(), ProgressionMod.CONFIG.rawGoldNuggetDrop)
                    );
                } else if (eventState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.ORES_DIAMOND)) {
                    maybeReplaceDrop(
                            event, toolStack, level, player, state, pos,
                            ToolMaterials.DIAMOND, new ItemStack(ProgressionModItemRegistry.DIAMOND_FRAGMENT.get(), ProgressionMod.CONFIG.rawDiamondFragmentDrop)
                    );
                }
            }
        }
    }

    public static void maybeReplaceDrop(BlockEvent.BreakEvent event, ItemStack toolStack, WorldAccess level, PlayerEntity player, BlockState blockState, BlockPos pos, ToolMaterial lowerThanTier, ItemStack toDrop) {
        var toolMaterial = ((MiningToolItem) toolStack.getItem()).getMaterial();
        if (TierSortingRegistry.getTiersLowerThan(lowerThanTier).contains(toolMaterial)) {
            level.breakBlock(pos, false, player);
            blockState.getBlock().onBroken(level, pos, blockState);
            Block.dropStack((World) level, pos, toDrop);
            var exp = event.getExpToDrop();
            if (exp > 0) {
                blockState.getBlock().dropExperience((ServerWorld) level, pos, exp);
            }
            toolStack.postMine((World) level, blockState, pos, player);
            event.setCanceled(true);

        }
    }
}
