package de.olivermakesco.server_qol_mod.mixin;

import de.olivermakesco.server_qol_mod.ItemDefaultInstanceModification;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class Mixin_ItemStack {
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V",
            at = @At("RETURN")
    )
    private void modifyDefaultInstance(ItemLike itemLike, int i, PatchedDataComponentMap patchedDataComponentMap, CallbackInfo ci) {
        ItemDefaultInstanceModification.modify((ItemStack)(Object)this);
    }
}
