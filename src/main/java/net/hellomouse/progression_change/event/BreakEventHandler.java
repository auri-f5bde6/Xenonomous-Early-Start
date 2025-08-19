package net.hellomouse.progression_change.event;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.registries.ProgressionModItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = ProgressionMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BreakEventHandler {
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event) {
        var eventState = event.getState();
        var level = event.getLevel();
        var pos = event.getPos();
        var player = event.getPlayer();
        var toolStack = player.getMainHandStack();
        var state = event.getState();
        var block = eventState.getBlock();
        if (eventState.canHarvestBlock(level, pos, player)) {
            var blockRegistryEntry = eventState.getBlock().getRegistryEntry();
            if (toolStack.getItem() instanceof MiningToolItem) {
                if (state.isIn(Tags.Blocks.ORES_COPPER) && shouldReplaceDrop(toolStack, ToolMaterials.DIAMOND)) {
                    replaceDrop(
                            event, toolStack, level, player, state, pos,
                            new ItemStack(ProgressionModItemRegistry.RAW_COPPER_NUGGET.get(), ProgressionMod.CONFIG.oreDropChanges.rawCopperNuggetDrop)
                    );
                    return;
                } else if (state.isIn(Tags.Blocks.ORES_IRON) && shouldReplaceDrop(toolStack, ToolMaterials.DIAMOND)) {
                    replaceDrop(
                            event, toolStack, level, player, state, pos,
                            new ItemStack(ProgressionModItemRegistry.RAW_IRON_NUGGET.get(), ProgressionMod.CONFIG.oreDropChanges.rawIronNuggetDrop)
                    );
                    return;
                } else if (state.isIn(Tags.Blocks.ORES_GOLD) && shouldReplaceDrop(toolStack, ToolMaterials.DIAMOND)) {
                    replaceDrop(
                            event, toolStack, level, player, state, pos,
                            new ItemStack(ProgressionModItemRegistry.RAW_GOLD_NUGGET.get(), ProgressionMod.CONFIG.oreDropChanges.rawGoldNuggetDrop)
                    );
                    return;
                } else if (state.isIn(Tags.Blocks.ORES_DIAMOND) && shouldReplaceDrop(toolStack, ToolMaterials.DIAMOND)) {
                    replaceDrop(
                            event, toolStack, level, player, state, pos,
                            new ItemStack(ProgressionModItemRegistry.DIAMOND_FRAGMENT.get(), ProgressionMod.CONFIG.oreDropChanges.rawDiamondFragmentDrop)
                    );
                    return;
                }
            }
            if (toolStack.isOf(Items.FLINT)) {
                if (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.REPLACEABLE_BY_TREES)) {
                    // I'm assuming that REPLACEABLE_BY_TREES blocks are either grass or flower, I hope I didn't make Ming looks bad
                    if (random.nextFloat() < ProgressionMod.CONFIG.plantFiberDropProbability) {
                        Block.dropStack((World) level, pos, ProgressionModItemRegistry.PLANT_FIBER.get().getDefaultStack());
                        return;
                    }
                }
            }
            if (state.isIn(BlockTags.LOGS) && !(toolStack.getItem() instanceof AxeItem)) {
                level.breakBlock(pos, false, player);
                state.getBlock().onBroken(level, pos, state);
                event.setCanceled(true);
            }
        }
    }

    private static boolean shouldReplaceDrop(ItemStack toolStack, ToolMaterial lowerThanTier) {
        var toolMaterial = ((MiningToolItem) toolStack.getItem()).getMaterial();
        return (toolMaterial.getMiningLevel() < lowerThanTier.getMiningLevel() && ProgressionMod.CONFIG.oreDropChanges.moddedPickaxeWorkaround)
                || TierSortingRegistry.getTiersLowerThan(lowerThanTier).contains(toolMaterial);
    }

    private static void replaceDrop(BlockEvent.BreakEvent event, ItemStack toolStack, WorldAccess level, PlayerEntity player, BlockState blockState, BlockPos pos, ItemStack toDrop) {
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
