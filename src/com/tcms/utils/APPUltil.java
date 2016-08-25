package com.tcms.utils;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.tcms.Constant;
import com.tcms.R;
import com.tcms.entity.HttpDatas;
import com.tcms.entity.Version;
import com.tcms.utils.HttpAsyncTask.TaskCallBack;
import com.tcms.view.CustomDialog;
import com.tcms.view.CustomDialog.ButtonRespond;

/**
 * @ClassName: APPUltil
 * @Description: �汾��Ϣ
 * @author �����
 * @date 2016-5-17 ����11:36:28
 * @package_name ·����com.tcms.utils
 */
public class APPUltil {
	private CustomDialog dialog;
	private Activity activity;
	private Handler handler;
	CustomRunnable<String, File> downloadAPKRunnable;
	private Version info;

	/**
	 * APP���¹�����
	 * 
	 * @param activity
	 *            ������
	 * @param handler
	 *            ֪ͨ���̵߳�handler�����Ժ���˵��������˼�ʱ��handler����What=1000��Ϣ
	 */
	public APPUltil(Activity activity, Handler handler) {
		super();
		this.activity = activity;
		this.handler = handler;
	}

	/**
	 * ��ȡ��ǰ�汾��Ϣ
	 */
	public int getVersionCode() {// ��ȡ��ǮAPK�汾��Ϣ
		PackageManager manager = null;
		int versioncode = -1;
		try {
			manager = activity.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					activity.getPackageName(), 0);
			versioncode = info.versionCode;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		return versioncode;

	}

	/**
	 * ��װ���سɹ���apk
	 * 
	 * @param file
	 *            apk���ļ�����
	 */
	public void installApk(File file) {
		Intent intent = new Intent();
		// �鿴����ͼ (����)
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		
		activity.startActivityForResult(intent, 100);
	}

	/**
	 * ����Ƿ����°汾
	 * 
	 * @param dialogStr
	 * @return
	 */
	public HttpAsyncTask getNewstVersion(final String dialogStr) {
		HttpDatas datas = new HttpDatas(Constant.DOWNLOADURL, false);
		// datas.putParam("method","update");
		HttpAsyncTask task = NetUtils.sendRequest(datas, activity, dialogStr,
				new TaskCallBack() {
				

					@Override
					public int excueHttpResponse(String respondsStr) {
						int code = 0;

						try {
							JSONObject jsonObject = new JSONObject(respondsStr);
							info = JSONUtil.jsonObject2Bean(jsonObject,
									Version.class);
							code = 200;
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
							// ��ǰ �汾С�ڷ������汾
							if (getVersionCode() < info.getVersionCode()) {
								// ��ʾ�û��а汾����
								dialog = new CustomDialog(activity,
										new ButtonRespond() {

											@Override
											public void buttonRightRespond() {

												String fileName = info
														.getFileName();
												// System.out.println(fileName);
												if (fileName == null) {
													fileName = "TCMS.apk";
												}
												downLoadAPK(
														Constant.DOWNLOADAPK,
														FileUitls
																.getSDCardPath(activity),
														fileName);
												dialog.dismiss();
											}

											@Override
											public void buttonLeftRespond() {
												// TODO Auto-generated method
												// stub
												if (handler != null) {
													handler.sendEmptyMessage(1000);
												}

												dialog.dismiss();
											}

										}) {
									public void cancel() {
										if (handler != null) {
											handler.sendEmptyMessage(1000);
										}

									};
								};
						
								dialog.setLeftButtonText(R.string.not_yet);
								dialog.setRightButtonText(R.string.update_now);
								dialog.setDialogTitle(R.string.findUpdate);
								StringBuffer buffer = new StringBuffer();
								if (info.getVersionName() != null) {
									buffer.append(activity
											.getString(R.string.versionName)
											+ info.getVersionName() + "\n");
								}
								String des = info.getVersionShuoMing();
								if (des != null && !"".equals(des)) {
									buffer.append(activity
											.getString(R.string.description)
											+ des);
								}
								dialog.setDialogMassage(buffer.toString());
								dialog.show();
							} else if (dialogStr != null) {
								Toast.makeText(activity,
										activity.getString(R.string.newest), 0)
										.show();
							}

							break;
						default:
							break;
						}
					}
				});
		return task;
	}

	public CustomDialog getDialog() {
		return dialog;
	}

	/**
	 * ����APP
	 * 
	 * @param uri
	 *            ���ص�ַ
	 * @param fileDirect
	 *            ����Ŀ¼
	 * @param fileName
	 *            �ļ�����
	 */
	public void downLoadAPK(String uri, String fileDirect, String fileName) {
		downloadAPKRunnable = new CustomRunnable<String, File>(new String[] {
				uri, fileDirect, fileName }) {
			ProgressDialog pd;

			@Override
			public void beforTask() {
				// TODO Auto-generated method stub
				pd = new ProgressDialog(activity) {
					@Override
					public void cancel() {
						// TODO Auto-generated method stub
						downloadAPKRunnable.cancleTask();
						Toast.makeText(activity,
								activity.getString(R.string.cancel_download), 0)
								.show();
						if (pd.getProgress() < pd.getMax()) {
							handler.sendEmptyMessage(1000);
						}
						super.cancel();
					}
				};
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();
				super.beforTask();
			}

			@Override
			public File executeTask(String... param) {
				// TODO Auto-generated method stub
				String uri = param[0];
				String fileDirect = param[1];
				String fileName = param[2];
				return NetUtils.fileDownLoad(uri, fileDirect, fileName,
						activity, pd);
			}

			@Override
			public void afterTask(File result) {
				// TODO Auto-generated method stub
				if (result == null) {
					Toast.makeText(activity, "����ʧ��", 0).show();
					pd.dismiss();
				} else {
					installApk(result);
				}
				super.afterTask(result);
			}

		};
		ThreadPoolService.execute(downloadAPKRunnable);
	}
}
