/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.app.ProgressDialog;
/*    */ import android.content.Context;
/*    */ import android.net.wifi.WifiManager;
/*    */ import android.os.Bundle;
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
/*    */ public class ConfigWifiDirectActivity
/*    */   extends Activity
/*    */ {
/*    */   private ProgressDialog a;
/*    */   private Context b;
/*    */   
/*    */   protected void onCreate(Bundle savedInstanceState)
/*    */   {
/* 53 */     super.onCreate(savedInstanceState);
/* 54 */     setContentView(R.layout.activity_config_wifi_direct);
/*    */     
/* 56 */     this.b = this;
/*    */   }
/*    */   
/*    */   protected void onResume()
/*    */   {
/* 61 */     super.onResume();
/*    */     
/* 63 */     new Thread(new a(null)).start();
/*    */   }
/*    */   
/*    */   private class a
/*    */     implements Runnable
/*    */   {
/*    */     private a() {}
/*    */     
/*    */     public void run()
/*    */     {
/* 73 */       DbgLog.msg("attempting to reconfigure Wifi Direct");
/*    */       
/* 75 */       ConfigWifiDirectActivity.this.runOnUiThread(new Runnable()
/*    */       {
/*    */         public void run() {
/* 78 */           ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.this, new ProgressDialog(ConfigWifiDirectActivity.a(ConfigWifiDirectActivity.this), R.style.CustomAlertDialog));
/* 79 */           ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).setMessage("Please wait");
/* 80 */           ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).setTitle("Configuring Wifi Direct");
/* 81 */           ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).setIndeterminate(true);
/* 82 */           ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).show();
/*    */         }
/*    */         
/* 85 */       });
/* 86 */       WifiManager localWifiManager = (WifiManager)ConfigWifiDirectActivity.this.getSystemService("wifi");
/*    */       try
/*    */       {
/* 89 */         FixWifiDirectSetup.fixWifiDirectSetup(localWifiManager);
/*    */       } catch (InterruptedException localInterruptedException) {
/* 91 */         DbgLog.msg("Cannot fix wifi setup - interrupted");
/*    */       }
/*    */       
/* 94 */       ConfigWifiDirectActivity.this.runOnUiThread(new Runnable()
/*    */       {
/*    */         public void run() {
/* 97 */           ConfigWifiDirectActivity.b(ConfigWifiDirectActivity.this).dismiss();
/* 98 */           ConfigWifiDirectActivity.this.finish();
/*    */         }
/*    */       });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\ConfigWifiDirectActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */