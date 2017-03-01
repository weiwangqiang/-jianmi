package com.example.object;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int uid = 0 ;
	private int credit = 0;//信用
	private String username = ""; // not null
	private String password = ""; // not null
	private String birthday = "";
	private String registtime = "";
	private String realname = "";
	private String gender = "男";
	private String city = "";
	private String area = "";
	private String school = "";
	private String college = "";
	private String intoyear = "";
	private String introduction = "";
	private String email = "";
	private String money = "";
	private int worknum = 0;
	private int goodas = 0;
	private int badas = 0;
	private int height = 0;
	private String picurl;
	private String picurlLocal;

	private int state = 0;
	private String role = "";
	private int isStudent = 0;
	private List<Job> collect = new ArrayList<Job>();
	private QQLogin qqLogin;
	private String icnFile = "";

	public String getIcnFile() {
		return icnFile;
	}

	public void setIcnFile(String icnFile) {
		this.icnFile = icnFile;
	}



	public QQLogin getQqLogin(){
		return qqLogin;
	}
	public void setQqLogin(QQLogin qqlogin){
		this.qqLogin = qqlogin;
	}
	public List<Job> getCollectlist(){
		return collect;
	}
	public void removeJobList(int position){
		this.collect.remove(position);
	}

	public void setCollectlist(Job job){
		collect.add(job);
	}
	public int getCollectNumber(){
		return collect.size();
	}

	public int getCredit(){
		return credit;
	}
	public void setCredit(int credit){
	  this.credit = credit;
	}
	public int getHeight(){
		return height;
	}
	public void setHeight(int height){
		this.height = height;
	}
	public int getStudent(){
		return isStudent;
	}
	public void setStudent(int isStudent){
		this.isStudent = isStudent;
	}
	public String getBirthday(){
		return birthday;
	}
	public void setBirthday(String birthday){
		this.birthday = birthday;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRegisttime() {
		return registtime;
	}
	public void setRegisttime(String registtime) {
		this.registtime = registtime;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getIntoyear() {
		return intoyear;
	}
	public void setIntoyear(String intoyear) {
		this.intoyear = intoyear;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public int getWorknum() {
		return worknum;
	}
	public void setWorknum(int worknum) {
		this.worknum = worknum;
	}
	public int getGoodas() {
		return goodas;
	}
	public void setGoodas(int goodas) {
		this.goodas = goodas;
	}
	public int getBadas() {
		return badas;
	}
	public void setBadas(int badas) {
		this.badas = badas;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getPicurlLocal() {
		return picurlLocal;
	}

	public void setPicurlLocal(String picurlLocal) {
		this.picurlLocal = picurlLocal;
	}
}