<?php 
namespace User\Model;
use Think\Model\ViewModel;
/**
 * 查询服务商订单详情
 * @time 2016-07-26
 * @author 邹义来
 */
class CommentOrderViewModel extends ViewModel{
	public $viewFields = array(
		'service_comment'=>array(
			'*',
			'_as'=>'a',
		),
		'service_order'=> array(
			'id'=>'order_id',
			'user_telephone',
			'_as'=>'b',
			'_on'=>'a.order_id=b.id',
		),
		'service_order_detail'=>array(
			'step_id',
			'step_price',
			'status',
			'_as'=>'c',
			'_on'=>'a.step_id=c.step_id and b.id=c.order_id',
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
			'_as'=>'d',
			'_on' => 'b.house_info_id = d.id',
		),

	);
}