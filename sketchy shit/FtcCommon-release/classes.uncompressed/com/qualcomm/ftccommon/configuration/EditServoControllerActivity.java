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
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditServoControllerActivity
/*     */   extends Activity
/*     */ {
/*     */   public static final String EDIT_SERVO_ACTIVITY = "Edit Servo ControllerConfiguration Activity";
/*     */   private Utility a;
/*     */   private ServoControllerConfiguration b;
/*     */   private ArrayList<DeviceConfiguration> c;
/*     */   private EditText d;
/*     */   private View e;
/*     */   private View f;
/*     */   private View g;
/*     */   private View h;
/*     */   private View i;
/*     */   private View j;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  76 */     super.onCreate(savedInstanceState);
/*  77 */     setContentView(R.layout.servos);
/*     */     
/*  79 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  80 */     this.a = new Utility(this);
/*  81 */     RobotLog.writeLogcatToDisk(this, 1024);
/*     */     
/*  83 */     this.d = ((EditText)findViewById(R.id.servocontroller_name));
/*     */     
/*  85 */     LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.linearLayout_servo1);
/*  86 */     this.e = getLayoutInflater().inflate(R.layout.servo, localLinearLayout1, true);
/*  87 */     TextView localTextView1 = (TextView)this.e.findViewById(R.id.port_number_servo);
/*  88 */     localTextView1.setText("1");
/*     */     
/*  90 */     LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.linearLayout_servo2);
/*  91 */     this.f = getLayoutInflater().inflate(R.layout.servo, localLinearLayout2, true);
/*  92 */     TextView localTextView2 = (TextView)this.f.findViewById(R.id.port_number_servo);
/*  93 */     localTextView2.setText("2");
/*     */     
/*  95 */     LinearLayout localLinearLayout3 = (LinearLayout)findViewById(R.id.linearLayout_servo3);
/*  96 */     this.g = getLayoutInflater().inflate(R.layout.servo, localLinearLayout3, true);
/*  97 */     TextView localTextView3 = (TextView)this.g.findViewById(R.id.port_number_servo);
/*  98 */     localTextView3.setText("3");
/*     */     
/* 100 */     LinearLayout localLinearLayout4 = (LinearLayout)findViewById(R.id.linearLayout_servo4);
/* 101 */     this.h = getLayoutInflater().inflate(R.layout.servo, localLinearLayout4, true);
/* 102 */     TextView localTextView4 = (TextView)this.h.findViewById(R.id.port_number_servo);
/* 103 */     localTextView4.setText("4");
/*     */     
/* 105 */     LinearLayout localLinearLayout5 = (LinearLayout)findViewById(R.id.linearLayout_servo5);
/* 106 */     this.i = getLayoutInflater().inflate(R.layout.servo, localLinearLayout5, true);
/* 107 */     TextView localTextView5 = (TextView)this.i.findViewById(R.id.port_number_servo);
/* 108 */     localTextView5.setText("5");
/*     */     
/* 110 */     LinearLayout localLinearLayout6 = (LinearLayout)findViewById(R.id.linearLayout_servo6);
/* 111 */     this.j = getLayoutInflater().inflate(R.layout.servo, localLinearLayout6, true);
/* 112 */     TextView localTextView6 = (TextView)this.j.findViewById(R.id.port_number_servo);
/* 113 */     localTextView6.setText("6");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 119 */     super.onStart();
/*     */     
/* 121 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 122 */     Intent localIntent = getIntent();
/* 123 */     Serializable localSerializable = localIntent.getSerializableExtra("Edit Servo ControllerConfiguration Activity");
/* 124 */     if (localSerializable != null) {
/* 125 */       this.b = ((ServoControllerConfiguration)localSerializable);
/* 126 */       this.c = ((ArrayList)this.b.getServos());
/*     */     }
/*     */     
/* 129 */     this.d.setText(this.b.getName());
/*     */     
/* 131 */     TextView localTextView = (TextView)findViewById(R.id.servo_controller_serialNumber);
/* 132 */     String str = this.b.getSerialNumber().toString();
/* 133 */     if (str.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
/* 134 */       str = "No serial number";
/*     */     }
/* 136 */     localTextView.setText(str);
/*     */     
/* 138 */     for (int k = 0; k < this.c.size(); k++) {
/* 139 */       c(k + 1);
/* 140 */       a(k + 1);
/* 141 */       b(k + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(int paramInt) {
/* 146 */     View localView = d(paramInt);
/* 147 */     EditText localEditText = (EditText)localView.findViewById(R.id.editTextResult_servo);
/*     */     
/* 149 */     localEditText.addTextChangedListener(new a(localView, null));
/*     */   }
/*     */   
/*     */   private void b(int paramInt) {
/* 153 */     View localView = d(paramInt);
/* 154 */     CheckBox localCheckBox = (CheckBox)localView.findViewById(R.id.checkbox_port_servo);
/* 155 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.c.get(paramInt - 1);
/* 156 */     if (localDeviceConfiguration.isEnabled()) {
/* 157 */       localCheckBox.setChecked(true);
/* 158 */       EditText localEditText = (EditText)localView.findViewById(R.id.editTextResult_servo);
/* 159 */       localEditText.setText(localDeviceConfiguration.getName());
/*     */     } else {
/* 161 */       localCheckBox.setChecked(true);
/*     */       
/*     */ 
/* 164 */       localCheckBox.performClick();
/*     */     }
/*     */   }
/*     */   
/*     */   private void c(int paramInt)
/*     */   {
/* 170 */     View localView = d(paramInt);
/*     */     
/* 172 */     final EditText localEditText = (EditText)localView.findViewById(R.id.editTextResult_servo);
/*     */     
/*     */ 
/* 175 */     final DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)this.c.get(paramInt - 1);
/*     */     
/* 177 */     CheckBox localCheckBox = (CheckBox)localView.findViewById(R.id.checkbox_port_servo);
/* 178 */     localCheckBox.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 181 */         if (((CheckBox)view).isChecked()) {
/* 182 */           localEditText.setEnabled(true);
/* 183 */           localEditText.setText("");
/* 184 */           localDeviceConfiguration.setEnabled(true);
/* 185 */           localDeviceConfiguration.setName("");
/*     */         } else {
/* 187 */           localEditText.setEnabled(false);
/* 188 */           localDeviceConfiguration.setEnabled(false);
/* 189 */           localDeviceConfiguration.setName("NO DEVICE ATTACHED");
/* 190 */           localEditText.setText("NO DEVICE ATTACHED");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private View d(int paramInt) {
/* 197 */     switch (paramInt) {
/*     */     case 1: 
/* 199 */       return this.e;
/*     */     case 2: 
/* 201 */       return this.f;
/*     */     case 3: 
/* 203 */       return this.g;
/*     */     case 4: 
/* 205 */       return this.h;
/*     */     case 5: 
/* 207 */       return this.i;
/*     */     case 6: 
/* 209 */       return this.j;
/*     */     }
/* 211 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void saveServoController(View v)
/*     */   {
/* 217 */     a();
/*     */   }
/*     */   
/*     */   private void a() {
/* 221 */     Intent localIntent = new Intent();
/*     */     
/* 223 */     this.b.addServos(this.c);
/* 224 */     this.b.setName(this.d.getText().toString());
/* 225 */     localIntent.putExtra("Edit Servo ControllerConfiguration Activity", this.b);
/* 226 */     setResult(-1, localIntent);
/* 227 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelServoController(View v) {
/* 231 */     setResult(0, new Intent());
/* 232 */     finish();
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
/*     */     private a(View paramView)
/*     */     {
/* 247 */       TextView localTextView = (TextView)paramView.findViewById(R.id.port_number_servo);
/* 248 */       this.b = Integer.parseInt(localTextView.getText().toString());
/*     */     }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/* 255 */     public void afterTextChanged(Editable editable) { DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)EditServoControllerActivity.a(EditServoControllerActivity.this).get(this.b - 1);
/* 256 */       String str = editable.toString();
/* 257 */       localDeviceConfiguration.setName(str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditServoControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */