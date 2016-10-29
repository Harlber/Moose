package moose.com.ac.retrofit.comment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新的comment list json包装类
 * Created by xiongxingxing on 16/10/16.
 */

public class CommentListWrapper implements Parcelable{

    public boolean success;
    public String msg;
    public int status;
    public CommentListData data;

    public static class CommentListData implements Parcelable{
        public String cache;
        public CommentListPage page;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.cache);
            dest.writeParcelable(this.page, flags);
        }

        public CommentListData() {
        }

        protected CommentListData(Parcel in) {
            this.cache = in.readString();
            this.page = in.readParcelable(CommentListPage.class.getClassLoader());
        }

        public static final Creator<CommentListData> CREATOR = new Creator<CommentListData>() {
            @Override
            public CommentListData createFromParcel(Parcel source) {
                return new CommentListData(source);
            }

            @Override
            public CommentListData[] newArray(int size) {
                return new CommentListData[size];
            }
        };
    }

    public static class CommentListPage implements Parcelable {
        public int totalCount;
        public int pageSize;
        public int pageNo;
        public Map<String, Comment> map;
        public List<Integer> list;

        public CommentListPage() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.totalCount);
            dest.writeInt(this.pageSize);
            dest.writeInt(this.pageNo);
            dest.writeInt(this.map.size());
            for (Map.Entry<String, Comment> entry : this.map.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeParcelable(entry.getValue(), flags);
            }
            dest.writeList(this.list);
        }

        protected CommentListPage(Parcel in) {
            this.totalCount = in.readInt();
            this.pageSize = in.readInt();
            this.pageNo = in.readInt();
            int mapSize = in.readInt();
            this.map = new HashMap<>(mapSize);
            for (int i = 0; i < mapSize; i++) {
                String key = in.readString();
                Comment value = in.readParcelable(Comment.class.getClassLoader());
                this.map.put(key, value);
            }
            this.list = new ArrayList<>();
            in.readList(this.list, Integer.class.getClassLoader());
        }

        public static final Creator<CommentListPage> CREATOR = new Creator<CommentListPage>() {
            @Override
            public CommentListPage createFromParcel(Parcel source) {
                return new CommentListPage(source);
            }

            @Override
            public CommentListPage[] newArray(int size) {
                return new CommentListPage[size];
            }
        };
    }

    public static class Comment implements Parcelable{
        public int id;
        public int quoteId;//引用id
        public int refCount;
        public String content;
        public long time;
        public int userId;
        public String username;
        public String avatar;
        public int floor;//楼层数
        public int deep;//深度
        public int isAt;
        public int nameRed;
        public int avatarFrame;
        public boolean isDelete;
        public boolean isUpDelete;

        //不存在的数据
        public boolean isQuoted;
        public int beQuotedPosition;

        public Comment() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.quoteId);
            dest.writeInt(this.refCount);
            dest.writeString(this.content);
            dest.writeLong(this.time);
            dest.writeInt(this.userId);
            dest.writeString(this.username);
            dest.writeString(this.avatar);
            dest.writeInt(this.floor);
            dest.writeInt(this.deep);
            dest.writeInt(this.isAt);
            dest.writeInt(this.nameRed);
            dest.writeInt(this.avatarFrame);
            dest.writeByte(this.isDelete ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isUpDelete ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isQuoted ? (byte) 1 : (byte) 0);
            dest.writeInt(this.beQuotedPosition);
        }

        protected Comment(Parcel in) {
            this.id = in.readInt();
            this.quoteId = in.readInt();
            this.refCount = in.readInt();
            this.content = in.readString();
            this.time = in.readLong();
            this.userId = in.readInt();
            this.username = in.readString();
            this.avatar = in.readString();
            this.floor = in.readInt();
            this.deep = in.readInt();
            this.isAt = in.readInt();
            this.nameRed = in.readInt();
            this.avatarFrame = in.readInt();
            this.isDelete = in.readByte() != 0;
            this.isUpDelete = in.readByte() != 0;
            this.isQuoted = in.readByte() != 0;
            this.beQuotedPosition = in.readInt();
        }

        public static final Creator<Comment> CREATOR = new Creator<Comment>() {
            @Override
            public Comment createFromParcel(Parcel source) {
                return new Comment(source);
            }

            @Override
            public Comment[] newArray(int size) {
                return new Comment[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.msg);
        dest.writeInt(this.status);
        dest.writeParcelable(this.data, flags);
    }

    public CommentListWrapper() {
    }

    protected CommentListWrapper(Parcel in) {
        this.success = in.readByte() != 0;
        this.msg = in.readString();
        this.status = in.readInt();
        this.data = in.readParcelable(CommentListData.class.getClassLoader());
    }

    public static final Creator<CommentListWrapper> CREATOR = new Creator<CommentListWrapper>() {
        @Override
        public CommentListWrapper createFromParcel(Parcel source) {
            return new CommentListWrapper(source);
        }

        @Override
        public CommentListWrapper[] newArray(int size) {
            return new CommentListWrapper[size];
        }
    };
}
