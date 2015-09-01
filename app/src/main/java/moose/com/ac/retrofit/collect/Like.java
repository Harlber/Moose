package moose.com.ac.retrofit.collect;

import java.io.Serializable;

/**
 * Created by dell on 2015/9/1.
 * 点赞
 */
public class Like implements Serializable{
    private boolean success;
    private int upds;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getUpds() {
        return upds;
    }

    public void setUpds(int upds) {
        this.upds = upds;
    }
}
