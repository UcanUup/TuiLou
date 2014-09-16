package com.example.adapter;

import com.example.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private String[] item1 = {"����", "ģ��", "C++"};
	private String[] item2 = {"���콻1��2����ҵ", "�ǵô�ʵ����", "��ɿκ�����"};
	private Context mContext;
		
	public MessageAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item1.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return item1[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ����ÿһ������ʾ������
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
		TextView tv1 = (TextView)layout.findViewById(R.id.hint);		
		tv1.setText(item1[position]);
		
		TextView tv2 = (TextView)layout.findViewById(R.id.inviteCode);		
		tv2.setText(item2[position]);

		return layout;
	}
}
