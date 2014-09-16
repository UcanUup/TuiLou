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

public class MyGroup extends Fragment {
	private ExpandableListView groupListView;
	
	private int times = 1;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private int groupPosition;
	private int childPosition;
	
	//ʹ��Handler���ȴ����߳���ɲ���
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			//����ˢ��С��ʱ
			if (msg.what == 1) {
				//�����ҵĴ����Ͳ���
				String[] myGroup = result.split("\\^");
	
				//�ҵĴ���
				UserInfo.myCreate = ParseMyGroup.parse(myGroup[0]);
				
				//�ҵĲ���
				UserInfo.myJoin = ParseMyGroup.parse(myGroup[1]);
				
				Group.groups = new ArrayList<String>();
				Group.childs = new ArrayList<List<Group>>();
				Group.child1 = new ArrayList<Group>();
				Group.child2 = new ArrayList<Group>();
				
				//ÿ��С����
				Group.groups.add(getString(R.string.my_create));
				Group.groups.add(getString(R.string.my_join));
				
				//�ҵĴ���������
				Group.child1 = new ArrayList<Group>();
				for (Iterator<Group> iterator = UserInfo.myCreate.iterator(); iterator.hasNext();) {
					Group group = (Group) iterator.next();
					Group.child1.add(group);
				}
				
				//�ҵĲ��������
				Group.child2 = new ArrayList<Group>();
				for (Iterator<Group> iterator = UserInfo.myJoin.iterator(); iterator.hasNext();) {
					Group group = (Group) iterator.next();
					Group.child2.add(group);
				}
				
				//����ÿ���������
				Group.childs.add(Group.child1);
				Group.childs.add(Group.child2);
				
				//�ر�Բ�ν�����
				customProgressDialog.dismiss();
				
				//����������
				groupListView.setGroupIndicator(null);
				groupListView.setAdapter(new GroupAdapter(getActivity(), Group.groups, Group.childs));
				groupListView.expandGroup(0);
				groupListView.expandGroup(1);
				
				Group.isUpdate = true;
				times++;
			}
			//����ɾ��С��ʱ
			else if (msg.what == 2 || msg.what == 3) {
				//�ر�Բ�ν�����
				customProgressDialog.dismiss();
				
				//����������
				groupListView.setGroupIndicator(null);
				groupListView.setAdapter(new GroupAdapter(getActivity(), Group.groups, Group.childs));
				groupListView.expandGroup(0);
				groupListView.expandGroup(1);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//��ʾԲ�ν�����
		customProgressDialog = new CustomProgressDialog(getActivity());
		customProgressDialog.show();
		
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
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
		//�����߳�
		thread.start();
	}
	
	//ÿһ���л������ѡ��ʱ�������
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.group_expandable_list, container,
				false);
		
		groupListView = (ExpandableListView)rootView.findViewById(R.id.groupListView);
		registerForContextMenu(rootView);
		
		//���ǵ�һ�ν���ʱ
		if (times > 1) {
			//����ÿ���������
			Group.childs = new ArrayList<List<Group>>();
			Group.childs.add(Group.child1);
			Group.childs.add(Group.child2);
			
			//����������
			groupListView.setGroupIndicator(null);
			groupListView.setAdapter(new GroupAdapter(getActivity(), Group.groups, Group.childs));
			groupListView.expandGroup(0);
			groupListView.expandGroup(1);
		}
		
		//���ÿһ��ʱ�Ĵ����¼�
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
		
		//����ʱ�������¼�
		groupListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
				{
					//�õ�λ��
					long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
					groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
					childPosition = ExpandableListView.getPackedPositionChild(packedPos);

					if (groupPosition == 0) {
						//�����Ի���
						new AlertDialog.Builder(getActivity())   
						.setTitle(getString(R.string.confirm))  
						.setMessage(getString(R.string.confirm_delete) + "?")  
						.setPositiveButton(getString(R.string.yes), new DeleteOnClickListener())  
						.setNegativeButton(getString(R.string.no), null)  
						.show();  
					}
					else if (groupPosition == 1) {
						//�����Ի���
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
	
	//���ȷ��ɾ����ť
	public class DeleteOnClickListener implements OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Group group = Group.childs.get(groupPosition).get(childPosition);
			
			//�ӱ����Ƴ������
			UserInfo.myCreate.remove(group);
			Group.childs.get(groupPosition).remove(childPosition);
			
			//��ʾԲ�ν�����
			customProgressDialog = new CustomProgressDialog(getActivity());
			customProgressDialog.show();
			
			// �����������ӵĲ���
			params = new HashMap<String, String>();
			params.put("gn", group.getGname());
			
			//android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
			Thread thread = new Thread(new Runnable() {
				//���ӷ�����
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
			//�����߳�
			thread.start();
		}
		
	}
	
	//���ȷ���˳���ť
	public class ExitOnClickListener implements OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Group group = Group.childs.get(groupPosition).get(childPosition);
			
			//�ӱ����Ƴ������
			UserInfo.myJoin.remove(group);
			Group.childs.get(groupPosition).remove(childPosition);
			
			//��ʾԲ�ν�����
			customProgressDialog = new CustomProgressDialog(getActivity());
			customProgressDialog.show();
			
			// �����������ӵĲ���
			params = new HashMap<String, String>();
			params.put("gn", group.getGname());
			params.put("em", UserInfo.email);
			
			//android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
			Thread thread = new Thread(new Runnable() {
				//���ӷ�����
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
			//�����߳�
			thread.start();
		}
		
	}
}
