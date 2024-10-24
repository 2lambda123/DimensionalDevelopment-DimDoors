package org.dimdev.dimdoors.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderInstance;

@Environment(EnvType.CLIENT)
public class ModShaders {
	private static ShaderInstance DIMENSIONAL_PORTAL = null;

	public static void setDimensionalPortal(ShaderInstance dimensionalPortal) {
		DIMENSIONAL_PORTAL = dimensionalPortal;
	}

	public static ShaderInstance getDimensionalPortal() {
		return DIMENSIONAL_PORTAL;
	}
}
