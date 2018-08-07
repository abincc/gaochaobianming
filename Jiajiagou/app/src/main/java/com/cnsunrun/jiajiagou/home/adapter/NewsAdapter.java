package com.cnsunrun.jiajiagou.home.adapter;

import android.text.Layout;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.home.bean.NewsListBean;

/**
 * Created by j2yyc on 2018/1/23.
 */

public class NewsAdapter extends CZBaseQucikAdapter<NewsListBean.InfoBean> {
    private RequestManager imageLoader;

    public NewsAdapter(RequestManager imageLoader) {
        super(R.layout.item_news);
        this.imageLoader = imageLoader;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsListBean.InfoBean item) {
        imageLoader.load(item.getCover()).into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_date,item.getAdd_time());
//                        .setText(R.id.tv_content,item.getDescription())
        final TextView contentTv = helper.getView(R.id.tv_content);
        contentTv.setText(item.getDescription());
        ViewTreeObserver observer = contentTv.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean isfirstRunning = true;
            @Override
            public void onGlobalLayout() {
                if(isfirstRunning==false)return;//如果不加这一行的条件，出来之后是完整单词+。。。，如果加上，出来就是截断的单词+。。。
                Layout layout2 = contentTv.getLayout();
//                System.out.println("layout2是"+layout2);
                if(contentTv!=null&&layout2!=null){
                    int lines = layout2.getLineCount();
                    int originalLength=contentTv.getText().length();
//                    LogUtils.i("原字符串字符数量是 "+originalLength);
//                    System.out.println("当前行数是"+layout2.getLineCount());
//                    System.out.println("被省略的字符数量是"+layout2.getEllipsisCount(lines-1));//看看最后一行被省略掉了多少
//                    System.out.println("被省略的字符起始位置是"+layout2.getEllipsisStart(lines-1));//看看最后一行被省略的起始位置
//                    System.out.println("最后一个可见字符的偏移是"+layout2.getLineVisibleEnd(lines-1));
                    //开始替换系统省略号
                    if(lines<2)return;//如果只有一行，就不管了
                    if(layout2.getEllipsisCount(lines-1)==0)return;//如果被省略的字符数量为0，就不管了
//                    LogUtils.i("被省略长度 "+layout2.getEllipsisCount(lines-1));
                    String showText = contentTv.getText().toString();
                    int ellipsisStart = layout2.getEllipsisStart(lines - 1);//第二行起始位置
                    int singleLength = ellipsisStart - 1;//单行的长度
                    if (originalLength>=singleLength*2) {
//                        System.out.println("删减前"+showText);
                        showText = showText.substring(0, layout2.getLineVisibleEnd(lines-1)-singleLength).concat("...");//在此处自定义要显示的字符
//                        System.out.println("删减后"+showText);
                    }else {
//                        System.out.println("删减前"+showText);
                        showText = showText.substring(0, layout2.getLineVisibleEnd(lines-1)).concat("...");//在此处自定义要显示的字符
//                        System.out.println("删减后"+showText);
                    }

                    contentTv.setText(showText);
                    isfirstRunning=false;
                }
            }
        });

    }
}
