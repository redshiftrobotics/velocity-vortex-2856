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
/*     */ 
/*     */ public class EditI2cDevicesActivity
/*     */   extends Activity
/*     */ {
/*     */   private Utility a;
/*     */   private View b;
/*     */   private View c;
/*     */   private View d;
/*     */   private View e;
/*     */   private View f;
/*     */   private View g;
/*     */   private ArrayList<DeviceConfiguration> h;
/*     */   private AdapterView.OnItemSelectedListener i;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  71 */     super.onCreate(savedInstanceState);
/*  72 */     setContentView(R.layout.i2cs);
/*     */     
/*  74 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  75 */     this.a = new Utility(this);
/*     */     
/*  77 */     RobotLog.writeLogcatToDisk(this, 1024);
/*     */     
/*  79 */     LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.linearLayout_i2c0);
/*  80 */     this.b = getLayoutInflater().inflate(R.layout.i2c_device, localLinearLayout1, true);
/*  81 */     TextView localTextView1 = (TextView)this.b.findViewById(R.id.port_number_i2c);
/*  82 */     localTextView1.setText("0");
/*     */     
/*  84 */     LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.linearLayout_i2c1);
/*  85 */     this.c = getLayoutInflater().inflate(R.layout.i2c_device, localLinearLayout2, true);
/*  86 */     TextView localTextView2 = (TextView)this.c.findViewById(R.id.port_number_i2c);
/*  87 */     localTextView2.setText("1");
/*     */     
/*  89 */     LinearLayout localLinearLayout3 = (LinearLayout)findViewById(R.id.linearLayout_i2c2);
/*  90 */     this.d = getLayoutInflater().inflate(R.layout.i2c_device, localLinearLayout3, true);
/*  91 */     TextView localTextView3 = (TextView)this.d.findViewById(R.id.port_number_i2c);
/*  92 */     localTextView3.setText("2");
/*     */     
/*  94 */     LinearLayout localLinearLayout4 = (LinearLayout)findViewById(R.id.linearLayout_i2c3);
/*  95 */     this.e = getLayoutInflater().inflate(R.layout.i2c_device, localLinearLayout4, true);
/*  96 */     TextView localTextView4 = (TextView)this.e.findViewById(R.id.port_number_i2c);
/*  97 */     localTextView4.setText("3");
/*     */     
/*  99 */     LinearLayout localLinearLayout5 = (LinearLayout)findViewById(R.id.linearLayout_i2c4);
/* 100 */     this.f = getLayoutInflater().inflate(R.layout.i2c_device, localLinearLayout5, true);
/* 101 */     TextView localTextView5 = (TextView)this.f.findViewById(R.id.port_number_i2c);
/* 102 */     localTextView5.setText("4");
/*     */     
/* 104 */     LinearLayout localLinearLayout6 = (LinearLayout)findViewById(R.id.linearLayout_i2c5);
/* 105 */     this.g = getLayoutInflater().inflate(R.layout.i2c_device, localLinearLayout6, true);
/* 106 */     TextView localTextView6 = (TextView)this.g.findViewById(R.id.port_number_i2c);
/* 107 */     localTextView6.setText("5");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 113 */     super.onStart();
/*     */     
/* 115 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 116 */     Intent localIntent = getIntent();
/* 117 */     Bundle localBundle = localIntent.getExtras();
/*     */     
/* 119 */     if (localBundle != null) {
/* 120 */       for (Iterator localIterator = localBundle.keySet().iterator(); localIterator.hasNext();) { localObject = (String)localIterator.next();
/* 121 */         localDeviceConfiguration = (DeviceConfiguration)localBundle.getSerializable((String)localObject);
/* 122 */         this.h.add(Integer.parseInt((String)localObject), localDeviceConfiguration); }
/*     */       Object localObject;
/*     */       DeviceConfiguration localDeviceConfiguration;
/* 125 */       for (int j = 0; j < this.h.size(); j++) {
/* 126 */         localObject = a(j);
/* 127 */         localDeviceConfiguration = (DeviceConfiguration)this.h.get(j);
/* 128 */         a((View)localObject);
/* 129 */         b((View)localObject, localDeviceConfiguration);
/* 130 */         a((View)localObject, localDeviceConfiguration);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(View paramView, DeviceConfiguration paramDeviceConfiguration) {
/* 136 */     Spinner localSpinner = (Spinner)paramView.findViewById(R.id.choiceSpinner_i2c);
/* 137 */     ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
/*     */     
/* 139 */     if (paramDeviceConfiguration.isEnabled()) {
/* 140 */       int j = localArrayAdapter.getPosition(paramDeviceConfiguration.getType().toString());
/* 141 */       localSpinner.setSelection(j);
/*     */     } else {
/* 143 */       localSpinner.setSelection(0);
/*     */     }
/*     */     
/* 146 */     localSpinner.setOnItemSelectedListener(this.i);
/*     */   }
/*     */   
/*     */   private void b(View paramView, DeviceConfiguration paramDeviceConfiguration) {
/* 150 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_i2c);
/* 151 */     if (paramDeviceConfiguration.isEnabled()) {
/* 152 */       localEditText.setText(paramDeviceConfiguration.getName());
/* 153 */       localEditText.setEnabled(true);
/*     */     } else {
/* 155 */       localEditText.setText("NO DEVICE ATTACHED");
/* 156 */       localEditText.setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(View paramView) {
/* 161 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_i2c);
/*     */     
/* 163 */     localEditText.addTextChangedListener(new a(paramView, null));
/*     */   }
/*     */   
/*     */   private View a(int paramInt) {
/* 167 */     switch (paramInt) {
/*     */     case 0: 
/* 169 */       return this.b;
/*     */     case 1: 
/* 171 */       return this.c;
/*     */     case 2: 
/* 173 */       return this.d;
/*     */     case 3: 
/* 175 */       return this.e;
/*     */     case 4: 
/* 177 */       return this.f;
/*     */     case 5: 
/* 179 */       return this.g;
/*     */     }
/* 181 */     return null;
/*     */   }
/*     */   
/*     */   public void saveI2cDevices(View v)
/*     */   {
/* 186 */     a();
/*     */   }
/*     */   
/*     */ 
/*     */   private void a()
/*     */   {
/* 192 */     Bundle localBundle = new Bundle();
/* 193 */     for (int j = 0; j < this.h.size(); j++) {
/* 194 */       DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.h.get(j);
/* 195 */       localBundle.putSerializable(String.valueOf(j), localDeviceConfiguration);
/*     */     }
/*     */     
/* 198 */     Intent localIntent = new Intent();
/* 199 */     localIntent.putExtras(localBundle);
/* 200 */     localIntent.putExtras(localBundle);
/* 201 */     setResult(-1, localIntent);
/* 202 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelI2cDevices(View v)
/*     */   {
/* 207 */     setResult(0, new Intent());
/* 208 */     finish();
/*     */   }
/*     */   
/*     */   private void a(LinearLayout paramLinearLayout) {
/* 212 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.port_number_i2c);
/* 213 */     int j = Integer.parseInt(localTextView.getText().toString());
/* 214 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_i2c);
/*     */     
/* 216 */     localEditText.setEnabled(false);
/* 217 */     localEditText.setText("NO DEVICE ATTACHED");
/*     */     
/* 219 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.h.get(j);
/* 220 */     localDeviceConfiguration.setEnabled(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(LinearLayout paramLinearLayout, String paramString)
/*     */   {
/* 229 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.port_number_i2c);
/* 230 */     int j = Integer.parseInt(localTextView.getText().toString());
/* 231 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_i2c);
/* 232 */     localEditText.setEnabled(true);
/*     */     
/* 234 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.h.get(j);
/* 235 */     DeviceConfiguration.ConfigurationType localConfigurationType = localDeviceConfiguration.typeFromString(paramString);
/* 236 */     localDeviceConfiguration.setType(localConfigurationType);
/* 237 */     localDeviceConfiguration.setEnabled(true);
/*     */     
/* 239 */     a(localEditText, localDeviceConfiguration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(EditText paramEditText, DeviceConfiguration paramDeviceConfiguration)
/*     */   {
/* 249 */     if (paramEditText.getText().toString().equalsIgnoreCase("NO DEVICE ATTACHED")) {
/* 250 */       paramEditText.setText("");
/* 251 */       paramDeviceConfiguration.setName("");
/*     */     } else {
/* 253 */       paramEditText.setText(paramDeviceConfiguration.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public EditI2cDevicesActivity()
/*     */   {
/*  67 */     this.h = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 260 */     this.i = new AdapterView.OnItemSelectedListener()
/*     */     {
/*     */       public void onItemSelected(AdapterView<?> parent, View view, int pos, long l)
/*     */       {
/* 264 */         String str = parent.getItemAtPosition(pos).toString();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 270 */         LinearLayout localLinearLayout = (LinearLayout)view.getParent().getParent().getParent();
/*     */         
/* 272 */         if (str.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
/* 273 */           EditI2cDevicesActivity.a(EditI2cDevicesActivity.this, localLinearLayout);
/*     */         } else {
/* 275 */           EditI2cDevicesActivity.a(EditI2cDevicesActivity.this, localLinearLayout, str);
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
/* 295 */       TextView localTextView = (TextView)paramView.findViewById(R.id.port_number_i2c);
/* 296 */       this.b = Integer.parseInt(localTextView.getText().toString());
/*     */     }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/* 303 */     public void afterTextChanged(Editable editable) { DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)EditI2cDevicesActivity.a(EditI2cDevicesActivity.this).get(this.b);
/* 304 */       String str = editable.toString();
/* 305 */       localDeviceConfiguration.setName(str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditI2cDevicesActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */