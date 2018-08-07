package com.cnsunrun.jiajiagou;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.home.CallResp;
import com.cnsunrun.jiajiagou.home.homepage.HomeFragment;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.cnsunrun.jiajiagou.personal.PersonalFragment;
import com.cnsunrun.jiajiagou.shopcart.ShopCartFragment;
import com.cnsunrun.jiajiagou.shopclassify.ShopClassifyFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener
{

    @BindView(R.id.rg_main_nav)
    RadioGroup mRgMainNav;
    @BindView(R.id.iv_hotline)
    ImageView mIvHotLine;
    private ArrayList<BaseFragment> mFragments;
    private BaseFragment mCurrentFragment;
    private int mPreNavIndex;
    private long exitTime;

    boolean hasNotChange;
    private String mInfo;

    @Override
    protected void init()
    {
        initFragments();
        mRgMainNav.setOnCheckedChangeListener(this);
        ((RadioButton) mRgMainNav.getChildAt(0)).setChecked(true);
    }

    private void initFragments()
    {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new ShopClassifyFragment());
        mFragments.add(new ShopCartFragment());
        mFragments.add(new PersonalFragment());
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_main;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
    {
        if (hasNotChange)
            return;
        int index = checkedId == R.id.rb_home ? 0 : checkedId == R.id.rb_classify ? 1 : checkedId == R.id.rb_shopcart
                ? 2 :
                checkedId == R.id.rb_personal ? 3 : 0;

        mIvHotLine.setVisibility(index == 0 ? View.VISIBLE : View.GONE);

        if (checkedId == R.id.rb_personal || checkedId == R.id.rb_shopcart)
        {
            //需要登录的页面
            if (!JjgConstant.isLogin(MainActivity.this))
            {
                startActivity(new Intent(mContext, LoginActivity.class));
                hasNotChange = true;
                ((RadioButton) mRgMainNav.getChildAt(mPreNavIndex)).setChecked(true);
                hasNotChange = false;
                return;
            }
        }

        changeFragment(index);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        int check_index = intent.getIntExtra("check_index", -1);
        if (check_index != -1)
        {
            ((RadioButton) mRgMainNav.getChildAt(check_index)).setChecked(true);
        }
    }

    private void changeFragment(int index)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BaseFragment baseFragment = mFragments.get(index);
        if (baseFragment.isAdded())
        {
            fragmentTransaction.show(baseFragment);
        } else
        {
            fragmentTransaction.add(R.id.fr_container, baseFragment);
        }
        if (mCurrentFragment != null)
        {
            fragmentTransaction.hide(mCurrentFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        mCurrentFragment = baseFragment;
        mPreNavIndex = index;
    }

    @Override
    public void onBackPressedSupport()
    {

        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else
        {
            finish();
        }
    }

    public void call(String phoneNum)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @OnClick(R.id.iv_hotline)
    public void onViewClicked()
    {
        if (!TextUtils.isEmpty(mInfo))
        {
            call(mInfo);
        }
        {
            HttpUtils.get(NetConstant.GETPHONE, null, new BaseCallBack(mContext)
            {
                @Override
                public void onResponse(String response, int id)
                {
                    CallResp baseResp = new Gson().fromJson(response, CallResp.class);
                    if (handleResponseCode(baseResp))
                    {
                        mInfo = baseResp.info;
                        if (!TextUtils.isEmpty(mInfo))
                        {
                            call(mInfo);
                        }
                    }
                }
            });
        }
    }
}
