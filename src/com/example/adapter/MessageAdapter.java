package com.example.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.R;
import com.example.utils.MyMessage;
import com.example.utils.UserInfo;

public class MessageAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private List<String> item1 = new ArrayList<String>();
	private List<String> item2 = new ArrayList<String>();
		
	public MessageAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		
		// ������ʾ����
		for (Iterator<MyMessage> iterator = UserInfo.myMessage.iterator(); iterator.hasNext();) {
			MyMessage message = (MyMessage) iterator.next();
			item1.add(message.getGname());
			item2.add(message.getTitle());
		}
	}

	@Override
	public int getCount() {
		return item1.size();
	}

	@Override
	public Object getItem(int position) {
		return item1.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ����ÿһ������ʾ������
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
		TextView tv1 = (TextView)layout.findViewById(R.id.group);		
		tv1.setText(item1.get(position));
		
		TextView tv2 = (TextView)layout.findViewById(R.id.title);		
		tv2.setText(item2.get(position));

		return layout;
	}
	
}
