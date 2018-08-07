package com.cnsunrun.jiajiagou.map;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cnsunrun.jiajiagou.home.location.LocationAreaAdapter;
import com.cnsunrun.jiajiagou.home.location.SearchCommunityAdapter;
import com.cnsunrun.jiajiagou.map.bean.CloudResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * POI搜索任务
 * <p>
 * author:yyc
 * date: 2017-09-15 16:45
 */
public class PoiSearchTask implements PoiSearch.OnPoiSearchListener {
    private static PoiSearchTask mInstance;
    private SearchCommunityAdapter mSearchCommunityAdapter;
    private LocationAreaAdapter mAdapter;
    private PoiSearch mSearch;
    private Context mContext;
    private boolean isSearch=false;

    private PoiSearchTask(Context context){
        this.mContext = context;
    }

    public static PoiSearchTask getInstance(Context context){
        if(mInstance == null){
            synchronized (PoiSearchTask.class) {
                if(mInstance == null){
                    mInstance = new PoiSearchTask(context);
                }
            }
        }
        return mInstance;
    }

    public PoiSearchTask setAdapter(LocationAreaAdapter adapter){
        this.mAdapter = adapter;
        return this;
    }
    public PoiSearchTask setAdapter(SearchCommunityAdapter adapter){
        this.mSearchCommunityAdapter = adapter;
        return this;
    }

    public void onSearch(String keyword, String city,double lat,double lng){
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        PoiSearch.Query query = new PoiSearch.Query(keyword, "", city);
        mSearch = new PoiSearch(mContext, query);
        mSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lng), 1000));//设置周边搜索的中心点以及半径
        //设置异步监听
        mSearch.setOnPoiSearchListener(this);
        //查询POI异步接口
        mSearch.searchPOIAsyn();
    }

    public void onSearch(String keyword, String city,boolean isSearch){
        this.isSearch=isSearch;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        PoiSearch.Query query = new PoiSearch.Query(keyword, "", city);
        mSearch = new PoiSearch(mContext, query);
        //设置异步监听
        mSearch.setOnPoiSearchListener(this);
        //查询POI异步接口
        mSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
        if(code == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {
                List<CloudResultBean> datas = new ArrayList<>();
                ArrayList<PoiItem> items = poiResult.getPois();
                for (PoiItem item : items) {
                    //获取经纬度对象
                    LatLonPoint llp = item.getLatLonPoint();
                    double lon = llp.getLongitude();
                    double lat = llp.getLatitude();
                    //获取标题
                    String title = item.getTitle();
                    String poiId = item.getPoiId();
                    //获取内容
                    String text = item.getSnippet();
                    datas.add(new CloudResultBean(poiId, title));
                }
                if (isSearch) {
                    mSearchCommunityAdapter.setNewData(datas);
                }else {
                    mAdapter.setNewData(datas);
                }
//                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
