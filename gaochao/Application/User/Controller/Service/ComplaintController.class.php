<?php
namespace User\Controller\Service;
use User\Controller\Base\BaseController;
class ComplaintController extends BaseController {
	
	/*
	 * 投诉
	 * @time 2015-07-21
	 * @author 周海涛
	 */
	public function index() {
		$this->display();
	}
	
	public function details(){
		$this->display();
	}

	/*
	 * 投诉的增加
	 */
	public function add() {
		if (IS_POST) {
			$this->success ("投诉成功！",U('index'));
		} else {
			$this->display ();
		}
	}
}