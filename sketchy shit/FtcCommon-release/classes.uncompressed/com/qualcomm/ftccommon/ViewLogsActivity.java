/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.os.Environment;
/*     */ import android.text.Spannable;
/*     */ import android.text.SpannableString;
/*     */ import android.text.style.ForegroundColorSpan;
/*     */ import android.widget.ScrollView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ViewLogsActivity
/*     */   extends Activity
/*     */ {
/*     */   TextView a;
/*  62 */   int b = 300;
/*     */   
/*     */   public static final String FILENAME = "Filename";
/*  65 */   String c = " ";
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  69 */     super.onCreate(savedInstanceState);
/*  70 */     setContentView(R.layout.activity_view_logs);
/*     */     
/*  72 */     this.a = ((TextView)findViewById(R.id.textAdbLogs));
/*     */     
/*  74 */     final ScrollView localScrollView = (ScrollView)findViewById(R.id.scrollView);
/*  75 */     localScrollView.post(new Runnable()
/*     */     {
/*     */       public void run() {
/*  78 */         localScrollView.fullScroll(130);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/*  85 */     super.onStart();
/*     */     
/*  87 */     Intent localIntent = getIntent();
/*  88 */     Serializable localSerializable = localIntent.getSerializableExtra("Filename");
/*  89 */     if (localSerializable != null) {
/*  90 */       this.c = ((String)localSerializable);
/*     */     }
/*  92 */     runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/*  96 */           String str = ViewLogsActivity.this.readNLines(ViewLogsActivity.this.b);
/*  97 */           Spannable localSpannable = ViewLogsActivity.a(ViewLogsActivity.this, str);
/*  98 */           ViewLogsActivity.this.a.setText(localSpannable);
/*     */         } catch (IOException localIOException) {
/* 100 */           RobotLog.e(localIOException.toString());
/* 101 */           ViewLogsActivity.this.a.setText("File not found: " + ViewLogsActivity.this.c);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public String readNLines(int n) throws IOException {
/* 108 */     File localFile1 = Environment.getExternalStorageDirectory();
/* 109 */     File localFile2 = new File(this.c);
/* 110 */     BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile2));
/* 111 */     String[] arrayOfString = new String[n];
/* 112 */     int i = 0;
/* 113 */     String str1 = null;
/*     */     
/* 115 */     while ((str1 = localBufferedReader.readLine()) != null) {
/* 116 */       arrayOfString[(i % arrayOfString.length)] = str1;
/* 117 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 123 */     int j = i - n;
/* 124 */     if (j < 0) {
/* 125 */       j = 0;
/*     */     }
/*     */     
/* 128 */     String str2 = "";
/* 129 */     for (int k = j; k < i; k++)
/*     */     {
/* 131 */       int m = k % arrayOfString.length;
/* 132 */       String str3 = arrayOfString[m];
/* 133 */       str2 = str2 + str3 + "\n";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 138 */     k = str2.lastIndexOf("--------- beginning");
/* 139 */     if (k < 0)
/*     */     {
/* 141 */       return str2;
/*     */     }
/* 143 */     return str2.substring(k);
/*     */   }
/*     */   
/*     */   private Spannable a(String paramString)
/*     */   {
/* 148 */     SpannableString localSpannableString = new SpannableString(paramString);
/* 149 */     String[] arrayOfString1 = paramString.split("\\n");
/* 150 */     int i = 0;
/* 151 */     for (String str : arrayOfString1) {
/* 152 */       if ((str.contains("E/RobotCore")) || (str.contains("### ERROR: "))) {
/* 153 */         localSpannableString.setSpan(new ForegroundColorSpan(-65536), i, i + str.length(), 33);
/*     */       }
/*     */       
/*     */ 
/* 157 */       i += str.length();
/* 158 */       i++;
/*     */     }
/*     */     
/* 161 */     return localSpannableString;
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\ViewLogsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */