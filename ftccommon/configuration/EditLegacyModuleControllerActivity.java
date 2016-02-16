/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.text.Editable;
/*     */ import android.text.TextWatcher;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.ViewParent;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemSelectedListener;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.Button;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.Spinner;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.ftccommon.R.xml;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditLegacyModuleControllerActivity
/*     */   extends Activity
/*     */ {
/*  68 */   private static boolean a = false;
/*     */   
/*     */   private Utility b;
/*     */   
/*     */   private String c;
/*     */   
/*     */   private Context d;
/*     */   
/*     */   public static final String EDIT_LEGACY_CONFIG = "EDIT_LEGACY_CONFIG";
/*     */   
/*     */   public static final int EDIT_MOTOR_CONTROLLER_REQUEST_CODE = 101;
/*     */   
/*     */   public static final int EDIT_SERVO_CONTROLLER_REQUEST_CODE = 102;
/*     */   
/*     */   public static final int EDIT_MATRIX_CONTROLLER_REQUEST_CODE = 103;
/*     */   
/*     */   private LegacyModuleControllerConfiguration e;
/*     */   
/*     */   private EditText f;
/*     */   private ArrayList<DeviceConfiguration> g;
/*     */   private View h;
/*     */   private View i;
/*     */   private View j;
/*     */   private View k;
/*     */   private View l;
/*     */   private View m;
/*     */   private AdapterView.OnItemSelectedListener n;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  98 */     super.onCreate(savedInstanceState);
/*  99 */     setContentView(R.layout.legacy);
/*     */     
/* 101 */     LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.linearLayout0);
/* 102 */     this.h = getLayoutInflater().inflate(R.layout.simple_device, localLinearLayout1, true);
/* 103 */     TextView localTextView1 = (TextView)this.h.findViewById(R.id.portNumber);
/* 104 */     localTextView1.setText("0");
/*     */     
/* 106 */     LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.linearLayout1);
/* 107 */     this.i = getLayoutInflater().inflate(R.layout.simple_device, localLinearLayout2, true);
/* 108 */     TextView localTextView2 = (TextView)this.i.findViewById(R.id.portNumber);
/* 109 */     localTextView2.setText("1");
/*     */     
/* 111 */     LinearLayout localLinearLayout3 = (LinearLayout)findViewById(R.id.linearLayout2);
/* 112 */     this.j = getLayoutInflater().inflate(R.layout.simple_device, localLinearLayout3, true);
/* 113 */     TextView localTextView3 = (TextView)this.j.findViewById(R.id.portNumber);
/* 114 */     localTextView3.setText("2");
/*     */     
/* 116 */     LinearLayout localLinearLayout4 = (LinearLayout)findViewById(R.id.linearLayout3);
/* 117 */     this.k = getLayoutInflater().inflate(R.layout.simple_device, localLinearLayout4, true);
/* 118 */     TextView localTextView4 = (TextView)this.k.findViewById(R.id.portNumber);
/* 119 */     localTextView4.setText("3");
/*     */     
/* 121 */     LinearLayout localLinearLayout5 = (LinearLayout)findViewById(R.id.linearLayout4);
/* 122 */     this.l = getLayoutInflater().inflate(R.layout.simple_device, localLinearLayout5, true);
/* 123 */     TextView localTextView5 = (TextView)this.l.findViewById(R.id.portNumber);
/* 124 */     localTextView5.setText("4");
/*     */     
/* 126 */     LinearLayout localLinearLayout6 = (LinearLayout)findViewById(R.id.linearLayout5);
/* 127 */     this.m = getLayoutInflater().inflate(R.layout.simple_device, localLinearLayout6, true);
/* 128 */     TextView localTextView6 = (TextView)this.m.findViewById(R.id.portNumber);
/* 129 */     localTextView6.setText("5");
/*     */     
/* 131 */     this.d = this;
/* 132 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/* 133 */     this.b = new Utility(this);
/* 134 */     RobotLog.writeLogcatToDisk(this, 1024);
/* 135 */     this.f = ((EditText)findViewById(R.id.device_interface_module_name));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 141 */     super.onStart();
/*     */     
/* 143 */     this.b.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 144 */     this.c = this.b.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
/*     */     
/* 146 */     Intent localIntent = getIntent();
/* 147 */     Serializable localSerializable = localIntent.getSerializableExtra("EDIT_LEGACY_CONFIG");
/* 148 */     if (localSerializable != null) {
/* 149 */       this.e = ((LegacyModuleControllerConfiguration)localSerializable);
/* 150 */       this.g = ((ArrayList)this.e.getDevices());
/*     */       
/* 152 */       this.f.setText(this.e.getName());
/* 153 */       this.f.addTextChangedListener(new a(null));
/*     */       
/* 155 */       TextView localTextView = (TextView)findViewById(R.id.legacy_serialNumber);
/* 156 */       localTextView.setText(this.e.getSerialNumber().toString());
/*     */       
/* 158 */       for (int i1 = 0; i1 < this.g.size(); i1++) {
/* 159 */         DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.g.get(i1);
/* 160 */         if (a) RobotLog.e("[onStart] module name: " + localDeviceConfiguration.getName() + ", port: " + localDeviceConfiguration.getPort() + ", type: " + localDeviceConfiguration.getType());
/* 161 */         a(a(i1), localDeviceConfiguration);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onActivityResult(int requestCode, int resultCode, Intent data)
/*     */   {
/* 169 */     Serializable localSerializable = null;
/* 170 */     if (resultCode == -1) {
/* 171 */       if (requestCode == 101) {
/* 172 */         localSerializable = data.getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
/* 173 */       } else if (requestCode == 102) {
/* 174 */         localSerializable = data.getSerializableExtra("Edit Servo ControllerConfiguration Activity");
/* 175 */       } else if (requestCode == 103) {
/* 176 */         localSerializable = data.getSerializableExtra("Edit Matrix ControllerConfiguration Activity");
/*     */       }
/*     */       
/* 179 */       if (localSerializable != null) {
/* 180 */         ControllerConfiguration localControllerConfiguration = (ControllerConfiguration)localSerializable;
/* 181 */         b(localControllerConfiguration);
/* 182 */         a(a(localControllerConfiguration.getPort()), (DeviceConfiguration)this.g.get(localControllerConfiguration.getPort()));
/* 183 */         String str = this.c;
/*     */         
/* 185 */         if (!str.toLowerCase().contains("Unsaved".toLowerCase())) {
/* 186 */           str = "Unsaved " + this.c;
/* 187 */           this.b.saveToPreferences(str, R.string.pref_hardware_config_filename);
/* 188 */           this.c = str;
/*     */         }
/* 190 */         this.b.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void saveLegacyController(View v) {
/* 196 */     a();
/*     */   }
/*     */   
/*     */   private void a() {
/* 200 */     Intent localIntent = new Intent();
/*     */     
/* 202 */     this.e.setName(this.f.getText().toString());
/*     */     
/* 204 */     localIntent.putExtra("EDIT_LEGACY_CONFIG", this.e);
/* 205 */     setResult(-1, localIntent);
/* 206 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelLegacyController(View v) {
/* 210 */     setResult(0, new Intent());
/* 211 */     finish();
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
/*     */   private void a(View paramView, DeviceConfiguration paramDeviceConfiguration)
/*     */   {
/* 225 */     Spinner localSpinner = (Spinner)paramView.findViewById(R.id.choiceSpinner_legacyModule);
/* 226 */     ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
/*     */     
/* 228 */     int i1 = localArrayAdapter.getPosition(paramDeviceConfiguration.getType().toString());
/* 229 */     localSpinner.setSelection(i1);
/* 230 */     localSpinner.setOnItemSelectedListener(this.n);
/*     */     
/* 232 */     String str = paramDeviceConfiguration.getName();
/* 233 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_name);
/*     */     
/* 235 */     TextView localTextView = (TextView)paramView.findViewById(R.id.portNumber);
/* 236 */     int i2 = Integer.parseInt(localTextView.getText().toString());
/* 237 */     localEditText.addTextChangedListener(new a(a(i2), null));
/* 238 */     localEditText.setText(str);
/*     */     
/* 240 */     if (a) { RobotLog.e("[populatePort] name: " + str + ", port: " + i2 + ", type: " + paramDeviceConfiguration.getType());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(LinearLayout paramLinearLayout)
/*     */   {
/* 249 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.portNumber);
/* 250 */     int i1 = Integer.parseInt(localTextView.getText().toString());
/* 251 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_name);
/*     */     
/* 253 */     localEditText.setEnabled(false);
/* 254 */     localEditText.setText("NO DEVICE ATTACHED");
/*     */     
/* 256 */     DeviceConfiguration localDeviceConfiguration = new DeviceConfiguration(DeviceConfiguration.ConfigurationType.NOTHING);
/* 257 */     localDeviceConfiguration.setPort(i1);
/* 258 */     b(localDeviceConfiguration);
/*     */     
/* 260 */     a(i1, 8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(LinearLayout paramLinearLayout, String paramString)
/*     */   {
/* 269 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.portNumber);
/* 270 */     int i1 = Integer.parseInt(localTextView.getText().toString());
/* 271 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_name);
/* 272 */     DeviceConfiguration localDeviceConfiguration1 = (DeviceConfiguration)this.g.get(i1);
/*     */     
/* 274 */     localEditText.setEnabled(true);
/* 275 */     a(localEditText, localDeviceConfiguration1);
/*     */     
/* 277 */     DeviceConfiguration.ConfigurationType localConfigurationType = localDeviceConfiguration1.typeFromString(paramString);
/* 278 */     if ((localConfigurationType == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER) || (localConfigurationType == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER) || (localConfigurationType == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER))
/*     */     {
/*     */ 
/* 281 */       a(i1, paramString);
/* 282 */       a(i1, 0);
/*     */     } else {
/* 284 */       localDeviceConfiguration1.setType(localConfigurationType);
/* 285 */       if (localConfigurationType == DeviceConfiguration.ConfigurationType.NOTHING) {
/* 286 */         localDeviceConfiguration1.setEnabled(false);
/*     */       } else {
/* 288 */         localDeviceConfiguration1.setEnabled(true);
/*     */       }
/* 290 */       a(i1, 8);
/*     */     }
/*     */     
/* 293 */     if (a) {
/* 294 */       DeviceConfiguration localDeviceConfiguration2 = (DeviceConfiguration)this.g.get(i1);
/* 295 */       RobotLog.e("[changeDevice] modules.get(port) name: " + localDeviceConfiguration2.getName() + ", port: " + localDeviceConfiguration2.getPort() + ", type: " + localDeviceConfiguration2.getType());
/*     */     }
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
/*     */   private void a(int paramInt, String paramString)
/*     */   {
/* 309 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.g.get(paramInt);
/*     */     
/* 311 */     String str = localDeviceConfiguration.getName();
/* 312 */     ArrayList localArrayList1 = new ArrayList();
/* 313 */     SerialNumber localSerialNumber = ControllerConfiguration.NO_SERIAL_NUMBER;
/*     */     
/* 315 */     DeviceConfiguration.ConfigurationType localConfigurationType = localDeviceConfiguration.getType();
/* 316 */     if (!localConfigurationType.toString().equalsIgnoreCase(paramString)) {
/* 317 */       Object localObject = new ControllerConfiguration("dummy module", localArrayList1, localSerialNumber, DeviceConfiguration.ConfigurationType.NOTHING);
/*     */       int i1;
/* 319 */       if (paramString.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
/* 320 */         for (i1 = 1; i1 <= 2; i1++) {
/* 321 */           localArrayList1.add(new MotorConfiguration(i1));
/*     */         }
/* 323 */         localObject = new MotorControllerConfiguration(str, localArrayList1, localSerialNumber);
/* 324 */         ((ControllerConfiguration)localObject).setPort(paramInt);
/*     */       }
/* 326 */       else if (paramString.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
/* 327 */         for (i1 = 1; i1 <= 6; i1++) {
/* 328 */           localArrayList1.add(new ServoConfiguration(i1));
/*     */         }
/* 330 */         localObject = new ServoControllerConfiguration(str, localArrayList1, localSerialNumber);
/* 331 */         ((ControllerConfiguration)localObject).setPort(paramInt);
/*     */       }
/* 333 */       else if (paramString.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
/* 334 */         ArrayList localArrayList2 = new ArrayList();
/* 335 */         for (int i2 = 1; i2 <= 4; i2++) {
/* 336 */           localArrayList2.add(new MotorConfiguration(i2));
/*     */         }
/*     */         
/* 339 */         ArrayList localArrayList3 = new ArrayList();
/* 340 */         for (int i3 = 1; i3 <= 4; i3++) {
/* 341 */           localArrayList3.add(new ServoConfiguration(i3));
/*     */         }
/* 343 */         localObject = new MatrixControllerConfiguration(str, localArrayList2, localArrayList3, localSerialNumber);
/* 344 */         ((ControllerConfiguration)localObject).setPort(paramInt);
/*     */       }
/* 346 */       ((ControllerConfiguration)localObject).setEnabled(true);
/* 347 */       b((DeviceConfiguration)localObject);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void editController_portALL(View v)
/*     */   {
/* 360 */     LinearLayout localLinearLayout = (LinearLayout)v.getParent().getParent();
/* 361 */     TextView localTextView = (TextView)localLinearLayout.findViewById(R.id.portNumber);
/* 362 */     int i1 = Integer.parseInt(localTextView.getText().toString());
/* 363 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.g.get(i1);
/*     */     
/* 365 */     if (!c(localDeviceConfiguration)) {
/* 366 */       Spinner localSpinner = (Spinner)localLinearLayout.findViewById(R.id.choiceSpinner_legacyModule);
/* 367 */       String str = localSpinner.getSelectedItem().toString();
/* 368 */       a(i1, str);
/*     */     }
/* 370 */     a(localDeviceConfiguration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(DeviceConfiguration paramDeviceConfiguration)
/*     */   {
/* 379 */     LinearLayout localLinearLayout = (LinearLayout)a(paramDeviceConfiguration.getPort());
/* 380 */     EditText localEditText = (EditText)localLinearLayout.findViewById(R.id.editTextResult_name);
/* 381 */     paramDeviceConfiguration.setName(localEditText.getText().toString());
/*     */     int i1;
/* 383 */     Intent localIntent; if (paramDeviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER) {
/* 384 */       i1 = 101;
/* 385 */       localIntent = new Intent(this.d, EditMotorControllerActivity.class);
/* 386 */       localIntent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", paramDeviceConfiguration);
/* 387 */       localIntent.putExtra("requestCode", i1);
/* 388 */       setResult(-1, localIntent);
/* 389 */       startActivityForResult(localIntent, i1);
/*     */     }
/* 391 */     else if (paramDeviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER) {
/* 392 */       i1 = 102;
/* 393 */       localIntent = new Intent(this.d, EditServoControllerActivity.class);
/* 394 */       localIntent.putExtra("Edit Servo ControllerConfiguration Activity", paramDeviceConfiguration);
/* 395 */       setResult(-1, localIntent);
/* 396 */       startActivityForResult(localIntent, i1);
/*     */     }
/* 398 */     else if (paramDeviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
/* 399 */       i1 = 103;
/* 400 */       localIntent = new Intent(this.d, EditMatrixControllerActivity.class);
/* 401 */       localIntent.putExtra("Edit Matrix ControllerConfiguration Activity", paramDeviceConfiguration);
/* 402 */       setResult(-1, localIntent);
/* 403 */       startActivityForResult(localIntent, i1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void b(DeviceConfiguration paramDeviceConfiguration) {
/* 408 */     int i1 = paramDeviceConfiguration.getPort();
/* 409 */     this.g.set(i1, paramDeviceConfiguration);
/*     */   }
/*     */   
/*     */ 
/*     */   private View a(int paramInt)
/*     */   {
/* 415 */     switch (paramInt) {
/*     */     case 0: 
/* 417 */       return this.h;
/*     */     case 1: 
/* 419 */       return this.i;
/*     */     case 2: 
/* 421 */       return this.j;
/*     */     case 3: 
/* 423 */       return this.k;
/*     */     case 4: 
/* 425 */       return this.l;
/*     */     case 5: 
/* 427 */       return this.m;
/*     */     }
/* 429 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(int paramInt1, int paramInt2)
/*     */   {
/* 439 */     View localView = a(paramInt1);
/* 440 */     Button localButton = (Button)localView.findViewById(R.id.edit_controller_btn);
/* 441 */     localButton.setVisibility(paramInt2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean c(DeviceConfiguration paramDeviceConfiguration)
/*     */   {
/* 450 */     return (paramDeviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER) || (paramDeviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(EditText paramEditText, DeviceConfiguration paramDeviceConfiguration)
/*     */   {
/* 461 */     if (paramEditText.getText().toString().equalsIgnoreCase("NO DEVICE ATTACHED")) {
/* 462 */       paramEditText.setText("");
/* 463 */       paramDeviceConfiguration.setName("");
/*     */     } else {
/* 465 */       paramEditText.setText(paramDeviceConfiguration.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public EditLegacyModuleControllerActivity()
/*     */   {
/*  80 */     this.g = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 474 */     this.n = new AdapterView.OnItemSelectedListener()
/*     */     {
/*     */       public void onItemSelected(AdapterView<?> parent, View view, int pos, long l)
/*     */       {
/* 478 */         String str = parent.getItemAtPosition(pos).toString();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 484 */         LinearLayout localLinearLayout = (LinearLayout)view.getParent().getParent().getParent();
/*     */         
/* 486 */         if (str.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
/* 487 */           EditLegacyModuleControllerActivity.a(EditLegacyModuleControllerActivity.this, localLinearLayout);
/*     */         } else {
/* 489 */           EditLegacyModuleControllerActivity.a(EditLegacyModuleControllerActivity.this, localLinearLayout, str);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       public void onNothingSelected(AdapterView<?> adapterView) {}
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class a
/*     */     implements TextWatcher
/*     */   {
/*     */     private int b;
/*     */     
/*     */ 
/* 508 */     private boolean c = false;
/*     */     
/*     */ 
/* 511 */     private a() { this.c = true; }
/*     */     
/*     */     private a(View paramView) {
/* 514 */       TextView localTextView = (TextView)paramView.findViewById(R.id.portNumber);
/* 515 */       this.b = Integer.parseInt(localTextView.getText().toString());
/*     */     }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/* 522 */     public void afterTextChanged(Editable editable) { String str = editable.toString();
/* 523 */       if (this.c) {
/* 524 */         EditLegacyModuleControllerActivity.a(EditLegacyModuleControllerActivity.this).setName(str);
/*     */       } else {
/* 526 */         DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)EditLegacyModuleControllerActivity.b(EditLegacyModuleControllerActivity.this).get(this.b);
/* 527 */         localDeviceConfiguration.setName(str);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/matt/Documents/robotics/2856/SwerveRoboticsLibrary/build/intermediates/exploded-aar/FtcCommon-release/jars/classes.jar!/com/qualcomm/ftccommon/configuration/EditLegacyModuleControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */