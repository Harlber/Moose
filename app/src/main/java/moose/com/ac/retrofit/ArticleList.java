package moose.com.ac.retrofit;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Farble on 2015/8/14 22.
 */
public class ArticleList implements Serializable {
    private boolean success;
    private String msg;
    private Integer status;
    private Data data;

    public static class Page{
        private Integer pageNo;
        private Integer pageSize;
        private Integer totalCount;
        private Integer orderBy;
        private List<Article>list;
    }
    public static class Data{
        private Page page;

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
