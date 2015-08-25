package moose.com.ac.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleUser;

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

    private List<DbArticle> getArticleLists(String tab) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<DbArticle> lists = new ArrayList<>();
        String[] projection = {
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_ID,
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_TITLE,
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_VIEWS,
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_USERNAME,
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_COMMENT,
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_RELEASEDATE,
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_SAVEDATE,
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_ISFAV
        };
        String sortOrder =
                ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_SAVEDATE + " DESC";
        Cursor c = db.query(tab, projection, null, null, null, null, sortOrder);
        Log.i(TAG, "cursor count:" + c.getCount());
        if (c.getCount() < 1) {
            c.close();
            db.close();
            return lists;
        }
        while (c.moveToFirst()) {
            DbArticle dbArticle = new DbArticle();
            Article article = new Article();
            ArticleUser user = new ArticleUser();
            article.setContentId(Integer.valueOf(c.getString(c.getColumnIndex("contentId"))));
            article.setTitle(c.getString(c.getColumnIndex("title")));
            article.setViews(Integer.valueOf(c.getString(c.getColumnIndex("views"))));
            user.setUsername(c.getString(c.getColumnIndex("username")));
            article.setComments(Integer.valueOf(c.getString(c.getColumnIndex("comment"))));
            article.setReleaseDate(Long.valueOf(c.getString(c.getColumnIndex("releaserdate"))));
            dbArticle.setSavedate(c.getString(c.getColumnIndex("savedate")));
            dbArticle.setIsfav(c.getString(c.getColumnIndex("isfav")));

            article.setUser(user);
            dbArticle.setArticle(article);
            lists.add(dbArticle);
        }

        return lists;
    }

    private boolean insertArticle(Article article, String tabName, String isfav, String date) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_AID, article.getContentId() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_TITLE, article.getTitle());
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_VIEWS, article.getViews() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_USERNAME, article.getUser().getUsername());
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_COMMENT, article.getComments() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_RELEASEDATE, article.getReleaseDate() + "");
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_SAVEDATE, date);
        values.put(ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_ISFAV, isfav);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                tabName,
                ArticleCollects.COLUMN_NAME_NULLABLE,
                values);
        db.close();
        return newRowId > 0;
    }

    private boolean deleteArticle(String tabName, String aid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define 'where' part of query.
        String selection = ArticleCollects.ArticleHistoryEntry.COLUMN_NAME_AID + " =?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(aid)};
        // Issue SQL statement.
        int status = db.delete(tabName, selection, selectionArgs);
        db.close();
        return status > 0;
    }

}
