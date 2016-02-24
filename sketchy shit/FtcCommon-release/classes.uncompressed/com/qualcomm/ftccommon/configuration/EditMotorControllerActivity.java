/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.widget.CheckBox;
/*     */ import android.widget.EditText;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.ftccommon.R.xml;
/*     */ import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
/*     */ import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
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
/*     */ public class EditMotorControllerActivity
/*     */   extends Activity
/*     */ {
/*     */   private Utility a;
/*     */   public static final String EDIT_MOTOR_CONTROLLER_CONFIG = "EDIT_MOTOR_CONTROLLER_CONFIG";
/*     */   private MotorControllerConfiguration b;
/*  60 */   private ArrayList<DeviceConfiguration> c = new ArrayList();
/*  61 */   private MotorConfiguration d = new MotorConfiguration(1);
/*  62 */   private MotorConfiguration e = new MotorConfiguration(2);
/*     */   
/*     */   private EditText f;
/*     */   
/*  66 */   private boolean g = true;
/*  67 */   private boolean h = true;
/*     */   
/*     */   private CheckBox i;
/*     */   
/*     */   private CheckBox j;
/*     */   private EditText k;
/*     */   private EditText l;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  77 */     super.onCreate(savedInstanceState);
/*  78 */     setContentView(R.layout.motors);
/*     */     
/*  80 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  81 */     this.a = new Utility(this);
/*     */     
/*  83 */     RobotLog.writeLogcatToDisk(this, 1024);
/*     */     
/*  85 */     this.f = ((EditText)findViewById(R.id.controller_name));
/*  86 */     this.i = ((CheckBox)findViewById(R.id.checkbox_port7));
/*  87 */     this.j = ((CheckBox)findViewById(R.id.checkbox_port6));
/*  88 */     this.k = ((EditText)findViewById(R.id.editTextResult_analogInput7));
/*  89 */     this.l = ((EditText)findViewById(R.id.editTextResult_analogInput6));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/*  95 */     super.onStart();
/*     */     
/*  97 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/*  98 */     Intent localIntent = getIntent();
/*  99 */     Serializable localSerializable = localIntent.getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
/*     */     
/* 101 */     if (localSerializable != null) {
/* 102 */       this.b = ((MotorControllerConfiguration)localSerializable);
/* 103 */       this.c = ((ArrayList)this.b.getMotors());
/* 104 */       this.d = ((MotorConfiguration)this.c.get(0));
/* 105 */       this.e = ((MotorConfiguration)this.c.get(1));
/*     */       
/* 107 */       this.f.setText(this.b.getName());
/*     */       
/* 109 */       TextView localTextView = (TextView)findViewById(R.id.motor_controller_serialNumber);
/* 110 */       String str = this.b.getSerialNumber().toString();
/* 111 */       if (str.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
/* 112 */         str = "No serial number";
/*     */       }
/* 114 */       localTextView.setText(str);
/*     */       
/* 116 */       this.k.setText(this.d.getName());
/* 117 */       this.l.setText(this.e.getName());
/*     */       
/* 119 */       a();
/* 120 */       a(this.d, this.i);
/* 121 */       b();
/* 122 */       a(this.e, this.j);
/*     */     }
/*     */   }
/*     */   
/* 126 */   private void a(MotorConfiguration paramMotorConfiguration, CheckBox paramCheckBox) { if ((paramMotorConfiguration.getName().equals("NO DEVICE ATTACHED")) || (paramMotorConfiguration.getType() == DeviceConfiguration.ConfigurationType.NOTHING))
/*     */     {
/* 128 */       paramCheckBox.setChecked(true);
/*     */       
/*     */ 
/* 131 */       paramCheckBox.performClick();
/*     */     } else {
/* 133 */       paramCheckBox.setChecked(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a() {
/* 138 */     this.i.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 141 */         if (((CheckBox)view).isChecked()) {
/* 142 */           EditMotorControllerActivity.a(EditMotorControllerActivity.this, true);
/* 143 */           EditMotorControllerActivity.a(EditMotorControllerActivity.this).setEnabled(true);
/* 144 */           EditMotorControllerActivity.a(EditMotorControllerActivity.this).setText("");
/* 145 */           EditMotorControllerActivity.b(EditMotorControllerActivity.this).setPort(1);
/* 146 */           EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.MOTOR);
/*     */         } else {
/* 148 */           EditMotorControllerActivity.a(EditMotorControllerActivity.this, false);
/* 149 */           EditMotorControllerActivity.a(EditMotorControllerActivity.this).setEnabled(false);
/* 150 */           EditMotorControllerActivity.a(EditMotorControllerActivity.this).setText("NO DEVICE ATTACHED");
/* 151 */           EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.NOTHING);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void b() {
/* 158 */     this.j.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 161 */         if (((CheckBox)view).isChecked()) {
/* 162 */           EditMotorControllerActivity.b(EditMotorControllerActivity.this, true);
/* 163 */           EditMotorControllerActivity.c(EditMotorControllerActivity.this).setEnabled(true);
/* 164 */           EditMotorControllerActivity.c(EditMotorControllerActivity.this).setText("");
/* 165 */           EditMotorControllerActivity.d(EditMotorControllerActivity.this).setPort(2);
/* 166 */           EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.MOTOR);
/*     */         } else {
/* 168 */           EditMotorControllerActivity.b(EditMotorControllerActivity.this, false);
/* 169 */           EditMotorControllerActivity.c(EditMotorControllerActivity.this).setEnabled(false);
/* 170 */           EditMotorControllerActivity.c(EditMotorControllerActivity.this).setText("NO DEVICE ATTACHED");
/* 171 */           EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.NOTHING);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void saveMotorController(View v) {
/* 178 */     c();
/*     */   }
/*     */   
/*     */   private void c()
/*     */   {
/* 183 */     Intent localIntent = new Intent();
/* 184 */     ArrayList localArrayList = new ArrayList();
/* 185 */     String str; MotorConfiguration localMotorConfiguration; if (this.g) {
/* 186 */       str = this.k.getText().toString();
/* 187 */       localMotorConfiguration = new MotorConfiguration(str);
/* 188 */       localMotorConfiguration.setEnabled(true);
/* 189 */       localMotorConfiguration.setPort(1);
/* 190 */       localArrayList.add(localMotorConfiguration);
/* 191 */     } else { localArrayList.add(new MotorConfiguration(1));
/*     */     }
/* 193 */     if (this.h) {
/* 194 */       str = this.l.getText().toString();
/* 195 */       localMotorConfiguration = new MotorConfiguration(str);
/* 196 */       localMotorConfiguration.setEnabled(true);
/* 197 */       localMotorConfiguration.setPort(2);
/* 198 */       localArrayList.add(localMotorConfiguration);
/* 199 */     } else { localArrayList.add(new MotorConfiguration(2));
/*     */     }
/* 201 */     this.b.addMotors(localArrayList);
/* 202 */     this.b.setName(this.f.getText().toString());
/* 203 */     localIntent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", this.b);
/*     */     
/* 205 */     setResult(-1, localIntent);
/* 206 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelMotorController(View v)
/*     */   {
/* 211 */     setResult(0, new Intent());
/* 212 */     finish();
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\EditMotorControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */