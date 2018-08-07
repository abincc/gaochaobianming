<?php
namespace User\Model;

use Think\Model\ViewModel;

class ComplaintViewModel extends ViewModel {
	public $viewFields = array(
		'complaints'=>array(
			'*',
			'id'=>'complaints_id',
			'_as'=>'complaints',
		),
		'order'=> array(
			'order_no',
			'total',
			'status',
			'id'=>'order_id',
			'title'=>'order_title',
			'status'=>'order_status',
			'_as'=> 'orders',
			'_on'=> 'complaints.order_id=orders.id',
		),
		'order_detail'=> array(
	
			'id'=>'order_detail_id',
			'title' => 'product_title',
			'addtime' => 'order_addtime',
			'_on'=> 'orders.id=order_detail.order_id',
		),
		'shop'=> array(
			'title'=>'shop_title',
			'_on'=> 'orders.seller_id=shop.seller_id'
		),
		'product'=>array(
			'title'=>'product_title',
			'_on'=> 'complaints.product_id=product.id'
		),
	);
}