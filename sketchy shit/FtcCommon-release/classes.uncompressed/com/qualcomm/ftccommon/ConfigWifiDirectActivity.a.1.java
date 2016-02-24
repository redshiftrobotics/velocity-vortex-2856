/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.app.ProgressDialog;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ConfigWifiDirectActivity$a$1
/*    */   implements Runnable
/*    */ {
/*    */   ConfigWifiDirectActivity$a$1(ConfigWifiDirectActivity.a parama) {}
/*    */   
/*    */   public void run()
/*    */   {
/* 78 */     ConfigWifiDirectActivity.a(this.a.a, new ProgressDialog(ConfigWifiDirectActivity.a(this.a.a), R.style.CustomAlertDialog));
/* 79 */     ConfigWifiDirectActivity.b(this.a.a).setMessage("Please wait");
/* 80 */     ConfigWifiDirectActivity.b(this.a.a).setTitle("Configuring Wifi Direct");
/* 81 */     ConfigWifiDirectActivity.b(this.a.a).setIndeterminate(true);
/* 82 */     ConfigWifiDirectActivity.b(this.a.a).show();
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\ConfigWifiDirectActivity$a$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */