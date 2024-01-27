package com.pedruhb.modblacklist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pedruhb.modblacklist.main.Config;
import com.pedruhb.modblacklist.main.Main;
import com.pedruhb.modblacklist.main.Mod;
import com.pedruhb.modblacklist.main.Mod;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class CommandKickMod {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("modblacklist_kick").requires(commandSource -> commandSource.hasPermission(3))
                        .then(Commands.argument("modId", StringArgumentType.string()).executes(context -> {

                            String modid = StringArgumentType.getString(context, "modId");

                            if (Config.INSTANCE.getLockedModIDs().contains(modid)) {

                                context.getSource().getEntity().sendMessage(
                                        new TextComponent(ChatFormatting.RED + "Mod: " + modid + " cannot be banned"),
                                        null);

                                return 0;

                            } else {

                                Main.MODLIST.put(modid,
                                        new Mod("You cant use this mod on server.", Mod.KICK));

                                context.getSource().getEntity().sendMessage(
                                        new TextComponent(ChatFormatting.GREEN + "Added " + ChatFormatting.YELLOW
                                                + modid + ChatFormatting.GREEN + " to the kick list."),
                                        null);

                                Main.save();

                                return 0;
                            }

                        })));

    }

}
