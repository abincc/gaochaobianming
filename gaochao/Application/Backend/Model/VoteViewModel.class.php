<?php
namespace Backend\Model;
use Think\Model\ViewModel;

/**
 * 投票图模型
 */
class VoteViewModel extends ViewModel{
    public $viewFields = array(
        'procedure_vote'=>array(
            '*',
            '_as'   =>'p',
            '_type' =>'LEFT',
        ),
        'member'=>array(
            'username',
            'mobile',
            '_as'   =>'m',
            '_on'   =>'m.id=p.member_id',
        )
    ); 
}