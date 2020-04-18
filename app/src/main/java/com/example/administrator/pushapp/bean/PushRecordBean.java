package com.example.administrator.pushapp.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/16</p>
 * <p>@for : </p>
 * <p></p>
 */
@Entity
public class PushRecordBean {
    @Id
    private Long id;

    private boolean succeed;

    private String content;

    private String time;

    public PushRecordBean() {
    }

    public PushRecordBean(boolean succeed, String content, String time) {
        this.succeed = succeed;
        this.content = content;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
