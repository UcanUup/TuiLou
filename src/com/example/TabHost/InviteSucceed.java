package com.example.TabHost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.R;

public class InviteSucceed extends Activity {
	
	private TextView group;
	
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.invite_succeed);
		
		// …Ë÷√◊È√˚
		group = (TextView)findViewById(R.id.group);
		Intent intent = getIntent();
		group.setText(intent.getStringExtra("groupName"));
		
		backButton = (Button)findViewById(R.id.back);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
