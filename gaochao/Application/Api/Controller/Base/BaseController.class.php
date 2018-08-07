<?php
namespace Api\Controller\Base;
use Common\Controller\ApiController;

class BaseController extends ApiController{
	

	protected function __autoload(){

		$this->__init();
	}
	
	protected function __init(){
		$posts = I();
		$this->auth($posts);
	}	


}