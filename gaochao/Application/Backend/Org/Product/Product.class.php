<?php
/**
 * 商品
 */
namespace Backend\Org\Product;

use Backend\Org\Form\Form;
use Backend\Org\Form\Rules;
use Backend\Org\Form\Search;

class Product{
	/**
	 * 表名
	 * @var string
	 */
	public $table = 'product';
	public $table_sku = 'product_sku';
	public $table_image = 'product_image';
	
	public function __construct() {
		
	}
	/**
	 * 搜索
	 */
	public function search() {
		$_map = [];

		$this->search_category($_map);
		Search::int($_map, 'is_sale');
		Search::int($_map, 'recommend');
		Search::string_like($_map, 'title');
		
		return $_map;
	}
	/**
	 * 分类搜索
	 */
	protected function search_category(&$_map) {
		$category_id = Form::int('cid', 'get.');
		if($category_id) {
			$cache = $this->cache_category();
			$_map['sub_category_id'] = ['in', get_all_child_ids($cache, $category_id)];
		}
	}
	/**
	 * 更新
	 * @param unknown $info
	 */
	public function update($info = []) {
		$_data = [];
		$_rules = [];
		if($info) $_data['id'] = $info['id'];
		/* 分类 */
		$this->field_category($_data, $_rules);
		/* 品牌 */
		$this->field_brand_id($_data, $_rules);
		/* 名称 */
		$this->field_title($_data, $_rules);
		/* 简介 */
		$this->field_description($_data, $_rules);
		/* 详情 */
		$this->field_content($_data, $_rules);
		/* 市场价 */
		$this->field_market_price($_data, $_rules);
		/* 价格 */
		$this->field_price($_data, $_rules);
		/* 库存 */
		$this->field_inventory($_data, $_rules);
		/* 运费 */
		$this->field_freight($_data, $_rules);
		/* 上架状态 */
		$this->field_is_sale($_data, $_rules);
		/* 推荐 */
		$this->field_recommend($_data, $_rules);
		/* 排序 */
		$this->field_sort($_data, $_rules);
		/* 商品规格 */
		$spec = $this->field_spec($_data, $_rules);
		if($spec === false) return '规格数据错误';
		$M = M();
		$M->startTrans();
		$result = update_data($this->table, $_rules, [], $_data);
		if(!is_numeric($result)) {
			$M->rollback();
			return $result;
		}
		/* 上传封面图 */
		$result_image = $this->field_image($result, $info);
		if($result_image !== true) {
			$M->rollback();
			return $result_image;
		}
		if($spec) {
			$result_sku = $this->field_sku($spec, $result);
			if($result_sku !== true) {
				$M->rollback();
				return $result_sku;
			}
		}
		$M->commit();
		return $result;
	}
	/**
	 * 品牌
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_brand_id(&$_data, &$_rules) {
		$cahce = $this->cache_brand();
		$brand_id = Form::int('brand_id');
		
		$_data['brand_id'] = $brand_id;
		if($brand_id && !isset($cahce[$brand_id])) {
			$_data['brand_id'] = '';
			$_rules[] = Rules::_require('brand_id', '请选择品牌');
		}
	}
	/**
	 * 品牌缓存
	 * @param string $key
	 */
	public function cache_brand($key = 'hid') {
		$func = 'get_no_'.$key;
		
		return $func('product_brand');
	}
	/**
	 * 商品分类
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_category(&$_data, &$_rules) {
		$cache = $this->cache_category();
		$category_id = Form::int('category_id');
		if($category_id && isset($cache[$category_id]) && $cache[$category_id]['pid'] != 0) {
			$_data['category_id'] = $cache[$category_id]['pid'];
			$_data['sub_category_id'] = $category_id;
			return ;
		}
		$_data['category_id'] = '';
		$_rules[] = Rules::_require('category_id', '请选择分类');
	}
	/**
	 * 分类缓存
	 * @param string $key
	 */
	public function cache_category($key = 'hid') {
		$func = 'get_no_'.$key;
	
		return $func('product_category');
	}
	/**
	 * 名称
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_title(&$_data, &$_rules) {
		$_data['title'] = Form::string('title');
		$_rules[] = Rules::_require('title', '请输入名称');
		$_rules[] = Rules::length('title', '请输入名称', 1, 50);
	}
	/**
	 * 简介
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_description(&$_data, &$_rules) {
		$_data['description'] = Form::string('description');
		$_rules[] = Rules::length('description', '请输入简介', 0, 200);
	}
	/**
	 * 详情
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_content(&$_data, &$_rules) {
		$_data['content'] = $_POST['content'];
	}
	/**
	 * 价格
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_price(&$_data, &$_rules) {
		$_data['price'] = Form::float('price');
		$_rules[] = Rules::currency('price', '请输入价格');
	}
	/**
	 * 市场价
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_market_price(&$_data, &$_rules) {
		$_data['market_price'] = Form::float('market_price');
		$_rules[] = Rules::currency('market_price', '请输入市场价');
	}
	/**
	 * 运费
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_freight(&$_data, &$_rules) {
		$_data['freight'] = Form::float('freight');
	}
	/**
	 * 库存
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_inventory(&$_data, &$_rules) {
		$_data['inventory'] = Form::int('inventory');
	}
	/**
	 * 上架状态
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_is_sale(&$_data, &$_rules) {
		$_data['is_sale'] = Form::zero_one('is_sale');
	}
	/**
	 * 推荐
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_recommend(&$_data, &$_rules) {
		$_data['recommend'] = Form::zero_one('recommend');
	}
	/**
	 * 排序
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_sort(&$_data, &$_rules) {
		$_data['sort'] = Form::int('sort');
	}
	/**
	 * 封面图
	 */
	protected function field_image($product_id, $product_info = []) {
		$result = false;
		
		$id = Form::int('image');
		if($id) $result = file_upload($id, 'Uploads/Product/Product/Cover', $this->table, 'id', $product_id, 'image');
		if($product_info) {
			if($result && $product_info) unlink($product_info['image']);
		}else {
			if($result !== true) return '请上传封面图';
		}
		
		return true;
	}
	/**
	 * 规格和规格值
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_spec(&$_data, &$_rules) {	
		$list = I('post.sp_val/a');
		if($list) {
			$_spec_value = $this->_spec_value($list);
			if($_spec_value !== false) {
				$_sku = $this->_sku($_spec_value['_spec_value'], -1);
				$_sku_string = $this->_sku_string($_sku);
				$_form_sku = $this->_form_sku_string();
				if($_form_sku && !array_diff($_sku_string, $_form_sku)) {
					$_data['spec'] = serialize($_spec_value['spec']);
					$_data['spec_value'] = serialize($_spec_value['spec_value']);
					
					return $_form_sku;
				}
			}
			return false;
		}
		$_data['spec'] = '';
		$_data['spec_value'] = '';
		
		return true;
	}
	/**
	 * 规格值数据处理
	 * @return boolean|unknown[]
	 */
	protected function _spec_value($list) {
		$_data = [];
		
		$cache_spec = $this->cache_spec();
		$cache_spec_value = $this->cache_spec_value();
		foreach($list as $k=> $v) {
			if(isset($cache_spec[$k])) {
				$_data['spec'][$k] = $cache_spec[$k]['title'];
				$_array = [];
				foreach($v as $_v) {
					if(isset($cache_spec_value[$_v]) && $cache_spec_value[$_v]['spec_id'] == $k) {
						$_data['spec_value'][$k][$_v] = $cache_spec_value[$_v]['title'];
						$_array[] = $_v;
					}else {
						return false;
					}
				}
				$_data['_spec_value'][] = $_array;
			}else {
				return false;
			}
		}
		return $_data;
	}
	/**
	 * 规格缓存
	 * @param string $key
	 */
	public function cache_spec($key = 'hid') {
		$func = 'get_no_'.$key;
		
		return $func('product_spec');
	}
	/**
	 * 规格值缓存
	 * @param string $key
	 */
	public function cache_spec_value($key = 'hid') {
		$func = 'get_no_'.$key;
	
		return $func('product_spec_value');
	}
	/**
	 * 生成SKU
	 * @param unknown $array
	 * @param unknown $key
	 * @return unknown[][]
	 */
	protected function _sku($array, $key) {
		static $_array;
		static $_key;
		static $_count;
		static $_temp;
		if($key < 0) {
			$_array = [];
			$_key = 0;
			$_count = count($array) - 1;
			$_temp = [];
			$this->_sku($array, 0);
		}else {
			foreach($array[$key] as $v) {
				if($key < $_count) {
					$_temp[$key] = $v;
					$this->_sku($array, $key+1);
				}elseif($key == $_count) {
					$_temp[$key] = $v;
					$_array[$_key] = $_temp;
					$_key++;
				}
			}
		}
		return $_array;
	}
	/**
	 * 格式化 SKU
	 * @param unknown $sku
	 */
	protected function _sku_string($sku) {
		$_string = [];
		foreach($sku as $v) {
			$_string[] = implode('_', $v);
		}
		return $_string;
	}
	/**
	 * 获取表单 SKU
	 */
	protected function _form_sku_string() {
		$_data = [];
		
		$list = I('post.spec/a');
		foreach($list as $v) {
			$_data[] = $v['sp_value'];
		}
		
		return $_data;
	}
	/**
	 * 商品规格
	 * @param unknown $_sku
	 */
	protected function field_sku($sku, $product_id) {
		$product_info = get_info($this->table, ['id'=> $product_id], 'id,market_price,price,inventory,image');
		if($sku && is_array($sku)) {
			$sku_list = array_column($this->_sku_list($product_id), 'id');
			$market_price = [];
			$price = [];
			$inventory = 0;
			$cache = $this->cache_spec_value();
			foreach($sku as $v) {
				$_data = [];
				$_rules = [];
				
				$key = 'i_'.$v;
				$data = $_POST['spec'][$key];
				$sku_id = Form::int('sku_id', 'data.', 0, $data);
				if($sku_id) {
					if(!in_array($sku_id, $sku_list)) return false;
					$_data['id'] = $sku_id;
					$_sku_ids[] = $sku_id;
				}
				$_data['product_id'] = $product_id;
				$market_price[] = $this->sku_field_market_price($_data, $_rules, $data);
				$price[] = $this->sku_field_price($_data, $_rules, $data);
				$inventory = $this->sku_field_inventory($_data, $_rules, $data, $inventory);
				$this->sku_field_spec_value($_data, $_rules, $data, $cache);
				$this->sku_field_image($_data, $_rules, $product_info['image']);
				$result = update_data($this->table_sku, $_rules, [], $_data);
				
				if(!is_numeric($result)) return $result;
			}
			/* 删除多余 SKU */
			$this->sku_default_delete($product_id);
			if($_sku_ids) $this->sku_delete($product_id, $_sku_ids);
			/* 更新商品数据 */
			return $this->_update_product($product_id, min($market_price), min($price), $inventory);
		}
		/* 删除多余 SKU */
		$this->sku_delete($product_id);
		/* 写入默认 SKU */
		return $this->sku_default($product_info);
	}
	/**
	 * 获取已存在规格
	 * @param unknown $product_id
	 */
	protected function _sku_list($product_id) {
		$_map = [
			'product_id'=> $product_id,
		];
		$list = get_result($this->table_sku, $_map, 'id asc', 'id');
		
		return $list;
	}
	/**
	 * SKU 价格
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function sku_field_price(&$_data, &$_rules, $data) {
		$_data['price'] = Form::float('price', 'data.', 0.00, $data);
		$_rules[] = Rules::currency('price', '请输入价格');
		
		return $_data['price'];
	}
	/**
	 * SKU 市场价
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function sku_field_market_price(&$_data, &$_rules, $data) {
		$_data['market_price'] = Form::float('market_price', 'data.', 0.00, $data);
		$_rules[] = Rules::currency('market_price', '请输入市场价');
		
		return $_data['market_price'];		
	}
	/**
	 * SKU 库存
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function sku_field_inventory(&$_data, &$_rules, $data, $inventory) {
		$_data['inventory'] = Form::int('stock', 'data.', 0, $data);
		$inventory += $_data['inventory'];
		
		return $inventory;
	}
	/**
	 * SKU 规格
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param unknown $data
	 * @param unknown $cache
	 */
	protected function sku_field_spec_value(&$_data, &$_rules, $data, $cache) {
		$_array = [];
		
		$spec_value = explode('_', Form::string('sp_value', 'data.', '', $data));
		if($spec_value) {
			foreach($spec_value as $v) {
				$_array[$v] = $cache[$v]['title'];
			}
			$_data['spec_value'] = serialize($_array);
		}else {
			$_data['spec_value'] = '';
		}
		$_rules[] = Rules::_require('spec_value', '规格数据错误');
	}
	/**
	 * 封面图
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param unknown $image
	 */
	protected function sku_field_image(&$_data, &$_rules, $image) {
		$_data['image'] = $image;
	}
	/**
	 * 默认 SKU
	 * @param unknown $product_info
	 */
	protected function sku_default($product_info) {
		$_data = [
			'product_id'=> $product_info['id'],
			'market_price'=> $product_info['market_price'],
			'price'=> $product_info['price'],
			'inventory'=> $product_info['inventory'],
			'spec_value'=> '',
			'image'=> $product_info['image'],
			'is_default'=> 1,
		];
		$_map = [
			'product_id'=> $product_info['id'],
			'is_default'=> 1,	
		];
		$info = get_info($this->table_sku, $_map, 'id');
		if($info) $_data['id'] = $info['id'];
		$result = update_data($this->table_sku, [], [], $_data);
		if(is_numeric($result)) return true;
		return false;
	}
	/**
	 * SKU 删除
	 */
	protected function sku_delete($product_id, $sku_id = []) {
		$_map = [
			'product_id'=> $product_id,
			'is_default'=> 0,
		];
		if($sku_id) $_map['id'] = ['NOT IN', $sku_id];
		
		return delete_data($this->table_sku, $_map);
	}
	/**
	 * SKU 删除 - 默认
	 */
	protected function sku_default_delete($product_id) {
		$_map = [
			'product_id'=> $product_id,
			'is_default'=> 1,
		];
	
		return delete_data($this->table_sku, $_map);
	}
	/**
	 * 更新商品数据
	 * @param unknown $product_id
	 * @param unknown $market_price
	 * @param unknown $price
	 * @param unknown $inventory
	 * @return boolean
	 */
	protected function _update_product($product_id, $market_price, $price, $inventory) {
		$_data = [
			'id'=> $product_id,
			'market_price'=> $market_price,
			'price'=> $price,
			'inventory'=> $inventory,
		];
		$result = update_data($this->table, [], [], $_data);
		if(is_numeric($result)) return true;
		return false;
	}
	/**
	 * 规格数据
	 * @param array $product_info
	 */
	public function spec($product_info = []) {
		$_data = [];
		
		$_data['spec'] = $this->cache_spec();
		$_data['spec_value'] = $this->format_spec_value();
		$_data['spec_exist'] = [];
		$_data['spec_value_exist'] = [];
		if($product_info) {
			$_data['spec_exist'] = $this->_spec_exist($product_info['spec']);
			$_data['spec_value_exist'] = $this->_spec_value_exist($product_info['spec_value']);
		}
		return $_data;
	}
	/**
	 * 规格值数据处理
	 * @return unknown
	 */
	protected function format_spec_value() {
		$_data = [];
		
		$cache = $this->cache_spec_value();
		foreach($cache as $v) {
			$_data[$v['spec_id']][] = $v;
		}
		
		return $_data;
	}
	/**
	 * 已存在的规格
	 * @param unknown $spec
	 */
	protected function _spec_exist($spec) {
		$spec_exist = unserialize($spec);
		if($spec_exist) return array_keys($spec_exist);
		
		return [];
	}
	/**
	 * 已存在的规格值
	 * @param unknown $spec_value
	 */
	protected function _spec_value_exist($spec_value) {
		$_data = [];
		
		$spec_value_exist = unserialize($spec_value);
		foreach($spec_value_exist as $k=> $v) {
			$_data[$k] = array_keys($v);
		}
		
		return $_data;
	}
	/**
	 * 规格值 - 商品图片
	 * @param unknown $product_info
	 */
	public function photo_spec_value($product_info) {
		$spec = unserialize($product_info['spec']);
		$spec_value = unserialize($product_info['spec_value']);
		if($spec && $spec_value) {
			$cache_spec = $this->cache_spec();
			$spec_id = $this->get_color_spec($spec, $cache_spec);
			if($spec_id && isset($spec_value[$spec_id])) {
				$_data['spec_title'] = $cache_spec[$spec_id]['title'];
				$_data['spec_value'] = $this->get_color_sku($spec_value[$spec_id], $product_info['id']);
				
				return $_data;
			}
		}
		return [];
	}
	/**
	 * 获取可上传图片规格
	 */
	protected function get_color_spec($spec, $cache) {
		foreach($spec as $k=> $v) {
			if(isset($cache[$k]) && $cache[$k]['is_color'] == 1) return $k;
		}
		return false;
	}
	/**
	 * 颜色 SKU
	 */
	protected function get_color_sku($spec_value, $product_id) {
		$_data = [];
		
		$list = get_result($this->table_sku, ['product_id'=> $product_id], 'id asc', 'id,image,spec_value');
		if($list) {
			$cache = $this->cache_spec_value();
			$spec_value_ids = array_keys($spec_value);
			foreach($spec_value_ids as $v) {
				$_data[$v]['title'] = isset($cache[$v]) ? $cache[$v]['title'] : $spec_value[$v]; 
				foreach($list as $_k=> $_v) {
					$_spec_value = unserialize($_v['spec_value']);
					if($_spec_value && isset($_spec_value[$v])) {
						if(!isset($_data[$v]['sku_id'])) $_data[$v]['sku_id'] = $_v['id'];
						$_data[$v]['image'] = $_v['image'];
						$_data[$v]['sku'][] = $_v['id'];
						unset($list[$_k]);
					}
				}
			}
		}
		
		return $_data;
	}
	/**
	 * 商品图片以及颜色封面图
	 */
	public function photo_color_update($product_info, $data) {
		try{
			$M = M();
			$M->startTrans();
			$table = C('DB_PREFIX').$this->table_sku;
			foreach($data['spec_value'] as $v) {
				if(Form::int('spec_value_'.$v['id'])) {
					$result = file_upload(Form::int('spec_value_'.$v['id']), 'Uploads/Product/Color', $this->table_sku, 'id', $v['sku_id']);
					if($result === true) {
						if($product_info['image'] != $v['image'] && file_exists($v['image'])) unlink($v['image']);
						$in = implode(',', $v['sku']);
						$sql = "update {$table} a,{$table} b set a.image=b.image where a.id in({$in}) and b.id={$v['sku_id']}";
						$result_sku = $M->execute($sql);
						if(!is_numeric($result_sku)) {
							$M->commit();
							return false;
						}
					}
				}
			}
			if(I('post.image/a')) {
				$result_image = file_upload_more(I('post.image/a'), 'Uploads/Product/Photo', $this->table_image, 'product_id', $product_info['id']);
				if($result_image !== true) {
					$M->commit();
					return false;
				}
			}
			$M->commit();
			return true;
		}catch(\Exception $e){
			$M->rollback();
			return false;
		}
	}
	public function get_sku_list($product_id) {
		$_data = [];
		
		$_map = [
			'product_id'=> $product_id,	
		];
		$list = get_result($this->table_sku, $_map, 'id asc', 'id,market_price,price,inventory,spec_value');
		foreach($list as $v) {
			$spec_value = unserialize($v['spec_value']);
			if($spec_value) {
				$_key = implode('_', array_keys($spec_value));
				$_data[$_key] = [
					'sku_id'=> $v['id'],
					'market_price'=> $v['market_price'],
					'price'=> $v['price'],
					'stock'=> $v['inventory'],
				];
			}
		}
		return $_data;
	}
	/**
	 * 图片删除
	 * @return boolean
	 */
	public function delete_image() {
		$product_id = Form::int('id');
		$id = Form::int('image_id');
		if($product_id && $id) {
			$_map = [
				'id'=> $id,
				'product_id'=> $product_id,
			];
			$info = get_info($this->table_image, $_map, 'image');
			if($info) {
				delete_data($this->table_image, $_map);
				if(file_exists($info['image'])) unlink($info['image']);
				return true;
			}
		}
		return false;
	}
}