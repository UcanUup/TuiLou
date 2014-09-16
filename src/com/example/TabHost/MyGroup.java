package com.example.TabHost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;

import com.example.R;
import com.example.adapter.GroupAdapter;

public class MyGroup extends Fragment {
	private ExpandableListView groupListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.group_expandable_list, container,
				false);
		
		groupListView = (ExpandableListView)rootView.findViewById(R.id.groupListView);
		
		//����������
		groupListView.setGroupIndicator(null);
		groupListView.setAdapter(new GroupAdapter(getActivity()));
		groupListView.expandGroup(0);
		
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
