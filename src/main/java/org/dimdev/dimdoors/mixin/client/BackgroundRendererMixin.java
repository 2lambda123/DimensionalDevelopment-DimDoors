package org.dimdev.dimdoors.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import net.fabricmc.api.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.FogRenderer;

@OnlyIn(Dist.CLIENT)
@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
//    @ModifyVariable(
//            method = "render",
//            at = @At(value = "STORE", ordinal = 0),
//            ordinal = 0
//    )
//    private static double modifyVoidColor(double scale) {
//        if(ModDimensions.isPrivatePocketDimension(MinecraftClient.getInstance().world)) {
//            scale = 1.0;
//        }
//        return scale;
//    }
}
