package com.yugoo.gis.pojo.po;

import java.util.Date;

/**
 * Created by nihao on 18/5/9.
 */
public class ReginPO {
    private Integer id;
    private String name;
    private String scope;
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
