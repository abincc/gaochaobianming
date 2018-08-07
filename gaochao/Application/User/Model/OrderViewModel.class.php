<?php
namespace User\Model;

use Think\Model\ViewModel;

class OrderViewModel extends ViewModel {
	public $viewFields = array(
		'order'=> array(
			'total',
			'price_reduction',
			'seller_price',
			'confirmtime',
			'freight',
			'integral',
			'real_money',
			'member_id',
			'seller_id',
			'_as'=> 'a',
		),
		'order_detail'=> array(
			'*',
			'_as'=> 'b',
			'_on'=> 'a.id=b.order_id',
		),
	);
}