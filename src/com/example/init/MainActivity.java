package com.example.init;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.example.R;
import com.example.lib.UserInfo;
import com.example.sqlite.UserDatabase;

public class MainActivity extends Activity {

	// 设置启动画面延迟的时间
	private final static int DISPLAY_LENGTH = 1300; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 一个Handler用于处理延迟事件，控制打开应用画面的时间
		new Handler().postDelayed(new Runnable() {  
            public void run() {  
            	// 清除数据库内容
				UserDatabase userDatabase = new UserDatabase(MainActivity.this);
				userDatabase.read();
				
				// 之前已经登陆过，则跳转到主页
				if (UserInfo.email != null && UserInfo.userName != null) {
					Intent intent = new Intent();
	    			intent.setClass(MainActivity.this, HomeActivity.class);
	    			startActivity(intent);
				}
				// 之前未登录过，则跳转到登陆页面
				else {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, LoginActivity.class);
					startActivity(intent);
				}
                finish();  
            }  
  
        }, DISPLAY_LENGTH);  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
