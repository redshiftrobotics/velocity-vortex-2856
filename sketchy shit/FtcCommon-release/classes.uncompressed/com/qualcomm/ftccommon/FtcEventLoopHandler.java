/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.qualcomm.hardware.HardwareFactory;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DcMotorController;
/*     */ import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap;
/*     */ import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
/*     */ import com.qualcomm.robotcore.hardware.LegacyModule;
/*     */ import com.qualcomm.robotcore.hardware.ServoController;
/*     */ import com.qualcomm.robotcore.hardware.VoltageSensor;
/*     */ import com.qualcomm.robotcore.robocol.Command;
/*     */ import com.qualcomm.robotcore.robocol.Telemetry;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.util.BatteryChecker;
/*     */ import com.qualcomm.robotcore.util.BatteryChecker.BatteryWatcher;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcEventLoopHandler
/*     */   implements BatteryChecker.BatteryWatcher
/*     */ {
/*     */   public static final String NO_VOLTAGE_SENSOR = "no voltage sensor found";
/*     */   EventLoopManager a;
/*     */   BatteryChecker b;
/*     */   Context c;
/*  69 */   ElapsedTime d = new ElapsedTime();
/*  70 */   double e = 0.25D;
/*     */   
/*     */   UpdateUI.Callback f;
/*     */   
/*     */   HardwareFactory g;
/*     */   
/*  76 */   HardwareMap h = new HardwareMap();
/*     */   
/*     */   public FtcEventLoopHandler(HardwareFactory hardwareFactory, UpdateUI.Callback callback, Context robotControllerContext) {
/*  79 */     this.g = hardwareFactory;
/*  80 */     this.f = callback;
/*  81 */     this.c = robotControllerContext;
/*     */     
/*  83 */     long l = 180000L;
/*  84 */     this.b = new BatteryChecker(robotControllerContext, this, l);
/*  85 */     this.b.startBatteryMonitoring();
/*     */   }
/*     */   
/*     */   public void init(EventLoopManager eventLoopManager) {
/*  89 */     this.a = eventLoopManager;
/*     */   }
/*     */   
/*     */   public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
/*  93 */     this.h = this.g.createHardwareMap(this.a);
/*  94 */     return this.h;
/*     */   }
/*     */   
/*     */   public void displayGamePadInfo(String activeOpModeName)
/*     */   {
/*  99 */     Gamepad[] arrayOfGamepad = this.a.getGamepads();
/*     */     
/* 101 */     this.f.updateUi(activeOpModeName, arrayOfGamepad);
/*     */   }
/*     */   
/*     */   public Gamepad[] getGamepads() {
/* 105 */     return this.a.getGamepads();
/*     */   }
/*     */   
/*     */   public void resetGamepads() {
/* 109 */     this.a.resetGamepads();
/*     */   }
/*     */   
/*     */   public void sendTelemetryData(Telemetry telemetry) {
/* 113 */     if (this.d.time() > this.e) {
/* 114 */       this.d.reset();
/*     */       
/* 116 */       if (telemetry.hasData()) this.a.sendTelemetryData(telemetry);
/* 117 */       telemetry.clearData();
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendBatteryInfo()
/*     */   {
/* 123 */     a();
/* 124 */     b();
/*     */   }
/*     */   
/*     */   private void a() {
/* 128 */     float f1 = this.b.getBatteryLevel();
/* 129 */     sendTelemetry("RobotController Battery Level", String.valueOf(f1));
/*     */   }
/*     */   
/*     */   private void b() {
/* 133 */     double d1 = Double.MAX_VALUE;
/*     */     
/* 135 */     for (Object localObject1 = this.h.voltageSensor.iterator(); ((Iterator)localObject1).hasNext();) { localObject2 = (VoltageSensor)((Iterator)localObject1).next();
/* 136 */       if (((VoltageSensor)localObject2).getVoltage() < d1) {
/* 137 */         d1 = ((VoltageSensor)localObject2).getVoltage();
/*     */       }
/*     */     }
/*     */     
/*     */     Object localObject2;
/* 142 */     if (this.h.voltageSensor.size() == 0) {
/* 143 */       localObject1 = "no voltage sensor found";
/*     */     } else {
/* 145 */       localObject2 = new BigDecimal(d1).setScale(2, RoundingMode.HALF_UP);
/* 146 */       localObject1 = String.valueOf(((BigDecimal)localObject2).doubleValue());
/*     */     }
/* 148 */     sendTelemetry("Robot Battery Level", (String)localObject1);
/*     */   }
/*     */   
/*     */   public void sendTelemetry(String tag, String msg) {
/* 152 */     Telemetry localTelemetry = new Telemetry();
/* 153 */     localTelemetry.setTag(tag);
/* 154 */     localTelemetry.addData(tag, msg);
/* 155 */     if (this.a != null) {
/* 156 */       this.a.sendTelemetryData(localTelemetry);
/* 157 */       localTelemetry.clearData();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdownMotorControllers() {
/* 162 */     for (Map.Entry localEntry : this.h.dcMotorController.entrySet()) {
/* 163 */       String str = (String)localEntry.getKey();
/* 164 */       DcMotorController localDcMotorController = (DcMotorController)localEntry.getValue();
/* 165 */       DbgLog.msg("Stopping DC Motor Controller " + str);
/* 166 */       localDcMotorController.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdownServoControllers() {
/* 171 */     for (Map.Entry localEntry : this.h.servoController.entrySet()) {
/* 172 */       String str = (String)localEntry.getKey();
/* 173 */       ServoController localServoController = (ServoController)localEntry.getValue();
/* 174 */       DbgLog.msg("Stopping Servo Controller " + str);
/* 175 */       localServoController.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdownLegacyModules() {
/* 180 */     for (Map.Entry localEntry : this.h.legacyModule.entrySet()) {
/* 181 */       String str = (String)localEntry.getKey();
/* 182 */       LegacyModule localLegacyModule = (LegacyModule)localEntry.getValue();
/* 183 */       DbgLog.msg("Stopping Legacy Module " + str);
/* 184 */       localLegacyModule.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdownCoreInterfaceDeviceModules() {
/* 189 */     for (Map.Entry localEntry : this.h.deviceInterfaceModule.entrySet()) {
/* 190 */       String str = (String)localEntry.getKey();
/* 191 */       DeviceInterfaceModule localDeviceInterfaceModule = (DeviceInterfaceModule)localEntry.getValue();
/* 192 */       DbgLog.msg("Stopping Core Interface Device Module " + str);
/* 193 */       localDeviceInterfaceModule.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void restartRobot() {
/* 198 */     this.b.endBatteryMonitoring();
/* 199 */     this.f.restartRobot();
/*     */   }
/*     */   
/*     */   public void sendCommand(Command command) {
/* 203 */     this.a.sendCommand(command);
/*     */   }
/*     */   
/*     */   public String getOpMode(String extra) {
/* 207 */     if (this.a.state != RobotState.RUNNING) {
/* 208 */       return "Stop Robot";
/*     */     }
/* 210 */     return extra;
/*     */   }
/*     */   
/*     */   public void updateBatteryLevel(float percent) {
/* 214 */     sendTelemetry("RobotController Battery Level", String.valueOf(percent));
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcEventLoopHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */