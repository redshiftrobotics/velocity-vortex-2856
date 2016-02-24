/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoop;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
/*     */ import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcEventLoop
/*     */   implements EventLoop
/*     */ {
/*     */   FtcEventLoopHandler a;
/*  57 */   OpModeManager b = new OpModeManager(new HardwareMap());
/*     */   OpModeRegister c;
/*     */   
/*     */   public FtcEventLoop(HardwareFactory hardwareFactory, OpModeRegister register, UpdateUI.Callback callback, Context robotControllerContext) {
/*  61 */     this.a = new FtcEventLoopHandler(hardwareFactory, callback, robotControllerContext);
/*  62 */     this.c = register;
/*     */   }
/*     */   
/*     */   public OpModeManager getOpModeManager() {
/*  66 */     return this.b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init(EventLoopManager eventLoopManager)
/*     */     throws RobotCoreException, InterruptedException
/*     */   {
/*  82 */     DbgLog.msg("======= INIT START =======");
/*     */     
/*  84 */     this.b.registerOpModes(this.c);
/*     */     
/*  86 */     this.a.init(eventLoopManager);
/*  87 */     HardwareMap localHardwareMap = this.a.getHardwareMap();
/*     */     
/*  89 */     ModernRoboticsUsbUtil.init(localHardwareMap.appContext, localHardwareMap);
/*     */     
/*  91 */     this.b.setHardwareMap(localHardwareMap);
/*  92 */     localHardwareMap.logDevices();
/*     */     
/*  94 */     DbgLog.msg("======= INIT FINISH =======");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loop()
/*     */     throws RobotCoreException
/*     */   {
/* 105 */     this.a.displayGamePadInfo(this.b.getActiveOpModeName());
/* 106 */     Gamepad[] arrayOfGamepad = this.a.getGamepads();
/*     */     
/* 108 */     this.b.runActiveOpMode(arrayOfGamepad);
/*     */     
/*     */ 
/* 111 */     this.a.sendTelemetryData(this.b.getActiveOpMode().telemetry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void teardown()
/*     */     throws RobotCoreException
/*     */   {
/* 128 */     DbgLog.msg("======= TEARDOWN =======");
/*     */     
/* 130 */     this.b.stopActiveOpMode();
/*     */     
/* 132 */     this.a.shutdownMotorControllers();
/*     */     
/* 134 */     this.a.shutdownServoControllers();
/*     */     
/*     */ 
/*     */ 
/* 138 */     this.a.shutdownLegacyModules();
/*     */     
/* 140 */     this.a.shutdownCoreInterfaceDeviceModules();
/*     */     
/* 142 */     DbgLog.msg("======= TEARDOWN COMPLETE =======");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processCommand(Command command)
/*     */   {
/* 151 */     DbgLog.msg("Processing Command: " + command.getName() + " " + command.getExtra());
/*     */     
/* 153 */     this.a.sendBatteryInfo();
/*     */     
/* 155 */     String str1 = command.getName();
/* 156 */     String str2 = command.getExtra();
/*     */     
/* 158 */     if (str1.equals("CMD_RESTART_ROBOT")) {
/* 159 */       a();
/* 160 */     } else if (str1.equals("CMD_REQUEST_OP_MODE_LIST")) {
/* 161 */       b();
/* 162 */     } else if (str1.equals("CMD_INIT_OP_MODE")) {
/* 163 */       a(str2);
/* 164 */     } else if (str1.equals("CMD_RUN_OP_MODE")) {
/* 165 */       c();
/*     */     } else {
/* 167 */       DbgLog.msg("Unknown command: " + str1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a() {
/* 172 */     this.a.restartRobot();
/*     */   }
/*     */   
/*     */   private void b() {
/* 176 */     String str1 = "";
/* 177 */     for (String str2 : this.b.getOpModes()) {
/* 178 */       if (!str2.equals("Stop Robot")) {
/* 179 */         if (!str1.isEmpty()) str1 = str1 + Util.ASCII_RECORD_SEPARATOR;
/* 180 */         str1 = str1 + str2;
/*     */       }
/*     */     }
/* 183 */     this.a.sendCommand(new Command("CMD_REQUEST_OP_MODE_LIST_RESP", str1));
/*     */   }
/*     */   
/*     */ 
/*     */   private void a(String paramString)
/*     */   {
/* 189 */     String str = this.a.getOpMode(paramString);
/*     */     
/* 191 */     this.a.resetGamepads();
/* 192 */     this.b.initActiveOpMode(str);
/*     */     
/*     */ 
/* 195 */     this.a.sendCommand(new Command("CMD_INIT_OP_MODE_RESP", str));
/*     */   }
/*     */   
/*     */   private void c()
/*     */   {
/* 200 */     this.b.startActiveOpMode();
/*     */     
/*     */ 
/* 203 */     this.a.sendCommand(new Command("CMD_RUN_OP_MODE_RESP", this.b.getActiveOpModeName()));
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */