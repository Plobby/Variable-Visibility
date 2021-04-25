package me.plobnob.variablevisibility.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.plobnob.variablevisibility.config.VisibilityConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@SuppressWarnings("resource")
	@Redirect(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;isVisible(Lnet/minecraft/entity/LivingEntity;)Z", ordinal = 0),
		method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
	)
	private boolean redirectIsVisible(LivingEntityRenderer<?, ?> renderer, LivingEntity e) {
		// Get config instance
		VisibilityConfig config = AutoConfig.getConfigHolder(VisibilityConfig.class).getConfig();
		
		// Check if enabled
		if (config.enabled) {
			// Return true if the entity is the client player
			if (e.equals(MinecraftClient.getInstance().player))
				return true;
			
			// Return visibility state of the entity if not the player
			if (!(e instanceof PlayerEntity))
				return !e.isInvisible();
			
			// Return if within distance to the player
			if (e.distanceTo(MinecraftClient.getInstance().player) >= Math.max(config.transparentRange, config.invisibilityRange))
				return true;
			return false;
		}
		
		// Return inverted entity visibility state
		return !e.isInvisible();
	}

	@Redirect(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z", ordinal = 0),
		method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
	)
	private boolean redirectIsInvisibleTo(LivingEntity e, PlayerEntity player) {
		// Get config instance
		VisibilityConfig config = AutoConfig.getConfigHolder(VisibilityConfig.class).getConfig();
		
		// Check if enabled
		if (config.enabled) {
			// Return false if the entity is the player
			if (e.equals(player))
				return false;
			
			// Perform calculations if they are a player
			if (e instanceof PlayerEntity) {
				// Make invisible to the player if within the invisibility distance
				if (e.distanceTo(player) <= config.invisibilityRange)
					return true;
				
				// Return false to make all other players visible
				return false;
			}
		}
		
		// Return default visibility state
		if (player.isSpectator()) {
			return false;
	    } else {
	    	AbstractTeam abstractTeam = e.getScoreboardTeam();
	    	return abstractTeam != null && player != null && player.getScoreboardTeam() == abstractTeam && abstractTeam.shouldShowFriendlyInvisibles() ? false : e.isInvisible();
	    }
	}
	
	@SuppressWarnings("resource")
	@Redirect(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;FFFFFF)V", ordinal = 0),
		method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
	)
	private void redirectFeatureRender(FeatureRenderer<Entity, ?> renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		// Only filter if the target entity is a player
		if (entity instanceof PlayerEntity) {
			// Get config instance
			VisibilityConfig config = AutoConfig.getConfigHolder(VisibilityConfig.class).getConfig();
			
			// Get instance player
			PlayerEntity player = MinecraftClient.getInstance().player;
			
			// Cancel if the correct config criteria are met
			if (config.enabled && !entity.equals(player) && entity.distanceTo(player) <= Math.max(config.transparentRange, config.invisibilityRange))
				return;
		}
		
		// Invoke default render function for the feature
		renderer.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
	}

	
}
