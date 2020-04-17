package com.example.administrator.pushapp.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/16</p>
 * <p>@for : </p>
 * <p></p>
 */
@Table(name = "pushRecord")
public class PushRecordBean {
    @Column(
            name = "ID",
            isId = true,
            autoGen = true
    )
    private int id;

    @Column(name = "SUCCEED")
    private boolean succeed;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "TIME")
    private String time;

    public PushRecordBean() {
    }

    public PushRecordBean(boolean succeed, String content, String time) {
        this.succeed = succeed;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
