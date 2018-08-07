<?php
namespace User\Model;

use Think\Model\ViewModel;

class ProductOrderViewModel extends ViewModel {
	public $viewFields = array(
		'order'=> array(
			'member_id',
			'seller_id',
			'_as'=> 'a',
		),
		'order_detail'=> array(
			'*',
			'_as'=> 'b',
			'_on'=> 'a.id=b.order_id',
		),
		'product'=> array(

			'_as'=> 'c',
			'_on'=> 'b.product_id=c.id'
		),
	);
}