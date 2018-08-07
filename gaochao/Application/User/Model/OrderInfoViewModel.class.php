<?php 
namespace User\Model;
use Think\Model\ViewModel;
/**
 * 查询服务商订单详情
 * @time 2016-07-26
 * @author 邹义来
 */
class OrderInfoViewModel extends ViewModel{
	public $viewFields = array(
		'service_order'=> array(
			'*',
			'id'=>'order_id',
			'type'=>'order_type',
			'_as'=>'a',
		),
		'house_info'=> array(
			'id'=>'house_info_id',
			'type',
			'style',
			'size',
			'cover',
			'decorate_style',
			'housing_estate',
			'title',
			'province',
			'city',
			'area',
			'_as'=>'b',
			'_on' => 'a.house_info_id = b.id',
		),
		'shop'=>array(
			'id'=>'shop_id',
			'_as'=>'c',
			'_on'=>'a.seller_id=c.seller_id',
		),
		'member'=>array(
			'_as'=>'d',
			'_on'=>'a.seller_id=d.id',
		),
	);
}