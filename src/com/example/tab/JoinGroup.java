package com.example.tab;

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
import com.example.lib.Group;
import com.example.lib.HttpUrl;
import com.example.lib.UserInfo;
import com.example.utils.CustomProgressDialog;
import com.example.utils.HttpLinker;
import com.example.utils.Validation;

public class JoinGroup extends Fragment {
	
	private Button confirmButton;
	private EditText code;
	
	private HashMap<String, String> params;
	
	private CustomProgressDialog customProgressDialog;
	
	private String groupCode;
	
	// ʹ��Handler���ȴ����߳���ɲ���
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			String result = bundle.getString("result");
			
			// �û����ٵĹص����粢�����ťʱ������״̬��һ˲�����ǿ���
			if (result.equals("")) {
				// �ر�Բ�ν�����
				customProgressDialog.dismiss();
				
				// �������Ӳ�����
				Toast.makeText(getActivity(), getString(R.string.network_error),
						Toast.LENGTH_SHORT).show();
				return ;
			}
			
			code.setText("");
			
			// �ر�Բ�ν�����
			customProgressDialog.dismiss();
			
			// �����벻����
			if (result.equals("%nothing%")) {
				Toast.makeText(getActivity(), getString(R.string.code_no_exist),
						Toast.LENGTH_SHORT).show();
			}
			// �Լ�������С��
			else if (result.equals("%yourself%")) {
				Toast.makeText(getActivity(), getString(R.string.your_group),
						Toast.LENGTH_SHORT).show();
			}
			// �Ѿ�������С��
			else if (result.equals("%already%")) {
				Toast.makeText(getActivity(), getString(R.string.already_add_group),
						Toast.LENGTH_SHORT).show();
			}
			else {
				String[] myGroup = result.split("\\^");
				
				// �ɹ�������鵽childs�����л�ѡ�ʱ���ټ���
				if (Group.isUpdate) {
					Group g = new Group();
					g.setGname(myGroup[0]);
					g.setEmail(myGroup[1]);
					g.setRemark(myGroup[2]);
					g.setCode(groupCode);
					
				    Group.child2.add(g);
				}
				
				Intent intent = new Intent();
				intent.putExtra("groupName", myGroup[0]);
				intent.setClass(getActivity(), InviteSucceed.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.add_group, container,
				false);
		
		confirmButton= (Button)rootView.findViewById(R.id.confirmButton);
		code = (EditText)rootView.findViewById(R.id.groupName);
		
		// ȷ�ϰ�ť�ļ�����
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// �����½��ť��,���û�������תΪ�ַ���
			    groupCode = code.getText().toString();

				// �����ֵ
				if (groupCode.equals("")) {
					Toast.makeText(getActivity(), getString(R.string.null_value),
						     Toast.LENGTH_SHORT).show();
				}
				// �������Ӳ�����
				else if (!Validation.isNetAvailable(getActivity())) {
					Toast.makeText(getActivity(), getString(R.string.network_error),
						     Toast.LENGTH_SHORT).show();
				}
				else {
					// ��ʾԲ�ν�����
					customProgressDialog = new CustomProgressDialog(getActivity());
					customProgressDialog.show();
					
					// �����������ӵĲ���
					params = new HashMap<String, String>();
					params.put("co", groupCode);
					params.put("em", UserInfo.email);
					
					// android 3.0�Ժ�涨Ҫ���µ��߳�ִ��������ʵȲ���
					Thread thread = new Thread(new Runnable() {
						// ���ӷ�����
						@Override
						public void run() {
							HttpLinker httpLinker = new HttpLinker();
							String result = httpLinker.link(params, HttpUrl.join_group);
							
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("result", result);
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					});
					// �����߳�
					thread.start();
				}
			}
		});
		
		return rootView;
	}

}
