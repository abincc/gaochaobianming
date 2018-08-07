<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 商品评价视图模型
 */
class OrderCommentViewModel extends ViewModel{
    
    public $viewFields = array(
        'order_comment'=>array(
            '*',
            '_as'   =>'v',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            '_as'   =>'m',
            '_on'   =>'m.id=v.member_id',
        )
    ); 
}