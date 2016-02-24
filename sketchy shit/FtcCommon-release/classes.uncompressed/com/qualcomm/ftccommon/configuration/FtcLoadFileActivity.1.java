/*    */ package com.qualcomm.ftccommon.configuration;
/*    */ 
/*    */ import android.app.AlertDialog;
/*    */ import android.app.AlertDialog.Builder;
/*    */ import android.view.View;
/*    */ import android.view.View.OnClickListener;
/*    */ import android.widget.TextView;
/*    */ import com.qualcomm.robotcore.hardware.configuration.Utility;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FtcLoadFileActivity$1
/*    */   implements View.OnClickListener
/*    */ {
/*    */   FtcLoadFileActivity$1(FtcLoadFileActivity paramFtcLoadFileActivity) {}
/*    */   
/*    */   public void onClick(View view)
/*    */   {
/* 87 */     AlertDialog.Builder localBuilder = FtcLoadFileActivity.a(this.a).buildBuilder("Available files", "These are the files the Hardware Wizard was able to find. You can edit each file by clicking the edit button. The 'Activate' button will set that file as the current configuration file, which will be used to start the robot.");
/*    */     
/*    */ 
/*    */ 
/* 91 */     localBuilder.setPositiveButton("Ok", this.a.a);
/* 92 */     AlertDialog localAlertDialog = localBuilder.create();
/* 93 */     localAlertDialog.show();
/* 94 */     TextView localTextView = (TextView)localAlertDialog.findViewById(16908299);
/* 95 */     localTextView.setTextSize(14.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\FtcLoadFileActivity$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */