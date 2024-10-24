package org.dimdev.dimdoors.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public interface UseItemOnBlockCallback {
	Event<UseItemOnBlockCallback> EVENT = EventFactory.of(
			listeners -> (player, world, hand, hitresult) -> {
				for (UseItemOnBlockCallback event : listeners) {
					InteractionResult result = event.useItemOnBlock(player, world, hand, hitresult);

					if (result != InteractionResult.PASS) {
						return result;
					}
				}

				return InteractionResult.PASS;
			}
	);

	InteractionResult useItemOnBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult);
}
