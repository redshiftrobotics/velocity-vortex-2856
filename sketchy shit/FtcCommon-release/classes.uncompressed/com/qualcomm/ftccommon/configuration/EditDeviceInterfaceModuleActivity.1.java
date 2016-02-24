/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.view.View;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemClickListener;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EditDeviceInterfaceModuleActivity$1
/*     */   implements AdapterView.OnItemClickListener
/*     */ {
/*     */   EditDeviceInterfaceModuleActivity$1(EditDeviceInterfaceModuleActivity paramEditDeviceInterfaceModuleActivity) {}
/*     */   
/*     */   public void onItemClick(AdapterView<?> parent, View view, int position, long id)
/*     */   {
/* 123 */     switch (position) {
/*     */     case 0: 
/* 125 */       EditDeviceInterfaceModuleActivity.a(this.a, 201, EditDeviceInterfaceModuleActivity.a(this.a).getPwmDevices(), EditPWMDevicesActivity.class);
/*     */       
/* 127 */       break;
/*     */     case 1: 
/* 129 */       EditDeviceInterfaceModuleActivity.a(this.a, 202, EditDeviceInterfaceModuleActivity.a(this.a).getI2cDevices(), EditI2cDevicesActivity.class);
/*     */       
/* 131 */       break;
/*     */     case 2: 
/* 133 */       EditDeviceInterfaceModuleActivity.a(this.a, 203, EditDeviceInterfaceModuleActivity.a(this.a).getAnalogInputDevices(), EditAnalogInputDevicesActivity.class);
/*     */       
/* 135 */       break;
/*     */     case 3: 
/* 137 */       EditDeviceInterfaceModuleActivity.a(this.a, 204, EditDeviceInterfaceModuleActivity.a(this.a).getDigitalDevices(), EditDigitalDevicesActivity.class);
/*     */       
/* 139 */       break;
/*     */     case 4: 
/* 141 */       EditDeviceInterfaceModuleActivity.a(this.a, 205, EditDeviceInterfaceModuleActivity.a(this.a).getAnalogOutputDevices(), EditAnalogOutputDevicesActivity.class);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditDeviceInterfaceModuleActivity$1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */