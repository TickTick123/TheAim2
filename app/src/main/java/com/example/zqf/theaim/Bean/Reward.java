package com.example.zqf.theaim.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zqf on 2018/2/8.
 */

public class Reward extends BmobObject {
    private User master; //日程所属用户的ID
    private int costpoint; //此奖励所话费的奖励点
    private String content; //内容

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public int getCostpoint() {
        return costpoint;
    }

    public void setCostpoint(int costpoint) {
        this.costpoint = costpoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
