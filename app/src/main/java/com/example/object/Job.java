package com.example.object;

import java.sql.Timestamp;

public class Job {
	private int jid;//id
	private String jurl;//图片路径
	private String title;//
	private String jobtype;//兼职类型
	private String salary;//工资
	private int personnum;// 招聘人数
	private int lastnum;//  最后人数
	private int businessid;//  发布公司的id
	private String content;// 发布内容
	private String ask;	//  疑问
	private String endtime;  //  截止日期
	private int interview;//  面试
	private String interviewtime;   //  面试时间
	private int train;  //  培训
	private String traintime;   //  培训时间
	private String worktime;//   工作时间
	private String workplace;//  工作地点
	private String gender;  //性别
	private String closetype;  //
	private String closetime;
	private int state;
	private String pubtime;//  发布时间
	private Business business;  //
	private String area;  // 工作地点
	private String Connecter;

	public String getConnecter(){
		return Connecter;
	}
	public void setConnecter(String Connecter){
		this.Connecter = Connecter;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public int getJid() {
		return jid;
	}
	public void setJid(int jid) {
		this.jid = jid;
	}
	public String getJurl() {
		return jurl;
	}
	public void setJurl(String jurl) {
		this.jurl = jurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJobtype() {
		return jobtype;
	}
	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public int getPersonnum() {
		return personnum;
	}
	public void setPersonnum(int personnum) {
		this.personnum = personnum;
	}
	public int getLastnum() {
		return lastnum;
	}
	public void setLastnum(int lastnum) {
		this.lastnum = lastnum;
	}
	public int getBusinessid() {
		return businessid;
	}
	public void setBusinessid(int businessid) {
		this.businessid = businessid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAsk() {
		return ask;
	}
	public void setAsk(String ask) {
		this.ask = ask;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getInterview() {
		return interview;
	}
	public void setInterview(int interview) {
		this.interview = interview;
	}
	public String getInterviewtime() {
		return interviewtime;
	}
	public void setInterviewtime(String interviewtime) {
		this.interviewtime = interviewtime;
	}
	public int getTrain() {
		return train;
	}
	public void setTrain(int train) {
		this.train = train;
	}
	public String getTraintime() {
		return traintime;
	}
	public void setTraintime(String traintime) {
		this.traintime = traintime;
	}
	public String getWorktime() {
		return worktime;
	}
	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	public String getWorkplace() {
		return workplace;
	}
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getClosetype() {
		return closetype;
	}
	public void setClosetype(String closetype) {
		this.closetype = closetype;
	}
	public String getClosetime() {
		return closetime;
	}
	public void setClosetime(String closetime) {
		this.closetime = closetime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	
	
}
