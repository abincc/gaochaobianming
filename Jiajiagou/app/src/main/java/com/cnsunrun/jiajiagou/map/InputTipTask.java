package com.cnsunrun.jiajiagou.map;

import android.content.Context;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.cnsunrun.jiajiagou.home.location.SearchCommunityAdapter;
import com.cnsunrun.jiajiagou.map.bean.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-15 17:50
 */
public class InputTipTask implements Inputtips.InputtipsListener{
    private static InputTipTask mInstance;
    private Inputtips mInputTips;
    private Context mContext;
    private AutoCompleteTextView et;
    private SearchCommunityAdapter mCommunityAdapter;
    private List<LocationBean> dataLists = new ArrayList<>();

    private InputTipTask(Context context) {
        this.mContext = context;
    }

    public static InputTipTask getInstance(Context context) {
        if (mInstance == null) {
            synchronized (InputTipTask.class) {
                if (mInstance == null) {
                    mInstance = new InputTipTask(context);
                }
            }
        }
        return mInstance;
    }
    public InputTipTask setAdapter(AutoCompleteTextView et) {
        this.et = et;
        return this;
    }

    public InputTipTask setAdapter(SearchCommunityAdapter adapter) {
        this.mCommunityAdapter=adapter;
        return this;
    }
    public List<LocationBean> getData() {
        return dataLists;
    }

    public void searchTips(String keyWord, String city) {
        //第二个参数默认代表全国，也可以为城市区号
        if (!TextUtils.isEmpty(keyWord)) {
            InputtipsQuery inputquery = new InputtipsQuery(keyWord, city);
            inputquery.setCityLimit(true);
            mInputTips = new Inputtips(mContext, inputquery);
            mInputTips.setInputtipsListener(this);
            mInputTips.requestInputtipsAsyn();
        }else {
            mCommunityAdapter.getData().clear();
            mCommunityAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetInputtips(final List<Tip> tipList, int rCode) {
        if (rCode == 1000) {
//            ArrayList<String> datas = new ArrayList<>();
//            if(tipList != null){
//                dataLists.clear();
//                for(Tip tip:tipList){
//                    datas.add(tip.getName());
////                    dataLists.add(new LocationBean(tip.getPoint().getLongitude(),tip.getPoint().getLatitude(),tip.getAddress(),tip.getDistrict()));
//                    if (tip.getPoiID()!=null&&tip.getPoint()!=null) {
//                        dataLists.add(new LocationBean(tip.getPoint().getLongitude(),tip.getPoint().getLatitude(),tip.getName(),tip.getDistrict()));
//                    }
//                }
//            }
//
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1,datas);
////            et.setAdapter(arrayAdapter);
//            mCommunityAdapter.setNewData(dataLists);
//            arrayAdapter.notifyDataSetChanged();
        }
    }
}
