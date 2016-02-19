package moose.com.ac.retrofit.login;

import java.io.Serializable;

/**
 * Created by dell on 2015/8/31.
 * {"result":"请先登录","success":false,"status":401,"info":"请先登录"}
 * {"result":"您今天已签到过","success":false}
 */
@Deprecated
public class CheckIn implements Serializable {
    private static final long serialVersionUID = -2346184601048001301L;
    private String result;
    private boolean success;
    private int status;
    private String info;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
