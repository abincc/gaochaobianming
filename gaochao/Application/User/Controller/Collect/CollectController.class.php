<?php
namespace User\Controller\Collect;
use User\Controller\Base\BaseController;
class CollectController extends BaseController{
	/**
	 * 服务商店铺收藏列表页
	 * @time 2016.8.10
	 */
	function shop(){
		$this->display();
	}

	/**
	 * 商城店铺收藏
	 */
	function mall(){
		$this->display();
	} 

	/**
	 * 商品收藏列表页
	 */
	function product(){
		$this->display();
	}
}