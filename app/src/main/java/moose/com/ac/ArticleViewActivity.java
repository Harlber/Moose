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
import android.os.Handler;
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
import moose.com.ac.data.ArticleCollects;
import moose.com.ac.data.DbHelper;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.Article;
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
    private String TAB_NAME = ArticleCollects.ArticleEntry.TABLE_NAME;
    private ObservableWebView mWeb;
    private FloatingActionButton fab;
    private MenuItem menFav;
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
    private String user = "";//default
    private String contend;
    private int toolbarHeight;
    private int level = 1;
    private boolean isFav = false;
    private boolean isWebViewLoading = false;
    private Article article;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        dbHelper = new DbHelper(this);

        article = (Article) getIntent().getSerializableExtra(Config.ARTICLE);
        isFav = dbHelper.isExits(TAB_NAME, String.valueOf(article.getContentId()));
        contendid = article.getContentId();
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
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        new Handler().postDelayed(() -> {
            menFav.setTitle(isFav ? getString(R.string.store_it) : getString(R.string.cancel_store));
            initData();
        }, Config.TIME_LATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_article, menu);
        menFav = menu.findItem(R.id.action_store);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ArticleViewActivity.this.finish();
                return true;
            case R.id.action_module_wap:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(Config.WEB_URL + contendid + "/");
                intent.setData(content_url);
                startActivity(intent);
                return true;
            case R.id.action_front_view:
                createTextSizeDialog().show();
                return true;
            case R.id.action_module_view:
                createModeDialog().show();
                return true;
            case R.id.action_store:
                if (isFav) {//cancel store
                    boolean deleteSuc = App.getDbHelper().deleteArticle(ArticleCollects.ArticleEntry.TABLE_NAME, String.valueOf(contendid));
                    menFav.setTitle(deleteSuc ? getString(R.string.cancel_store) : getString(R.string.store_it));
                    isFav = !deleteSuc;
                    if (deleteSuc) {
                        Snack(getString(R.string.cancel_success));
                    } else {
                        Snack(getString(R.string.cancel_fal));
                    }
                } else {//store it
                    article.setIsfav(Config.STORE);//set not fav
                    article.setSavedate(String.valueOf(System.currentTimeMillis()));//set save date
                    boolean insertSuc = dbHelper.insertArticle(article, TAB_NAME, article.getChannelId());
                    menFav.setTitle(insertSuc ? getString(R.string.store_it) : getString(R.string.cancel_store));
                    isFav = insertSuc;
                    if (isFav) {
                        Snack(getString(R.string.store_success));
                    } else {
                        Snack(getString(R.string.store_fal));
                    }
                }
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
        HtmlBody = "";
        HtmlBodyClone = "";//maybe reset by getting data again
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
                            user = articleBody.getData().getFullArticle().getUser().getUsername();
                            dealBody(HtmlBody);
                            addHead();
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
        head.append("<head>");
        head.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">");

        head.append("<style type=\"text/css\">\n");
        head.append("* {\n" +
                "\t\t\tpadding: 0;\n" +
                "\t\t\tmargin: 0;\n" +
                "\t\t}\n" +
                "\t\thtml,\n" +
                "\t\tbody { height: 100%;}\n" +
                "\t\thtml {\n" +
                "\t\t\tfont-size: 100%;\n" +
                "\t\t\tfont-size: 1rem;\n" +
                "\t\t}\n" +
                "\t\tbody {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t\tfont-size: 12px;\n" +
                "\t\t\tfont-family: SimHei, '黑体', '宋体';\n" +
                "\t\t\tcolor: #949393;\n" +
                "\t\t}\n" +
                "\t\t.block-title {\n" +
                "\t\t  margin: 0 8px 8px 0;\n" +
                "\t\t  padding: 8px 0;\n" +
                "\t\t  border-bottom: 1px dashed #eee;\n" +
                "\t\t}\n" +
                "\t\t.block-title .title {\n" +
                "\t\t  font-size: 1.125rem;\n" +
                "\t\t  font-weight: normal;\n" +
                "\t\t  border-left: 6px solid #ff851b;\n" +
                "\t\t  padding: 2px 0 4px 8px;\n" +
                "\t\t  text-shadow: 0 1px 4px rgba(0,0,0,0.1);\n" +
                "\t\t  color: #333;\n" +
                "\t\t}\n" +
                "\t\t.block-title .name {\n" +
                "\t\t\tfont-size: 14px;\n" +
                "\t\t  border-left: 6px solid #ff851b;\n" +
                "\t\t  text-align: right;\n" +
                "\t\t  padding-right: 8px;\n" +
                "\t\t  padding-top: 4px;\n" +
                "\t\t}\n" +
                "\t\t.block-title .name span {\n" +
                "\t\t  color: #ff851b;\n" +
                "\t\t}\n" +
                "\t\t.icon {\n" +
                "\t\t\tmargin-right: 5px;\n" +
                "\t\t\tfont-size: .875rem;\n" +
                "  \t\tcolor: #666;\n" +
                "\t\t}");
        head.append("</style>\n");

        head.append("</head>");
        head.append("<body>");

        head.append("<div class=\"block-title\">\n");
        head.append("<h2 class=\"title\">");
        head.append(title);
        head.append("</h2>\n");
        head.append("<p class=\"name\"><span>");
        head.append(getString(R.string.upuser) + user);
        head.append("</span></p>\n");
        head.append("</div>\n");

        head.append("<div>");

        StringBuffer body = new StringBuffer();
        body.append("</div>");
        body.append("</body>");
        body.append("</html>");
        String index = HtmlBody;
        HtmlBody = head.toString() + index + body.toString();
        HtmlBodyClone = head.toString() + index + body.toString();
    }

    /**
     * <li>deal image <img src=\"http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg\" /></li>
     * into <li><img src="http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg" /></li>
     * <li>\n</li>
     * <li>\r</li>
     * <li>other </li>
     */
    private void dealBody(String html) {
        html.replace("\\n", "").replace("\\r", "").replace("\\", "").replace(title, "");
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
        Snackbar.make(mWeb, msg, Snackbar.LENGTH_SHORT).show();
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
                    if (isWebViewLoading)//cancel loading
                        mWeb.stopLoading();
                    if (oldMode != CommonUtil.getMode()) {
                        if (HtmlBody != null && !HtmlBody.equals("")) {
                            filterImg(HtmlBody);//whenever mode what,do this
                            mWeb.stopLoading();//maybe load image then ANR comes
                            mWeb.loadData(CommonUtil.getMode() == 0 ? HtmlBodyClone : HtmlBody, "text/html; charset=UTF-8", null);
                        } else {
                            Snack(getString(R.string.get_data_again));
                            initData();
                        }
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
            isWebViewLoading = true;
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setRefreshing(true);//show progressbar
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isWebViewLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
        }
    }
}
