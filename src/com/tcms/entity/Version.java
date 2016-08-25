package com.tcms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: VersionInfo
 * @Description: 版本信息实体
 * @author 朱恒章
 * @date 2016-5-17 上午11:36:08
 * @package_name 路径：com.tcms.entity
 */
public class Version implements Bean, Serializable {

	/**
	  * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	  */
	
	private static final long serialVersionUID = 3355134461941500873L;
	private Integer versionCode;// 版本号
	private String versionName;// 版本名称
	private String fileURL;
	private String versionShuoMing;// 描述
	private String updateTime;

	private String fileName;// 安装包名称

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
