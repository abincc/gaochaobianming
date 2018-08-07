<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 申请志愿者视图模型
 */
class VolunteerViewModel extends ViewModel{
    
    public $viewFields = array(
        'apply_volunteer'=>array(
            '*',
            '_as'   =>'v',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=v.member_id',
        )
    ); 
}