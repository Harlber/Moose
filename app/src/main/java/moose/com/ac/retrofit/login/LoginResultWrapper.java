package moose.com.ac.retrofit.login;

/**
 * Created by dell on 2015/8/31.
 * {
 * "data": {
 * "access_token": "***",
 * "userImg": "http://cdn.aixifan.com/dotnet/20120923/style/image/avatar.jpg",
 * "expires": 1478702196000,
 * "userGroupLevel": 1,
 * "mobileCheck": 1,
 * "userId": 6*****5,
 * "username": "x*******x"
 * },
 * "success": true,
 * "status": 200
 * }
 *
 * {
 "result": "密纹都写错？小心召唤出怪物",
 "success": false,
 "status": 403,
 "info": "密纹都写错？小心召唤出怪物"
 }
 */
public class LoginResultWrapper {

    public LoginResult data;
    public boolean success;
    public int status;
    public String result;
    public String info;

    public static class LoginResult {
        public String access_token;
        public String userImg;
        public long expires;
        public int userGroupLevel;
        public int mobileCheck;
        public int userId;
        public String username;
    }
}
