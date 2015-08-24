package moose.com.ac;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.ArticleBody;
import moose.com.ac.ui.view.MultiSwipeRefreshLayout;
import moose.com.ac.ui.view.ObservableWebView;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.DisplayUtil;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.ScrollFABBehavior;
import retrofit.RetrofitError;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/16 20.
 * WebView TextSize:
 * <li>SMALLER</li>
 * <li>NORMAL</li>
 * <li>LARGER</li>
 * <li>LARGEST</li>
 */
public class ArticleViewActivity extends AppCompatActivity implements ObservableWebView.OnScrollChangedCallback {
    private static final String TAG = "ArticleViewActivity";
    private static final int FAB_SHOW = 0x0000aa;
    private static final int FAB_HIDE = 0x0000bb;
    private ObservableWebView mWeb;
    private FloatingActionButton fab;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api;
    private Document mDocument;

    private String HtmlBody;//get body from network
    private String HtmlBodyClone;//get body from network
    private WebSettings settings;
    private int contendid;//article id
    private int fabStatus = FAB_SHOW;
    private String title = "";//default
    private String contend;
    private int toolbarHeight;
    private int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        contendid = getIntent().getIntExtra(Config.CONTENTID, 1);
        toolbarHeight = DisplayUtil.dip2px(this, 56f);
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        contend = "ac" + contendid;
        getSupportActionBar().setTitle(contend);

        fab = (FloatingActionButton) findViewById(R.id.view_fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, BigNewsActivity.class);
            intent.putExtra(Config.CONTENTID, contendid);
            intent.putExtra(Config.TITLE, title);
            startActivity(intent);
        });

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.web_swipe);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.view_fab);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);

        mWeb = (ObservableWebView) findViewById(R.id.view_webview);
        settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(RxUtils.UA);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDefaultTextEncodingName("UTF -8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mWeb.getSettings().setDisplayZoomControls(false);
        //setTextZoom(AcApp.getConfig().getInt("text_size", 0));
        mWeb.setWebViewClient(new Client());
        if (Build.VERSION.SDK_INT >= 11)
            mWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        mWeb.setOnScrollChangedCallback(this);
        level = CommonUtil.getTextSize();
        setText();
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
                mWeb.loadUrl(Config.WEB_URL + contendid + "/");
                return true;
            case R.id.action_front_view:
                createTextSizeDialog().show();
                return true;
            case R.id.action_module_view:
                createModeDialog().show();
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
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);
        subscription.add(api.getArticleBody(contendid)
                .observeOn(AndroidSchedulers.mainThread())
                .retry(1)
                .subscribe(new Observer<ArticleBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mSwipeRefreshLayout.setRefreshing(false);//show progressbar
                        mSwipeRefreshLayout.setEnabled(true);
                        if (e instanceof RetrofitError) {
                            if (((RetrofitError) e).getResponse() != null) {
                                Snack(getString(R.string.net_work) + ((RetrofitError) e).getResponse().getStatus());
                            } else {
                                Snack(getString(R.string.no_network));
                            }

                        }
                    }

                    @Override
                    public void onNext(ArticleBody articleBody) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(false);
                        if (!articleBody.isSuccess()) {
                            Snack(articleBody.getMsg());
                        } else {
                            HtmlBody = articleBody.getData().getFullArticle().getTxt();
                            title = articleBody.getData().getFullArticle().getTitle();
                            dealBody(HtmlBody);
                            addHead();
                            //mSwipeRefreshLayout.setRefreshing(true);//show progressbar
                            if (CommonUtil.getMode() == 1) {
                                filterImg(HtmlBody);
                            }
                            mWeb.loadData(HtmlBody, "text/html; charset=UTF-8", null);
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
        HtmlBody = head.toString() + index + body.toString();
        HtmlBodyClone = head.toString() + index + body.toString();
    }

    private void dealBody(String html) {
        //1.deal image <img src=\"http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg\" />
        //into <img src="http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg" />
        //2.\n
        //3.\r
        //4.other \
        html.replace("\\n", "").replace("\\r", "").replace("\\", "");
    }

    private void filterImg(String str) {
        mDocument = Jsoup.parse(str);

        Elements imgs = mDocument.select("img");
        for (int imgIndex = 0; imgIndex < imgs.size(); imgIndex++) {
            Element img = imgs.get(imgIndex);
            String src = img.attr("src").trim();
            if (TextUtils.isEmpty(src))
                continue;
            Uri parsedUri = Uri.parse(src);
            if ("file".equals(parsedUri.getScheme()))
                continue;
            if (parsedUri.getPath() == null)
                continue;
            if (!"http".equals(parsedUri.getScheme())) {
                parsedUri = parsedUri.buildUpon()
                        .scheme("http")
                        .authority("www.acfun.tv")
                        .build();
            }
            // url may have encoded path
            parsedUri = parsedUri.buildUpon().path(parsedUri.getPath()).build();
            src = parsedUri.toString();
            img.attr("org", src);

            img.after("<p >[图片]</p>");
            img.remove();
            img.removeAttr("style");
            HtmlBody = mDocument.toString();
        }
    }

    private void Snack(String msg) {
        Snackbar.make(fab, msg, Snackbar.LENGTH_SHORT).show();
    }

    private Dialog createTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleViewActivity.this);
        builder.setTitle(R.string.text_size)
                .setSingleChoiceItems(R.array.textsize, CommonUtil.getTextSize(), (dialog, which) -> {
                    //zoom text
                    level = settings.getTextZoom();
                    setZoom(which);
                })
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    CommonUtil.setTextSize(level);
                    setText();
                })
                .setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) (dialog, id) -> {
                });

        return builder.create();
    }

    private Dialog createModeDialog() {
        int oldMode = CommonUtil.getMode();
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleViewActivity.this);
        builder.setTitle(R.string.text_mode)
                .setSingleChoiceItems(R.array.mode, CommonUtil.getMode(), (dialog, which) -> {
                            CommonUtil.setMode(which);
                        }
                )
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    if (oldMode != CommonUtil.getMode()) {
                        filterImg(HtmlBody);//whenever mode what,do this
                        mWeb.loadData(CommonUtil.getMode() == 0 ? HtmlBodyClone : HtmlBody, "text/html; charset=UTF-8", null);
                    }
                })
                .setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) (dialog, id) -> {
                    CommonUtil.setMode(oldMode);
                });

        return builder.create();
    }

    private void setZoom(int lev) {
        level = lev;
    }

    @SuppressWarnings("deprecation")
    private void setText() {
        switch (level) {
            case 0:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
                break;
            case 1:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case 2:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case 3:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.LARGER);
                break;
            default:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                break;

        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (level > 4) {
                level = 3;
                CommonUtil.setTextSize(level + 1);//save text size
            }
            mWeb.getSettings().setTextSize(WebSettings.TextSize.values()[level + 1]);
        } else
            mWeb.getSettings().setTextZoom(100 + level * 25);
    }

    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        if (t > toolbarHeight) {
            getSupportActionBar().setTitle(title);
        } else {
            getSupportActionBar().setTitle(contend);
        }
        if (t - oldt > 0) {
            if (fabStatus == FAB_SHOW) {
                //hide fab
                ScrollFABBehavior.animateOut(fab);
                fabStatus = FAB_HIDE;
            }
        } else {
            if (fabStatus == FAB_HIDE) {
                //show fab
                ScrollFABBehavior.animateIn(fab);
                fabStatus = FAB_SHOW;
            }
        }
    }


    class Client extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setRefreshing(true);//show progressbar
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
        }
    }
}
