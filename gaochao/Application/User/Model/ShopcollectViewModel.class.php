<?php 
namespace User\Model;

use Think\Model\ViewModel;
/**
 * 商城店铺收藏模型
 */
class ShopcollectViewModel extends ViewModel{
		public $viewFields = array(
		'collect'=> array(
			'*',
			'_type' => 'left' ,
		),
		'shop'=> array(
			'*',
			'id'=>'shop_id',
			'_on' => 'collect.collect_id = shop.id' ,
		),
	);
}
?>