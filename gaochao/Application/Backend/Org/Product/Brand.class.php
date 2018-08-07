<?php
/**
 * 商品品牌
 */
namespace Backend\Org\Product;

use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;
use Backend\Org\Form\Search;

class Brand{
	/**
	 * 表名
	 * @var string
	 */
	public $table = 'product_brand';
	/**
	 * 构造函数
	 */
	public function __construct() {
		
	}
	/**
	 * 搜索
	 */
	public function search() {
		$_map = [];
		
		Search::int($_map, 'is_hid');
		Search::string_like($_map, 'title');
		
		return $_map;
	}
	/**
	 * 更新
	 */
	public function update($info = []) {
		$_data = [];
		$_rules = [];
	
		/* ID */
		if($info) $_data['id'] = $info['id'];
		/* 名称 */
		$this->field_title($_data, $_rules);
		/* 描述 */
		$this->field_description($_data, $_rules);
		/* 排序 */
		$this->field_sort($_data);
		/* 首字母 */
		$this->field_initial($_data, $_rules);
		
		$result = update_data($this->table, $_rules, [], $_data);
		if(!is_numeric($result)) return $result;
		$this->field_image($result);
	
		return $result;
	}
	/**
	 * 名称
	 */
	protected function field_title(&$_data, &$_rules) {
		$_data['title'] = Form::string('title');
		$_rules[] = Rules::_require('title', '请输入名称');
		$_rules[] = Rules::length('title', '请输入名称', 1, 20);
	}
	/**
	 * 描述
	 */
	protected function field_description(&$_data, &$_rules) {
		$_data['description'] = Form::string('description');
		$_rules[] = Rules::length('description', '请输入描述', 0, 100);
	}
	/**
	 * 图片
	 */
	protected function field_image($id, $info = []) {
		$icon_id = Form::int('image');
		if($icon_id > 0) {
			$result = multi_file_upload($icon_id, 'Uploads/Product/Brand', $this->table, 'id', $id, 'image');
		}
	}
	/**
	 * 排序
	 */
	protected function field_sort(&$_data) {
		$_data['sort'] = Form::int('sort');
	}
	/**
	 * 首字母
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_initial(&$_data, &$_rules) {
		$_data['initial'] = Form::abc_single('initial');
		$_rules[] = Rules::length('initial', '请输入首字母', 0, 1);
	}
}