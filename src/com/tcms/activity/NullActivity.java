package com.tcms.activity;

import com.tcms.R;

import android.os.Bundle;
import android.widget.ImageView;

public class NullActivity extends BaseActivity{
	private ImageView yanfazhong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weizhi_kongjian);
		yanfazhong = (ImageView) this.findViewById(R.id.kaifazhong);
	}
	
}
