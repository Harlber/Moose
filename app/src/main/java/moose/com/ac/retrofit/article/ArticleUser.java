package moose.com.ac.retrofit.article;

import java.io.Serializable;

/**
 * Created by Farble on 2015/8/14 22.
 * user info in ArticleList
 */
public class ArticleUser implements Serializable {
    private static final long serialVersionUID = 6249573211851758282L;
    private String username;
    private Integer userId;
    private String userImg;

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
