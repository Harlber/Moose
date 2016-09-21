package moose.com.ac.util;

import java.util.Comparator;

import moose.com.ac.retrofit.article.Article;

/**
 * Created by dell on 2015/8/26.
 */
public class SortUtil {
    public static class StoreComparator implements Comparator<Article> {

        @Override
        public int compare(Article article, Article t1) {
            return Integer.valueOf(String.valueOf(Long.valueOf(article.savedate) - Long.valueOf(t1.savedate)));

        }
    }

    public static class PushComparator implements Comparator<Article> {

        @Override
        public int compare(Article article, Article t1) {
            return Integer.valueOf((article.releaseDate - t1.releaseDate) + "");
        }
    }

    public static class HisComparator implements Comparator<Article> {

        @Override
        public int compare(Article article, Article t1) {
            return Integer.valueOf(String.valueOf(Long.valueOf(t1.savedate) - Long.valueOf(article.savedate)));

        }
    }
}
