package com.tcms.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcms.R;

public class TelUsActivity extends BaseActivity {
	private TextView tvTelephone;// 客服电话
	private ImageView ivCallService;// 呼叫客服

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tel_us);

		initivReabck(this);
		setTitle(this, R.string.aboutus);

		tvTelephone = (TextView) this.findViewById(R.id.tv_telephone);
		ivCallService = (ImageView) this.findViewById(R.id.iv_call_service);
		ivCallService.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_call_service:
			callPhone();
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	/**
	 * 启动打电话功能
	 */
	private void callPhone() {
		String phone = tvTelephone.getText().toString();

		Intent intent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + phone));
		startActivity(intent);
	}
}
