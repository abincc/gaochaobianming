<?php 
namespace User\Model;
use Think\Model\ViewModel;
/**
 * 商品店铺收藏模型
 */
class ProductcollectViewModel extends ViewModel{
		public $viewFields = array(
		'collect'=> array(
			'*',
			'_type' => 'left' ,
		),
		'product'=> array(
			'*',
			'id'=>'product_id',
			'_on' => 'collect.collect_id = product.id' ,
		),
	);
}
?>