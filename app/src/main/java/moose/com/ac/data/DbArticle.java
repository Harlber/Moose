package moose.com.ac.data;

import java.io.Serializable;

import moose.com.ac.retrofit.article.Article;

/**
 * Created by dell on 2015/8/25.
 */
public class DbArticle implements Serializable {
    private Article article;
    private String savedate;
    private String isfav;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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
}
