package com.pedruhb.modblacklist.main;

public class Mod {

   private String modId;
   private ModAction action;
   private String kick_message;

   public Mod(String modId, ModAction action, String msg) {
      this.modId = modId;
      this.action = action;
      this.kick_message = msg;
   }

   public ModAction getAction() {
      return this.action;
   }

   public String getMsg(String modid) {
      return this.kick_message == null ? null : this.kick_message.replaceAll("%MOD%", modId);
   }
   
}