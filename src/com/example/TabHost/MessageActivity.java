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

	// ʹ��Handler���ȴ����߳���ɲ���
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			// �������Ӳ�����
			if (msg.what == -1) {
				Toast.makeText(getActivity(), getString(R.string.network_error),
					     Toast.LENGTH_SHORT).show();
				return ;
			}
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			UserInfo.myMessage = ParseMyMessage.parse(result);
			
			// �ر�Բ�ν�����
			customProgressDialog.dismiss();
			
			// ����������
			MessageAdapter messageAdapter = new MessageAdapter(getActivity());
			messageListView.setAdapter(messageAdapter);
			
			times++;
		}
	};
	
	// ��һ�δ���ʱ
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// �������Ӳ�����
		if (!Validation.isNetAvailable(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.network_error),
				     Toast.LENGTH_SHORT).show();
		}
		else {
			// ��ʾԲ�ν�����
			customProgressDialog = new CustomProgressDialog(getActivity());
			customProgressDialog.show();
			
			// �����������ӵĲ���
			params = new HashMap<String, String>();
			params.put("em", UserInfo.email);
			
			// android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
			Thread thread = new Thread(new Runnable() {
				// ���ӷ�����
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
			// �����߳�
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
		
		// ���ǵ�һ�ν���ʱ
		if (times > 1) {
			// ����������
			MessageAdapter messageAdapter = new MessageAdapter(getActivity());
			messageListView.setAdapter(messageAdapter);
		}
		
		// ����ʱ�Ĳ���
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			
			// �����������߳��е��õģ� �������߳������к�ʱ������
			@Override
			public void onRefresh() {
				// �������Ӳ�����
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
						// �����������ӵĲ���
						params = new HashMap<String, String>();
						params.put("em", UserInfo.email);
						
						// ��������
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
				
				// ���ˢ��
				refreshableView.finishRefreshing();
			}
		}, 0);
		
		// ���ÿһ��ʱ�Ĵ����¼�
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
