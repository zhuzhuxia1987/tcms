package com.tcms.activity;

import com.tcms.Constant;
import com.tcms.TCMSApplication;
import com.tcms.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: BaseActivity
 * @Description: ����activity���Ǹ�����
 * @author �����
 * @date 2016-5-17 ����10:54:06
 * @package_name ·����com.tcms.activity
 */
public class BaseActivity extends Activity implements OnClickListener {
	public ImageView ivReback;
	public TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		TCMSApplication.addActivity2Stack(this);
		// initView();
	}

	/**
	 * view �����ʼ������
	 */
	public void initView() {
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		TCMSApplication.removeActivityFromStack(this);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_reback) {
			doReback();
		}
	}

	/**
	 * ���ñ���
	 * 
	 * @param activity
	 * @param titleStr
	 *            ��������
	 */
	public void setTitle(Activity activity, String titleStr) {
		tvTitle = (TextView) activity.findViewById(R.id.tv_title);
		setBoldText(tvTitle);
		tvTitle.setText(titleStr);
	}

	/**
	 * ͨ����Դid���ñ���
	 * 
	 * @param activity
	 * @param titleResouceId
	 *            ��ԴID
	 */
	public void setTitle(Activity activity, int titleResouceId) {
		tvTitle = (TextView) activity.findViewById(R.id.tv_title);
		setBoldText(tvTitle);
		tvTitle.setText(titleResouceId);
	}

	/**
	 * ��ʼ��ivReback
	 * 
	 * @param activity
	 */
	public void initivReabck(Activity activity) {
		ivReback = (ImageView) activity.findViewById(R.id.iv_reback);
		ivReback.setOnClickListener((OnClickListener) activity);
	}

	/**
	 * ���ivReback���¼���Ӧ
	 */
	public void doReback() {
		finish();
	}

	/**
	 * ��������������Ϊ������ androidĬ�ϲ�֧������
	 * 
	 * @param tv
	 */
	public static void setBoldText(TextView tv) {
		TextPaint tp2 = tv.getPaint();
		tp2.setFakeBoldText(true);
	}

	public static void showResulttoast(int result, Context context) {
		TCMSApplication.showResultToast(result, context);
	}

	public static SharedPreferences getOrSharedPrefences(Context context) {
		return context.getSharedPreferences(Constant.ORPREFERENCES,
				Context.MODE_PRIVATE);

	}
}
