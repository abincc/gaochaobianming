<?php
/**
 * 商品分类
 */
namespace Backend\Org\Product;

use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;

class Category{
	/**
	 * 表名
	 * @var string
	 */
	public $table = 'product_category';
	/**
	 * 分类最大层级
	 * @var integer
	 */
	public $level = 2;
	/**
	 * 构造函数
	 */
	public function __construct() {
		
	}
	/**
	 * 列表
	 */
	public function index() {
		$_data = [];
		$_map = [];

		$_data['list'] = get_result($this->table, $_map, 'sort desc');
		$tree = array_to_tree($_data['list'], array(
			'root' => Form::int('category_id', 'get.') ? Form::int('category_id', 'get.') : 0,
			'function'=> array(
				'format_data' => function($row, $set) {
					/*遍历调整显示结构*/
					for($i = $set['level']; $i--; $i == 0) {
						$row['title'] = '—— '.$row['title'];
					}
					if($set['level']) $row['title'] = '|'.$row['title'];
					return $row;
				}
			)
		));
		$_data['list'] = tree_to_array($tree);
		$_data['category'] = array_to_select(get_no_del($this->table), Form::int('category_id', 'get.'));
		
		return $_data;	
	}
	/**
	 * 更新
	 */
	public function update($info = []) {
		$_data = [];
		$_rules = [];
	
		/* 分类缓存 */
		$cache = get_no_del($this->table);
		/* ID */
		if($info) $_data['id'] = $info['id'];
		/* 父级更改，更新父级以及层级 */
		if(!$info || $info['pid'] != Form::int('pid')) {
			if($this->field_pid($_data, $cache) === false) return '请选择父级';
			if($this->field_level($_data, $cache) === false) return '分类最大支持 '.$this->level.' 级';
		}
		/* 名称 */
		$this->field_title($_data, $_rules);
		/* 描述 */
		$this->field_description($_data, $_rules);
		/* 排序 */
		$this->field_sort($_data);
		
		$result = update_data($this->table, $_rules, [], $_data);
		if(!is_numeric($result)) return $result;
		$this->field_icon($result);
	
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
	 * 父级
	 */
	protected function field_pid(&$_data, $cache) {
		$pid = Form::int('pid');
	
		$_data['pid'] = $pid;
		if($pid > 0) {
			if(!isset($cache[$pid])) return false;
		}
		return true;
	}
	/**
	 * 层级
	 */
	protected function field_level(&$_data, $cache) {
		if($_data['pid'] > 0) {
			$p_info = $cache[$_data['pid']];
			if(($p_info['level'] + 1) > $this->level) return false;
			$_data['level'] = ++$p_info['level'];
		}
		return true;
	}
	/**
	 * 图标
	 */
	protected function field_icon($id, $info = []) {
		$icon_id = Form::int('icon');
		if($icon_id > 0) {
			$result = multi_file_upload($icon_id, 'Uploads/Product/Category', $this->table, 'id', $id, 'icon');
		}
	}
	/**
	 * 排序
	 */
	protected function field_sort(&$_data) {
		$_data['sort'] = Form::int('sort');
	}
}