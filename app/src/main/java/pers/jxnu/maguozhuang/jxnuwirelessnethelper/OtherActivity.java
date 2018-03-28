package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Button but_reback;
    private GestureDetector gestureDetector;
    private String username;
    static final private String yjusername="2015262020";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.layout.activity_other);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        username=pref.getString("username","");
        text_net=(TextView)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.text_net);
        src_text=(TextView)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.src_text);

        src_text.setMovementMethod(LinkMovementMethod.getInstance());
        but_reback=(Button)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.button_reback);
        but_reback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
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
                if(e1.getRawX()-e2.getRawX()>100 && (username.substring(0,12)).equals(yjusername))
                {
                    startSecretTask();
                    return true;
                }
                return false;
            }
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
