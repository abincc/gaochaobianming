<?php 
namespace User\Model;
use Think\Model\ViewModel;
/**
 * 查询服务商订单详情
 * @time 2016-07-26
 * @author 邹义来
 */
class ServiceRefundViewModel extends ViewModel{
	public $viewFields = array(
		'service_refund'=> array(
			'*',
			'_as'=>'a',
		),
		'service_order'=> array(
			'user_telephone',
			'order_no',
			'house_info_id',
			'_as'=>'b',
			'_on'=>'a.order_id=b.id',
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
			'_as'=>'c',
			'_on' => 'b.house_info_id = c.id',
		),
	);
}