<?php
	/**
	* 无需写对应控制器，自动加载对应模版
	* @name 空控制器
	* @author llf
	* @time 2017-06-06
	*/
namespace Home\Controller;
use Home\Controller\Base\AdminController;
	/**
	* 无需写对应控制器，自动加载对应模版
	* @name 空控制器
	* @author llf
	* @time 2017-06-06
	*/
class EmptyController extends AdminController {
	/**
	* 无需写对应ACTION，自动加载对应模版
	* @name 空方法
	* @author llf
	* @time 2017-06-06
	*/
	public function _empty() {

		$this->ajaxReturn(array('status'=>0,'msg'=>'请检查地址是否错误'));
	}
}
