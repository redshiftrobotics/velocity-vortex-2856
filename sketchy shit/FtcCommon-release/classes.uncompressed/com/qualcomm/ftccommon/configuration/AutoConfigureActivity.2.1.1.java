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
/*     */ class AutoConfigureActivity$2$1$1
/*     */   implements Runnable
/*     */ {
/*     */   AutoConfigureActivity$2$1$1(AutoConfigureActivity.2.1 param1) {}
/*     */   
/*     */   public void run()
/*     */   {
/* 169 */     AutoConfigureActivity.b(this.a.a.a).resetCount();
/* 170 */     if (this.a.a.a.scannedDevices.size() == 0) {
/* 171 */       AutoConfigureActivity.b(this.a.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 172 */       AutoConfigureActivity.b(this.a.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 173 */       AutoConfigureActivity.c(this.a.a.a);
/*     */     }
/* 175 */     this.a.a.a.entries = this.a.a.a.scannedDevices.entrySet();
/* 176 */     AutoConfigureActivity.a(this.a.a.a, new HashMap());
/*     */     
/* 178 */     AutoConfigureActivity.b(this.a.a.a).createLists(this.a.a.a.entries, AutoConfigureActivity.d(this.a.a.a));
/* 179 */     boolean bool = AutoConfigureActivity.h(this.a.a.a);
/* 180 */     if (bool) {
/* 181 */       AutoConfigureActivity.a(this.a.a.a, "K9USBBot");
/*     */     } else {
/* 183 */       AutoConfigureActivity.b(this.a.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 184 */       AutoConfigureActivity.b(this.a.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 185 */       AutoConfigureActivity.i(this.a.a.a);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\AutoConfigureActivity$2$1$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */