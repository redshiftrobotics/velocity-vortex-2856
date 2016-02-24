/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AutoConfigureActivity$1$1$1
/*     */   implements Runnable
/*     */ {
/*     */   AutoConfigureActivity$1$1$1(AutoConfigureActivity.1.1 param1) {}
/*     */   
/*     */   public void run()
/*     */   {
/* 126 */     AutoConfigureActivity.b(this.a.a.a).resetCount();
/* 127 */     if (this.a.a.a.scannedDevices.size() == 0) {
/* 128 */       AutoConfigureActivity.b(this.a.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 129 */       AutoConfigureActivity.b(this.a.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 130 */       AutoConfigureActivity.c(this.a.a.a);
/*     */     }
/* 132 */     this.a.a.a.entries = this.a.a.a.scannedDevices.entrySet();
/* 133 */     AutoConfigureActivity.a(this.a.a.a, new HashMap());
/*     */     
/* 135 */     AutoConfigureActivity.b(this.a.a.a).createLists(this.a.a.a.entries, AutoConfigureActivity.d(this.a.a.a));
/* 136 */     boolean bool = AutoConfigureActivity.e(this.a.a.a);
/* 137 */     if (bool) {
/* 138 */       AutoConfigureActivity.a(this.a.a.a, "K9LegacyBot");
/*     */     } else {
/* 140 */       AutoConfigureActivity.b(this.a.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 141 */       AutoConfigureActivity.b(this.a.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 142 */       AutoConfigureActivity.f(this.a.a.a);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\AutoConfigureActivity$1$1$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */