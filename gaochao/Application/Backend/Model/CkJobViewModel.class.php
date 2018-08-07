<?php
namespace Backend\Model;
use Think\Model\ViewModel;

/**
 * 用户视图模型
 */
class CkJobViewModel extends ViewModel{
    public $viewFields = array(
        'ck_job'=>array(
            '*',
            '_as'   =>'m',
            '_type' =>'LEFT',
        ),
        'district'=>array(
            'title'=>'district_title',
            '_as'   =>'d',
            '_on'   =>'d.id=m.district_id',
        )
    ); 
}