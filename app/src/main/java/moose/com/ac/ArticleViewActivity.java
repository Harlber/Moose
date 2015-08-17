package moose.com.ac;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
    private int contendid;//article id
    private int textSize = 2;
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);
        contendid = getIntent().getIntExtra(Config.CONTENTID,1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.view_fab);
        fab.setOnClickListener(v -> {
        //
        });

        mWeb = (WebView) findViewById(R.id.view_webview);
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setUserAgentString(RxUtils.UA);
        mWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWeb.getSettings().setDefaultTextEncodingName("UTF -8");
        mWeb.getSettings().setSupportZoom(true);
        mWeb.getSettings().setBuiltInZoomControls(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mWeb.getSettings().setDisplayZoomControls(false);
        //setTextZoom(AcApp.getConfig().getInt("text_size", 0));
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
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
                mWeb.loadUrl(Config.WEB_URL+contendid+"/");
                return true;
            case R.id.action_front_view:
                createTextSizeDialog().show();
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

    protected void initData() {
        api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);
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
                            mWeb.loadData(HtmlBody, "text/html; charset=UTF-8",null);
                        }
                    }

                }));
    }

    private void addHead() {
        StringBuffer head = new StringBuffer();
        head.append("<html>");
        head.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">");
        head.append("<body>");
        head.append("<div>");

        StringBuffer body = new StringBuffer();
        body.append("</div>");
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
        html.replace("\\n","").replace("\\r","").replace("\\", "");
    }

    private void Snack(String msg) {
        Snackbar.make(mWeb, msg, Snackbar.LENGTH_SHORT).show();
    }
    private Dialog createTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleViewActivity.this);
        builder.setTitle(R.string.text_size)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.textsize, textSize, (dialog, which) -> {
                    Snack("choice:"+which);

                })
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                })
                .setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) (dialog, id) -> {
                });

        return builder.create();
    }
}
