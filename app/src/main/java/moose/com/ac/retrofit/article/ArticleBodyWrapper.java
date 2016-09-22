package moose.com.ac.retrofit.article;

import java.io.Serializable;

/**
 * Created by Farble on 2015/8/15 11.
 * ArticleBody
 */
public class ArticleBodyWrapper implements Serializable {
    private static final long serialVersionUID = -3956012318479660215L;
    public String message;
    public int code;
    public ArticleBody data;
}
