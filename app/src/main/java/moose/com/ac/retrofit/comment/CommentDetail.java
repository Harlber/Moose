package moose.com.ac.retrofit.comment;

import java.io.Serializable;

/**
 * Created by Farble on 2015/8/19 23.
 * "c40969778": {
 * "cid": 40969778,
 * "quoteId": 0,
 * "content": "有次去医院看病。。有医生带小孩来玩。。围在一起讨论德国奶粉，贵归贵  放心",
 * "postDate": "2015-08-19 22:19:13",
 * "userID": 728764,
 * "userName": "分度偏偏又无比俊朗",
 * "userImg": "http://cdn.aixifan.com/dotnet/20120923/style/image/avatar.jpg",
 * "count": 5,
 * "deep": 0,
 * "refCount": 2,
 * "ups": 0,
 * "downs": 0,
 * "nameRed": 1,
 * "avatarFrame": 1
 * },
 *
 *  "commentList": [
 40972045, //最后一楼
 40970753,
 40969589 //第一楼
 ],
 */
public class CommentDetail implements Serializable {
    private static final String TAG = "CommentDetail";
    private static final long serialVersionUID = -2941613424575567707L;
    private Long cid;//评论id
    private Long quoteId;//引用的评论id  -1：用户不存在或已删除
    private String content;//评论内容  [emot\u003dac,17/]  [at]领袖斯大林[/at]  [size\u003d48px][color\u003d#ff0000]接龙大成功！！！！！[size\u003d18px][color\u003d#ff0000]看到这么多人我就安心了。
    private String postDate;//评论日期 2015-08-11 21:05:25

    private Long userID;//user id
    private String userName;//用户名
    private String userImg;//用户头像
    private Long count;//楼层 count=15  15楼
    private Long deep;//在一个楼层中属于第几层 从0开始
    private Long refCount;//被人引用次数
    private Long ups;
    private Long downs;
    private Long nameRed;//0: 没红名 1: 有红名
    private Long avatarFrame;//0: 还有楼下跟帖 1：单独的帖子/该楼层帖子终结

    public boolean isQuoted;
    public int beQuotedPosition;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Long getCount() {
        return null==count?0L:count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getDeep() {
        return deep;
    }

    public void setDeep(Long deep) {
        this.deep = deep;
    }

    public Long getRefCount() {
        return refCount;
    }

    public void setRefCount(Long refCount) {
        this.refCount = refCount;
    }

    public Long getUps() {
        return ups;
    }

    public void setUps(Long ups) {
        this.ups = ups;
    }

    public Long getDowns() {
        return downs;
    }

    public void setDowns(Long downs) {
        this.downs = downs;
    }

    public Long getNameRed() {
        return nameRed;
    }

    public void setNameRed(Long nameRed) {
        this.nameRed = nameRed;
    }

    public Long getAvatarFrame() {
        return avatarFrame;
    }

    public void setAvatarFrame(Long avatarFrame) {
        this.avatarFrame = avatarFrame;
    }
}
