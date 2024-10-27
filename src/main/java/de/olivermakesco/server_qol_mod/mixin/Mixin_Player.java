package de.olivermakesco.server_qol_mod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.olivermakesco.server_qol_mod.ModGamerules;
import de.olivermakesco.server_qol_mod.PlayerConfigAttachment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class Mixin_Player {
    @WrapOperation(
            method = "dropEquipment",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
            )
    )
    private boolean dropEquipment_checkKeepInventory(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Operation<Boolean> original) {
        if (!ModGamerules.getBoolean(ModGamerules.PER_PLAYER_KEEP_INVENTORY))
            return original.call(instance, key);

        var self = (Player)(Object)this;

        if (key == GameRules.RULE_KEEPINVENTORY && PlayerConfigAttachment.get(self).shouldKeepInventory)
            return true;

        return original.call(instance, key);
    }

    @WrapOperation(
            method = "getBaseExperienceReward",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
            )
    )
    private boolean getBaseExperienceReward_checkKeepInventory(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Operation<Boolean> original) {
        if (!ModGamerules.getBoolean(ModGamerules.PER_PLAYER_KEEP_INVENTORY))
            return original.call(instance, key);

        var self = (Player)(Object)this;

        if (key == GameRules.RULE_KEEPINVENTORY && PlayerConfigAttachment.get(self).shouldKeepInventory)
            return true;

        return original.call(instance, key);
    }
}
