package org.dimdev.dimdoors.pockets.virtual.selection;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.dimdoors.pockets.virtual.AbstractVirtualPocket;
import org.dimdev.dimdoors.pockets.virtual.VirtualPocket;
import org.dimdev.dimdoors.pockets.virtual.reference.PocketGeneratorReference;
import org.dimdev.dimdoors.pockets.PocketGenerationContext;
import org.dimdev.dimdoors.api.util.math.Equation;
import org.dimdev.dimdoors.world.pocket.type.Pocket;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConditionalSelector implements AbstractVirtualPocket {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String KEY = "conditional";

	private LinkedHashMap<String, VirtualPocket> pocketMap = Maps.newLinkedHashMap();
	private LinkedHashMap<String, Equation> equationMap = Maps.newLinkedHashMap();

	public ConditionalSelector() {
	}

	public ConditionalSelector(LinkedHashMap<String, VirtualPocket> pocketMap) {
		this.pocketMap = pocketMap;
	}

	public LinkedHashMap<String, VirtualPocket> getPocketMap() {
		return pocketMap;
	}

	@Override
	public AbstractVirtualPocket fromTag(CompoundTag tag) {
		ListTag conditionalPockets = tag.getList("pockets", 10);
		for (int i = 0; i < conditionalPockets.size(); i++) {
			CompoundTag pocket = conditionalPockets.getCompound(i);
			String condition = pocket.getString("condition");
			if (pocketMap.containsKey(condition)) continue;
			try {
				equationMap.put(condition, Equation.parse(condition));
				pocketMap.put(condition, VirtualPocket.deserialize(pocket.get("pocket")));
			} catch (Equation.EquationParseException e) {
				LOGGER.error("Could not parse pocket condition equation!", e);
			}
		}
		return this;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		AbstractVirtualPocket.super.toTag(tag);

		ListTag conditionalPockets = new ListTag();
		pocketMap.forEach((condition, pocket) -> {
			CompoundTag compound = new CompoundTag();
			compound.putString("condition", condition);
			compound.put("pocket", VirtualPocket.serialize(pocket));
			conditionalPockets.add(compound);
		});
		tag.put("pockets", conditionalPockets);
		return tag;
	}

	@Override
	public Pocket prepareAndPlacePocket(PocketGenerationContext parameters) {
		return getNextPocket(parameters).prepareAndPlacePocket(parameters);
	}

	@Override
	public PocketGeneratorReference getNextPocketGeneratorReference(PocketGenerationContext parameters) {
		return getNextPocket(parameters).getNextPocketGeneratorReference(parameters);
	}

	@Override
	public PocketGeneratorReference peekNextPocketGeneratorReference(PocketGenerationContext parameters) {
		return getNextPocket(parameters).peekNextPocketGeneratorReference(parameters);
	}

	@Override
	public void init() {
		pocketMap.values().forEach(VirtualPocket::init);
	}

	@Override
	public VirtualPocketType<? extends AbstractVirtualPocket> getType() {
		return VirtualPocketType.CONDITIONAL_SELECTOR;
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public double getWeight(PocketGenerationContext parameters) {
		return getNextPocket(parameters).getWeight(parameters);
	}

	private VirtualPocket getNextPocket(PocketGenerationContext parameters) {
		for (Map.Entry<String, VirtualPocket> entry : pocketMap.entrySet()) {
			if (equationMap.get(entry.getKey()).asBoolean(parameters.toVariableMap(new HashMap<>()))) {
				return entry.getValue();
			}
		}
		return pocketMap.values().stream().findFirst().orElse(NoneVirtualPocket.NONE);
	}
}
