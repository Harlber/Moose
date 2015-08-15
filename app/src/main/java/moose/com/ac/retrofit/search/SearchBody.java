package moose.com.ac.retrofit.search;

import java.io.Serializable;

/**
 * Created by Farble on 2015/8/15 11.
 */
public class SearchBody implements Serializable {
    private Integer status;
    private String msg;
    private boolean success;
    private SearchData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SearchData getData() {
        return data;
    }

    public void setData(SearchData data) {
        this.data = data;
    }

    public static class SearchData{
        private SearchPage page;

        public SearchPage getPage() {
            return page;
        }

        public void setPage(SearchPage page) {
            this.page = page;
        }
    }
}
