package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecretActivity extends AppCompatActivity
{
    private TextView secret_text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.layout.activity_secret);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
        secret_text=(TextView)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.secret_text);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/myfont.ttf");
        secret_text.setTypeface(typeface);
        secret_text.setText(R.string.secret_str);
    }
}
