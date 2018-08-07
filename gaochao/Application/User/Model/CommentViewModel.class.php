<?php 
namespace User\Model;
use Think\Model\ViewModel;
/**
 * 查询服务商需求
 */
class CommentViewModel extends ViewModel{
		public $viewFields = array(
		'comment'=> array(
			'*',
			'_type' => 'left',
		),
		'product'=> array(
			'title'=>'product_title',
			'discount_price',
			'_on' => 'comment.product_id = product.id' ,
		),
		'member'=>array(
			'nickname',
			'mobile',
			'_on'=>'comment.member_id=member.id',
		),
		'product_category'=>array(
			'discount',
			'_on'=>'product.category_id=product_category.id',
		),
	);
}