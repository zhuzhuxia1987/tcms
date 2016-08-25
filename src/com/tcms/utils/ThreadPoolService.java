package com.tcms.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;

/**
 * @ClassName: ThreadPoolService
 * @Description: 使用线程池 管理线程池的服务类
 * @author 朱恒章
 * @date 2016-5-17 上午11:39:30
 * @package_name 路径：com.tcms.utils
 */
public class ThreadPoolService {

	private static final int MESSAGE_POST_RESULT = 0x1;// 任务完成后的通知handler 的code
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
	 * 将线程任务添加到线程池 执行的方法
	 * 
	 * @param mr
	 *            自定义继承Runnable 的类
	 */
	public static void execute(CustomRunnable mr) {

		mr.beforTask();
		executor.execute(mr);
	}

}
