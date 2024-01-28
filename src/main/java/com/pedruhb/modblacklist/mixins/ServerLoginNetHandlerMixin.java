package com.pedruhb.modblacklist.mixins;

import com.mojang.logging.LogUtils;
import com.pedruhb.modblacklist.main.Main;

import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ServerLoginPacketListenerImpl.class })
public class ServerLoginNetHandlerMixin {

    @Shadow
    @Final
    public Connection connection;
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(at = { @At("HEAD") }, method = { "handleAcceptedLogin" })
    public void handleAcceptedLogin(CallbackInfo info) {
        Main.PROFILES.put(this.connection.getRemoteAddress(), this.connection);
    }

}