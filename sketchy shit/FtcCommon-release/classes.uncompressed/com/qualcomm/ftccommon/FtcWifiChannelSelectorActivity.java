/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.ProgressDialog;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.net.wifi.WifiManager;
/*     */ import android.os.Bundle;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemSelectedListener;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.Button;
/*     */ import android.widget.Spinner;
/*     */ import android.widget.Toast;
/*     */ import com.qualcomm.wirelessp2p.WifiDirectChannelSelection;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcWifiChannelSelectorActivity
/*     */   extends Activity
/*     */   implements View.OnClickListener, AdapterView.OnItemSelectedListener
/*     */ {
/*  56 */   private static int a = 0;
/*     */   
/*     */   private Button b;
/*     */   
/*     */   private Button c;
/*     */   
/*     */   private Spinner d;
/*     */   
/*     */   private ProgressDialog e;
/*     */   private WifiDirectChannelSelection f;
/*  66 */   private int g = -1;
/*  67 */   private int h = -1;
/*     */   
/*     */   private Context i;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  73 */     super.onCreate(savedInstanceState);
/*  74 */     setContentView(R.layout.activity_ftc_wifi_channel_selector);
/*     */     
/*  76 */     this.i = this;
/*     */     
/*  78 */     this.d = ((Spinner)findViewById(R.id.spinnerChannelSelect));
/*  79 */     ArrayAdapter localArrayAdapter = ArrayAdapter.createFromResource(this, R.array.wifi_direct_channels, 17367048);
/*     */     
/*     */ 
/*  82 */     localArrayAdapter.setDropDownViewResource(17367049);
/*  83 */     this.d.setAdapter(localArrayAdapter);
/*  84 */     this.d.setOnItemSelectedListener(this);
/*     */     
/*  86 */     this.b = ((Button)findViewById(R.id.buttonConfigure));
/*  87 */     this.b.setOnClickListener(this);
/*     */     
/*  89 */     this.c = ((Button)findViewById(R.id.buttonWifiSettings));
/*  90 */     this.c.setOnClickListener(this);
/*     */     
/*  92 */     WifiManager localWifiManager = (WifiManager)getSystemService("wifi");
/*  93 */     this.f = new WifiDirectChannelSelection(this, localWifiManager);
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/*  98 */     super.onStart();
/*  99 */     this.d.setSelection(a);
/*     */   }
/*     */   
/*     */   public void onItemSelected(AdapterView<?> av, View v, int item, long l)
/*     */   {
/* 104 */     switch (item) {
/* 105 */     case 0:  this.g = -1;this.h = -1; break;
/*     */     case 1: 
/* 107 */       this.g = 81;this.h = 1; break;
/* 108 */     case 2:  this.g = 81;this.h = 6; break;
/* 109 */     case 3:  this.g = 81;this.h = 11; break;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 4: 
/* 116 */       this.g = 124;this.h = 149; break;
/* 117 */     case 5:  this.g = 124;this.h = 153; break;
/* 118 */     case 6:  this.g = 124;this.h = 157; break;
/* 119 */     case 7:  this.g = 124;this.h = 161;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void onNothingSelected(AdapterView<?> av) {}
/*     */   
/*     */ 
/*     */   public void onClick(View v)
/*     */   {
/* 130 */     if (v.getId() == R.id.buttonConfigure) {
/* 131 */       a = this.d.getSelectedItemPosition();
/* 132 */       a();
/*     */     }
/* 134 */     else if (v.getId() == R.id.buttonWifiSettings) {
/* 135 */       DbgLog.msg("launch wifi settings");
/*     */       
/* 137 */       startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
/*     */     }
/*     */   }
/*     */   
/*     */   private void a() {
/* 142 */     DbgLog.msg(String.format("configure p2p channel - class %d channel %d", new Object[] { Integer.valueOf(this.g), Integer.valueOf(this.h) }));
/*     */     
/*     */     try
/*     */     {
/* 146 */       this.e = ProgressDialog.show(this, "Configuring Channel", "Please Wait", true);
/*     */       
/* 148 */       this.f.config(this.g, this.h);
/* 149 */       new Thread(new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 153 */             Thread.sleep(5000L);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException) {}
/* 156 */           FtcWifiChannelSelectorActivity.this.runOnUiThread(new Runnable()
/*     */           {
/*     */             public void run() {
/* 159 */               FtcWifiChannelSelectorActivity.this.setResult(-1);
/* 160 */               FtcWifiChannelSelectorActivity.a(FtcWifiChannelSelectorActivity.this).dismiss();
/* 161 */               FtcWifiChannelSelectorActivity.this.finish();
/*     */             }
/*     */           });
/*     */         }
/*     */       }).start();
/*     */     } catch (IOException localIOException) {
/* 167 */       a("Failed - root is required", 0);
/* 168 */       localIOException.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(final String paramString, final int paramInt) {
/* 173 */     Runnable local2 = new Runnable()
/*     */     {
/*     */       public void run() {
/* 176 */         Toast.makeText(FtcWifiChannelSelectorActivity.b(FtcWifiChannelSelectorActivity.this), paramString, paramInt).show();
/*     */       }
/*     */       
/* 179 */     };
/* 180 */     runOnUiThread(local2);
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcWifiChannelSelectorActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */