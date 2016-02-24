/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.content.Intent;
/*    */ import android.preference.Preference;
/*    */ import android.preference.Preference.OnPreferenceClickListener;
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
/*    */ class FtcRobotControllerSettingsActivity$SettingsFragment$2
/*    */   implements Preference.OnPreferenceClickListener
/*    */ {
/*    */   FtcRobotControllerSettingsActivity$SettingsFragment$2(FtcRobotControllerSettingsActivity.SettingsFragment paramSettingsFragment) {}
/*    */   
/*    */   public boolean onPreferenceClick(Preference preference)
/*    */   {
/* 78 */     Intent localIntent = new Intent(preference.getIntent().getAction());
/* 79 */     this.a.startActivity(localIntent);
/* 80 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcRobotControllerSettingsActivity$SettingsFragment$2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */