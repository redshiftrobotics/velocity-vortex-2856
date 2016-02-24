/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
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
/*     */ class FtcConfigurationActivity$6
/*     */   implements View.OnClickListener
/*     */ {
/*     */   FtcConfigurationActivity$6(FtcConfigurationActivity paramFtcConfigurationActivity) {}
/*     */   
/*     */   public void onClick(View view)
/*     */   {
/* 182 */     FtcConfigurationActivity.a(this.a, new Thread(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 186 */           DbgLog.msg("Scanning USB bus");
/* 187 */           FtcConfigurationActivity.6.this.a.scannedDevices = FtcConfigurationActivity.b(FtcConfigurationActivity.6.this.a).scanForUsbDevices();
/*     */         }
/*     */         catch (RobotCoreException localRobotCoreException) {
/* 190 */           DbgLog.error("Device scan failed: " + localRobotCoreException.toString());
/*     */         }
/*     */         
/* 193 */         FtcConfigurationActivity.6.this.a.runOnUiThread(new Runnable()
/*     */         {
/*     */           public void run() {
/* 196 */             FtcConfigurationActivity.a(FtcConfigurationActivity.6.this.a).resetCount();
/* 197 */             FtcConfigurationActivity.c(FtcConfigurationActivity.6.this.a);
/* 198 */             FtcConfigurationActivity.d(FtcConfigurationActivity.6.this.a);
/* 199 */             FtcConfigurationActivity.a(FtcConfigurationActivity.6.this.a).updateHeader(FtcConfigurationActivity.e(FtcConfigurationActivity.6.this.a), R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 200 */             FtcConfigurationActivity.6.this.a.scannedEntries = FtcConfigurationActivity.6.this.a.scannedDevices.entrySet();
/*     */             
/* 202 */             FtcConfigurationActivity.a(FtcConfigurationActivity.6.this.a, FtcConfigurationActivity.f(FtcConfigurationActivity.6.this.a));
/* 203 */             FtcConfigurationActivity.g(FtcConfigurationActivity.6.this.a);
/* 204 */             FtcConfigurationActivity.h(FtcConfigurationActivity.6.this.a);
/*     */           }
/*     */         });
/*     */       }
/* 208 */     }));
/* 209 */     FtcConfigurationActivity.i(this.a);
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\FtcConfigurationActivity$6.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */