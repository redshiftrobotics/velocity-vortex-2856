/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Intent;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.os.Build;
/*     */ import android.os.Bundle;
/*     */ import android.preference.Preference;
/*     */ import android.preference.Preference.OnPreferenceClickListener;
/*     */ import android.preference.PreferenceFragment;
/*     */ import android.widget.Toast;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcRobotControllerSettingsActivity$SettingsFragment
/*     */   extends PreferenceFragment
/*     */ {
/*     */   public void onCreate(Bundle savedInstanceState)
/*     */   {
/*  48 */     super.onCreate(savedInstanceState);
/*     */     
/*     */ 
/*  51 */     addPreferencesFromResource(R.xml.preferences);
/*     */     
/*  53 */     Preference localPreference1 = findPreference(getString(R.string.pref_launch_configure));
/*  54 */     localPreference1.setOnPreferenceClickListener(this.a);
/*     */     
/*  56 */     Preference localPreference2 = findPreference(getString(R.string.pref_launch_autoconfigure));
/*  57 */     localPreference2.setOnPreferenceClickListener(this.a);
/*     */     Preference localPreference3;
/*  59 */     if ((Build.MANUFACTURER.equalsIgnoreCase("zte")) && (Build.MODEL.equalsIgnoreCase("N9130"))) {
/*  60 */       localPreference3 = findPreference(getString(R.string.pref_launch_settings));
/*  61 */       localPreference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
/*     */       {
/*     */         public boolean onPreferenceClick(Preference preference) {
/*  64 */           Intent localIntent = FtcRobotControllerSettingsActivity.SettingsFragment.this.getActivity().getPackageManager().getLaunchIntentForPackage("com.zte.wifichanneleditor");
/*     */           try {
/*  66 */             FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(localIntent);
/*     */           } catch (NullPointerException localNullPointerException) {
/*  68 */             Toast.makeText(FtcRobotControllerSettingsActivity.SettingsFragment.this.getActivity(), "Unable to launch ZTE WifiChannelEditor", 0).show();
/*     */           }
/*  70 */           return true;
/*     */         }
/*     */       });
/*     */     } else {
/*  74 */       localPreference3 = findPreference(getString(R.string.pref_launch_settings));
/*  75 */       localPreference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
/*     */       {
/*     */         public boolean onPreferenceClick(Preference preference) {
/*  78 */           Intent localIntent = new Intent(preference.getIntent().getAction());
/*  79 */           FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(localIntent);
/*  80 */           return true;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*  85 */     if (Build.MODEL.equals("FL7007")) {
/*  86 */       localPreference3 = findPreference(getString(R.string.pref_launch_settings));
/*  87 */       localPreference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
/*     */       {
/*     */         public boolean onPreferenceClick(Preference preference) {
/*  90 */           Intent localIntent = new Intent("android.settings.SETTINGS");
/*  91 */           FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(localIntent);
/*     */           
/*  93 */           return true;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*  99 */   Preference.OnPreferenceClickListener a = new Preference.OnPreferenceClickListener()
/*     */   {
/*     */     public boolean onPreferenceClick(Preference preference) {
/* 102 */       Intent localIntent = new Intent(preference.getIntent().getAction());
/* 103 */       FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivityForResult(localIntent, 3);
/* 104 */       return true;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */   public void onActivityResult(int request, int result, Intent intent)
/*     */   {
/* 111 */     if ((request == 3) && 
/* 112 */       (result == -1)) {
/* 113 */       getActivity().setResult(-1, intent);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcRobotControllerSettingsActivity$SettingsFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */