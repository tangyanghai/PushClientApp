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
public class RegIdBean {
    @Id
    private Long id;

    private String regId;

    public RegIdBean() {
    }

    public RegIdBean(String regId) {
        this.regId = regId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
