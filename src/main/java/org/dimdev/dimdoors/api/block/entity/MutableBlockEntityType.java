package org.dimdev.dimdoors.api.block.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.mojang.datafixers.types.Type;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MutableBlockEntityType<T extends BlockEntity> extends BlockEntityType<T> {

	public MutableBlockEntityType(BlockEntityFactory<? extends T> factory, Set<Block> blocks, Type<?> type) {
		// ensure that the Set is mutable
		super(factory, new HashSet<>(blocks), type);
	}

	public boolean addBlock(Block block) {
		return this.validBlocks.add(block);
	}

	public boolean removeBlock(Block block) {
		return this.validBlocks.remove(block);
	}


	public static final class Builder<T extends BlockEntity> {
		private final BlockEntityFactory<? extends T> factory;
		private final Set<Block> blocks;

		private Builder(BlockEntityFactory<? extends T> factory, Set<Block> blocks) {
			this.factory = factory;
			this.blocks = blocks;
		}

		public static <T extends BlockEntity> org.dimdev.dimdoors.api.block.entity.MutableBlockEntityType.Builder<T> create(BlockEntityFactory<? extends T> factory, Block... blocks) {
			// ensure mutability
			return new org.dimdev.dimdoors.api.block.entity.MutableBlockEntityType.Builder<>(factory, new HashSet<>(Arrays.asList(blocks)));
		}

		public MutableBlockEntityType<T> build() {
			return build(null);
		}

		public MutableBlockEntityType<T> build(Type<?> type) {
			return new MutableBlockEntityType<>(this.factory, this.blocks, type);
		}
	}


	// exists for convenience so that no access widener for BlockEntityType.BlockEntityFactory is necessary
	@FunctionalInterface
	public interface BlockEntityFactory<T extends BlockEntity> extends BlockEntityType.BlockEntitySupplier<T> {
	}
}
