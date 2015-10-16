package moose.com.ac.retrofit.collect;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2015/10/16.
 * "page": {
 * "pageNo": 1,
 * "pageSize": 10,
 * "totalCount": 87,
 * "totalPage": 9,
 * "prePage": 1,
 * "nextPage": 2
 * },
 * "totalpage": 9,
 * "totalcount": 87,
 * "contents": [],
 * "success": true
 * }
 */
public class ArticleCloud implements Serializable {
    private static final long serialVersionUID = -92344597598262985L;
    private Page page;
    private Long totalpage;
    private Long totalcount;
    private List<ArticleContent> contents;
    private boolean success;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Long getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(Long totalpage) {
        this.totalpage = totalpage;
    }

    public Long getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Long totalcount) {
        this.totalcount = totalcount;
    }

    public List<ArticleContent> getContents() {
        return contents;
    }

    public void setContents(List<ArticleContent> contents) {
        this.contents = contents;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
