<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 小事视图模型
 */
class TrifleViewModel extends ViewModel{
    public $viewFields = array(
        'trifle'=>array(
            '*',
            '_as'   =>'t',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=t.member_id',
            '_type' =>'LEFT',
        ),
        'district'=>array(
            'title',
            '_as'   =>'d',
            '_on'   =>'d.id=t.district_id',
        )
    ); 
}