<?php 
namespace User\Model;
use Think\Model\ViewModel;
/**
 * 查询服务商需求
 */
class CommentShopViewModel extends ViewModel{
		public $viewFields = array(
		'comment'=> array(
			'*',
			'_type' => 'left',
		),
		'shop'=> array(
			'title'=>'shop_title',
			'_on' => 'comment.shop_id = shop.id' ,
		),
	);
}