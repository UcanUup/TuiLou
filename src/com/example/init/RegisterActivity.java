package com.example.init;

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
import android.widget.Toast;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.CustomProgressDialog;

public class RegisterActivity extends Activity {
	private EditText emailText;
	private EditText pwdText;
	private EditText cpwdText;
	private EditText nameText;
	
	private Button registerButton;
	private Button backButton;

	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	//使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			//关闭圆形进度条
			customProgressDialog.dismiss();
			
			//用户名已经存在
			if (result.equals("%exist%")) {
				Toast.makeText(getApplicationContext(), getString(R.string.user_already_exist),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, RegisterSucceed.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.register);
		
		emailText = (EditText)findViewById(R.id.email);
		pwdText = (EditText)findViewById(R.id.password);
		cpwdText = (EditText)findViewById(R.id.confirmPassword);
		nameText = (EditText)findViewById(R.id.nickname);
		
		backButton = (Button)findViewById(R.id.backButton1);
		//点击返回按钮
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
				
		registerButton = (Button)findViewById(R.id.registerButton);
		//点击注册按钮的操作
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 点击注册按钮后,将用户的输入转为字符串
				String email = emailText.getText().toString();
				String pwd = pwdText.getText().toString();
				String cpwd = cpwdText.getText().toString();
				String name = nameText.getText().toString();
				
				// 输入空值
				if (email.equals("") || pwd.equals("") || cpwd.equals("") || name.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				// 密码不一致
				else if (!pwd.equals(cpwd)) {
					Toast.makeText(getApplicationContext(), getString(R.string.password_differ),
						     Toast.LENGTH_SHORT).show();
				} else {
					//显示圆形进度条
					customProgressDialog = new CustomProgressDialog(RegisterActivity.this);
					customProgressDialog.show();
					
					// 设置请求链接的参数
					params = new HashMap<String, String>();
					params.put("em", email);
					params.put("pw", pwd);
					params.put("na", name);
				
					//android 3.0以后规定要在新的线程执行网络访问等操作
					Thread thread = new Thread(new Runnable() {
						//连接服务器
						@Override
						public void run() {
							// TODO Auto-generated method stub
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.insert_user);
							
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("result", result);
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					});
					//启动线程
					thread.start();
				}
			}
		});
	}
	
}
