package de.olivermakesco.server_qol_mod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.olivermakesco.server_qol_mod.ModGamerules;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {"net.minecraft.world.entity.monster.EnderMan.EndermanTakeBlockGoal"})
public class Mixin_EnderMan_EndermanTakeBlockGoal {
    @WrapOperation(
            method = "canUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
            )
    )
    private boolean dontPickUp(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Operation<Boolean> original) {
        if (ModGamerules.getBoolean(ModGamerules.ENDERMEN_PICK_UP_BLOCKS))
            return original.call(instance, key);

        if (key == GameRules.RULE_MOBGRIEFING)
            return false;

        return original.call(instance, key);
    }
}
