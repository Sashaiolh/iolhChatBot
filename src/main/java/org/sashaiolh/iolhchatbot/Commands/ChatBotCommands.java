package org.sashaiolh.iolhchatbot.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.sashaiolh.iolhchatbot.IolhChatBot;
import org.sashaiolh.iolhchatbot.RulesManager;
import org.sashaiolh.iolhchatbot.Utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatBotCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        List<String> arguments = new ArrayList<>();
        arguments.add("rules.txt");
        arguments.add("questions.txt");
        arguments.add("badwords.txt");
        arguments.add("ChatBotConfig.cfg");
        for (String argument : arguments) {
            dispatcher.register(
                    Commands.literal("chatbot")
                            .requires(source -> source.hasPermission(2))
                            .then(Commands.literal("getfile")
                                    .then(Commands.literal(argument)
                                            .then(Commands.argument("url", StringArgumentType.greedyString())
                                                    .executes(context -> {
                                                        String filename = argument;
                                                        String fileUrl = StringArgumentType.getString(context, "url");
                                                        String saveFilePath = "config/" + IolhChatBot.MODID + "/" + filename;
                                                        RulesManager rulesManager = new RulesManager();
                                                        try {
                                                            FileUtils.saveFileFromUrl(fileUrl, saveFilePath);
//                                                            rulesManager.loadRulesFromUrl(fileUrl, saveFilePath);
                                                            sendMessage(context.getSource().getPlayerOrException(), "Файл был загружен");
                                                        } catch (IOException e) {
                                                            sendMessage(context.getSource().getPlayerOrException(), "Файл не был загружен");
                                                            throw new RuntimeException(e);
                                                        }
                                                        return 1;
                                                    })
                                            )
                                    )
                            )
            );
        }
    }

    private static void sendMessage(ServerPlayer player, String message) {
        player.displayClientMessage(Component.literal(message), false);
    }
}
