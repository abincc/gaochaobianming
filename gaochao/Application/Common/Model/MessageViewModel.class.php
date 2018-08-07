<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 消息视图模型
 */
class MessageViewModel extends ViewModel{
    public $viewFields = array(
        'message'=>array(
            '*',
            '_as'   =>'s',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=s.member_id',
        )
    ); 
}