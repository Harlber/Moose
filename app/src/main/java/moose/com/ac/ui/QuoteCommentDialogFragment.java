package moose.com.ac.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import me.shaohui.bottomdialog.BaseBottomDialog;
import moose.com.ac.R;
import moose.com.ac.retrofit.comment.CommentListWrapper;
import moose.com.ac.util.TextViewUtils;

/**
 * Created by shaohui on 16/10/12.
 */

public class QuoteCommentDialogFragment extends BaseBottomDialog implements View.OnClickListener {

    private static final String ARG_QUOTE_COMMENT = "quoteComment";
    private EditText mEditText;
    private ImageView mCommentEmoji;
    private View mCloseView;
    private View mQuoteLayout;
    private TextView mQuoteUserNameView;
    private TextView mQuoteCommentContentView;
    private View mCommentLayout;
    private View mSendButton;
    private CommentListWrapper.Comment mQuoteComment;

    public static QuoteCommentDialogFragment newInstance(CommentListWrapper.Comment quoteComent) {

        Bundle args = new Bundle();

        QuoteCommentDialogFragment fragment = new QuoteCommentDialogFragment();
        args.putParcelable(ARG_QUOTE_COMMENT, quoteComent);
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
        mCloseView = v.findViewById(R.id.ic_close);
        mQuoteLayout = v.findViewById(R.id.quote_layout);
        mQuoteUserNameView = (TextView) v.findViewById(R.id.quote_username);
        mQuoteCommentContentView = (TextView) v.findViewById(R.id.quote_comment_content);
        mCommentLayout = v.findViewById(R.id.comment_layout);
        mSendButton = v.findViewById(R.id.comment_send_button);
        mEditText = (EditText) v.findViewById(R.id.comment_edittext);
        mCommentEmoji = (ImageView) v.findViewById(R.id.comment_emoji);
        mEditText.post(() -> {
            InputMethodManager imm =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.showSoftInput(mEditText, 0);
        });

        initQuoteComment();

        mCloseView.setOnClickListener(this);
        mCommentEmoji.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
    }

    private void initQuoteComment() {
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


                break;
        }
    }
}
