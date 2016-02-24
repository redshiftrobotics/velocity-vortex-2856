/*     */ package com.qualcomm.ftccommon;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.widget.TextView;
/*     */ import android.widget.Toast;
/*     */ import com.qualcomm.robotcore.hardware.Gamepad;
/*     */ import com.qualcomm.robotcore.util.Dimmer;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
/*     */ import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpdateUI
/*     */ {
/*     */   protected TextView textDeviceName;
/*     */   protected TextView textWifiDirectStatus;
/*     */   protected TextView textRobotStatus;
/*     */   
/*     */   public class Callback
/*     */   {
/*     */     public Callback() {}
/*     */     
/*     */     public void restartRobot()
/*     */     {
/*  53 */       UpdateUI.this.c.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/*  56 */           Toast.makeText(UpdateUI.this.c, "Restarting Robot", 0).show();
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*  61 */       });
/*  62 */       Thread local2 = new Thread() {
/*     */         public void run() {
/*     */           try {
/*  65 */             Thread.sleep(1500L); } catch (InterruptedException localInterruptedException) {}
/*  66 */           UpdateUI.this.c.runOnUiThread(new Runnable()
/*     */           {
/*     */             public void run() {
/*  69 */               UpdateUI.a(UpdateUI.this);
/*     */             }
/*     */             
/*     */           });
/*     */         }
/*  74 */       };
/*  75 */       local2.start();
/*     */     }
/*     */     
/*     */     public void updateUi(final String opModeName, final Gamepad[] gamepads) {
/*  79 */       UpdateUI.this.c.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/*  82 */           for (int i = 0; (i < UpdateUI.this.textGamepad.length) && (i < gamepads.length); i++) {
/*  83 */             if (gamepads[i].id == -1) {
/*  84 */               UpdateUI.this.textGamepad[i].setText(" ");
/*     */             } else {
/*  86 */               UpdateUI.this.textGamepad[i].setText(gamepads[i].toString());
/*     */             }
/*     */           }
/*     */           
/*  90 */           UpdateUI.this.textOpMode.setText("Op Mode: " + opModeName);
/*     */           
/*     */ 
/*  93 */           UpdateUI.this.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public void wifiDirectUpdate(WifiDirectAssistant.Event event) {
/*  99 */       String str = "Wifi Direct - ";
/*     */       
/* 101 */       switch (UpdateUI.3.a[event.ordinal()]) {
/*     */       case 1: 
/* 103 */         UpdateUI.a(UpdateUI.this, "Wifi Direct - disconnected");
/* 104 */         break;
/*     */       case 2: 
/* 106 */         UpdateUI.a(UpdateUI.this, "Wifi Direct - enabled");
/* 107 */         break;
/*     */       case 3: 
/* 109 */         UpdateUI.a(UpdateUI.this, "Wifi Direct - ERROR");
/* 110 */         break;
/*     */       case 4: 
/* 112 */         WifiDirectAssistant localWifiDirectAssistant = UpdateUI.this.b.getWifiDirectAssistant();
/* 113 */         UpdateUI.b(UpdateUI.this, localWifiDirectAssistant.getDeviceName());
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */     public void robotUpdate(final String status)
/*     */     {
/* 120 */       DbgLog.msg(status);
/* 121 */       UpdateUI.this.c.runOnUiThread(new Runnable()
/*     */       {
/*     */         public void run() {
/* 124 */           UpdateUI.this.textRobotStatus.setText(status);
/*     */           
/*     */ 
/*     */ 
/* 128 */           UpdateUI.this.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
/* 129 */           if (RobotLog.hasGlobalErrorMsg()) {
/* 130 */             UpdateUI.this.d.longBright();
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */   protected TextView[] textGamepad = new TextView[2];
/*     */   
/*     */   protected TextView textOpMode;
/*     */   protected TextView textErrorMessage;
/*     */   Restarter a;
/*     */   FtcRobotControllerService b;
/*     */   Activity c;
/*     */   Dimmer d;
/*     */   
/*     */   public UpdateUI(Activity activity, Dimmer dimmer)
/*     */   {
/* 154 */     this.c = activity;
/* 155 */     this.d = dimmer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTextViews(TextView textWifiDirectStatus, TextView textRobotStatus, TextView[] textGamepad, TextView textOpMode, TextView textErrorMessage, TextView textDeviceName)
/*     */   {
/* 162 */     this.textWifiDirectStatus = textWifiDirectStatus;
/* 163 */     this.textRobotStatus = textRobotStatus;
/* 164 */     this.textGamepad = textGamepad;
/* 165 */     this.textOpMode = textOpMode;
/* 166 */     this.textErrorMessage = textErrorMessage;
/* 167 */     this.textDeviceName = textDeviceName;
/*     */   }
/*     */   
/*     */   public void setControllerService(FtcRobotControllerService controllerService)
/*     */   {
/* 172 */     this.b = controllerService;
/*     */   }
/*     */   
/*     */   public void setRestarter(Restarter restarter) {
/* 176 */     this.a = restarter;
/*     */   }
/*     */   
/*     */   private void a(String paramString) {
/* 180 */     DbgLog.msg(paramString);
/* 181 */     final String str = paramString;
/* 182 */     this.c.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 185 */         UpdateUI.this.textWifiDirectStatus.setText(str);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void b(final String paramString) {
/* 191 */     this.c.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 194 */         UpdateUI.this.textDeviceName.setText(paramString);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void a() {
/* 200 */     this.a.requestRestart();
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\UpdateUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */