package cn.exrick.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TbThanks implements Serializable{
    private Integer id;

    private String nickName;

    private String username;

    private BigDecimal money;

    private String info;

    private Date date;

    private String email;

    private Integer state;

    private String payType;

    private String orderId;

    private String time;

    private String userId;

    private String payNum;

    private Boolean custom;

    private String tokenNum;

    private String token;

    private String passUrl;

    private String passUrl2;

    private String passUrl3;

    private String backUrl;

    private String passNotShowUrl;

    private String editUrl;

    private String delUrl;

    private String closeUrl;

    private String openUrl;

    public String getTokenNum() {
        return tokenNum;
    }

    public void setTokenNum(String tokenNum) {
        this.tokenNum = tokenNum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassUrl() {
        return passUrl;
    }

    public void setPassUrl(String passUrl) {
        this.passUrl = passUrl;
    }

    public String getPassUrl2() {
        return passUrl2;
    }

    public void setPassUrl2(String passUrl2) {
        this.passUrl2 = passUrl2;
    }

    public String getPassUrl3() {
        return passUrl3;
    }

    public void setPassUrl3(String passUrl3) {
        this.passUrl3 = passUrl3;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getPassNotShowUrl() {
        return passNotShowUrl;
    }

    public void setPassNotShowUrl(String passNotShowUrl) {
        this.passNotShowUrl = passNotShowUrl;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }

    public String getDelUrl() {
        return delUrl;
    }

    public void setDelUrl(String delUrl) {
        this.delUrl = delUrl;
    }

    public String getCloseUrl() {
        return closeUrl;
    }

    public void setCloseUrl(String closeUrl) {
        this.closeUrl = closeUrl;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
}