package com.example.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
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
import android.widget.ExpandableListView.OnChildClickListener;

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
	
	//使用Handler来等待子线程完成操作
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			//划分我的创建和参与
			String[] myGroup = result.split("\\.");

			//我的创建
			UserInfo.myCreate = ParseMyGroup.parse(myGroup[0]);
			
			//我的参与
			UserInfo.myJoin = ParseMyGroup.parse(myGroup[1]);
			
			groups = new ArrayList<String>();
			childs = new ArrayList<List<Group>>();
			
			//每个小组名
			groups.add(getString(R.string.my_create));
			groups.add(getString(R.string.my_join));
			
			//我的创建的子项
			List<Group> child1 = new ArrayList<Group>();
			for (Iterator iterator = UserInfo.myCreate.iterator(); iterator.hasNext();) {
				Group group = (Group) iterator.next();
				child1.add(group);
			}
			
			//我的参与的子项
			List<Group> child2 = new ArrayList<Group>();
			for (Iterator iterator = UserInfo.myJoin.iterator(); iterator.hasNext();) {
				Group group = (Group) iterator.next();
				child2.add(group);
			}
			
			//加入每个组的子项
			childs.add(child1);
			childs.add(child2);
			
			//设置设配器
			groupListView.setGroupIndicator(null);
			groupListView.setAdapter(new GroupAdapter(getActivity(), groups, childs));
			groupListView.expandGroup(0);
		}
	};
	
	//每一次切换到这个选项时都会调用
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.group_expandable_list, container,
				false);
		
		groupListView = (ExpandableListView)rootView.findViewById(R.id.groupListView);
		
		// 设置请求链接的参数
		params = new HashMap<String, String>();
		params.put("em", UserInfo.email);
		
		//android 3.0以后规定要在新的线程执行网络访问等操作
		Thread thread = new Thread(new Runnable() {
			//连接服务器
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
		//启动线程
		thread.start();
		
		//点击每一项时的触发事件
		groupListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (groupPosition == 0) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), SendMessage.class);
					intent.putExtra("groupName", childs.get(groupPosition).get(childPosition).getGname());
					startActivity(intent);
				}
				
				return false;
			}
		});
		
		//长按时触发的事件
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
