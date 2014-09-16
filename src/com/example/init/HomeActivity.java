package com.example.init;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.R;

public class HomeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.home);
		
		// �õ�TabHost
		FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
	
		// ��ѡ����������
		tabHost.addTab(
				tabHost.newTabSpec(getString(R.string.message))
					.setIndicator(getString(R.string.message)), 
				com.example.TabHost.MessageActivity.class,
				null);
		tabHost.addTab(
				tabHost.newTabSpec(getString(R.string.add_group))
					.setIndicator(getString(R.string.add_group)), 
				com.example.TabHost.JoinGroup.class,
				null);
		tabHost.addTab(
				tabHost.newTabSpec(getString(R.string.create_group))
					.setIndicator(getString(R.string.create_group)), 
				com.example.TabHost.CreateGroup.class,
				null);
		tabHost.addTab(
				tabHost.newTabSpec(getString(R.string.my_group))
					.setIndicator(getString(R.string.my_group)), 
				com.example.TabHost.MyGroup.class,
				null);
		tabHost.addTab(
				tabHost.newTabSpec(getString(R.string.setting))
					.setIndicator(getString(R.string.setting)), 
				com.example.TabHost.SettingActivity.class,
				null);
	}

	private long exitTime = 0;
	
	// �û����·��ؼ�ʱ��ʾ�ٰ�һ���˳�����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), getString(R.string.exit_application),
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				ExitApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
