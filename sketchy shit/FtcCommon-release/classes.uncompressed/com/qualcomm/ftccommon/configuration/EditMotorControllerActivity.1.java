/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.CheckBox;
/*     */ import android.widget.EditText;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EditMotorControllerActivity$1
/*     */   implements View.OnClickListener
/*     */ {
/*     */   EditMotorControllerActivity$1(EditMotorControllerActivity paramEditMotorControllerActivity) {}
/*     */   
/*     */   public void onClick(View view)
/*     */   {
/* 141 */     if (((CheckBox)view).isChecked()) {
/* 142 */       EditMotorControllerActivity.a(this.a, true);
/* 143 */       EditMotorControllerActivity.a(this.a).setEnabled(true);
/* 144 */       EditMotorControllerActivity.a(this.a).setText("");
/* 145 */       EditMotorControllerActivity.b(this.a).setPort(1);
/* 146 */       EditMotorControllerActivity.b(this.a).setType(DeviceConfiguration.ConfigurationType.MOTOR);
/*     */     } else {
/* 148 */       EditMotorControllerActivity.a(this.a, false);
/* 149 */       EditMotorControllerActivity.a(this.a).setEnabled(false);
/* 150 */       EditMotorControllerActivity.a(this.a).setText("NO DEVICE ATTACHED");
/* 151 */       EditMotorControllerActivity.b(this.a).setType(DeviceConfiguration.ConfigurationType.NOTHING);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditMotorControllerActivity$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */