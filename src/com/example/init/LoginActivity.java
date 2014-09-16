package com.example.init;

import com.example.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {
	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//添加到需要退出的Activity中
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.login);
		
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
    			intent.setClass(LoginActivity.this, HomeActivity.class);
    			startActivity(intent);
			}
		});
	}
	
}
