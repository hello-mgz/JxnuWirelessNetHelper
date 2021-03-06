package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class OtherActivity extends AppCompatActivity
{
    private TextView text_net,src_text;
    private GestureDetector gestureDetector;
    private Toolbar mToolbar;
    private String username;

    static final private String yjusername="2015262020";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.layout.activity_other);
        ActionBar actionBar=getSupportActionBar();

        username=AllMyInfo.getInstance(getApplicationContext()).getUsername();
        text_net=(TextView)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.text_net);
        src_text=(TextView)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.src_text);
        mToolbar=(Toolbar)findViewById(R.id.about_toolbar);
        setSupportActionBar(mToolbar);
        src_text.setMovementMethod(LinkMovementMethod.getInstance());
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final String str=getLastestVersion();
                Log.d("Debug",str);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        text_net.setText(str);
                    }
                });
            }
        }).start();
        gestureDetector=new GestureDetector(this, new GestureDetector.OnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e)
            {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                if(e1.getRawX()-e2.getRawX()>100 && username.contains(yjusername))
                {
                    startSecretTask();
                    return true;
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener((v)->{
            finish();
        });
    }

    private void startSecretTask()
    {
        Intent intent=new Intent(OtherActivity.this,SecretActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }




    private String getLastestVersion()
    {
        try{
            Document doc = Jsoup.connect("http://www.cnblogs.com/mgz-/p/8361083.html").get();
            Element e=doc.getElementById("cnblogs_post_body");
            return e.text();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "最新版本：获取失败";
        }
    }
}
