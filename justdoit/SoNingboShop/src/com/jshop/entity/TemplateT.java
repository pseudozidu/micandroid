package com.jshop.entity;


import java.util.Date;

/**
 * TemplateT entity. @author MyEclipse Persistence Tools
 */

public class TemplateT implements java.io.Serializable {

	// Fields    

	private String tid;
	private String url;
	private String note;
	private String name;
	private Date createtime;
	private String creatorid;
	private String type;
	private String themeid;
	private String themename;
	private String tvalue;
	private String sign;
	private String status;

	// Constructors

	/** default constructor */
	public TemplateT() {
	}

	/** minimal constructor */
	public TemplateT(String tid, String url, String note, String name, Date createtime, String creatorid) {
		this.tid = tid;
		this.url = url;
		this.note = note;
		this.name = name;
		this.createtime = createtime;
		this.creatorid = creatorid;
	}

	/** full constructor */
	public TemplateT(String tid, String url, String note, String name, Date createtime, String creatorid, String type, String themeid, String themename, String tvalue, String sign, String status) {
		this.tid = tid;
		this.url = url;
		this.note = note;
		this.name = name;
		this.createtime = createtime;
		this.creatorid = creatorid;
		this.type = type;
		this.themeid = themeid;
		this.themename = themename;
		this.tvalue = tvalue;
		this.sign = sign;
		this.status = status;
	}

	// Property accessors

	public String getTid() {
		return this.tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCreatorid() {
		return this.creatorid;
	}

	public void setCreatorid(String creatorid) {
		this.creatorid = creatorid;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getThemeid() {
		return this.themeid;
	}

	public void setThemeid(String themeid) {
		this.themeid = themeid;
	}

	public String getThemename() {
		return this.themename;
	}

	public void setThemename(String themename) {
		this.themename = themename;
	}

	public String getTvalue() {
		return this.tvalue;
	}

	public void setTvalue(String tvalue) {
		this.tvalue = tvalue;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}