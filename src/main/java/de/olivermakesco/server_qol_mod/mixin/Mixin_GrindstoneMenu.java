package de.olivermakesco.server_qol_mod.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.olivermakesco.server_qol_mod.ServerQolMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneMenu.class)
public class Mixin_GrindstoneMenu {
    @WrapOperation(
            method = "removeNonCursesFrom",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    ItemStack removeComponent(ItemStack instance, ItemLike itemLike, Operation<ItemStack> original) {
        var result = original.call(instance, itemLike);
        result.set(DataComponents.MAX_STACK_SIZE, Items.BOOK.getDefaultMaxStackSize());
        return result;
    }
}
