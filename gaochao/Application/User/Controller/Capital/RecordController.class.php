<?php
namespace User\Controller\Capital;
	/**
	 * 收支记录
	 */
class RecordController extends IndexController{
	/**
	 * 资金记录表
	 * @var string
	 */
	protected $table = 'capital_record';
	/**
	 * 收支记录
	 */
	public function index(){
		$gets = I('get.');
		if($gets['deal_no']){
			$map['deal_no'] = array('like','%'.trim($gets['deal_no']).'%');
		}
		if($gets['type']){
			$map['type'] = $gets['type'];
		}
		$start = !empty($gets['s_addtime'])?$gets['s_addtime']:0;
		$end = !empty($gets['e_addtime'])?($gets['e_addtime'] . ' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));
		
		$map['from_member_id|to_member_id'] = session('member_id');
		$order = 'id desc';
		$list = $this->page($this->table,$map,$order,$field);
		$this->display();
	}
}