/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.res.Resources;
/*     */ import android.os.Bundle;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.text.Editable;
/*     */ import android.text.TextWatcher;
/*     */ import android.view.View;
/*     */ import android.widget.AdapterView;
/*     */ import android.widget.AdapterView.OnItemClickListener;
/*     */ import android.widget.ArrayAdapter;
/*     */ import android.widget.EditText;
/*     */ import android.widget.ListView;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.array;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.ftccommon.R.xml;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import com.qualcomm.robotcore.util.SerialNumber;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditDeviceInterfaceModuleActivity
/*     */   extends Activity
/*     */ {
/*     */   private Utility a;
/*     */   private String b;
/*     */   private Context c;
/*     */   public static final String EDIT_DEVICE_INTERFACE_MODULE_CONFIG = "EDIT_DEVICE_INTERFACE_MODULE_CONFIG";
/*     */   public static final int EDIT_PWM_PORT_REQUEST_CODE = 201;
/*     */   public static final int EDIT_I2C_PORT_REQUEST_CODE = 202;
/*     */   public static final int EDIT_ANALOG_INPUT_REQUEST_CODE = 203;
/*     */   public static final int EDIT_DIGITAL_REQUEST_CODE = 204;
/*     */   public static final int EDIT_ANALOG_OUTPUT_REQUEST_CODE = 205;
/*     */   private DeviceInterfaceModuleConfiguration d;
/*     */   private EditText e;
/*     */   private ArrayList<DeviceConfiguration> f;
/*     */   private AdapterView.OnItemClickListener g;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  83 */     super.onCreate(savedInstanceState);
/*  84 */     setContentView(R.layout.device_interface_module);
/*     */     
/*  86 */     String[] arrayOfString = getResources().getStringArray(R.array.device_interface_module_options_array);
/*  87 */     ListView localListView = (ListView)findViewById(R.id.listView_devices);
/*  88 */     localListView.setAdapter(new ArrayAdapter(this, 17367043, arrayOfString));
/*     */     
/*  90 */     localListView.setOnItemClickListener(this.g);
/*     */     
/*  92 */     this.c = this;
/*  93 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  94 */     this.a = new Utility(this);
/*  95 */     RobotLog.writeLogcatToDisk(this, 1024);
/*  96 */     this.e = ((EditText)findViewById(R.id.device_interface_module_name));
/*  97 */     this.e.addTextChangedListener(new a(null));
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/* 102 */     super.onStart();
/*     */     
/* 104 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 105 */     this.b = this.a.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
/*     */     
/* 107 */     Intent localIntent = getIntent();
/* 108 */     Serializable localSerializable = localIntent.getSerializableExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG");
/* 109 */     if (localSerializable != null) {
/* 110 */       this.d = ((DeviceInterfaceModuleConfiguration)localSerializable);
/* 111 */       this.f = ((ArrayList)this.d.getDevices());
/*     */       
/* 113 */       this.e.setText(this.d.getName());
/*     */       
/* 115 */       TextView localTextView = (TextView)findViewById(R.id.device_interface_module_serialNumber);
/* 116 */       localTextView.setText(this.d.getSerialNumber().toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public EditDeviceInterfaceModuleActivity()
/*     */   {
/*  73 */     this.f = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */     this.g = new AdapterView.OnItemClickListener()
/*     */     {
/*     */       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/* 123 */         switch (position) {
/*     */         case 0: 
/* 125 */           EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 201, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getPwmDevices(), EditPWMDevicesActivity.class);
/*     */           
/* 127 */           break;
/*     */         case 1: 
/* 129 */           EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 202, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getI2cDevices(), EditI2cDevicesActivity.class);
/*     */           
/* 131 */           break;
/*     */         case 2: 
/* 133 */           EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 203, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getAnalogInputDevices(), EditAnalogInputDevicesActivity.class);
/*     */           
/* 135 */           break;
/*     */         case 3: 
/* 137 */           EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 204, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getDigitalDevices(), EditDigitalDevicesActivity.class);
/*     */           
/* 139 */           break;
/*     */         case 4: 
/* 141 */           EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 205, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getAnalogOutputDevices(), EditAnalogOutputDevicesActivity.class);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private void a(int paramInt, List<DeviceConfiguration> paramList, Class paramClass) {
/* 148 */     Bundle localBundle = new Bundle();
/* 149 */     for (int i = 0; i < paramList.size(); i++) {
/* 150 */       localBundle.putSerializable(String.valueOf(i), (Serializable)paramList.get(i));
/*     */     }
/* 152 */     Intent localIntent = new Intent(this.c, paramClass);
/* 153 */     localIntent.putExtras(localBundle);
/* 154 */     setResult(-1, localIntent);
/* 155 */     startActivityForResult(localIntent, paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onActivityResult(int requestCode, int resultCode, Intent data)
/*     */   {
/* 161 */     Bundle localBundle = null;
/* 162 */     if (resultCode == -1) {
/* 163 */       if (requestCode == 201) {
/* 164 */         localBundle = data.getExtras();
/* 165 */         if (localBundle != null) {
/* 166 */           this.d.setPwmDevices(a(localBundle));
/*     */         }
/* 168 */       } else if (requestCode == 203) {
/* 169 */         localBundle = data.getExtras();
/* 170 */         if (localBundle != null) {
/* 171 */           this.d.setAnalogInputDevices(a(localBundle));
/*     */         }
/* 173 */       } else if (requestCode == 204) {
/* 174 */         localBundle = data.getExtras();
/* 175 */         if (localBundle != null) {
/* 176 */           this.d.setDigitalDevices(a(localBundle));
/*     */         }
/* 178 */       } else if (requestCode == 202) {
/* 179 */         localBundle = data.getExtras();
/* 180 */         if (localBundle != null) {
/* 181 */           this.d.setI2cDevices(a(localBundle));
/*     */         }
/* 183 */       } else if (requestCode == 205) {
/* 184 */         localBundle = data.getExtras();
/* 185 */         if (localBundle != null) {
/* 186 */           this.d.setAnalogOutputDevices(a(localBundle));
/*     */         }
/*     */       }
/* 189 */       a();
/*     */     }
/*     */   }
/*     */   
/*     */   private ArrayList<DeviceConfiguration> a(Bundle paramBundle) {
/* 194 */     ArrayList localArrayList = new ArrayList();
/* 195 */     for (String str : paramBundle.keySet()) {
/* 196 */       DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)paramBundle.getSerializable(str);
/* 197 */       localArrayList.add(Integer.parseInt(str), localDeviceConfiguration);
/*     */     }
/* 199 */     return localArrayList;
/*     */   }
/*     */   
/*     */   private void a() {
/* 203 */     String str = this.b;
/*     */     
/* 205 */     if (!str.toLowerCase().contains("Unsaved".toLowerCase())) {
/* 206 */       str = "Unsaved " + this.b;
/* 207 */       this.a.saveToPreferences(str, R.string.pref_hardware_config_filename);
/* 208 */       this.b = str;
/*     */     }
/* 210 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/*     */   }
/*     */   
/*     */   public void saveDeviceInterface(View v) {
/* 214 */     b();
/*     */   }
/*     */   
/*     */   private void b() {
/* 218 */     Intent localIntent = new Intent();
/*     */     
/* 220 */     this.d.setName(this.e.getText().toString());
/*     */     
/* 222 */     localIntent.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", this.d);
/* 223 */     setResult(-1, localIntent);
/* 224 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelDeviceInterface(View v) {
/* 228 */     setResult(0, new Intent());
/* 229 */     finish();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class a
/*     */     implements TextWatcher
/*     */   {
/*     */     private a() {}
/*     */     
/*     */ 
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */ 
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */ 
/*     */     public void afterTextChanged(Editable editable)
/*     */     {
/* 248 */       String str = editable.toString();
/* 249 */       EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).setName(str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditDeviceInterfaceModuleActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */