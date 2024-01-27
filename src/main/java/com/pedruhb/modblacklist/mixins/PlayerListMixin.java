package com.pedruhb.modblacklist.mixins;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import com.pedruhb.modblacklist.main.Actions;
import com.pedruhb.modblacklist.main.Config;
import com.pedruhb.modblacklist.main.Main;
import com.pedruhb.modblacklist.main.ModAction;

import java.net.SocketAddress;
import java.util.Date;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.slf4j.Logger;

@Mixin({ PlayerList.class })
public class PlayerListMixin {

    private static final Logger LOGGER = LogUtils.getLogger();
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(at = { @At("HEAD") }, method = { "canPlayerLogin" }, cancellable = true)
    public void canPlayerLogin(SocketAddress socket, GameProfile profile, CallbackInfoReturnable<Component> info) {

        LOGGER.info("Player se conectando ao mapa... Modlist:");

        Connection con = (Connection) Main.PROFILES.get(socket);
        ConnectionData connection = NetworkHooks.getConnectionData(con);

        if (connection != null) {
            for (int i = 0; i < connection.getModList().size(); ++i) {

                String mod = (String) connection.getModList().get(i);

                LOGGER.info(mod + " Ilegal? " + Main.MODLIST.containsKey(mod));


                if (!Config.INSTANCE.getLockedModIDs().contains(mod) && Main.MODLIST.containsKey(mod)) {

                    LOGGER.info("Mod ilegal encontrado: " + mod);

                    Actions action = Main.MODLIST.get(mod);
                    ModAction ac = action.getAction();
                    String msg = Config.INSTANCE.getDefaultMessage(mod);

                    if (ac == null) {
                        ac = Config.INSTANCE.getDefaultAction();
                    }

                    if (action.getMsg(mod) != null) {
                        msg = action.getMsg(mod);
                    }

                    if (ac == ModAction.BAN) {
                        UserBanList user_ban_list = server.getPlayerList().getBans();
                        UserBanListEntry profile_ban_entry = new UserBanListEntry(profile, null, "Server", null, msg);
                        user_ban_list.add(profile_ban_entry);
                        info.setReturnValue(new TextComponent("You have been banned\n\n" + msg));
                    }

                }
            }
        }

        /*
         * Connection con = (Connection) Main.PROFILES.get(p_206258_1_);
         * ConnectionData connection = NetworkHooks.getConnectionData(con);
         * if (connection != null) {
         * for (int i = 0; i < connection.getModList().size(); ++i) {
         * String mod = (String) connection.getModList().get(i);
         * 
         * LOGGER.info(mod);
         * 
         * if (!Config.INSTANCE.getLockedModIDs().contains(mod) &&
         * Main.MODLIST.containsKey(mod)) {
         * 
         * LOGGER.error("Usuário entrando com mod proibido! " + mod);
         * 
         * 
         * Actions action = Main.MODLIST.get(mod);
         * String msg = Config.INSTANCE.getDefaultMessage(mod);
         * ModAction ac = action.getAction();
         * if (ac == null) {
         * ac = Config.INSTANCE.getDefaultAction();
         * }
         * 
         * if (action.getMsg(mod) != null) {
         * msg = action.getMsg(mod);
         * }
         * 
         * if (ac == ModAction.BAN) {
         * UserBanListEntry profilebanentry = new UserBanListEntry(p_206258_2_, (Date)
         * null, "Server",
         * (Date) null, msg);
         * this.m_11295_().m_11381_(profilebanentry);
         * info.setReturnValue(Component.m_237113_("You have been banned\n\n" + msg));
         * 
         * } else if (ac == ModAction.KICK) {
         * info.setReturnValue(Component.m_237113_("Unable to connect\n\n" + msg));
         * } else {
         * String[] var11 = this.m_11307_().m_5875_();
         * int var12 = var11.length;
         * 
         * for (int var13 = 0; var13 < var12; ++var13) {
         * String playerName = var11[var13];
         * ServerPlayer player = this.f_11195_.m_6846_().m_11255_(playerName);
         * String var10001 = p_206258_2_.getName();
         * player.m_5661_(
         * Component
         * .m_237113_("[Warning] " + var10001 + " has logged in with mod: " + mod
         * + (action.getMsg(mod) != null ? "(" + action.getMsg(mod) + ")"
         * : ""))
         * .m_130940_(ChatFormatting.GRAY).m_130940_(ChatFormatting.ITALIC),
         * false);
         * }
         * }
         * 
         * }
         * }
         * }
         */

    }

}
