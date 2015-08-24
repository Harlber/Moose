package moose.com.ac.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import moose.com.ac.common.Config;

/**
 * Created by Farble on 2015/8/15 17.
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";

    private CommonUtil() {

    }

    public static StringBuffer getTags(List<String> list) {
        StringBuffer buffer = new StringBuffer();
        if (list.size() == 0) {
            return buffer;
        }
        for (int i = 0; i < list.size(); i++)
            buffer.append(list.get(i));
        return buffer;
    }

    public static String toDate(Long aLong) {
        String beginDate = String.valueOf(aLong);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.parseLong(beginDate)));
    }

    public static int getTextSize() {
        return PreferenceUtil.getInt(Config.TEXTSIZE, 1);//default 1
    }

    public static void setTextSize(int size) {
        PreferenceUtil.setIntValue(Config.TEXTSIZE, size);
    }

    public static int getMode() {
        int mode = PreferenceUtil.getInt(Config.MODE, Config.MODE_IMAGE);//default
        return mode == Config.MODE_TEXT_ONLY ? 1 : 0;
    }

    public static void setMode(int mode) {
        PreferenceUtil.setIntValue(Config.MODE, mode == 0 ? Config.MODE_IMAGE : Config.MODE_TEXT_ONLY);
    }

    public static int getMaxLine(){
        return PreferenceUtil.getInt(Config.MAX_LINE,6);
    }
}
