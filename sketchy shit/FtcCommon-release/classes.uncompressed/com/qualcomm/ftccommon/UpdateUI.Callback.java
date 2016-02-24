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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpdateUI$Callback
/*     */ {
/*     */   public UpdateUI$Callback(UpdateUI paramUpdateUI) {}
/*     */   
/*     */   public void restartRobot()
/*     */   {
/*  53 */     this.a.c.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/*  56 */         Toast.makeText(UpdateUI.Callback.this.a.c, "Restarting Robot", 0).show();
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*  61 */     });
/*  62 */     Thread local2 = new Thread() {
/*     */       public void run() {
/*     */         try {
/*  65 */           Thread.sleep(1500L); } catch (InterruptedException localInterruptedException) {}
/*  66 */         UpdateUI.Callback.this.a.c.runOnUiThread(new Runnable()
/*     */         {
/*     */           public void run() {
/*  69 */             UpdateUI.a(UpdateUI.Callback.this.a);
/*     */           }
/*     */           
/*     */         });
/*     */       }
/*  74 */     };
/*  75 */     local2.start();
/*     */   }
/*     */   
/*     */   public void updateUi(final String opModeName, final Gamepad[] gamepads) {
/*  79 */     this.a.c.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/*  82 */         for (int i = 0; (i < UpdateUI.Callback.this.a.textGamepad.length) && (i < gamepads.length); i++) {
/*  83 */           if (gamepads[i].id == -1) {
/*  84 */             UpdateUI.Callback.this.a.textGamepad[i].setText(" ");
/*     */           } else {
/*  86 */             UpdateUI.Callback.this.a.textGamepad[i].setText(gamepads[i].toString());
/*     */           }
/*     */         }
/*     */         
/*  90 */         UpdateUI.Callback.this.a.textOpMode.setText("Op Mode: " + opModeName);
/*     */         
/*     */ 
/*  93 */         UpdateUI.Callback.this.a.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void wifiDirectUpdate(WifiDirectAssistant.Event event) {
/*  99 */     String str = "Wifi Direct - ";
/*     */     
/* 101 */     switch (UpdateUI.3.a[event.ordinal()]) {
/*     */     case 1: 
/* 103 */       UpdateUI.a(this.a, "Wifi Direct - disconnected");
/* 104 */       break;
/*     */     case 2: 
/* 106 */       UpdateUI.a(this.a, "Wifi Direct - enabled");
/* 107 */       break;
/*     */     case 3: 
/* 109 */       UpdateUI.a(this.a, "Wifi Direct - ERROR");
/* 110 */       break;
/*     */     case 4: 
/* 112 */       WifiDirectAssistant localWifiDirectAssistant = this.a.b.getWifiDirectAssistant();
/* 113 */       UpdateUI.b(this.a, localWifiDirectAssistant.getDeviceName());
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public void robotUpdate(final String status)
/*     */   {
/* 120 */     DbgLog.msg(status);
/* 121 */     this.a.c.runOnUiThread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 124 */         UpdateUI.Callback.this.a.textRobotStatus.setText(status);
/*     */         
/*     */ 
/*     */ 
/* 128 */         UpdateUI.Callback.this.a.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
/* 129 */         if (RobotLog.hasGlobalErrorMsg()) {
/* 130 */           UpdateUI.Callback.this.a.d.longBright();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\UpdateUI$Callback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */