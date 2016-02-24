/*     */ package com.qualcomm.ftccommon.configuration;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.AlertDialog;
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.content.Context;
/*     */ import android.content.DialogInterface;
/*     */ import android.content.DialogInterface.OnClickListener;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.view.LayoutInflater;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.view.ViewGroup;
/*     */ import android.widget.Button;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.TextView;
/*     */ import com.qualcomm.ftccommon.DbgLog;
/*     */ import com.qualcomm.ftccommon.R.id;
/*     */ import com.qualcomm.ftccommon.R.layout;
/*     */ import com.qualcomm.ftccommon.R.string;
/*     */ import com.qualcomm.robotcore.hardware.configuration.Utility;
/*     */ import java.io.File;
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
/*     */ public class FtcLoadFileActivity
/*     */   extends Activity
/*     */ {
/*  56 */   private ArrayList<String> b = new ArrayList();
/*     */   
/*     */   private Context c;
/*     */   private Utility d;
/*     */   
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/*  63 */     super.onCreate(savedInstanceState);
/*  64 */     setContentView(R.layout.activity_load);
/*     */     
/*  66 */     this.c = this;
/*  67 */     this.d = new Utility(this);
/*  68 */     this.d.createConfigFolder();
/*  69 */     a();
/*     */   }
/*     */   
/*     */   protected void onStart()
/*     */   {
/*  74 */     super.onStart();
/*     */     
/*  76 */     this.b = this.d.getXMLFiles();
/*  77 */     b();
/*  78 */     this.d.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/*  79 */     c();
/*     */   }
/*     */   
/*     */   private void a() {
/*  83 */     Button localButton1 = (Button)findViewById(R.id.files_holder).findViewById(R.id.info_btn);
/*  84 */     localButton1.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/*  87 */         AlertDialog.Builder localBuilder = FtcLoadFileActivity.a(FtcLoadFileActivity.this).buildBuilder("Available files", "These are the files the Hardware Wizard was able to find. You can edit each file by clicking the edit button. The 'Activate' button will set that file as the current configuration file, which will be used to start the robot.");
/*     */         
/*     */ 
/*     */ 
/*  91 */         localBuilder.setPositiveButton("Ok", FtcLoadFileActivity.this.a);
/*  92 */         AlertDialog localAlertDialog = localBuilder.create();
/*  93 */         localAlertDialog.show();
/*  94 */         TextView localTextView = (TextView)localAlertDialog.findViewById(16908299);
/*  95 */         localTextView.setTextSize(14.0F);
/*     */       }
/*     */       
/*  98 */     });
/*  99 */     Button localButton2 = (Button)findViewById(R.id.autoconfigure_holder).findViewById(R.id.info_btn);
/* 100 */     localButton2.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View view) {
/* 103 */         AlertDialog.Builder localBuilder = FtcLoadFileActivity.a(FtcLoadFileActivity.this).buildBuilder("AutoConfigure", "This is the fastest way to get a new machine up and running. The AutoConfigure tool will automatically enter some default names for devices. AutoConfigure expects certain devices.  If there are other devices attached, the AutoConfigure tool will not name them.");
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 108 */         localBuilder.setPositiveButton("Ok", FtcLoadFileActivity.this.a);
/* 109 */         AlertDialog localAlertDialog = localBuilder.create();
/* 110 */         localAlertDialog.show();
/* 111 */         TextView localTextView = (TextView)localAlertDialog.findViewById(16908299);
/* 112 */         localTextView.setTextSize(14.0F);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/* 117 */   DialogInterface.OnClickListener a = new DialogInterface.OnClickListener() {
/*     */     public void onClick(DialogInterface dialog, int button) {}
/*     */   };
/*     */   public static final String CONFIGURE_FILENAME = "CONFIGURE_FILENAME";
/*     */   
/*     */   private void b() {
/*     */     Object localObject;
/* 124 */     if (this.b.size() == 0) {
/* 125 */       localObject = "No files found!";
/* 126 */       String str = "In order to proceed, you must create a new file";
/* 127 */       this.d.setOrangeText((String)localObject, str, R.id.empty_filelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
/*     */     } else {
/* 129 */       localObject = (ViewGroup)findViewById(R.id.empty_filelist);
/* 130 */       ((ViewGroup)localObject).removeAllViews();
/* 131 */       ((ViewGroup)localObject).setVisibility(8);
/*     */     }
/*     */   }
/*     */   
/*     */   private void c() {
/* 136 */     ViewGroup localViewGroup = (ViewGroup)findViewById(R.id.inclusionlayout);
/* 137 */     localViewGroup.removeAllViews();
/* 138 */     for (String str : this.b) {
/* 139 */       View localView = LayoutInflater.from(this).inflate(R.layout.file_info, null);
/* 140 */       localViewGroup.addView(localView);
/* 141 */       TextView localTextView = (TextView)localView.findViewById(R.id.filename_editText);
/* 142 */       localTextView.setText(str);
/*     */     }
/*     */   }
/*     */   
/*     */   public void new_button(View v) {
/* 147 */     this.d.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 148 */     Intent localIntent = new Intent(this.c, FtcConfigurationActivity.class);
/* 149 */     startActivity(localIntent);
/*     */   }
/*     */   
/*     */   public void file_edit_button(View v) {
/* 153 */     String str = a(v, true);
/* 154 */     this.d.saveToPreferences(str, R.string.pref_hardware_config_filename);
/*     */     
/* 156 */     Intent localIntent = new Intent(this.c, FtcConfigurationActivity.class);
/* 157 */     startActivity(localIntent);
/*     */   }
/*     */   
/*     */   public void file_activate_button(View v) {
/* 161 */     String str = a(v, false);
/* 162 */     this.d.saveToPreferences(str, R.string.pref_hardware_config_filename);
/* 163 */     this.d.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/*     */   }
/*     */   
/*     */   public void file_delete_button(View v) {
/* 167 */     String str = a(v, true);
/* 168 */     File localFile = new File(Utility.CONFIG_FILES_DIR + str);
/*     */     
/* 170 */     if (localFile.exists()) {
/* 171 */       localFile.delete();
/*     */     } else {
/* 173 */       this.d.complainToast("That file does not exist: " + str, this.c);
/* 174 */       DbgLog.error("Tried to delete a file that does not exist: " + str);
/*     */     }
/*     */     
/* 177 */     this.b = this.d.getXMLFiles();
/* 178 */     this.d.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
/* 179 */     this.d.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
/* 180 */     c();
/*     */   }
/*     */   
/*     */   private String a(View paramView, boolean paramBoolean) {
/* 184 */     LinearLayout localLinearLayout1 = (LinearLayout)paramView.getParent();
/* 185 */     LinearLayout localLinearLayout2 = (LinearLayout)localLinearLayout1.getParent();
/* 186 */     TextView localTextView = (TextView)localLinearLayout2.findViewById(R.id.filename_editText);
/* 187 */     String str = localTextView.getText().toString();
/* 188 */     if (paramBoolean) {
/* 189 */       str = str + ".xml";
/*     */     }
/* 191 */     return str;
/*     */   }
/*     */   
/*     */   public void launch_autoConfigure(View v) {
/* 195 */     startActivity(new Intent(getBaseContext(), AutoConfigureActivity.class));
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBackPressed()
/*     */   {
/* 201 */     String str = this.d.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
/*     */     
/* 203 */     Intent localIntent = new Intent();
/* 204 */     localIntent.putExtra("CONFIGURE_FILENAME", str);
/* 205 */     setResult(-1, localIntent);
/* 206 */     finish();
/*     */   }
/*     */ }


/* Location:              C:\Users\SAAS Student\Documents\Robotics\2856\sketchy shit\FtcCommon-release\classes.zip!\com\qualcomm\ftccommon\configuration\FtcLoadFileActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */