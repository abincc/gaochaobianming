<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 卡券视图模型
 */
class CardViewModel extends ViewModel{
    public $viewFields = array(
        'coupon_card'=>array(
            '*',
            '_as'   =>'c',
            '_type' =>'LEFT',
        ),
        'coupon'=>array(
            'title',
            'description',
            'amount',
            'minimum',
            'begin_date',
            'end_date',
            '_as'   =>'p',
            '_on'   =>'p.id=c.coupon_id',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=c.member_id',
        )
    ); 
}