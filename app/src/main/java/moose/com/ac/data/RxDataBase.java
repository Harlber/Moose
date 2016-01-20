package moose.com.ac.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.AppApplication;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleUser;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by dell on 2015/8/25.
 */
public class RxDataBase {
    private static final String TAG = "TAG";
    private String tabName;

    public RxDataBase(String tabName) {
        this.tabName = tabName;
    }

    public Observable<List<Article>> favLists = Observable.create(new Observable.OnSubscribe<List<Article>>() {
        @Override
        public void call(Subscriber<? super List<Article>> subscriber) {
            DBCustomHelper mDbHelper = new DBCustomHelper(AppApplication.getmContext());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            List<Article> lists = new ArrayList<>();
            Cursor c = db.rawQuery("SELECT * FROM " + tabName, null);
            Log.i(TAG, "cursor count:" + c.getCount());
            if (c.getCount() < 1) {
                c.close();
                db.close();
                mDbHelper.close();
            } else {
                while (c.moveToNext()) {
                    Article article = new Article();
                    ArticleUser user = new ArticleUser();
                    article.setContentId(c.getInt(c.getColumnIndex("contentId")));
                    article.setTitle(c.getString(c.getColumnIndex("title")));
                    article.setViews(c.getInt(c.getColumnIndex("views")));
                    user.setUsername(c.getString(c.getColumnIndex("username")));
                    article.setComments(c.getInt(c.getColumnIndex("comment")));
                    article.setReleaseDate(c.getLong(c.getColumnIndex("releaserdate")));
                    article.setSavedate(c.getString(c.getColumnIndex("savedate")));
                    article.setIsfav(c.getString(c.getColumnIndex("isfav")));
                    article.setChannelId(c.getInt(c.getColumnIndex("channelid")));

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

    public Observable<List<LocalCookie>> cookieCollect = Observable.create(subscriber -> {
        DBCustomHelper mDbHelper = new DBCustomHelper(AppApplication.getmContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<LocalCookie> lists = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + tabName, null);
        Log.i(TAG, "cursor count:" + c.getCount());
        if (c.getCount() < 1) {
            c.close();
            db.close();
            mDbHelper.close();
        } else {
            while (c.moveToNext()) {
                LocalCookie localCookie = new LocalCookie();
                localCookie.setCookie(c.getString(c.getColumnIndex("content")));
                lists.add(localCookie);
            }
        }
        c.close();
        db.close();
        mDbHelper.close();
        subscriber.onNext(lists);
        subscriber.onCompleted();

    });

    //get cookies from database

    public Observable<Integer> dropTable = Observable.create(subscriber -> {
        DBCustomHelper mDbHelper = new DBCustomHelper(AppApplication.getmContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int result = db.delete(tabName, null, null);
        db.close();
        subscriber.onNext(result);
        subscriber.onCompleted();
    });

    public Observable<Integer> ReSetInstance = Observable.create(subscriber -> {
        DBCustomHelper mDbHelper = new DBCustomHelper(AppApplication.getmContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //create table after dropping
        db.execSQL(ArticleCollects.SQL_DELETE_ARTICLESTORY);//
        db.execSQL(ArticleCollects.SQL_DELETE_ARTICLEHISTORY);//
        db.execSQL(ArticleCollects.SQL_CREATE_ARTICLESTORY);//story
        db.execSQL(ArticleCollects.SQL_CREATE_ARTICLEHISTORY);//history
    });

}
