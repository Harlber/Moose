package moose.com.ac.event;

/**
 * Created by xiongxingxing on 16/10/31.
 */

public class CommentEvent {
    public static final int TYPE_REFRESH_COMMENT = 0;
    public int type;

    public CommentEvent(int type) {
        this.type = type;
    }
}
