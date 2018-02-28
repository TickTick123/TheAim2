package com.example.zqf.theaim.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zqf on 2018/2/8.
 */

public class Goal extends BmobObject {
    private String master;  //目标所属用户的ID
    private String content; //内容

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
