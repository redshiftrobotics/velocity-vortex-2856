/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Service;
/*     */ import android.content.Intent;
/*     */ import android.os.Binder;
/*     */ import android.os.Build;
/*     */ import android.os.IBinder;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoop;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager;
/*     */ import com.qualcomm.robotcore.eventloop.EventLoopManager.EventLoopMonitor;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.factory.RobotFactory;
/*     */ import com.qualcomm.robotcore.robot.Robot;
/*     */ import com.qualcomm.robotcore.robot.RobotState;
/*     */ import com.qualcomm.robotcore.util.ElapsedTime;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant.WifiDirectAssistantCallback;
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
/*     */ public class FtcRobotControllerService
/*     */   extends Service
/*     */   implements WifiDirectAssistant.WifiDirectAssistantCallback
/*     */ {
/*     */   private final IBinder a;
/*     */   private WifiDirectAssistant b;
/*     */   private Robot c;
/*     */   private EventLoop d;
/*     */   private WifiDirectAssistant.Event e;
/*     */   private String f;
/*     */   private UpdateUI.Callback g;
/*     */   private final a h;
/*     */   private final ElapsedTime i;
/*     */   private Thread j;
/*     */   
/*     */   public FtcRobotControllerService()
/*     */   {
/*  59 */     this.a = new FtcRobotControllerBinder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */     this.e = WifiDirectAssistant.Event.DISCONNECTED;
/*  66 */     this.f = "Robot Status: null";
/*     */     
/*  68 */     this.g = null;
/*  69 */     this.h = new a(null);
/*     */     
/*  71 */     this.i = new ElapsedTime();
/*     */     
/*  73 */     this.j = null; }
/*     */   
/*     */   public class FtcRobotControllerBinder extends Binder { public FtcRobotControllerBinder() {}
/*     */     
/*  77 */     public FtcRobotControllerService getService() { return FtcRobotControllerService.this; }
/*     */   }
/*     */   
/*     */   private class a implements EventLoopManager.EventLoopMonitor
/*     */   {
/*     */     private a() {}
/*     */     
/*     */     public void onStateChange(RobotState state) {
/*  85 */       if (FtcRobotControllerService.a(FtcRobotControllerService.this) == null) { return;
/*     */       }
/*  87 */       switch (FtcRobotControllerService.1.a[state.ordinal()]) {
/*     */       case 1: 
/*  89 */         FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: init");
/*  90 */         break;
/*     */       case 2: 
/*  92 */         FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: not started");
/*  93 */         break;
/*     */       case 3: 
/*  95 */         FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: running");
/*  96 */         break;
/*     */       case 4: 
/*  98 */         FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: stopped");
/*  99 */         break;
/*     */       case 5: 
/* 101 */         FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: EMERGENCY STOP");
/* 102 */         break;
/*     */       case 6: 
/* 104 */         FtcRobotControllerService.a(FtcRobotControllerService.this).robotUpdate("Robot Status: dropped connection");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class b implements Runnable
/*     */   {
/*     */     private b() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/* 115 */         if (FtcRobotControllerService.b(FtcRobotControllerService.this) != null) {
/* 116 */           FtcRobotControllerService.b(FtcRobotControllerService.this).shutdown();
/* 117 */           FtcRobotControllerService.a(FtcRobotControllerService.this, null);
/*     */         }
/*     */         
/* 120 */         FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: scanning for USB devices");
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 132 */           Thread.sleep(5000L);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException1) {
/* 135 */           FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: abort due to interrupt");
/* 136 */           return;
/*     */         }
/*     */         
/* 139 */         FtcRobotControllerService.a(FtcRobotControllerService.this, RobotFactory.createRobot());
/*     */         
/* 141 */         FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: waiting on network");
/*     */         
/*     */ 
/* 144 */         FtcRobotControllerService.c(FtcRobotControllerService.this).reset();
/* 145 */         while (!FtcRobotControllerService.d(FtcRobotControllerService.this).isConnected()) {
/*     */           try {
/* 147 */             Thread.sleep(1000L);
/* 148 */             if (FtcRobotControllerService.c(FtcRobotControllerService.this).time() > 120.0D) {
/* 149 */               FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: network timed out");
/* 150 */               return;
/*     */             }
/*     */           } catch (InterruptedException localInterruptedException2) {
/* 153 */             DbgLog.msg("interrupt waiting for network; aborting setup");
/* 154 */             return;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 159 */         FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: starting robot");
/*     */         try {
/* 161 */           FtcRobotControllerService.b(FtcRobotControllerService.this).eventLoopManager.setMonitor(FtcRobotControllerService.e(FtcRobotControllerService.this));
/* 162 */           InetAddress localInetAddress = FtcRobotControllerService.d(FtcRobotControllerService.this).getGroupOwnerAddress();
/* 163 */           FtcRobotControllerService.b(FtcRobotControllerService.this).start(localInetAddress, FtcRobotControllerService.f(FtcRobotControllerService.this));
/*     */         } catch (RobotCoreException localRobotCoreException1) {
/* 165 */           FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: failed to start robot");
/* 166 */           RobotLog.setGlobalErrorMsg(localRobotCoreException1.getMessage());
/*     */         }
/*     */       } catch (RobotCoreException localRobotCoreException2) {
/* 169 */         FtcRobotControllerService.a(FtcRobotControllerService.this, "Robot Status: Unable to create robot!");
/* 170 */         RobotLog.setGlobalErrorMsg(localRobotCoreException2.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public WifiDirectAssistant getWifiDirectAssistant() {
/* 176 */     return this.b;
/*     */   }
/*     */   
/*     */   public WifiDirectAssistant.Event getWifiDirectStatus() {
/* 180 */     return this.e;
/*     */   }
/*     */   
/*     */   public String getRobotStatus() {
/* 184 */     return this.f;
/*     */   }
/*     */   
/*     */   public IBinder onBind(Intent intent)
/*     */   {
/* 189 */     DbgLog.msg("Starting FTC Controller Service");
/*     */     
/* 191 */     DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
/*     */     
/* 193 */     this.b = WifiDirectAssistant.getWifiDirectAssistant(this);
/* 194 */     this.b.setCallback(this);
/*     */     
/* 196 */     this.b.enable();
/* 197 */     if (Build.MODEL.equals("FL7007"))
/*     */     {
/* 199 */       this.b.discoverPeers();
/*     */     } else {
/* 201 */       this.b.createGroup();
/*     */     }
/*     */     
/* 204 */     return this.a;
/*     */   }
/*     */   
/*     */   public boolean onUnbind(Intent intent)
/*     */   {
/* 209 */     DbgLog.msg("Stopping FTC Controller Service");
/*     */     
/* 211 */     this.b.disable();
/* 212 */     shutdownRobot();
/*     */     
/* 214 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized void setCallback(UpdateUI.Callback callback) {
/* 218 */     this.g = callback;
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
/*     */   public synchronized void setupRobot(EventLoop eventLoop)
/*     */   {
/* 231 */     if ((this.j != null) && (this.j.isAlive())) {
/* 232 */       DbgLog.msg("FtcRobotControllerService.setupRobot() is currently running, stopping old setup");
/* 233 */       this.j.interrupt();
/* 234 */       while (this.j.isAlive() == true) Thread.yield();
/* 235 */       DbgLog.msg("Old setup stopped; restarting setup");
/*     */     }
/*     */     
/* 238 */     RobotLog.clearGlobalErrorMsg();
/* 239 */     DbgLog.msg("Processing robot setup");
/*     */     
/* 241 */     this.d = eventLoop;
/*     */     
/*     */ 
/* 244 */     this.j = new Thread(new b(null), "Robot Setup");
/* 245 */     this.j.start();
/*     */     
/*     */ 
/* 248 */     while (this.j.getState() == Thread.State.NEW) { Thread.yield();
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void shutdownRobot()
/*     */   {
/* 254 */     if ((this.j != null) && (this.j.isAlive())) { this.j.interrupt();
/*     */     }
/*     */     
/* 257 */     if (this.c != null) this.c.shutdown();
/* 258 */     this.c = null;
/* 259 */     a("Robot Status: null");
/*     */   }
/*     */   
/*     */ 
/*     */   public void onWifiDirectEvent(WifiDirectAssistant.Event event)
/*     */   {
/* 265 */     switch (1.b[event.ordinal()]) {
/*     */     case 1: 
/* 267 */       DbgLog.msg("Wifi Direct - Group Owner");
/* 268 */       this.b.cancelDiscoverPeers();
/* 269 */       break;
/*     */     case 2: 
/* 271 */       DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
/*     */       
/*     */ 
/*     */ 
/* 275 */       break;
/*     */     case 3: 
/* 277 */       DbgLog.msg("Wifi Direct Passphrase: " + this.b.getPassphrase());
/* 278 */       break;
/*     */     case 4: 
/* 280 */       DbgLog.error("Wifi Direct Error: " + this.b.getFailureReason());
/* 281 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 286 */     a(event);
/*     */   }
/*     */   
/*     */   private void a(WifiDirectAssistant.Event paramEvent) {
/* 290 */     this.e = paramEvent;
/* 291 */     if (this.g != null) this.g.wifiDirectUpdate(this.e);
/*     */   }
/*     */   
/*     */   private void a(String paramString) {
/* 295 */     this.f = paramString;
/* 296 */     if (this.g != null) {
/* 297 */       this.g.robotUpdate(paramString);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\FtcRobotControllerService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */