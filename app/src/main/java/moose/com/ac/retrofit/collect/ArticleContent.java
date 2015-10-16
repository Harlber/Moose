package moose.com.ac.retrofit.collect;

import java.io.Serializable;

/**
 * Created by dell on 2015/10/16.
 * {
 * "username": "羽烈王",
 * "userId": 302870,
 * "userImg": "http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201506/11205001quo6.jpg",
 * "avatar": "http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201506/11205001quo6.jpg",
 * "sign": "在自卑这个领域，我可是相当自信的。",
 * "aid": 2263649,
 * "cid": 2263649,
 * "title": "世界上竟有三国军队被称“第二解放军”",
 * "titleImg": "http://cdn.aixifan.com/dotnet/20120923/style/image/cover-day.png",
 * "url": "/a/ac2263649",
 * "releaseDate": 1444721028000,
 * "description": "世界上竟有三国军队被称“第二解放军”",
 * "channelId": 110,
 * "tags": "世界,军队,第二解放军,解放军",
 * "contentClass": "",
 * "author": "羽烈王",
 * "allowDanmaku": 1,
 * "views": 19818,
 * "stows": 36,
 * "comments": 204,
 * "score": 0,
 * "time": 0,
 * "isArticle": 1,
 * "success": true,
 * "errorlog": ""
 * }
 */
public class ArticleContent implements Serializable {
    private static final long serialVersionUID = 8827494376361509552L;
    private String username;
    private Long userId;
    private String userImg;
    private String avatar;
    private String sign;
    private Long aid;
    private Long cid;
    private String title;
    private String titleImg;
    private String url;
    private Long releaseDate;
    private String description;
    private int channelId;//channel
    private String tags;
    private String contentClass;
    private String author;
    private int allowDanmaku;
    private Long views;
    private Long stows;
    private Long comments;
    private Long score;
    private Long time;
    private Long isArticle;
    private boolean success;
    private String errorlog;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContentClass() {
        return contentClass;
    }

    public void setContentClass(String contentClass) {
        this.contentClass = contentClass;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAllowDanmaku() {
        return allowDanmaku;
    }

    public void setAllowDanmaku(int allowDanmaku) {
        this.allowDanmaku = allowDanmaku;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getStows() {
        return stows;
    }

    public void setStows(Long stows) {
        this.stows = stows;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getIsArticle() {
        return isArticle;
    }

    public void setIsArticle(Long isArticle) {
        this.isArticle = isArticle;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorlog() {
        return errorlog;
    }

    public void setErrorlog(String errorlog) {
        this.errorlog = errorlog;
    }
}
