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
		
		//ÿ��С����
		groups.add("�ҵĲ���");
		groups.add("�ҵĴ���");
		
		//��һ��
		List<String> child1 = new ArrayList<String>();
		child1.add("����");
		child1.add("C++");
		child1.add("ģ��");
		childs.add(child1);
		
		//�ڶ���
		List<String> child2 = new ArrayList<String>();
		child2.add("���������");
		child2.add("����ԭ��");
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
		
		//����������
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
		
		//����������
		tv.setText(childs.get(groupPosition).get(childPosition));
	
		return layout;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		//����trueʱ���������˸
		return true;
	}
	
}
