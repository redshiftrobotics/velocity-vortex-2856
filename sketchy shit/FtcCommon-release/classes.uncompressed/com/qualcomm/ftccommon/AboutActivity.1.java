/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.PackageManager.NameNotFoundException;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.util.Version;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
/*     */ import java.net.InetAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AboutActivity$1
/*     */   extends ArrayAdapter<AboutActivity.Item>
/*     */ {
/*  96 */   AboutActivity$1(AboutActivity paramAboutActivity, Context paramContext, int paramInt1, int paramInt2) { super(paramContext, paramInt1, paramInt2); }
/*     */   
/*     */   public View getView(int position, View convertView, ViewGroup parent) {
/*  99 */     View localView = super.getView(position, convertView, parent);
/* 100 */     TextView localTextView1 = (TextView)localView.findViewById(16908308);
/* 101 */     TextView localTextView2 = (TextView)localView.findViewById(16908309);
/*     */     
/* 103 */     AboutActivity.Item localItem = a(position);
/* 104 */     localTextView1.setText(localItem.title);
/* 105 */     localTextView2.setText(localItem.info);
/*     */     
/* 107 */     return localView;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 113 */     return 3;
/*     */   }
/*     */   
/*     */   public AboutActivity.Item a(int paramInt)
/*     */   {
/* 118 */     switch (paramInt) {
/* 119 */     case 0:  return a();
/* 120 */     case 1:  return b();
/* 121 */     case 2:  return c();
/*     */     }
/* 123 */     return new AboutActivity.Item();
/*     */   }
/*     */   
/*     */   private AboutActivity.Item a() {
/* 127 */     AboutActivity.Item localItem = new AboutActivity.Item();
/* 128 */     localItem.title = "App Version";
/*     */     try {
/* 130 */       localItem.info = this.a.getPackageManager().getPackageInfo(this.a.getPackageName(), 0).versionName;
/*     */     } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
/* 132 */       localItem.info = localNameNotFoundException.getMessage();
/*     */     }
/* 134 */     return localItem;
/*     */   }
/*     */   
/*     */   private AboutActivity.Item b() {
/* 138 */     AboutActivity.Item localItem = new AboutActivity.Item();
/* 139 */     localItem.title = "Library Version";
/* 140 */     localItem.info = Version.getLibraryVersion();
/* 141 */     return localItem;
/*     */   }
/*     */   
/*     */   private AboutActivity.Item c() {
/* 145 */     AboutActivity.Item localItem = new AboutActivity.Item();
/* 146 */     localItem.title = "Wifi Direct Information";
/* 147 */     localItem.info = "unavailable";
/*     */     
/* 149 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */     
/* 151 */     if ((this.a.a != null) && (this.a.a.isEnabled())) {
/* 152 */       localStringBuilder.append("Name: ").append(this.a.a.getDeviceName());
/* 153 */       if (this.a.a.isGroupOwner()) {
/* 154 */         localStringBuilder.append("\nIP Address").append(this.a.a.getGroupOwnerAddress().getHostAddress());
/* 155 */         localStringBuilder.append("\nPassphrase: ").append(this.a.a.getPassphrase());
/* 156 */         localStringBuilder.append("\nGroup Owner");
/* 157 */       } else if (this.a.a.isConnected()) {
/* 158 */         localStringBuilder.append("\nGroup Owner: ").append(this.a.a.getGroupOwnerName());
/* 159 */         localStringBuilder.append("\nConnected");
/*     */       } else {
/* 161 */         localStringBuilder.append("\nNo connection information");
/*     */       }
/* 163 */       localItem.info = localStringBuilder.toString();
/*     */     }
/*     */     
/* 166 */     return localItem;
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\AboutActivity$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */