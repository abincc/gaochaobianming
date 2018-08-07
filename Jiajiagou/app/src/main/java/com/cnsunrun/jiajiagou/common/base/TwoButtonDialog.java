package com.cnsunrun.jiajiagou.common.base;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;


public class TwoButtonDialog extends DialogFragment
{

    private TextView tvContent;
    private Button btnConfirm;
    private Button btnCancel;
    private CharSequence content;
    private String btnConfirmText;
    private String btnCancelText;
    private View.OnClickListener onBtnConfirmClickListener;
    private View.OnClickListener onBtnCancelClickListener;
    private boolean showCancelBtn = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_two_button, container);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        return view;
    }

    public TwoButtonDialog setContent(CharSequence content)
    {
        this.content = content;
        return this;
    }

    public TwoButtonDialog setBtnConfirmText(String btnConfirmText)
    {
        this.btnConfirmText = btnConfirmText;
        return this;
    }

    public TwoButtonDialog setBtnCancelText(String btnCancelText)
    {
        this.btnCancelText = btnCancelText;
        return this;
    }

    public TwoButtonDialog setOnBtnConfirmClickListener(View.OnClickListener onBtnConfirmClickListener)
    {
        this.onBtnConfirmClickListener = onBtnConfirmClickListener;
        return this;
    }

    public TwoButtonDialog setOnBtnCancelClickListener(View.OnClickListener onBtnCancelClickListener)
    {
        this.onBtnCancelClickListener = onBtnCancelClickListener;
        return this;
    }

    public TwoButtonDialog showCancelBtn(boolean show)
    {
        showCancelBtn = show;
        return this;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        tvContent.setText(tvContent == null ? "确定执行该操作？" : content);
        btnConfirm.setText(btnConfirmText == null ? "确定" :
                btnConfirmText);

        btnConfirm.setOnClickListener(onBtnConfirmClickListener == null ? btnConfirmOnClickListenerDefault :
                onBtnConfirmClickListener);

        btnCancel.setText(btnCancelText == null ? "取消" : btnCancelText);

        btnCancel.setOnClickListener(onBtnCancelClickListener == null ? btnCancelOnClickListenerDefault :
                onBtnCancelClickListener);
        btnCancel.setVisibility(showCancelBtn ? View.VISIBLE : View.GONE);

    }

    private View.OnClickListener btnConfirmOnClickListenerDefault = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            dismiss();
        }
    };

    private View.OnClickListener btnCancelOnClickListenerDefault = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            dismiss();
        }
    };

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }
}
