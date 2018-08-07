<?php
/**
 * 商品规格值
 */
namespace Backend\Org\Product;

use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;
use Backend\Org\Form\Search;

class SpecValue{
	/**
	 * 表名
	 * @var string
	 */
	public $table = 'product_spec_value';
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
	public function add() {
		$spec_id = $this->verify_spec_id();
		if($spec_id === false) return '请选择规格';
		$this->add_batch($spec_id);
		return 1;
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
		
		$result = update_data($this->table, $_rules, [], $_data);
		if(!is_numeric($result)) return $result;
	
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
	 * 规格 ID
	 */
	protected function field_spec_id(&$_data, $spec_id) {
		$_data['spec_id'] = $spec_id;
	}
	/**
	 * 获取 规格并校验 规格 ID
	 * @return mixed|\Common\Org\Form\NULL|boolean
	 */
	protected function verify_spec_id() {
		$cahce = $this->cache_spec();
		
		$spec_id = Form::int('spec_id');
		if($spec_id && isset($cahce[$spec_id])) return $spec_id;
		
		return false;
	}
	/**
	 * 规格缓存
	 * @param string $table
	 */
	public function cache_spec($key = 'hid') {
		$func = 'get_no_'.$key;
		return $func('product_spec');
	}
	/**
	 * 批量添加
	 */
	public function add_batch($spec_id) {
		$list = I('post.spec_val/a');
		
		if(!$list) return false;
		foreach($list as $v) {
			$_data = [];
			$_rules = [];
			
			$_data['title'] = Form::string('title', 'data.', '', $v);
			$_rules[] = Rules::_require('title', '请输入名称');
			$_rules[] = Rules::length('title', '请输入名称', 1, 20);
			$_data['description'] = Form::string('description', 'data.', '', $v);
			$_rules[] = Rules::length('description', '请输入描述', 0, 100);
			$_data['sort'] = Form::int('sort', 'data.', '', $v);
			$_data['spec_id'] = $spec_id;
			
			$result = update_data($this->table, $_rules, [], $_data);
		}
		return true;
	}
}