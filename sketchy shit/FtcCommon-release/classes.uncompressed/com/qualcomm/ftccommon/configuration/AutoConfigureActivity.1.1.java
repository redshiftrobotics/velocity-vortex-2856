/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import com.qualcomm.ftccommon.DbgLog;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager;
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
/*     */ class AutoConfigureActivity$1$1
/*     */   implements Runnable
/*     */ {
/*     */   AutoConfigureActivity$1$1(AutoConfigureActivity.1 param1) {}
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 117 */       DbgLog.msg("Scanning USB bus");
/* 118 */       this.a.a.scannedDevices = AutoConfigureActivity.a(this.a.a).scanForUsbDevices();
/*     */     } catch (RobotCoreException localRobotCoreException) {
/* 120 */       DbgLog.error("Device scan failed");
/*     */     }
/*     */     
/* 123 */     this.a.a.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 126 */         AutoConfigureActivity.b(AutoConfigureActivity.1.1.this.a.a).resetCount();
/* 127 */         if (AutoConfigureActivity.1.1.this.a.a.scannedDevices.size() == 0) {
/* 128 */           AutoConfigureActivity.b(AutoConfigureActivity.1.1.this.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 129 */           AutoConfigureActivity.b(AutoConfigureActivity.1.1.this.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 130 */           AutoConfigureActivity.c(AutoConfigureActivity.1.1.this.a.a);
/*     */         }
/* 132 */         AutoConfigureActivity.1.1.this.a.a.entries = AutoConfigureActivity.1.1.this.a.a.scannedDevices.entrySet();
/* 133 */         AutoConfigureActivity.a(AutoConfigureActivity.1.1.this.a.a, new HashMap());
/*     */         
/* 135 */         AutoConfigureActivity.b(AutoConfigureActivity.1.1.this.a.a).createLists(AutoConfigureActivity.1.1.this.a.a.entries, AutoConfigureActivity.d(AutoConfigureActivity.1.1.this.a.a));
/* 136 */         boolean bool = AutoConfigureActivity.e(AutoConfigureActivity.1.1.this.a.a);
/* 137 */         if (bool) {
/* 138 */           AutoConfigureActivity.a(AutoConfigureActivity.1.1.this.a.a, "K9LegacyBot");
/*     */         } else {
/* 140 */           AutoConfigureActivity.b(AutoConfigureActivity.1.1.this.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 141 */           AutoConfigureActivity.b(AutoConfigureActivity.1.1.this.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 142 */           AutoConfigureActivity.f(AutoConfigureActivity.1.1.this.a.a);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\AutoConfigureActivity$1$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */