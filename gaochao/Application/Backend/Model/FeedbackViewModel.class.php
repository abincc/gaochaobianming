<?php
namespace Backend\Model;
use Think\Model\ViewModel;

/**
 * 意见反馈视图模型
 */
class FeedbackViewModel extends ViewModel{
    public $viewFields = array(
        'feedback'=>array(
            '*',
            '_as'   =>'f',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'district_id',
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=f.member_id',
        ),
        'district'=>array(
            'title',
            '_as'   =>'d',
            '_on'   =>'d.id=m.district_id',
        ),
    ); 
}