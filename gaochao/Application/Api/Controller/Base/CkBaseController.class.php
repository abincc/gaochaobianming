<?php
namespace Api\Controller\Base;
use Common\Controller\ApiController;

class CkBaseController extends ApiController{
	

	protected function __autoload(){

		$this->__init();
	}
	
	protected function __init(){
		$posts = I();

		$Member = D('CkMember');
		$this->member_id = $Member->check_token($posts);
		if(!$this->member_id){
			$status = '0';
			if(!empty($Member->status)){
				$status = $Member->status;
			}
			$this->apiReturn(array('status'=>$status,'msg'=>$Member->getError()));
		}
	}

}