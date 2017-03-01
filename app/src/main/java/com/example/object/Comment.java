package com.example.object;

import java.sql.Timestamp;

public class Comment {
	private int cid;
	private int uid;
	private int jid;
	private int bid;
	private int state;
	private String content;
	private Job job;
	private Business business;
	private User user;
	private int commentor;
	Timestamp time;
	
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getCommentor() {
		return commentor;
	}
	public void setCommentor(int commentor) {
		this.commentor = commentor;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getJid() {
		return jid;
	}
	public void setJid(int jid) {
		this.jid = jid;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
