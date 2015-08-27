package moose.com.ac.common;

/**
 * Created by Farble on 2015/8/14 22.
 */
public class Config {
    public static final String ARTICLE_URL = "http://api.acfun.tv";
    public static final String COMMENT_URL = "http://www.acfun.tv";
    public static final String SEARCH_URL = "http://search.acfun.tv";
    public static final String WEB_URL = "http://www.acfun.tv/a/ac";

    public static final String API_CHANNEL = "/apiserver/content/channel";
    public static final String API_COMMENT_LIST = "/comment_list_json.aspx";
    public static final String API_ARTICLE = "/apiserver/content/article";
    public static final String API_SEARCH = "/search?type=2&field=title&sortField=releaseDate&parentChannelId=63";
    public static final String API_SORT = "/apiserver/content/rank";

    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String CHANNEL_TYPE = "CHANNEL_TYPE";
    public static final String CONTENTID = "CONTENTID";
    public static final String TITLE = "TITLE";
    public static final String STORE = "st";
    public static final String NO_ST = "nost";
    /*setting*/
    public static final String TEXTSIZE = "TEXTSIZE";
    public static final int SIZE_SMALL = 1;
    public static final int SIZE_MIDDLE = 2;
    public static final int SIZE_BIG = 3;
    public static final int SIZE_LAEGE = 4;

    public static final String MODE = "MODE";
    public static final int MODE_TEXT_ONLY = 11;
    public static final int MODE_IMAGE = 12;

    public static final int OKHTTP_CLIENT_CONNECT_TIMEOUT = 3;
    public static final int OKHTTP_CLIENT_WRITE_TIMEOUT = 5;
    public static final int OKHTTP_CLIENT_READ_TIMEOUT = 5;

    public static final int PAGESIZE = 10;
    public static final int COMPLEX = 110;
    public static final int WORK = 73;
    public static final int ANIMATION = 74;
    public static final int CARTOON = 75;

    public static final int TIME_LATE = 200;

    public static final String MAX_LINE = "MAX_LINE";
    public static final String VISISTOR_MODE = "VISISTOR_MODE";
}
