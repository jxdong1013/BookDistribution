package com.jxd.bookdistribution.bean;

import java.io.Serializable;

/**
 * Created by jinxiangdong on 2015/5/5.
 */
public class Inventory implements Serializable{
    private Integer id;
    private String taskName;
    private Integer siteId;
    private String siteName;
    private Integer status;
    private String modifytime;
    private String createtime;

    public Integer getId(){return id;}
    public void setId(Integer id){this.id=id;}

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getSiteId(){return siteId;}
    public void setSiteId(Integer siteId){ this.siteId=siteId;}

    public String getSiteName(){return siteName;}
    public void setSiteName(String siteName){this.siteName=siteName;}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getModifytime() {
        return modifytime;
    }

    public void setModifytime(String modifytime) {
        this.modifytime = modifytime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

}
