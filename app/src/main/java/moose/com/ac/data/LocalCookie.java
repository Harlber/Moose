package moose.com.ac.data;

import java.io.Serializable;

/**
 * Created by dell on 2015/9/2.
 */
public class LocalCookie implements Serializable {
    private int id;
    private String cookie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
