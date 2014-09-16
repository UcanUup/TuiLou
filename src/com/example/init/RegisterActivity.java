package com.example.init;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;

public class RegisterActivity extends Activity {
	private EditText emailText;
	private EditText pwdText;
	private EditText cpwdText;
	private EditText nameText;
	
	private Button registerButton;
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register);
		
		emailText = (EditText)findViewById(R.id.email);
		pwdText = (EditText)findViewById(R.id.password);
		cpwdText = (EditText)findViewById(R.id.confirmPassword);
		nameText = (EditText)findViewById(R.id.nickname);
		
		backButton = (Button)findViewById(R.id.backButton1);
		//������ذ�ť
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		registerButton = (Button)findViewById(R.id.registerButton);
		//���ע�ᰴť�Ĳ���
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isSucceed = false;
				
				// ���ע�ᰴť��,���û�������תΪ�ַ���
				String email = emailText.getText().toString();
				String pwd = pwdText.getText().toString();
				String cpwd = cpwdText.getText().toString();
				String name = nameText.getText().toString();
				
				Intent intent = new Intent();
				
				// �����ֵ
				if (email.equals("") || pwd.equals("") || cpwd.equals("") || name.equals("")) {
					intent.putExtra("error", getString(R.string.null_value));
				}
				// ���벻һ��
				else if (!pwd.equals(cpwd)) {
					intent.putExtra("error", getString(R.string.password_differ));
				} else {
					// �����������ӵĲ���
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("em", email);
					params.put("pw", pwd);
					params.put("na", name);
				
					//���ӷ�����
					HttpLinker httpLinker = new HttpLinker();
					String result = httpLinker.link(params, HttpUrl.insert_user);
					
					//�û����Ѿ�����
					if (result.equals("exist"))
						intent.putExtra("error", getString(R.string.user_already_exist));
					else
						isSucceed = true;
				}
				
				//ע��ʧ��ʱ
				if (!isSucceed) {
					intent.setClass(RegisterActivity.this, FailHint.class);
				}
				//ע��ɹ�ʱ
				else {
					intent.setClass(RegisterActivity.this, RegisterSucceed.class);
				}
				startActivity(intent);
			}
		});
	}
	
}
