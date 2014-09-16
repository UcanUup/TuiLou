package com.example.init;

import com.example.R;
import com.example.lib.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterSucceed extends Activity {
	
	private Button login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 添加到需要退出的Activity中
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.register_succeed);
		
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisterSucceed.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
