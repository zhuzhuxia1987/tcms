package com.tcms.entity;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

/**
 * @ClassName: UpLoadDatas
 * @Description: ��װ�ϴ�ͼƬ���ļ�������Ҫ������
 * @author �����
 * @date 2016-5-17 ����11:35:40
 * @package_name ·����com.tcms.entity
 */
public class UpLoadDatas {
	private String url;// �ϴ�ͼƬ��·��
	private File imageFile;
	private Context context;
	private ArrayList<BasicNameValuePair> paramList; // ���ݲ���

	public UpLoadDatas() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UpLoadDatas(String url, File imageFile, Context context) {
		super();
		this.url = url;
		this.imageFile = imageFile;
		this.context = context;
		paramList = new ArrayList<BasicNameValuePair>();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	public ArrayList<BasicNameValuePair> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<BasicNameValuePair> paramList) {
		this.paramList = paramList;
	}

	public void putParam(String name, String value) {
		if (paramList == null) {
			paramList = new ArrayList<BasicNameValuePair>();
		}
		paramList.add(new BasicNameValuePair(name, value));
	}
}
