package com.example.TabHost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.R;
import com.example.adapter.MessageAdapter;

public class MessageActivity extends Fragment {
	private ListView messageListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.message, container,
				false);
		
		messageListView = (ListView)rootView.findViewById(R.id.messageListView);
		
		//����������
		MessageAdapter messageAdapter = new MessageAdapter(getActivity());
		messageListView.setAdapter(messageAdapter);
		
		//���ÿһ��ʱ�Ĵ����¼�
		messageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), MessageInfo.class);
				
				intent.putExtra("groupName", "����");
				intent.putExtra("groupInfo", "����Ҫ��1��2�µ���ҵ");
				
				startActivity(intent);
			}
			
		});
		
		return rootView;
	}

}
