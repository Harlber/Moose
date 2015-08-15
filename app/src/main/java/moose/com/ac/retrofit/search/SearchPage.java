package moose.com.ac.retrofit.search;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Farble on 2015/8/15 11.
 */
public class SearchPage implements Serializable {
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalCount;
    private List<SearchList>list;

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

    public List<SearchList> getList() {
        return list;
    }

    public void setList(List<SearchList> list) {
        this.list = list;
    }
}
