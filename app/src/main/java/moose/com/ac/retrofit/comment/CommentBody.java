package moose.com.ac.retrofit.comment;

/**
 * Created by xiongxingxing on 16/10/17.
 */

/**
 * 写评论的消息体
 * {captcha
 text	是啊:grin:，赞同:thumbsup:[emot=ac,14/]
 source	mobile
 quoteId	67510240
 //access_token	WkdVk9zcyU6BAfuqIpw4KaCJZMGGwtZb
 contentId	3184134
 userId	6479165}
 */

public class CommentBody {
    public String captcha;
    public String text;
    public String source;
    public int quoteId;
    public int contentId;
    public int userId;
    public String access_token;
}
