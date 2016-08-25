package com.tcms;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;



import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
  * @ClassName: MyCrashHandler
  * @Description: �쳣����
  * @author �����
  * @date 2016-5-17 ����11:33:27
  * @package_name ·����com.tcms
  */
public class MyCrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private static MyCrashHandler myCrashHandler;

	private MyCrashHandler() {
	};

	private Context context;

	public synchronized static MyCrashHandler getInstance() {
		if (myCrashHandler == null) {
			myCrashHandler = new MyCrashHandler();
			return myCrashHandler;
		} else {
			return myCrashHandler;
		}
	}

	public void init(Context context) {
		this.context = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			// ��throwable�Ĳ������汣����г�����쳣��Ϣ
			StringBuffer sb = new StringBuffer();
			// 1.�õ��ֻ��İ汾��Ϣ Ӳ����Ϣ
			// Field[] fields = Build.class.getDeclaredFields();
			// for(Field filed:fields){
			// filed.setAccessible(true); //��������
			// String name = filed.getName();
			// String value = filed.get(null).toString();
			// sb.append(name);
			// sb.append("=");
			// sb.append(value);
			// sb.append("\n");
			// }
			//
			//
			// //2.�õ���ǰ����İ汾��
			// PackageInfo info =
			// context.getPackageManager().getPackageInfo(context.getPackageName(),
			// 0);
			// sb.append( info.versionName);
			// sb.append("\n");
			// 3.�õ���ǰ������쳣��Ϣ
			Writer writer = new StringWriter();
			PrintWriter printwriter = new PrintWriter(writer);

			ex.printStackTrace(printwriter);
			printwriter.flush();
			printwriter.close();

			sb.append(writer.toString());
		
			// 4.�ύ�쳣��Ϣ��������
			Log.e(TAG, sb.toString());
			// System.out.println(sb.toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, "�쳣�˳�", 1).show();
				Looper.loop();

			}

		}.start();

		new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				android.os.Process.killProcess(android.os.Process.myPid());
			}

		}.start();

	}

}
