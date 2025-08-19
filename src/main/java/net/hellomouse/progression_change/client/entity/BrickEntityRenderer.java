package net.hellomouse.progression_change.client.entity;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.entity.BrickEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BrickEntityRenderer extends EntityRenderer<BrickEntity> {
    public static final Identifier TEXTURE = ProgressionMod.of("textures/entity/brick.png");

    private final BrickEntityModel model;

    public BrickEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg);
        this.model = new BrickEntityModel(arg.getPart(BrickEntityModel.LAYER_LOCATION));
    }

    @Override
    public Identifier getTexture(BrickEntity entity) {
        return TEXTURE;
    }

    public void render(BrickEntity arg, float f, float g, MatrixStack stack, VertexConsumerProvider arg3, int i) {
        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, arg.prevYaw, arg.getYaw()) - 90.0f));
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, arg.prevPitch, arg.getPitch())));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(arg3, this.model.getLayer(this.getTexture(arg)), false, false);
        this.model.render(stack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.pop();
        super.render(arg, f, g, stack, arg3, i);
    }

}
