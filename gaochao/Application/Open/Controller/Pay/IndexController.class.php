<?php
namespace Open\Controller\Pay;
use Open\Controller\Base\AdminController;
	/**
	 * 支付总类
	 */
class IndexController extends AdminController {
	
	public function index(){
		$callback = urldecode($_GET['url']);
		$html = file_get_contents('http://127.0.0.1/'.$callback);
		if($html){
				echo $html;
		}else{
				echo file_get_contents('http://www.baidu.com');
		}
	}
}