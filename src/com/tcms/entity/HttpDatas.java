package com.tcms.entity;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: HttpDatas
 * @Description: ��װ��http�������
 * @author �����
 * @date 2016-5-17 ����11:35:15
 * @package_name ·����com.tcms.entity
 */
public class HttpDatas {
	// private Context context;
	private String url;// ����URL
	private boolean isPost;// �Ƿ���POST����
	// private BasicNameValuePair header;// ����ͷ
	private ArrayList<BasicNameValuePair> paramList; // ���ݲ���
	private JSONObject jsonObject;
	private JSONArray jsonarray;

	public HttpDatas(String url, boolean isPost,
			ArrayList<BasicNameValuePair> paramList) {
		super();
		// this.context = context;
		this.url = url;
		this.isPost = isPost;
		// this.header = header;
		this.paramList = paramList;
	}

	public HttpDatas(String url, boolean isPost) {
		this(url, isPost, null);
	}

	public HttpDatas(String url) {
		this(url, true, null);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isPost() {
		return isPost;
	}

	public void setPost(boolean isPost) {
		this.isPost = isPost;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray getJsonarray() {
		return jsonarray;
	}

	public void setJsonarray(JSONArray jsonarray) {
		this.jsonarray = jsonarray;
	}

	public ArrayList<BasicNameValuePair> getParamList() {
		return paramList;
	}

	public void putParam(String name, String value) {
		if (paramList == null) {
			paramList = new ArrayList<BasicNameValuePair>();
		}
		paramList.add(new BasicNameValuePair(name, value));
	}

	public void put(String name, String value) {

		try {
			if (jsonObject == null) {
				jsonObject = new JSONObject();
			}

			jsonObject.put(name, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
