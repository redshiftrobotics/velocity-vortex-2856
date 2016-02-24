/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.content.Intent;
/*    */ import android.content.pm.PackageManager;
/*    */ import android.preference.Preference;
/*    */ import android.preference.Preference.OnPreferenceClickListener;
/*    */ import android.widget.Toast;
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
/*    */ class FtcRobotControllerSettingsActivity$SettingsFragment$1
/*    */   implements Preference.OnPreferenceClickListener
/*    */ {
/*    */   FtcRobotControllerSettingsActivity$SettingsFragment$1(FtcRobotControllerSettingsActivity.SettingsFragment paramSettingsFragment) {}
/*    */   
/*    */   public boolean onPreferenceClick(Preference preference)
/*    */   {
/* 64 */     Intent localIntent = this.a.getActivity().getPackageManager().getLaunchIntentForPackage("com.zte.wifichanneleditor");
/*    */     try {
/* 66 */       this.a.startActivity(localIntent);
/*    */     } catch (NullPointerException localNullPointerException) {
/* 68 */       Toast.makeText(this.a.getActivity(), "Unable to launch ZTE WifiChannelEditor", 0).show();
/*    */     }
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcRobotControllerSettingsActivity$SettingsFragment$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */