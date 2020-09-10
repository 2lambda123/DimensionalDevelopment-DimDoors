package org.dimdev.dimdoors.rift.targets;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

/**
 * Helps flow (fluid, redstone, power) senders to keep track of flow received by the
 * target rift.
 */

public class FlowTracker { // TODO
    //@Saved public Map<Direction, Map<Fluid, Integer>> fluids = new HashMap<>();
    public Map<Direction, Integer> redstone = new HashMap<>();
    public Map<Direction, Integer> power = new HashMap<>();

    public void fromTag(CompoundTag nbt) {
    }

    public CompoundTag toTag(CompoundTag nbt) {
        return nbt;
    }
}
