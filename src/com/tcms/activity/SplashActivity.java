package com.tcms.activity;

import com.tcms.Constant;
import com.tcms.TCMSApplication;
import com.tcms.R;
import com.tcms.entity.Version;
import com.tcms.utils.APPUltil;
import com.tcms.utils.HttpAsyncTask;
import com.tcms.view.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

/**
 * @ClassName: SplashActivity
 * @Description: ��ӭ����
 * @author �����
 * @date 2016-5-17 ����11:34:25
 * @package_name ·����com.tcms.activity
 */
public class SplashActivity extends Activity {
	private APPUltil appUltil;// �汾����
//	private CustomDialog newVersionDialog; // �°汾��ʾ�Ի���
//
//	private HttpAsyncTask cheVersionTask; // ����°汾 ��һ������
//
//	private Version version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activtity_splash);
		appUltil = new APPUltil(SplashActivity.this, handler);
		int versionCode = appUltil.getVersionCode();// ��ǰ�汾��Ϣ
		SharedPreferences sp = TCMSApplication.getOrSharedPrefences(this);
		int saveCode = sp.getInt(Constant.SAVAVERSIONCODE, 0);// ��ȡ��һ������ʱ�İ汾��Ϣ
		if (versionCode > saveCode) {// �µİ汾
			handler.postDelayed(runnable, 1500);
			Editor editor = sp.edit();
			editor.putInt(Constant.SAVAVERSIONCODE, versionCode);
			editor.commit();
			editor.clear();
		} else {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

			startActivity(intent);
			finish();
		}
		//

		// cheVersionTask = appUltil.getNewstVersion(null);

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// switch (msg.what) {
			// }
			// case 1000:
			// handler.removeCallbacks(runnable);
			// Intent intent = new Intent(SplashActivity.this,
			// LoginActivity.class);
			// // intent.putExtra("version", version);
			// startActivity(intent);
			// finish();
			// break;
			//
			// default:
			// break;
			// }
			super.handleMessage(msg);
		}
	};

	Runnable runnable = new Runnable() {
		public void run() {
			// newVersionDialog = appUltil.getDialog();
			// cheVersionTask = appUltil.getNewstVersion(null);
			// if (newVersionDialog == null) {
			// handler.sendEmptyMessage(1000);
			// }
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			// intent.putExtra("version", version);
			startActivity(intent);
			finish();
			// }
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == 100) {
//			System.out.println(resultCode);
//			version = (Version) data.getSerializableExtra("version");
//			System.out.println(version);
//			handler.sendEmptyMessage(1000);
//		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// if(appUltil.g)
//		if (cheVersionTask != null && !cheVersionTask.isCancelled()) {
//			cheVersionTask.cancel(true);
//		}
//		if (appUltil.getDialog() != null && appUltil.getDialog().isShowing()) {
//			appUltil.getDialog().dismiss();
//		}
		super.onDestroy();
	}
}
