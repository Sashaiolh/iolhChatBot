package org.sashaiolh.iolhchatbot;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
    import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.sashaiolh.iolhchatbot.Commands.ChatBotCommands;
import org.sashaiolh.iolhchatbot.Commands.ReloadCommand;
import org.sashaiolh.iolhchatbot.Commands.RuleCommand;
import org.sashaiolh.iolhchatbot.Utils.BadWord;
import org.slf4j.Logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


@Mod(IolhChatBot.MODID)
public class IolhChatBot {

    public static final String MODID = "iolhchatbot";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static ConfigManager configManager;
    public static RulesManager rulesManager;
    public static BadWordsManager badWordsManager;



    public IolhChatBot() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void registerConfigs(){
        ConfigFilesManager.init();
        configManager = new ConfigManager();

        BadWordsManager.init();
        badWordsManager = new BadWordsManager();

        rulesManager = new RulesManager();
        RulesManager.main(rulesManager);

    }


    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        registerConfigs();

        ReloadCommand.register(dispatcher);
        ChatBotCommands.register(dispatcher);

        RuleCommand.register(dispatcher, rulesManager);


        LOGGER.info("Registered commands.");
    }

    @Mod.EventBusSubscriber(modid = IolhChatBot.MODID)
    public static class ForgeBeautifulChatEvent {

        public static final double rangeTalk = 100;


        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void onServerChat(@NotNull ServerChatEvent event) throws CommandSyntaxException {
            ServerPlayer serverPlayer = event.getPlayer();
            MinecraftServer server = serverPlayer.getServer();
            String playerName = serverPlayer.getName().getString();
            String rawMessage = event.getMessage().getString();


            if (rawMessage.startsWith("!")) {
                if (Objects.equals(configManager.getConfig("autoModerationEnabled"), "true")) {
                    // Проверка на наличие плохих слов в сообщении
                    BadWordsManager badWordsConfig = new BadWordsManager();
                    List<BadWord> badWords = badWordsConfig.getBadWords();
                    ChatFilter chatFilter = new ChatFilter(badWords);

                    if (chatFilter.containsBadWord(rawMessage.toLowerCase())) {
                        CommandSourceStack sourceStack = server.createCommandSourceStack();


                        Iterable<ServerPlayer> players = server.getPlayerList().getPlayers();

                        // Задержка перед отправкой сообщения игроку
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // Отправляем сообщение каждому игроку
                                LocalTime timeNow = LocalTime.now();
                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                String formattedTime = timeNow.format(timeFormatter);

                                String botPrefix = configManager.getConfig("botPrefix");
                                String botNickname = configManager.getConfig("botNickname");
                                String muteMessage = "§8" + "[" + "§7" + formattedTime + "§8" + "]" + " " + botPrefix + " " + botNickname + "§8: " + "§6" + playerName + ", нецензурная лексика запрещена! §c/rule " + configManager.getConfig("badWordsRuleNum");
//                            try {
//                                server.getCommands().getDispatcher().execute("say test", sourceStack); //Тут будет мут
//                            } catch (CommandSyntaxException e) {
//                                throw new RuntimeException(e);
//                            }
                                for (ServerPlayer player : players) {
                                    player.displayClientMessage(Component.literal(muteMessage), false);
                                }
                            }
                        }, 300);


                    }
                }
            }
        }
    }
}