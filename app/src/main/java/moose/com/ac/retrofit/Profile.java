package moose.com.ac.retrofit;

import java.io.Serializable;

/**
 * Created by dell on 2015/9/2.
 * {
 "qq": "",
 "userImg": "http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201505/1413200735rj.jpg",
 "gender": 1,  男 0:女 -1 不公开
 "emailCheck": 1,
 "mobileCheck": 0,
 "sign": "一直等待",
 "comefrom": "",
 "blog": "",
 "sextrend": 1, 1：异性恋  0:同性恋 2:双性恋 -1:不公开
 "realname": "",
 "uid": 880780,
 "regTime": 1404209153000,
 "success": true,
 "email": "29*@qq.com",
 "username": "hhh"
 }
 {"result":"请先登录","success":false,"status":401,"info":"请先登录"}
 */
public class Profile implements Serializable {
    private String qq;
    private String userImg;
    private Integer gender;
    private Integer emailCheck;
    private Integer mobileCheck;
    private String sign;
    private String comefrom;
    private String blog;
    private Integer sextrend;
    private String realname;
    private Long uid;
    private Long regTime;
    private boolean success;
    private String email;
    private String username;

    private String result;
    private Integer status;
    private String info;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getEmailCheck() {
        return emailCheck;
    }

    public void setEmailCheck(Integer emailCheck) {
        this.emailCheck = emailCheck;
    }

    public Integer getMobileCheck() {
        return mobileCheck;
    }

    public void setMobileCheck(Integer mobileCheck) {
        this.mobileCheck = mobileCheck;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getComefrom() {
        return comefrom;
    }

    public void setComefrom(String comefrom) {
        this.comefrom = comefrom;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public Integer getSextrend() {
        return sextrend;
    }

    public void setSextrend(Integer sextrend) {
        this.sextrend = sextrend;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRegTime() {
        return regTime;
    }

    public void setRegTime(Long regTime) {
        this.regTime = regTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
