package moose.com.ac.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import moose.com.ac.App;
import moose.com.ac.R;
import moose.com.ac.common.Config;

/**
 * Created by Farble on 2015/8/15 17.
 * CommonUtil
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";

    private CommonUtil() {

    }

    public static String groupTitle(int title) {
        return Config.AC + title;
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

    public static String getUserName() {
        return PreferenceUtil.getString(Config.USERNAME, App.getmContext().getString(R.string.un_login));
    }

    public static void setUserName(String name) {
        PreferenceUtil.setStringValue(Config.USERNAME, name);
    }

    public static String getUserLogo() {
        return PreferenceUtil.getString(Config.USER_LOG, App.getmContext().getString(R.string.default_user_logo));
    }

    public static void setUserLogo(String str) {
        PreferenceUtil.setStringValue(Config.USER_LOG, str);
    }

    public static String getLoginStatus() {
        return PreferenceUtil.getString(Config.LOGIN_STATUS, Config.LOGIN_OUT);
    }

    public static void setLoginStatus(String str) {
        PreferenceUtil.setStringValue(Config.LOGIN_STATUS, str);
    }

    public static void setRegistDate() {
        PreferenceUtil.setLongValue(Config.LOGIN_REGIST, System.currentTimeMillis());
    }

    public static Long getRegist() {
        return PreferenceUtil.getLong(Config.LOGIN_REGIST, 0l);
    }

    public static void setSignatrue(String sign) {
        PreferenceUtil.setStringValue(Config.USER_SIGNATRUE, sign);
    }

    public static String getSignatrue() {
        return PreferenceUtil.getString(Config.USER_SIGNATRUE, App.getmContext().getString(R.string.un_login));
    }

    public static void setRegDate(Long date) {
        PreferenceUtil.setLongValue(Config.USER_DATE, date);
    }

    public static Long getRegDate() {
        return PreferenceUtil.getLong(Config.USER_DATE, 0l);
    }

    public static void setGender(int gender) {
        PreferenceUtil.setIntValue(Config.USER_GENDER, gender);
    }

    public static int getGender() {
        return PreferenceUtil.getInt(Config.USER_GENDER, -4);
    }

    public static void setLoginEmail(String email) {
        PreferenceUtil.setStringValue(Config.USER_LOGIN_EMAIL, email);
    }

    public static String getLoginEmail() {
        return PreferenceUtil.getString(Config.USER_LOGIN_EMAIL, "");
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

    /*1,  男 0:女 -1 不公开*/
    public static String getGender(Integer gender) {
        switch (gender) {
            case 0:
                return App.getmContext().getString(R.string.fem);
            case 1:
                return App.getmContext().getString(R.string.mal);
            case -1:
                return App.getmContext().getString(R.string.unknown);
            default:
                return App.getmContext().getString(R.string.unknown);
        }
    }

    public static boolean hasRegis() {
        String now = toDate(System.currentTimeMillis());
        String local = String.valueOf(getRegist());
        if (local.length() < 11) {
            return false;
        }
        String nowDate = now.substring(0, 10);
        String localDate = toDate(getRegist());
        String localToDate = localDate.substring(0, 10);
        return nowDate.equals(localToDate);
    }

    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * <p>通过Uri获取android的资源文件</p>
     * <p>类型	   Scheme     示例</p>
     * <p>远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案</p>
     * <p>本地文件	file://	FileInputStream</p>
     * <p>Content provider	content://	ContentResolver</p>
     * <p>asset目录下的资源	asset://	AssetManager</p>
     * <p>res目录下的资源	res://	Resources.openRawResource</p>
     *
     * @return
     */
    public static Uri getUriFromResource(Context context, String scheme, int rid) {
        StringBuffer sb = new StringBuffer(scheme + "://" + context.getPackageName() + "/" + rid);
        return Uri.parse(sb + "");
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return out.toString() == null ? "" : out.toString();
    }
}
