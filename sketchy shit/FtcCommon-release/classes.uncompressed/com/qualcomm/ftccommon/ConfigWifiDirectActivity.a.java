/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.app.ProgressDialog;
/*    */ import android.net.wifi.WifiManager;
/*    */ import com.qualcomm.robotcore.wifi.FixWifiDirectSetup;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ConfigWifiDirectActivity$a
/*    */   implements Runnable
/*    */ {
/*    */   private ConfigWifiDirectActivity$a(ConfigWifiDirectActivity paramConfigWifiDirectActivity) {}
/*    */   
/*    */   public void run()
/*    */   {
/* 73 */     DbgLog.msg("attempting to reconfigure Wifi Direct");
/*    */     
/* 75 */     this.a.runOnUiThread(new Runnable()
/*    */     {
/*    */       public void run() {
/* 78 */         ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.a.this.a, new ProgressDialog(ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.a.this.a), R.style.CustomAlertDialog));
/* 79 */         ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).setMessage("Please wait");
/* 80 */         ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).setTitle("Configuring Wifi Direct");
/* 81 */         ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).setIndeterminate(true);
/* 82 */         ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).show();
/*    */       }
/*    */       
/* 85 */     });
/* 86 */     WifiManager localWifiManager = (WifiManager)this.a.getSystemService("wifi");
/*    */     try
/*    */     {
/* 89 */       FixWifiDirectSetup.fixWifiDirectSetup(localWifiManager);
/*    */     } catch (InterruptedException localInterruptedException) {
/* 91 */       DbgLog.msg("Cannot fix wifi setup - interrupted");
/*    */     }
/*    */     
/* 94 */     this.a.runOnUiThread(new Runnable()
/*    */     {
/*    */       public void run() {
/* 97 */         ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.a.this.a).dismiss();
/* 98 */         ConfigWifiDirectActivity.a.this.a.finish();
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\ConfigWifiDirectActivity$a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */