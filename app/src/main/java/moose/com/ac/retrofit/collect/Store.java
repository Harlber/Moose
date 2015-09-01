package moose.com.ac.retrofit.collect;

import java.io.Serializable;

/**
 * Created by dell on 2015/9/1.
 * {"result":true,"success":true}
 */
public class Store implements Serializable {
    private boolean result;
    private boolean success;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
