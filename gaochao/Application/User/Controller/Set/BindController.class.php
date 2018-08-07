<?php
namespace User\Controller\Set;
	/**
	 * 账号绑定
	 */
class BindController extends IndexController { 
	/**
	 * 表名
	 * @var string
	 */
	protected $table='member';
	
	/**
	 * 列表
	 * @author 秦晓武
	 * @time 2016-08-16
	 */
	public function index(){
		$this->display();
	}
}
