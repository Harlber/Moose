package moose.com.ac.retrofit;


import com.google.gson.JsonObject;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.article.ArticleBody;
import moose.com.ac.retrofit.article.ArticleList;
import moose.com.ac.retrofit.collect.Like;
import moose.com.ac.retrofit.collect.Store;
import moose.com.ac.retrofit.login.CheckIn;
import moose.com.ac.retrofit.login.LoginEntry;
import moose.com.ac.retrofit.search.SearchBody;
import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Farble on 2015/8/14 22.
 * ac request
 */
public interface Api {
    @GET(Config.API_CHANNEL)
    Observable<ArticleList> getArticleList(@Query("orderBy") int orderBy, @Query("channelId") int channelId
            , @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET(Config.API_COMMENT_LIST)
    Observable<JsonObject> getCommentList(@Query("contentId") int contentId, @Query("currentPage") int currentPage);

    @GET(Config.API_ARTICLE)
    Observable<ArticleBody> getArticleBody(@Query("contentId") int contentId);

    @GET(Config.API_SEARCH)
    Observable<SearchBody> getSearch(@Query("q") String q, @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET(Config.API_SORT)/*?channelIds=110,73,74,75*/
    Observable<ArticleList> getSortList(@Query("channelIds") String channelIds, @Query("pageSize") int pageSize);

    @POST(Config.API_LOGIN)
    Observable<LoginEntry> login(@Query("username") String username, @Query("password") String password);

    @POST(Config.API_CHENK_IN)
    Observable<CheckIn> chenkin();

    @POST(Config.API_COLLECT)
    Observable<Store> collectArticle(@Query("cId") int cId, @Query("operate") int operate);
    //operate = 1 收藏 0：取消收藏
    //http://www.acfun.tv/member/collect.aspx?cId=2147867&operate=1
    @Deprecated
    @POST(Config.API_LIKE)
    Observable<Like> likeArticle(@Query("contentId") int contentId);
    //http://www.acfun.tv/content_up.aspx?contentId=2147685

    @GET(Config.API_USERINFO)
    Observable<Response> getUserInfo(@Query("time")String time);

    @GET(Config.API_PROFILE)
    Observable<Profile> getUserProfile();
}
