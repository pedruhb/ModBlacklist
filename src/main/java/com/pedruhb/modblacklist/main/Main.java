package com.pedruhb.modblacklist.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.pedruhb.modblacklist.command.CommandBanMod;
import com.pedruhb.modblacklist.command.CommandBannedModList;
import com.pedruhb.modblacklist.command.CommandKickMod;
import com.pedruhb.modblacklist.main.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import net.minecraft.network.Connection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("modblacklist")
@Mod.EventBusSubscriber(modid = "modblacklist", bus = Mod.EventBusSubscriber.Bus.FORGE)

public class Main {

   public static Map<SocketAddress, Connection> PROFILES = new HashMap();
   public static Map<Mod, ModAction> MODLIST = new HashMap();
   private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final Logger LOGGER = LogUtils.getLogger();

   public Main() {
      Config.load();
      load();
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public static void RegisterCommads(RegisterCommandsEvent event) {
      CommandBanMod.register(event.getDispatcher());
      CommandBannedModList.register(event.getDispatcher());
      CommandKickMod.register(event.getDispatcher());
   }

   public static void load() {

      File file = new File("config/modblacklist_data.json");
      if (!file.exists()) {
         save();
      }

      try {
         BufferedReader reader = new BufferedReader(new FileReader(file));
         StringBuilder sb = new StringBuilder();
         String s = null;

         while ((s = reader.readLine()) != null) {
            sb.append(s);
         }

         reader.close();

         MODLIST = GSON.fromJson(sb.toString(), null);

         LOGGER.info("----------- modlist size " + MODLIST.size());

         LOGGER.info("----------- modlist json " + sb.toString());

      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void save() {
      File file = new File("config/modblacklist_data.json");
      if (!file.getParentFile().exists()) {
         file.getParentFile().mkdirs();
      }

      try {
         FileWriter writer = new FileWriter(file);
         writer.write(GSON.toJson(MODLIST));
         writer.close();
      } catch (Exception var2) {
      }

   }
}