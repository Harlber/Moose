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
import android.support.annotation.IntDef;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.trello.rxlifecycle.ActivityEvent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

import moose.com.ac.common.Config;
import moose.com.ac.data.ArticleCollects;
import moose.com.ac.data.DbHelper;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleBodyWrapper;
import moose.com.ac.ui.BaseActivity;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.ui.widget.ObservableWebView;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.DisplayUtil;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.ScrollFABBehavior;
import moose.com.ac.util.SettingPreferences;
import moose.com.ac.util.chrome.CustomTabActivityHelper;
import moose.com.ac.util.chrome.WebviewFallback;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/*
 * Copyright 2015,2016 Farble Dast
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by Farble on 2015/8/16 20.
 * View article body
 */
public class ArticleViewActivity extends BaseActivity
        implements ObservableWebView.OnScrollChangedCallback {
    private static final String TAG = "ArticleViewActivity";
    private static final int FAB_SHOW = 0x0000aa;
    private static final int FAB_HIDE = 0x0000bb;
    private final String TAB_NAME = ArticleCollects.ArticleEntry.TABLE_NAME;
    private ObservableWebView mWeb;
    private FloatingActionButton fab;
    private MenuItem menFav;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);
    private DbHelper dbHelper = AppApplication.getDbHelper();

    private String HtmlBody;//get body from network
    private String HtmlBodyClone;//get body from network
    private WebSettings settings;
    private int articleId;//article id
    private int fabStatus = FAB_SHOW;

    private String title = "";//default
    private String user = "";//default
    private String contend;
    private int toolbarHeight;
    private int level = 1;
    private boolean isFav = false;
    private boolean isWebViewLoading = false;
    private Article article;
    private boolean isRequest = false;
    private HashMap<String,String> htmlImages = new HashMap<>();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            SMALLEST,
            SMALLER,
            NORMAL,
            LARGER,
            LARGEST
    })
    @interface TextSize {
    }

    private static final int SMALLEST = 0;
    private static final int SMALLER = 1;
    private static final int NORMAL = 2;
    private static final int LARGER = 3;
    private static final int LARGEST = 4;

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_article_view);
        article = (Article) getIntent().getSerializableExtra(Config.ARTICLE);
        /*
        * Uri data = getIntent().getData();
        if(Intent.ACTION_VIEW.equalsIgnoreCase(getIntent().getAction()) && data!=null){
            String scheme = data.getScheme();
            if(scheme.equals("ac")){
                // ac://ac000000
                aid = Integer.parseInt(getIntent().getDataString().substring(7));
            }else if(scheme.equals("http")){
                // http://www.acfun.tv/v/ac123456
                Matcher matcher;
                String path = data.getPath();
                if(path==null){
                    finish();
                    return;
                }
                if((matcher = sVreg.matcher(path)).find()
                        || (matcher = sAreg.matcher(path)).find()){
                    aid = Integer.parseInt(matcher.group(1));
                }
            }
            if(aid != 0) title = "ac"+aid;
            isWebMode = getIntent().getBooleanExtra("webmode", false) && aid == 0;
        }else{
            aid = getIntent().getIntExtra("aid", 0);
            title = getIntent().getStringExtra("title");
        }*/
        isFav = dbHelper.isExits(TAB_NAME, article.isfav);
        articleId = Integer.valueOf(article.contentId);
        toolbarHeight = DisplayUtil.dip2px(this, 56f);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        contend = "ac" + articleId;
        getSupportActionBar().setTitle(contend);

        fab = (FloatingActionButton) findViewById(R.id.view_fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, BigNewsActivity.class);
            intent.putExtra(Config.CONTENTID, articleId);
            intent.putExtra(Config.TITLE, title);
            startActivity(intent);
        });

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.web_swipe);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.view_fab);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);

        mWeb = (ObservableWebView) findViewById(R.id.view_webview);
        settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUserAgentString(RxUtils.UA);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDefaultTextEncodingName(Config.TEXT_ENCODING);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWeb.getSettings().setDisplayZoomControls(false);
        }
        mWeb.setWebViewClient(new Client());
        mWeb.addJavascriptInterface(new JsBridge(),"JsBridge");
        if (Build.VERSION.SDK_INT >= 11)
            mWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        mWeb.setOnScrollChangedCallback(this);
        level = CommonUtil.getTextSize();
        setText();
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_article, menu);
        menFav = menu.findItem(R.id.action_store);
        menFav.setTitle(isFav ? getString(R.string.store_it) : getString(R.string.cancel_store));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                doExitActivity();
                return true;
            case R.id.action_share:
                String shareUrl = article.title + " " + Config.WEB_URL + articleId + getString(R.string.share_content);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
                return true;
            case R.id.action_module_wap:
                if (!SettingPreferences.externalBrowserEnabled(mContext)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(Config.WEB_URL + articleId + "/");
                    intent.setData(content_url);
                    startActivity(intent);
                } else {
                    String url = Config.WAP_URL + "v#ac=" + articleId + ";type=article";
                    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                    CustomTabActivityHelper.openCustomTab(
                            this, customTabsIntent, Uri.parse(url), new WebviewFallback());
                }
                return true;
            case R.id.action_front_view:
                createTextSizeDialog().show();
                return true;
            case R.id.action_module_view:
                createModeDialog().show();
                return true;
            case R.id.action_store:
                if (isFav) {//cancel store
                    boolean deleteSuc = AppApplication.getDbHelper().deleteArticle(ArticleCollects.ArticleEntry.TABLE_NAME, String.valueOf(articleId));
                    menFav.setTitle(deleteSuc ? getString(R.string.cancel_store) : getString(R.string.store_it));
                    isFav = !deleteSuc;
                    snackStore(deleteSuc ? getString(R.string.cancel_success) : getString(R.string.cancel_fal));
                } else {//store it
                    article.isfav = Config.STORE;//set not fav
                    article.savedate = String.valueOf(System.currentTimeMillis());//set save date
                    boolean insertSuc = dbHelper.insertArticle(article, TAB_NAME, article.channelId);
                    menFav.setTitle(insertSuc ? getString(R.string.store_it) : getString(R.string.cancel_store));
                    isFav = insertSuc;
                    snackStore(isFav ? getString(R.string.store_success) : getString(R.string.store_fal));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        isRequest = true;
        HtmlBody = "";
        HtmlBodyClone = "";
        subscription.add(api.getNewUrlArticleBody(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .retry(1)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<ArticleBodyWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        isRequest = true;
                        e.printStackTrace();
                        mSwipeRefreshLayout.setRefreshing(false);//show progressbar
                        mSwipeRefreshLayout.setEnabled(true);
                        snack(e.getMessage());
                    }

                    @Override
                    public void onNext(ArticleBodyWrapper articleBody) {
                        isRequest = true;
                        if (TextUtils.isEmpty(articleBody.message) || !TextUtils.equals(articleBody.message.toUpperCase(), "OK")) {
                            snack(articleBody.message);
                        } else {
                            HtmlBody = articleBody.data.article.content;
                            title = articleBody.data.title;
                            user = articleBody.data.owner.name;

                            //fix this issues https://github.com/Harlber/Moose/issues/8
                            rx.Observable.create(subscriber -> {
                                dealBody(HtmlBody);
                                addHead();
                                filterImg(HtmlBody);
                                subscriber.onNext(HtmlBody);
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(s -> {
                                        mWeb.loadDataWithBaseURL("", HtmlBody, "text/html", "UTF-8", "");
                                    });
                        }
                    }

                }));
    }

    private void addHead() {
        StringBuilder head = new StringBuilder();
        head.append(getResources().getString(R.string.article_head));
        head.append(title);
        head.append("</h2>\n");
        head.append("<p class=\"name\"><span>");
        //noinspection StringConcatenationInsideStringBufferAppend
        head.append(getString(R.string.up_author) + user);

        head.append("</span></p>\n");
        head.append("</div>\n");
        head.append("<div>");

        StringBuilder body = new StringBuilder();
        body.append("</div>");
        body.append("</body>");
        body.append("</html>");
        String index = HtmlBody;
        HtmlBody = head.toString() + index + body.toString();
        HtmlBodyClone = head.toString() + index + body.toString();
        Log.i(TAG,"html:"+HtmlBodyClone);
    }

    /**
     * <li>deal image <img src=\"http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg\" /></li>
     * into <li><img src="http://n.sinaimg.cn/transform/20150817/seQF-fxfxzzn7510940.jpg" /></li>
     * <li>\n</li>
     * <li>\r</li>
     * <li>other </li>
     */
    private void dealBody(String html) {
        //noinspection ResultOfMethodCallIgnored
        html.replaceAll(Config.IMAGE_REG, "");
    }

    private void filterImg(String str) {
        Document mDocument = Jsoup.parse(str);

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
            Log.i(TAG, "image src:" + src);
            img.attr("org", src);
            StringBuilder builder = new StringBuilder();
            builder.append("<div style='text-align: center;'><br>")
                    .append("<img src='file:///android_asset/loading.gif'")
                    .append("name = '")
                    .append(src)
                    .append("'\n;onclick = window.JsBridge.showImage('")
                    .append(src)
                    .append("')")
                    .append(" alt=' 加载失败'/>\n")
                    .append("</div>");
            img.after(builder.toString());
            Log.i(TAG,"image:table:-"+builder.toString());
            /*
            if (CommonUtil.getMode() == 1 && !CommonUtil.isWifiConnected(mContext)) {
                img.after("<p >[图片]</p>");
            } else if (!src.contains(Config.AC_EMOTION)) {
                StringBuilder builder = new StringBuilder();
                builder.append("<div style=\"width: 100%;text-align: center;\"><br><img src=\"")
                        .append(src)
                        .append("\" width=: 100%;height:auto\"")
                        .append(" alt=\" 加载失败\"/>\n")
                        .append("</div>");
                Log.i(TAG, "index image:" + builder.toString());
                img.after(builder.toString());
            } else {
                img.after("<img src=\"" + src + "\" alt=\" 加载失败\"/>\n");
            }*/

            img.remove();
            //img.removeAttr("style");
            HtmlBody = mDocument.toString();
            Log.i(TAG,"处理后的html:"+HtmlBody);
        }
    }

    private void snack(String msg) {
        Snackbar snackBar = Snackbar.make(mWeb, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.reload_action, v -> {
            snackBar.dismiss();
            initData();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }

    private void snackStore(String msg) {
        Snackbar snackBar = Snackbar.make(mWeb, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }

    @SuppressWarnings("RedundantCast")
    private Dialog createTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleViewActivity.this);
        builder.setTitle(R.string.text_size)
                .setSingleChoiceItems(R.array.font_text_size, CommonUtil.getTextSize(), (dialog, which) -> {
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

    @SuppressWarnings("RedundantCast")
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
                            rx.Observable.create(subscriber -> {
                                filterImg(HtmlBody);//whenever mode what,do this
                                subscriber.onNext(HtmlBody);
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(s -> {
                                        mWeb.stopLoading();//maybe load image then ANR comes
                                        mWeb.loadDataWithBaseURL("", CommonUtil.getMode() == 0 ? HtmlBodyClone : HtmlBody, "text/html", "UTF-8", "");
                                    });
                        } else {
                            if (isRequest) {
                                RxUtils.unsubscribeIfNotNull(subscription);
                                subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
                            }
                            snack(getString(R.string.get_data_again));
                            initData();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) (dialog, id) -> CommonUtil.setMode(oldMode));

        return builder.create();
    }

    private void setZoom(int lev) {
        level = lev;
    }

    @SuppressWarnings("deprecation")
    private void setText() {
        switch (level) {
            case SMALLEST:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
                break;
            case SMALLER:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case NORMAL:
                mWeb.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case LARGER:
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

    @SuppressWarnings("ConstantConditions")
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            doExitActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doExitActivity() {
        Intent intent = new Intent(getString(R.string.store_action));
        intent.putExtra(Config.CONTENTID, articleId);
        intent.putExtra(Config.STORE, isFav);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        finish();
    }

    private class Client extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isWebViewLoading = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isWebViewLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    private class JsBridge{

        public JsBridge() {
        }

        @JavascriptInterface
        public boolean isViewMode(){
            return !(CommonUtil.getMode() == 1 && !CommonUtil.isWifiConnected(mContext));
        }

        /**@param id image id*/
        @JavascriptInterface
        public void showImage(String id){
            snack(id);
        }

        public String getImageUrlById(String id){
            return htmlImages.get(id);
        }
    }
}
