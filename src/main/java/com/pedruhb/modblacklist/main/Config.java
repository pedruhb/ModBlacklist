package com.pedruhb.modblacklist.main;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

@Mod.EventBusSubscriber(modid = "modblacklist", bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
   private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

   private static final ForgeConfigSpec.BooleanValue ENABLED = BUILDER
         .comment("Enable mod blacklist?")
         .define("blacklistEnabled", false);

   private static final ForgeConfigSpec.ConfigValue<String> BAN_LIST = BUILDER
         .comment("Comma Separated Mod Ids to BAN! ex: journeymap;jei;jade")
         .define("banMods", "");

   private static final ForgeConfigSpec.ConfigValue<String> KICK_LIST = BUILDER
         .comment("Comma Separated Mod Ids to KICK! ex: journeymap;jei;jade")
         .define("kickMods", "");

   static final ForgeConfigSpec SPEC = BUILDER.build();
   public static boolean enabled;
   public static Set<String> list_ban = new HashSet<String>();
   public static Set<String> list_kick = new HashSet<String>();
   private static final Logger LOGGER = LogUtils.getLogger();

   @SubscribeEvent
   static void onLoad(final ModConfigEvent event) {

      enabled = ENABLED.get();

      LOGGER.info(BAN_LIST.get());
      LOGGER.info(KICK_LIST.get());

      String[] banListSplit = BAN_LIST.get().split(";");
      for (int i = 0; i < banListSplit.length; ++i) {
         list_ban.add(banListSplit[i]);
         LOGGER.info("[ModBlacklist] Added " + banListSplit[i] + " to ban list.");
      }

      String[] kickListSplit = KICK_LIST.get().split(";");
      for (int i = 0; i < kickListSplit.length; ++i) {
         list_kick.add(kickListSplit[i]);
         LOGGER.info("[ModBlacklist] Added " + banListSplit[i] + " to kick list.");
      }

   }
}