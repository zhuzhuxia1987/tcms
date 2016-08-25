package com.tcms.entity;

import java.io.Serializable;



public class JianYanXinXiBean implements Bean ,Serializable{

	/**
	  * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	  */

	private static final long serialVersionUID = 8456574000906209444L;

	/**
	 * @Fields serialVersionUID : TODO（检验信息）
	 */

	

	private String jianYanBH;

	private String baoGaoBM;

	private Integer baoGaoID;

	private String baoGaomenu;

	private String jianYanID;

	private Integer jianYanZT;

	private Integer rollbackzt;

	private Integer clid;

	private String clmc;

	private String baoGaoBH;// 报告编号

	private String jiGouID;// 机构id

	private String piZhuYuanId;

	private String piZhuRQ;
	
	private String jiezhang;

	private String jianyanbz;

	public String getJianYanBH() {
		return jianYanBH;
	}

	public String getBaoGaoBM() {
		return baoGaoBM;
	}

	public Integer getBaoGaoID() {
		return baoGaoID;
	}

	public String getBaoGaomenu() {
		return baoGaomenu;
	}

	public String getJianYanID() {
		return jianYanID;
	}

	public Integer getJianYanZT() {
		return jianYanZT;
	}

	public Integer getRollbackzt() {
		return rollbackzt;
	}

	public Integer getClid() {
		return clid;
	}

	public String getClmc() {
		return clmc;
	}

	public String getBaoGaoBH() {
		return baoGaoBH;
	}

	public String getJiGouID() {
		return jiGouID;
	}

	public String getPiZhuYuanId() {
		return piZhuYuanId;
	}

	public String getPiZhuRQ() {
		return piZhuRQ;
	}

	public String getJiezhang() {
		return jiezhang;
	}

	public String getJianyanbz() {
		return jianyanbz;
	}

	public void setJiezhang(String jiezhang) {
		this.jiezhang = jiezhang;
	}

	public void setJianyanbz(String jianyanbz) {
		this.jianyanbz = jianyanbz;
	}

	public void setJianYanBH(String jianYanBH) {
		this.jianYanBH = jianYanBH;
	}

	public void setBaoGaoBM(String baoGaoBM) {
		this.baoGaoBM = baoGaoBM;
	}

	public void setBaoGaoID(Integer baoGaoID) {
		this.baoGaoID = baoGaoID;
	}

	public void setBaoGaomenu(String baoGaomenu) {
		this.baoGaomenu = baoGaomenu;
	}

	public void setJianYanID(String jianYanID) {
		this.jianYanID = jianYanID;
	}

	public void setJianYanZT(Integer jianYanZT) {
		this.jianYanZT = jianYanZT;
	}

	public void setRollbackzt(Integer rollbackzt) {
		this.rollbackzt = rollbackzt;
	}

	public void setClid(Integer clid) {
		this.clid = clid;
	}

	public void setClmc(String clmc) {
		this.clmc = clmc;
	}

	public void setBaoGaoBH(String baoGaoBH) {
		this.baoGaoBH = baoGaoBH;
	}

	public void setJiGouID(String jiGouID) {
		this.jiGouID = jiGouID;
	}

	public void setPiZhuYuanId(String piZhuYuanId) {
		this.piZhuYuanId = piZhuYuanId;
	}

	public void setPiZhuRQ(String piZhuRQ) {
		this.piZhuRQ = piZhuRQ;
	}

}
