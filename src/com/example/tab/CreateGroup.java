package com.example.tab;

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
import com.example.lib.Group;
import com.example.lib.HttpUrl;
import com.example.lib.UserInfo;
import com.example.utils.CustomProgressDialog;
import com.example.utils.HttpLinker;
import com.example.utils.Validation;

public class CreateGroup extends Fragment {
	
	private EditText groupText;
	private EditText remarkText;
	
	private Button createButton;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private String group;
	private String remark;
	
	// 使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			// 用户快速的关掉网络并点击按钮时，网络状态在一瞬间仍是可用
			if (result.equals("")) {
				// 关闭圆形进度条
				customProgressDialog.dismiss();
				
				// 网络连接不可用
				Toast.makeText(getActivity(), getString(R.string.network_error),
						Toast.LENGTH_SHORT).show();
				return ;
			}
			
			// 清空
			groupText.setText("");
			remarkText.setText("");
			
			// 关闭圆形进度条
			customProgressDialog.dismiss();
			
			// 小组已经存在
			if (result.equals("%exist%")) {
				Toast.makeText(getActivity(), getString(R.string.group_exist),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				// 成功后加入组到childs用于切换选项卡时快速加载
				if (Group.isUpdate) {
					Group g = new Group();
					g.setGname(group);
					g.setEmail(UserInfo.email);
					g.setRemark(remark);
					g.setCode(result);
					
					Group.child1.add(g);
				}
				
				// 页面跳转
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
		View rootView = inflater.inflate(R.layout.create_group, container,
				false);
		
		groupText = (EditText)rootView.findViewById(R.id.groupName);
		remarkText = (EditText)rootView.findViewById(R.id.remark);
		
		createButton = (Button)rootView.findViewById(R.id.createButton);
		createButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 点击按钮后,将用户的输入转为字符串
				group = groupText.getText().toString();
				remark = remarkText.getText().toString();
				
				// 输入空值
				if (group.equals("") || remark.equals("")) {
					Toast.makeText(getActivity(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				// 网络连接不可用
				else if (!Validation.isNetAvailable(getActivity())) {
					Toast.makeText(getActivity(), getString(R.string.network_error),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					// 显示圆形进度条
					customProgressDialog = new CustomProgressDialog(getActivity());
					customProgressDialog.show();
					
					// 设置请求链接的参数
					params = new HashMap<String, String>();
					params.put("gn", group);
					params.put("re", remark);
					params.put("em", UserInfo.email);
					
					// android 3.0以后规定要在新的线程执行网络访问等操作
					Thread thread = new Thread(new Runnable() {
						// 连接服务器
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
					// 启动线程
					thread.start();
				}
			}
		});
		
		return rootView;
	}

}
