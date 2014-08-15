package com.vee.myhealth.bean;

import java.io.Serializable;

public class NewFriendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1590177609165407504L;

	private int accountid;
	private String accountname;
	private String accountavatar;
	private int friendaddi;
	private int iaddfriend;
	private long updatetime;
	private int readflag;
	
	public int getAccountid() {
		return accountid;
	}
	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public int getFriendaddi() {
		return friendaddi;
	}
	public void setFriendaddi(int friendaddi) {
		this.friendaddi = friendaddi;
	}
	public int getIaddfriend() {
		return iaddfriend;
	}
	public void setIaddfriend(int iaddfriend) {
		this.iaddfriend = iaddfriend;
	}
	public int getReadflag() {
		return readflag;
	}
	public void setReadflag(int readflag) {
		this.readflag = readflag;
	}
	public String getAccountavatar() {
		return accountavatar;
	}
	public void setAccountavatar(String accountavatar) {
		this.accountavatar = accountavatar;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
}
