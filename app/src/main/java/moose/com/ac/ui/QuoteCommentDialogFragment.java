package moose.com.ac.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.event.CommentEvent;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.comment.CommentListWrapper;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.RxBus;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.TextViewUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shaohui on 16/10/12.
 */

public class QuoteCommentDialogFragment extends BottomDialogFragment implements View.OnClickListener {

    private static final String ARG_QUOTE_COMMENT = "quoteComment";
    private static final String ARG_CONTENT_ID = "contentId";
    private EditText mEditText;
    private ImageView mCommentEmojiView;
    private View mCloseView;
    private View mQuoteLayout;
    private TextView mQuoteUserNameView;
    private TextView mQuoteCommentContentView;
    private View mCommentLayout;
    private View mSendButton;
    private CommentListWrapper.Comment mQuoteComment;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createApi(Api.class, Config.COMMENT_URL);
    private int mContentId;

    public static QuoteCommentDialogFragment newInstance(CommentListWrapper.Comment quoteComent, int contentId) {
        Bundle args = new Bundle();
        QuoteCommentDialogFragment fragment = new QuoteCommentDialogFragment();
        args.putParcelable(ARG_QUOTE_COMMENT, quoteComent);
        args.putInt(ARG_CONTENT_ID, contentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_quote_comment;
    }

    @Override
    public void bindView(View v) {
        mQuoteComment = getArguments().getParcelable(ARG_QUOTE_COMMENT);
        mContentId = getArguments().getInt(ARG_CONTENT_ID, 0);
        mCloseView = v.findViewById(R.id.ic_close);
        mQuoteLayout = v.findViewById(R.id.quote_layout);
        mQuoteUserNameView = (TextView) v.findViewById(R.id.quote_username);
        mQuoteCommentContentView = (TextView) v.findViewById(R.id.quote_comment_content);
        mCommentLayout = v.findViewById(R.id.comment_layout);
        mSendButton = v.findViewById(R.id.comment_send_button);
        mEditText = (EditText) v.findViewById(R.id.comment_edittext);
        mCommentEmojiView = (ImageView) v.findViewById(R.id.comment_emoji);
        mEditText.post(() -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditText, 0);
        });
        initQuoteComment();
        mCloseView.setOnClickListener(this);
        mCommentEmojiView.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
    }

    private void initQuoteComment() {
        if (mQuoteComment == null) {
            mQuoteLayout.setVisibility(View.GONE);
            return;
        }
        mQuoteUserNameView.setText(String.format(Locale.getDefault(), "#%d %s", mQuoteComment.floor, mQuoteComment.username));
        if (!TextUtils.isEmpty(mQuoteComment.content) ) {
            String tempContent = TextViewUtils.replace(mQuoteComment.content);
            tempContent = tempContent.length() > 50 ? tempContent.substring(0, 50) + "..." : tempContent;
            TextViewUtils.setCommentContent(mQuoteCommentContentView, tempContent);
        }
    }

    @Override
    public float getDimAmount() {
        return 0.4f;
    }

    @Override
    public String getFragmentTag() {
        return "QuoteCommentDialogFragment";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_close:
                dismiss();
                break;
            case R.id.comment_emoji:

                break;
            case R.id.comment_send_button:
                if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                    sendComment(mEditText.getText().toString());
                }
                break;
        }
    }

    public Map<String, Object> generateCommentBody(String commentText) {
        Map<String, Object> map = new HashMap<>();
        map.put("text", commentText);
        map.put("source", "mobile");
        map.put("quoteId", mQuoteComment == null ? 0 : mQuoteComment.id);
        map.put("contentId", mContentId);
        map.put("userId", CommonUtil.getUseruid());
        map.put("access_token", CommonUtil.getToken());
        return map;
    }

    private void sendComment(String text) {
        subscription.add(api.sendComment(generateCommentBody(text))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(commentSend -> {
                    RxBus.get().post(new CommentEvent(CommentEvent.TYPE_REFRESH_COMMENT));
                    dismiss();
                }, throwable -> {
                    dismiss();
                }));
    }

}
