package com.tcms.entity;

import java.io.Serializable;

/**
 * @ClassName: UserInfo
 * @Description: 用户信息实体
 * @author 朱恒章
 * @date 2016-5-17 上午11:30:03
 * @package_name 路径：com.tcms.entity
 */
public class UserInfo implements Bean, Serializable {
	/**
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */

	private static final long serialVersionUID = -5908919774786694691L;
	/**
	 * 
	 */

	private String Name;// 用户名
	private String pwd; // 密码
	private String phone;// 手机电话
	private String city;// 常在城市
	private String jigoumc;// 机构名称
	private String jigouini;// 机构地址
	private String userid;// 检测机构用户id
	private String loginname;// 检测机构登录用户
	private String nolockpwd;// 用户密码
	private String jigouid;// 检测机构id
	private String xingming;//检测机构操作员姓名

	public UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserInfo(String name, String pwd, String phone, String city,
			String jigoumc, String jigouini) {
		super();
		Name = name;
		this.pwd = pwd;
		this.phone = phone;
		this.city = city;
		this.jigoumc = jigoumc;
		this.jigouini = jigouini;
	}

	public String getName() {
		return Name;
	}

	public String getPwd() {
		return pwd;
	}

	public String getPhone() {
		return phone;
	}

	public String getCity() {
		return city;
	}

	public String getJigoumc() {
		return jigoumc;
	}

	public String getJigouini() {
		return jigouini;
	}

	public String getUserid() {
		return userid;
	}

	public String getLoginname() {
		return loginname;
	}

	public String getNolockpwd() {
		return nolockpwd;
	}

	public String getJigouid() {
		return jigouid;
	}

	public String getXingming() {
		return xingming;
	}

	public void setXingming(String xingming) {
		this.xingming = xingming;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setNolockpwd(String nolockpwd) {
		this.nolockpwd = nolockpwd;
	}

	public void setJigouid(String jigouid) {
		this.jigouid = jigouid;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setJigoumc(String jigoumc) {
		this.jigoumc = jigoumc;
	}

	public void setJigouini(String jigouini) {
		this.jigouini = jigouini;
	}

}
