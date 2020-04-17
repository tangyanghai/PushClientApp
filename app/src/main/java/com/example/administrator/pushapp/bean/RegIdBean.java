package com.example.administrator.pushapp.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/16</p>
 * <p>@for : </p>
 * <p></p>
 */
@Table(name = "regId")
public class RegIdBean {
    @Column(name = "REG_ID",property = "NOT NULL")//NAME字段非空
    private String regId;
    @Column(
            name = "ID",
            isId = true,
            autoGen = true
    )
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
