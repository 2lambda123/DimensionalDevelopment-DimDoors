package org.dimdev.dimdoors.rift.targets;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.dimdev.dimdoors.api.rift.target.EntityTarget;
import org.dimdev.dimdoors.api.util.EntityUtils;
import org.dimdev.dimdoors.api.util.Location;

public class PocketExitMarker extends VirtualTarget implements EntityTarget {
	public static final MapCodec<PocketExitMarker> CODEC = MapCodec.unit(PocketExitMarker::new);

	public PocketExitMarker() {
	}

	@Override
	public boolean receiveEntity(Entity entity, Vec3 relativePos, Rotations relativeAngle, Vec3 relativeVelocity, Location location) {
		EntityUtils.chat(entity, Component.literal("The exit of this dungeon has not been linked. If this is a normally generated pocket, please report this bug."));
		return false;
	}

	@Override
	public VirtualTargetType<? extends VirtualTarget> getType() {
		return VirtualTargetType.POCKET_EXIT.get();
	}

	@Override
	public VirtualTarget copy() {
		return this;
	}
}
