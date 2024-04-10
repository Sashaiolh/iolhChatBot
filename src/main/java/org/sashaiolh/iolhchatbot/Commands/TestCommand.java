package org.sashaiolh.iolhchatbot.Commands;



import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TestCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("test")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            sendMessage(context.getSource().getPlayerOrException(), "Привет! Это тестовая команда.");
                            return 1;
                        })
        );
    }

    private static void sendMessage(ServerPlayer player, String message) {
        player.displayClientMessage(Component.literal(message), false);
    }
}
