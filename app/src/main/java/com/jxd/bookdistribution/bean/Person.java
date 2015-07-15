package com.jxd.bookdistribution.bean;

public class Person {
	private Integer userId;
	private String username;
	private String password;
	private String sex;
	private String createtime;
	private String modifytime;
	private String enable;
	private String address;
    private String telephone;
    private boolean isSelected;

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

    public String getTelephone(){return telephone;}
    public void setTelephone(String telephone){this.telephone=telephone;}

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

    public boolean getSelected() {    return isSelected;   }
    public void setSelected(boolean isSelected) {    this.isSelected = isSelected;   }

}
