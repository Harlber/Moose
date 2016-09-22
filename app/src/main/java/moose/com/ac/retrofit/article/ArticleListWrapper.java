package moose.com.ac.retrofit.article;

import java.io.Serializable;
import java.util.List;

public class ArticleListWrapper implements Serializable{

    public int code;
    public ArticleList data;
    public String message;

    public static class ArticleList {
        public int pageNo;
        public int pageSize;
        public int totalCount;
        public List<Article> list;
    }

    public static class ArticleUser implements Serializable{
        public String userImg;
        public int userId;
        public String username;
    }
}
