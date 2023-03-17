package org.dimdev.dimdoors.block;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.dimdev.dimdoors.block.entity.RiftBlockEntity;

public interface RiftProvider<T extends RiftBlockEntity> extends EntityBlock {
	T getRift(Level world, BlockPos pos, BlockState state);

	@OnlyIn(Dist.CLIENT)
	default boolean isTall(BlockState cachedState) {
		return false;
	}
}
