package com.tcms.utils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tcms.Constant;
import com.tcms.TCMSApplication;
import com.tcms.R;
import com.tcms.entity.HttpDatas;

/**
 * @ClassName: HttpAsyncTask
 * @Description: ��̬�ڲ��� http�첽������
 * @author �����
 * @date 2016-5-17 ����11:37:58
 * @package_name ·����com.tcms.utils
 * @param HttpDatas
 *            ��װ��һЩ�������
 * @param Integer
 *            ����ֵΪint��װ����
 */
public class HttpAsyncTask extends AsyncTask<HttpDatas, Void, Integer> {
	private TaskCallBack callback;
	private Context context;
	private ProgressDialog dialog;
	private String dialogStr;

	/**
	 * ���캯��
	 * 
	 * @param callback
	 *            �ص�����
	 */
	public HttpAsyncTask(TaskCallBack callback, Context context) {
		super();
		this.callback = callback;
		this.context = context;
	}

	/**
	 * ���캯��
	 * 
	 * @param callback
	 *            �ص�����
	 */
	public HttpAsyncTask(TaskCallBack callback, Context context,
			String dialogStr) {
		super();
		this.callback = callback;
		this.context = context;
		this.dialogStr = dialogStr;

	}

	@Override
	protected Integer doInBackground(HttpDatas... params) {
		// TODO Auto-generated method stub
		HttpDatas datas = null;
		if (params != null) {
			datas = params[0];
		} else {
			return Constant.NULLPARAMEXCEPTION;
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
		try {

			HttpResponse responese = null;
			if (datas.isPost()) {
				responese = doPost(datas, client);
			} else {
				responese = doGet(datas, client);
			}
			int responeseCode = responese.getStatusLine().getStatusCode();
			if (responeseCode == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(responese.getEntity(),
						HTTP.UTF_8);
				System.out.println(result);
				TCMSApplication.cookieStore = client.getCookieStore();
				return callback.excueHttpResponse(result);
			} else if (399 < responeseCode && responeseCode < 500) {// ��������Ӧ�ܾ���
				return Constant.NO_RESPONSE;
			} else if (500 <= responeseCode && responeseCode < 600) {// ��������������쳣
				return Constant.S_EXCEPTION;
			} else {// �����쳣
				System.out.println(responeseCode);
				return Constant.RESPONESE_EXCEPTION;
			}
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return Constant.TIMEOUT;
		} catch (UnknownHostException e) {
			// ����״̬�Ƿ����
			if (!NetUtils.isHasNet(context)) {
				return Constant.NO_NETWORK;// �޿�������
			} else {
				e.printStackTrace();
				return Constant.RESPONESE_EXCEPTION;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return Constant.RESPONESE_EXCEPTION;
		}

	}

	protected void onPreExecute() {
		if (dialogStr != null && !"".equals(dialogStr)) {
			dialog = new ProgressDialog(context) {
				@Override
				public void cancel() {
					if (!HttpAsyncTask.this.isCancelled()) {
						HttpAsyncTask.this.cancel(true);
					}
					Toast.makeText(context,
							context.getString(R.string.cancelrequest), 0)
							.show();
					super.cancel();
				}
			};
			dialog.setMessage(dialogStr);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		callback.beforeTask();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Integer result) {
		callback.afterTask(result);
		if (dialog != null) {
			dialog.dismiss();
		}
		super.onPostExecute(result);
	}

	/**
	 * ��get��ʽ���͵�����
	 * 
	 * @param datas
	 *            �������
	 * @param client
	 *            httpclientʵ��
	 * @return http��Ӧʵ��
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private HttpResponse doGet(HttpDatas datas, DefaultHttpClient client)
			throws ClientProtocolException, IOException {
		// quest����
		List<BasicNameValuePair> list = datas.getParamList();
		String url = datas.getUrl();
		if (list != null && list.size() > 0) {
			// StringBuffer buffer = new StringBuffer("?");
			StringBuffer buffer = new StringBuffer("/");
			BasicNameValuePair pair0 = list.get(0);
			// buffer.append(pair0.getName() + "=" + pair0.getValue());
			buffer.append(pair0.getValue());
			if (list.size() > 1) {
				for (int i = 1; i < list.size(); i++) {
					BasicNameValuePair pair = list.get(i);
					// buffer.append("&" + pair.getName() + "=" +
					// pair.getValue());
					buffer.append("/" + pair.getValue());
				}
			}
			url = url + buffer.toString();

		}
		System.out.println("url: " + url);
		HttpGet get = new HttpGet(url);
		get.addHeader("Content-Type","application/json");
		return client.execute(get);
	}

	/**
	 * ͨ��Post��ʽ���͵�����
	 * 
	 * @param datas
	 *            �������
	 * @param client
	 *            httpclientʵ��
	 * @return http��Ӧʵ��
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private HttpResponse doPost(HttpDatas datas, DefaultHttpClient client)
			throws ClientProtocolException, IOException {
//		List<BasicNameValuePair> list = datas.getParamList();
		if (datas.getJsonObject()==null) {
			// ���û���κ��������ֱ����get����
			if(datas.getJsonarray()!=null){
				String url = datas.getUrl();
				System.out.println("url: " + url);
				HttpPost post = new HttpPost(url);
//				HttpEntity entity = new UrlEncodedFormEntity(list);
				StringEntity entity=new StringEntity(datas.getJsonarray().toString(), "utf-8");
//				post.setHeader("Content-Type", "application/json");
				entity.setContentType("application/json");

				post.setEntity(entity);

				return client.execute(post);
			}
			return doGet(datas, client);
		}
		String url = datas.getUrl();
		System.out.println("url: " + url);
		HttpPost post = new HttpPost(url);
//		HttpEntity entity = new UrlEncodedFormEntity(list);
		StringEntity entity=new StringEntity(datas.getJsonObject().toString(), "utf-8");
//		post.setHeader("Content-Type", "application/json");
		entity.setContentType("application/json");

		post.setEntity(entity);

		return client.execute(post);

	}

	/**
	 * httpһ������Ļص����� �ӿ�
	 * 
	 * @author lanhaizhong
	 * 
	 */
	public interface TaskCallBack {

		/**
		 * ��������ǰ�Ĳ���
		 */
		public void beforeTask();

		/**
		 * ��ִ̨��http ����õ���Ӧ��Ĵ���
		 * 
		 * @param respondsStr
		 *            ��Ӧʵ����ַ���
		 * @return ״̬��
		 */
		public int excueHttpResponse(String respondsStr);

		/**
		 * ���������Ĳ���
		 * 
		 * @param result
		 */
		public void afterTask(int result);

	}

}
