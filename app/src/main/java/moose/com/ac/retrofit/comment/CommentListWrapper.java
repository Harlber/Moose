package moose.com.ac.retrofit.comment;

import java.util.List;
import java.util.Map;

/**
 * 新的comment list json包装类
 * Created by xiongxingxing on 16/10/16.
 */

public class CommentListWrapper {

    public boolean success;
    public String msg;
    public int status;
    public CommentListData data;

    public static class CommentListData {
        public String cache;
        public CommentListPage page;
    }

    public static class CommentListPage {
        public double totalCount;
        public double pageSize;
        public double pageNo;
        public Map<String, Comment> map;
        public List<Integer> list;
    }

    public static class Comment {
        public int id;
        public int quoteId;
        public double refCount;
        public String content;
        public long time;
        public double userId;
        public String username;
        public String avatar;
        public double floor;
        public double deep;
        public double isAt;
        public double nameRed;
        public double avatarFrame;
        public boolean isDelete;
        public boolean isUpDelete;

        //不存在的数据
        public boolean isQuoted;
        public int beQuotedPosition;

    }
}
