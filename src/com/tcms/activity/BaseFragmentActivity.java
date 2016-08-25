package com.tcms.activity;


import com.tcms.TCMSApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextPaint;
import android.view.Window;
import android.widget.TextView;


/**
  * @ClassName: BaseFragmentActivity
  * @Description: TODO
  * @author �����
  * @date 2016-5-18 ����9:53:49
  * @package_name ·����com.tcms.activity
  */
public class BaseFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TCMSApplication.addActivity2Stack(this);
		super.onCreate(arg0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		TCMSApplication.removeActivityFromStack(this);
		super.onDestroy();
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
	
}
