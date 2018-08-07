package com.cnsunrun.jiajiagou.common.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 17:10.
 */

public class LeftEdittext extends EditText implements View.OnFocusChangeListener
{
    public LeftEdittext(Context context)
    {
        super(context);
        initListener();
    }

    private void initListener()
    {
        this.setOnFocusChangeListener(this);
    }

    public LeftEdittext(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initListener();
    }

    public LeftEdittext(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initListener();
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        EditText editText = (EditText) v;
        if (hasFocus){
            editText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        }else{

            editText.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        }

    }
}
