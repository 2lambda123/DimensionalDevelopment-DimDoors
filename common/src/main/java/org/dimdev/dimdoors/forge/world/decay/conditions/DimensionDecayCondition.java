package org.dimdev.dimdoors.forge.world.decay.conditions;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import org.dimdev.dimdoors.forge.world.decay.DecayCondition;
import org.dimdev.dimdoors.forge.world.decay.DecayConditionType;
import org.dimdev.dimdoors.forge.world.decay.DecaySource;

public class DimensionDecayCondition extends GenericDecayCondition<DimensionType> {
    public static Codec<DimensionDecayCondition> CODEC = createCodec(DimensionDecayCondition::new, Registry.DIMENSION_TYPE_REGISTRY);
    public static final String KEY = "dimension";

    private DimensionDecayCondition(TagOrElementLocation<DimensionType> tagOrElementLocation, boolean invert) {
        super(tagOrElementLocation, invert);
    }

    public static DimensionDecayCondition of(TagKey<DimensionType> tag, boolean invert) {
        return new DimensionDecayCondition(TagOrElementLocation.of(tag, Registry.DIMENSION_TYPE_REGISTRY), invert);
    }

    public static DimensionDecayCondition of(TagKey<DimensionType> tag) {
        return new DimensionDecayCondition(TagOrElementLocation.of(tag, Registry.DIMENSION_TYPE_REGISTRY), false);
    }

    public static DimensionDecayCondition of(ResourceKey<DimensionType> key, boolean invert) {
        return new DimensionDecayCondition(TagOrElementLocation.of(key, Registry.DIMENSION_TYPE_REGISTRY), invert);
    }

    public static DimensionDecayCondition of(ResourceKey<DimensionType> key) {
        return new DimensionDecayCondition(TagOrElementLocation.of(key, Registry.DIMENSION_TYPE_REGISTRY), false);
    }

    @Override
    public DecayConditionType<? extends DecayCondition> getType() {
        return DecayConditionType.DIMENSION_CONDITION_TYPE.get();
    }

    @Override
    public Holder<DimensionType> getHolder(Level world, BlockPos pos, BlockState origin, BlockState targetBlock, FluidState targetFluid, DecaySource source) {
        return world.dimensionTypeRegistration();
    }
}
