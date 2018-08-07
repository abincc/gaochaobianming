package com.cnsunrun.jiajiagou.common.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;

public class AddSubView extends LinearLayout implements OnClickListener, TextWatcher, View.OnFocusChangeListener
{

    private Context mContext;
    private EditText et;
    private ImageView add;
    private ImageView sub;
    private int maxNum = 9999;
    private int minNum = 1;

    public AddSubView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // 获取xml配置的maxNum属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AddSubView);
        setMaxNum(a.getInt(R.styleable.AddSubView_AddSubView_maxNum, Integer.MAX_VALUE));
        a.recycle();

        init();
    }

    public AddSubView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AddSubView(Context context)
    {
        this(context, null);
    }

    private void init()
    {
        addView(View.inflate(mContext, R.layout.item_add_sub_et, null));
        add = (ImageView) findViewById(R.id.add);
        sub = (ImageView) findViewById(R.id.sub);
        et = (EditText) findViewById(R.id.add_sub_et);
        et.setOnFocusChangeListener(this);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        et.addTextChangedListener(this);
        // et默认内容
        et.setText("0");
        // 默认最大宽度
//		et.setMaxWidth(UIUtils.px2dip(getContext(), 180));

        minNum = 1;
    }

    public void setMaxNum(int num)
    {
        maxNum = num;
    }

    @Override
    public void onClick(View v)
    {
        String numStr = et.getText().toString().trim();
        if (TextUtils.isEmpty(numStr))
        {
            numStr = String.valueOf(pre_num);
        }

        int num = Integer.valueOf(numStr);
        switch (v.getId())
        {
            case R.id.add:
                if (TextUtils.isEmpty(numStr))
                {
                    // et无内容时 按加按钮 内容变为1
                    et.setText(String.valueOf(1));

                    break;
                }
                if (num < maxNum)
                {

                    if (otcl != null)
                    {
                        otcl.add(num, this);
                    }
//                    et.setText(String.valueOf(Integer.valueOf(num + 1)));
                } else
                {
                    if (otcl != null)
                        otcl.onReachMaxNum();
                }
                break;
            case R.id.sub:
                if (num == minNum)
                {
                    if (otcl != null)
                        otcl.onReachMinNum();
                    break;
                }
                if (otcl != null)
                {
                    otcl.reduce(num, this);
                }
//                et.setText(String.valueOf(Integer.valueOf(et.getText().toString().trim()) - 1));
                break;
        }
    }

    private int pre_num;

    public int getPre_num()
    {
        return pre_num;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        if (otcl != null)
        {
            otcl.beforeTextChanged(s, this);
        }
        if (!s.toString().equals(""))
            pre_num = Integer.valueOf(s.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        // if (s.toString().trim().equals("0")) {
        // et.setText(String.valueOf(1));
        // }
        String numStr = et.getText().toString().trim();
        if (TextUtils.isEmpty(numStr))
        {
            sub.setEnabled(false);
        } else
        {
            et.setSelection(et.getText().length());
            if (otcl != null && Integer.valueOf(numStr) != pre_num)
            {
                LogUtils.d(numStr);
                otcl.afterTextChanged(et, Integer.valueOf(numStr));
            }
        }

    }

    public int getNum()
    {
        String text = et.getText().toString().trim();
        if (text.equals(""))
        {
            et.setText("1");
            return 1;
        }
        return Integer.valueOf(text);
    }

    public void setNum(String num)
    {
        et.setText(num);
        pre_num = Integer.valueOf(num);
    }

    /**
     * 供调用者在文本改变时做特殊处理的接口
     */
    private OnTextChangeListener otcl;

    public void setOnTextChangeListener(OnTextChangeListener otcl)
    {
        this.otcl = otcl;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (!hasFocus)
        {
            if (TextUtils.isEmpty(et.getText().toString().trim()))
            {
                et.setText(String.valueOf(pre_num));
            }
        }
    }

    public interface OnTextChangeListener
    {

        void afterTextChanged(EditText s, int num);

        void beforeTextChanged(CharSequence s, AddSubView v);

        void onReachMaxNum();

        void onReachMinNum();

        void add(int num, AddSubView subView);

        void reduce(int num, AddSubView subView);
    }

}
