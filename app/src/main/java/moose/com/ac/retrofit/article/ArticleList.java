package moose.com.ac.retrofit.article;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Farble on 2015/8/14 22.
 * ArticleList
 */
public class ArticleList implements Serializable {
    private static final long serialVersionUID = 6545745968349508771L;
    private boolean success;
    private String msg;
    private Integer status;
    @SerializedName("data")
    private Page page;

    public static class Page{
        private Integer pageNo;
        private Integer pageSize;
        private Integer totalCount;
        private List<Article>list;

        public Integer getPageNo() {
            return pageNo;
        }

        public void setPageNo(Integer pageNo) {
            this.pageNo = pageNo;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public List<Article> getList() {
            return list;
        }

        public void setList(List<Article> list) {
            this.list = list;
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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

}
