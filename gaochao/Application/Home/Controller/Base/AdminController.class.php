<?php
namespace Home\Controller\Base;
use Common\Controller\CommonController;
	/**
	 * 前台总父类
	 */
class AdminController extends CommonController {
	/**
	 * 全局加载
	 * @time 2014-12-26
	 * @author 郭文龙 <2824682114@qq.com>
	 */
	protected function __autoload() {
		if (C ( "SITE_STATUS" ) != 1) {
			$this->error("站点已关闭");
		}
		$this->_init();
		/* 记录URL历史 */
		$url = MODULE_NAME . '/' . CONTROLLER_NAME . '/' . ACTION_NAME;
		$ignore_url = array(
			'User/Account/Login/login_out'
		);
		if(!in_array($url,$ignore_url)){
			session('history_url',__SELF__);
		}
	}

	/**
	 * 初始化，用于继承
	 */
  protected function _init() {

	}
}
