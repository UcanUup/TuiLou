package com.example.TabHost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.R;

public class MessageInfo extends Activity {
	private Button backButton;
	private TextView Title;
	private TextView Info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.message_info);
		
		//得到Intent对象
		Intent intent = getIntent();
		String groupName = intent.getStringExtra("groupName");
		String groupInfo = intent.getStringExtra("groupInfo");
		
		backButton = (Button)findViewById(R.id.backButton);
		Title = (TextView)findViewById(R.id.textView1);
		Info = (TextView)findViewById(R.id.textView2);
		
		Title.setText(groupName);
		Info.setText(groupInfo);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
