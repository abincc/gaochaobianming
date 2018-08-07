<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 车位视图模型
 */
class ParkingViewModel extends ViewModel{
    
    public $viewFields = array(
        'parking'=>array(
            '*',
            '_as'   =>'p',
            '_type' =>'LEFT',
        ),
        'district'=>array(
            'title' =>'district_title',
            '_as'   =>'d',
            '_on'   =>'d.id=p.district_id',
        )
    ); 
}