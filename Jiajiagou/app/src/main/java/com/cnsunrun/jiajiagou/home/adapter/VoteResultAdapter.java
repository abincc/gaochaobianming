package com.cnsunrun.jiajiagou.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.home.bean.DiscussDetailBean;

/**
 * Created by j2yyc on 2018/1/23.
 */

public class VoteResultAdapter extends BaseQuickAdapter<DiscussDetailBean.InfoBean.TypeListBean, BaseViewHolder> {
    private int maxWidth;
    private int totalNum;

    public VoteResultAdapter(int maxWidth) {
        super(R.layout.item_vote_result);
        this.maxWidth = maxWidth;
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscussDetailBean.InfoBean.TypeListBean item) {
        helper.setText(R.id.tv_left_text, item.name)
                .setText(R.id.tv_vote_number, String.valueOf(item.num));
        helper.setProgress(R.id.progressBarHorizontal, item.num *100/ totalNum);
//        ImageView view = helper.getView(R.id.iv_result);
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        if (item.getTitle().equals("同意")) {
//            helper .setText(R.id.tv_vote_number,item.getAgree_number());
//            params.width=item.getAgree_number()/item.getTotal()*maxWidth;
//        }else if (item.getTitle().equals("不同意")){
//            helper .setText(R.id.tv_vote_number,item.getDisagree_number());
//            params.width=item.getDisagree_number()/item.getTotal()*maxWidth;
//        }else {
//            helper .setText(R.id.tv_vote_number,item.getAbandon_number());
//            params.width=item.getAbandon_number()/item.getTotal()*maxWidth;
//        }
//        view.setLayoutParams(params);
    }

    public void setTotalNum(int totalNum) {
        if (totalNum==0){
            totalNum=1;
        }
        this.totalNum = totalNum;
    }
}
