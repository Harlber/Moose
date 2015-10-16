package moose.com.ac.retrofit.collect;

import java.io.Serializable;

/**
 * Created by dell on 2015/10/16.
 * {
 * "pageNo": 1,
 * "pageSize": 10,
 * "totalCount": 87,
 * "totalPage": 9,
 * "prePage": 1,
 * "nextPage": 2
 * }
 */
public class Page implements Serializable {
    private static final long serialVersionUID = 9217577111022419111L;
    private int pageNo;
    private int pageSize;
    private Long totalCount;
    private Long totalPage;
    private Long prePage;
    private Long nextPage;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getPrePage() {
        return prePage;
    }

    public void setPrePage(Long prePage) {
        this.prePage = prePage;
    }

    public Long getNextPage() {
        return nextPage;
    }

    public void setNextPage(Long nextPage) {
        this.nextPage = nextPage;
    }
}
