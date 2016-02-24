/*    */ package com.qualcomm.ftccommon;
/*    */ 
/*    */ import android.util.Log;
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
/*    */ public class DbgLog
/*    */ {
/*    */   public static final String TAG = "FIRST";
/*    */   public static final String ERROR_PREPEND = "### ERROR: ";
/*    */   
/*    */   public static void msg(String message)
/*    */   {
/* 52 */     Log.i("FIRST", message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void error(String message)
/*    */   {
/* 62 */     Log.e("FIRST", "### ERROR: " + message);
/*    */   }
/*    */   
/*    */   public static void logStacktrace(Exception e) {
/* 66 */     msg(e.toString());
/* 67 */     for (StackTraceElement localStackTraceElement : e.getStackTrace()) {
/* 68 */       msg(localStackTraceElement.toString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/matt/Documents/robotics/2856/SwerveRoboticsLibrary/build/intermediates/exploded-aar/FtcCommon-release/jars/classes.jar!/com/qualcomm/ftccommon/DbgLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */