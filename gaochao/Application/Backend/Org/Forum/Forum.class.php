<?php
/**
 * 版块管理
 */
namespace Backend\Org\Forum;

use Common\Org\Forum\Forum as _Forum;
use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;

class Forum extends _Forum{	
	public function search() {
		$_map = [];
		
		return $_map;
	}
	/**
	 * 列表
	 */
	public function _list() {
		$_data = [];
		$_map = [];
	
		$_data['list'] = get_result($this->table, $_map, 'sort desc');
		$tree = array_to_tree($_data['list'], array(
			'root' => 0,
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
	
		return $_data;
	}
	/**
	 * 分区
	 * @param array $info
	 */
	public function partition($info = []) {
		$_data = [];
		$_rules = [];
		
		if($info) $_data['id'] = $info['id'];
		$this->field_title($_data, $_rules);
		$this->field_description($_data, $_rules);
		$this->field_sort($_data);
		
		return $this->update($_data, $_rules);
	}
	/**
	 * 版块
	 * @param array $info
	 */
	public function _forum($info = []) {
		$_data = [];
		$_rules = [];
		
		if($info) $_data['id'] = $info['id'];
		$this->field_pid($_data, $_rules);
		$this->field_type($_data);
		$this->field_title($_data, $_rules);
		$this->field_description($_data, $_rules);
		$this->field_sort($_data);
		
		$M = M();
		$M->startTrans();
		$result = $this->update($_data, $_rules);
		if(is_numeric($result)) {
			$this->field_icon($result, $info);
			$M->commit();
			
			return $result;
		}
		$M->rollback();
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
	 * 更新
	 */
	protected function update($_data, $_rules) {
		return update_data($this->table, $_rules, [], $_data);
	}
	/**
	 * 分区
	 */
	protected function field_pid(&$_data, &$_rules) {
		$pid = Form::int('pid');
	
		$_data['pid'] = $pid;
		if($pid > 0) {
			$cache = $this->cache_partition();
			if(!isset($cache[$pid])) $_data['pid'] = '';
		}
		Rules::_require('', '请选择分区');
	}
	/**
	 * 图标
	 */
	protected function field_icon($id, $info = []) {
		$icon_id = Form::int('icon');
		if($icon_id > 0) {
			$result = multi_file_upload($icon_id, 'Uploads/Forum/Forum', $this->table, 'id', $id, 'icon');
		}
	}
	/**
	 * 类型
	 */
	protected function field_type(&$_data, $type = 1) {
		$_data['type'] = $type;
	}
}