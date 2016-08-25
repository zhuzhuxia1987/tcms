package com.tcms.utils;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.tcms.Constant;
import com.tcms.TCMSApplication;
import com.tcms.entity.HttpDatas;
import com.tcms.entity.UpLoadDatas;
import com.tcms.utils.HttpAsyncTask.TaskCallBack;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @ClassName: NetUtils
 * @Description: һЩ����Ӧ�õĹ�����
 * @author �����
 * @date 2016-5-17 ����11:16:50
 * @package_name ·����com.tcms.utils
 */
public class NetUtils {
	/**
	 * ����Ƿ��п�������
	 * 
	 * @param context
	 *            �����Ļ���
	 * @return �п������緵��true ���򷵻�false
	 */
	public static boolean isHasNet(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();// ��ȡ����״̬����
		if (info == null || !info.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}

	public static File fileDownLoad(String uri, String fileDirect,
			String fileName, final Context context, ProgressDialog pd) {
		File file = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httParams, 20000);
		HttpConnectionParams.setSoTimeout(httParams, 20000);

		HttpProtocolParams.setContentCharset(httParams, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(httParams, false);
		if (TCMSApplication.cookieStore != null) {
			client.setCookieStore(TCMSApplication.cookieStore);
		}
		HttpGet get = new HttpGet(uri);
		try {
			HttpResponse responese = client.execute(get);
			int responeseCode = responese.getStatusLine().getStatusCode();
			if (responeseCode == HttpStatus.SC_OK) {
				HttpEntity httpentity = responese.getEntity();
				int max = (int) httpentity.getContentLength();
				pd.setMax(max);
				if (fileDirect != null) {
					File dire = new File(fileDirect);
					if (!dire.exists()) {
						dire.mkdirs();
					}
					if (dire.exists()) {
						if (fileName == null) {
							fileName = uri.substring(uri.lastIndexOf("/"));
							if (!fileName.endsWith(".apk")) {
								fileName = fileName + ".apk";
							}
						} else {
							fileName = "/" + fileName;
						}
						file = new File(dire + fileName);
						file.createNewFile();
						InputStream is = httpentity.getContent();
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int len = 0;
						int total = 0;
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
							total += len;
							pd.setProgress(total);
						}
						if (total == max) {
							pd.dismiss();
						}
						fos.flush();
						fos.close();
						is.close();
					}

				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return file;
	}

	public static CustomRunnable<?, ?> imageUpload(UpLoadDatas datas,
			final UploadCallback callback) {
		CustomRunnable<UpLoadDatas, String> customRunnable = new CustomRunnable<UpLoadDatas, String>(
				datas) {
			@Override
			public String executeTask(UpLoadDatas... param) {
				UpLoadDatas upLoadDatas = param[0];
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpParams httParams = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httParams, 20000);
				HttpConnectionParams.setSoTimeout(httParams, 20000);
				HttpProtocolParams.setContentCharset(httParams, HTTP.UTF_8);
				HttpProtocolParams.setUseExpectContinue(httParams, false);
				if (TCMSApplication.cookieStore != null) {
					httpclient.setCookieStore(TCMSApplication.cookieStore);
				}
				// ����ͨ��Э��汾
				httpclient.getParams().setParameter(
						CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);
				HttpPost httppost = new HttpPost(upLoadDatas.getUrl());
				File file = upLoadDatas.getImageFile();

				MultipartEntity mpEntity = new MultipartEntity(); // �ļ�����
				ContentBody cbFile = new FileBody(file);
				StringBody imageStr = null;
				StringBody mothodStr = null;
				ArrayList<BasicNameValuePair> paramList = upLoadDatas
						.getParamList();
				try {
					imageStr = new StringBody("image");
					mpEntity.addPart("upType", imageStr);
					if (paramList != null) {
						for (BasicNameValuePair valuePair : paramList) {
							mpEntity.addPart(valuePair.getName(),
									new StringBody(valuePair.getValue()));
						}
					}
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mpEntity.addPart("fileData", cbFile);
				// mpEntity.addPart(file.getName(), cbFile); // <input
				// type="file"
				// name="userfile"
				// /> ��Ӧ��
				System.out.println(file.getName());
				httppost.setEntity(mpEntity);
				HttpResponse responese;
				String result = null;
				try {
					responese = httpclient.execute(httppost);
					int responeseCode = responese.getStatusLine()
							.getStatusCode();
					if (responeseCode == HttpStatus.SC_OK) {

						result = EntityUtils.toString(responese.getEntity(),
								HTTP.UTF_8);
						System.out.println(result);
						return result;
					} else if (399 < responeseCode && responeseCode < 500) {// ��������Ӧ�ܾ���
						return String.valueOf(Constant.NO_RESPONSE);
					} else if (500 <= responeseCode && responeseCode < 600) {// ��������������쳣
						return String.valueOf(Constant.S_EXCEPTION);
					} else {// �����쳣
						return String.valueOf(Constant.RESPONESE_EXCEPTION);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConnectTimeoutException e) {
					e.printStackTrace();
					return String.valueOf(Constant.TIMEOUT);
				} catch (UnknownHostException e) {
					// ����״̬�Ƿ����
					if (!NetUtils.isHasNet(upLoadDatas.getContext())) {
						return String.valueOf(Constant.NO_NETWORK);// �޿�������
					} else {
						e.printStackTrace();
						return String.valueOf(Constant.RESPONESE_EXCEPTION);
					}

				} catch (IOException e) {
					e.printStackTrace();
					return String.valueOf(Constant.RESPONESE_EXCEPTION);
				}
				httpclient.getConnectionManager().shutdown();
				return result;
			}

			@Override
			public void beforTask() {
				// TODO Auto-generated method stub
				callback.beforeUpload();
				super.beforTask();
			}

			@Override
			public void afterTask(String result) {
				callback.afterUpload(result);
				super.afterTask(result);

			}
		};
		ThreadPoolService.execute(customRunnable);
		return customRunnable;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param url
	 *            ͼƬ��Url
	 * @param context
	 *            ������
	 * @param callback
	 *            �ص�
	 * @return
	 */
	public static CustomRunnable<String, LoadImageRespone> downLoadImage(
			String url, final Context context, final DownloadCallback callback) {
		return downLoadImage(url, context, null, null, callback);
	}

	/**
	 * 
	 * ����ͼƬ
	 * 
	 * @param url
	 *            ͼƬ��Url
	 * @param context
	 *            ������
	 * @param savePath
	 *            ����·�� ��Ŀ¼��Ϊ�ձ�ʾ������
	 * @param callback
	 *            �ص�
	 * @return
	 */
	public static CustomRunnable<String, LoadImageRespone> downLoadImage(
			String url, final Context context, String savePath,
			final DownloadCallback callback) {
		return downLoadImage(url, context, savePath, null, callback);
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param url
	 *            ͼƬ��Url
	 * @param context
	 *            ������
	 * @param savePath
	 *            ����·�� ��Ŀ¼��Ϊ�ձ�ʾ������
	 * @param imagefile
	 *            ͼƬ�ļ����� ���Ϊ������url��ͼƬ�������ļ����� ֻ���� .png��.jpg��׺·��
	 * @param callback
	 *            �ص�
	 * @return
	 */
	public static CustomRunnable<String, LoadImageRespone> downLoadImage(
			String url, final Context context, String savePath,
			final String imagefileName, final DownloadCallback callback) {
		if (url == null) {
			return null;
		}
		CustomRunnable<String, LoadImageRespone> customRunnable = new CustomRunnable<String, LoadImageRespone>(
				new String[] { url, savePath }) {
			@Override
			public LoadImageRespone executeTask(String... param) {

				String uri = param[0];
				if (uri == null) {
					return new LoadImageRespone();
				}
				String savePath = null;
				if (param.length > 1) {
					savePath = param[1];
				}
				DefaultHttpClient client = new DefaultHttpClient();
				HttpParams httParams = client.getParams();
				HttpConnectionParams.setConnectionTimeout(httParams, 20000);
				HttpConnectionParams.setSoTimeout(httParams, 20000);

				HttpProtocolParams.setContentCharset(httParams, HTTP.UTF_8);
				HttpProtocolParams.setUseExpectContinue(httParams, false);
				if (TCMSApplication.cookieStore != null) {
					client.setCookieStore(TCMSApplication.cookieStore);
				}
				HttpGet get = new HttpGet(uri);
				LoadImageRespone respone = null;

				try {
					HttpResponse responese = client.execute(get);
					int responeseCode = responese.getStatusLine()
							.getStatusCode();
					if (responeseCode == HttpStatus.SC_OK) {
						HttpEntity httpentity = responese.getEntity();
						InputStream imStream = httpentity.getContent();
						Bitmap bitmap = BitmapFactory.decodeStream(imStream);
						respone = new LoadImageRespone(bitmap);
						if (savePath != null) {
							File dire = FileUitls.createDirectory(context,
									savePath);
							if (dire.exists()) {
								String fileName = null;
								if (imagefileName != null
										&& (imagefileName.endsWith(".jpg") || imagefileName
												.endsWith(".png"))) {
									fileName = "/" + imagefileName;
								} else {
									fileName = uri.substring(uri
											.lastIndexOf("/"));
								}
								File file = new File(dire + fileName);
								if (!file.exists()) {
									file.createNewFile();
									FileOutputStream out = new FileOutputStream(
											file);
									if (bitmap != null) {
										bitmap.compress(
												Bitmap.CompressFormat.JPEG,
												100, out);
									}
									out.flush();
									out.close();
								}

								respone.setFileSavePath(file);
							}

						}

					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return respone;
			}

			@Override
			public void beforTask() {
				// TODO Auto-generated method stub
				callback.beforeDownload();
				super.beforTask();
			}

			@Override
			public void afterTask(LoadImageRespone result) {
				callback.loadCompleteCallback(result);
				super.afterTask(result);
			}
		};
		ThreadPoolService.execute(customRunnable);
		return customRunnable;
	}

	/**
	 * ������������
	 * 
	 * @param datas
	 *            �����������
	 * @param context
	 *            ������
	 * @param callback
	 *            �ص�
	 * @return
	 */
	public static HttpAsyncTask sendRequest(HttpDatas datas, Context context,
			TaskCallBack callback) {
		HttpAsyncTask task = new HttpAsyncTask(callback, context);
		task.execute(datas);
		return task;
	}

	/**
	 * ������������
	 * 
	 * @param datas
	 *            �����������
	 * @param context
	 *            ������
	 * @param dialogStr
	 *            ��������
	 * @param callback
	 *            �ص�
	 * @return
	 */
	public static HttpAsyncTask sendRequest(HttpDatas datas, Context context,
			String dialogStr, TaskCallBack callback) {
		HttpAsyncTask task = new HttpAsyncTask(callback, context, dialogStr);
		task.execute(datas);
		return task;
	}

	public interface UploadCallback {
		public void beforeUpload();

		public void afterUpload(String response);
	}

	public interface DownloadCallback {
		public void beforeDownload();

		public void loadCompleteCallback(LoadImageRespone respone);
	}

}
