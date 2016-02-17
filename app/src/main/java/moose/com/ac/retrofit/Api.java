package moose.com.ac.retrofit;


import com.google.gson.JsonObject;
import com.squareup.okhttp.ResponseBody;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.article.ArticleBody;
import moose.com.ac.retrofit.article.ArticleList;
import moose.com.ac.retrofit.collect.ArticleCloud;
import moose.com.ac.retrofit.collect.Like;
import moose.com.ac.retrofit.collect.Store;
import moose.com.ac.retrofit.login.CheckIn;
import moose.com.ac.retrofit.login.LoginEntry;
import moose.com.ac.retrofit.search.SearchBody;
import retrofit.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.Streaming;
import rx.Observable;

/**
 * Created by Farble on 2015/8/14 22.
 * ac request
 */
public interface Api {
    /**
     * get Article List
     *
     * @param orderBy   channel type
     * @param channelId channel id
     * @param pageNo    page number
     * @param pageSize  number of page size
     */
    @GET(Config.API_CHANNEL)
    Observable<ArticleList> getArticleList(@Query("orderBy") int orderBy, @Query("channelId") int channelId
            , @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    /**
     * get comment list
     *
     * @param contentId   article id
     * @param currentPage page number
     */
    @GET(Config.API_COMMENT_LIST)
    Observable<JsonObject> getCommentList(@Query("contentId") int contentId, @Query("currentPage") int currentPage);

    /**
     * get article body
     *
     * @param contentId article id
     */
    @GET(Config.API_ARTICLE)
    Observable<ArticleBody> getArticleBody(@Query("contentId") int contentId);

    /**
     * search by key word
     *
     * @param q        key
     * @param pageNo   page number
     * @param pageSize number in one page
     */
    @GET(Config.API_SEARCH)
    Observable<SearchBody> getSearch(@Query("q") String q, @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    /**
     * search by key word
     * <b>attention</b>
     *
     * @see #getSearch(String q, int pageNo, int pageSize)
     */
    @GET(Config.API_SEARCH)
    Observable<SearchBody> getSearch(@Query("q") String q);

    /**
     * get article list after sorting
     *
     * @param channelIds channel ids
     * @param pageSize   page size
     */
    @GET(Config.API_SORT)/*?channelIds=110,73,74,75*/
    Observable<ArticleList> getSortList(@Query("channelIds") String channelIds, @Query("pageSize") int pageSize);

    /**
     * login action
     *
     * @param username username
     * @param password password
     */
    @FormUrlEncoded
    @POST(Config.API_LOGIN)
    Observable<LoginEntry> login(@Field("username") String username, @Field("password") String password);

    /**
     * check in action
     */
    @GET(Config.API_CHENK_IN)
    Observable<CheckIn> checkIn();

    /**
     * collect article
     *
     * @param cId     article id
     * @param operate operate
     */
    @FormUrlEncoded
    @POST(Config.API_COLLECT)
    Observable<Store> collectArticle(@Field("cId") int cId, @Field("operate") int operate);

    //operate = 1 收藏 0：取消收藏
    //http://www.acfun.tv/member/collect.aspx?cId=2147867&operate=1

    /**
     * give a thumb
     *
     * @param contentId article id
     */
    @Deprecated
    @FormUrlEncoded
    @POST(Config.API_LIKE)
    Observable<Like> likeArticle(@Field("contentId") int contentId);
    //http://www.acfun.tv/content_up.aspx?contentId=2147685

    /**
     * get user information
     *
     * @param time the current system timestamp
     */
    @GET(Config.API_USERINFO)
    Observable<Response> getUserInfo(@Query("time") String time);

    /**
     * get user profile
     * need cookies
     */
    @GET(Config.API_PROFILE)
    Observable<Profile> getUserProfile();

    /**
     * get Article List from net which user stored before
     *
     * @param count     size of List in this query
     * @param channelId channel no
     * @param pageNo    current no
     *                  http://www.acfun.tv/member/collection.aspx?count=10&pageNo=1&channelId=63
     */
    @GET(Config.API_COLLECT_LIST)
    Observable<ArticleCloud> getArticleCloudList(@Query("count") int count, @Query("pageNo") int pageNo, @Query("channelId") String channelId);

    /**
     * get AppApplication Version dynamically
     *
     * @return #ResponseBody file body
     */
    @GET(Config.API_VERSION)
    @Streaming
    Observable<ResponseBody> receiveVeision();
}
