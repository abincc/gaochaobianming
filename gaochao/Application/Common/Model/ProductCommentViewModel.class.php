<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 商品评论视图模型
 */
class ProductCommentViewModel extends ViewModel{
    
    public $viewFields = array(
        'order_comment'=>array(
            '*',
            '_as'   =>'c',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'nickname',
            '_as'   =>'m',
            '_on'   =>'m.id=c.member_id',
        ),
        'product'=>array(
            'title' =>'product_title',
            '_as'   =>'p',
            '_on'   =>'p.id=c.product_id',
        )
    ); 
}