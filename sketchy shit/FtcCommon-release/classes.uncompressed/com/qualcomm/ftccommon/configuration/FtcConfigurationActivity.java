/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.AlertDialog;
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.content.Context;
/*     */ import android.content.DialogInterface;
/*     */ import android.content.DialogInterface.OnClickListener;
/*     */ import android.content.Intent;
/*     */ import android.content.SharedPreferences;
/*     */ import android.os.Bundle;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemClickListener;
/*     */ import android.widget.Button;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.ListView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.DbgLog;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.hardware.HardwareDeviceManager;
/*     */ import com.qualcomm.robotcore.exception.RobotCoreException;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager;
/*     */ import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceInfoAdapter;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FtcConfigurationActivity
/*     */   extends Activity
/*     */ {
/*     */   private Thread d;
/*  87 */   private Map<SerialNumber, ControllerConfiguration> e = new HashMap();
/*     */   private Context f;
/*     */   private DeviceManager g;
/*     */   private Button h;
/*  91 */   private String i = "No current file!";
/*  92 */   protected Map<SerialNumber, DeviceManager.DeviceType> scannedDevices = new HashMap();
/*  93 */   protected Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> scannedEntries = new HashSet();
/*     */   protected SharedPreferences preferences;
/*     */   private Utility j;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  99 */     super.onCreate(savedInstanceState);
/* 100 */     setContentView(R.layout.activity_ftc_configuration);
/*     */     
/* 102 */     RobotLog.writeLogcatToDisk(this, 1024);
/* 103 */     this.f = this;
/* 104 */     this.j = new Utility(this);
/* 105 */     this.h = ((Button)findViewById(R.id.scanButton));
/* 106 */     a();
/*     */     try
/*     */     {
/* 109 */       this.g = new HardwareDeviceManager(this.f, null);
/*     */     } catch (RobotCoreException localRobotCoreException) {
/* 111 */       this.j.complainToast("Failed to open the Device Manager", this.f);
/* 112 */       DbgLog.error("Failed to open deviceManager: " + localRobotCoreException.toString());
/* 113 */       DbgLog.logStacktrace(localRobotCoreException);
/*     */     }
/*     */     
/* 116 */     this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
/*     */   }
/*     */   
/* 119 */   DialogInterface.OnClickListener a = new DialogInterface.OnClickListener()
/*     */   {
/*     */     public void onClick(DialogInterface dialog, int button) {}
/*     */   };
/*     */   
/*     */   private void a()
/*     */   {
/* 126 */     Button localButton1 = (Button)findViewById(R.id.devices_holder).findViewById(R.id.info_btn);
/* 127 */     localButton1.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 130 */         AlertDialog.Builder localBuilder = FtcConfigurationActivity.a(FtcConfigurationActivity.this).buildBuilder("Devices", "These are the devices discovered by the Hardware Wizard. You can click on the name of each device to edit the information relating to that device. Make sure each device has a unique name. The names should match what is in the Op mode code. Scroll down to see more devices if there are any.");
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 135 */         localBuilder.setPositiveButton("Ok", FtcConfigurationActivity.this.a);
/* 136 */         AlertDialog localAlertDialog = localBuilder.create();
/* 137 */         localAlertDialog.show();
/* 138 */         TextView localTextView = (TextView)localAlertDialog.findViewById(16908299);
/* 139 */         localTextView.setTextSize(14.0F);
/*     */       }
/*     */       
/* 142 */     });
/* 143 */     Button localButton2 = (Button)findViewById(R.id.save_holder).findViewById(R.id.info_btn);
/* 144 */     localButton2.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 147 */         AlertDialog.Builder localBuilder = FtcConfigurationActivity.a(FtcConfigurationActivity.this).buildBuilder("Save Configuration", "Clicking the save button will create an xml file in: \n      /sdcard/FIRST/  \nThis file will be used to initialize the robot.");
/*     */         
/*     */ 
/* 150 */         localBuilder.setPositiveButton("Ok", FtcConfigurationActivity.this.a);
/* 151 */         AlertDialog localAlertDialog = localBuilder.create();
/* 152 */         localAlertDialog.show();
/* 153 */         TextView localTextView = (TextView)localAlertDialog.findViewById(16908299);
/* 154 */         localTextView.setTextSize(14.0F);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 161 */     super.onStart();
/*     */     
/* 163 */     this.i = this.j.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
/* 164 */     if (this.i.equalsIgnoreCase("No current file!")) {
/* 165 */       this.j.createConfigFolder();
/*     */     }
/*     */     
/* 168 */     this.j.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 169 */     e();
/*     */     
/*     */ 
/*     */ 
/* 173 */     if (!this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
/* 174 */       d();
/*     */     }
/*     */     
/* 177 */     this.h.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */ 
/*     */       public void onClick(View view)
/*     */       {
/* 182 */         FtcConfigurationActivity.a(FtcConfigurationActivity.this, new Thread(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 186 */               DbgLog.msg("Scanning USB bus");
/* 187 */               FtcConfigurationActivity.this.scannedDevices = FtcConfigurationActivity.b(FtcConfigurationActivity.this).scanForUsbDevices();
/*     */             }
/*     */             catch (RobotCoreException localRobotCoreException) {
/* 190 */               DbgLog.error("Device scan failed: " + localRobotCoreException.toString());
/*     */             }
/*     */             
/* 193 */             FtcConfigurationActivity.this.runOnUiThread(new Runnable()
/*     */             {
/*     */               public void run() {
/* 196 */                 FtcConfigurationActivity.a(FtcConfigurationActivity.this).resetCount();
/* 197 */                 FtcConfigurationActivity.c(FtcConfigurationActivity.this);
/* 198 */                 FtcConfigurationActivity.d(FtcConfigurationActivity.this);
/* 199 */                 FtcConfigurationActivity.a(FtcConfigurationActivity.this).updateHeader(FtcConfigurationActivity.e(FtcConfigurationActivity.this), R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 200 */                 FtcConfigurationActivity.this.scannedEntries = FtcConfigurationActivity.this.scannedDevices.entrySet();
/*     */                 
/* 202 */                 FtcConfigurationActivity.a(FtcConfigurationActivity.this, FtcConfigurationActivity.f(FtcConfigurationActivity.this));
/* 203 */                 FtcConfigurationActivity.g(FtcConfigurationActivity.this);
/* 204 */                 FtcConfigurationActivity.h(FtcConfigurationActivity.this);
/*     */               }
/*     */             });
/*     */           }
/* 208 */         }));
/* 209 */         FtcConfigurationActivity.i(FtcConfigurationActivity.this);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private HashMap<SerialNumber, ControllerConfiguration> b()
/*     */   {
/* 216 */     HashMap localHashMap = new HashMap();
/*     */     
/* 218 */     for (Map.Entry localEntry : this.scannedEntries) {
/* 219 */       SerialNumber localSerialNumber = (SerialNumber)localEntry.getKey();
/* 220 */       if (this.e.containsKey(localSerialNumber)) {
/* 221 */         localHashMap.put(localSerialNumber, this.e.get(localSerialNumber));
/*     */       } else {
/* 223 */         switch (3.a[((DeviceManager.DeviceType)localEntry.getValue()).ordinal()]) {
/*     */         case 1: 
/* 225 */           localHashMap.put(localSerialNumber, this.j.buildMotorController(localSerialNumber));
/* 226 */           break;
/*     */         case 2: 
/* 228 */           localHashMap.put(localSerialNumber, this.j.buildServoController(localSerialNumber));
/* 229 */           break;
/*     */         case 3: 
/* 231 */           localHashMap.put(localSerialNumber, this.j.buildLegacyModule(localSerialNumber));
/* 232 */           break;
/*     */         case 4: 
/* 234 */           localHashMap.put(localSerialNumber, this.j.buildDeviceInterfaceModule(localSerialNumber));
/*     */         }
/*     */         
/*     */       }
/*     */     }
/* 239 */     return localHashMap;
/*     */   }
/*     */   
/*     */   private void c() {
/* 243 */     if (this.i.toLowerCase().contains("Unsaved".toLowerCase()))
/*     */     {
/* 245 */       String str = "If you scan, your current unsaved changes will be lost.";
/* 246 */       EditText localEditText = new EditText(this.f);
/* 247 */       localEditText.setEnabled(false);
/* 248 */       localEditText.setText("");
/* 249 */       AlertDialog.Builder localBuilder = this.j.buildBuilder("Unsaved changes", str);
/* 250 */       localBuilder.setView(localEditText);
/* 251 */       DialogInterface.OnClickListener local7 = new DialogInterface.OnClickListener() {
/*     */         public void onClick(DialogInterface dialog, int button) {
/* 253 */           FtcConfigurationActivity.j(FtcConfigurationActivity.this).start();
/*     */         }
/*     */         
/* 256 */       };
/* 257 */       localBuilder.setPositiveButton("Ok", local7);
/* 258 */       localBuilder.setNegativeButton("Cancel", this.c);
/* 259 */       localBuilder.show();
/*     */     } else {
/* 261 */       this.d.start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void d()
/*     */   {
/* 271 */     ReadXMLFileHandler localReadXMLFileHandler = new ReadXMLFileHandler(this.f);
/*     */     
/* 273 */     if (this.i.equalsIgnoreCase("No current file!"))
/*     */     {
/* 275 */       return;
/*     */     }
/*     */     
/* 278 */     FileInputStream localFileInputStream = null;
/*     */     try {
/* 280 */       localFileInputStream = new FileInputStream(Utility.CONFIG_FILES_DIR + this.i + ".xml");
/* 281 */       ArrayList localArrayList = (ArrayList)localReadXMLFileHandler.parse(localFileInputStream);
/* 282 */       a(localArrayList);
/* 283 */       h();
/* 284 */       f();
/*     */     } catch (RobotCoreException localRobotCoreException) {
/* 286 */       RobotLog.e("Error parsing XML file");
/* 287 */       RobotLog.logStacktrace(localRobotCoreException);
/* 288 */       this.j.complainToast("Error parsing XML file: " + this.i, this.f);
/* 289 */       return;
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 291 */       DbgLog.error("File was not found: " + this.i);
/* 292 */       DbgLog.logStacktrace(localFileNotFoundException);
/* 293 */       this.j.complainToast("That file was not found: " + this.i, this.f);
/* 294 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   private void e() { Object localObject;
/* 299 */     if (this.e.size() == 0) {
/* 300 */       localObject = "Scan to find devices.";
/* 301 */       String str = "In order to find devices: \n    1. Attach a robot \n    2. Press the 'Scan' button";
/*     */       
/*     */ 
/* 304 */       this.j.setOrangeText((String)localObject, str, R.id.empty_devicelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/* 305 */       g();
/*     */     } else {
/* 307 */       localObject = (LinearLayout)findViewById(R.id.empty_devicelist);
/* 308 */       ((LinearLayout)localObject).removeAllViews();
/* 309 */       ((LinearLayout)localObject).setVisibility(8);
/*     */     }
/*     */   }
/*     */   
/*     */   private void f() {
/*     */     Object localObject;
/* 315 */     if (this.e.size() == 0) {
/* 316 */       localObject = "No devices found!";
/* 317 */       String str = "In order to find devices: \n    1. Attach a robot \n    2. Press the 'Scan' button";
/*     */       
/*     */ 
/* 320 */       this.j.setOrangeText((String)localObject, str, R.id.empty_devicelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/* 321 */       g();
/*     */     } else {
/* 323 */       localObject = (LinearLayout)findViewById(R.id.empty_devicelist);
/* 324 */       ((LinearLayout)localObject).removeAllViews();
/* 325 */       ((LinearLayout)localObject).setVisibility(8);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(String paramString) {
/* 330 */     String str1 = "Found " + paramString;
/* 331 */     String str2 = "Please fix and re-save.";
/* 332 */     this.j.setOrangeText(str1, str2, R.id.warning_layout, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */   }
/*     */   
/*     */   private void g() {
/* 336 */     LinearLayout localLinearLayout = (LinearLayout)findViewById(R.id.warning_layout);
/* 337 */     localLinearLayout.removeAllViews();
/* 338 */     localLinearLayout.setVisibility(8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void h()
/*     */   {
/* 348 */     ListView localListView = (ListView)findViewById(R.id.controllersList);
/* 349 */     DeviceInfoAdapter localDeviceInfoAdapter = new DeviceInfoAdapter(this, 17367044, this.e);
/* 350 */     localListView.setAdapter(localDeviceInfoAdapter);
/*     */     
/* 352 */     localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
/*     */     {
/*     */       public void onItemClick(AdapterView<?> adapterView, View v, int pos, long arg3)
/*     */       {
/* 356 */         ControllerConfiguration localControllerConfiguration = (ControllerConfiguration)adapterView.getItemAtPosition(pos);
/* 357 */         DeviceConfiguration.ConfigurationType localConfigurationType = localControllerConfiguration.getType();
/* 358 */         int i; Intent localIntent; if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER)) {
/* 359 */           i = 1;
/* 360 */           localIntent = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), EditMotorControllerActivity.class);
/* 361 */           localIntent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", localControllerConfiguration);
/* 362 */           localIntent.putExtra("requestCode", i);
/* 363 */           FtcConfigurationActivity.this.setResult(-1, localIntent);
/* 364 */           FtcConfigurationActivity.this.startActivityForResult(localIntent, i);
/*     */         }
/* 366 */         if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER)) {
/* 367 */           i = 2;
/* 368 */           localIntent = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), EditServoControllerActivity.class);
/* 369 */           localIntent.putExtra("Edit Servo ControllerConfiguration Activity", localControllerConfiguration);
/* 370 */           localIntent.putExtra("requestCode", i);
/* 371 */           FtcConfigurationActivity.this.setResult(-1, localIntent);
/* 372 */           FtcConfigurationActivity.this.startActivityForResult(localIntent, i);
/*     */         }
/* 374 */         if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER)) {
/* 375 */           i = 3;
/* 376 */           localIntent = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), EditLegacyModuleControllerActivity.class);
/* 377 */           localIntent.putExtra("EDIT_LEGACY_CONFIG", localControllerConfiguration);
/* 378 */           localIntent.putExtra("requestCode", i);
/* 379 */           FtcConfigurationActivity.this.setResult(-1, localIntent);
/* 380 */           FtcConfigurationActivity.this.startActivityForResult(localIntent, i);
/*     */         }
/* 382 */         if (localConfigurationType.equals(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE)) {
/* 383 */           i = 4;
/* 384 */           localIntent = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), EditDeviceInterfaceModuleActivity.class);
/* 385 */           localIntent.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", localControllerConfiguration);
/* 386 */           localIntent.putExtra("requestCode", i);
/* 387 */           FtcConfigurationActivity.this.setResult(-1, localIntent);
/* 388 */           FtcConfigurationActivity.this.startActivityForResult(localIntent, i);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onActivityResult(int requestCode, int resultCode, Intent data)
/*     */   {
/* 399 */     if (resultCode == 0) {
/* 400 */       return;
/*     */     }
/* 402 */     Serializable localSerializable = null;
/* 403 */     if (requestCode == 1) {
/* 404 */       localSerializable = data.getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
/* 405 */     } else if (requestCode == 2) {
/* 406 */       localSerializable = data.getSerializableExtra("Edit Servo ControllerConfiguration Activity");
/* 407 */     } else if (requestCode == 3) {
/* 408 */       localSerializable = data.getSerializableExtra("EDIT_LEGACY_CONFIG");
/* 409 */     } else if (requestCode == 4) {
/* 410 */       localSerializable = data.getSerializableExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG");
/*     */     }
/* 412 */     if (localSerializable != null) {
/* 413 */       ControllerConfiguration localControllerConfiguration = (ControllerConfiguration)localSerializable;
/* 414 */       this.scannedDevices.put(localControllerConfiguration.getSerialNumber(), localControllerConfiguration.configTypeToDeviceType(localControllerConfiguration.getType()));
/* 415 */       this.e.put(localControllerConfiguration.getSerialNumber(), localControllerConfiguration);
/* 416 */       h();
/*     */       
/* 418 */       i();
/*     */     }
/*     */     else {
/* 421 */       DbgLog.error("Received Result with an incorrect request code: " + String.valueOf(requestCode));
/*     */     }
/*     */   }
/*     */   
/*     */   private void i() {
/* 426 */     if (!this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
/* 427 */       String str = "Unsaved " + this.i;
/* 428 */       this.j.saveToPreferences(str, R.string.pref_hardware_config_filename);
/* 429 */       this.i = str;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onDestroy()
/*     */   {
/* 435 */     super.onDestroy();
/* 436 */     this.j.resetCount();
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
/*     */   public void onBackPressed()
/*     */   {
/* 449 */     if (this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
/* 450 */       boolean bool = this.j.writeXML(this.e);
/* 451 */       if (bool) {
/* 452 */         return;
/*     */       }
/* 454 */       String str1 = "Please save your file before exiting the Hardware Wizard! \n If you click 'Cancel' your changes will be lost.";
/*     */       
/* 456 */       final EditText localEditText = new EditText(this);
/* 457 */       String str2 = this.j.prepareFilename(this.i);
/*     */       
/* 459 */       localEditText.setText(str2);
/* 460 */       AlertDialog.Builder localBuilder = this.j.buildBuilder("Unsaved changes", str1);
/* 461 */       localBuilder.setView(localEditText);
/* 462 */       DialogInterface.OnClickListener local9 = new DialogInterface.OnClickListener() {
/*     */         public void onClick(DialogInterface dialog, int button) {
/* 464 */           String str = localEditText.getText().toString() + ".xml";
/* 465 */           str = str.trim();
/* 466 */           if (str.equals(".xml")) {
/* 467 */             FtcConfigurationActivity.a(FtcConfigurationActivity.this).complainToast("File not saved: Please entire a filename.", FtcConfigurationActivity.k(FtcConfigurationActivity.this));
/* 468 */             return;
/*     */           }
/*     */           try {
/* 471 */             FtcConfigurationActivity.a(FtcConfigurationActivity.this).writeToFile(str);
/*     */           } catch (RobotCoreException localRobotCoreException) {
/* 473 */             FtcConfigurationActivity.a(FtcConfigurationActivity.this).complainToast(localRobotCoreException.getMessage(), FtcConfigurationActivity.k(FtcConfigurationActivity.this));
/* 474 */             DbgLog.error(localRobotCoreException.getMessage());
/* 475 */             return;
/*     */           } catch (IOException localIOException) {
/* 477 */             FtcConfigurationActivity.a(FtcConfigurationActivity.this, localIOException.getMessage());
/* 478 */             DbgLog.error(localIOException.getMessage());
/* 479 */             return;
/*     */           }
/* 481 */           FtcConfigurationActivity.c(FtcConfigurationActivity.this);
/* 482 */           FtcConfigurationActivity.a(FtcConfigurationActivity.this).saveToPreferences(localEditText.getText().toString(), R.string.pref_hardware_config_filename);
/* 483 */           FtcConfigurationActivity.b(FtcConfigurationActivity.this, localEditText.getText().toString());
/* 484 */           FtcConfigurationActivity.a(FtcConfigurationActivity.this).updateHeader("robot_config", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 485 */           FtcConfigurationActivity.a(FtcConfigurationActivity.this).confirmSave();
/* 486 */           FtcConfigurationActivity.this.finish();
/*     */         }
/*     */         
/* 489 */       };
/* 490 */       localBuilder.setPositiveButton("Ok", local9);
/* 491 */       localBuilder.setNegativeButton("Cancel", this.b);
/* 492 */       localBuilder.show();
/*     */     } else {
/* 494 */       super.onBackPressed();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 502 */   DialogInterface.OnClickListener b = new DialogInterface.OnClickListener()
/*     */   {
/*     */     public void onClick(DialogInterface dialog, int button) {
/* 505 */       FtcConfigurationActivity.a(FtcConfigurationActivity.this).saveToPreferences(FtcConfigurationActivity.e(FtcConfigurationActivity.this).substring(7).trim(), R.string.pref_hardware_config_filename);
/* 506 */       FtcConfigurationActivity.this.finish();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveConfiguration(View v)
/*     */   {
/* 518 */     boolean bool = this.j.writeXML(this.e);
/* 519 */     if (bool) {
/* 520 */       return;
/*     */     }
/*     */     
/* 523 */     String str1 = "Please enter the file name";
/* 524 */     final EditText localEditText = new EditText(this);
/* 525 */     String str2 = this.j.prepareFilename(this.i);
/*     */     
/* 527 */     localEditText.setText(str2);
/* 528 */     AlertDialog.Builder localBuilder = this.j.buildBuilder("Enter file name", str1);
/* 529 */     localBuilder.setView(localEditText);
/* 530 */     DialogInterface.OnClickListener local11 = new DialogInterface.OnClickListener() {
/*     */       public void onClick(DialogInterface dialog, int button) {
/* 532 */         String str = localEditText.getText().toString() + ".xml";
/* 533 */         str = str.trim();
/* 534 */         if (str.equals(".xml")) {
/* 535 */           FtcConfigurationActivity.a(FtcConfigurationActivity.this).complainToast("File not saved: Please entire a filename.", FtcConfigurationActivity.k(FtcConfigurationActivity.this));
/* 536 */           return;
/*     */         }
/*     */         try {
/* 539 */           FtcConfigurationActivity.a(FtcConfigurationActivity.this).writeToFile(str);
/*     */         } catch (RobotCoreException localRobotCoreException) {
/* 541 */           FtcConfigurationActivity.a(FtcConfigurationActivity.this).complainToast(localRobotCoreException.getMessage(), FtcConfigurationActivity.k(FtcConfigurationActivity.this));
/* 542 */           DbgLog.error(localRobotCoreException.getMessage());
/* 543 */           return;
/*     */         }
/*     */         catch (IOException localIOException) {
/* 546 */           FtcConfigurationActivity.a(FtcConfigurationActivity.this, localIOException.getMessage());
/* 547 */           DbgLog.error(localIOException.getMessage());
/* 548 */           return;
/*     */         }
/* 550 */         FtcConfigurationActivity.c(FtcConfigurationActivity.this);
/* 551 */         FtcConfigurationActivity.a(FtcConfigurationActivity.this).saveToPreferences(localEditText.getText().toString(), R.string.pref_hardware_config_filename);
/* 552 */         FtcConfigurationActivity.b(FtcConfigurationActivity.this, localEditText.getText().toString());
/* 553 */         FtcConfigurationActivity.a(FtcConfigurationActivity.this).updateHeader("robot_config", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 554 */         FtcConfigurationActivity.a(FtcConfigurationActivity.this).confirmSave();
/*     */       }
/*     */       
/* 557 */     };
/* 558 */     localBuilder.setPositiveButton("Ok", local11);
/* 559 */     localBuilder.setNegativeButton("Cancel", this.c);
/* 560 */     localBuilder.show();
/*     */   }
/*     */   
/* 563 */   DialogInterface.OnClickListener c = new DialogInterface.OnClickListener()
/*     */   {
/*     */     public void onClick(DialogInterface dialog, int button) {}
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(ArrayList<ControllerConfiguration> paramArrayList)
/*     */   {
/* 576 */     this.e = new HashMap();
/*     */     
/* 578 */     for (ControllerConfiguration localControllerConfiguration : paramArrayList) {
/* 579 */       this.e.put(localControllerConfiguration.getSerialNumber(), localControllerConfiguration);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\FtcConfigurationActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */