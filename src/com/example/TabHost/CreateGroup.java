package com.example.TabHost;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.init.HomeActivity;
import com.example.init.LoginActivity;
import com.example.init.UserInfo;
import com.example.utils.CustomProgressDialog;

public class CreateGroup extends Fragment {
	private EditText groupText;
	private EditText remarkText;
	
	private Button createButton;
	
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
				Toast.makeText(getActivity(), getString(R.string.group_exist),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				Intent intent = new Intent();
				intent.putExtra("code", result);
				intent.setClass(getActivity(), CreateSucceed.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.create_group, container,
				false);
		
		groupText = (EditText)rootView.findViewById(R.id.groupName);
		remarkText = (EditText)rootView.findViewById(R.id.remark);
		
		createButton = (Button)rootView.findViewById(R.id.createButton);
		createButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 点击登陆按钮后,将用户的输入转为字符串
				String group = groupText.getText().toString();
				String remark = remarkText.getText().toString();
				
				// 输入空值
				if (group.equals("") || remark.equals("")) {
					Toast.makeText(getActivity(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					//显示圆形进度条
					customProgressDialog = new CustomProgressDialog(getActivity());
					customProgressDialog.show();
					
					// 设置请求链接的参数
					params = new HashMap<String, String>();
					params.put("gr", group);
					params.put("re", remark);
					params.put("em", UserInfo.email);
					params.put("na", UserInfo.userName);
					
					//android 3.0以后规定要在新的线程执行网络访问等操作
					Thread thread = new Thread(new Runnable() {
						//连接服务器
						@Override
						public void run() {
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.insert_group);
							
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
		
		return rootView;
	}

}
