/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.text.Spannable;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ class ViewLogsActivity$2
/*     */   implements Runnable
/*     */ {
/*     */   ViewLogsActivity$2(ViewLogsActivity paramViewLogsActivity) {}
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  96 */       String str = this.a.readNLines(this.a.b);
/*  97 */       Spannable localSpannable = ViewLogsActivity.a(this.a, str);
/*  98 */       this.a.a.setText(localSpannable);
/*     */     } catch (IOException localIOException) {
/* 100 */       RobotLog.e(localIOException.toString());
/* 101 */       this.a.a.setText("File not found: " + this.a.c);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\ViewLogsActivity$2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */