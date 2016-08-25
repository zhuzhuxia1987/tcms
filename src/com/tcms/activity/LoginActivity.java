package com.tcms.activity;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tcms.Constant;
import com.tcms.R;
import com.tcms.TCMSApplication;
import com.tcms.entity.HttpDatas;
import com.tcms.entity.UserInfo;
import com.tcms.entity.Version;
import com.tcms.utils.AESUtil;
import com.tcms.utils.APPUltil;
import com.tcms.utils.HttpAsyncTask;
import com.tcms.utils.HttpAsyncTask.TaskCallBack;
import com.tcms.utils.JSONUtil;
import com.tcms.utils.MatchUtil;
import com.tcms.utils.Md5Util;
import com.tcms.utils.NetUtils;
import com.tcms.view.CustomDialog;

/**
 * @ClassName: LoginActivity
 * @Description: ��¼����
 * @author �����
 * @date 2016-5-17 ����10:53:25
 * @package_name ·����com.tcms.activity
 * 
 *               ���������� doLongin()ִ�е�¼����
 */
public class LoginActivity extends BaseActivity {
	private TextView tvPhoneRegisted;// �����Ѿ�ע����ʾ
	private TextView tvfuwuqixx;
	private EditText etAccount;// �ʺ�
	private EditText etPassword;
	private CheckBox cbSavedpw;
	private Button btRegister;// ע�ᰴť
	private Button btLogin;// ��¼��ť
	private UserInfo user = null;
	private String username = null;
	String userFormat;
	String name = null;
	// private APPUltil appUltil;// �汾����
	// private CustomDialog newVersionDialog; // �°汾��ʾ�Ի���
	// private HttpAsyncTask cheVersionTask; // ����°汾 ��һ������
//	public static Version version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		// appUltil = new APPUltil(LoginActivity.this, null);
		// cheVersionTask = appUltil.getNewstVersion(null);
		SharedPreferences sp = getOrSharedPrefences(this);
		boolean isChecked = sp.getBoolean(Constant.ISSAVEPW, false);
		cbSavedpw.setChecked(isChecked);
		if (isChecked) {
			username = sp.getString(Constant.USERNAME, null);
			userFormat = String.format(getString(R.string.userformat),
					username.subSequence(0, 1),
					username.substring(username.length() - 1));
			etAccount.setText(username);
			String pw = sp.getString(Constant.PASSWORD, null);
			if (pw != null) {
				try {

					etPassword.setText(pw);

					// etPassword.setText(pw);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			doLongin();
		}
		// etAccount.addTextChangedListener(new UsernameTextWatcher());
		tvfuwuqixx.setText("��ǰ��������" + sp.getString(Constant.JIGOUMC, "δѡ��"));

	}

//	public static Version getVersion() {
//		return version;
//	}
//
//	public static void setVersion(Version version) {
//		LoginActivity.version = version;
//	}

	@Override
	public void initView() {
		super.initView();
		etAccount = (EditText) this.findViewById(R.id.et_account);
		etPassword = (EditText) this.findViewById(R.id.et_password);
		cbSavedpw = (CheckBox) this.findViewById(R.id.cb_savedpw);
		TextView tvTitle = (TextView) this.findViewById(R.id.tv_labletext1);
		setBoldText(tvTitle);
		this.btLogin = (Button) findViewById(R.id.bt_login);
		btLogin.setOnClickListener(this);
		// ������������Ӵ�
		setBoldText(btLogin);
		btRegister = (Button) findViewById(R.id.bt_register);
		// ������������Ӵ�
		setBoldText(btRegister);
		btRegister.setOnClickListener(this);
		tvPhoneRegisted = (TextView) this.findViewById(R.id.tv_phoneRegister);
		tvfuwuqixx = (TextView) this.findViewById(R.id.tv_fuwuqixx);
//		Intent intent = getIntent();
//		Serializable serializable = intent.getSerializableExtra("version");
//		if (serializable != null) {
//			version = (Version) serializable;
//			TCMSApplication.version = version;
//		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_register:
			startActivityForResult(new Intent(this, RegisterActivity.class),
					100);
			break;
		case R.id.bt_login:
			doLongin();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			if (resultCode == 3001) {
				String registerAccount = data.getStringExtra("Account");
				if (registerAccount != null && registerAccount.length() > 1) {
					String text = String.format(
							getString(R.string.phone_registerNotifide),
							registerAccount.charAt(0), registerAccount
									.charAt(registerAccount.length() - 1));
					tvPhoneRegisted.setText(text);
					tvPhoneRegisted.setVisibility(View.VISIBLE);
				}
			} else {
				tvPhoneRegisted.setVisibility(View.GONE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ִ�е�¼����
	 */
	private void doLongin() {

		name = etAccount.getText().toString().trim();
		if (name.equals(userFormat)) {
			name = username;
		}
		if (!MatchUtil.isLicitAccount(name)) {
			if ("".equals(name)) {
				Toast.makeText(this, R.string.null_account, 0).show();
			} else if (name.length() < 3) {
				Toast.makeText(this, R.string.short_account, 0).show();
			} else {
				Toast.makeText(this, R.string.error_account, 0).show();
			}
			return;
		}
		// ��֤����
		final String loginPasWord = etPassword.getText().toString().trim();

		if (!MatchUtil.isLicitPassword(loginPasWord)) {
			if ("".equals(loginPasWord)) {
				Toast.makeText(this, R.string.null_password, 0).show();
			} else if (loginPasWord.length() < 6) {
				Toast.makeText(this, R.string.short_password, 0).show();
			} else {
				Toast.makeText(this, R.string.error_password, 0).show();
			}
			return;
		}
		HttpDatas datas = new HttpDatas(Constant.LOANAPI, false);
		// datas.putParam("method", "login");auth/loginV2/
		// datas.putParam("method", "auth/loginV2");
		datas.putParam("name", name);
		// datas.putParam("pwd", Md5Util.md5Diagest(loginPasWord, 16));
		datas.putParam("pwd", Md5Util.md5Diagest(loginPasWord, 16));
		NetUtils.sendRequest(datas, LoginActivity.this,
				getString(R.string.login_now), new TaskCallBack() {

					@Override
					public int excueHttpResponse(String strResponds) {
						System.out.println(strResponds);
						int code = 0;
						String msg1;

						JSONObject jsonObject = JSONUtil
								.instaceJsonObject(strResponds);
						if (jsonObject != null) {
							try {
								code = jsonObject.getInt("coding");
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
						}

						if (code == 200) {
							// user = JSONUtil.jsonObject2Bean(jsonObject,
							// UserInfo.class);
							user = JSONUtil.jsonObject2Bean(jsonObject,
									UserInfo.class);

							if (cbSavedpw.isChecked()) {
								SharedPreferences sp = LoginActivity
										.getOrSharedPrefences(LoginActivity.this);
								Editor editor = sp.edit();
								editor.putBoolean(Constant.ISSAVEPW, true);
								editor.putString(Constant.USERNAME, name);
								editor.putString(Constant.PASSWORD,
										loginPasWord);
								editor.putString(Constant.JIGOUINI,
										user.getJigouini());
								editor.putString(Constant.JIGOUMC,
										user.getJigoumc());
								editor.commit();

							}
							return Constant.STATAS_OK;
						}
						return code;

					}

					@Override
					public void beforeTask() {
					}

					@Override
					public void afterTask(int result) {
						System.out.println(result);
						switch (result) {
						case Constant.STATAS_OK:
							Toast.makeText(
									getApplicationContext(),
									LoginActivity.this
											.getString(R.string.login_success),
									0).show();
							Intent intent = new Intent(LoginActivity.this,
									MainActivity.class);
							intent.putExtra("User", user);
							startActivity(intent);
							break;
						case 201:
							Toast.makeText(getApplicationContext(),
									getString(R.string.error_user), 0).show();
							break;
						case 403:
							Toast.makeText(getApplicationContext(),
									getString(R.string.error_pw_or_user), 0)
									.show();
							break;
						default:
							showResulttoast(result, LoginActivity.this);
							break;
						}
					}
				});

	}

	class UsernameTextWatcher implements TextWatcher {

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
			if (username != null && userFormat != null) {
				// ����������е���������X***��ͷ���� ������������ֲ��ǿո� ����Ϊ�û���׼�������µ��ʺ�
				if (length > 3 && s.toString().subSequence(1, 4).equals("***")
						&& s.charAt(length - 1) != ' ') {
					if (!userFormat.equals(s.toString().trim())) {// ��Ҫ�ı�
						if (userFormat.length() < length) {
							s.delete(0, userFormat.length());
							username = null;
							userFormat = null;
						} else {

						}

					}
				}
			}
		}
	}

}
