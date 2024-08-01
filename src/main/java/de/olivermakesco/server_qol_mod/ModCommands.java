package de.olivermakesco.server_qol_mod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(LiteralArgumentBuilder
            .<CommandSourceStack>literal("self")
            .then(LiteralArgumentBuilder
                .<CommandSourceStack>literal("keepInventory")
                .then(RequiredArgumentBuilder
                    .<CommandSourceStack, Boolean>argument("value", BoolArgumentType.bool())
                    .executes(ModCommands::setKeepInventory)
                )
                .executes(ModCommands::getKeepInventory)
            )
            .then(LiteralArgumentBuilder
                .<CommandSourceStack>literal("acceptPvp")
                .then(RequiredArgumentBuilder
                    .<CommandSourceStack, Boolean>argument("value", BoolArgumentType.bool())
                    .executes(ModCommands::setPvp)
                )
                .executes(ModCommands::getPvp)
            )
        );
    }

    private static int getPvp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();

        var conf = PlayerConfigAttachment.get(player);

        context.getSource().sendSuccess(() -> Component.literal(String.format("Value acceptPvp set to %s", conf.shouldAcceptPvp)), true);

        return 0;
    }

    private static int getKeepInventory(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();

        var conf = PlayerConfigAttachment.get(player);

        context.getSource().sendSuccess(() -> Component.literal(String.format("Value keepInventory set to %s", conf.shouldKeepInventory)), true);

        return 0;
    }

    private static int setPvp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var value = BoolArgumentType.getBool(context, "value");

        var conf = PlayerConfigAttachment.get(player);

        conf.shouldAcceptPvp = value;

        PlayerConfigAttachment.set(player, conf);

        context.getSource().sendSuccess(() -> Component.literal(String.format("Value acceptPvp set to %s", value)), true);
        return 0;
    }

    private static int setKeepInventory(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var value = BoolArgumentType.getBool(context, "value");

        var conf = PlayerConfigAttachment.get(player);

        conf.shouldKeepInventory = value;

        PlayerConfigAttachment.set(player, conf);

        context.getSource().sendSuccess(() -> Component.literal(String.format("Value keepInventory set to %s", value)), true);
        return 0;
    }
}
