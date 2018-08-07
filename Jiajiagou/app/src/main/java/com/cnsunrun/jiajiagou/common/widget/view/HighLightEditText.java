package com.cnsunrun.jiajiagou.common.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.widget.TextWatcherImpl;

import static com.cnsunrun.jiajiagou.R.styleable.highLightEditText;

/**
 * Description:
 * Data：2017/8/18 0018-下午 5:16
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class HighLightEditText extends AppCompatEditText
{

    private Drawable mNormalDrawable;
    private Drawable mHighLightDrawable;
    private Drawable mRightDrawable;
    private OnRightDrawableClickListener mOnRightDrawableClickListener;
    private boolean mHightLight;

    public HighLightEditText(Context context)
    {
        super(context);
    }

    public HighLightEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public HighLightEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs, highLightEditText);
        int normalResId = ta.getResourceId(R.styleable.highLightEditText_normalDrawable, -1);
        int highLightResId = ta.getResourceId(R.styleable.highLightEditText_highLightDrawable, -1);
        int rightDrawableId = ta.getResourceId(R.styleable.highLightEditText_rightDrawable, -1);
        ta.recycle();
        if (normalResId != -1 && highLightResId != -1)
        {
            mNormalDrawable = ContextCompat.getDrawable(context, normalResId);
            mHighLightDrawable = ContextCompat.getDrawable(context, highLightResId);
            setupDrawables(false);
            setListeners();
        }

        if (rightDrawableId != -1)
        {
            mRightDrawable = ContextCompat.getDrawable(context, rightDrawableId);
            setupDrawables(false);
        }

    }

    private void setListeners()
    {
        this.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                setupDrawables(hasFocus);
            }
        });
        this.addTextChangedListener(new TextWatcherImpl()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                setupDrawables(!TextUtils.isEmpty(s));
            }
        });
    }

    public void setupDrawables(boolean highLight)
    {
        HighLightEditText.this.setCompoundDrawablesWithIntrinsicBounds((
                highLight ? mHighLightDrawable
                        : mNormalDrawable
        ), null, mRightDrawable, null);
        mHightLight = highLight;
    }

    public boolean isHightLight()
    {
        return mHightLight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
                boolean isClick = (event.getX() > (getWidth() - getTotalPaddingRight())) &&
                        (event.getX() < (getWidth() - getPaddingRight()));

                if (isClick && mOnRightDrawableClickListener != null)
                    mOnRightDrawableClickListener.onRightDrawableClick(HighLightEditText.this);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setRightDrawable(Drawable drawable)
    {
        mRightDrawable = drawable;
        setupDrawables(mHightLight);
    }

    public Drawable getRightDrawable()
    {
        return mRightDrawable;
    }

    public void setOnRightDrawableClickListener(OnRightDrawableClickListener onRightDrawableClickListener)
    {
        mOnRightDrawableClickListener = onRightDrawableClickListener;
    }

    public interface OnRightDrawableClickListener
    {
        void onRightDrawableClick(HighLightEditText highLightEditText);
    }

    public static class PswRightClickListener implements OnRightDrawableClickListener
    {

        private final Drawable mChangeDrawable;
        private boolean mIsShow = false;
        private Drawable mNormalDrwable;

        public PswRightClickListener(HighLightEditText editText, Drawable changeDrawable)
        {
            mNormalDrwable = editText.getRightDrawable();
            mChangeDrawable = changeDrawable;
        }

        @Override
        public void onRightDrawableClick(HighLightEditText highLightEditText)
        {
            mIsShow = !mIsShow;
            highLightEditText.setRightDrawable(mIsShow ? mChangeDrawable : mNormalDrwable);
            if (mIsShow)
                highLightEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
             else
                highLightEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            highLightEditText.setSelection(highLightEditText.getText().length());
        }
    }

}
