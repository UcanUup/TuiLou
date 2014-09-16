package com.example.init;

import com.example.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterActivity extends Activity {
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register);
		
		registerButton = (Button)findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//×¢²áÊ§°ÜÊ±
				if (true) {
					Intent intent = new Intent();
					intent.setClass(RegisterActivity.this, RegisterFail.class);
					startActivity(intent);
				}
			}
		});
	}
	
}
