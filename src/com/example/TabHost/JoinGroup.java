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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.CustomProgressDialog;
import com.example.utils.Group;
import com.example.utils.UserInfo;
import com.example.utils.Validation;

public class JoinGroup extends Fragment {
	private Button confirmButton;
	private EditText code;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private String groupCode;
	
	//使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			code.setText("");
			
			//关闭圆形进度条
			customProgressDialog.dismiss();
			
			// 邀请码不存在
			if (result.equals("%nothing%")) {
				Toast.makeText(getActivity(), getString(R.string.code_no_exist),
						Toast.LENGTH_SHORT).show();
			}
			else if (result.equals("%yourself%")) {
				Toast.makeText(getActivity(), getString(R.string.your_group),
						Toast.LENGTH_SHORT).show();
			}
			else if (result.equals("%already%")) {
				Toast.makeText(getActivity(), getString(R.string.already_add_group),
						Toast.LENGTH_SHORT).show();
			}
			else {
				String[] myGroup = result.split("\\^");
				
				//成功后加入组到childs用于切换选项卡时快速加载
				if (Group.isUpdate) {
					Group g = new Group();
					g.setGname(myGroup[0]);
					g.setEmail(myGroup[1]);
					g.setRemark(myGroup[2]);
					g.setCode(groupCode);
					
				    Group.child2.add(g);
				}
				
				Intent intent = new Intent();
				intent.putExtra("groupName", myGroup[0]);
				intent.setClass(getActivity(), InviteSucceed.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.add_group, container,
				false);
		
		confirmButton= (Button)rootView.findViewById(R.id.confirmButton);
		code = (EditText)rootView.findViewById(R.id.groupName);
		
		//确认按钮的监听器
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 点击登陆按钮后,将用户的输入转为字符串
			    groupCode = code.getText().toString();

				// 输入空值
				if (groupCode.equals("")) {
					Toast.makeText(getActivity(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				// 网络连接不可用
				else if (!Validation.isNetAvailable(getActivity())) {
					Toast.makeText(getActivity(), getString(R.string.network_error),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					//显示圆形进度条
					customProgressDialog = new CustomProgressDialog(getActivity());
					customProgressDialog.show();
					
					// 设置请求链接的参数
					params = new HashMap<String, String>();
					params.put("co", groupCode);
					params.put("em", UserInfo.email);
					
					//android 3.0以后规定要在新的线程执行网络访问等操作
					Thread thread = new Thread(new Runnable() {
						//连接服务器
						@Override
						public void run() {
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.join_group);
							
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
