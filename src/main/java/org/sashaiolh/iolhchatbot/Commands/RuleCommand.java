package org.sashaiolh.iolhchatbot.Commands;



import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.sashaiolh.iolhchatbot.IolhChatBot;
import org.sashaiolh.iolhchatbot.RulesManager;
import org.sashaiolh.iolhchatbot.Utils.Rule;

import java.io.IOException;
import java.util.List;

public class RuleCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, RulesManager rulesManager) {
        List<Rule> allRules = rulesManager.getAllRules();
            for (Rule rule : allRules) {
                dispatcher.register(
                        Commands.literal("rule")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.literal(rule.getId())
                                        .executes(context -> {
                                            String ruleText = rule.getContent().replaceAll("<n>", "\n").replaceAll("<l>", "§l").replaceAll("<r>","§r").replaceAll("#c1", "§6").replaceAll("#c2", "§c");
                                            String ruleMessage = "§c"+rule.getId()+": "+"§6"+ruleText;
                                            sendMessage(context.getSource().getPlayerOrException(), ruleMessage);
                                            return 1;
                                        })
                                )
                );
            }
    }

    private static void sendMessage(ServerPlayer player, String message) {
        player.displayClientMessage(Component.literal(message), false);
    }
}
