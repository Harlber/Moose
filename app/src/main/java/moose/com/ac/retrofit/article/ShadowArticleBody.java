package moose.com.ac.retrofit.article;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Farble on 2016/2/17 21.
 * {
 "code": 200,
 "data": {
 "article": {
 "content": "<p>南都讯 记者吴曦 在中秋前，15岁的顺德容桂实验学校学生林某某在体育课长跑后晕倒，
 几经抢救后仍未能清醒，意外身亡。顺德区教育局官方微博@顺德教育称，具体死亡原因正在调查。</p>\r\n<p>
 　 　9月5日，网上多条信息指容桂实验学校下午发生一起意外。&ldquo;容桂实验学校初三某班周五下午体育课，
 一学生怀疑跑完1500米后晕倒，送医院抢救无效死 亡。&rdquo;而@顺德容桂民生事汇集多条朋友圈和微博信息后
 ，在9月7日发布相关微博，将有关学生疑似猝死的信息集纳并顺德区教育局、公安局的官方微博求证，
 一 时间引起网友关注，其中林某某的多位同学也在微博中发蜡烛悼念。</p>\r\n<p>　　
 当天下午操场在上体育课的有几个班，因此很多人都目击了当时的状况。一 位同学表示，
 当时林某某跑完后直接倒在了地上，晕了过去，大家马上围了上去。&ldquo;脸上没什么颜色，还翻白眼，
 大家叫他一点反应都没有。&rdquo;他表示，几位老师马 上对其进行心外压急救，救护车很快也直接开进操场，
 将林某某送到医院。网友@ C A  ndyFishs称是死者的同班同学，她指林某某是她的好朋友，
 而当天体育课中林某某&ldquo;是跑一个1500米&rdquo;。</p>\r\n<p>　　15岁的林某某，
 不少同学在网上都表示他是个正能量、阳光的男孩。@顺德教育随后在@顺德容桂民生事的微博中进行了事件的回应：
 2014年9月5日下午 16：30左右，容桂实验学校初三级林某某同学(男，15岁)在体育课做跑步运动时，
 突然晕倒在运动场的跑道上。体育老师和同学们发现林某某情况危急，便 迅速拨打120求救。16：40左右，
 救护车赶到现场，医生立即对林同学进行急救，然后送往桂洲医院抢救，但林同学终因抢救无效死亡.
 </p>\r\n<p>&nbsp;</p>"
 },
 "channelId": 110,
 "contentId": 1399954,
 "cover": "http://cdn.aixifan.com/dotnet/20120923/style/image/cover.png",
 "description": "15岁男生跑完1500米晕倒身亡 同学发微博悼念",
 "display": 0,
 "isArticle": 1,
 "isRecommend": 0,
 "owner": {
 "avatar": "http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201404/19220659fesv.jpg",
 "id": 390011,
 "name": "梦斩千钧"
 },
 "releaseDate": 1410216888000,
 "status": 2,
 "tags": [
 "1500米",
 "跑",
 "猝死"
 ],
 "title": "15岁男生跑完1500米晕倒身亡 同学发微博悼念",
 "topLevel": 0,
 "updatedAt": 1455539479000,
 "viewOnly": 0,
 "visit": {
 "comments": 749,
 "danmakuSize": 0,
 "goldBanana": 0,
 "score": 35552,
 "stows": 22,
 "ups": 0,
 "views": 8
 }
 },
 "message": "OK"
 }
 */
public class ShadowArticleBody implements Serializable {
    private static final long serialVersionUID = 4748923917420196273L;
    private Integer code;//200
    private String message;//"OK"
    private ArticleData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArticleData getData() {
        return data;
    }

    public void setData(ArticleData data) {
        this.data = data;
    }

    public static class ArticleData implements Serializable{

        private static final long serialVersionUID = -512247793160411135L;
        private Content article;
        private Integer channelId;//110
        private Long contentId;
        private String cover;
        private String description;
        private Integer display;
        private Integer isArticle;
        private Integer isRecommend;
        private Owner owner;
        private Long releaseDate;
        private Integer status;
        private ArrayList<String> tags;
        private String title;
        private Integer topLevel;
        private Long updatedAt;
        private Long viewOnly;
        private Visit visit;

        public Content getArticle() {
            return article;
        }

        public void setArticle(Content article) {
            this.article = article;
        }

        public Integer getChannelId() {
            return channelId;
        }

        public void setChannelId(Integer channelId) {
            this.channelId = channelId;
        }

        public Long getContentId() {
            return contentId;
        }

        public void setContentId(Long contentId) {
            this.contentId = contentId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getDisplay() {
            return display;
        }

        public void setDisplay(Integer display) {
            this.display = display;
        }

        public Integer getIsArticle() {
            return isArticle;
        }

        public void setIsArticle(Integer isArticle) {
            this.isArticle = isArticle;
        }

        public Integer getIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(Integer isRecommend) {
            this.isRecommend = isRecommend;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public Long getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(Long releaseDate) {
            this.releaseDate = releaseDate;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public ArrayList<String> getTags() {
            return tags;
        }

        public void setTags(ArrayList<String> tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getTopLevel() {
            return topLevel;
        }

        public void setTopLevel(Integer topLevel) {
            this.topLevel = topLevel;
        }

        public Long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Long getViewOnly() {
            return viewOnly;
        }

        public void setViewOnly(Long viewOnly) {
            this.viewOnly = viewOnly;
        }

        public Visit getVisit() {
            return visit;
        }

        public void setVisit(Visit visit) {
            this.visit = visit;
        }
    }

    public static class Visit implements Serializable{

        private static final long serialVersionUID = 8393297980652685421L;
        private Long comments;
        private Long danmakuSize;
        private Long goldBanana;
        private Long score;
        private Long stows;
        private Long ups;
        private Long views;

        public Long getComments() {
            return comments;
        }

        public void setComments(Long comments) {
            this.comments = comments;
        }

        public Long getDanmakuSize() {
            return danmakuSize;
        }

        public void setDanmakuSize(Long danmakuSize) {
            this.danmakuSize = danmakuSize;
        }

        public Long getGoldBanana() {
            return goldBanana;
        }

        public void setGoldBanana(Long goldBanana) {
            this.goldBanana = goldBanana;
        }

        public Long getScore() {
            return score;
        }

        public void setScore(Long score) {
            this.score = score;
        }

        public Long getStows() {
            return stows;
        }

        public void setStows(Long stows) {
            this.stows = stows;
        }

        public Long getUps() {
            return ups;
        }

        public void setUps(Long ups) {
            this.ups = ups;
        }

        public Long getViews() {
            return views;
        }

        public void setViews(Long views) {
            this.views = views;
        }
    }
    public static class Owner implements Serializable{
        private static final long serialVersionUID = 5131586927404644421L;
        private String avatar;
        private Long id;
        private String name;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Content implements Serializable{

        private static final long serialVersionUID = 6538721113949611042L;
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
