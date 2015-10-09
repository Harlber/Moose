package moose.com.ac.retrofit.login;

import java.io.Serializable;

/**
 * Created by dell on 2015/8/31.
 * {"success":false,"result":"您输入的帐号或密码错误"}
 * img: "http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201505/1413200735rj.jpg"
 * success: true
 * username: "*****"
 */
public class LoginEntry implements Serializable{
    private static final long serialVersionUID = 9094698838135949639L;
    private boolean success;
    private String result;
    private String img;
    private String username;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
