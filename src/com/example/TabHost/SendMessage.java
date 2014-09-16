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
import com.example.utils.Validation;

public class SendMessage extends Activity {
	
	private TextView gname;
	private EditText titleText;
	private EditText contentText;
	private Button back;
	private Button send;
	
	private String groupName;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	// 使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			titleText.setText("");
			contentText.setText("");
			
			// 关闭圆形进度条
			customProgressDialog.dismiss();
			
			// 用户名已经存在
			if (result.equals("%success%")) {
				Toast.makeText(SendMessage.this, getString(R.string.send_message_succeed),
					     Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.send_message);
		
		gname = (TextView)findViewById(R.id.myGroup);
		titleText = (EditText)findViewById(R.id.title);
		contentText = (EditText)findViewById(R.id.content);
		back = (Button)findViewById(R.id.back);
		send = (Button)findViewById(R.id.send);
		
		// 设置组名
		Intent intent = getIntent();
		groupName = intent.getStringExtra("groupName");
		gname.setText(groupName);
		
		// 返回
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// 发送消息
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 点击登陆按钮后,将用户的输入转为字符串
				String title = titleText.getText().toString();
				String content = contentText.getText().toString();
				
				// 输入空值
				if (title.equals("") || content.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				// 网络连接不可用
				else if (!Validation.isNetAvailable(SendMessage.this)) {
					Toast.makeText(getApplicationContext(), getString(R.string.network_error),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					// 显示圆形进度条
					customProgressDialog = new CustomProgressDialog(SendMessage.this);
					customProgressDialog.show();
					
					// 设置请求链接的参数
					params = new HashMap<String, String>();
					params.put("gn", groupName);
					params.put("em", UserInfo.email);
					params.put("ti", title);
					params.put("co", content);
					
					// android 3.0以后规定要在新的线程执行网络访问等操作
					Thread thread = new Thread(new Runnable() {
						// 连接服务器
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
					// 启动线程
					thread.start();
				}
			}
		});
	}
	
}
