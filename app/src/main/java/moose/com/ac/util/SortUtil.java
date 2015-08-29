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
            return Integer.valueOf(String.valueOf(Long.valueOf(article.getSavedate()) - Long.valueOf(t1.getSavedate())));

        }
    }

    public static class PushComparator implements Comparator<Article> {

        @Override
        public int compare(Article article, Article t1) {
            return Integer.valueOf((article.getReleaseDate() - t1.getReleaseDate()) + "");
        }
    }

    public static class HisComparator implements Comparator<Article> {

        @Override
        public int compare(Article article, Article t1) {
            return Integer.valueOf(String.valueOf(Long.valueOf(t1.getSavedate()) - Long.valueOf(article.getSavedate())));

        }
    }
}
