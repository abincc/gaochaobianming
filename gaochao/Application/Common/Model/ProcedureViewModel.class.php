<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 议事事件视图模型
 */
class ProcedureViewModel extends ViewModel{
    public $viewFields = array(
        'procedure'=>array(
            '*',
            '_as'   =>'p',
            '_type' =>'LEFT',
        ),
        'district'=>array(
            'title' =>'district_title',
            '_as'   =>'d',
            '_on'   =>'d.id=p.district_id',
        ),
    ); 
}