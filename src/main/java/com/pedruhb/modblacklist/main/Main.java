package com.pedruhb.modblacklist.main;

import com.pedruhb.modblacklist.main.Main;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.Connection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod("modblacklist")
@Mod.EventBusSubscriber(modid = "modblacklist", bus = Mod.EventBusSubscriber.Bus.FORGE)

public class Main {

   public static Map<SocketAddress, Connection> PROFILES = new HashMap();

   public Main() {
      MinecraftForge.EVENT_BUS.register(this);
      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
   }

}