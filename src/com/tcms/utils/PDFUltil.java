package com.tcms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.tcms.TCMSApplication;
import com.tcms.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

public class PDFUltil {
	private Activity activity;
	private Handler handler;
	private static String filepath;
	CustomRunnable<String, File> downloadpdf;

	public PDFUltil(Activity activity, Handler handler) {
		super();
		this.activity = activity;
		this.handler = handler;
	}

	/**
	 * 下载PDf
	 * 
	 * @param uri
	 *            下载地址
	 * @param fileDirect
	 *            保存目录
	 * @param fileName
	 *            文件名称
	 */
	public void downLoadpdf(String uri, String fileDirect, String fileName) {
		downloadpdf = new CustomRunnable<String, File>(new String[] { uri,
				fileDirect, fileName }) {
			ProgressDialog pd;

			@Override
			public void beforTask() {
				// TODO Auto-generated method stub
				pd = new ProgressDialog(activity) {
					@Override
					public void cancel() {
						// TODO Auto-generated method stub
						downloadpdf.cancleTask();
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

				return fileDownLoad(uri, fileDirect, fileName, activity, pd);
			}

			@Override
			public void afterTask(File result) {
				// TODO Auto-generated method stub
				if (result == null) {
					Toast.makeText(activity, "未找到pdf报告", 0).show();
					pd.dismiss();
				} else {
					readPdf(result);
				}
				super.afterTask(result);
			}

		};
		ThreadPoolService.execute(downloadpdf);
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
							if (!fileName.endsWith(".pdf")) {
								fileName = fileName + ".pdf";
							}
						} else {
							fileName = "/" + fileName;
						}
						file = new File(dire + fileName);
						filepath = dire + fileName;
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
			} else {
				return null;
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

	/**
	 * 读取下载好的文件
	 * 
	 * @param file
	 * 
	 */
	public void readPdf(File file) {
		// Intent intent = new Intent();
		// 查看的意图 (动作)
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.fromFile(file),
		// "application/vnd.android.package-archive");
		// activity.startActivityForResult(intent, 100);
		// Intent intent = new Intent(activity, PdfActivity.class);
		// intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, filepath);
		// activity.startActivity(intent);
		// 第二种方式 待试
		Intent intent = getPdfFileIntent(filepath);
		activity.startActivity(intent);
	}

	public Intent getPdfFileIntent(String path) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		i.setDataAndType(uri, "application/pdf");
		return i;
	}
}
