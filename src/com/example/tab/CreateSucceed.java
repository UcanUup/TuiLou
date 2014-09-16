package com.example.tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.R;

public class CreateSucceed extends Activity {
	
	private TextView code;
	
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_succeed);
		
		// �õ�������
		code = (TextView)findViewById(R.id.inviteCode);
		Intent intent = getIntent();
		code.setText(intent.getStringExtra("code"));
		
		backButton = (Button)findViewById(R.id.back);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
