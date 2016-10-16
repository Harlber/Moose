package moose.com.ac.retrofit;


import com.google.gson.JsonObject;
import com.squareup.okhttp.ResponseBody;

import java.util.Map;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.article.ArticleBodyWrapper;
import moose.com.ac.retrofit.article.ArticleListWrapper;
import moose.com.ac.retrofit.article.ShadowArticleBody;
import moose.com.ac.retrofit.collect.ArticleCloud;
import moose.com.ac.retrofit.collect.Like;
import moose.com.ac.retrofit.collect.Store;
import moose.com.ac.retrofit.comment.CommentListWrapper;
import moose.com.ac.retrofit.comment.CommentSend;
import moose.com.ac.retrofit.login.CheckIn;
import moose.com.ac.retrofit.login.LoginEntry;
import moose.com.ac.retrofit.search.SearchBody;
import retrofit.Response;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
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
     * @param sort   sort type
     * @param channelId channel id
     * @param pageNo    page number
     * @param pageSize  number of page size
     */
    @GET(Config.API_CHANNEL)
    Observable<ArticleListWrapper> getArticleList(@Query("sort") int sort, @Query("channelIds") int channelId
            , @Query("pageSize") int pageSize, @Query("pageNo") int pageNo);

    /**
     * get comment list
     *
     * @param contentId article id
     * @param pageSize page size
     * @param pageNo page number
     */
    @GET(Config.API_COMMENT_LIST)
    Observable<CommentListWrapper> getCommentList(@Query("contentId") int contentId, @Query("pageSize") int pageSize, @Query("pageNo") int pageNo);

    /**
     * get article body
     *
     * @param contentId article id
     */
    @GET(Config.API_ARTICLE)
    Observable<ArticleBodyWrapper> getArticleBody(@Query("contentId") int contentId);

    /**
     * get article body
     *
     * @param contentId article id
     */
    @GET(Config.API_ARTICLE_NEW)
    Observable<ArticleBodyWrapper> getNewUrlArticleBody(@Path("id") int contentId);

    /**
     * get article body
     *
     * @param contentId article id
     */
    @GET("/articles/{contentId}")
    Observable<ShadowArticleBody> getShadowArticleBody(@Path("contentId") String contentId);

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
    Observable<ArticleListWrapper> getSortList(@Query("channelIds") String channelIds, @Query("pageSize") int pageSize);

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
     * see #signIn
     */
    @Deprecated
    @GET(Config.API_CHENK_IN)
    Observable<CheckIn> checkIn();

    /**
     * http://webapi.acfun.tv/record/actions/signin?channel=0&date=1455873634311
     *
     * @param channel 0
     * @param date    timestamp
     */
    @POST(Config.API_SIGNIN)
    Observable<JsonObject> signIn(@Query("channel") String channel, @Query("date") Long date);

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

    /**
     * post comment to server
     *
     * @param map params collection
     */
    @FormUrlEncoded
    @POST(Config.API_COMMENT)
    Observable<CommentSend> sendComment(@FieldMap Map<String, String> map);
}
