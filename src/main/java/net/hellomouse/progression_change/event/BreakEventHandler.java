package net.hellomouse.progression_change.event;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.ProgressionModConfig;
import net.hellomouse.progression_change.ProgressionModTags;
import net.hellomouse.progression_change.registries.ProgressionModItemRegistry;
import net.hellomouse.progression_change.registries.ProgressionModRecipeRegistry;
import net.hellomouse.progression_change.utils.MiningLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraftforge.event.ForgeEventFactory;
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
            if (toolStack.isIn(ProgressionModTags.Items.SHARDS)) {
                if (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.REPLACEABLE_BY_TREES)) {
                    // I'm assuming that REPLACEABLE_BY_TREES blocks are either grass or flower, I hope I didn't make Ming looks bad
                    if (level.getRandom().nextFloat() < (ProgressionModConfig.earlyGameChanges.plantFiberDropProbability / 100f)) {
                        Block.dropStack((World) level, pos, ProgressionModItemRegistry.PLANT_FIBER.get().getDefaultStack());
                        return;
                    }
                }
            }
            if (state.isIn(BlockTags.LOGS) && !(toolStack.getItem() instanceof AxeItem)) {
                breakBlock(toolStack, level, player, state, pos);
                event.setCanceled(true);
                return;
            }
            if (state.isIn(ProgressionModTags.Blocks.HAS_BLOCK_TO_BLOCK_RECIPE)) {
                var recipes = ((World) level).getRecipeManager().listAllOfType(ProgressionModRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get());
                for (var recipe : recipes) {
                    if (recipe.matches(state) && (recipe.isAnyTier() || MiningLevel.IsToolLowerThanTier(toolStack, recipe.getMiningTierLowerThan())) && (!recipe.isOreToStone() || (recipe.isOreToStone() && ProgressionModConfig.oreDropChanges.oreToStone))) {
                        breakBlock(toolStack, level, player, state, pos, recipe.isDropBlockLootTable());
                        recipe.maybeDropItemsInList((World) level, pos);
                        ((World) level).setBlockState(pos, recipe.getResultingBlock().getDefaultState());
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }

    private static void replaceDrop(BlockEvent.BreakEvent event, ItemStack toolStack, WorldAccess level, PlayerEntity player, BlockState blockState, BlockPos pos, ItemStack toDrop) {
        breakBlock(toolStack, level, player, blockState, pos);
        Block.dropStack((World) level, pos, toDrop);
        var exp = event.getExpToDrop();
        if (exp > 0) {
            blockState.getBlock().dropExperience((ServerWorld) level, pos, exp);
        }
        event.setCanceled(true);
    }

    private static void breakBlock(ItemStack toolStack, WorldAccess level, PlayerEntity player, BlockState blockState, BlockPos pos) {
        breakBlock(toolStack, level, player, blockState, pos, false);
    }

    private static void breakBlock(ItemStack toolStack, WorldAccess level, PlayerEntity player, BlockState blockState, BlockPos pos, boolean drop) {
        var toolstack1 = toolStack.copy();
        level.breakBlock(pos, false, player);
        blockState.getBlock().onBroken(level, pos, blockState);
        toolStack.postMine((World) level, blockState, pos, player);
        if (toolStack.isEmpty() && !toolstack1.isEmpty()) {
            ForgeEventFactory.onPlayerDestroyItem(player, toolstack1, Hand.MAIN_HAND);
        }
        if (drop) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            blockState.getBlock().afterBreak((World) level, player, pos, blockState, blockentity, toolstack1);
        }
    }
}
