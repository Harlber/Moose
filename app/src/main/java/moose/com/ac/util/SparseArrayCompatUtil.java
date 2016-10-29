package moose.com.ac.util;

import android.os.Parcel;
import android.support.v4.util.SparseArrayCompat;

import moose.com.ac.retrofit.comment.CommentListWrapper;

/**
 * Created by xiongxingxing on 16/10/29.
 * 用来序列化反序列化SpareArrayCompat的工具类，当前parcel不支持SparseArrayCompat，需自己实现
 */

public class SparseArrayCompatUtil {

    public static SparseArrayCompat<CommentListWrapper.Comment> readSparseArrayCompat(Parcel in){
        SparseArrayCompat<CommentListWrapper.Comment> data = new SparseArrayCompat<>();
        int size = in.readInt();
        for(int i = 0;i<size;++i){
            Integer key = in.readInt();
            CommentListWrapper.Comment value = in.readParcelable(CommentListWrapper.Comment.class.getClassLoader());
            data.put(key,value);
        }
        return data;
    }

    public static void writeSparseArrayCompat(Parcel dest,SparseArrayCompat<CommentListWrapper.Comment> data) {
        if (data == null || data.size() <= 0) return;
        int size = data.size();
        dest.writeInt(size);
        for (int i = 0; i < size; ++i) {
            dest.writeInt(data.keyAt(i));
            dest.writeParcelable(data.get(i), 0);
        }
    }

}
