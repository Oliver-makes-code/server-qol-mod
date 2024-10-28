package de.olivermakesco.server_qol_mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerQolMod implements ModInitializer {
    public static final String MODID = "server_qol_mod";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);
    public static MinecraftServer serverInstance;

    @SuppressWarnings("UnstableApiUsage")
    public static final AttachmentType<PlayerConfigAttachment> PLAYER_CONFIG_TYPE = AttachmentRegistry.<PlayerConfigAttachment>builder()
            .persistent(PlayerConfigAttachment.CODEC)
            .initializer(PlayerConfigAttachment::new)
            .copyOnDeath()
            .buildAndRegister(rl("player_config"));

    public static final TagKey<Item> SIXTEEN_STACKABLES = TagKey.create(Registries.ITEM, rl("sixteen_stackables"));
    public static final TagKey<Item> THIRTY_TWO_STACKABLES = TagKey.create(Registries.ITEM, rl("thirty_two_stackables"));
    public static final TagKey<Item> SIXTY_FOUR_STACKABLES = TagKey.create(Registries.ITEM, rl("sixty_four_stackables"));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(ModCommands::register);
        ModGamerules.init();

        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            serverInstance = server;
        });

        ItemDefaultInstanceModification.add(stack -> {
            if (!ModGamerules.getBoolean(ModGamerules.STACKABLE_ITEMS))
                return;

            if (stack.is(SIXTEEN_STACKABLES))
                stack.set(DataComponents.MAX_STACK_SIZE, 16);

            if (stack.is(THIRTY_TWO_STACKABLES))
                stack.set(DataComponents.MAX_STACK_SIZE, 32);

            if (stack.is(SIXTY_FOUR_STACKABLES))
                stack.set(DataComponents.MAX_STACK_SIZE, 64);
        });
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
