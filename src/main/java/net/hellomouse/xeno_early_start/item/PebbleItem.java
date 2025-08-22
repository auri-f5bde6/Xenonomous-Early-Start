package net.hellomouse.xeno_early_start.item;

import net.hellomouse.xeno_early_start.ProgressionMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraftforge.common.Tags;

public class PebbleItem extends Item {
    private final Item result;

    public PebbleItem(Item knappedItem, Settings settings) {
        super(settings);
        result = knappedItem;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var blockPos = context.getBlockPos();
        var blockState = world.getBlockState(blockPos);
        var player = context.getPlayer();
        if (player != null) {
            var mainhandStack = player.getMainHandStack();
            var offhandStack = player.getOffHandStack();
            if (offhandStack.isOf(mainhandStack.getItem()) && offhandStack.getItem() instanceof PebbleItem && blockState.isIn(Tags.Blocks.COBBLESTONE)) {
                mainhandStack.decrement(1);
                offhandStack.decrement(1);
                player.giveItemStack(result.getDefaultStack());
                world.playSound(player, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        } else {
            ProgressionMod.LOGGER.warn("RockItem useOnBlock's context have no player set");
            return ActionResult.FAIL;
        }
    }
}
