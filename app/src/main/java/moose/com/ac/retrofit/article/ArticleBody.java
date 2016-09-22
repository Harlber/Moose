package moose.com.ac.retrofit.article;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Farble on 2015/8/15 11.
 * ArticleInfo
 */
public class ArticleBody implements Serializable {
    private static final long serialVersionUID = -7611454799113987909L;

    public ArticleContent article;
    public int channelId;
    public int contentId;
    public String cover;
    public String description;
    public int display;
    public int isArticle;
    public boolean isComment;
    public int isRecommend;
    public ArticleUser owner;
    public int parentChannelId;
    public long releaseDate;
    public int status;
    public String title;
    public int topLevel;
    public long updatedAt;
    public int viewOnly;
    public ArticleVisit visit;
    public List<String> tags;

    public static class ArticleContent {
        public String content;
    }

    public static class ArticleVisit {
        public int comments;
        public int danmakuSize;
        public int goldBanana;
        public int score;
        public int stows;
        public int ups;
        public int views;
    }

    public static class ArticleUser implements Serializable {
        private static final long serialVersionUID = 6249573211851758282L;
        public String avatar;
        public int id;
        public String name;
    }
}
