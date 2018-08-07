<?php
namespace Common\Model;
use Think\Model\ViewModel;
	/**
	* 资金模型
	* @time 2016-08-02
	* @author 秦晓武
	*/
class CRMViewModel extends ViewModel {
	public $viewFields = array(
		'capital_record'=>array('*','_as'=>'cr','_type'=>'LEFT'),
		'member_1'=>array('mobile' => 'from_mobile','nickname'=>'from_nickname','_table' => 'sr_member','_as' => 'm_1','_on'=>'cr.from_member_id = m_1.id','_type'=>'LEFT'),
		'member_2'=>array('mobile' => 'to_mobile','nickname'=>'to_nickname','_table' => 'sr_member','_as' => 'm_2','_on'=>'cr.to_member_id = m_2.id'),
	);
}
	