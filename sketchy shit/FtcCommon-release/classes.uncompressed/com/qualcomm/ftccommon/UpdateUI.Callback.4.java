/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.robotcore.util.Dimmer;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UpdateUI$Callback$4
/*     */   implements Runnable
/*     */ {
/*     */   UpdateUI$Callback$4(UpdateUI.Callback paramCallback, String paramString) {}
/*     */   
/*     */   public void run()
/*     */   {
/* 124 */     this.b.a.textRobotStatus.setText(this.a);
/*     */     
/*     */ 
/*     */ 
/* 128 */     this.b.a.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
/* 129 */     if (RobotLog.hasGlobalErrorMsg()) {
/* 130 */       this.b.a.d.longBright();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\UpdateUI$Callback$4.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */