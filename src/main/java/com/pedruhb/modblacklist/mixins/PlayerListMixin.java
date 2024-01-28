package com.pedruhb.modblacklist.mixins;

import com.mojang.authlib.GameProfile;
import com.pedruhb.modblacklist.main.Config;
import com.pedruhb.modblacklist.main.Main;

import java.net.SocketAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
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

@Mixin({ PlayerList.class })
public class PlayerListMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(at = { @At("HEAD") }, method = { "canPlayerLogin" }, cancellable = true)
    public void canPlayerLogin(SocketAddress socket, GameProfile profile, CallbackInfoReturnable<Component> info) {

        Connection con = (Connection) Main.PROFILES.get(socket);
        ConnectionData connection = NetworkHooks.getConnectionData(con);

        if (connection != null) {

            for (int i = 0; i < connection.getModList().size(); ++i) {

                String mod = (String) connection.getModList().get(i);

                if (Config.list_ban.contains(mod)) {

                    UserBanList user_ban_list = server.getPlayerList().getBans();
                    UserBanListEntry profile_ban_entry = new UserBanListEntry(profile, null, "Server", null,
                            "Banned because are using " + mod + " mod.");
                    user_ban_list.add(profile_ban_entry);
                    info.setReturnValue(new TextComponent(
                            "You have been banned because you can't use " + mod + " mod on this server!"));

                } else if (Config.list_kick.contains(mod)) {

                    info.setReturnValue(new TextComponent(
                            "You have been kicked because you can't use " + mod + " mod on this server!"));

                }
            }
        }

    }

}
