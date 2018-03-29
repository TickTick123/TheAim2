package com.example.zqf.theaim.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zqf on 2018/2/8.
 */

public class Note extends BmobObject {
    private User master; //日程所属用户的ID
    private String content; //内容
    private String title;
//    private String createAt;
//
//    public String getCreateAt() {
//        return createAt;
//    }
//
//    public void setCreateAt(String createAt) {
//        this.createAt = createAt;
//    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
