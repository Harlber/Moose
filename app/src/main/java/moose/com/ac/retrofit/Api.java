package moose.com.ac.retrofit;


import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Farble on 2015/8/14 22.
 * ac request
 */
public interface Api {
    /*http://api.acfun.tv/apiserver/content/channel?orderBy=0&channelId=110&pageSize=20&pageNo=1*/
    @POST("/apiserver/content/channel?")
    Observable<ArticleList> getArticleList(@Query("orderBy") int orderBy, @Query("channelId") int channelId
            , @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);
}
