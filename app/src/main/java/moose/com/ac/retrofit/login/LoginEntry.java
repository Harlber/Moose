package moose.com.ac.retrofit.login;

import java.io.Serializable;

/**
 * Created by dell on 2015/8/31.
 * {"success":false,"result":"您输入的帐号或密码错误"}
 */
public class LoginEntry implements Serializable{
    private boolean success;
    private String result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
