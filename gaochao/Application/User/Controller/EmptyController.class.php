<?php
	/**
	* 无需写对应控制器，自动加载对应模版
	* @name 空控制器
	* @author 秦晓武
	* @time 2016-06-06
	*/
namespace User\Controller;
use User\Controller\Base\BaseController;
	/**
	* 无需写对应控制器，自动加载对应模版
	* @name 空控制器
	* @author 秦晓武
	* @time 2016-06-06
	*/
class EmptyController extends BaseController {
	/**
	* 无需写对应ACTION，自动加载对应模版
	* @name 空方法
	* @author 秦晓武
	* @time 2016-06-06
	*/
	public function _empty() {
		$this->display();
	}
}
