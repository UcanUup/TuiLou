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
	
	private TextView group;
	private TextView time;
	private TextView title;
	private TextView content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.message_info);
		
		backButton = (Button)findViewById(R.id.backButton);
		group = (TextView)findViewById(R.id.Group);
		time = (TextView)findViewById(R.id.time);
		title = (TextView)findViewById(R.id.Title);
		content = (TextView)findViewById(R.id.Content);
		
		// 得到Intent对象
		Intent intent = getIntent();
		
		// 设置显示的内容
		group.setText(intent.getStringExtra("groupName"));
		time.setText(intent.getStringExtra("time"));
		title.setText(intent.getStringExtra("title"));
		content.setText(intent.getStringExtra("content"));
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
