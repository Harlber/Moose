package moose.com.ac.retrofit.article;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Farble on 2015/8/15 11.
 * ArticleInfo
 */
public class ArticleInfo implements Serializable {
    private static final long serialVersionUID = -7611454799113987909L;
    private ArticleUser user;
    private List<String> tags;
    private String txt;
    private String description;
    private Integer contentId;
    private Integer isArticle;
    private Integer channelId;
    private Long releaseDate;
    private String title;
    private Integer isRecommend;
    private Integer views;
    private Integer comments;
    private Integer stows;
    private Integer viewOnly;
    private Integer toplevel;
    private String cover;

    public ArticleUser getUser() {
        return user;
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

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
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

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
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
}
