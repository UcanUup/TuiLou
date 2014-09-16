package com.example.init;

import com.example.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {
	private Button loginButton;
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//添加到需要退出的Activity中
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.start);
		loginButton = (Button)findViewById(R.id.loginButton);
		registerButton = (Button)findViewById(R.id.registerButton);
		
		//跳转到登陆页面
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		//跳转到注册页面
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
