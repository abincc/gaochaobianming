package com.cnsunrun.jiajiagou.map;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.cnsunrun.jiajiagou.home.location.LocationAreaAdapter;
import com.cnsunrun.jiajiagou.home.location.SearchCommunityAdapter;
import com.cnsunrun.jiajiagou.map.bean.CloudResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 云图搜索任务
 * <p>
 * author:yyc
 * date: 2017-09-16 13:46
 */
public class CloudSearchTask implements CloudSearch.OnCloudSearchListener {
    private static CloudSearchTask mInstance;
    private Context mContext;
    private SearchCommunityAdapter mSearchCommunityAdapter;
    private LocationAreaAdapter mAdapter;
    private CloudSearch mCloudSearch;
    private TextView mRecommendTv;
    private String keyword;
    private boolean isSearch=false;
    boolean flag=false;//设置默认小区
    private boolean isAll;
    private List<CloudResultBean> cloudResultList;
    private ArrayList<CloudItem> clouds;


    private CloudSearchTask(Context context){
        this.mContext = context;
    }

    public static CloudSearchTask getInstance(Context context){
        if(mInstance == null){
            synchronized (CloudSearchTask.class) {
                if(mInstance == null){
                    mInstance = new CloudSearchTask(context);
                }
            }
        }
        return mInstance;
    }
    public CloudSearchTask setAdapter(LocationAreaAdapter adapter){
        this.mAdapter = adapter;
        return this;
    }
    public CloudSearchTask setAdapter(SearchCommunityAdapter adapter){
        this.mSearchCommunityAdapter = adapter;
        return this;
    }
    public CloudSearchTask setRecommendCommunity(TextView text){
        this.mRecommendTv=text;
        return this;
    }
    public CloudSearchTask setTextViewFlag(boolean flag){
        this.flag=flag;
        return this;
    }

    public void onSearch(String tableId,String keyword,boolean isSearch,double lat,double lng) {
        if (isSearch) {
            if (TextUtils.isEmpty(keyword)) {
                mSearchCommunityAdapter.getData().clear();
                mSearchCommunityAdapter.notifyDataSetChanged();
                return;
            }
        }
        this.isSearch=isSearch;
        mCloudSearch = new CloudSearch(mContext);
        mCloudSearch.setOnCloudSearchListener(this);
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(
                lat,lng), 10000);
        try {
            CloudSearch.Query query =new CloudSearch.Query(tableId, keyword, bound);
            query.setPageSize(10);
            mCloudSearch.searchCloudAsyn(query);// 异步搜索
        } catch (AMapException e) {
            e.printStackTrace();
        }


    }
    public void onSearchCity(String tableId,boolean isAll,String keyword,String city,int page) {
        this.isAll=isAll;
        mCloudSearch = new CloudSearch(mContext);
        mCloudSearch.setOnCloudSearchListener(this);
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(city);
        try {
            CloudSearch.Query query =new CloudSearch.Query(tableId, keyword, bound);
            query.setPageSize(10);
            query.setPageNum(page);
            mCloudSearch.searchCloudAsyn(query);// 异步搜索
        } catch (AMapException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCloudSearched(CloudResult cloudResult, int i) {
        clouds = cloudResult.getClouds();
        cloudResultList = new ArrayList<>();
        if (clouds.size() > 0) {
            for (CloudItem cloud : clouds) {
                String title = cloud.getTitle();
                String id = cloud.getID();
                int distance = cloud.getDistance();
                CloudResultBean bean = new CloudResultBean();
                bean.setId(id);
                bean.setTitle(title);
                 bean.setDistance(distance);
                cloudResultList.add(bean);
            }
            if (isSearch) {
                mSearchCommunityAdapter.setNewData(cloudResultList);
            }else {
//                mSearchCommunityAdapter.setNewData(cloudResultList);
//                mAdapter.setNewData(cloudResultList);
            }
            if (isAll){
                mSearchCommunityAdapter.addData(cloudResultList);
                mAdapter.addData(cloudResultList);
            }
            CloudResultBean cloudResultBean = cloudResultList.get(0);
            if (isAll&&mRecommendTv!=null&& cloudResultList.size()>0&&cloudResultBean.getDistance()>0) {
                mRecommendTv.setText(cloudResultBean.getTitle());
            }
        }
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {

    }
}
