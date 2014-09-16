package com.example.TabHost;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.R;
import com.example.adapter.MessageAdapter;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.CustomProgressDialog;
import com.example.utils.MyMessage;
import com.example.utils.ParseMyMessage;
import com.example.utils.RefreshableView;
import com.example.utils.Validation;
import com.example.utils.RefreshableView.PullToRefreshListener;
import com.example.utils.UserInfo;

public class MessageActivity extends Fragment {
	
	private RefreshableView refreshableView;
	private ListView messageListView;
	
	private int times = 1;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;

	// 使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			// 网络连接不可用
			if (msg.what == -1) {
				Toast.makeText(getActivity(), getString(R.string.network_error),
					     Toast.LENGTH_SHORT).show();
				return ;
			}
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			UserInfo.myMessage = ParseMyMessage.parse(result);
			
			// 关闭圆形进度条
			customProgressDialog.dismiss();
			
			// 设置适配器
			MessageAdapter messageAdapter = new MessageAdapter(getActivity());
			messageListView.setAdapter(messageAdapter);
			
			times++;
		}
	};
	
	// 第一次创建时
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 网络连接不可用
		if (!Validation.isNetAvailable(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.network_error),
				     Toast.LENGTH_SHORT).show();
		}
		else {
			// 显示圆形进度条
			customProgressDialog = new CustomProgressDialog(getActivity());
			customProgressDialog.show();
			
			// 设置请求链接的参数
			params = new HashMap<String, String>();
			params.put("em", UserInfo.email);
			
			// android 3.0以后规定要在新的线程执行网络访问等操作
			Thread thread = new Thread(new Runnable() {
				// 连接服务器
				@Override
				public void run() {
					HttpLinker httpLinker = new HttpLinker();
					String result = httpLinker.link(params, HttpUrl.get_message);
					
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.message, container,
				false);
	
		refreshableView = (RefreshableView)rootView.findViewById(R.id.refreshable_view);
		messageListView = (ListView)rootView.findViewById(R.id.messageListView);
		
		// 不是第一次进入时
		if (times > 1) {
			// 设置适配器
			MessageAdapter messageAdapter = new MessageAdapter(getActivity());
			messageListView.setAdapter(messageAdapter);
		}
		
		// 下拉时的操作
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			
			// 方法是在子线程中调用的， 不用另开线程来进行耗时操作。
			@Override
			public void onRefresh() {
				// 网络连接不可用
				if (!Validation.isNetAvailable(getActivity())) {
					Message msg = new Message();
					msg.what = -1;
					handler.sendMessage(msg);
					
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						// 设置请求链接的参数
						params = new HashMap<String, String>();
						params.put("em", UserInfo.email);
						
						// 进行链接
						HttpLinker httpLinker = new HttpLinker();
						String result = httpLinker.link(params, HttpUrl.get_message);
						
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("result", result);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// 完成刷新
				refreshableView.finishRefreshing();
			}
		}, 0);
		
		// 点击每一项时的触发事件
		messageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), MessageInfo.class);
				
				MyMessage message = UserInfo.myMessage.get(position);
				
				intent.putExtra("groupName", message.getGname());
				intent.putExtra("time", message.getTime());
				intent.putExtra("title", message.getTitle());
				intent.putExtra("content", message.getContent());
				
				startActivity(intent);
			}
			
		});
		
		return rootView;
	}

}
