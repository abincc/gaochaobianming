package com.cnsunrun.jiajiagou.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/26 0026.
 */

public class BaseEmptyView extends FrameLayout
{


    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_tip_big)
    TextView tvTipBig;
    @BindView(R.id.tv_tip_small)
    TextView tvTipSmall;
    @BindView(R.id.btn_action)
    TextView btnAction;
    private Context mContext;
    private int mImgRes;
    private String mBigTipText;
    private String mSmallTipText;
    private String mBtnText;
    private OnClickListener mOnclickListener;


    public BaseEmptyView(Context context)
    {
        this(context, null);
    }

    public BaseEmptyView(Context context, int
            imgRes, String bigTipText, String smallTipText, String btnText,
                         OnClickListener listener)
    {
        this(context);
        mImgRes = imgRes;
        mBigTipText = bigTipText;
        mSmallTipText = smallTipText;
        mBtnText = btnText;
        mOnclickListener = listener;
        if (smallTipText == null)
            tvTipSmall.setVisibility(GONE);
        if (btnText != null)
            btnAction.setVisibility(VISIBLE);
    }

    public BaseEmptyView(Context context, int imgRes, String bigTipText, String smallTipText)
    {
        this(context, imgRes, bigTipText, smallTipText, null, null);
    }

    public BaseEmptyView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BaseEmptyView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mImgRes == 0)
        {
            return;
        }
        ivImg.setImageResource(mImgRes);
        tvTipBig.setText(mBigTipText);
        tvTipSmall.setText(mSmallTipText);
        if (mBtnText != null)
        {
            btnAction.setText(mBtnText);
            btnAction.setOnClickListener(mOnclickListener);
        }
    }

    private void initView()
    {
        View view = View.inflate(mContext, R.layout.empty_view, null);
        addView(view);
        ButterKnife.bind(this, view);
    }

    public void setBigTipText(String bigTipText)
    {
        tvTipBig.setText(bigTipText);
    }

    public void setBtnText(String btnText)
    {
        btnAction.setVisibility(VISIBLE);
        btnAction.setText(btnText);
    }

    public void setImgRes(int res)
    {
        ivImg.setImageResource(res);
    }

    public void setSmallTipText(String smallTipText)
    {
        tvTipSmall.setText(smallTipText);
    }

    public void setBtnClickListener(OnClickListener listener)
    {
        btnAction.setOnClickListener(listener);
    }

}
