package org.sashaiolh.iolhchatbot.Commands;



import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.sashaiolh.iolhchatbot.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.sashaiolh.iolhchatbot.IolhChatBot.configManager;
import static org.sashaiolh.iolhchatbot.IolhChatBot.rulesManager;

public class ReloadCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("chatbot")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                            .executes(context -> {
                                IolhChatBot.registerConfigs();
                                sendMessage(context.getSource().getPlayerOrException(), "Конфиг бота был перезагружен.");
                                return 1;
                            })
//                        .then(Commands.literal("rules")
//                                .executes(context -> {
//                                    rulesManager = new RulesManager();
//                                    RulesManager.main(rulesManager);
//                                    sendMessage(context.getSource().getPlayerOrException(), "Список правил был обновлён");
//                                    return 1;
//                                })
//                        ) // SOON

        )
        );
    }

    private static void sendMessage(ServerPlayer player, String message) {
        player.displayClientMessage(Component.literal(message), false);
    }
}
