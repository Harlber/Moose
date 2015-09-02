package moose.com.ac;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.Profile;
import moose.com.ac.ui.view.MultiSwipeRefreshLayout;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.RxUtils;
import retrofit.RetrofitError;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dell on 2015/9/1.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private Api api;
    private CompositeSubscription subscription = new CompositeSubscription();
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView logo;
    private AppCompatButton registButton;
    private AppCompatButton logoutButton;
    private AppCompatTextView uid;
    private AppCompatTextView signature;
    private AppCompatTextView date;
    private AppCompatTextView gender;
    private View snakView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        toolbar.setTitle(getString(R.string.user_info));
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.profile_swipe);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.profile_toolbar);

        logo = (ImageView)findViewById(R.id.profile_logo);
        registButton = (AppCompatButton)findViewById(R.id.profile_chenkin);
        logoutButton = (AppCompatButton)findViewById(R.id.profile_logout);
        registButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        uid = (AppCompatTextView)findViewById(R.id.profile_uid);
        signature = (AppCompatTextView)findViewById(R.id.profile_qian);
        date = (AppCompatTextView)findViewById(R.id.profile_date);
        gender = (AppCompatTextView)findViewById(R.id.profile_sex);
        snakView = (View)findViewById(R.id.snak_view);

        api = RxUtils.createCookieApi(Api.class, Config.COLLECT_URL);
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(api.getUserProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Profile>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(false);
                        e.printStackTrace();
                        if (e instanceof RetrofitError) {
                            if (((RetrofitError) e).getResponse() != null) {
                                Snack(getString(R.string.net_work) + ((RetrofitError) e).getResponse().getStatus());
                            } else {
                                Snack(getString(R.string.no_network));
                            }

                        }
                    }

                    @Override
                    public void onNext(Profile profile) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(false);
                        if (profile.isSuccess()) {
                            Glide.with(ProfileActivity.this)
                                    .load(profile.getUserImg())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(logo);
                            uid.setText(profile.getUsername());
                            signature.setText(getString(R.string.user_sign) + profile.getSign());
                            date.setText(CommonUtil.toDate(profile.getRegTime()));
                            gender.setText(getString(R.string.gender)+CommonUtil.getGender(profile.getGender()));
                        }else {
                            Snack(profile.getInfo());
                        }
                    }
                }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ProfileActivity.this.finish();
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

    private void Snack(String msg) {
        Snackbar.make(snakView, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }
}
