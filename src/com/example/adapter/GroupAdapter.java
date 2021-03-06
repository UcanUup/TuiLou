package com.example.adapter;

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
import com.example.lib.Group;

public class GroupAdapter extends BaseExpandableListAdapter {
	
	private Context mContext;
	
	private List<String> groups;
	private List<List<Group>> childs;
	
	public GroupAdapter(Context mContext, List<String> groups, List<List<Group>> childs) {
		super();
		this.mContext = mContext;
		this.groups = groups;
		this.childs = childs;
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// 设置group内容的回调函数
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.group, null);
		ImageView ie = (ImageView)layout.findViewById(R.id.isExpand);
		TextView tv = (TextView)layout.findViewById(R.id.TextView1);
		
		// 设置组内容
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
		// 设置child内容的回调函数
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.child, null);
		TextView tv = (TextView)layout.findViewById(R.id.TextView1);
		TextView tv2 = (TextView)layout.findViewById(R.id.TextView2);
		
		// 设置子内容
		tv.setText(childs.get(groupPosition).get(childPosition).getGname());
		
		if (groupPosition == 0)
			tv2.setText(childs.get(groupPosition).get(childPosition).getCode());
		
		return layout;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// 返回true时点击会有闪烁
		return true;
	}
	
}
