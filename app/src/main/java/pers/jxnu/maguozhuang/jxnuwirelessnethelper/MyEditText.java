package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ma on 2018/4/18.
 */

public class MyEditText extends AppCompatEditText
{
    private Context mContext;
    private Drawable delete_Img;

    public MyEditText(Context context)
    {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext=context;
        init(attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init(attrs);
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd)
    {
        super.onSelectionChanged(selStart, selEnd);
        if(selStart==selEnd)
            setSelection(getText().length());
    }

    private void init(AttributeSet attrs)
    {

        TypedArray typedArray=mContext.obtainStyledAttributes(attrs,R.styleable.MyEditText);
        delete_Img=typedArray.getDrawable(R.styleable.MyEditText_deleteImg);
        typedArray.recycle();
        if(delete_Img!=null)
        {
            addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if(length()<1)
                        setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0],null,null,null);
                    else setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0],null,delete_Img,null);
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(delete_Img!=null&&event.getAction()==MotionEvent.ACTION_UP)
        {
            boolean judge=event.getX()>(getWidth()-getTotalPaddingRight())
                    &&event.getX()<(getWidth()-getPaddingRight());
            if(judge)
            getText().clear();
        }
        return super.onTouchEvent(event);
    }
}
