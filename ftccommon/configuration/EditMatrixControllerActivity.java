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
/*     */ import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditMatrixControllerActivity
/*     */   extends Activity
/*     */ {
/*     */   public static final String EDIT_MATRIX_ACTIVITY = "Edit Matrix ControllerConfiguration Activity";
/*     */   private Utility a;
/*     */   private MatrixControllerConfiguration b;
/*     */   private ArrayList<DeviceConfiguration> c;
/*     */   private ArrayList<DeviceConfiguration> d;
/*     */   private EditText e;
/*     */   private View f;
/*     */   private View g;
/*     */   private View h;
/*     */   private View i;
/*     */   private View j;
/*     */   private View k;
/*     */   private View l;
/*     */   private View m;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  78 */     super.onCreate(savedInstanceState);
/*  79 */     setContentView(R.layout.matrices);
/*     */     
/*  81 */     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
/*  82 */     this.a = new Utility(this);
/*  83 */     RobotLog.writeLogcatToDisk(this, 1024);
/*     */     
/*  85 */     this.e = ((EditText)findViewById(R.id.matrixcontroller_name));
/*     */     
/*     */ 
/*  88 */     LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.linearLayout_matrix1);
/*  89 */     this.f = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout1, true);
/*  90 */     TextView localTextView1 = (TextView)this.f.findViewById(R.id.port_number_matrix);
/*  91 */     localTextView1.setText("1");
/*     */     
/*  93 */     LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.linearLayout_matrix2);
/*  94 */     this.g = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout2, true);
/*  95 */     TextView localTextView2 = (TextView)this.g.findViewById(R.id.port_number_matrix);
/*  96 */     localTextView2.setText("2");
/*     */     
/*  98 */     LinearLayout localLinearLayout3 = (LinearLayout)findViewById(R.id.linearLayout_matrix3);
/*  99 */     this.h = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout3, true);
/* 100 */     TextView localTextView3 = (TextView)this.h.findViewById(R.id.port_number_matrix);
/* 101 */     localTextView3.setText("3");
/*     */     
/* 103 */     LinearLayout localLinearLayout4 = (LinearLayout)findViewById(R.id.linearLayout_matrix4);
/* 104 */     this.i = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout4, true);
/* 105 */     TextView localTextView4 = (TextView)this.i.findViewById(R.id.port_number_matrix);
/* 106 */     localTextView4.setText("4");
/*     */     
/*     */ 
/* 109 */     LinearLayout localLinearLayout5 = (LinearLayout)findViewById(R.id.linearLayout_matrix5);
/* 110 */     this.j = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout5, true);
/* 111 */     TextView localTextView5 = (TextView)this.j.findViewById(R.id.port_number_matrix);
/* 112 */     localTextView5.setText("1");
/*     */     
/* 114 */     LinearLayout localLinearLayout6 = (LinearLayout)findViewById(R.id.linearLayout_matrix6);
/* 115 */     this.k = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout6, true);
/* 116 */     TextView localTextView6 = (TextView)this.k.findViewById(R.id.port_number_matrix);
/* 117 */     localTextView6.setText("2");
/*     */     
/* 119 */     LinearLayout localLinearLayout7 = (LinearLayout)findViewById(R.id.linearLayout_matrix7);
/* 120 */     this.l = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout7, true);
/* 121 */     TextView localTextView7 = (TextView)this.l.findViewById(R.id.port_number_matrix);
/* 122 */     localTextView7.setText("3");
/*     */     
/* 124 */     LinearLayout localLinearLayout8 = (LinearLayout)findViewById(R.id.linearLayout_matrix8);
/* 125 */     this.m = getLayoutInflater().inflate(R.layout.matrix_devices, localLinearLayout8, true);
/* 126 */     TextView localTextView8 = (TextView)this.m.findViewById(R.id.port_number_matrix);
/* 127 */     localTextView8.setText("4");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 133 */     super.onStart();
/*     */     
/* 135 */     this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 136 */     Intent localIntent = getIntent();
/* 137 */     Serializable localSerializable = localIntent.getSerializableExtra("Edit Matrix ControllerConfiguration Activity");
/* 138 */     if (localSerializable != null) {
/* 139 */       this.b = ((MatrixControllerConfiguration)localSerializable);
/* 140 */       this.c = ((ArrayList)this.b.getMotors());
/* 141 */       this.d = ((ArrayList)this.b.getServos());
/*     */     }
/*     */     
/* 144 */     this.e.setText(this.b.getName());
/*     */     View localView;
/* 146 */     for (int n = 0; n < this.c.size(); n++) {
/* 147 */       localView = b(n + 1);
/* 148 */       b(n + 1, localView, this.c);
/* 149 */       a(localView, (DeviceConfiguration)this.c.get(n));
/* 150 */       a(n + 1, localView, this.c);
/*     */     }
/*     */     
/* 153 */     for (n = 0; n < this.d.size(); n++) {
/* 154 */       localView = a(n + 1);
/* 155 */       b(n + 1, localView, this.d);
/* 156 */       a(localView, (DeviceConfiguration)this.d.get(n));
/* 157 */       a(n + 1, localView, this.d);
/*     */     }
/*     */   }
/*     */   
/*     */   private void a(View paramView, DeviceConfiguration paramDeviceConfiguration) {
/* 162 */     EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_matrix);
/*     */     
/* 164 */     localEditText.addTextChangedListener(new a(paramDeviceConfiguration, null));
/*     */   }
/*     */   
/*     */   private void a(int paramInt, View paramView, ArrayList<DeviceConfiguration> paramArrayList) {
/* 168 */     CheckBox localCheckBox = (CheckBox)paramView.findViewById(R.id.checkbox_port_matrix);
/* 169 */     DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)paramArrayList.get(paramInt - 1);
/* 170 */     if (localDeviceConfiguration.isEnabled()) {
/* 171 */       localCheckBox.setChecked(true);
/* 172 */       EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_matrix);
/* 173 */       localEditText.setText(localDeviceConfiguration.getName());
/*     */     } else {
/* 175 */       localCheckBox.setChecked(true);
/*     */       
/*     */ 
/* 178 */       localCheckBox.performClick();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void b(int paramInt, View paramView, ArrayList<DeviceConfiguration> paramArrayList)
/*     */   {
/* 185 */     final EditText localEditText = (EditText)paramView.findViewById(R.id.editTextResult_matrix);
/*     */     
/*     */ 
/* 188 */     final DeviceConfiguration localDeviceConfiguration = (DeviceConfiguration)paramArrayList.get(paramInt - 1);
/*     */     
/* 190 */     CheckBox localCheckBox = (CheckBox)paramView.findViewById(R.id.checkbox_port_matrix);
/* 191 */     localCheckBox.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 194 */         if (((CheckBox)view).isChecked()) {
/* 195 */           localEditText.setEnabled(true);
/* 196 */           localEditText.setText("");
/* 197 */           localDeviceConfiguration.setEnabled(true);
/* 198 */           localDeviceConfiguration.setName("");
/*     */         } else {
/* 200 */           localEditText.setEnabled(false);
/* 201 */           localDeviceConfiguration.setEnabled(false);
/* 202 */           localDeviceConfiguration.setName("NO DEVICE ATTACHED");
/* 203 */           localEditText.setText("NO DEVICE ATTACHED");
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private View a(int paramInt) {
/* 210 */     switch (paramInt) {
/*     */     case 1: 
/* 212 */       return this.f;
/*     */     case 2: 
/* 214 */       return this.g;
/*     */     case 3: 
/* 216 */       return this.h;
/*     */     case 4: 
/* 218 */       return this.i;
/*     */     }
/* 220 */     return null;
/*     */   }
/*     */   
/*     */   private View b(int paramInt)
/*     */   {
/* 225 */     switch (paramInt) {
/*     */     case 1: 
/* 227 */       return this.j;
/*     */     case 2: 
/* 229 */       return this.k;
/*     */     case 3: 
/* 231 */       return this.l;
/*     */     case 4: 
/* 233 */       return this.m;
/*     */     }
/* 235 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void saveMatrixController(View v)
/*     */   {
/* 241 */     a();
/*     */   }
/*     */   
/*     */   private void a() {
/* 245 */     Intent localIntent = new Intent();
/*     */     
/* 247 */     this.b.addServos(this.d);
/* 248 */     this.b.addMotors(this.c);
/* 249 */     this.b.setName(this.e.getText().toString());
/* 250 */     localIntent.putExtra("Edit Matrix ControllerConfiguration Activity", this.b);
/* 251 */     setResult(-1, localIntent);
/* 252 */     finish();
/*     */   }
/*     */   
/*     */   public void cancelMatrixController(View v) {
/* 256 */     setResult(0, new Intent());
/* 257 */     finish();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class a
/*     */     implements TextWatcher
/*     */   {
/*     */     private DeviceConfiguration b;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 272 */     private a(DeviceConfiguration paramDeviceConfiguration) { this.b = paramDeviceConfiguration; }
/*     */     
/*     */     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
/*     */     
/*     */     public void afterTextChanged(Editable editable) {
/* 279 */       String str = editable.toString();
/* 280 */       this.b.setName(str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/matt/Documents/robotics/2856/SwerveRoboticsLibrary/build/intermediates/exploded-aar/FtcCommon-release/jars/classes.jar!/com/qualcomm/ftccommon/configuration/EditMatrixControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */