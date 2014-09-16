package com.example.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.Group;
import com.example.utils.ParseMyGroup;
import com.example.utils.UserInfo;

public class MyGroup extends Fragment {
	private ExpandableListView groupListView;
	
	private HashMap<String, String> params;
	
	List<String> groups;
	List<List<Group>> childs;
	
	//ʹ��Handler���ȴ����߳���ɲ���
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			//�����ҵĴ����Ͳ���
			String[] myGroup = result.split("\\.");

			//�ҵĴ���
			UserInfo.myCreate = ParseMyGroup.parse(myGroup[0]);
			
			//�ҵĲ���
			UserInfo.myJoin = ParseMyGroup.parse(myGroup[1]);
			
			groups = new ArrayList<String>();
			childs = new ArrayList<List<Group>>();
			
			//ÿ��С����
			groups.add(getString(R.string.my_create));
			groups.add(getString(R.string.my_join));
			
			//�ҵĴ���������
			List<Group> child1 = new ArrayList<Group>();
			for (Iterator iterator = UserInfo.myCreate.iterator(); iterator.hasNext();) {
				Group group = (Group) iterator.next();
				child1.add(group);
			}
			
			//�ҵĲ��������
			List<Group> child2 = new ArrayList<Group>();
			for (Iterator iterator = UserInfo.myJoin.iterator(); iterator.hasNext();) {
				Group group = (Group) iterator.next();
				child2.add(group);
			}
			
			//����ÿ���������
			childs.add(child1);
			childs.add(child2);
			
			//����������
			groupListView.setGroupIndicator(null);
			groupListView.setAdapter(new GroupAdapter(getActivity(), groups, childs));
			groupListView.expandGroup(0);
		}
	};
	
	//ÿһ���л������ѡ��ʱ�������
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.group_expandable_list, container,
				false);
		
		groupListView = (ExpandableListView)rootView.findViewById(R.id.groupListView);
		
		// �����������ӵĲ���
		params = new HashMap<String, String>();
		params.put("em", UserInfo.email);
		
		//android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
		Thread thread = new Thread(new Runnable() {
			//���ӷ�����
			@Override
			public void run() {
				HttpLinker httpLinker = new HttpLinker();
				String result = httpLinker.link(params, HttpUrl.my_group);
				
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("result", result);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		});
		//�����߳�
		thread.start();
		
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
