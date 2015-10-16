package moose.com.ac.ui;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.collect.ArticleContent;
import moose.com.ac.util.RxUtils;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dell on 2015/10/16.
 * CloudCollectFragment
 */
public class CloudCollectFragment extends Fragment {
    private static final String TAG = "CloudCollectFragment";

    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createCookieApi(Api.class, Config.BASE_URL);
    private List<ArticleContent> list = new ArrayList<>();

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
}
