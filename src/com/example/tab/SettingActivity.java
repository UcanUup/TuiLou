package com.example.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.R;
import com.example.init.LoginActivity;
import com.example.lib.ExitApplication;
import com.example.lib.Group;
import com.example.lib.UserInfo;
import com.example.sqlite.UserDatabase;

public class SettingActivity extends Fragment {
	
	private TextView userName;
	
	private Button exitButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.setting, container,
				false);
		
		// �����û���
		userName = (TextView)rootView.findViewById(R.id.userName);
		userName.setText(UserInfo.userName);
		
		exitButton= (Button)rootView.findViewById(R.id.exitButton);
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ����û���Ϣ
				UserInfo.myMessage = null;
				UserInfo.myCreate = null;
				UserInfo.myJoin = null;
				UserInfo.email = null;
				UserInfo.userName = null;
				
				// ���������Ϣ
				Group.isUpdate = false;
				Group.groups = null;
				Group.childs = null;
				Group.child1 = null;
				Group.child2 = null;
				
				// ������ݿ�����
				UserDatabase userDatabase = new UserDatabase(getActivity());
				userDatabase.delete();
				
				// ��ת����½����
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
				
				// ���Activity��ջ
				ExitApplication.getInstance().exit();
			}
		});
		
		return rootView;
	}

}