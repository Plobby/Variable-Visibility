package me.plobnob.variablevisibility.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.plobnob.variablevisibility.config.VisibilityConfig;
import me.plobnob.variablevisibility.struct.NameTagRender;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
	
	@SuppressWarnings("resource")
	@Inject(
		at = @At("HEAD"),
		method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
		cancellable = true
	)
	private void injectRenderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		// Return if not a player
		if (!(entity instanceof PlayerEntity))
			return;
		
		// Get config instance
		VisibilityConfig config = AutoConfig.getConfigHolder(VisibilityConfig.class).getConfig();
		
		// Return if not enabled
		if (!config.enabled)
			return;
		
		// Cancel the render function ready to custom render the name tags
		info.cancel();
		
		// Get distance to the main player
		float dist = entity.distanceTo(MinecraftClient.getInstance().player);
		
		// Decide upon a rendering type
		NameTagRender renderType = config.allNameTags;
		if (dist <= config.invisibilityRange)
			renderType = config.invisibilityNameTags;
		else if (dist <= config.transparentRange)
			renderType = config.transparentNameTags;
		
		// Perform action based on render type
		boolean shifting = false;
		switch (renderType) {
		case YES:
			shifting = false;
			break;
		case SHIFTING:
			shifting = true;
			break;
		case NO:
			return;
		}
		
		// Get the renderer instance
		EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		
		// Render appropriate name tag
		if (dispatcher.getSquaredDistanceToCamera(entity) <= 4096.0D) {
			int i = "deadmau5".equals(text.getString()) ? -10 : 0;
			matrices.push();
			matrices.translate(0.0D, (double) entity.getHeight() + 0.5F, 0.0D);
			matrices.multiply(dispatcher.getRotation());
			matrices.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrices.peek().getModel();
			float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
			int j = (int) (g * 255.0F) << 24;
			TextRenderer textRenderer = dispatcher.getTextRenderer();
			float h = (float) (-textRenderer.getWidth((StringVisitable) text) / 2);
			textRenderer.draw(text, h, (float) i, 553648127, false, matrix4f, vertexConsumers, !shifting, j, light);
			if (!shifting) {
				textRenderer.draw((Text) text, h, (float) i, -1, false, matrix4f, vertexConsumers, false, 0, light);
			}

			matrices.pop();
		}
	}

}
