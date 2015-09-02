package moose.com.ac.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dell on 2015/8/25.
 */
public class DBCustomHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBCustomHelper";
    public static final String DATABASE_NAME = "moose.db";
    public static final int DATABASE_VERSION = 1;

    public DBCustomHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"db onCreate");
        db.execSQL(ArticleCollects.SQL_CREATE_ARTICLESTORY);//story
        db.execSQL(ArticleCollects.SQL_CREATE_ARTICLEHISTORY);//history
        db.execSQL(ArticleCollects.SQL_CREATE_COOKIES);//cookie
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i("DB", "onUpgrade::oldVersion="+i+",newVersion="+i1);
        db.execSQL(ArticleCollects.SQL_DELETE_ARTICLESTORY);
        db.execSQL(ArticleCollects.SQL_DELETE_ARTICLEHISTORY);
        db.execSQL(ArticleCollects.SQL_DELETE_COOKIE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
