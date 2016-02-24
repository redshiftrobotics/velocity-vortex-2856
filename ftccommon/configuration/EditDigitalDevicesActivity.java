/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
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
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.Spinner;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.ftccommon.R.xml;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class EditDigitalDevicesActivity
/*     */   extends Activity
/*     */ {
/*     */   private Utility a;
/*     */   private View b;
/*     */   private View c;
/*     */   private View d;
/*     */   private View e;
/*     */   private View f;
/*     */   private View g;
/*     */   private View h;
/*     */   private View i;
/*     */   private ArrayList<DeviceConfiguration> j;
/*     */   private AdapterView.OnItemSelectedListener k;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  73 */     super.onCreate(savedInstanceState);
/*  74 */     setContentView(R.layout.digital_devices);
/*     */     
/*  76 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  77 */     this.a = new Utility(this);
/*     */     
/*  79 */     RobotLog.writeLogcatToDisk(this, 1024);
/*     */     
/*  81 */     LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.linearLayout_digital_device0);
/*  82 */     this.b = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout1, true);
/*  83 */     TextView localTextView1 = (TextView)this.b.findViewById(R.id.port_number_digital_device);
/*  84 */     localTextView1.setText("0");
/*     */     
/*  86 */     LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.linearLayout_digital_device1);
/*  87 */     this.c = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout2, true);
/*  88 */     TextView localTextView2 = (TextView)this.c.findViewById(R.id.port_number_digital_device);
/*  89 */     localTextView2.setText("1");
/*     */     
/*  91 */     LinearLayout localLinearLayout3 = (LinearLayout)findViewById(R.id.linearLayout_digital_device2);
/*  92 */     this.d = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout3, true);
/*  93 */     TextView localTextView3 = (TextView)this.d.findViewById(R.id.port_number_digital_device);
/*  94 */     localTextView3.setText("2");
/*     */     
/*  96 */     LinearLayout localLinearLayout4 = (LinearLayout)findViewById(R.id.linearLayout_digital_device3);
/*  97 */     this.e = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout4, true);
/*  98 */     TextView localTextView4 = (TextView)this.e.findViewById(R.id.port_number_digital_device);
/*  99 */     localTextView4.setText("3");
/*     */     
/* 101 */     LinearLayout localLinearLayout5 = (LinearLayout)findViewById(R.id.linearLayout_digital_device4);
/* 102 */     this.f = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout5, true);
/* 103 */     TextView localTextView5 = (TextView)this.f.findViewById(R.id.port_number_digital_device);
/* 104 */     localTextView5.setText("4");
/*     */     
/* 106 */     LinearLayout localLinearLayout6 = (LinearLayout)findViewById(R.id.linearLayout_digital_device5);
/* 107 */     this.g = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout6, true);
/* 108 */     TextView localTextView6 = (TextView)this.g.findViewById(R.id.port_number_digital_device);
/* 109 */     localTextView6.setText("5");
/*     */     
/* 111 */     LinearLayout localLinearLayout7 = (LinearLayout)findViewById(R.id.linearLayout_digital_device6);
/* 112 */     this.h = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout7, true);
/* 113 */     TextView localTextView7 = (TextView)this.h.findViewById(R.id.port_number_digital_device);
/* 114 */     localTextView7.setText("6");
/*     */     
/* 116 */     LinearLayout localLinearLayout8 = (LinearLayout)findViewById(R.id.linearLayout_digital_device7);
/* 117 */     this.i = getLayoutInflater().inflate(R.layout.digital_device, localLinearLayout8, true);
/* 118 */     TextView localTextView8 = (TextView)this.i.findViewById(R.id.port_number_digital_device);
/* 119 */     localTextView8.setText("7");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 125 */     super.onStart();
/*     */     
/* 127 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 128 */     Intent localIntent = getIntent();
/* 129 */     Bundle localBundle = localIntent.getExtras();
/*     */     
/* 131 */     if (localBundle != null) {
/* 132 */       for (Iterator localIterator = localBundle.keySet().iterator(); localIterator.hasNext();) { localObject = (String)localIterator.next();
/* 133 */         localDeviceConfiguration = (DeviceConfiguration)localBundle.getSerializable((String)localObject);
/* 134 */         this.j.add(Integer.parseInt((String)localObject), localDeviceConfiguration); }
/*     */       Object localObject;
/*     */       DeviceConfiguration localDeviceConfiguration;
/* 137 */       for (int m = 0; m < this.j.size(); m++) {
/* 138 */         localObject = a(m);
/* 139 */         localDeviceConfiguration = (DeviceConfiguration)this.j.get(m);
/* 140 */         a((View)localObject);
/* 141 */         b((View)localObject, localDeviceConfiguration);
/* 142 */         a((View)localObject, localDeviceConfiguration);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(View paramView, DeviceConfiguration paramDeviceConfiguration) {
/* 148 */     Spinner localSpinner = (Spinner)paramView.findViewById(R.id.choiceSpinner_digital_device);
/* 149 */     ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
/*     */     
/* 151 */     if (paramDeviceConfiguration.isEnabled()) {
/* 152 */       int m = localArrayAdapter.getPosition(paramDeviceConfiguration.getType().toString());
/* 153 */       localSpinner.setSelection(m);
/*     */     } else {
/* 155 */       localSpinner.setSelection(0);
/*     */     }
/*     */     
/* 158 */     localSpinner.setOnItemSelectedListener(this.k);
/*     */   }
/*     */   
/*     */   private void b(View paramView, DeviceConfiguration paramDeviceConfiguration) {
/* 162 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_digital_device);
/* 163 */     if (paramDeviceConfiguration.isEnabled()) {
/* 164 */       localEditText.setText(paramDeviceConfiguration.getName());
/* 165 */       localEditText.setEnabled(true);
/*     */     } else {
/* 167 */       localEditText.setText("NO DEVICE ATTACHED");
/* 168 */       localEditText.setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(View paramView) {
/* 173 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_digital_device);
/*     */     
/* 175 */     localEditText.addTextChangedListener(new a(paramView, null));
/*     */   }
/*     */   
/*     */   private View a(int paramInt) {
/* 179 */     switch (paramInt) {
/*     */     case 0: 
/* 181 */       return this.b;
/*     */     case 1: 
/* 183 */       return this.c;
/*     */     case 2: 
/* 185 */       return this.d;
/*     */     case 3: 
/* 187 */       return this.e;
/*     */     case 4: 
/* 189 */       return this.f;
/*     */     case 5: 
/* 191 */       return this.g;
/*     */     case 6: 
/* 193 */       return this.h;
/*     */     case 7: 
/* 195 */       return this.i;
/*     */     }
/* 197 */     return null;
/*     */   }
/*     */   
/*     */   public void saveDigitalDevices(View v)
/*     */   {
/* 202 */     a();
/*     */   }
/*     */   
/*     */ 
/*     */   private void a()
/*     */   {
/* 208 */     Bundle localBundle = new Bundle();
/* 209 */     for (int m = 0; m < this.j.size(); m++) {
/* 210 */       localBundle.putSerializable(String.valueOf(m), (Serializable)this.j.get(m));
/*     */     }
/*     */     
/* 213 */     Intent localIntent = new Intent();
/* 214 */     localIntent.putExtras(localBundle);
/* 215 */     localIntent.putExtras(localBundle);
/* 216 */     setResult(-1, localIntent);
/* 217 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelDigitalDevices(View v)
/*     */   {
/* 222 */     setResult(0, new Intent());
/* 223 */     finish();
/*     */   }
/*     */   
/*     */   private void a(LinearLayout paramLinearLayout) {
/* 227 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.port_number_digital_device);
/* 228 */     int m = Integer.parseInt(localTextView.getText().toString());
/* 229 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_digital_device);
/*     */     
/* 231 */     localEditText.setEnabled(false);
/* 232 */     localEditText.setText("NO DEVICE ATTACHED");
/*     */     
/* 234 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.j.get(m);
/* 235 */     localDeviceConfiguration.setEnabled(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(LinearLayout paramLinearLayout, String paramString)
/*     */   {
/* 244 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.port_number_digital_device);
/* 245 */     int m = Integer.parseInt(localTextView.getText().toString());
/* 246 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_digital_device);
/* 247 */     localEditText.setEnabled(true);
/*     */     
/* 249 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.j.get(m);
/* 250 */     DeviceConfiguration.ConfigurationType localConfigurationType = localDeviceConfiguration.typeFromString(paramString);
/* 251 */     localDeviceConfiguration.setType(localConfigurationType);
/* 252 */     localDeviceConfiguration.setEnabled(true);
/*     */     
/* 254 */     a(localEditText, localDeviceConfiguration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(EditText paramEditText, DeviceConfiguration paramDeviceConfiguration)
/*     */   {
/* 264 */     if (paramEditText.getText().toString().equalsIgnoreCase("NO DEVICE ATTACHED")) {
/* 265 */       paramEditText.setText("");
/* 266 */       paramDeviceConfiguration.setName("");
/*     */     } else {
/* 268 */       paramEditText.setText(paramDeviceConfiguration.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public EditDigitalDevicesActivity()
/*     */   {
/*  69 */     this.j = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 275 */     this.k = new AdapterView.OnItemSelectedListener()
/*     */     {
/*     */       public void onItemSelected(AdapterView<?> parent, View view, int pos, long l)
/*     */       {
/* 279 */         String str = parent.getItemAtPosition(pos).toString();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 285 */         LinearLayout localLinearLayout = (LinearLayout)view.getParent().getParent().getParent();
/*     */         
/* 287 */         if (str.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
/* 288 */           EditDigitalDevicesActivity.a(EditDigitalDevicesActivity.this, localLinearLayout);
/*     */         } else {
/* 290 */           EditDigitalDevicesActivity.a(EditDigitalDevicesActivity.this, localLinearLayout, str);
/*     */         }
/*     */       }
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
/*     */     private a(View paramView)
/*     */     {
/* 310 */       TextView localTextView = (TextView)paramView.findViewById(R.id.port_number_digital_device);
/* 311 */       this.b = Integer.parseInt(localTextView.getText().toString());
/*     */     }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/* 318 */     public void afterTextChanged(Editable editable) { DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)EditDigitalDevicesActivity.a(EditDigitalDevicesActivity.this).get(this.b);
/* 319 */       String str = editable.toString();
/* 320 */       localDeviceConfiguration.setName(str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/matt/Documents/robotics/2856/SwerveRoboticsLibrary/build/intermediates/exploded-aar/FtcCommon-release/jars/classes.jar!/com/qualcomm/ftccommon/configuration/EditDigitalDevicesActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */