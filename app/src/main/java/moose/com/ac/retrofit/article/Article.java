package moose.com.ac.retrofit.article;

import java.io.Serializable;
import java.util.List;

/**
 * {
 "channelId": 75,
 "comments": 26,
 "contentId": "3119706",
 "cover": "http://cdn.aixifan.com/dotnet/20120923/style/image/cover-night.png",
 "description": "请填写文章简介。简介中不得包含令人反感的信息，且长度应在10到255个字符之间。",
 "isArticle": 1,
 "isRecommend": 1,
 "releaseDate": 1474360036000,
 "stows": 21,
 "tags": ["剑风传奇"],
 "title": "【剑风传奇】340-342",
 "toplevel": 1,
 "user": {
 "userImg": "http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201209/281420416b5c.jpg",
 "userId": 314662,
 "username": "请叫我大萌德"
 },
 "viewOnly": 0,
 "views": 3130
 *}
 */
public class Article implements Serializable {
    private static final long serialVersionUID = 920177008898972172L;
    public String tudouDomain;

    public int channelId;
    public int comments;
    public String contentId;
    public String cover;
    public String description;
    public int isArticle;
    public int isRecommend;
    public long releaseDate;
    public int stows;
    public String title;
    public int toplevel;
    public ArticleListWrapper.ArticleUser user;
    public int viewOnly;
    public int views;
    public List<String> tags;

    public String savedate;
    public String isfav;
    public String channelType;//used for chart

}
