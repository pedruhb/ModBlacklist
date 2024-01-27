package com.pedruhb.modblacklist.command;

import org.slf4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;
import com.pedruhb.modblacklist.main.Config;
import com.pedruhb.modblacklist.main.Main;
import com.pedruhb.modblacklist.main.Actions;
import com.pedruhb.modblacklist.main.ModAction;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class CommandBanMod {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("modblacklist_ban").requires(commandSource -> commandSource.hasPermission(3)).then(Commands.argument("modId", StringArgumentType.string()).executes(context -> {

                    String modid = StringArgumentType.getString(context, "modId");

                    LOGGER.debug(context.getSource().getPlayerOrException().getName().toString());
                    
                    if (Config.INSTANCE.getLockedModIDs().contains(modid)) {
            
                        context.getSource().getEntity().sendMessage(new TextComponent(ChatFormatting.RED + "Mod: " + modid + " cannot be banned"), null);
                                
                        return 0;
            
                    } else {
                        
                        Main.MODLIST.put(modid, new Actions("You cant use this mod on server.", ModAction.BAN));
            
                        context.getSource().getEntity().sendMessage(new TextComponent(ChatFormatting.GREEN + "Added " + ChatFormatting.YELLOW + modid + ChatFormatting.GREEN + " to the ban list."),null);
            
                        
                        Main.save();
            
                        return 0;
                    }

        })));

    }

}
