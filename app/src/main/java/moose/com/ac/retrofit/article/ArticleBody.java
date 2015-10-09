package moose.com.ac.retrofit.article;

import java.io.Serializable;

/**
 * Created by Farble on 2015/8/15 11.
 * ArticleBody
 */
public class ArticleBody implements Serializable {
    private static final long serialVersionUID = -3956012318479660215L;
    private boolean success;
    private String msg;
    private Integer status;
    private FullArticle data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public FullArticle getData() {
        return data;
    }

    public void setData(FullArticle data) {
        this.data = data;
    }

    public static class FullArticle{
        private ArticleInfo fullArticle;

        public ArticleInfo getFullArticle() {
            return fullArticle;
        }

        public void setFullArticle(ArticleInfo fullArticle) {
            this.fullArticle = fullArticle;
        }
    }
}
