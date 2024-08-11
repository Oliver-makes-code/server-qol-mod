package de.olivermakesco.server_qol_mod;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemDefaultInstanceModification {
    private static final List<Consumer<ItemStack>> CONSUMERS = new ArrayList<>();

    public static void add(Consumer<ItemStack> consumer) {
        CONSUMERS.add(consumer);
    }

    public static void modify(ItemStack stack) {
        for (var consumer : CONSUMERS)
            consumer.accept(stack);
    }
}
