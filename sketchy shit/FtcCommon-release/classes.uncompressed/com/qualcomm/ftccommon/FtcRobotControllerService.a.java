/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager.EventLoopMonitor;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FtcRobotControllerService$a
/*     */   implements EventLoopManager.EventLoopMonitor
/*     */ {
/*     */   private FtcRobotControllerService$a(FtcRobotControllerService paramFtcRobotControllerService) {}
/*     */   
/*     */   public void onStateChange(RobotState state)
/*     */   {
/*  85 */     if (FtcRobotControllerService.a(this.a) == null) { return;
/*     */     }
/*  87 */     switch (FtcRobotControllerService.1.a[state.ordinal()]) {
/*     */     case 1: 
/*  89 */       FtcRobotControllerService.a(this.a).robotUpdate("Robot Status: init");
/*  90 */       break;
/*     */     case 2: 
/*  92 */       FtcRobotControllerService.a(this.a).robotUpdate("Robot Status: not started");
/*  93 */       break;
/*     */     case 3: 
/*  95 */       FtcRobotControllerService.a(this.a).robotUpdate("Robot Status: running");
/*  96 */       break;
/*     */     case 4: 
/*  98 */       FtcRobotControllerService.a(this.a).robotUpdate("Robot Status: stopped");
/*  99 */       break;
/*     */     case 5: 
/* 101 */       FtcRobotControllerService.a(this.a).robotUpdate("Robot Status: EMERGENCY STOP");
/* 102 */       break;
/*     */     case 6: 
/* 104 */       FtcRobotControllerService.a(this.a).robotUpdate("Robot Status: dropped connection");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcRobotControllerService$a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */