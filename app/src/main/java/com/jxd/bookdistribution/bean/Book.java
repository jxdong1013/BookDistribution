package com.jxd.bookdistribution.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 向东 on 2015/5/2.
 */
public class Book implements Serializable {
    private Integer bookId;
    private String bookName ;
    private String author;
    private String publish;
    private String isbn;
    private String publishDate;
    private String barcode;
    private Integer status;
    private BigDecimal price;
    private Integer siteId;
    private String remark;
    private String modifytime;
    private String createtime;
    private boolean isSelected;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName(){return bookName;}
    public void setBookName(String bookName){ this.bookName=bookName;}

    public String getAuthor(){return author;}
    public void setAuthor(String author){ this.author=author;}

    public String getPublish(){ return publish;}
    public void setPublish(String publish ){this.publish=publish;}

    public String getIsbn(){return isbn;}
    public void setIsbn(String isbn ){this.isbn=isbn;}

    public String getPublishDate(){return publishDate;}
    public void  setPublishDate(String publishDate){this.publishDate=publishDate;}

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSiteId(){return this.siteId;}
    public void setSiteId(Integer siteId) {this.siteId=siteId;}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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


    public boolean getSelected() {    return isSelected;   }
    public void setSelected(boolean isSelected) {    this.isSelected = isSelected;   }
}
