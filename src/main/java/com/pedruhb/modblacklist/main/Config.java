package com.pedruhb.modblacklist.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Config {
   private String default_message = "Mod: %MOD%, is not allowed on this server";
   private ModAction default_action;
   private List<String> locked_modids;
   private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   public static Config INSTANCE;

   public Config() {
      this.default_action = ModAction.KICK;
      this.locked_modids = new ArrayList();
   }

   public List<String> getLockedModIDs() {
      return this.locked_modids;
   }

   public ModAction getDefaultAction() {
      return this.default_action;
   }

   public String getDefaultMessage(String modid) {
      
      if (this.default_message == null) {
         this.default_message = "Mod: %MOD%, is not allowed on this server";
         save(false);
      }

      return this.default_message.replaceAll("%MOD%", modid);
   }

   public static void load() {
      File file = new File("config/modblacklist.json");
      if (!file.exists()) {
         save(true);
      }

      try {
         BufferedReader reader = new BufferedReader(new FileReader(file));
         StringBuilder sb = new StringBuilder();
         String s = null;

         while((s = reader.readLine()) != null) {
            sb.append(s);
         }

         reader.close();
         INSTANCE = (Config)GSON.fromJson(sb.toString(), Config.class);
      } catch (Exception var4) {
         INSTANCE = new Config();
      }

   }

   private static void save(boolean generated) {
      File file = new File("config/modblacklist.json");
      if (!file.getParentFile().exists()) {
         file.getParentFile().mkdirs();
      }

      INSTANCE = new Config();
      if (generated) {
         INSTANCE.locked_modids.add("minecraft");
         INSTANCE.locked_modids.add("forge");
      }

      try {
         FileWriter writer = new FileWriter(file);
         writer.write(GSON.toJson(INSTANCE));
         writer.close();
      } catch (Exception var3) {
      }

   }
}