package moose.com.ac.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import moose.com.ac.R;

/**
 *Created by dell on 2015/9/1.
 */
public class EmailEditText extends AppCompatAutoCompleteTextView {

    private static final String TAG = "EmailAutoCompleteTextView";

    private String[] emailSufixs = new String[]{ "@163.com","@qq.com","@gmail.com",  "@sina.com","@126.com", "@mac.com","@foxmail.com","@me.com","@icloud.com", "@hotmail.com",
            "@yahoo.cn", "@sohu.com",  "@139.com", "@yeah.net", "@vip.qq.com", "@vip.sina.com","@vip.sina.com"};

    public EmailEditText(Context context) {
        super(context);
        init(context);
    }


    public EmailEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public EmailEditText(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public void setAdapterString(String[] es) {
        if (es != null && es.length > 0)
            this.emailSufixs = es;
    }


    private void init(final Context context) {
        this.setAdapter(new EmailAutoCompleteAdapter(context, R.layout.auto_complete_item, emailSufixs));
        this.setThreshold(1);
        this.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String text = EmailEditText.this.getText().toString();
                if (!"".equals(text))
                    performFiltering(text, 0);
            } else {
                EmailEditText ev = (EmailEditText) v;
                String text = ev.getText().toString();
            }
        });
    }

    @Override
    protected void replaceText(CharSequence text) {
        String t = this.getText().toString();
        int index = t.indexOf("@");
        if (index != -1)
            t = t.substring(0, index);
        super.replaceText(t + text);
    }


    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        String t = text.toString();
        int index = t.indexOf("@");
        if (index == -1) {
            if (t.matches("^[a-zA-Z0-9_]+$")) {
                super.performFiltering("@", keyCode);
            } else
                this.dismissDropDown();//
        } else {
            super.performFiltering(t.substring(index), keyCode);
        }
    }


    private class EmailAutoCompleteAdapter extends ArrayAdapter<String> {
         String[] email;
        public EmailAutoCompleteAdapter(Context context, int textViewResourceId, String[] email_s) {
            super(context, textViewResourceId, email_s);
            email = email_s;
        }

        @Override
        public String getItem(int position) {
            if (position<1) {
                return "";
            }
            return email[position-1];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null)
                v = LayoutInflater.from(getContext()).inflate(
                        R.layout.auto_complete_item, null);
            TextView tv = (TextView) v.findViewById(R.id.auto_textview);
            if (position==0) {
                tv.setTextColor(getResources().getColor(R.color.colorBlack));
            }else{
                tv.setTextColor(getResources().getColor(R.color.colorEmailTextView));
            }
            String t = EmailEditText.this.getText().toString();
            int index = t.indexOf("@");
            if (index != -1)
                t = t.substring(0, index);
            tv.setText(t + getItem(position));
            return v;
        }

    }

}
