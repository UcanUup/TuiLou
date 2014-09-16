package com.example.TabHost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.R;
import com.example.utils.UserInfo;

public class SettingActivity extends Fragment {
	private TextView userName;
	
	private Button exitButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.setting, container,
				false);
		
		//设置用户名
		userName = (TextView)rootView.findViewById(R.id.userName);
		userName.setText(UserInfo.userName);
		
		exitButton= (Button)rootView.findViewById(R.id.exitButton);
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//清空用户信息
				UserInfo.myMessage = null;
				UserInfo.myCreate = null;
				UserInfo.myJoin = null;
				
				getActivity().finish();
			}
		});
		
		return rootView;
	}

}
