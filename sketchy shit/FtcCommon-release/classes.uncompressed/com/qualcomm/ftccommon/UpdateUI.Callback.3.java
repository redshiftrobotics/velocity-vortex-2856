/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.widget.TextView;
/*    */ import com.qualcomm.robotcore.hardware.Gamepad;
/*    */ import com.qualcomm.robotcore.util.RobotLog;
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
/*    */ class UpdateUI$Callback$3
/*    */   implements Runnable
/*    */ {
/*    */   UpdateUI$Callback$3(UpdateUI.Callback paramCallback, Gamepad[] paramArrayOfGamepad, String paramString) {}
/*    */   
/*    */   public void run()
/*    */   {
/* 82 */     for (int i = 0; (i < this.c.a.textGamepad.length) && (i < this.a.length); i++) {
/* 83 */       if (this.a[i].id == -1) {
/* 84 */         this.c.a.textGamepad[i].setText(" ");
/*    */       } else {
/* 86 */         this.c.a.textGamepad[i].setText(this.a[i].toString());
/*    */       }
/*    */     }
/*    */     
/* 90 */     this.c.a.textOpMode.setText("Op Mode: " + this.b);
/*    */     
/*    */ 
/* 93 */     this.c.a.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\UpdateUI$Callback$3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */