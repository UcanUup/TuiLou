package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.R;

public class GroupAdapter extends BaseExpandableListAdapter {
	private List<String> groups = new ArrayList<String>();
	private List<List<String>> childs = new ArrayList<List<String>>();

	private Context mContext;
	
	public GroupAdapter(Context mContext) {
		super();
		
		//每个小组名
		groups.add("我的参与");
		groups.add("我的创建");
		
		//第一组
		List<String> child1 = new ArrayList<String>();
		child1.add("高数");
		child1.add("C++");
		child1.add("模电");
		childs.add(child1);
		
		//第二组
		List<String> child2 = new ArrayList<String>();
		child2.add("计算机网络");
		child2.add("编译原理");
		childs.add(child2);
		
		this.mContext = mContext;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.group, null);
		ImageView ie = (ImageView)layout.findViewById(R.id.isExpand);
		TextView tv = (TextView)layout.findViewById(R.id.TextView1);
		
		//设置组内容
		tv.setText(groups.get(groupPosition));

		if (isExpanded) {
			ie.setImageResource(R.drawable.expand);
		}
		else {
			ie.setImageResource(R.drawable.no_expand);
		}
		
		return layout;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.child, null);
		TextView tv = (TextView)layout.findViewById(R.id.TextView1);
		
		//设置子内容
		tv.setText(childs.get(groupPosition).get(childPosition));
	
		return layout;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		//返回true时点击会有闪烁
		return true;
	}
	
}
