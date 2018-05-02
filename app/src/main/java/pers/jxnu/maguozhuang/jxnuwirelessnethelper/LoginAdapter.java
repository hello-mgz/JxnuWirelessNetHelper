package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ma on 2018/4/18.
 * 用来管理登录方式、登录状态等
 */

public class LoginAdapter
{

    public static final String LoginSuccessStr="login_ok";

    public static void executeLoginIn(AllMyInfo allMyInfo,Callback callback)
    {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        RequestBody loginform=new FormBody.Builder()
                .add("action", "login")
                .add("username", allMyInfo.getUsername())
                .add("domain",allMyInfo.getDomain())
                .add("password", allMyInfo.getPassword())
                .add("ac_id", "1")
                .add("user_ip", "")
                .add("nas_ip", "")
                .add("user_mac", "")
                .add("save_me", "1")
                .add("ajax", "1")
                .build();

        Request loginRequest = new Request.Builder()
                .url("http://219.229.251.2/include/auth_action.php")
                .post(loginform)
                .build();
        client.newCall(loginRequest).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                callback.onResponse(call,response);
            }
        });
    }

    public static void executeLoginOut(AllMyInfo allMyInfo,Callback callback)
    {

        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        RequestBody loginform=new FormBody.Builder()
                .add("action", "logout")
                .add("username", allMyInfo.getUsername())
                .add("ajax", "1")
                .build();
        Request loginRequest = new Request.Builder()
                .url("http://219.229.251.2/include/auth_action.php")
                .post(loginform)
                .build();
        client.newCall(loginRequest).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                callback.onResponse(call,response);
            }
        });
    }
}
