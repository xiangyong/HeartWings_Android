package com.nwpu.heartwings.activities;

import java.util.HashMap;
import java.util.Map;


import com.nwpu.heartwings.R;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.HttpUtil;

import android.R.raw;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register3Activity extends ActionBarActivity {

	private EditText nameEditText;
	
	private EditText pwdEditText;
	private EditText pwd2EditText;
	private Button registerBtn;
	
	private ProgressDialog register_ProgressDialog;
	
	private RegisterHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register3);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
        getSupportActionBar().setHomeButtonEnabled(true);
        
        nameEditText = (EditText)findViewById(R.id.register_name);
        pwdEditText = (EditText)findViewById(R.id.pwd_one);
        pwd2EditText = (EditText)findViewById(R.id.pwd_two);
        
        registerBtn = (Button)findViewById(R.id.get_register);
        
        register_ProgressDialog = new ProgressDialog(this);
        register_ProgressDialog.setTitle(null);
        register_ProgressDialog.setMessage("����ע��...");
        register_ProgressDialog.setCancelable(true);
     
        handler = new RegisterHandler(register_ProgressDialog, this);
        
     final String phone = getIntent().getStringExtra(Register2Activity.INTENT_PHONE);
     
        registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				  if(TextUtils.isEmpty(pwdEditText.getText()) || TextUtils.isEmpty(pwd2EditText.getText())
						  || TextUtils.isEmpty(nameEditText.getText()))
				  {
					  DialogUtil.showDialog(Register3Activity.this, "����д����");
					  return;
				  }
				
				  if(!CheckNetWork.isNetAvailable(Register3Activity.this.getBaseContext()))
				  {
					  Toast.makeText(Register3Activity.this, CONSTANTS.NETWORKERR, Toast.LENGTH_SHORT).show();
					  return;
				  }
				  
				  final String pwd1 = pwdEditText.getText().toString();
				  final String pwd2 = pwd2EditText.getText().toString();
				  final String name = nameEditText.getText().toString();
				  
				  if(name.length() < 6){
					  
					  DialogUtil.showDialog(Register3Activity.this, "�û�����3λ�ַ�����");
					  return;
				  }
				  if(!pwd1.equals(pwd2))
				  {
					  DialogUtil.showDialog(Register3Activity.this, "�������벻һ��");
					  pwdEditText.setText("");
					  pwd2EditText.setText("");
					  return;
				  }
				  
				  if(pwd1.length() < 6 || pwd2.length() < 6)
				  {
        	           DialogUtil.showDialog(Register3Activity.this, "���볤��������6λ");
        	           pwdEditText.setText("");
 					  pwd2EditText.setText("");
        	           return;
				  }
				  
				  register_ProgressDialog.show();
				  
				  new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						Map<String, String> rawParams = new HashMap<String, String>();
						rawParams.put("phone", phone);
						rawParams.put("pwd", pwd1);
						rawParams.put("name", name);
						
						String result = HttpUtil.postRequest(HttpUtil.BASEURL + "guardianRegister", rawParams);
						
						Message message = new Message();
						message.what = 0x123;
						message.obj = result;
						
						handler.sendMessage(message);
						
					}
				}).start();
			}
		});

	}

	static class RegisterHandler extends Handler{
		
		    private ProgressDialog mProgressDialog;
		    private Context mContext;
		    
			public RegisterHandler(ProgressDialog mProgressDialog, Context mContext) {
				super();
				this.mProgressDialog = mProgressDialog;
				this.mContext = mContext;
			}

			@Override
			public void handleMessage(Message msg) {
			
				if(msg.what == 0x123)
				{
					 try {
						Thread.sleep(1250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					 
					 mProgressDialog.dismiss();
					 if(msg.obj.toString().equals(CONSTANTS.LOGIN_ERR_TAG))
					 {
						 DialogUtil.showDialog(mContext, "����������Ժ�����");
					 }else {
						
						 // ǰ����¼��
						 Intent intent = new Intent(mContext,LoginActivity.class);
						 mContext.startActivity(intent);
						 
					}
					 
				}
				  
			}
		    
		    
	}

}
