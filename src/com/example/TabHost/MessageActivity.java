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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.R;
import com.example.adapter.MessageAdapter;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.MyMessage;
import com.example.utils.ParseMyMessage;
import com.example.utils.UserInfo;

public class MessageActivity extends Fragment {
	private ListView messageListView;
	
	private HashMap<String, String> params;
	
	//使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			UserInfo.myMessage = ParseMyMessage.parse(result);
			
			//设置设配器
			MessageAdapter messageAdapter = new MessageAdapter(getActivity());
			messageListView.setAdapter(messageAdapter);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.message, container,
				false);
		
		messageListView = (ListView)rootView.findViewById(R.id.messageListView);
		
		// 设置请求链接的参数
		params = new HashMap<String, String>();
		params.put("em", UserInfo.email);
		
		//android 3.0以后规定要在新的线程执行网络访问等操作
		Thread thread = new Thread(new Runnable() {
			//连接服务器
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
		//启动线程
		thread.start();
		
		//点击每一项时的触发事件
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
