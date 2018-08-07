<?php
namespace User\Model;

use Think\Model\ViewModel;

class RefundViewModel extends ViewModel {
	public $viewFields = array(
		'order'=> array(
			'id'=> 'orderid',
			'shipping_address',
			'order_no',
			'paytime',
			'total',
			'freight',
			'confirmtime',
			'seller_price',
			'integral',
			'real_money',
			'title'=> 'shop_title',
			'_as'=> 'a',
		),
		'order_detail'=> array(
			'id'=> 'detail_id',
			'attr_value',
			'title'=> 'product_title',
			'cover',
			'description'=> 'product_description',
			'price'=> 'product_price',
			'num',		
			'_as'=> 'b',
			'_on'=> 'a.id=b.order_id',
		),
		'refund'=> array(
			'*',
			'_as'=> 'c',
			'_on'=> 'a.id=c.order_id'
		),
		'member'=> array(
			'username',
			'_as'=> 'd',
			'_on'=> 'a.member_id=d.id',
		),
	);
}