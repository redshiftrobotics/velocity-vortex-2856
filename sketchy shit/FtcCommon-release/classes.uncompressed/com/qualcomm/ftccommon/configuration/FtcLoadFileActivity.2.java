/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.AlertDialog;
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FtcLoadFileActivity$2
/*     */   implements View.OnClickListener
/*     */ {
/*     */   FtcLoadFileActivity$2(FtcLoadFileActivity paramFtcLoadFileActivity) {}
/*     */   
/*     */   public void onClick(View view)
/*     */   {
/* 103 */     AlertDialog.Builder localBuilder = FtcLoadFileActivity.a(this.a).buildBuilder("AutoConfigure", "This is the fastest way to get a new machine up and running. The AutoConfigure tool will automatically enter some default names for devices. AutoConfigure expects certain devices.  If there are other devices attached, the AutoConfigure tool will not name them.");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 108 */     localBuilder.setPositiveButton("Ok", this.a.a);
/* 109 */     AlertDialog localAlertDialog = localBuilder.create();
/* 110 */     localAlertDialog.show();
/* 111 */     TextView localTextView = (TextView)localAlertDialog.findViewById(16908299);
/* 112 */     localTextView.setTextSize(14.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\FtcLoadFileActivity$2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */