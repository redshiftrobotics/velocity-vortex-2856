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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AutoConfigureActivity$2$1
/*     */   implements Runnable
/*     */ {
/*     */   AutoConfigureActivity$2$1(AutoConfigureActivity.2 param2) {}
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 160 */       DbgLog.msg("Scanning USB bus");
/* 161 */       this.a.a.scannedDevices = AutoConfigureActivity.a(this.a.a).scanForUsbDevices();
/*     */     } catch (RobotCoreException localRobotCoreException) {
/* 163 */       DbgLog.error("Device scan failed");
/*     */     }
/*     */     
/* 166 */     this.a.a.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 169 */         AutoConfigureActivity.b(AutoConfigureActivity.2.1.this.a.a).resetCount();
/* 170 */         if (AutoConfigureActivity.2.1.this.a.a.scannedDevices.size() == 0) {
/* 171 */           AutoConfigureActivity.b(AutoConfigureActivity.2.1.this.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 172 */           AutoConfigureActivity.b(AutoConfigureActivity.2.1.this.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 173 */           AutoConfigureActivity.c(AutoConfigureActivity.2.1.this.a.a);
/*     */         }
/* 175 */         AutoConfigureActivity.2.1.this.a.a.entries = AutoConfigureActivity.2.1.this.a.a.scannedDevices.entrySet();
/* 176 */         AutoConfigureActivity.a(AutoConfigureActivity.2.1.this.a.a, new HashMap());
/*     */         
/* 178 */         AutoConfigureActivity.b(AutoConfigureActivity.2.1.this.a.a).createLists(AutoConfigureActivity.2.1.this.a.a.entries, AutoConfigureActivity.d(AutoConfigureActivity.2.1.this.a.a));
/* 179 */         boolean bool = AutoConfigureActivity.h(AutoConfigureActivity.2.1.this.a.a);
/* 180 */         if (bool) {
/* 181 */           AutoConfigureActivity.a(AutoConfigureActivity.2.1.this.a.a, "K9USBBot");
/*     */         } else {
/* 183 */           AutoConfigureActivity.b(AutoConfigureActivity.2.1.this.a.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 184 */           AutoConfigureActivity.b(AutoConfigureActivity.2.1.this.a.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 185 */           AutoConfigureActivity.i(AutoConfigureActivity.2.1.this.a.a);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\AutoConfigureActivity$2$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */