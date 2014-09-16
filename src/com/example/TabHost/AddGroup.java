package com.example.TabHost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.R;

public class AddGroup extends Fragment {
	private Button confirmButton;
	private EditText code;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.add_group, container,
				false);
		
		confirmButton= (Button)rootView.findViewById(R.id.confirmButton);
		code = (EditText)rootView.findViewById(R.id.editText1);
		
		//ȷ�ϰ�ť�ļ�����
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//��������ȷ��������
				if (!code.getText().toString().equals("")) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), InviteSucceed.class);
					startActivity(intent);
				}
				//���������
				else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), InviteFail.class);
					startActivity(intent);
				}
			}
		});
		
		
		return rootView;
	}

}
