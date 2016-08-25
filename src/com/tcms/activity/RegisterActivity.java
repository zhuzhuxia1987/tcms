package com.tcms.activity;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.tcms.Constant;
import com.tcms.R;

import com.tcms.entity.HttpDatas;
import com.tcms.entity.UserInfo;
import com.tcms.utils.HttpAsyncTask.TaskCallBack;
import com.tcms.utils.JSONUtil;
import com.tcms.utils.MatchUtil;
import com.tcms.utils.Md5Util;
import com.tcms.utils.NetUtils;

/**
 * @ClassName: RegisterActivity
 * @Description: 注册页面
 * @author 朱恒章
 * @date 2016-5-17 上午10:52:10
 * @package_name 路径：com.orong.activity
 * 
 * 方法索引：getJiGouList()根据citycode 返回机构列表 
 * requestRegister()注册登录请求 
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	private LinearLayout llStepNavigate;// 页面第一第二步的导航条
	private TextView tvStep1;
	private TextView tvStep2;
	private ViewFlipper vfRegister;// 页面切换的容器
	private int viewIndex = 0;// 初始化当前页排位

	// step1界面元素
	private EditText etPhoneNum;// 电话号码输入框
	private String phone;
	private Button btGetCode;// 获取验证码按钮;
	private EditText etCheckCode;// 验证码输入框
	private Button btNextStep;// 下一步按钮
	private CheckBox cbAccepted;// 同意复选框
	private TextView tvProtocolOfOrong;// 服务条款标题
	String APPKEY = "101732155b605";
	String APPSECRETE = "69d1850f4b74100266ab576b64e6cb16";
	int i = 60;

	// step2的界面元素
	private EditText etUserName;// 电话或用户名
	private EditText etPassWord;// 登录密码
	private TextView etshengshi;// 省市

	private Button btchengshi;// 选择城市
	private Button btRegissterLogin;// 注册并登录

	private Spinner spinner;// 机构下拉框
	private ArrayAdapter<String> arr_adapter; // 适配器数据
	private Context context;
	private String jigouid=null;
	private String city;

	private Runnable timerRunnable;// 定时器
	private static final int TIMECHANGE = 200;
	// 定时结束
	private static final int TIMERCOMPLETE = 300;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				btGetCode.setText("重新发送(" + i + ")");
			} else if (msg.what == -8) {
				btGetCode.setText("获取验证码");
				btGetCode.setClickable(true);
				i = 60;
			} else {
				int event = msg.arg1;
				int result = -1;
				// int result = msg.arg2; 测试为-1
				Object data = msg.obj;
				Log.e("event", "event=" + event);
				if (result == SMSSDK.RESULT_COMPLETE) {
					// 短信注册成功后，跳转step2页面
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
						Toast.makeText(getApplicationContext(), "提交验证码成功",
								Toast.LENGTH_SHORT).show();
						llStepNavigate.setBackgroundResource(R.drawable.step2);
						tvStep1.setTextColor(getResources().getColor(
								R.color.white2));
						tvStep2.setTextColor(getResources().getColor(
								R.color.white2));
						vfRegister.showNext();
						viewIndex++;

					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						Toast.makeText(getApplicationContext(), "正在获取验证码",
								Toast.LENGTH_SHORT).show();
					} else {
						((Throwable) data).printStackTrace();
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		setTitle(this, R.string.register);
		initView();

	}

	@Override
	public void initView() {
		super.initView();
		llStepNavigate = (LinearLayout) this
				.findViewById(R.id.ll_step_navigate);
		tvStep1 = (TextView) this.findViewById(R.id.tv_step1);
		tvStep2 = (TextView) this.findViewById(R.id.tv_step2);

		ivReback = (ImageView) this.findViewById(R.id.iv_reback);
		ivReback.setOnClickListener(this);
		vfRegister = (ViewFlipper) this.findViewById(R.id.vfl_register);

		// step1
		etPhoneNum = (EditText) this.findViewById(R.id.et_phonenum);
		btGetCode = (Button) this.findViewById(R.id.bt_get_checkCode);
		btGetCode.setOnClickListener(this);

		etCheckCode = (EditText) this.findViewById(R.id.et_checkcode);
		etCheckCode.addTextChangedListener(new captChaTextWatcher());

		// --------------------------------------------
		SMSSDK.initSDK(this, APPKEY, APPSECRETE);
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		// 同意并已经输入验证码才能实行下一步
		cbAccepted = (CheckBox) this.findViewById(R.id.cb_accepted);
		cbAccepted.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				btNextStep
						.setClickable(isChecked
								&& etCheckCode.getText().toString().trim()
										.length() == 4
								&& !btGetCode.isClickable());
				setButtonBackgroud(btNextStep);
			}
		});

		tvProtocolOfOrong = (TextView) this
				.findViewById(R.id.tv_protocol_of_orong);
		tvProtocolOfOrong.setOnClickListener(this);

		btNextStep = (Button) this.findViewById(R.id.bt_next_step);
		btNextStep.setOnClickListener(this);
		btNextStep.setClickable(false);
		setButtonBackgroud(btNextStep);

		// step2
		etUserName = (EditText) this.findViewById(R.id.et_usernmae);
		etPassWord = (EditText) this.findViewById(R.id.et_login_paw);
		etshengshi = (TextView) this.findViewById(R.id.tv_shengshi);

		btchengshi = (Button) this.findViewById(R.id.bt_chengshi);
		btchengshi.setOnClickListener(this);

		btRegissterLogin = (Button) this
				.findViewById(R.id.bt_register_and_login);
		btRegissterLogin.setOnClickListener(this);

		spinner = (Spinner) findViewById(R.id.spinner1);

		// 试图 获取本机电话号码并显示到电话号输入框
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String telephone = manager.getLine1Number();
		if (telephone != null) {
			if (telephone.startsWith("+86")) {
				telephone = telephone.substring(3);
			} else if (telephone.startsWith("86")) {
				telephone = telephone.substring(2);
			}
		}
		etPhoneNum.setText(telephone);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (timerRunnable != null) {
			handler.removeCallbacks(timerRunnable);
			timerRunnable = null;
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_reback:// 返回图标《
			reback();
			break;
		case R.id.bt_get_checkCode:// 点击获取验证码
			phone = etPhoneNum.getText().toString().trim();
			getCaptcha(phone);
			break;
		case R.id.bt_next_step:// 点击下一步
			String captcha = etCheckCode.getText().toString().trim();
			doCheckCaptcha(captcha);
			break;
		case R.id.tv_protocol_of_orong:// 点击查看条款
			startActivity(new Intent(this, ProtocolActivity.class));
			break;
		case R.id.bt_chengshi:// 选择省市
			startActivityForResult(new Intent(this, CityActivity.class), 200);
			break;
		case R.id.bt_register_and_login:// 点击注册并登陆
			doRegisterAndLogin();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 200) {
			if (resultCode == 3001) {
				city = data.getStringExtra("City");

				String Citycode = data.getStringExtra("CityCode").substring(0,
						4);
				etshengshi.setText("当前城市：" + city);
				etshengshi.setVisibility(View.VISIBLE);
				btchengshi.setText("重新选择城市");
				if (Citycode == null) {
					Toast.makeText(
							getApplicationContext(),
							RegisterActivity.this
									.getString(R.string.chongxinxuanzhe), 0)
							.show();
					etshengshi.setVisibility(View.GONE);
				}
				getJiGouList(Citycode);
			} else {
				Toast.makeText(
						getApplicationContext(),
						RegisterActivity.this
								.getString(R.string.chongxinxuanzhe), 0).show();
				etshengshi.setVisibility(View.GONE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getJiGouList(String citycode) {
		HttpDatas datas = new HttpDatas(Constant.OrganAPI, false);
		datas.putParam("citycode", citycode);
		NetUtils.sendRequest(datas, RegisterActivity.this, new TaskCallBack() {
			ArrayList<String> jigoulist = new ArrayList<String>();
			Hashtable<String, String> jigouGroup = new Hashtable<String, String>();

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
							RegisterActivity.this.getString(R.string.organnull),
							0).show();
					break;
				case 1:
					spinner.setVisibility(View.VISIBLE);
					Toast.makeText(
							getApplicationContext(),
							RegisterActivity.this
									.getString(R.string.select_organ), 0)
							.show();
					getspinnerlist(jigoulist, jigouGroup);
					break;
				default:
					showResulttoast(result, RegisterActivity.this);
					break;
				}
			}

		});
	}

	private void getspinnerlist(ArrayList<String> jigoulist,
			final Hashtable<String, String> jigouGroup) {
		arr_adapter = new ArrayAdapter(RegisterActivity.this,
				android.R.layout.simple_spinner_item, jigoulist);
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arr_adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String data = (String) spinner.getSelectedItem();
				jigouid = jigouGroup.get(data);
				System.out.println(jigouid);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 获取验证码
	 * 
	 * @param phone
	 *            接收验证码的手机帐号
	 */
	private void getCaptcha(String phone) {
		if (MatchUtil.isPhoneNum(phone)) {
			SMSSDK.getVerificationCode("86", phone);

			// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
			btGetCode.setClickable(false);
			btGetCode.setText("重新发送(" + i + ")");
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (; i > 0; i--) {
						handler.sendEmptyMessage(-9);
						if (i <= 0) {
							break;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(-8);
				}
			}).start();

		}

	}

	/**
	 * 点击下一步对验证码验证
	 */
	private void doCheckCaptcha(String captcha) {
		if (captcha == null || "".equals(captcha)) {
			Toast.makeText(this, R.string.null_captcha, 0).show();
			return;
		} else {
			// ---------------------------------------------------------------
			SMSSDK.submitVerificationCode("86", phone, captcha);

		}
	}

	protected void mySetResult(int resultCode, String loginName) {
		Intent data = new Intent();
		data.putExtra("Account", loginName);
		setResult(resultCode, data);
	}

	/**
	 * 注册并登录
	 */
	private void doRegisterAndLogin() {
		// 验证用户名
		String username = etUserName.getText().toString().trim();
		if (!MatchUtil.isLicitAccount(username)) {
			if ("".equals(username)) {
				Toast.makeText(this, R.string.null_account, 0).show();
			} else if (username.length() < 3) {
				Toast.makeText(this, R.string.short_account, 0).show();
			} else {
				Toast.makeText(this, R.string.error_account, 0).show();
			}
			return;
		}
		// 验证密码
		String loginPasWord = etPassWord.getText().toString().trim();
		if ("".equals(loginPasWord)) {
			Toast.makeText(this, R.string.null_password, 0).show();
			return;
		} else if (loginPasWord.length() < 6) {
			Toast.makeText(this, R.string.short_password, 0).show();
			return;
		}
		if (MatchUtil.isAllNumber(loginPasWord)) {
			Toast.makeText(this, R.string.number_password, 0).show();
			return;
		} else if (!MatchUtil.isLicitPassword(loginPasWord)) {
			Toast.makeText(this, R.string.error_password, 0).show();

			return;
		}
		// 常在城市

		if ("".equals(city)) {
			Toast.makeText(this, R.string.null_city, 0).show();
			return;
		}
	
		if (jigouid == null || "".equals(jigouid)) {
			Toast.makeText(this, R.string.organnull, 0).show();
		

		}
		requestRegister(phone, username, loginPasWord, city, jigouid);

	}

	/**
	 * 注册登录请求
	 * 
	 * @param phone
	 *            注册手机
	 * @param username
	 *            注册帐号
	 * @param loginPasWord
	 *            登录密码
	 * @param
	 * 
	 */
	private void requestRegister(String phone, String username,
			String loginPasWord, String city, String jgid) {
		HttpDatas datas = new HttpDatas(Constant.USERAPI);

		datas.put("phone", phone);
		datas.put("name", username);
		datas.put("pwd", Md5Util.md5Diagest(loginPasWord, 16));
		datas.put("city", city);
		if (jgid == null || "".equals(jgid)) {
			datas.put("jigouid", "-1");
		}else{
			datas.put("jigouid", jgid);
		}
	
		// datas.putParam("tradePwd", Md5Util.md5Diagest(transationPassWord,
		// 16));
		NetUtils.sendRequest(datas, RegisterActivity.this,
				getString(R.string.registering), new TaskCallBack() {
					UserInfo info;
					String loginName = null;

					@Override
					public int excueHttpResponse(String strResponds) {
						int code = 0;
						String msg;
						try {
							JSONObject jsonObject = new JSONObject(strResponds);
							code = jsonObject.getInt("coding");

							if (code == Constant.RESPONSE_OK) {

								info = JSONUtil.jsonObject2Bean(jsonObject,
										UserInfo.class);
							} else if (code == 403) {
								loginName = jsonObject.getString("msg");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return code;
					}

					@Override
					public void beforeTask() {

					}

					@Override
					public void afterTask(int result) {
						switch (result) {
						case Constant.RESPONSE_OK:
							Toast.makeText(
									getApplicationContext(),
									RegisterActivity.this
											.getString(R.string.registerSuccess),
									0).show();
							Intent intent = new Intent(RegisterActivity.this,
									MainActivity.class);
							intent.putExtra("User", info);
							startActivity(intent);
							finish();
							break;
						case 403:
							Toast.makeText(getApplicationContext(),
									getString(R.string.phone_has_register), 0)
									.show();
							mySetResult(3001, loginName);
							finish();
					
						default:
							showResulttoast(result, RegisterActivity.this);
							break;
						}

					}
				});
	}

	/**
	 * 返回前一页 如果ViewFlipper 的当前页不是第一页退回前一页，否则退出整个页面，
	 */
	private void reback() {
		if (viewIndex > 0) {
			llStepNavigate.setBackgroundResource(R.drawable.step1);
			tvStep1.setTextColor(getResources().getColor(R.color.white2));
			tvStep2.setTextColor(getResources().getColor(R.color.textcolor));
			vfRegister.showPrevious();
			viewIndex--;
		} else {
			finish();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		reback();

	}

	private void setButtonBackgroud(Button button) {
		if (button.isClickable()) {
			button.setBackgroundResource(R.drawable.register_step_background_selector);
		} else {
			button.setBackgroundResource(R.drawable.unclickble_right_round);
		}
	}

	class captChaTextWatcher implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			int length = s.length();
			if (length > 0) {
				if (s.charAt(length - 1) == ' ') {
					s.delete(length - 1, length);
					length--;
					return;
				}
			}
			if (length > 4) {
				s.delete(length - 1, length);
				length--;
				return;
			}
			if (length == 4 && cbAccepted.isChecked()) {
				phone = etPhoneNum.getText().toString().trim();
				if (MatchUtil.isPhoneNum(phone)) {
					btNextStep.setClickable(true);
					setButtonBackgroud(btNextStep);
				} else {
					if ("".equals(phone)) {
						Toast.makeText(getApplicationContext(),
								R.string.null_phone, 0).show();
					} else {
						Toast.makeText(getApplicationContext(),
								R.string.error_phone, 0).show();
					}
				}

			} else {
				btNextStep.setClickable(false);
				setButtonBackgroud(btNextStep);
			}
		}

	}
}
