package moose.com.ac.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farble on 2015/8/16 16.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {
    private static final String TAG = "ViewPageAdapter";
    private final List<Fragment> mFragments = new ArrayList<>();

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void clearFragments() {
        mFragments.clear();
        notifyDataSetChanged();
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
