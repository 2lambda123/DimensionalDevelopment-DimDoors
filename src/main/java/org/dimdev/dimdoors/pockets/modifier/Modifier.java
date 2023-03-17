package org.dimdev.dimdoors.pockets.modifier;

import java.util.Collection;
import java.util.function.Supplier;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import org.dimdev.dimdoors.DimensionalDoors;
import org.dimdev.dimdoors.api.util.ReferenceSerializable;
import org.dimdev.dimdoors.api.util.ResourceUtil;
import org.dimdev.dimdoors.pockets.PocketGenerationContext;
import org.dimdev.dimdoors.world.pocket.type.Pocket;

public interface Modifier extends ReferenceSerializable {
	Registry<ModifierType<? extends Modifier>> REGISTRY = FabricRegistryBuilder.from(new MappedRegistry<ModifierType<? extends Modifier>>(ResourceKey.createRegistryKey(DimensionalDoors.resource("modifier_type")), Lifecycle.stable(), false)).buildAndRegister();

	String RESOURCE_STARTING_PATH = "pockets/modifier"; //TODO: might want to restructure data packs

	static Modifier deserialize(Tag nbt, ResourceManager manager) {
		switch (nbt.getId()) {
			case Tag.TAG_COMPOUND: // It's a serialized Modifier
				return Modifier.deserialize((CompoundTag) nbt, manager);
			case Tag.TAG_STRING: // It's a reference to a resource location
				// TODO: throw if manager is null
				return ResourceUtil.loadReferencedResource(manager, RESOURCE_STARTING_PATH, nbt.getAsString(), ResourceUtil.NBT_READER.andThenComposable(nbtElement -> deserialize(nbtElement, manager)));
			default:
				throw new RuntimeException(String.format("Unexpected NbtType %d!", nbt.getId()));
		}
	}

	static Modifier deserialize(Tag nbt) {
		return deserialize(nbt, null);
	}

	static Modifier deserialize(CompoundTag nbt, ResourceManager manager) {
		ResourceLocation id = ResourceLocation.tryParse(nbt.getString("type")); // TODO: return some NONE Modifier if type cannot be found or deserialization fails.
		return REGISTRY.get(id).fromNbt(nbt, manager);
	}

	static Modifier deserialize(CompoundTag nbt) {
		return deserialize(nbt, null);
	}

	static Tag serialize(Modifier modifier, boolean allowReference) {
		return modifier.toNbt(new CompoundTag(), allowReference);
	}

	static Tag serialize(Modifier modifier) {
		return serialize(modifier, false);
	}


	Modifier fromNbt(CompoundTag nbt, ResourceManager manager);

	default Modifier fromNbt(CompoundTag nbt) {
		return fromNbt(nbt, null);
	}

	default Tag toNbt(CompoundTag nbt, boolean allowReference) {
		return this.getType().toNbt(nbt);
	}

	default Tag toNbt(CompoundTag nbt) {
		return toNbt(nbt, false);
	}

	void setResourceKey(String resourceKey);

	String getResourceKey();

	default void processFlags(Multimap<String, String> flags) {
		// TODO: discuss some flag standardization
		Collection<String> reference = flags.get("reference");
		if (reference.stream().findFirst().map(string -> string.equals("local") || string.equals("global")).orElse(false)) {
			setResourceKey(flags.get("resource_key").stream().findFirst().orElse(null));
		}
	}

	ModifierType<? extends Modifier> getType();

	String getKey();

	void apply(PocketGenerationContext parameters, RiftManager manager);

	void apply(PocketGenerationContext parameters, Pocket.PocketBuilder<?, ?> builder);

	interface ModifierType<T extends Modifier> {
		ModifierType<ShellModifier> SHELL_MODIFIER_TYPE = register(DimensionalDoors.resource(ShellModifier.KEY), ShellModifier::new);
		ModifierType<DimensionalDoorModifier> DIMENSIONAL_DOOR_MODIFIER_TYPE = register(DimensionalDoors.resource(DimensionalDoorModifier.KEY), DimensionalDoorModifier::new);
		ModifierType<PocketEntranceModifier> PUBLIC_MODIFIER_TYPE = register(DimensionalDoors.resource(PocketEntranceModifier.KEY), PocketEntranceModifier::new);
		ModifierType<RiftDataModifier> RIFT_DATA_MODIFIER_TYPE = register(DimensionalDoors.resource(RiftDataModifier.KEY), RiftDataModifier::new);
		ModifierType<RelativeReferenceModifier> RELATIVE_REFERENCE_MODIFIER_TYPE = register(DimensionalDoors.resource(RelativeReferenceModifier.KEY), RelativeReferenceModifier::new);
		ModifierType<OffsetModifier> OFFSET_MODIFIER_TYPE = register(DimensionalDoors.resource(OffsetModifier.KEY), OffsetModifier::new);
		ModifierType<AbsoluteRiftBlockEntityModifier> ABSOLUTE_RIFT_BLOCK_ENTITY_MODIFIER_TYPE = register(DimensionalDoors.resource(AbsoluteRiftBlockEntityModifier.KEY), AbsoluteRiftBlockEntityModifier::new);

		Modifier fromNbt(CompoundTag nbt, ResourceManager manager);

		default Modifier fromNbt(CompoundTag nbt) {
			return fromNbt(nbt, null);
		}

		CompoundTag toNbt(CompoundTag nbt);

		static void register() {
			DimensionalDoors.apiSubscribers.forEach(d -> d.registerModifierTypes(REGISTRY));
		}

		static <U extends Modifier> ModifierType<U> register(ResourceLocation id, Supplier<U> factory) {
			return Registry.register(REGISTRY, id, new ModifierType<U>() {
				@Override
				public Modifier fromNbt(CompoundTag nbt, ResourceManager manager) {
					return factory.get().fromNbt(nbt, manager);
				}

				@Override
				public CompoundTag toNbt(CompoundTag nbt) {
					nbt.putString("type", id.toString());
					return nbt;
				}
			});
		}
	}
}
