package moose.com.ac.retrofit;


import com.google.gson.JsonObject;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.article.ArticleBody;
import moose.com.ac.retrofit.article.ArticleList;
import moose.com.ac.retrofit.search.SearchBody;
import retrofit.http.GET;
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
    Observable<ArticleList> getSortList(@Query("channelIds")String channelIds,@Query("pageSize") int pageSize);
}
