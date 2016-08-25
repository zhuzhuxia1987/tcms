package com.tcms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tcms.R;

public class HelpCaptionActivity extends BaseActivity {
	private RelativeLayout rltcms;//
	private RelativeLayout rlshanghun;//
	private Intent intent;

	// private RelativeLayout rlRecProjectCaption;//
	// private RelativeLayout rlRepaymentCaption;//

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_caption);
		initivReabck(this);
		setTitle(this, R.string.help_caption);
		initView();
	}

	@Override
	public void initView() {
		super.initView();
		rltcms = (RelativeLayout) this.findViewById(R.id.rl_tcms);
		rltcms.setOnClickListener(this);
		rlshanghun = (RelativeLayout) this.findViewById(R.id.rl_shanghun);
		rlshanghun.setOnClickListener(this);
		intent = new Intent(HelpCaptionActivity.this, WebViewActivity.class);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_tcms:

			intent.putExtra("url", "http://www.jsgl.com.cn/jhgscp/941.jhtml");
			startActivity(intent);
			break;
		case R.id.rl_shanghun:

			intent.putExtra("url", "http://www.jsgl.com.cn/jhgscp/943.jhtml");
			startActivity(intent);

			break;

		default:
			super.onClick(v);
			break;
		}

	}

}
