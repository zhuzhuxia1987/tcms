package com.tcms.utils;

import com.tcms.utils.ThreadPoolService.CusRunnable;

import android.os.Message;

/**
 * @ClassName: CustomRunnable
 * @Description: �̳�Runnalbe��һ���лص���������
 * @author �����
 * @date 2016-5-17 ����11:36:54
 * @package_name ·����com.tcms.utils
 * @param <Params>
 *            �����񷽷����ݵĲ�����������
 * @param <Result>����ִ�����ķ���ֵ��������
 */
public abstract class CustomRunnable<Params, Result> implements CusRunnable {

	private Params[] params;
	private Result result;
	private Thread cThread;// ��ǰ���к�̨������߳�
	private boolean interruptTag = false;// ��������Ϊstatic����

	public CustomRunnable(Params... params) {
		this.params = params;
	}

	/**
	 * ���߳��߳�����ǰִ�еķ��� ����ThreadPoolExecutor.execute(Runnalbe r)ִ��ǰִ�еķ���
	 */
	public void beforTask() {
	};

	/**
	 * �����߳�֮��ִ�е����񷽷�
	 * 
	 * @param params
	 *            �����񷽷����ݵĲ���
	 * @return ����ִ�����ķ���ֵ
	 */
	public abstract Result executeTask(Params... param);

	/**
	 * ���߳��߳������ִ�еķ��� ����ThreadPoolExecutor.execute(Runnalbe r)ִ�����ִ�еķ���
	 * 
	 * @param result
	 *            �߳�������ɺ󷵻ص�����
	 */
	public void afterTask(Result result) {

	};

	@Override
	public void run() {
		cThread = Thread.currentThread();
		result = executeTask(params);
		if (!interruptTag) {

			Message message = mhandler.obtainMessage(RESULT,
					CustomRunnable.this);
			message.sendToTarget();
		}
	}

	public Result getResult() {
		return result;
	}

	public void cancleTask() {
		if (cThread != null) {
			interruptTag = true;
			// ����߳�
			cThread.interrupt();
		}
	}
}