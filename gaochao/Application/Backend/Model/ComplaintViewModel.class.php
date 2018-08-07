<?php
namespace Backend\Model;
use Think\Model\ViewModel;

/**
 * 投诉建议图模型
 */
class ComplaintViewModel extends ViewModel{
    public $viewFields = array(
        'complaint'=>array(
            '*',
            '_as'   =>'c',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=c.member_id',
        ),
        'district'=>array(
            'title',
            '_as'   =>'d',
            '_on'   =>'d.id=c.district_id',
        ),
    ); 
}