<?php 
namespace User\Model;

use Think\Model\ViewModel;
/**
 * 装修日记表 会员表 房屋信息表 基本分类表的房屋类型 装修日志表 
 */
class MyDiaryViewModel extends ViewModel{
		public $viewFields = array(
		'diary'=> array(
			'*',
			'_type' => 'LEFT' ,
		),
		'house_info'=> array(
			'id'=>'house_info_id',
			'city',
			'style',
			'size',
			'size_id',
			'type',
			'housing_estate',
			'decorate_style',
			'style'=>'style_id',
			'have_company',
			'decoration_company',
			'_on' => 'house_info.id = diary.house_info_id' ,
			'_type' => 'left',
		),
		'base_tag'=> array(
			'title'=>'house_style',
			'_on' => 'base_tag.id = house_info.style',
		)
	);
}
?>