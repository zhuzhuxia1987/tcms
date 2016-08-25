package com.tcms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: VersionInfo
 * @Description: �汾��Ϣʵ��
 * @author �����
 * @date 2016-5-17 ����11:36:08
 * @package_name ·����com.tcms.entity
 */
public class Version implements Bean, Serializable {

	/**
	  * @Fields serialVersionUID : TODO����һ�仰�������������ʾʲô��
	  */
	
	private static final long serialVersionUID = 3355134461941500873L;
	private Integer versionCode;// �汾��
	private String versionName;// �汾����
	private String fileURL;
	private String versionShuoMing;// ����
	private String updateTime;

	private String fileName;// ��װ������

	public Version() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Version(int versionCode, String versionName, String fileURL,
			String versionShuoMing, String updateTime, String fileName) {
		super();
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.fileURL = fileURL;
		this.versionShuoMing = versionShuoMing;
		this.updateTime = updateTime;
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public String getVersionShuoMing() {
		return versionShuoMing;
	}

	public void setVersionShuoMing(String versionShuoMing) {
		this.versionShuoMing = versionShuoMing;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

}
