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
	private List<String> item1 = new ArrayList<String>();
	private List<String> item2 = new ArrayList<String>();
	private Context mContext;
		
	public MessageAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		super();
		this.mContext = mContext;
		
		//设置显示内容
		for (Iterator<MyMessage> iterator = UserInfo.myMessage.iterator(); iterator.hasNext();) {
			MyMessage message = (MyMessage) iterator.next();
			item1.add(message.getGname());
			item2.add(message.getTitle());
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item1.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return item1.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 设置每一个项显示的内容
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
		TextView tv1 = (TextView)layout.findViewById(R.id.group);		
		tv1.setText(item1.get(position));
		
		TextView tv2 = (TextView)layout.findViewById(R.id.title);		
		tv2.setText(item2.get(position));

		return layout;
	}
}
