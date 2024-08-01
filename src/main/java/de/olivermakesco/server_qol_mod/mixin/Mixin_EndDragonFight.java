package de.olivermakesco.server_qol_mod.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndDragonFight.class)
public class Mixin_EndDragonFight {
    @ModifyExpressionValue(
            method = "setDragonKilled",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/dimension/end/EndDragonFight;previouslyKilled:Z",
                    ordinal = 0
            )
    )
    private boolean alwaysPlaceEgg(boolean original) {
        return false;
    }
}
