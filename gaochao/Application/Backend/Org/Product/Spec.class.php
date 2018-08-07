<?php
/**
 * 商品规格
 */
namespace Backend\Org\Product;

use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;
use Backend\Org\Form\Search;

class Spec{
	/**
	 * 表名
	 * @var string
	 */
	public $table = 'product_spec';
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
		/* 是否支持图片 */
		$this->field_is_color($_data);
		
		$result = update_data($this->table, $_rules, [], $_data);
		if(!is_numeric($result)) return $result;
		if(!$info) {
			$spec_value = new SpecValue();
			$spec_value->add_batch($result);
		}
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
	 * 排序
	 */
	protected function field_sort(&$_data) {
		$_data['sort'] = Form::int('sort');
	}
	/**
	 * 支持图片
	 */
	protected function field_is_color(&$_data) {
		$_data['is_color'] = Form::zero_one('is_color');
	}
}