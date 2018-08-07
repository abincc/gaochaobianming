<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 保洁维修视图模型
 */
class CleanViewModel extends ViewModel{
    public $viewFields = array(
        'clean'=>array(
            '*',
            '_as'   =>'c',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=c.member_id',
            '_type' =>'LEFT',
        ),
        'district'=>array(
            'title',
            '_as'   =>'d',
            '_on'   =>'d.id=c.district_id',
        )
    ); 
}