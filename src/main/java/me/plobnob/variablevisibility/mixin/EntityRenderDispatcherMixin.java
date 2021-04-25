package me.plobnob.variablevisibility.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.plobnob.variablevisibility.config.VisibilityConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.WorldView;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	
	@SuppressWarnings("resource")
	@Inject(
		at = @At("HEAD"), 
		method = "renderShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;FFLnet/minecraft/world/WorldView;F)V",
		cancellable = true
	)
	private static void injectRenderShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world, float radius, CallbackInfo info) {
		// Return to ignore any non-player entities
		if (!(entity instanceof PlayerEntity))
			return;
		
		// Get config instance
		VisibilityConfig config = AutoConfig.getConfigHolder(VisibilityConfig.class).getConfig();
		
		// Check if enabled
		if (config.enabled) {
			// Return if entity is the main player
			if (entity.equals(MinecraftClient.getInstance().player))
				return;
			
			// Get distance to the main player
			float dist = entity.distanceTo(MinecraftClient.getInstance().player);
			
			// Cancel callback info based on settings
			if ((dist <= config.invisibilityRange && !config.invisibilityShadows) || (dist <= config.transparentRange && !config.transparentShadows) || (!config.allShadows)) {
				info.cancel();
				return;
			}
		}
	}
	
}
