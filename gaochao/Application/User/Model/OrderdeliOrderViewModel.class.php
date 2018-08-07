<?php
namespace User\Model;

use Think\Model\ViewModel;

class OrderdeliOrderViewModel extends ViewModel {
	public $viewFields = array(
		'order_detail'=> array(
			'*',
			'_as'=> 'a',
			'_type'=>'left'
		),
		'order'=> array(
			'member_id',
			'seller_id',
			'_as'=> 'b',
			'_on'=> 'a.order_id = b.id',
		),
		'shop'=>array(
			'id'=>'shop_id',
			'_as'=>'c',
			'_on'=>'b.seller_id = c.seller_id'	
		),
	);
}