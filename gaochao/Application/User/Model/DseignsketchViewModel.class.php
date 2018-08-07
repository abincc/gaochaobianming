<?php 
namespace User\Model;

use Think\Model\ViewModel;
/**
 * 效果图图片表-户型数据表
 */
class DseignsketchViewModel extends ViewModel{
		public $viewFields = array(
		'design_sketch_image'=> array(
			'*',
			'_type' => 'left' ,
		),
		'design_sketch'=> array(
			'*',
			'_on' => 'design_sketch_image.des_id = design_sketch.des_id' ,
		),
	);
}
?>