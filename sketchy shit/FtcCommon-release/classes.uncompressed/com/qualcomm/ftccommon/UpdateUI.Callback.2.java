/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.app.Activity;
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
/*    */ class UpdateUI$Callback$2
/*    */   extends Thread
/*    */ {
/*    */   UpdateUI$Callback$2(UpdateUI.Callback paramCallback) {}
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 65 */       Thread.sleep(1500L); } catch (InterruptedException localInterruptedException) {}
/* 66 */     this.a.a.c.runOnUiThread(new Runnable()
/*    */     {
/*    */       public void run() {
/* 69 */         UpdateUI.a(UpdateUI.Callback.2.this.a.a);
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\UpdateUI$Callback$2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */