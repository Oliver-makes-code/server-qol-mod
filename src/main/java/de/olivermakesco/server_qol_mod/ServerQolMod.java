package de.olivermakesco.server_qol_mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerQolMod implements ModInitializer {
    public static final String MODID = "server_qol_mod";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);

    @SuppressWarnings("UnstableApiUsage")
    public static final AttachmentType<PlayerConfigAttachment> PLAYER_CONFIG_TYPE = AttachmentRegistry.<PlayerConfigAttachment>builder()
            .persistent(PlayerConfigAttachment.CODEC)
            .initializer(PlayerConfigAttachment::new)
            .copyOnDeath()
            .buildAndRegister(rl("player_config"));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(ModCommands::register);

        ItemDefaultInstanceModification.add(stack -> {
            if (
                    stack.is(Items.POTION) ||
                    stack.is(Items.LINGERING_POTION) ||
                    stack.is(Items.SPLASH_POTION) ||
                    (stack.getItem() instanceof BucketItem && stack.getItem() != Items.BUCKET) ||
                    stack.getItem() instanceof SolidBucketItem ||
                    stack.getItem().components().has(DataComponents.JUKEBOX_PLAYABLE)
            )
                stack.set(DataComponents.MAX_STACK_SIZE, 16);
        });
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
