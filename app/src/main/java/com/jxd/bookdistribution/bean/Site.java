package com.jxd.bookdistribution.bean;

/**
 * Created by 向东 on 2015/5/2.
 */
public class Site {
    private String siteName;
    private Integer siteId;
    private String address;
    private String telephone;
    private String remark;
    private String modifyTime;
    private String createTime;
    private boolean isSelected=false;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getAddress(){return address;}

    public void setAddress(String address){this.address=address;}

    public String getTelephone(){return this.telephone;}
    public void setTelephone(String telephone){this.telephone=telephone;}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getModifytime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean getSelected(){return isSelected;}
    public void setSelected(boolean isSelected){this.isSelected=isSelected;}

}
