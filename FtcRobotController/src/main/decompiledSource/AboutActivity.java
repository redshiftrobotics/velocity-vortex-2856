/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Context;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.PackageManager.NameNotFoundException;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.ListView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class AboutActivity
/*     */   extends Activity
/*     */ {
/*     */   WifiDirectAssistant a;
/*     */   
/*     */   public AboutActivity()
/*     */   {
/*  79 */     this.a = null;
/*     */   }
/*     */   
/*     */   protected void onStart() {
/*  83 */     super.onStart();
/*  84 */     setContentView(R.layout.activity_about);
/*     */     
/*  86 */     ListView localListView = (ListView)findViewById(R.id.aboutList);
/*     */     try
/*     */     {
/*  89 */       this.a = WifiDirectAssistant.getWifiDirectAssistant(null);
/*  90 */       this.a.enable();
/*     */     } catch (NullPointerException localNullPointerException) {
/*  92 */       RobotLog.i("Cannot start Wifi Direct Assistant");
/*  93 */       this.a = null;
/*     */     }
/*     */     
/*  96 */     ArrayAdapter local1 = new ArrayAdapter(this, 17367044, 16908308)
/*     */     {
/*     */       public View getView(int position, View convertView, ViewGroup parent) {
/*  99 */         View localView = super.getView(position, convertView, parent);
/* 100 */         TextView localTextView1 = (TextView)localView.findViewById(16908308);
/* 101 */         TextView localTextView2 = (TextView)localView.findViewById(16908309);
/*     */         
/* 103 */         AboutActivity.Item localItem = a(position);
/* 104 */         localTextView1.setText(localItem.title);
/* 105 */         localTextView2.setText(localItem.info);
/*     */         
/* 107 */         return localView;
/*     */       }
/*     */       
/*     */ 
/*     */       public int getCount()
/*     */       {
/* 113 */         return 3;
/*     */       }
/*     */       
/*     */       public AboutActivity.Item a(int paramAnonymousInt)
/*     */       {
/* 118 */         switch (paramAnonymousInt) {
/* 119 */         case 0:  return a();
/* 120 */         case 1:  return b();
/* 121 */         case 2:  return c();
/*     */         }
/* 123 */         return new AboutActivity.Item();
/*     */       }
/*     */       
/*     */       private AboutActivity.Item a() {
/* 127 */         AboutActivity.Item localItem = new AboutActivity.Item();
/* 128 */         localItem.title = "App Version GLORY REVELATION";
/*     */         try {
/* 130 */           localItem.info = AboutActivity.this.getPackageManager().getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName;
/*     */         } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
/* 132 */           localItem.info = localNameNotFoundException.getMessage();
/*     */         }
/* 134 */         return localItem;
/*     */       }
/*     */       
/*     */       private AboutActivity.Item b() {
/* 138 */         AboutActivity.Item localItem = new AboutActivity.Item();
/* 139 */         localItem.title = "Library Version";
/* 140 */         localItem.info = Version.getLibraryVersion();
/* 141 */         return localItem;
/*     */       }
/*     */       
/*     */       private AboutActivity.Item c() {
/* 145 */         AboutActivity.Item localItem = new AboutActivity.Item();
/* 146 */         localItem.title = "Wifi Direct Information";
/* 147 */         localItem.info = "unavailable";
/*     */         
/* 149 */         StringBuilder localStringBuilder = new StringBuilder();
/*     */         
/* 151 */         if ((AboutActivity.this.a != null) && (AboutActivity.this.a.isEnabled())) {
/* 152 */           localStringBuilder.append("Name: ").append(AboutActivity.this.a.getDeviceName());
/* 153 */           if (AboutActivity.this.a.isGroupOwner()) {
/* 154 */             localStringBuilder.append("\nIP Address").append(AboutActivity.this.a.getGroupOwnerAddress().getHostAddress());
/* 155 */             localStringBuilder.append("\nPassphrase: ").append(AboutActivity.this.a.getPassphrase());
/* 156 */             localStringBuilder.append("\nGroup Owner");
/* 157 */           } else if (AboutActivity.this.a.isConnected()) {
/* 158 */             localStringBuilder.append("\nGroup Owner: ").append(AboutActivity.this.a.getGroupOwnerName());
/* 159 */             localStringBuilder.append("\nConnected");
/*     */           } else {
/* 161 */             localStringBuilder.append("\nNo connection information");
/*     */           }
/* 163 */           localItem.info = localStringBuilder.toString();
/*     */         }
/*     */         
/* 166 */         return localItem;
/*     */       }
/*     */       
/* 169 */     };
/* 170 */     localListView.setAdapter(local1);
/*     */   }
/*     */   
/*     */   protected void onStop() {
/* 174 */     super.onStop();
/*     */     
/* 176 */     if (this.a != null) {
/* 177 */       this.a.disable();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Item {
/* 182 */     public String title = "";
/* 183 */     public String info = "";
/*     */   }
/*     */ }


/* Location:              /home/matt/Documents/robotics/2856/SwerveRoboticsLibrary/build/intermediates/exploded-aar/FtcCommon-release/jars/classes.jar!/com/qualcomm/ftccommon/AboutActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
