package com.jshop.entity;


import java.util.Date;

/**
 * UserT entity. @author MyEclipse Persistence Tools
 */

public class UserT implements java.io.Serializable {

	// Fields    

	private String userid;
	private String username;
	private String realname;
	private String email;
	private String telno;
	private String mobile;
	private String question;
	private String answer;
	private String password;
	private String userstate;
	private Double points;
	private Integer postingcount;
	private String sex;
	private Date registtime;
	private Date disablebegin;
	private Date disableend;
	private String section;
	private String position;
	private String groupid;
	private String parttime1;
	private String parttime2;
	private String parttime3;
	private String parttime4;
	private String parttime5;
	private String parttime6;
	private String hobby;
	private String qq;
	private String msn;
	private String othercontract;
	private String address;
	private String postcode;
	private String birthday;
	private String grade;
	private Date gradetime;
	private String state;
	private String uid;

	// Constructors

	/** default constructor */
	public UserT() {
	}

	/** minimal constructor */
	public UserT(String userid, String username, String email, String password, String userstate, Double points, Integer postingcount, Date registtime) {
		this.userid = userid;
		this.username = username;
		this.email = email;
		this.password = password;
		this.userstate = userstate;
		this.points = points;
		this.postingcount = postingcount;
		this.registtime = registtime;
	}

	/** full constructor */
	public UserT(String userid, String username, String realname, String email, String telno, String mobile, String question, String answer, String password, String userstate, Double points, Integer postingcount, String sex, Date registtime, Date disablebegin, Date disableend, String section, String position, String groupid, String parttime1, String parttime2, String parttime3, String parttime4, String parttime5, String parttime6, String hobby, String qq, String msn, String othercontract, String address, String postcode, String birthday, String grade, Date gradetime, String state, String uid) {
		this.userid = userid;
		this.username = username;
		this.realname = realname;
		this.email = email;
		this.telno = telno;
		this.mobile = mobile;
		this.question = question;
		this.answer = answer;
		this.password = password;
		this.userstate = userstate;
		this.points = points;
		this.postingcount = postingcount;
		this.sex = sex;
		this.registtime = registtime;
		this.disablebegin = disablebegin;
		this.disableend = disableend;
		this.section = section;
		this.position = position;
		this.groupid = groupid;
		this.parttime1 = parttime1;
		this.parttime2 = parttime2;
		this.parttime3 = parttime3;
		this.parttime4 = parttime4;
		this.parttime5 = parttime5;
		this.parttime6 = parttime6;
		this.hobby = hobby;
		this.qq = qq;
		this.msn = msn;
		this.othercontract = othercontract;
		this.address = address;
		this.postcode = postcode;
		this.birthday = birthday;
		this.grade = grade;
		this.gradetime = gradetime;
		this.state = state;
		this.uid = uid;
	}

	// Property accessors

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelno() {
		return this.telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserstate() {
		return this.userstate;
	}

	public void setUserstate(String userstate) {
		this.userstate = userstate;
	}

	public Double getPoints() {
		return this.points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Integer getPostingcount() {
		return this.postingcount;
	}

	public void setPostingcount(Integer postingcount) {
		this.postingcount = postingcount;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	
	public Date getRegisttime() {
		return registtime;
	}

	public void setRegisttime(Date registtime) {
		this.registtime = registtime;
	}

	public Date getDisablebegin() {
		return disablebegin;
	}

	public void setDisablebegin(Date disablebegin) {
		this.disablebegin = disablebegin;
	}

	public Date getDisableend() {
		return disableend;
	}



	public void setDisableend(Date disableend) {
		this.disableend = disableend;
	}

	public String getSection() {
		return this.section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getGroupid() {
		return this.groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getParttime1() {
		return this.parttime1;
	}

	public void setParttime1(String parttime1) {
		this.parttime1 = parttime1;
	}

	public String getParttime2() {
		return this.parttime2;
	}

	public void setParttime2(String parttime2) {
		this.parttime2 = parttime2;
	}

	public String getParttime3() {
		return this.parttime3;
	}

	public void setParttime3(String parttime3) {
		this.parttime3 = parttime3;
	}

	public String getParttime4() {
		return this.parttime4;
	}

	public void setParttime4(String parttime4) {
		this.parttime4 = parttime4;
	}

	public String getParttime5() {
		return this.parttime5;
	}

	public void setParttime5(String parttime5) {
		this.parttime5 = parttime5;
	}

	public String getParttime6() {
		return this.parttime6;
	}

	public void setParttime6(String parttime6) {
		this.parttime6 = parttime6;
	}

	public String getHobby() {
		return this.hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMsn() {
		return this.msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getOthercontract() {
		return this.othercontract;
	}

	public void setOthercontract(String othercontract) {
		this.othercontract = othercontract;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Date getGradetime() {
		return this.gradetime;
	}

	public void setGradetime(Date gradetime) {
		this.gradetime = gradetime;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}