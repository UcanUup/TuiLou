package com.example.TabHost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.R;
import com.example.adapter.GroupAdapter;

public class MyGroup extends Fragment {
	private ListView groupListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.my_group, container,
				false);
		
		groupListView = (ListView)rootView.findViewById(R.id.groupListView);
		
		//����������
		GroupAdapter groupAdapter = new GroupAdapter(getActivity());
		groupListView.setAdapter(groupAdapter);
		
		//���ÿһ��ʱ�Ĵ����¼�
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
			
		});
		
		//����ʱ�������¼�
		groupListView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		return rootView;
	}

}
