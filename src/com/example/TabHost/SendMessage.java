package com.example.TabHost;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.CustomProgressDialog;
import com.example.utils.UserInfo;

public class SendMessage extends Activity {
	private TextView gname;
	private EditText titleText;
	private EditText contentText;
	private Button back;
	private Button send;
	
	private String groupName;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	//ʹ��Handler���ȴ����߳���ɲ���
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			//�ر�Բ�ν�����
			customProgressDialog.dismiss();
			
			//�û����Ѿ�����
			if (result.equals("%success%")) {
				Toast.makeText(SendMessage.this, getString(R.string.send_message_succeed),
					     Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.send_message);
		
		gname = (TextView)findViewById(R.id.myGroup);
		titleText = (EditText)findViewById(R.id.editText1);
		contentText = (EditText)findViewById(R.id.editText2);
		back = (Button)findViewById(R.id.button1);
		send = (Button)findViewById(R.id.button2);
		
		//��������
		Intent intent = getIntent();
		groupName = intent.getStringExtra("groupName");
		gname.setText(groupName);
		
		//����
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		//������Ϣ
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// �����½��ť��,���û�������תΪ�ַ���
				String title = titleText.getText().toString();
				String content = contentText.getText().toString();
				
				// �����ֵ
				if (title.equals("") || content.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					//��ʾԲ�ν�����
					customProgressDialog = new CustomProgressDialog(SendMessage.this);
					customProgressDialog.show();
					
					// �����������ӵĲ���
					params = new HashMap<String, String>();
					params.put("gn", groupName);
					params.put("em", UserInfo.email);
					params.put("ti", title);
					params.put("co", content);
					
					//android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
					Thread thread = new Thread(new Runnable() {
						//���ӷ�����
						@Override
						public void run() {
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.send_message);
							
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("result", result);
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					});
					//�����߳�
					thread.start();
				}
			}
		});
	}
	
}
