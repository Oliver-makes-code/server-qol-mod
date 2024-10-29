package de.olivermakesco.server_qol_mod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class Mixin_AnvilMenu {
    @WrapOperation(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private void decrementNotReplace(Container instance, int i, ItemStack itemStack, Operation<Void> original) {
        if (i != 1 || !itemStack.isEmpty()) {
            original.call(instance, i, itemStack);
            return;
        }
        var stack = instance.getItem(i);
        stack.shrink(1);
        instance.setItem(i, stack);
    }
}
