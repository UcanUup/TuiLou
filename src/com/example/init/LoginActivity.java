package com.example.init;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;

public class LoginActivity extends Activity {
	private EditText emailText;
	private EditText pwdText;
	
	private Button loginButton;
	
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
				// TODO Auto-generated method stub
				boolean isSucceed = false;
				
				// �����½��ť��,���û�������תΪ�ַ���
				String email = emailText.getText().toString();
				String pwd = pwdText.getText().toString();
				
				Intent intent = new Intent();
				
				// �����ֵ
				if (email.equals("") || pwd.equals("")) {
					intent.putExtra("error", getString(R.string.null_value));
				}
				else {
					// �����������ӵĲ���
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("em", email);
					params.put("pw", pwd);
					
					//���ӷ�����
					HttpLinker httpLinker = new HttpLinker();
					String result = httpLinker.link(params, HttpUrl.user_login);
					
					//�û����Ѿ�����
					if (result.equals("nothing"))
						intent.putExtra("error", getString(R.string.user_no_exist));
					else if (result.equals("error"))
						intent.putExtra("error", getString(R.string.password_error));
					else 
						isSucceed = true;
				}
				
				//��½ʧ��ʱ
				if (!isSucceed) {
					intent.setClass(LoginActivity.this, FailHint.class);
				}
				//��½�ɹ�ʱ
				else {
					intent.setClass(LoginActivity.this, HomeActivity.class);
				}
    			startActivity(intent);
			}
		});
	}
	
}
