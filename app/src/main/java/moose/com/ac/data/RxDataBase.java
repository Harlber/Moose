package moose.com.ac.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.App;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleUser;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by dell on 2015/8/25.
 */
public class RxDataBase {
    private static final String TAG = "TAG";

    public static Observable<List<Article>> favLists = Observable.create(new Observable.OnSubscribe<List<Article>>() {
        @Override
        public void call(Subscriber<? super List<Article>> subscriber) {
            DBCustomHelper mDbHelper = new DBCustomHelper(App.getmContext());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            List<Article> lists = new ArrayList<>();
            Cursor c = db.rawQuery("SELECT * FROM " + ArticleCollects.ArticleEntry.TABLE_NAME, null);
            Log.i(TAG, "cursor count:" + c.getCount());
            if (c.getCount() < 1) {
                c.close();
                db.close();
                mDbHelper.close();
            }else {
                while (c.moveToNext()) {
                    Article article = new Article();
                    ArticleUser user = new ArticleUser();
                    article.setContentId(Integer.valueOf(c.getString(c.getColumnIndex("contentId"))));
                    article.setTitle(c.getString(c.getColumnIndex("title")));
                    article.setViews(Integer.valueOf(c.getString(c.getColumnIndex("views"))));
                    user.setUsername(c.getString(c.getColumnIndex("username")));
                    article.setComments(Integer.valueOf(c.getString(c.getColumnIndex("comment"))));
                    article.setReleaseDate(Long.valueOf(c.getString(c.getColumnIndex("releaserdate"))));
                    article.setSavedate(c.getString(c.getColumnIndex("savedate")));
                    article.setIsfav(c.getString(c.getColumnIndex("isfav")));

                    article.setUser(user);
                    lists.add(article);
                }
            }
            c.close();
            db.close();
            mDbHelper.close();
            subscriber.onNext(lists);
            subscriber.onCompleted();
        }
    });

}
