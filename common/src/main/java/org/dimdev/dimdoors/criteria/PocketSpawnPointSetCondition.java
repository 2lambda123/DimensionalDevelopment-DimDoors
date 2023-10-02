package org.dimdev.dimdoors.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.dimdev.dimdoors.DimensionalDoors;

public class PocketSpawnPointSetCondition extends SimpleCriterionTrigger<PocketSpawnPointSetCondition.Conditions> {
	public static final ResourceLocation ID = DimensionalDoors.id("pocket_spawn_point_set");

	@Override
	protected Conditions createInstance(JsonObject jsonObject, ContextAwarePredicate composite, DeserializationContext deserializationContext) {
		return new Conditions(composite);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, t -> true);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		public Conditions(ContextAwarePredicate playerPredicate) {
			super(ID, playerPredicate);
		}
	}
}
