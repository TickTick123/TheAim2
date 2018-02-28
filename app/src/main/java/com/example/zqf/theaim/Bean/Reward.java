package com.example.zqf.theaim.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zqf on 2018/2/8.
 */

public class Reward extends BmobObject {
    private String master; //日程所属用户的ID
    private String costpoint; //此奖励所话费的奖励点
    private String content; //内容

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getCostpoint() {
        return costpoint;
    }

    public void setCostpoint(String costpoint) {
        this.costpoint = costpoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
