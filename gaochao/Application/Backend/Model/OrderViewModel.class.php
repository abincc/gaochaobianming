<?php
namespace Backend\Model;
use Think\Model\ViewModel;

/**
 * 订单视图模型
 */
class OrderViewModel extends ViewModel{
    public $viewFields = array(
        'order'=>array(
            '*',
            '_as'   =>'o',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=o.member_id',
        )
    ); 
}