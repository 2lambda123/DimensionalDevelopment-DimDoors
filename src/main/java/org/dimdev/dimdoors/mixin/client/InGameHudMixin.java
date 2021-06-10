package org.dimdev.dimdoors.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.dimdev.dimdoors.DimensionalDoorsInitializer;
import org.dimdev.dimdoors.ModConfig;
import org.dimdev.dimdoors.world.ModBiomes;
import org.dimdev.dimdoors.world.ModDimensions;
import org.dimdev.dimdoors.world.level.component.PlayerModifiersComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin{
	private ModConfig.Player config = DimensionalDoorsInitializer.getConfig().getPlayerConfig();
	@Shadow
	private int scaledHeight;
	@Shadow
	private int scaledWidth;

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	//	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"), method = "renderVignetteOverlay(Lnet/minecraft/entity/Entity;)V")
//	public void renderVignetteOverlay(Entity entity, CallbackInfo info) {
//		if (ModDimensions.isLimboDimension(entity.world)) {
//			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//		}
//	}
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void renderOverlayMixin(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		float overlayOpacity = (config.fray.grayScreenFray - PlayerModifiersComponent.getFray(getCameraPlayer()))/(config.fray.grayScreenFray - (float)config.fray.maxFray);
		if (PlayerModifiersComponent.getFray(getCameraPlayer()) > config.fray.grayScreenFray) {
			System.out.println(overlayOpacity);
			this.renderOverlay(new Identifier("dimdoors", "textures/other/grey_vingette.png"), overlayOpacity);
		}
	}
	private void renderOverlay(Identifier texture, float opacity) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
		RenderSystem.setShaderTexture(0, texture);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0.0D, (double)this.scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
		bufferBuilder.vertex((double)this.scaledWidth, (double)this.scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
		bufferBuilder.vertex((double)this.scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
		bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
		tessellator.draw();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}
}
