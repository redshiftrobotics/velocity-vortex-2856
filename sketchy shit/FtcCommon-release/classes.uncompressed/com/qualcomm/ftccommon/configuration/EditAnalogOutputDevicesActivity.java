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
/*     */ public class EditAnalogOutputDevicesActivity
/*     */   extends Activity
/*     */ {
/*     */   private Utility a;
/*     */   private View b;
/*     */   private View c;
/*     */   private ArrayList<DeviceConfiguration> d;
/*     */   private AdapterView.OnItemSelectedListener e;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  67 */     super.onCreate(savedInstanceState);
/*  68 */     setContentView(R.layout.analog_outputs);
/*     */     
/*  70 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  71 */     this.a = new Utility(this);
/*     */     
/*  73 */     RobotLog.writeLogcatToDisk(this, 1024);
/*     */     
/*  75 */     LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.linearLayout_analogOutput0);
/*  76 */     this.b = getLayoutInflater().inflate(R.layout.analog_output_device, localLinearLayout1, true);
/*  77 */     TextView localTextView1 = (TextView)this.b.findViewById(R.id.port_number_analogOutput);
/*  78 */     localTextView1.setText("0");
/*     */     
/*  80 */     LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.linearLayout_analogOutput1);
/*  81 */     this.c = getLayoutInflater().inflate(R.layout.analog_output_device, localLinearLayout2, true);
/*  82 */     TextView localTextView2 = (TextView)this.c.findViewById(R.id.port_number_analogOutput);
/*  83 */     localTextView2.setText("1");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/*  89 */     super.onStart();
/*     */     
/*  91 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/*  92 */     Intent localIntent = getIntent();
/*  93 */     Bundle localBundle = localIntent.getExtras();
/*     */     
/*  95 */     if (localBundle != null) {
/*  96 */       for (Iterator localIterator = localBundle.keySet().iterator(); localIterator.hasNext();) { localObject = (String)localIterator.next();
/*  97 */         localDeviceConfiguration = (DeviceConfiguration)localBundle.getSerializable((String)localObject);
/*  98 */         this.d.add(Integer.parseInt((String)localObject), localDeviceConfiguration); }
/*     */       Object localObject;
/*     */       DeviceConfiguration localDeviceConfiguration;
/* 101 */       for (int i = 0; i < this.d.size(); i++) {
/* 102 */         localObject = a(i);
/* 103 */         localDeviceConfiguration = (DeviceConfiguration)this.d.get(i);
/* 104 */         a((View)localObject);
/* 105 */         b((View)localObject, localDeviceConfiguration);
/* 106 */         a((View)localObject, localDeviceConfiguration);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(View paramView, DeviceConfiguration paramDeviceConfiguration) {
/* 112 */     Spinner localSpinner = (Spinner)paramView.findViewById(R.id.choiceSpinner_analogOutput);
/* 113 */     ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
/*     */     
/* 115 */     if (paramDeviceConfiguration.isEnabled()) {
/* 116 */       int i = localArrayAdapter.getPosition(paramDeviceConfiguration.getType().toString());
/* 117 */       localSpinner.setSelection(i);
/*     */     } else {
/* 119 */       localSpinner.setSelection(0);
/*     */     }
/*     */     
/* 122 */     localSpinner.setOnItemSelectedListener(this.e);
/*     */   }
/*     */   
/*     */   private void b(View paramView, DeviceConfiguration paramDeviceConfiguration) {
/* 126 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_analogOutput);
/* 127 */     if (paramDeviceConfiguration.isEnabled()) {
/* 128 */       localEditText.setText(paramDeviceConfiguration.getName());
/* 129 */       localEditText.setEnabled(true);
/*     */     } else {
/* 131 */       localEditText.setText("NO DEVICE ATTACHED");
/* 132 */       localEditText.setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(View paramView) {
/* 137 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_analogOutput);
/*     */     
/* 139 */     localEditText.addTextChangedListener(new a(paramView, null));
/*     */   }
/*     */   
/*     */   private View a(int paramInt) {
/* 143 */     switch (paramInt) {
/*     */     case 0: 
/* 145 */       return this.b;
/*     */     case 1: 
/* 147 */       return this.c;
/*     */     }
/* 149 */     return null;
/*     */   }
/*     */   
/*     */   public void saveanalogOutputDevices(View v)
/*     */   {
/* 154 */     a();
/*     */   }
/*     */   
/*     */ 
/*     */   private void a()
/*     */   {
/* 160 */     Bundle localBundle = new Bundle();
/* 161 */     for (int i = 0; i < this.d.size(); i++) {
/* 162 */       DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.d.get(i);
/* 163 */       localBundle.putSerializable(String.valueOf(i), localDeviceConfiguration);
/*     */     }
/*     */     
/* 166 */     Intent localIntent = new Intent();
/* 167 */     localIntent.putExtras(localBundle);
/* 168 */     localIntent.putExtras(localBundle);
/* 169 */     setResult(-1, localIntent);
/* 170 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelanalogOutputDevices(View v)
/*     */   {
/* 175 */     setResult(0, new Intent());
/* 176 */     finish();
/*     */   }
/*     */   
/*     */   private void a(LinearLayout paramLinearLayout) {
/* 180 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.port_number_analogOutput);
/* 181 */     int i = Integer.parseInt(localTextView.getText().toString());
/* 182 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_analogOutput);
/*     */     
/* 184 */     localEditText.setEnabled(false);
/* 185 */     localEditText.setText("NO DEVICE ATTACHED");
/*     */     
/* 187 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.d.get(i);
/* 188 */     localDeviceConfiguration.setEnabled(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(LinearLayout paramLinearLayout, String paramString)
/*     */   {
/* 197 */     TextView localTextView = (TextView)paramLinearLayout.findViewById(R.id.port_number_analogOutput);
/* 198 */     int i = Integer.parseInt(localTextView.getText().toString());
/* 199 */     EditText localEditText = (EditText)paramLinearLayout.findViewById(R.id.editTextResult_analogOutput);
/* 200 */     localEditText.setEnabled(true);
/*     */     
/* 202 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.d.get(i);
/* 203 */     DeviceConfiguration.ConfigurationType localConfigurationType = localDeviceConfiguration.typeFromString(paramString);
/* 204 */     localDeviceConfiguration.setType(localConfigurationType);
/* 205 */     localDeviceConfiguration.setEnabled(true);
/*     */     
/* 207 */     a(localEditText, localDeviceConfiguration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void a(EditText paramEditText, DeviceConfiguration paramDeviceConfiguration)
/*     */   {
/* 217 */     if (paramEditText.getText().toString().equalsIgnoreCase("NO DEVICE ATTACHED")) {
/* 218 */       paramEditText.setText("");
/* 219 */       paramDeviceConfiguration.setName("");
/*     */     } else {
/* 221 */       paramEditText.setText(paramDeviceConfiguration.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public EditAnalogOutputDevicesActivity()
/*     */   {
/*  63 */     this.d = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */     this.e = new AdapterView.OnItemSelectedListener()
/*     */     {
/*     */       public void onItemSelected(AdapterView<?> parent, View view, int pos, long l)
/*     */       {
/* 232 */         String str = parent.getItemAtPosition(pos).toString();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */         LinearLayout localLinearLayout = (LinearLayout)view.getParent().getParent().getParent();
/*     */         
/* 240 */         if (str.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
/* 241 */           EditAnalogOutputDevicesActivity.a(EditAnalogOutputDevicesActivity.this, localLinearLayout);
/*     */         } else {
/* 243 */           EditAnalogOutputDevicesActivity.a(EditAnalogOutputDevicesActivity.this, localLinearLayout, str);
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
/* 263 */       TextView localTextView = (TextView)paramView.findViewById(R.id.port_number_analogOutput);
/* 264 */       this.b = Integer.parseInt(localTextView.getText().toString());
/*     */     }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/* 271 */     public void afterTextChanged(Editable editable) { DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)EditAnalogOutputDevicesActivity.a(EditAnalogOutputDevicesActivity.this).get(this.b);
/* 272 */       String str = editable.toString();
/* 273 */       localDeviceConfiguration.setName(str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditAnalogOutputDevicesActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */