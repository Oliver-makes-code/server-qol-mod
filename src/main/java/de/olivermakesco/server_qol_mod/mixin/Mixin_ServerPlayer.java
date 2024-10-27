package de.olivermakesco.server_qol_mod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.olivermakesco.server_qol_mod.ModGamerules;
import de.olivermakesco.server_qol_mod.PlayerConfigAttachment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(ServerPlayer.class)
public abstract class Mixin_ServerPlayer {
    @Shadow protected abstract void onEffectsRemoved(Collection<MobEffectInstance> collection);

    @WrapOperation(
            method = "restoreFrom",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
            )
    )
    private boolean restoreFrom_checkKeepInventory(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Operation<Boolean> original, ServerPlayer old) {
        if (!ModGamerules.getBoolean(ModGamerules.PER_PLAYER_KEEP_INVENTORY))
            return original.call(instance, key);

        if (key == GameRules.RULE_KEEPINVENTORY && PlayerConfigAttachment.get(old).shouldKeepInventory)
            return true;

        return original.call(instance, key);
    }

    @WrapOperation(
            method = "canHarmPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;isPvpAllowed()Z"
            )
    )
    private boolean isPvpMutual(ServerPlayer self, Operation<Boolean> original, Player other) {
        if (!ModGamerules.getBoolean(ModGamerules.PER_PLAYER_PVP))
            return original.call(self);

        if (PlayerConfigAttachment.get(self).shouldAcceptPvp && PlayerConfigAttachment.get(other).shouldAcceptPvp)
            return original.call(self);

        return false;
    }
}
