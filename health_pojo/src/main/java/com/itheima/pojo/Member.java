package com.itheima.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员
 */
public class Member implements Serializable{
    private Integer id;//主键
    private String fileNumber;//档案号
    private String name;//姓名
    private String sex;//性别
    private String idCard;//身份证号
    private String phoneNumber;//手机号
    private Date regTime;//注册时间
    private String password;//登录密码
    private String email;//邮箱
    private Date  birthday;//出生日期
    private String remark;//备注


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Member(Integer id, String fileNumber, String name, String sex, String idCard, String phoneNumber, Date regTime, String password, String email, Date birthday, String remark) {
        this.id = id;
        this.fileNumber = fileNumber;
        this.name = name;
        this.sex = sex;
        this.idCard = idCard;
        this.phoneNumber = phoneNumber;
        this.regTime = regTime;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.remark = remark;
    }

    public Member() {
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", fileNumber='" + fileNumber + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", regTime=" + regTime +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", remark='" + remark + '\'' +
                '}';
    }
}
