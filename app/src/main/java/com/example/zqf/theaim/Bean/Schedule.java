package com.example.zqf.theaim.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zqf on 2018/2/8.
 */

public class Schedule extends BmobObject {
    private User master;  //日程所属用户的ID
    private String mastergoal;  //日程所属子目标的ID``
    private String rewardpoint;  //此日程所得的奖励点（1，2，3）
    private String content;  //内容
    private String decribe;  // 描述
    private int year;  //年
    private int month;  //月
    private int day;  //日
    private String done;  //是否完成（ture:完成，false:未完成）
    private String time;  //所设置时间
    private String remind;  //是否提醒（闹铃）这三个先不用
    private String repeat;  //重复类型（如：每天，周五周六）
    private String taketime;  //所花时间（计时功能）

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public String getMastergoal() {
        return mastergoal;
    }

    public void setMastergoal(String mastergoal) {
        this.mastergoal = mastergoal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDecribe() {
        return decribe;
    }

    public void setDecribe(String decribe) {
        this.decribe = decribe;
    }

    public String getRewardpoint() {
        return rewardpoint;
    }

    public void setRewardpoint(String rewardpoint) {
        this.rewardpoint = rewardpoint;
    }



    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getTaketime() {
        return taketime;
    }

    public void setTaketime(String taketime) {
        this.taketime = taketime;
    }

}
