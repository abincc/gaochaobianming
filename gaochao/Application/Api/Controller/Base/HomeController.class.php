<?php
namespace Api\Controller\Base;
use Common\Controller\ApiController;

class HomeController extends ApiController{
	
	//接口公共模块基础控制器 无登限制
	protected function __autoload(){

		$this->__init();
	}
	
	protected function __init(){

	}

}