package moose.com.ac;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import org.jsoup.nodes.Document;

import java.io.IOException;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.ArticleBody;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/16 20.
 */
public class ArticleViewActivity extends AppCompatActivity {
    private static final String TAG = "ArticleViewActivity";
    private WebView mWeb;
    private FloatingActionButton fab;

    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api;
    private String HtmlBody;//get body from network
    private int contendid;

    private Document mDoc;
    /*wap http://www.acfun.tv/lite/v/#ac=2104712*/

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);
        contendid = getIntent().getIntExtra(Config.CONTENTID,1);
        //mProgress = findViewById(R.id.loading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.view_fab);
        mWeb = (WebView) findViewById(R.id.view_webview);
        mWeb.getSettings().setAllowFileAccess(true);
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setUserAgentString(RxUtils.UA);
        mWeb.getSettings().setUseWideViewPort(true);
        mWeb.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 11)
            mWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
        initData();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ArticleViewActivity.this.finish();
                return true;
            case R.id.action_module_wap:
                mWeb.loadUrl("http://www.acfun.tv/lite/v/#ac="+contendid);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscription);
    }

    protected DialogInterface.OnClickListener mErrorDialogListener = (dialog, which) -> {
        dialog.dismiss();
        if (which == DialogInterface.BUTTON_POSITIVE) {
            initData();
        } else {
            finish();
        }
    };

    protected void showErrorDialog() {
        try {
            Drawable icon = Drawable.createFromStream(getAssets().open("emotion/ais/27.gif"), "27.gif");
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            new AlertDialog.Builder(this).setTitle("加载失败！").setIcon(icon).setMessage("是否重试？").setPositiveButton("重试", mErrorDialogListener)
                    .setNegativeButton("算了", mErrorDialogListener).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void initData() {
        api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);
        setSupportProgressBarIndeterminateVisibility(true);
        subscription.add(api.getArticleBody(contendid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticleBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snack(getString(R.string.network_exception));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArticleBody articleBody) {
                        if (!articleBody.isSuccess()) {
                            Snack(articleBody.getMsg());
                        } else {
                            HtmlBody = articleBody.getData().getFullArticle().getTxt();
                            dealBody(HtmlBody);
                            addHead();
                            mWeb.loadData(HtmlBody, "text/html; charset=UTF-8", null);
                            Log.e(TAG,"source:"+HtmlBody);
                        }
                    }

                }));
    }

    private void addHead() {
        StringBuffer head = new StringBuffer();
        head.append("<html lang=\"zh-CN\">");
        head.append("<body>");

        StringBuffer body = new StringBuffer();
        body.append("</body>");
        body.append("</html>");
        String index = HtmlBody;
        HtmlBody = head.toString()+index+body.toString();
    }

    private void dealBody(String html) {
        //1.deal image <img src=\"http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg\" />
        //into <img src="http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg" />
        //2.\n
        //3.\r
        //4.other \
        html.replace("\\n","").replace("\\r","").replace("\\","");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        //mProgress.setVisibility(visible ? View.VISIBLE : View.GONE);
        mWeb.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    /**
     * @param script         the JavaScript to execute.
     * @param resultCallback A callback to be invoked when the script execution completes
     *                       with the result of the execution (if any). May be null if no
     *                       notificaion of the result is required.
     */
    public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                mWeb.evaluateJavascript(script, resultCallback);
                return;
            } catch (Exception ignored) {
            }
        }
        mWeb.loadUrl(script);
    }

    private void Snack(String msg) {
        Snackbar.make(mWeb, msg, Snackbar.LENGTH_SHORT).show();
    }
}
