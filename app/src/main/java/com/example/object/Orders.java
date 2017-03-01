package com.example.object;

import java.sql.Timestamp;

public class Orders {
private int oid;
private int uid;
private int jid;
private int bid;
private int state;
private Job job;
private Business business;
private User user;
private Comment comment;
private Timestamp applytime;




public Timestamp getApplytime() {
	return applytime;
}
public void setApplytime(Timestamp applytime) {
	this.applytime = applytime;
}
public Comment getComment() {
	return comment;
}
public void setComment(Comment comment) {
	this.comment = comment;
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



public int getOid() {
	return oid;
}
public void setOid(int oid) {
	this.oid = oid;
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

}
