package com.tcms.activity;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tcms.Constant;
import com.tcms.R;
import com.tcms.TCMSApplication;
import com.tcms.city.CityPicker;
import com.tcms.entity.HttpDatas;
import com.tcms.entity.UserInfo;
import com.tcms.utils.NetUtils;
import com.tcms.utils.HttpAsyncTask.TaskCallBack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CityChooseActivity extends BaseActivity implements OnClickListener {

	private TextView tvTitle;
	private TextView tvchengshi;
	private CityPicker cityPicker;
	private Button bt_qiehuandiqu;
	private Button bt_city_tijiao;
	private Spinner sp_jigoulist;
	private TextView tv_qiehuanjigou;
	private String chengshi;
	private String code;
	private UserInfo user;
	private ArrayAdapter<String> arr_jigoulist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qiehuandq);
		user = MainActivity.getUser();
		tvTitle = (TextView) findViewById(R.id.tv_title);
		TCMSApplication.setBoldText(tvTitle);
		tvTitle.setText(R.string.choose_jigou);
		initivReabck(this);
		cityPicker = (CityPicker) findViewById(R.id.citypicker);
		bt_qiehuandiqu = (Button) findViewById(R.id.bt_qhdq);
		bt_city_tijiao = (Button) findViewById(R.id.bt_city_tijiao);
		sp_jigoulist = (Spinner) findViewById(R.id.sp_qh_jigoulist);
		tv_qiehuanjigou = (TextView) findViewById(R.id.tv_qh_jigou);
		tvchengshi=(TextView)findViewById(R.id.tv_chengshi);
		bt_qiehuandiqu.setOnClickListener(this);
		bt_city_tijiao.setOnClickListener(this);
		sp_jigoulist.setVisibility(View.GONE);
		tv_qiehuanjigou.setVisibility(View.GONE);
		bt_city_tijiao.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bt_qhdq:
			chengshi = cityPicker.getCity_string();
			code = cityPicker.getCity_code_string();
			
			tvchengshi.setText("当前城市："+chengshi);
			tvchengshi.setTextColor(getResources().getColor(R.color.red1));
			tvchengshi.setTextSize(10);
			if (code != null) {
				String Citycode = code.substring(0, 4);
				getJiGouList(Citycode);
			}

			break;
		case R.id.bt_city_tijiao:
			updateUser(user.getJigouid(), chengshi, user.getPhone());

			break;
		case R.id.iv_reback:
			doReback();
			break;
		default:
			break;
		}

	}

	private void updateUser(String jigouid, String chengshi2, String phone) {
		// TODO Auto-generated method stub
		HttpDatas datas = new HttpDatas(Constant.UPDATEUSER, false);
		datas.putParam("jigouid", jigouid);
		datas.putParam("Phone", phone);
		datas.putParam("chengshi", chengshi2);
		NetUtils.sendRequest(datas, CityChooseActivity.this,
				new TaskCallBack() {

					@Override
					public int excueHttpResponse(String respondsStr) {
						// TODO Auto-generated method stub
						int code = 0;
						try {
							JSONObject jsonObject = new JSONObject(respondsStr);
							code = jsonObject.getInt("coding");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return code;
					}

					@Override
					public void beforeTask() {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTask(int result) {
						// TODO Auto-generated method stub
						switch (result) {
						case Constant.RESPONSE_OK:
							Intent data = new Intent();

							data.putExtra("chengshi", chengshi);
							setResult(3001, data);
							finish();

							break;
						case 403:
							Toast.makeText(
									getApplicationContext(),
									CityChooseActivity.this
											.getString(R.string.baocun_err), 0)
									.show();

							break;
						default:
							showResulttoast(result, CityChooseActivity.this);
							break;
						}
					}
				});
	}

	private void getJiGouList(String citycode) {
		HttpDatas datas = new HttpDatas(Constant.OrganAPI, false);
		datas.putParam("citycode", citycode);
		NetUtils.sendRequest(datas, CityChooseActivity.this,
				new TaskCallBack() {
					ArrayList<String> jigoulist = new ArrayList<String>();
					Hashtable<String, String> jigouGroup = new Hashtable<String, String>();
					String jigouid = null;

					@Override
					public void beforeTask() {
						// TODO Auto-generated method stub

					}

					@Override
					public int excueHttpResponse(String respondsStr) {
						System.out.println(respondsStr);
						int code = 0;
						if (respondsStr == null || respondsStr.equals("[]")) {
							return code;
						}
						code = 1;

						try {
							JSONArray array = new JSONArray(respondsStr);
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonobject = array.getJSONObject(i);
								String jigoumc = jsonobject.getString("id");
								jigouid = jsonobject.getString("name");
								jigoulist.add(jigoumc);
								jigouGroup.put(jigoumc, jigouid);

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
						return code;
					}

					@Override
					public void afterTask(int result) {
						// TODO Auto-generated method stub
						switch (result) {
						case 0:
							Toast.makeText(
									getApplicationContext(),
									CityChooseActivity.this
											.getString(R.string.organnull), 0)
									.show();
							break;
						case 1:
							sp_jigoulist.setVisibility(View.VISIBLE);
							tv_qiehuanjigou.setVisibility(View.VISIBLE);
							bt_qiehuandiqu.setText("重新选择");
							bt_city_tijiao.setVisibility(View.VISIBLE);
							Toast.makeText(
									getApplicationContext(),
									CityChooseActivity.this
											.getString(R.string.select_organ),
									0).show();
							getspinnerlist(jigoulist, jigouGroup);
							break;
						default:
							showResulttoast(result, CityChooseActivity.this);
							break;
						}
					}

				});
	}

	private void getspinnerlist(ArrayList<String> jigoulist,
			final Hashtable<String, String> jigouGroup) {
		arr_jigoulist = new ArrayAdapter(CityChooseActivity.this,
				android.R.layout.simple_spinner_item, jigoulist);
		arr_jigoulist
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_jigoulist.setAdapter(arr_jigoulist);
		sp_jigoulist.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String data = (String) sp_jigoulist.getSelectedItem();
				user.setJigouid(jigouGroup.get(data));
				SharedPreferences sp = LoginActivity
						.getOrSharedPrefences(CityChooseActivity.this);
				Editor editor = sp.edit();
				editor.putString(Constant.JIGOUINI, jigouGroup.get(data));
				editor.putString(Constant.JIGOUMC, data);
				editor.commit();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void doReback() {
		// TODO Auto-generated method stub
		super.doReback();
	}

}
