package com.example.experiment03.bmob;

import java.math.BigDecimal;

import cn.bmob.v3.BmobObject;

public class Task extends BmobObject {
//    任务标题
    private String title;
//    金钱数额
    private BigDecimal money;
//    属性一
    private String show1;
//    属性二
    private String show2;
//    已完成数
    private Integer CompletedNumber;
//    剩余数
    private Integer RemainingNumber;
//    任务ID
    private Integer Id;
//    步骤一
    private String step1;
//    步骤二
    private String step2;
//    公告
    private String notice;
//    发布者昵称
    private String PublisherNickname;
//    发布者ID
    private Integer PublisherId;

    public Task() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getShow1() {
        return show1;
    }

    public void setShow1(String show1) {
        this.show1 = show1;
    }

    public String getShow2() {
        return show2;
    }

    public void setShow2(String show2) {
        this.show2 = show2;
    }

    public Integer getCompletedNumber() {
        return CompletedNumber;
    }

    public void setCompletedNumber(Integer completedNumber) {
        CompletedNumber = completedNumber;
    }

    public Integer getRemainingNumber() {
        return RemainingNumber;
    }

    public void setRemainingNumber(Integer remainingNumber) {
        RemainingNumber = remainingNumber;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getStep1() {
        return step1;
    }

    public void setStep1(String step1) {
        this.step1 = step1;
    }

    public String getStep2() {
        return step2;
    }

    public void setStep2(String step2) {
        this.step2 = step2;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPublisherNickname() {
        return PublisherNickname;
    }

    public void setPublisherNickname(String publisherNickname) {
        PublisherNickname = publisherNickname;
    }

    public Integer getPublisherId() {
        return PublisherId;
    }

    public void setPublisherId(Integer publisherId) {
        PublisherId = publisherId;
    }
}
