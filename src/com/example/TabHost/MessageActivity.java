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
		
		//设置设配器
		MessageAdapter messageAdapter = new MessageAdapter(getActivity());
		messageListView.setAdapter(messageAdapter);
		
		//点击每一项时的触发事件
		messageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), MessageInfo.class);
				
				intent.putExtra("groupName", "高数");
				intent.putExtra("groupInfo", "明天要交1到2章的作业");
				
				startActivity(intent);
			}
			
		});
		
		return rootView;
	}

}
