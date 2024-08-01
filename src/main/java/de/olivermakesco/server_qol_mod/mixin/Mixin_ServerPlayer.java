package de.olivermakesco.server_qol_mod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.olivermakesco.server_qol_mod.PlayerConfigAttachment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public class Mixin_ServerPlayer {
    @WrapOperation(
            method = "restoreFrom",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
            )
    )
    private boolean restoreFrom_checkKeepInventory(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Operation<Boolean> original, ServerPlayer old) {
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
        if (PlayerConfigAttachment.get(self).shouldAcceptPvp && PlayerConfigAttachment.get(other).shouldAcceptPvp)
            return original.call(self);

        return false;
    }
}
