package com.pedruhb.modblacklist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.pedruhb.modblacklist.main.Main;
import com.pedruhb.modblacklist.main.Actions;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import java.util.Map;

public class CommandBannedModList {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("modblacklist_list")
                .requires(commandSource -> commandSource.hasPermission(3)).executes(context -> {

                    context.getSource().getEntity()
                            .sendMessage(new TextComponent(ChatFormatting.GREEN + "----------------------"), null);
                    context.getSource().getEntity().sendMessage(new TextComponent(ChatFormatting.GOLD + "Banned Mods"),
                            null);

                    if (Main.MODLIST.size() > 0) {

                        for (Map.Entry<String, Actions> set : Main.MODLIST.entrySet()) {

                            String key = set.getKey();
                            Actions action = set.getValue();

                            context.getSource().getEntity().sendMessage(
                                    new TextComponent(ChatFormatting.GOLD + "ModID: " + ChatFormatting.GRAY + key),
                                    null);
                            context.getSource().getEntity().sendMessage(
                                    new TextComponent(ChatFormatting.GOLD + "Action: " + action.getMsg(key)), null);

                        }

                    } else {

                        context.getSource().getEntity()
                                .sendMessage(new TextComponent(ChatFormatting.GOLD + "Currently no banned mods"), null);
                        context.getSource().getEntity()
                                .sendMessage(new TextComponent(ChatFormatting.GREEN + "----------------------"), null);

                    }

                    return 0;

                }));

    }

}
