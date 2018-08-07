<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 体检报告视图模型
 */
class ReportViewModel extends ViewModel{
    
    public $viewFields = array(
        'report'=>array(
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