package com.tcms.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.tcms.R;
import com.tcms.entity.Version;

public class AboutActivity extends BaseActivity {

	private Version version = null;
	private TextView tv_update_date;// 更新时间
	private TextView tv_version_code;// 版本号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initivReabck(this);
		setTitle(this, R.string.aboutus);
		tv_version_code = (TextView) findViewById(R.id.tv_version_code);
		tv_update_date = (TextView) findViewById(R.id.tv_update_date);
	
		if (version != null) {
			tv_version_code.setText(version.getVersionCode());
			tv_update_date.setText(version.getUpdateTime());
		}

	}
}
