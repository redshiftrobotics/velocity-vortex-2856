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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AutoConfigureActivity$2
/*     */   implements View.OnClickListener
/*     */ {
/*     */   AutoConfigureActivity$2(AutoConfigureActivity paramAutoConfigureActivity) {}
/*     */   
/*     */   public void onClick(View view)
/*     */   {
/* 156 */     AutoConfigureActivity.a(this.a, new Thread(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 160 */           DbgLog.msg("Scanning USB bus");
/* 161 */           AutoConfigureActivity.2.this.a.scannedDevices = AutoConfigureActivity.a(AutoConfigureActivity.2.this.a).scanForUsbDevices();
/*     */         } catch (RobotCoreException localRobotCoreException) {
/* 163 */           DbgLog.error("Device scan failed");
/*     */         }
/*     */         
/* 166 */         AutoConfigureActivity.2.this.a.runOnUiThread(new Runnable()
/*     */         {
/*     */           public void run() {
/* 169 */             AutoConfigureActivity.b(AutoConfigureActivity.2.this.a).resetCount();
/* 170 */             if (AutoConfigureActivity.2.this.a.scannedDevices.size() == 0) {
/* 171 */               AutoConfigureActivity.b(AutoConfigureActivity.2.this.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 172 */               AutoConfigureActivity.b(AutoConfigureActivity.2.this.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 173 */               AutoConfigureActivity.c(AutoConfigureActivity.2.this.a);
/*     */             }
/* 175 */             AutoConfigureActivity.2.this.a.entries = AutoConfigureActivity.2.this.a.scannedDevices.entrySet();
/* 176 */             AutoConfigureActivity.a(AutoConfigureActivity.2.this.a, new HashMap());
/*     */             
/* 178 */             AutoConfigureActivity.b(AutoConfigureActivity.2.this.a).createLists(AutoConfigureActivity.2.this.a.entries, AutoConfigureActivity.d(AutoConfigureActivity.2.this.a));
/* 179 */             boolean bool = AutoConfigureActivity.h(AutoConfigureActivity.2.this.a);
/* 180 */             if (bool) {
/* 181 */               AutoConfigureActivity.a(AutoConfigureActivity.2.this.a, "K9USBBot");
/*     */             } else {
/* 183 */               AutoConfigureActivity.b(AutoConfigureActivity.2.this.a).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 184 */               AutoConfigureActivity.b(AutoConfigureActivity.2.this.a).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 185 */               AutoConfigureActivity.i(AutoConfigureActivity.2.this.a);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/* 190 */     }));
/* 191 */     AutoConfigureActivity.g(this.a).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\AutoConfigureActivity$2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */