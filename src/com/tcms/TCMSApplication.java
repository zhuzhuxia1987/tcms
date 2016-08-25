package com.tcms;

import java.util.ArrayList;

import org.apache.http.client.CookieStore;

import com.tcms.entity.UserInfo;
import com.tcms.entity.Version;
import com.tcms.utils.ThreadPoolService;
import com.tcms.view.CustomDialog;
import com.tcms.view.CustomDialog.ButtonRespond;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextPaint;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: OrongApplication
 * @Description: Ӧ�ó����������
 * @author �����
 * @date 2016-5-17 ����11:33:58
 * @package_name ·����com.tcms
 */
public class TCMSApplication extends Application {

	public static TCMSApplication instance;// ʵ����һ��app
	private static ArrayList<Activity> activitystack;// activity����ջ����¼ջ�е�activityʵ��
	public static CookieStore cookieStore;
	public static ThreadPoolService service;
	public static UserInfo user = null;
	public static Version version=null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance = this;

		activitystack = new ArrayList<Activity>();
		service = new ThreadPoolService();
		MyCrashHandler handler = MyCrashHandler.getInstance();
		handler.init(getApplicationContext());
		// ���쳣�����handler���õ����߳�����
		Thread.setDefaultUncaughtExceptionHandler(handler);
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		// ����ȫ�˳�
		for (Activity activity : activitystack) {
			activity.finish();
		}
		activitystack.clear();
		super.onTerminate();
	}

	/**
	 * ��Ӧ�ó��������ջ�е�һactivityʵ����ӵ�activitystack��
	 * 
	 * @param activity
	 *            һ��activityʵ��
	 */
	public static void addActivity2Stack(Activity activity) {
		instance.activitystack.add(activity);
	}

	/**
	 * ��activityʵ����activitystack���Ƴ�
	 * 
	 * @param activity
	 *            һ��activityʵ��
	 */
	public static void removeActivityFromStack(Activity activity) {
		instance.activitystack.remove(activity);
	}

	public static TCMSApplication getInstance() {
		return instance;
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

	public static SharedPreferences getOrSharedPrefences(Context context) {
		return context.getSharedPreferences(Constant.ORPREFERENCES,
				Context.MODE_PRIVATE);

	}

	public static void showResultToast(int result, Context context) {
		switch (result) {
		case Constant.NO_RESPONSE:
			Toast.makeText(context, R.string.no_response, 0).show();
			break;
		case Constant.S_EXCEPTION:
			Toast.makeText(context, R.string.server_exception, 0).show();
			break;
		case Constant.RESPONESE_EXCEPTION:
			Toast.makeText(context, R.string.responese_exception, 0).show();
			break;
		case Constant.TIMEOUT:
			Toast.makeText(context, R.string.timeout, 0).show();
			break;
		case Constant.NO_NETWORK:
			Toast.makeText(context, R.string.no_network, 0).show();
			break;
		case Constant.NULLPARAMEXCEPTION:
			Toast.makeText(context, R.string.nullparamexception, 0).show();
			break;
		case Constant.SERVER_EXCEPTION:
			Toast.makeText(context, R.string.server_exception, 0).show();
			break;
		case Constant.RELOGIN:
			CustomDialog dialog = new CustomDialog(context,
					new ButtonRespond() {

						@Override
						public void buttonRightRespond() {
							Activity activity = activitystack.get(0);
							activitystack.remove(0);// �ѵ�¼���������
							TCMSApplication.instance.onTerminate();
							activitystack.add(activity);// ���·ŵ�ջ��
						}

						@Override
						public void buttonLeftRespond() {
							// TODO Auto-generated method stub
							TCMSApplication.instance.onTerminate();
						}
					});
			dialog.setDialogTitle(R.string.relogin);
			dialog.setDialogMassage(R.string.relogin_message);
			dialog.setLeftButtonText(R.string.exit_app);
			dialog.setRightButtonText(R.string.relogin);
			dialog.setCancelable(false);
			dialog.show();
			break;
		case 4005:
			Toast.makeText(context, "ȱ�ٲ���", 0).show();
			break;
		case 4006:
			Toast.makeText(context, "����ֵ����Ϊ��", 0).show();
			break;
		default:
			Toast.makeText(context, "������Ӧʧ�ܣ������" + result, 0).show();
			break;
		}
	}
}
