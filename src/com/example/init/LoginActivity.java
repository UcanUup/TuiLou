package com.example.init;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.CustomProgressDialog;
import com.example.utils.UserInfo;
import com.example.utils.Validation;

public class LoginActivity extends Activity {
	private EditText emailText;
	private EditText pwdText;
	
	private Button loginButton;
	private Button registerButton;

	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private String email;
	
	//ʹ��Handler���ȴ����߳���ɲ���
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			emailText.setText("");
			pwdText.setText("");
			
			//�ر�Բ�ν�����
			customProgressDialog.dismiss();
			
			//�û����Ѿ�����
			if (result.equals("%nothing%")) {
				Toast.makeText(getApplicationContext(), getString(R.string.user_no_exist),
					     Toast.LENGTH_SHORT).show();
			}
			else if (result.equals("%error%")) {
				Toast.makeText(getApplicationContext(), getString(R.string.password_error),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				UserInfo.email = email;
				
				Intent intent = new Intent();
				intent.putExtra("userName", result);
				intent.setClass(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//��ӵ���Ҫ�˳���Activity��
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.login);
		
		emailText = (EditText)findViewById(R.id.email);
		pwdText = (EditText)findViewById(R.id.password);
		
		//�����½��ťʱ
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				// �����½��ť��,���û�������תΪ�ַ���
				email = emailText.getText().toString();
				String pwd = pwdText.getText().toString();
				
				// �����ֵ
				if (email.equals("") || pwd.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				//�����ʽ����ȷ
				else if (!Validation.isEmailValid(email)) {
					Toast.makeText(getApplicationContext(), getString(R.string.email_format),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					//��ʾԲ�ν�����
					customProgressDialog = new CustomProgressDialog(LoginActivity.this);
					customProgressDialog.show();
					
					// �����������ӵĲ���
					params = new HashMap<String, String>();
					params.put("em", email);
					params.put("pw", pwd);
					
					//android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
					Thread thread = new Thread(new Runnable() {
						//���ӷ�����
						@Override
						public void run() {
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.user_login);
							
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("result", result);
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					});
					//�����߳�
					thread.start();
				}
			}
		});
		
		//���ע�ᰴť
		registerButton = (Button)findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
