package com.example.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.example.R;
import com.example.adapter.GroupAdapter;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.CustomProgressDialog;
import com.example.utils.Group;
import com.example.utils.ParseMyGroup;
import com.example.utils.UserInfo;
import com.example.utils.Validation;

public class MyGroup extends Fragment {
	
	private ExpandableListView groupListView;
	
	private int times = 1;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private int groupPosition;
	private int childPosition;
	
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
			
			// 用于刷新小组时
			if (msg.what == 1) {
				// 划分我的创建和参与
				String[] myGroup = result.split("\\^");
	
				// 我的创建
				UserInfo.myCreate = ParseMyGroup.parse(myGroup[0]);
				
				// 我的参与
				UserInfo.myJoin = ParseMyGroup.parse(myGroup[1]);
				
				Group.groups = new ArrayList<String>();
				Group.childs = new ArrayList<List<Group>>();
				Group.child1 = new ArrayList<Group>();
				Group.child2 = new ArrayList<Group>();
				
				// 每个小组名
				Group.groups.add(getString(R.string.my_create));
				Group.groups.add(getString(R.string.my_join));
				
				// 我的创建的子项
				Group.child1 = new ArrayList<Group>();
				for (Iterator<Group> iterator = UserInfo.myCreate.iterator(); iterator.hasNext();) {
					Group group = (Group) iterator.next();
					Group.child1.add(group);
				}
				
				// 我的参与的子项
				Group.child2 = new ArrayList<Group>();
				for (Iterator<Group> iterator = UserInfo.myJoin.iterator(); iterator.hasNext();) {
					Group group = (Group) iterator.next();
					Group.child2.add(group);
				}
				
				// 加入每个组的子项
				Group.childs.add(Group.child1);
				Group.childs.add(Group.child2);
				
				// 关闭圆形进度条
				customProgressDialog.dismiss();
				
				// 设置适配器
				groupListView.setGroupIndicator(null);
				groupListView.setAdapter(new GroupAdapter(getActivity(), Group.groups, Group.childs));
				groupListView.expandGroup(0);
				groupListView.expandGroup(1);
				
				Group.isUpdate = true;
				times++;
			}
			// 用于删除小组时
			else if (msg.what == 2 || msg.what == 3) {
				// 关闭圆形进度条
				customProgressDialog.dismiss();
				
				// 设置适配器
				groupListView.setGroupIndicator(null);
				groupListView.setAdapter(new GroupAdapter(getActivity(), Group.groups, Group.childs));
				groupListView.expandGroup(0);
				groupListView.expandGroup(1);
			}
		}
	};
	
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
					String result = httpLinker.link(params, HttpUrl.my_group);
					
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("result", result);
					msg.setData(bundle);
					msg.what = 1;
					handler.sendMessage(msg);
				}
			});
			// 启动线程
			thread.start();
		}
	}
	
	// 每一次切换到这个选项时都会调用
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.group_expandable_list, container,
				false);
		
		groupListView = (ExpandableListView)rootView.findViewById(R.id.groupListView);
		registerForContextMenu(rootView);
		
		// 不是第一次进入时
		if (times > 1) {
			// 加入每个组的子项
			Group.childs = new ArrayList<List<Group>>();
			Group.childs.add(Group.child1);
			Group.childs.add(Group.child2);
			
			// 设置适配器
			groupListView.setGroupIndicator(null);
			groupListView.setAdapter(new GroupAdapter(getActivity(), Group.groups, Group.childs));
			groupListView.expandGroup(0);
			groupListView.expandGroup(1);
		}
		
		// 点击每一项时的触发事件
		groupListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (groupPosition == 0) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), SendMessage.class);
					intent.putExtra("groupName", Group.childs.get(groupPosition).get(childPosition).getGname());
					startActivity(intent);
				}
				
				return false;
			}
		});
		
		// 长按时触发的事件
		groupListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
				{
					// 得到位置
					long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
					groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
					childPosition = ExpandableListView.getPackedPositionChild(packedPos);

					if (groupPosition == 0) {
						// 弹出对话框
						new AlertDialog.Builder(getActivity())   
						.setTitle(getString(R.string.confirm))  
						.setMessage(getString(R.string.confirm_delete) + "?")  
						.setPositiveButton(getString(R.string.yes), new DeleteOnClickListener())  
						.setNegativeButton(getString(R.string.no), null)  
						.show();  
					}
					else if (groupPosition == 1) {
						// 弹出对话框
						new AlertDialog.Builder(getActivity())   
						.setTitle(getString(R.string.confirm))  
						.setMessage(getString(R.string.exit_group) + "?")  
						.setPositiveButton(getString(R.string.yes), new ExitOnClickListener())  
						.setNegativeButton(getString(R.string.no), null)  
						.show();  
					}
					
					return true;
				}
				
				return false;
			}
			
		});
		
		return rootView;
	}
	
	// 点击确认删除按钮
	public class DeleteOnClickListener implements OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Group group = Group.childs.get(groupPosition).get(childPosition);
			
			// 网络连接不可用
			if (!Validation.isNetAvailable(getActivity())) {
				Toast.makeText(getActivity(), getString(R.string.network_error),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				// 从本地移除这个组
				UserInfo.myCreate.remove(group);
				Group.childs.get(groupPosition).remove(childPosition);
				
				// 显示圆形进度条
				customProgressDialog = new CustomProgressDialog(getActivity());
				customProgressDialog.show();
				
				// 设置请求链接的参数
				params = new HashMap<String, String>();
				params.put("gn", group.getGname());
				
				// android 3.0以后规定要在新的线程执行网络访问等操作
				Thread thread = new Thread(new Runnable() {
					// 连接服务器
					@Override
					public void run() {
						HttpLinker httpLinker = new HttpLinker();
						String result = httpLinker.link(params, HttpUrl.delete_group);
						
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("result", result);
						msg.setData(bundle);
						msg.what = 2;
						handler.sendMessage(msg);
					}
				});
				// 启动线程
				thread.start();
			}
		}
		
	}
	
	// 点击确认退出按钮
	public class ExitOnClickListener implements OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Group group = Group.childs.get(groupPosition).get(childPosition);
			
			// 网络连接不可用
			if (!Validation.isNetAvailable(getActivity())) {
				Toast.makeText(getActivity(), getString(R.string.network_error),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				// 从本地移除这个组
				UserInfo.myJoin.remove(group);
				Group.childs.get(groupPosition).remove(childPosition);
				
				// 显示圆形进度条
				customProgressDialog = new CustomProgressDialog(getActivity());
				customProgressDialog.show();
				
				// 设置请求链接的参数
				params = new HashMap<String, String>();
				params.put("gn", group.getGname());
				params.put("em", UserInfo.email);
				
				// android 3.0以后规定要在新的线程执行网络访问等操作
				Thread thread = new Thread(new Runnable() {
					// 连接服务器
					@Override
					public void run() {
						HttpLinker httpLinker = new HttpLinker();
						String result = httpLinker.link(params, HttpUrl.exit_group);
						
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("result", result);
						msg.setData(bundle);
						msg.what = 3;
						handler.sendMessage(msg);
					}
				});
				// 启动线程
				thread.start();
			}
		}	
	}
	
}
