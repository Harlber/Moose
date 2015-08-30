package moose.com.ac.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        @SuppressLint("SimpleDateFormat")
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

    public static int getMaxLine() {
        return PreferenceUtil.getInt(Config.MAX_LINE, 6);
    }

    public static boolean isVisistor() {
        return PreferenceUtil.getBoolean(Config.VISISTOR_MODE, false);
    }

    public static void setVisistor(boolean bl) {
        PreferenceUtil.setBooleanValue(Config.VISISTOR_MODE, bl);
    }
    public static String getUserName(){
        return PreferenceUtil.getString(Config.USERNAME,"未登陆");
    }
    public static void setUserName(String name){
        PreferenceUtil.setStringValue(Config.USERNAME,name);
    }
    public static String getUserLogo(){
        return PreferenceUtil.getString(Config.USER_LOG,"http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201508/28125157rq4fchv6.jpg");
    }

    public static String[] getDays() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String str = df.format(new Date());
        String[] index = new String[7];
        ArrayList<String> data = new ArrayList<>();
        int MON_TWO;

        String year = str.substring(0, 4);
        String mon = str.substring(5, 7);
        String day = str.substring(8, 10);
        int iyear = Integer.valueOf(year);
        int imon = Integer.valueOf(mon);
        int iday = Integer.valueOf(day);
        MON_TWO = (iyear % 400 == 0 || (iyear % 4 == 0 && iyear % 100 != 0)) ? 29
                : 28;
        System.out.println(iyear + "");//
        System.out.println(imon + "");//
        System.out.println("iday:" + iday + "");//
        if (imon < 10) {
            data.add("0" + imon + "/" + iday);
        } else {
            data.add(imon + "/" + iday);
        }
        if (imon == 1) {
            if (iday > 6) {
                for (int i = 0; i < 6; i++)
                    data.add("0" + imon + "/" + --iday);
            } else {
                for (int i = 1; i < iday; i++)
                    data.add("0" + imon + "/" + (iday - i));
                for (int j = 0; j < 7 - iday; j++)
                    data.add("12/" + (31 - j));
            }
        } else if (imon == 3) {
            if (iday > 6) {
                for (int i = 0; i < 6; i++)
                    data.add("0" + imon + "/" + --iday);
            } else {
                for (int i = 1; i < iday; i++)
                    data.add(" 0" + imon + "/" + (iday - i));
                for (int j = 0; j < 7 - iday; j++)
                    data.add("02/" + (MON_TWO - j));
            }
        } else {

            if (iday > 6) {
                String m = "";
                String n = "";
                if (imon < 10)
                    m = " 0";
                for (int i = 0; i < 6; i++) {
                    n = (iday - 1) < 10 ? " 0" : "";
                    data.add(m + imon + "/" + n + (--iday));
                }
            } else {
                String m = "";
                if (imon < 10)
                    m = " 0";
                for (int i = 1; i < iday; i++)
                    data.add(m + imon + "/" + (iday - i));
                for (int j = 0; j < 7 - iday; j++)
                    data.add(m + (imon - 1) + "/" + (daysInMonth(imon - 1) - j));
            }
        }
        for (int i = 0; i < data.size(); i++) {
            index[i] = data.get(i);
        }
        return index;
    }

    public static int daysInMonth(int mon) {
        switch (mon) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            default:
                return 30;
        }
    }
}
