/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.factory.RobotFactory;
/*     */ import com.qualcomm.robotcore.robot.Robot;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
/*     */ import java.net.InetAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FtcRobotControllerService$b
/*     */   implements Runnable
/*     */ {
/*     */   private FtcRobotControllerService$b(FtcRobotControllerService paramFtcRobotControllerService) {}
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 115 */       if (FtcRobotControllerService.b(this.a) != null) {
/* 116 */         FtcRobotControllerService.b(this.a).shutdown();
/* 117 */         FtcRobotControllerService.a(this.a, null);
/*     */       }
/*     */       
/* 120 */       FtcRobotControllerService.a(this.a, "Robot Status: scanning for USB devices");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 132 */         Thread.sleep(5000L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException1) {
/* 135 */         FtcRobotControllerService.a(this.a, "Robot Status: abort due to interrupt");
/* 136 */         return;
/*     */       }
/*     */       
/* 139 */       FtcRobotControllerService.a(this.a, RobotFactory.createRobot());
/*     */       
/* 141 */       FtcRobotControllerService.a(this.a, "Robot Status: waiting on network");
/*     */       
/*     */ 
/* 144 */       FtcRobotControllerService.c(this.a).reset();
/* 145 */       while (!FtcRobotControllerService.d(this.a).isConnected()) {
/*     */         try {
/* 147 */           Thread.sleep(1000L);
/* 148 */           if (FtcRobotControllerService.c(this.a).time() > 120.0D) {
/* 149 */             FtcRobotControllerService.a(this.a, "Robot Status: network timed out");
/* 150 */             return;
/*     */           }
/*     */         } catch (InterruptedException localInterruptedException2) {
/* 153 */           DbgLog.msg("interrupt waiting for network; aborting setup");
/* 154 */           return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 159 */       FtcRobotControllerService.a(this.a, "Robot Status: starting robot");
/*     */       try {
/* 161 */         FtcRobotControllerService.b(this.a).eventLoopManager.setMonitor(FtcRobotControllerService.e(this.a));
/* 162 */         InetAddress localInetAddress = FtcRobotControllerService.d(this.a).getGroupOwnerAddress();
/* 163 */         FtcRobotControllerService.b(this.a).start(localInetAddress, FtcRobotControllerService.f(this.a));
/*     */       } catch (RobotCoreException localRobotCoreException1) {
/* 165 */         FtcRobotControllerService.a(this.a, "Robot Status: failed to start robot");
/* 166 */         RobotLog.setGlobalErrorMsg(localRobotCoreException1.getMessage());
/*     */       }
/*     */     } catch (RobotCoreException localRobotCoreException2) {
/* 169 */       FtcRobotControllerService.a(this.a, "Robot Status: Unable to create robot!");
/* 170 */       RobotLog.setGlobalErrorMsg(localRobotCoreException2.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcRobotControllerService$b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */