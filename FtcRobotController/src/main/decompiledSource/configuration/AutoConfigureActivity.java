/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Context;
/*     */ import android.os.Bundle;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.Button;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.Toast;
/*     */ import com.qualcomm.ftccommon.DbgLog;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.hardware.HardwareDeviceManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoConfigureActivity
/*     */   extends Activity
/*     */ {
/*     */   private Context a;
/*     */   private Button b;
/*     */   private Button c;
/*     */   private DeviceManager d;
/*  71 */   protected Map<SerialNumber, DeviceManager.DeviceType> scannedDevices = new HashMap();
/*  72 */   protected Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> entries = new HashSet();
/*  73 */   private Map<SerialNumber, ControllerConfiguration> e = new HashMap();
/*     */   
/*     */   private Thread f;
/*     */   
/*     */   private Utility g;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  81 */     super.onCreate(savedInstanceState);
/*  82 */     this.a = this;
/*  83 */     setContentView(R.layout.activity_autoconfigure);
/*     */     
/*  85 */     this.g = new Utility(this);
/*  86 */     this.b = ((Button)findViewById(R.id.configureLegacy));
/*  87 */     this.c = ((Button)findViewById(R.id.configureUSB));
/*     */     try
/*     */     {
/*  90 */       this.d = new HardwareDeviceManager(this.a, null);
/*     */     } catch (RobotCoreException localRobotCoreException) {
/*  92 */       this.g.complainToast("Failed to open the Device Manager", this.a);
/*  93 */       DbgLog.error("Failed to open deviceManager: " + localRobotCoreException.toString());
/*  94 */       DbgLog.logStacktrace(localRobotCoreException);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 100 */     super.onStart();
/*     */     
/* 102 */     this.g.updateHeader("K9LegacyBot", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 103 */     String str = this.g.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
/* 104 */     if ((str.equals("K9LegacyBot")) || (str.equals("K9USBBot"))) {
/* 105 */       d();
/*     */     } else {
/* 107 */       a();
/*     */     }
/* 109 */     this.b.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view)
/*     */       {
/* 113 */         AutoConfigureActivity.a(AutoConfigureActivity.this, new Thread(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 117 */               DbgLog.msg("Scanning USB bus");
/* 118 */               AutoConfigureActivity.this.scannedDevices = AutoConfigureActivity.a(AutoConfigureActivity.this).scanForUsbDevices();
/*     */             } catch (RobotCoreException localRobotCoreException) {
/* 120 */               DbgLog.error("Device scan failed");
/*     */             }
/*     */             
/* 123 */             AutoConfigureActivity.this.runOnUiThread(new Runnable()
/*     */             {
/*     */               public void run() {
/* 126 */                 AutoConfigureActivity.b(AutoConfigureActivity.this).resetCount();
/* 127 */                 if (AutoConfigureActivity.this.scannedDevices.size() == 0) {
/* 128 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 129 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 130 */                   AutoConfigureActivity.c(AutoConfigureActivity.this);
/*     */                 }
/* 132 */                 AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
/* 133 */                 AutoConfigureActivity.a(AutoConfigureActivity.this, new HashMap());
/*     */                 
/* 135 */                 AutoConfigureActivity.b(AutoConfigureActivity.this).createLists(AutoConfigureActivity.this.entries, AutoConfigureActivity.d(AutoConfigureActivity.this));
/* 136 */                 boolean bool = AutoConfigureActivity.e(AutoConfigureActivity.this);
/* 137 */                 if (bool) {
/* 138 */                   AutoConfigureActivity.a(AutoConfigureActivity.this, "K9LegacyBot");
/*     */                 } else {
/* 140 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 141 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 142 */                   AutoConfigureActivity.f(AutoConfigureActivity.this);
/*     */                 }
/*     */               }
/*     */             });
/*     */           }
/* 147 */         }));
/* 148 */         AutoConfigureActivity.g(AutoConfigureActivity.this).start();
/*     */       }
/*     */       
/* 151 */     });
/* 152 */     this.c.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view)
/*     */       {
/* 156 */         AutoConfigureActivity.a(AutoConfigureActivity.this, new Thread(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 160 */               DbgLog.msg("Scanning USB bus");
/* 161 */               AutoConfigureActivity.this.scannedDevices = AutoConfigureActivity.a(AutoConfigureActivity.this).scanForUsbDevices();
/*     */             } catch (RobotCoreException localRobotCoreException) {
/* 163 */               DbgLog.error("Device scan failed");
/*     */             }
/*     */             
/* 166 */             AutoConfigureActivity.this.runOnUiThread(new Runnable()
/*     */             {
/*     */               public void run() {
/* 169 */                 AutoConfigureActivity.b(AutoConfigureActivity.this).resetCount();
/* 170 */                 if (AutoConfigureActivity.this.scannedDevices.size() == 0) {
/* 171 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 172 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 173 */                   AutoConfigureActivity.c(AutoConfigureActivity.this);
/*     */                 }
/* 175 */                 AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
/* 176 */                 AutoConfigureActivity.a(AutoConfigureActivity.this, new HashMap());
/*     */                 
/* 178 */                 AutoConfigureActivity.b(AutoConfigureActivity.this).createLists(AutoConfigureActivity.this.entries, AutoConfigureActivity.d(AutoConfigureActivity.this));
/* 179 */                 boolean bool = AutoConfigureActivity.h(AutoConfigureActivity.this);
/* 180 */                 if (bool) {
/* 181 */                   AutoConfigureActivity.a(AutoConfigureActivity.this, "K9USBBot");
/*     */                 } else {
/* 183 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 184 */                   AutoConfigureActivity.b(AutoConfigureActivity.this).updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 185 */                   AutoConfigureActivity.i(AutoConfigureActivity.this);
/*     */                 }
/*     */               }
/*     */             });
/*     */           }
/* 190 */         }));
/* 191 */         AutoConfigureActivity.g(AutoConfigureActivity.this).start();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void a(String paramString) {
/* 197 */     this.g.writeXML(this.e);
/*     */     try {
/* 199 */       this.g.writeToFile(paramString + ".xml");
/* 200 */       this.g.saveToPreferences(paramString, R.string.pref_hardware_config_filename);
/* 201 */       this.g.updateHeader(paramString, R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 202 */       Toast.makeText(this.a, "AutoConfigure " + paramString + " Successful", 0).show();
/*     */     } catch (RobotCoreException localRobotCoreException) {
/* 204 */       this.g.complainToast(localRobotCoreException.getMessage(), this.a);
/* 205 */       DbgLog.error(localRobotCoreException.getMessage());
/*     */     } catch (IOException localIOException) {
/* 207 */       this.g.complainToast("Found " + localIOException.getMessage() + "\n Please fix and re-save", this.a);
/* 208 */       DbgLog.error(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private void a() {
/* 213 */     String str1 = "No devices found!";
/* 214 */     String str2 = "To configure K9LegacyBot, please: \n   1. Attach a LegacyModuleController, \n       with \n       a. MotorController in port 0, with a \n         motor in port 1 and port 2 \n       b. ServoController in port 1, with a \n         servo in port 1 and port 6 \n      c. IR seeker in port 2\n      d. Light sensor in port 3 \n   2. Press the K9LegacyBot button\n \nTo configure K9USBBot, please: \n   1. Attach a USBMotorController, with a \n       motor in port 1 and port 2 \n    2. USBServoController in port 1, with a \n      servo in port 1 and port 6 \n   3. LegacyModule, with \n      a. IR seeker in port 2\n      b. Light sensor in port 3 \n   4. Press the K9USBBot button";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 234 */     this.g.setOrangeText(str1, str2, R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */   }
/*     */   
/*     */   private void b() {
/* 238 */     String str1 = "Wrong devices found!";
/* 239 */     String str2 = "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. LegacyModuleController, with \n " + "      a. MotorController in port 0, with a \n" + "          motor in port 1 and port 2 \n " + "      b. ServoController in port 1, with a \n" + "          servo in port 1 and port 6 \n" + "       c. IR seeker in port 2\n" + "       d. Light sensor in port 3 ";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 248 */     this.g.setOrangeText(str1, str2, R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */   }
/*     */   
/*     */   private void c() {
/* 252 */     String str1 = "Wrong devices found!";
/* 253 */     String str2 = "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. USBMotorController with a \n" + "      motor in port 1 and port 2 \n " + "   2. USBServoController with a \n" + "      servo in port 1 and port 6 \n" + "   3. LegacyModuleController, with \n " + "       a. IR seeker in port 2\n" + "       b. Light sensor in port 3 ";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */     this.g.setOrangeText(str1, str2, R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */   }
/*     */   
/*     */   private void d() {
/* 266 */     String str1 = "Already configured!";
/* 267 */     String str2 = "";
/* 268 */     this.g.setOrangeText(str1, str2, R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */   }
/*     */   
/*     */   private boolean e() {
/* 272 */     int i = 1;
/* 273 */     int j = 1;
/* 274 */     int k = 1;
/*     */     
/* 276 */     for (Object localObject = this.entries.iterator(); ((Iterator)localObject).hasNext();) { Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
/* 277 */       DeviceManager.DeviceType localDeviceType = (DeviceManager.DeviceType)localEntry.getValue();
/* 278 */       if ((localDeviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) && (i != 0)) {
/* 279 */         a((SerialNumber)localEntry.getKey(), "sensors");
/* 280 */         i = 0;
/*     */       }
/* 282 */       if ((localDeviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) && (j != 0)) {
/* 283 */         a((SerialNumber)localEntry.getKey(), "motor_1", "motor_2", "wheels");
/* 284 */         j = 0;
/*     */       }
/* 286 */       if ((localDeviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) && (k != 0)) {
/* 287 */         a((SerialNumber)localEntry.getKey(), f(), "servos");
/* 288 */         k = 0;
/*     */       }
/*     */     }
/* 291 */     if ((i != 0) || (j != 0) || (k != 0)) {
/* 292 */       return false;
/*     */     }
/* 294 */     localObject = (LinearLayout)findViewById(R.id.autoconfigure_info);
/* 295 */     ((LinearLayout)localObject).removeAllViews();
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   private ArrayList<String> f() {
/* 300 */     ArrayList localArrayList = new ArrayList();
/* 301 */     localArrayList.add("servo_1");
/* 302 */     localArrayList.add("NO DEVICE ATTACHED");
/* 303 */     localArrayList.add("NO DEVICE ATTACHED");
/* 304 */     localArrayList.add("NO DEVICE ATTACHED");
/* 305 */     localArrayList.add("NO DEVICE ATTACHED");
/* 306 */     localArrayList.add("servo_6");
/* 307 */     return localArrayList;
/*     */   }
/*     */   
/*     */   private void a(SerialNumber paramSerialNumber, String paramString) {
/* 311 */     LegacyModuleControllerConfiguration localLegacyModuleControllerConfiguration = (LegacyModuleControllerConfiguration)this.e.get(paramSerialNumber);
/* 312 */     localLegacyModuleControllerConfiguration.setName(paramString);
/*     */     
/*     */ 
/* 315 */     DeviceConfiguration localDeviceConfiguration1 = a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
/*     */     
/*     */ 
/* 318 */     DeviceConfiguration localDeviceConfiguration2 = a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
/*     */     
/* 320 */     ArrayList localArrayList = new ArrayList();
/* 321 */     for (int i = 0; i < 6; i++) {
/* 322 */       if (i == 2) {
/* 323 */         localArrayList.add(localDeviceConfiguration1);
/*     */       }
/* 325 */       if (i == 3) {
/* 326 */         localArrayList.add(localDeviceConfiguration2);
/*     */       } else {
/* 328 */         DeviceConfiguration localDeviceConfiguration3 = new DeviceConfiguration(i);
/* 329 */         localArrayList.add(localDeviceConfiguration3);
/*     */       }
/*     */     }
/* 332 */     localLegacyModuleControllerConfiguration.addDevices(localArrayList);
/*     */   }
/*     */   
/*     */   private boolean g()
/*     */   {
/* 337 */     int i = 1;
/* 338 */     for (Object localObject = this.entries.iterator(); ((Iterator)localObject).hasNext();) { Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
/* 339 */       DeviceManager.DeviceType localDeviceType = (DeviceManager.DeviceType)localEntry.getValue();
/* 340 */       if ((localDeviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) && (i != 0)) {
/* 341 */         b((SerialNumber)localEntry.getKey(), "devices");
/* 342 */         i = 0;
/*     */       }
/*     */     }
/* 345 */     if (i != 0) {
/* 346 */       return false;
/*     */     }
/* 348 */     localObject = (LinearLayout)findViewById(R.id.autoconfigure_info);
/* 349 */     ((LinearLayout)localObject).removeAllViews();
/* 350 */     return true;
/*     */   }
/*     */   
/*     */   private void b(SerialNumber paramSerialNumber, String paramString)
/*     */   {
/* 355 */     LegacyModuleControllerConfiguration localLegacyModuleControllerConfiguration = (LegacyModuleControllerConfiguration)this.e.get(paramSerialNumber);
/* 356 */     localLegacyModuleControllerConfiguration.setName(paramString);
/*     */     
/* 358 */     MotorControllerConfiguration localMotorControllerConfiguration = a(ControllerConfiguration.NO_SERIAL_NUMBER, "motor_1", "motor_2", "wheels");
/* 359 */     localMotorControllerConfiguration.setPort(0);
/*     */     
/*     */ 
/* 362 */     ArrayList localArrayList1 = f();
/* 363 */     ServoControllerConfiguration localServoControllerConfiguration = a(ControllerConfiguration.NO_SERIAL_NUMBER, localArrayList1, "servos");
/* 364 */     localServoControllerConfiguration.setPort(1);
/*     */     
/*     */ 
/* 367 */     DeviceConfiguration localDeviceConfiguration1 = a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
/*     */     
/*     */ 
/* 370 */     DeviceConfiguration localDeviceConfiguration2 = a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
/*     */     
/* 372 */     ArrayList localArrayList2 = new ArrayList();
/* 373 */     localArrayList2.add(localMotorControllerConfiguration);
/* 374 */     localArrayList2.add(localServoControllerConfiguration);
/* 375 */     localArrayList2.add(localDeviceConfiguration1);
/* 376 */     localArrayList2.add(localDeviceConfiguration2);
/* 377 */     for (int i = 4; i < 6; i++) {
/* 378 */       DeviceConfiguration localDeviceConfiguration3 = new DeviceConfiguration(i);
/* 379 */       localArrayList2.add(localDeviceConfiguration3);
/*     */     }
/* 381 */     localLegacyModuleControllerConfiguration.addDevices(localArrayList2);
/*     */   }
/*     */   
/*     */   private DeviceConfiguration a(DeviceConfiguration.ConfigurationType paramConfigurationType, String paramString, int paramInt) {
/* 385 */     DeviceConfiguration localDeviceConfiguration = new DeviceConfiguration(paramInt, paramConfigurationType, paramString, true);
/* 386 */     return localDeviceConfiguration;
/*     */   }
/*     */   
/*     */   private MotorControllerConfiguration a(SerialNumber paramSerialNumber, String paramString1, String paramString2, String paramString3) {
/*     */     MotorControllerConfiguration localMotorControllerConfiguration;
/* 391 */     if (!paramSerialNumber.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
/* 392 */       localMotorControllerConfiguration = (MotorControllerConfiguration)this.e.get(paramSerialNumber);
/*     */     } else {
/* 394 */       localMotorControllerConfiguration = new MotorControllerConfiguration();
/*     */     }
/* 396 */     localMotorControllerConfiguration.setName(paramString3);
/*     */     
/* 398 */     ArrayList localArrayList = new ArrayList();
/* 399 */     MotorConfiguration localMotorConfiguration1 = new MotorConfiguration(1, paramString1, true);
/* 400 */     MotorConfiguration localMotorConfiguration2 = new MotorConfiguration(2, paramString2, true);
/* 401 */     localArrayList.add(localMotorConfiguration1);
/* 402 */     localArrayList.add(localMotorConfiguration2);
/* 403 */     localMotorControllerConfiguration.addMotors(localArrayList);
/*     */     
/* 405 */     return localMotorControllerConfiguration;
/*     */   }
/*     */   
/*     */   private ServoControllerConfiguration a(SerialNumber paramSerialNumber, ArrayList<String> paramArrayList, String paramString)
/*     */   {
/*     */     ServoControllerConfiguration localServoControllerConfiguration;
/* 411 */     if (!paramSerialNumber.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
/* 412 */       localServoControllerConfiguration = (ServoControllerConfiguration)this.e.get(paramSerialNumber);
/*     */     } else {
/* 414 */       localServoControllerConfiguration = new ServoControllerConfiguration();
/*     */     }
/* 416 */     localServoControllerConfiguration.setName(paramString);
/*     */     
/* 418 */     ArrayList localArrayList = new ArrayList();
/* 419 */     for (int i = 1; i <= 6; i++) {
/* 420 */       String str = (String)paramArrayList.get(i - 1);
/* 421 */       boolean bool = true;
/* 422 */       if (str.equals("NO DEVICE ATTACHED")) {
/* 423 */         bool = false;
/*     */       }
/* 425 */       ServoConfiguration localServoConfiguration = new ServoConfiguration(i, str, bool);
/* 426 */       localArrayList.add(localServoConfiguration);
/*     */     }
/* 428 */     localServoControllerConfiguration.addServos(localArrayList);
/* 429 */     return localServoControllerConfiguration;
/*     */   }
/*     */ }


/* Location:              /home/matt/Documents/robotics/2856/SwerveRoboticsLibrary/build/intermediates/exploded-aar/FtcCommon-release/jars/classes.jar!/com/qualcomm/ftccommon/configuration/AutoConfigureActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */