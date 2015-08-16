package moose.com.ac.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moose.com.ac.R;

/**
 * Created by Farble on 2015/8/16 16.
 */
public class SubmitCommentFragment extends Fragment {
    private static final String TAG = "SubmitCommentFragment";

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_submit_comment, container, false);
        Log.e(TAG, "start");
        return rootView;
    }
}
