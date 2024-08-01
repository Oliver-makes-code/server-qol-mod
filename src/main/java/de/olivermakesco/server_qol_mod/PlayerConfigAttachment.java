package de.olivermakesco.server_qol_mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class PlayerConfigAttachment {
    public static final Codec<PlayerConfigAttachment> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.BOOL.fieldOf("shouldKeepInventory").forGetter(PlayerConfigAttachment::shouldKeepInventory),
                    Codec.BOOL.fieldOf("shouldAcceptPvp").forGetter(PlayerConfigAttachment::shouldAcceptPvp)
            ).apply(instance, PlayerConfigAttachment::new)
    );

    public boolean shouldKeepInventory = false;
    public boolean shouldAcceptPvp = false;

    public PlayerConfigAttachment() {}

    public PlayerConfigAttachment(boolean shouldKeepInventory, boolean shouldAcceptPvp) {
        this.shouldKeepInventory = shouldKeepInventory;
        this.shouldAcceptPvp = shouldAcceptPvp;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static PlayerConfigAttachment get(Player player) {
        return player.getAttachedOrCreate(ServerQolMod.PLAYER_CONFIG_TYPE);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void set(Player player, PlayerConfigAttachment value) {
        player.setAttached(ServerQolMod.PLAYER_CONFIG_TYPE, value);
    }

    public boolean shouldKeepInventory() {
        return shouldKeepInventory;
    }

    public boolean shouldAcceptPvp() {
        return shouldAcceptPvp;
    }
}
