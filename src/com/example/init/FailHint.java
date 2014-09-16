package com.example.init;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.R;

public class FailHint extends Activity {
	private TextView hint;
	private Button back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register_fail);
		
		//得到错误提示的内容
		Intent intent = getIntent();
		String errorString = intent.getStringExtra("error");
		
		//设置错误内容
		hint = (TextView)findViewById(R.id.hint);
		hint.setText(errorString);
		
		back = (Button)findViewById(R.id.login);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
}
