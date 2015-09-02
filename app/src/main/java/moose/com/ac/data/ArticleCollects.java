package moose.com.ac.data;

import android.provider.BaseColumns;

/**
 * Created by dell on 2015/8/25.
 * http://developer.android.com/training/basics/data-storage/databases.html
 * max lines 500?
 */
public final class ArticleCollects {
    public static final String TAG = "ArticleCollects";
    public static final String TEXT_TYPE = " TEXT";
    public static final String COLUMN_NAME_NULLABLE = "EMPTY_COLUMN";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String VARCHAR_TITLE = " VARCHAR(200)";
    public static final String VARCHAR_NAME = " VARCHAR(20)";
    public static final String PRIMARY = " PRIMARY KEY ";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ARTICLESTORY =
            "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
                    ArticleEntry._ID + PRIMARY +COMMA_SEP+
                    ArticleEntry.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_AID + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_TITLE + VARCHAR_TITLE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_VIEWS + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_USERNAME + VARCHAR_NAME + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_COMMENT + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_RELEASEDATE + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_SAVEDATE + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_ISFAV + TEXT_TYPE +COMMA_SEP+
                    ArticleEntry.COLUMN_NAME_CHANNEL + INTEGER_TYPE +
            ")";

    public static final String SQL_DELETE_ARTICLESTORY =
            "DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME;

    public static final String SQL_CREATE_ARTICLEHISTORY =
            "CREATE TABLE " + ArticleHistoryEntry.TABLE_NAME + " (" +
                    ArticleHistoryEntry._ID + PRIMARY +COMMA_SEP+
                    ArticleHistoryEntry.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_AID + TEXT_TYPE + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_TITLE + VARCHAR_TITLE + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_VIEWS + TEXT_TYPE + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_USERNAME + VARCHAR_NAME + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_COMMENT + TEXT_TYPE + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_RELEASEDATE + TEXT_TYPE + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_SAVEDATE + TEXT_TYPE + COMMA_SEP +
                    ArticleHistoryEntry.COLUMN_NAME_ISFAV + TEXT_TYPE +COMMA_SEP+
                    ArticleHistoryEntry.COLUMN_NAME_CHANNEL + INTEGER_TYPE +
                    ")";

    public static final String SQL_DELETE_ARTICLEHISTORY =
            "DROP TABLE IF EXISTS " + ArticleHistoryEntry.TABLE_NAME;

    public static final String SQL_CREATE_COOKIES =
            "CREATE TABLE " + ArticleCookies.TABLE_NAME + " (" +
                    ArticleCookies._ID + PRIMARY +COMMA_SEP+
                    ArticleCookies.COLUMN_NAME_COOKIES + VARCHAR_TITLE +
                    ")";

    public static final String SQL_DELETE_COOKIE =
            "DROP TABLE IF EXISTS " + ArticleCookies.TABLE_NAME;
    public ArticleCollects() {
    }

    public static abstract class ArticleEntry implements BaseColumns{
        public static final String TABLE_NAME = "story";
        public static final String COLUMN_NAME_ID = "id";

        public static final String COLUMN_NAME_AID = "contentId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VIEWS = "views";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_RELEASEDATE = "releaserdate";
        public static final String COLUMN_NAME_SAVEDATE = "savedate";
        public static final String COLUMN_NAME_ISFAV = "isfav";
        public static final String COLUMN_NAME_CHANNEL = "channelid";
    }
    public static abstract class ArticleHistoryEntry implements BaseColumns{
        public static final String TABLE_NAME = "histroy";
        public static final String COLUMN_NAME_ID = "id";

        public static final String COLUMN_NAME_AID = "contentId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VIEWS = "views";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_RELEASEDATE = "releaserdate";
        public static final String COLUMN_NAME_SAVEDATE = "savedate";
        public static final String COLUMN_NAME_ISFAV = "isfav";
        public static final String COLUMN_NAME_CHANNEL = "channelid";
    }

    public static abstract class ArticleCookies implements BaseColumns{
        public static final String TABLE_NAME = "cookie";
        public static final String COLUMN_NAME_COOKIES = "content";
    }
}
