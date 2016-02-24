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
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.CheckBox;
/*     */ import android.widget.EditText;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.ftccommon.R.xml;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import com.qualcomm.robotcore.util.RobotLog;
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
/*     */ public class EditPWMDevicesActivity
/*     */   extends Activity
/*     */ {
/*     */   private Utility a;
/*     */   public static final String EDIT_PWM_DEVICES = "EDIT_PWM_DEVICES";
/*     */   private View b;
/*     */   private View c;
/*     */   private ArrayList<DeviceConfiguration> d;
/*     */   
/*     */   public EditPWMDevicesActivity()
/*     */   {
/*  61 */     this.d = new ArrayList();
/*     */   }
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState) {
/*  65 */     super.onCreate(savedInstanceState);
/*  66 */     setContentView(R.layout.pwms);
/*     */     
/*  68 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  69 */     this.a = new Utility(this);
/*     */     
/*  71 */     RobotLog.writeLogcatToDisk(this, 1024);
/*     */     
/*  73 */     LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.linearLayout_pwm0);
/*  74 */     this.b = getLayoutInflater().inflate(R.layout.pwm_device, localLinearLayout1, true);
/*  75 */     TextView localTextView1 = (TextView)this.b.findViewById(R.id.port_number_pwm);
/*  76 */     localTextView1.setText("0");
/*     */     
/*  78 */     LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.linearLayout_pwm1);
/*  79 */     this.c = getLayoutInflater().inflate(R.layout.pwm_device, localLinearLayout2, true);
/*  80 */     TextView localTextView2 = (TextView)this.c.findViewById(R.id.port_number_pwm);
/*  81 */     localTextView2.setText("1");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/*  87 */     super.onStart();
/*     */     
/*  89 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/*  90 */     Intent localIntent = getIntent();
/*  91 */     Bundle localBundle = localIntent.getExtras();
/*     */     
/*  93 */     if (localBundle != null) {
/*  94 */       for (String str : localBundle.keySet()) {
/*  95 */         this.d.add(Integer.parseInt(str), (DeviceConfiguration)localBundle.getSerializable(str));
/*     */       }
/*     */       
/*  98 */       for (int i = 0; i < this.d.size(); i++) {
/*  99 */         c(i);
/* 100 */         b(i);
/* 101 */         a(i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(int paramInt) {
/* 107 */     View localView = d(paramInt);
/* 108 */     CheckBox localCheckBox = (CheckBox)localView.findViewById(R.id.checkbox_port_pwm);
/* 109 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.d.get(paramInt);
/* 110 */     if (localDeviceConfiguration.isEnabled()) {
/* 111 */       localCheckBox.setChecked(true);
/* 112 */       EditText localEditText = (EditText)localView.findViewById(R.id.editTextResult_pwm);
/* 113 */       localEditText.setText(localDeviceConfiguration.getName());
/*     */     } else {
/* 115 */       localCheckBox.setChecked(true);
/*     */       
/*     */ 
/* 118 */       localCheckBox.performClick();
/*     */     }
/*     */   }
/*     */   
/*     */   private void b(int paramInt) {
/* 123 */     View localView = d(paramInt);
/* 124 */     EditText localEditText = (EditText)localView.findViewById(R.id.editTextResult_pwm);
/*     */     
/* 126 */     localEditText.addTextChangedListener(new a(localView, null));
/*     */   }
/*     */   
/*     */   private void c(int paramInt) {
/* 130 */     View localView = d(paramInt);
/*     */     
/* 132 */     final EditText localEditText = (EditText)localView.findViewById(R.id.editTextResult_pwm);
/*     */     
/*     */ 
/* 135 */     final DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.d.get(paramInt);
/*     */     
/* 137 */     CheckBox localCheckBox = (CheckBox)localView.findViewById(R.id.checkbox_port_pwm);
/* 138 */     localCheckBox.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 141 */         if (((CheckBox)view).isChecked()) {
/* 142 */           localEditText.setEnabled(true);
/* 143 */           localEditText.setText("");
/* 144 */           localDeviceConfiguration.setEnabled(true);
/* 145 */           localDeviceConfiguration.setName("");
/*     */         } else {
/* 147 */           localEditText.setEnabled(false);
/* 148 */           localDeviceConfiguration.setEnabled(false);
/* 149 */           localDeviceConfiguration.setName("NO DEVICE ATTACHED");
/* 150 */           localEditText.setText("NO DEVICE ATTACHED");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private View d(int paramInt) {
/* 157 */     switch (paramInt) {
/*     */     case 0: 
/* 159 */       return this.b;
/*     */     case 1: 
/* 161 */       return this.c;
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */   
/*     */   public void savePWMDevices(View v)
/*     */   {
/* 168 */     a();
/*     */   }
/*     */   
/*     */   private void a()
/*     */   {
/* 173 */     Bundle localBundle = new Bundle();
/* 174 */     for (int i = 0; i < this.d.size(); i++) {
/* 175 */       localBundle.putSerializable(String.valueOf(i), (Serializable)this.d.get(i));
/*     */     }
/*     */     
/* 178 */     Intent localIntent = new Intent();
/* 179 */     localIntent.putExtras(localBundle);
/* 180 */     localIntent.putExtras(localBundle);
/* 181 */     setResult(-1, localIntent);
/* 182 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelPWMDevices(View v) {
/* 186 */     setResult(0, new Intent());
/* 187 */     finish();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private class a
/*     */     implements TextWatcher
/*     */   {
/*     */     private int b;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private a(View paramView)
/*     */     {
/* 203 */       TextView localTextView = (TextView)paramView.findViewById(R.id.port_number_pwm);
/* 204 */       this.b = Integer.parseInt(localTextView.getText().toString());
/*     */     }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/* 211 */     public void afterTextChanged(Editable editable) { DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)EditPWMDevicesActivity.a(EditPWMDevicesActivity.this).get(this.b);
/* 212 */       String str = editable.toString();
/* 213 */       localDeviceConfiguration.setName(str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditPWMDevicesActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */