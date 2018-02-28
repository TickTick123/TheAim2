package com.example.zqf.theaim.Bean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

/**
 * 用户实体类
 */
public class User extends BmobUser {

	// 父类中已经存在的属性
	// private String objectid;
	// private String username;手机号（可用于登录）
	// private String password;
	// private String mobilePhoneNumber  手机号（可用于登录）
	// private String email;手机号（可用于登录）


	private String scheduleNumber;     //日程数
	private String completedscheduleNumber;  //已完成日程数
	private String rewardpoint;   //奖励点
	private String state; 		     // 状态
	private BmobFile picUser; 	// 头像



	public String getScheduleNumber() {
		return scheduleNumber;
	}

	public void setScheduleNumber(String scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}

	public String getCompletedscheduleNumber() {
		return completedscheduleNumber;
	}

	public void setCompletedscheduleNumber(String completedscheduleNumber) {
		this.completedscheduleNumber = completedscheduleNumber;
	}

	public String getRewardpoint() {
		return rewardpoint;
	}

	public void setRewardpoint(String rewardpoint) {
		this.rewardpoint = rewardpoint;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BmobFile getPicUser() {
		return picUser;
	}

	public void setPicUser(BmobFile picUser) {
		this.picUser = picUser;
	}

}
