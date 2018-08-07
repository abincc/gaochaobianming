<?php
/**
 * 小区
 */
namespace Backend\Org\Property;

use Common\Org\Amap\YunTu;
use Backend\Org\Form\Search;
use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;

class District{
	public $table = 'district';
	protected $yun_tu;
	
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
	 * @param array $info
	 */
	public function update($info = []) {
		$_data = [];
		$_rules = [];
		
		if($info) $_data['id'] = $info['id'];
		$this->field_title($_data, $_rules);
		$this->field_contacts($_data, $_rules);
		$this->field_contact_number($_data, $_rules);
		$province = $this->field_province($_data, $_rules);
		$city = $this->field_city($_data, $_rules, $province);
		$this->field_area($_data, $_rules, $province, $city);
		$this->field_address($_data, $_rules);
		$this->field_longitude($_data, $_rules);
		$this->field_latitude($_data, $_rules);
		$M = M();
		$M->startTrans();
		$result = update_data($this->table, $_rules, [], $_data);
		if(!is_numeric($result)) {
			$M->rollback();
			return $result;
		}
		/* 云图 - S */
		$result_yuntu = $this->yun_tu_update($_data, $info, $result);
		if($result_yuntu === false) {
			$M->rollback();
			return '小区信息更新失败';
		}
		$M->commit();
		return $result;
		/* 云图 - E */
	}
	/**
	 * 名称
	 */
	protected function field_title(&$_data, &$_rules) {
		$_data['title'] = Form::string('title');
		$_rules[] = Rules::_require('title', '请输入名称');
		$_rules[] = Rules::length('title', '请输入名称', 1, 30);
	}
	/**
	 * 联系人
	 */
	protected function field_contacts(&$_data, &$_rules) {
		$_data['contacts'] = Form::string('contacts');
		$_rules[] = Rules::_require('contacts', '请输入联系人');
		$_rules[] = Rules::length('contacts', '请输入联系人', 1, 10);
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
	 * 省
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @return mixed|\Common\Org\Form\NULL|string
	 */
	protected function field_province(&$_data, &$_rules) {
		$_data['province'] = '';
	
		$province = Form::int('province');
		if($province) {
			$cache = $this->cache_area_tree();
			if(isset($cache[$province])) {
				$_data['province'] = $province;
				return $province;
			}
		}
		$_rules[] = Rules::_require('province', '请选择地址1');
	
		return '';
	}
	/**
	 * 市
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param unknown $province
	 * @return mixed|\Common\Org\Form\NULL|string
	 */
	protected function field_city(&$_data, &$_rules, $province) {
		$_data['city'] = '';
		$city = Form::int('city');
		if($province && $city) {
			$cache = $this->cache_area_tree();
			if(isset($cache[$province]['_child'][$city])) {
				$_data['city'] = $city;
				return $city;
			}
		}
		$_rules[] = Rules::_require('city', '请选择地址2');
	
		return '';
	}
	/**
	 * 区
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param unknown $city
	 * @return void|mixed|\Common\Org\Form\NULL|string
	 */
	protected function field_area(&$_data, &$_rules, $province, $city) {
		$_data['area'] = '';
		
		$cache = $this->cache_area_tree();
		if(!count($cache[$province]['_child'][$city]['_child'])) return ;
		$area = Form::int('area');
		if(isset($cache[$province]['_child'][$city]['_child'][$area])) {
			$_data['area'] = $area;
			return $area;
		}
		$_rules[] = Rules::_require('area', '请选择地址3');
	
		return '';
	}
	/**
	 * 详细地址
	 */
	protected function field_address(&$_data, &$_rules) {
		$_data['address'] = Form::string('address');
		$_rules[] = Rules::_require('address', '请输入详细地址');
		$_rules[] = Rules::length('address', '请输入详细地址', 1, 100);
	}
	/**
	 * 区域搜索
	 */
	protected function search_area($search, $pid = 0) {
		$cache = $this->cache_area();
		foreach($cache as $v) {
			if($v['title'] == $search && $v['pid'] == $pid) return $v;
		}
	
		return false;
	}
	/**
	 * 经度
	 */
	protected function field_longitude(&$_data, &$_rules) {
		$_data['longitude'] = Form::float('longitude');
		$_rules[] = Rules::_require('longitude', '请选择经度');
	}
	/**
	 * 纬度
	 */
	protected function field_latitude(&$_data, &$_rules) {
		$_data['latitude'] = Form::float('latitude');
		$_rules[] = Rules::_require('latitude', '请选择纬度');
	}
	/**
	 * 云图对象
	 * @return \Common\Org\Amap\YunTu
	 */
	protected function yun_tu() {
		if($this->yun_tu) return $this->yun_tu;
		$this->yun_tu = new YunTu();
	}
	/**
	 * 区域缓存
	 * @param string $key
	 */
	public function cache_area($key = 'hid') {
		$func = 'get_no_'.$key;
	
		return $func('area');
	}
	/**
	 * 地址库树状结构
	 * @return 结果集[]
	 */
	protected function cache_area_tree() {
		static $cache = [];
		if(!$cache) $cache = list_to_tree($this->cache_area(), 'id', 'pid', '_child', 0, 'id');
	
		return $cache;
	}
	/**
	 * 云图更新
	 * @param unknown $_data
	 * @param unknown $info
	 */
	protected function yun_tu_update($_data, $info, $id) {
		$cloud_id = 0;
		$this->yun_tu();
		$address = Form::string('province').Form::string('city').Form::string('area').Form::string('address');
		if($info && $info['cloud_id']) {
			$result = $this->yun_tu->update($_data['title'], $_data['longitude'].','.$_data['latitude'], $address, $_data['contact_number'], $info['cloud_id']);
			if($result !== false) return true;
		}else {
			$cloud_id = $this->yun_tu->add($_data['title'], $_data['longitude'].','.$_data['latitude'], $address, $_data['contact_number']);
			if($cloud_id) {
				$data = [
					'id'=> $id,
					'cloud_id'=> $cloud_id,
				];
				$result = update_data($this->table, [], [], $data);
				if(is_numeric($result)) return true;
			}
		}
		return false;
	}
}