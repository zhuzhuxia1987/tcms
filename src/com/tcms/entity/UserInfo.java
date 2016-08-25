package com.tcms.entity;

import java.io.Serializable;

/**
 * @ClassName: UserInfo
 * @Description: �û���Ϣʵ��
 * @author �����
 * @date 2016-5-17 ����11:30:03
 * @package_name ·����com.tcms.entity
 */
public class UserInfo implements Bean, Serializable {
	/**
	 * @Fields serialVersionUID : TODO����һ�仰�������������ʾʲô��
	 */

	private static final long serialVersionUID = -5908919774786694691L;
	/**
	 * 
	 */

	private String Name;// �û���
	private String pwd; // ����
	private String phone;// �ֻ��绰
	private String city;// ���ڳ���
	private String jigoumc;// ��������
	private String jigouini;// ������ַ
	private String userid;// �������û�id
	private String loginname;// ��������¼�û�
	private String nolockpwd;// �û�����
	private String jigouid;// ������id
	private String xingming;//����������Ա����

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
