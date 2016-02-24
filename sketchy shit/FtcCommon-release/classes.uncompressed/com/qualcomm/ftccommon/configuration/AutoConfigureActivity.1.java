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
/*     */ class AutoConfigureActivity$1
/*     */   implements View.OnClickListener
/*     */ {
/*     */   AutoConfigureActivity$1(AutoConfigureActivity paramAutoConfigureActivity) {}
/*     */   
/*     */   public void onClick(View view)
/*     */   {
/* 113 */     AutoConfigureActivity.a(this.a, new Thread(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 117 */           DbgLog.msg("Scanning USB bus");
/* 118 */           AutoConfigureActivity.1.this.a.scannedDevices = AutoConfigureActivity.a(AutoConfigureActivity.1.this.a).scanForUsbDevices();
/*     */         } catch (RobotCoreException localRobotCoreException) {
/* 120 */           DbgLog.error("Device scan failed");
/*     */         }
/*     */         
/* 123 */         AutoConfigureActivity.1.this.a.runOnUiThread(new Runnable()
/*     */         {
/*     */           public void run() {
/* 126 */             AutoConfigureActivity.b(AutoConfigureActivity.1.this.a).resetCount();
/* 127 */             if (AutoConfigureActivity.1.this.a.scannedDevices.size() == 0) {
/* 128 */               AutoConfigureActivity.b(AutoConfigureActivity.1.this.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 129 */               AutoConfigureActivity.b(AutoConfigureActivity.1.this.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 130 */               AutoConfigureActivity.c(AutoConfigureActivity.1.this.a);
/*     */             }
/* 132 */             AutoConfigureActivity.1.this.a.entries = AutoConfigureActivity.1.this.a.scannedDevices.entrySet();
/* 133 */             AutoConfigureActivity.a(AutoConfigureActivity.1.this.a, new HashMap());
/*     */             
/* 135 */             AutoConfigureActivity.b(AutoConfigureActivity.1.this.a).createLists(AutoConfigureActivity.1.this.a.entries, AutoConfigureActivity.d(AutoConfigureActivity.1.this.a));
/* 136 */             boolean bool = AutoConfigureActivity.e(AutoConfigureActivity.1.this.a);
/* 137 */             if (bool) {
/* 138 */               AutoConfigureActivity.a(AutoConfigureActivity.1.this.a, "K9LegacyBot");
/*     */             } else {
/* 140 */               AutoConfigureActivity.b(AutoConfigureActivity.1.this.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 141 */               AutoConfigureActivity.b(AutoConfigureActivity.1.this.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 142 */               AutoConfigureActivity.f(AutoConfigureActivity.1.this.a);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/* 147 */     }));
/* 148 */     AutoConfigureActivity.g(this.a).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\AutoConfigureActivity$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */