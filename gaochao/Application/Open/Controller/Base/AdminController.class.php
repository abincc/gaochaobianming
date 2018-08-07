<?php
namespace Open\Controller\Base;
use Common\Controller\CommonController;
	/**
	 * 开放功能总父类
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
	}

	/**
	 * 初始化，用于继承
	 */
  protected function _init() {

	}
}
