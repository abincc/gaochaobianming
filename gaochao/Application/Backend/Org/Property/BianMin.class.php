<?php
/**
 * 商品品牌
 */
namespace Backend\Org\Property;

use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;
use Backend\Org\Form\Search;

class BianMin{
	/**
	 * 表名
	 * @var string
	 */
	public $table = 'bian_min';
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
		/* 服务内容 */
		$this->field_service($_data, $_rules);
		/* 联系电话 */
		$this->field_contact_number($_data, $_rules);
		/* 排序 */
		$this->field_sort($_data);
		
		$result = update_data($this->table, $_rules, [], $_data);
		if(!is_numeric($result)) return $result;
		if(!$this->field_image($result)) return '请上传图片';
	
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
	 * 联系电话
	 */
	protected function field_contact_number(&$_data, &$_rules) {
		$_data['contact_number'] = I('post.contact_number', '', ['/^[\d-]{1,20}$/']);
		$_rules[] = Rules::_require('contact_number', '请输入联系电话');
		$_rules[] = Rules::length('contact_number', '请输入联系电话', 1, 20);
	}
	/**
	 * 服务
	 */
	protected function field_service(&$_data, &$_rules) {
		$_data['service'] = Form::string('service');
		$_rules[] = Rules::_require('service', '请输入服务内容');
		$_rules[] = Rules::length('service', '请输入服务内容', 1, 200);
	}
	/**
	 * 图片
	 */
	protected function field_image($id, $info = []) {
		$icon_id = Form::int('image');
		if($info) {
			if($icon_id > 0 && file_upload($icon_id, 'Uploads/Property/BianMin', $this->table, 'id', $id, 'image')) return true;
			if($info['image'] == '') return false;
		}else {
			if($icon_id > 0 && file_upload($icon_id, 'Uploads/Property/BianMin', $this->table, 'id', $id, 'image')) return true;
			return false;
		}
	}
	/**
	 * 排序
	 */
	protected function field_sort(&$_data) {
		$_data['sort'] = Form::int('sort');
	}
}