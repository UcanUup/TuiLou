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
		
		//添加到需要退出的Activity中
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.login);
		
		emailText = (EditText)findViewById(R.id.email);
		pwdText = (EditText)findViewById(R.id.password);
		
		//点击登陆按钮时
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isSucceed = false;
				
				// 点击登陆按钮后,将用户的输入转为字符串
				String email = emailText.getText().toString();
				String pwd = pwdText.getText().toString();
				
				Intent intent = new Intent();
				
				// 输入空值
				if (email.equals("") || pwd.equals("")) {
					intent.putExtra("error", getString(R.string.null_value));
				}
				else {
					// 设置请求链接的参数
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("em", email);
					params.put("pw", pwd);
					
					//连接服务器
					HttpLinker httpLinker = new HttpLinker();
					String result = httpLinker.link(params, HttpUrl.user_login);
					
					//用户名已经存在
					if (result.equals("nothing"))
						intent.putExtra("error", getString(R.string.user_no_exist));
					else if (result.equals("error"))
						intent.putExtra("error", getString(R.string.password_error));
					else 
						isSucceed = true;
				}
				
				//登陆失败时
				if (!isSucceed) {
					intent.setClass(LoginActivity.this, FailHint.class);
				}
				//登陆成功时
				else {
					intent.setClass(LoginActivity.this, HomeActivity.class);
				}
    			startActivity(intent);
			}
		});
	}
	
}
