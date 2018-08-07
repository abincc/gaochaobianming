<?php
/**
 * 物业管理总父类
 */
namespace Backend\Controller\Property;

use Backend\Controller\Base\AdminController;
use Backend\Org\Form\Form;

class IndexController extends AdminController {
	/**
	 * 编辑数据验证
	 * @param unknown $key
	 * @param unknown $msg
	 * @return string[]|unknown
	 */
	protected function _edit($msg, $key = 'ids') {		
		$id = Form::int($key, '');
		if(!$id) $this->error($msg);
		$map = [
			'id'=> $id,
		];
		$info = get_info($this->table, $map);
		if(!$info) $this->error($msg);
		
		return $info;
	}
}