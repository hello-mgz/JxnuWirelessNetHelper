package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.os.AsyncTask;
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
 * Created by ma on 2018/5/3.
 */

public class LoginAsyncTask extends AsyncTask<AllMyInfo,Integer,String>
{
    public interface CallBack
    {
        void execute(String s);
    }

    public static final String LoginMode="MODE.LOGIN";
    public static final String LogOutMode="MODE.LOGIN_OUT";

    private String Mode;
    private CallBack back;
    public LoginAsyncTask(String Mode,CallBack back)
    {
        super();
        this.Mode=Mode;
        this.back=back;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        back.execute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(AllMyInfo... allMyInfos)
    {
        String backInfo;
        if(Mode.equals(LoginMode))
        {
            backInfo=executeLoginIn(allMyInfos[0]);
        }else
        {
            backInfo=executeLoginOut(allMyInfos[0]);
        }
        if(backInfo.contains("login_ok"))
            backInfo="登录成功";
        else if(backInfo.contains("延迟"))
            backInfo="已经连接成功";
        return backInfo;
    }

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
        String backInfo="error";
        try{
            Response response=client.newCall(loginRequest).execute();
            backInfo=response.body().string().toString();
            return backInfo;
        }catch (IOException e){e.printStackTrace();}
        return backInfo;
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
        String backInfo="error";
        try{
            Response response=client.newCall(loginRequest).execute();
            backInfo=response.body().string().toString();
            return backInfo;
        }catch (IOException e){e.printStackTrace();}
        return backInfo;
    }
}
