package moose.com.ac.retrofit.article;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Farble on 2015/8/14 22.
 * "success": true,
 * "msg": "查询成功",
 * "status": 200,
 * "data": {
 * "page": {
 * "pageNo": 1,
 * "pageSize": 20,
 * "totalCount": 115697,
 * "orderBy": 0,
 * "list": [
 * {
 * "user": {
 * "username": "心落",
 * "userId": 285615,
 * "userImg": "http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201209/141735379wxp.jpg"
 * },
 * "tags": [],
 * "description": "ti5过啦，又到了转会留言飞起的时候啦，那么up带着各位看官看看到底现在有哪些流言和信息~",
 * "contentId": 2099054,
 * "isArticle": 1,
 * "channelId": 110,
 * "releaseDate": 1439561591000,
 * "title": "近期dota2转会信息流言",
 * "isRecommend": 0,
 * "views": 2,
 * "comments": 1,
 * "stows": 0,
 * "viewOnly": 0,
 * "toplevel": 0,
 * "cover": "http://cdn.aixifan.com/dotnet/20120923/style/image/cover.png"
 * },
 */
public class Article implements Serializable {
    private static final long serialVersionUID = 920177008898972172L;
    private ArticleUser user;
    private List<String> tags;
    private String tudouDomain;
    private String description;
    private Integer contentId;
    private Integer isArticle;
    private Integer channelId;
    private Long releaseDate;
    private String title;
    private String isRecommend;
    private Integer views;
    private Integer comments;
    private Integer stows;
    private Integer viewOnly;
    private Integer toplevel;
    private String cover;

    private String savedate;
    private String isfav;
    private String channelType;//used for chart

    public ArticleUser getUser() {
        return user;
    }

    public String getTudouDomain() {
        return tudouDomain;
    }

    public void setTudouDomain(String tudouDomain) {
        this.tudouDomain = tudouDomain;
    }

    public void setUser(ArticleUser user) {
        this.user = user;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Integer getIsArticle() {
        return isArticle;
    }

    public void setIsArticle(Integer isArticle) {
        this.isArticle = isArticle;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getStows() {
        return stows;
    }

    public void setStows(Integer stows) {
        this.stows = stows;
    }

    public Integer getViewOnly() {
        return viewOnly;
    }

    public void setViewOnly(Integer viewOnly) {
        this.viewOnly = viewOnly;
    }

    public Integer getToplevel() {
        return toplevel;
    }

    public void setToplevel(Integer toplevel) {
        this.toplevel = toplevel;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSavedate() {
        return savedate;
    }

    public void setSavedate(String savedate) {
        this.savedate = savedate;
    }

    public String getIsfav() {
        return isfav;
    }

    public void setIsfav(String isfav) {
        this.isfav = isfav;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
}
