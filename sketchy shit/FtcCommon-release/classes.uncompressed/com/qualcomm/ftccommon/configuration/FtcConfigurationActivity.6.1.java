/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import com.qualcomm.ftccommon.DbgLog;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FtcConfigurationActivity$6$1
/*     */   implements Runnable
/*     */ {
/*     */   FtcConfigurationActivity$6$1(FtcConfigurationActivity.6 param6) {}
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 186 */       DbgLog.msg("Scanning USB bus");
/* 187 */       this.a.a.scannedDevices = FtcConfigurationActivity.b(this.a.a).scanForUsbDevices();
/*     */     }
/*     */     catch (RobotCoreException localRobotCoreException) {
/* 190 */       DbgLog.error("Device scan failed: " + localRobotCoreException.toString());
/*     */     }
/*     */     
/* 193 */     this.a.a.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 196 */         FtcConfigurationActivity.a(FtcConfigurationActivity.6.1.this.a.a).resetCount();
/* 197 */         FtcConfigurationActivity.c(FtcConfigurationActivity.6.1.this.a.a);
/* 198 */         FtcConfigurationActivity.d(FtcConfigurationActivity.6.1.this.a.a);
/* 199 */         FtcConfigurationActivity.a(FtcConfigurationActivity.6.1.this.a.a).updateHeader(FtcConfigurationActivity.e(FtcConfigurationActivity.6.1.this.a.a), R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 200 */         FtcConfigurationActivity.6.1.this.a.a.scannedEntries = FtcConfigurationActivity.6.1.this.a.a.scannedDevices.entrySet();
/*     */         
/* 202 */         FtcConfigurationActivity.a(FtcConfigurationActivity.6.1.this.a.a, FtcConfigurationActivity.f(FtcConfigurationActivity.6.1.this.a.a));
/* 203 */         FtcConfigurationActivity.g(FtcConfigurationActivity.6.1.this.a.a);
/* 204 */         FtcConfigurationActivity.h(FtcConfigurationActivity.6.1.this.a.a);
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\FtcConfigurationActivity$6$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */