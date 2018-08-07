<?php
	/**
	 * demo
	 * @package 
	 * @author 王淳熙 
	 */
namespace Backend\Controller\Demo;
use Backend\Controller\Base\AdminController;
	/**
	 * demo
	 * @package 
	 * @author 王淳熙 
	 */
class IndexController extends AdminController {
	/**
	 * index
	 * @param $tpl		 
	 * @author 王淳熙 
	 */
	public function index($tpl='index') {
		$this->display ( $tpl );
	}
	/**
	 * 文件加载	 
	 * @author 王淳熙 
	 */
	public function down() {
		$http = new \Org\Net\Http ();
		$http->download ( './Public/Static/fonts/fontawesome-webfont.ttf' );
	}
	/**
	 * del	 
	 * @author 王淳熙 
	 */
	public function del() {
		$this->success ( "删除成功" );
	}
}