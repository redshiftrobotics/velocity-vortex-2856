/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.content.Intent;
/*     */ import android.view.View;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemClickListener;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FtcConfigurationActivity$8
/*     */   implements AdapterView.OnItemClickListener
/*     */ {
/*     */   FtcConfigurationActivity$8(FtcConfigurationActivity paramFtcConfigurationActivity) {}
/*     */   
/*     */   public void onItemClick(AdapterView<?> adapterView, View v, int pos, long arg3)
/*     */   {
/* 356 */     ControllerConfiguration localControllerConfiguration = (ControllerConfiguration)adapterView.getItemAtPosition(pos);
/* 357 */     DeviceConfiguration.ConfigurationType localConfigurationType = localControllerConfiguration.getType();
/* 358 */     int i; Intent localIntent; if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER)) {
/* 359 */       i = 1;
/* 360 */       localIntent = new Intent(FtcConfigurationActivity.k(this.a), EditMotorControllerActivity.class);
/* 361 */       localIntent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", localControllerConfiguration);
/* 362 */       localIntent.putExtra("requestCode", i);
/* 363 */       this.a.setResult(-1, localIntent);
/* 364 */       this.a.startActivityForResult(localIntent, i);
/*     */     }
/* 366 */     if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER)) {
/* 367 */       i = 2;
/* 368 */       localIntent = new Intent(FtcConfigurationActivity.k(this.a), EditServoControllerActivity.class);
/* 369 */       localIntent.putExtra("Edit Servo ControllerConfiguration Activity", localControllerConfiguration);
/* 370 */       localIntent.putExtra("requestCode", i);
/* 371 */       this.a.setResult(-1, localIntent);
/* 372 */       this.a.startActivityForResult(localIntent, i);
/*     */     }
/* 374 */     if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER)) {
/* 375 */       i = 3;
/* 376 */       localIntent = new Intent(FtcConfigurationActivity.k(this.a), EditLegacyModuleControllerActivity.class);
/* 377 */       localIntent.putExtra("EDIT_LEGACY_CONFIG", localControllerConfiguration);
/* 378 */       localIntent.putExtra("requestCode", i);
/* 379 */       this.a.setResult(-1, localIntent);
/* 380 */       this.a.startActivityForResult(localIntent, i);
/*     */     }
/* 382 */     if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE)) {
/* 383 */       i = 4;
/* 384 */       localIntent = new Intent(FtcConfigurationActivity.k(this.a), EditDeviceInterfaceModuleActivity.class);
/* 385 */       localIntent.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", localControllerConfiguration);
/* 386 */       localIntent.putExtra("requestCode", i);
/* 387 */       this.a.setResult(-1, localIntent);
/* 388 */       this.a.startActivityForResult(localIntent, i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\FtcConfigurationActivity$8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */