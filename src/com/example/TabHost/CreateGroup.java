package com.example.TabHost;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.http.HttpLinker;
import com.example.http.HttpUrl;
import com.example.utils.CustomProgressDialog;
import com.example.utils.Group;
import com.example.utils.UserInfo;

public class CreateGroup extends Fragment {
	private EditText groupText;
	private EditText remarkText;
	
	private Button createButton;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private String group;
	private String remark;
	
	//ʹ��Handler���ȴ����߳���ɲ���
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			//�ر�Բ�ν�����
			customProgressDialog.dismiss();
			
			//С���Ѿ�����
			if (result.equals("%exist%")) {
				Toast.makeText(getActivity(), getString(R.string.group_exist),
					     Toast.LENGTH_SHORT).show();
			}
			else {
				//�ɹ�������鵽childs�����л�ѡ�ʱ���ټ���
				Group g = new Group();
				g.setGname(group);
				g.setEmail(UserInfo.email);
				g.setRemark(remark);
				g.setCode(result);
				Group.childs.get(0).add(g);
				
				//ҳ����ת
				Intent intent = new Intent();
				intent.putExtra("code", result);
				intent.setClass(getActivity(), CreateSucceed.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.create_group, container,
				false);
		
		groupText = (EditText)rootView.findViewById(R.id.groupName);
		remarkText = (EditText)rootView.findViewById(R.id.remark);
		
		createButton = (Button)rootView.findViewById(R.id.createButton);
		createButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// �����½��ť��,���û�������תΪ�ַ���
				group = groupText.getText().toString();
				remark = remarkText.getText().toString();
				
				// �����ֵ
				if (group.equals("") || remark.equals("")) {
					Toast.makeText(getActivity(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					//��ʾԲ�ν�����
					customProgressDialog = new CustomProgressDialog(getActivity());
					customProgressDialog.show();
					
					// �����������ӵĲ���
					params = new HashMap<String, String>();
					params.put("gn", group);
					params.put("re", remark);
					params.put("em", UserInfo.email);
					
					//android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
					Thread thread = new Thread(new Runnable() {
						//���ӷ�����
						@Override
						public void run() {
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.insert_group);
							
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("result", result);
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					});
					//�����߳�
					thread.start();
				}
			}
		});
		
		return rootView;
	}

}
