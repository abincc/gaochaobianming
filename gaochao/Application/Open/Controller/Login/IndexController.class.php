<?php
namespace Open\Controller\Login;
use Open\Controller\Base\AdminController;
	/**
	 * 支付总类
	 */
class IndexController extends AdminController {
	public function callback($type = null, $url = null, $code = null){
		/*获取第三方登录请求类型*/
		(empty($type) || empty($url) || empty($code)) && $this->error('参数错误');
		redirect($url.'&code='.$code);
		exit;
	}
}