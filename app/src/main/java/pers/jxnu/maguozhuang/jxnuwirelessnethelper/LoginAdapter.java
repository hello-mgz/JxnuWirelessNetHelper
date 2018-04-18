package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    public static String executeLoginIn(AllMyInfo allMyInfo)
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
        String body="未知信息";
        try {
            Response loginResponse=client.newCall(loginRequest).execute();
            body=loginResponse.body().string().toString();
        }
        catch (IOException e){
            e.printStackTrace();body="登录失败，地址无法访问！";
        }
        catch (Exception e){e.printStackTrace();}
        if(body.contains(LoginSuccessStr))
            return "登录成功！";
        return body;
    }

    public static String executeLoginOut(AllMyInfo allMyInfo)
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
        String backInfo="未知信息";
        try {
            //非异步执行
            Response loginResponse = client.newCall(loginRequest).execute();
            String body=loginResponse.body().string();
            backInfo=body.toString();

        } catch (IOException e) {
            e.printStackTrace();
            backInfo="访问失败";
        }
        catch (Exception e){e.printStackTrace();}
        return backInfo;
    }
}
