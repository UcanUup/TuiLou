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
import com.example.sqlite.UserDatabase;
import com.example.utils.CustomProgressDialog;
import com.example.utils.UserInfo;
import com.example.utils.Validation;

public class LoginActivity extends Activity {
	
	private EditText emailText;
	private EditText pwdText;
	
	private Button loginButton;
	private Button registerButton;

	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private String email;
	
	// 使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			emailText.setText("");
			pwdText.setText("");
			
			// 关闭圆形进度条
			customProgressDialog.dismiss();
			
			// 用户名已经存在
			if (result.equals("%nothing%")) {
				Toast.makeText(getApplicationContext(), getString(R.string.user_no_exist),
					     Toast.LENGTH_SHORT).show();
			}
			// 密码错误
			else if (result.equals("%error%")) {
				Toast.makeText(getApplicationContext(), getString(R.string.password_error),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				UserInfo.email = email;
				UserInfo.userName = result;
				
				// 将用户信息存入数据库
				UserDatabase userDatabase = new UserDatabase(LoginActivity.this);
				userDatabase.write();
				
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 添加到需要退出的Activity中
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.login);
		
		emailText = (EditText)findViewById(R.id.email);
		pwdText = (EditText)findViewById(R.id.password);
		
		// 点击登陆按钮时
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				// 点击登陆按钮后,将用户的输入转为字符串
				email = emailText.getText().toString();
				String pwd = pwdText.getText().toString();
				
				// 输入空值
				if (email.equals("") || pwd.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				// 邮箱格式不正确
				else if (!Validation.isEmailValid(email)) {
					Toast.makeText(getApplicationContext(), getString(R.string.email_format),
						     Toast.LENGTH_SHORT).show();
				}
				// 网络连接不可用
				else if (!Validation.isNetAvailable(LoginActivity.this)) {
					Toast.makeText(getApplicationContext(), getString(R.string.network_error),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					// 显示圆形进度条
					customProgressDialog = new CustomProgressDialog(LoginActivity.this);
					customProgressDialog.show();
					
					// 设置请求链接的参数
					params = new HashMap<String, String>();
					params.put("em", email);
					params.put("pw", pwd);
					
					// android 3.0以后规定要在新的线程执行网络访问等操作
					Thread thread = new Thread(new Runnable() {
						// 连接服务器
						@Override
						public void run() {
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.user_login);
							
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
		
		// 点击注册按钮
		registerButton = (Button)findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
