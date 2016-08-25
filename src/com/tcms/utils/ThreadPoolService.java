package com.tcms.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;

/**
 * @ClassName: ThreadPoolService
 * @Description: ʹ���̳߳� �����̳߳صķ�����
 * @author �����
 * @date 2016-5-17 ����11:39:30
 * @package_name ·����com.tcms.utils
 */
public class ThreadPoolService {

	private static final int MESSAGE_POST_RESULT = 0x1;// ������ɺ��֪ͨhandler ��code
	private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
			5, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue());
	private final static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MESSAGE_POST_RESULT:
				CustomRunnable cr = (CustomRunnable) msg.obj;
				// System.out.println(cr==null);
				if (cr != null) {
					cr.afterTask(cr.getResult());
				}
				break;
			default:
				break;
			}
		}

	};

	public interface CusRunnable extends Runnable {
		public Handler mhandler = handler;
		public int RESULT = MESSAGE_POST_RESULT;

	}

	/**
	 * ���߳�������ӵ��̳߳� ִ�еķ���
	 * 
	 * @param mr
	 *            �Զ���̳�Runnable ����
	 */
	public static void execute(CustomRunnable mr) {

		mr.beforTask();
		executor.execute(mr);
	}

}
