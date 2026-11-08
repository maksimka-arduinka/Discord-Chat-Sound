package net.mcreator.discordchatsound.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;

import net.mcreator.discordchatsound.event.PlayerEvents;

@Mixin(Player.class)
public abstract class PlayerMixin {
	@org.spongepowered.asm.mixin.injection.Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
	private void mcreator$onItemDropped(net.minecraft.world.item.ItemStack itemstack, boolean includeThrower, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.world.entity.item.ItemEntity> cir) {
		Player player = (Player) (Object) this;
		if (!player.level().isClientSide() && cir.getReturnValue() != null) {
			net.mcreator.discordchatsound.event.PlayerEvents.ITEM_DROPPED.invoker().onItemDropped(player, cir.getReturnValue().getItem());
		}
	}

	@Inject(method = "giveExperiencePoints(I)V", at = @At("HEAD"))
	public void giveExperiencePoints(int amount, CallbackInfo ci) {
		PlayerEvents.XP_CHANGE.invoker().onXpChange((Player) (Object) this, amount);
	}

	@Inject(method = "giveExperienceLevels(I)V", at = @At("HEAD"))
	public void giveExperienceLevels(int amount, CallbackInfo ci) {
		PlayerEvents.LEVEL_CHANGE.invoker().onLevelChange((Player) (Object) this, amount);
	}
}