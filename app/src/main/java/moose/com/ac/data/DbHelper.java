package moose.com.ac.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.retrofit.Profile;
import moose.com.ac.retrofit.article.Article;

/**
 * Created by dell on 2015/8/25.
 * DbHelper
 */
public class DbHelper {
    private static final String TAG = "DbHelper";
    private DBCustomHelper mDbHelper;

    public DbHelper(Context context) {
        mDbHelper = new DBCustomHelper(context);
    }

    public DBCustomHelper getmDbHelper() {
        return mDbHelper;
    }

    public boolean insertArticle(Article article, String tabName, int channel) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_AID, article.getContentId() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_TITLE, article.getTitle());
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_VIEWS, article.getViews() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_USERNAME, article.getUser().getUsername());
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_COMMENT, article.getComments() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_RELEASEDATE, article.getReleaseDate() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_SAVEDATE, article.getSavedate());
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_ISFAV, article.getIsfav());
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_CHANNEL, channel);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                tabName,
                ArticleCollects.COLUMN_NAME_NULLABLE,
                values);
        db.close();
        Log.i(TAG, "insertArticle result:" + newRowId);
        return newRowId > 0;
    }

    /**
     * @param cookie cookies information
     */
    public boolean insertCookies(String cookie) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ArticleCollects.ArticleCookies.COLUMN_NAME_COOKIES, cookie);
        long newRowId;
        newRowId = db.insert(
                ArticleCollects.ArticleCookies.TABLE_NAME,
                ArticleCollects.COLUMN_NAME_NULLABLE,
                values);
        db.close();
        Log.i(TAG, "insert Cookies result:" + newRowId);
        return newRowId > 0;
    }

    public void clearCookies() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("DELETE FROM " + ArticleCollects.ArticleCookies.TABLE_NAME);
        db.close();
    }

    public boolean insertUserInfo(Profile profile) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ArticleCollects.UserInfo.COLUMN_NAME_NICKNAME, profile.getUsername());
        values.put(ArticleCollects.UserInfo.COLUMN_NAME_SIGNWORD, profile.getSign());
        values.put(ArticleCollects.UserInfo.COLUMN_NAME_RSGDATE, profile.getRegTime() + "");//long
        values.put(ArticleCollects.UserInfo.COLUMN_NAME_GENER, profile.getGender());//int

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ArticleCollects.UserInfo.TABLE_NAME,
                ArticleCollects.COLUMN_NAME_NULLABLE,
                values);
        db.close();
        Log.i(TAG, "insertArticle result:" + newRowId);
        return newRowId > 0;

    }

    public Profile getUserInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Profile profile = new Profile();
        Cursor c = db.rawQuery("SELECT * FROM " + ArticleCollects.ArticleCookies.TABLE_NAME, null);
        Log.i(TAG, "cursor count:" + c.getCount());
        if (c.getCount() < 1) {
            c.close();
            db.close();
            mDbHelper.close();
        } else {
            if (c.moveToNext()) {
                profile.setUsername(c.getString(c.getColumnIndex("nickname")));
                profile.setSign(c.getString(c.getColumnIndex("signword")));
                profile.setRegTime(Long.valueOf(c.getString(c.getColumnIndex("regdate"))));
                profile.setGender(c.getInt(c.getColumnIndex("gener")));
            }

        }
        c.close();
        db.close();
        mDbHelper.close();
        return profile;
    }

    public List<LocalCookie> getDbCookies() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<LocalCookie> lists = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + ArticleCollects.ArticleCookies.TABLE_NAME, null);
        Log.i(TAG, "cursor count:" + c.getCount());
        if (c.getCount() < 1) {
            c.close();
            db.close();
            mDbHelper.close();
        } else {
            while (c.moveToNext()) {
                LocalCookie cookie = new LocalCookie();
                cookie.setCookie(c.getString(c.getColumnIndex("content")));

                lists.add(cookie);
            }
        }
        c.close();
        db.close();
        mDbHelper.close();
        return lists;
    }

    public boolean deleteArticle(String tabName, String aid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define 'where' part of query.
        String selection = ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_AID + " =?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(aid)};
        // Issue SQL statement.
        int status = db.delete(tabName, selection, selectionArgs);
        db.close();
        //Log.i(TAG, "deleteArticle result:" + status);
        return status > 0;
    }

    public boolean isExits(String tabName, String aid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor query = db.rawQuery("SELECT contentId FROM " + tabName + " where contentId=?", new String[]{String.valueOf(aid)});
        boolean isFav = query.getCount() > 0;
        query.close();
        db.close();
        Log.i(TAG, "isExits result:" + isFav);
        return isFav;
    }

    public void dropSql(String tab) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String deleteSql = "drop table if exists " + tab;
        db.execSQL(deleteSql);
        db.close();
    }

    public void createTab(String tab) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(tab);
        db.close();
    }

}
