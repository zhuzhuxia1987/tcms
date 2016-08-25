package com.tcms.activity;



import com.tcms.R;
import com.tcms.city.CityPicker;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class CityActivity extends Activity implements OnClickListener {

	private CityPicker cityPicker;
	private Button mBtnConfirm;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		cityPicker = (CityPicker) findViewById(R.id.citypicker);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(
				CityActivity.this,
				"当前选中:" + cityPicker.getCity_string() + ","
						+ cityPicker.getCity_code_string(), Toast.LENGTH_SHORT)
				.show();
		Intent data = new Intent();
	
		data.putExtra("City", cityPicker.getCity_string());
		data.putExtra("CityCode", cityPicker.getCity_code_string());
		setResult(3001, data);
		finish();
	}

}
