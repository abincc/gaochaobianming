<?php
/**
 * 商品管理
 */
namespace Backend\Controller\Product;

use Backend\Org\Product\Product;

class ProductController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'product';
	protected $obj;
	
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Backend\Controller\Base\AdminController::_init()
	 */
	public function _init() {
		parent::_init();
		$this->obj = new Product();
	}
	/**
	 * 列表
	 */
	public function index() {
		$_data = [];
		$_map = $this->obj->search();
		
		$_data = $this->page($this->table, $_map, 'add_time desc');
		if($_data['list']) $_data['list'] = $this->handle_list($_data['list']);
		$_data['category_list'] = $this->obj->cache_category();
		$_data['recommend'] = [
			1=> ['id'=> 1, 'title'=> '是'],
			0=> ['id'=> 0, 'title'=> '否'],
		];
		$_data['is_sale'] = [
			1=> ['id'=> 1, 'title'=> '上架'],
			0=> ['id'=> 0, 'title'=> '下架'],
		];
		
		$this->assign($_data);
		$this->display();
	}
	/**
	 * 处理列表数据
	 * @param unknown $list
	 */
	protected function handle_list($list) {
		$_data = [];
		
		$cache_category = $this->obj->cache_category();
		$cache_brand = $this->obj->cache_brand();
		foreach($list as $v) {
			$v['category_html'] = array_to_crumbs($cache_category, $v['sub_category_id']);
			$v['brand_html'] = isset($cache_brand[$v['brand_id']]) ? $cache_brand[$v['brand_id']]['title'] : '';
			$_data[] = $v;
		}
		
		return $_data;
	}
	/**
	 * 添加
	 */
	public function add() {
		if(IS_POST) {
			$this->update();
			return ;
		}
		$_data['info'] = get_info($this->table, []);
		$_data['info']['sort'] = 0;
		$_data['info']['is_sale'] = 0;
		$_data['info']['recommend'] = 0;
		$_data['category_list'] = $this->_category_list(0);
		$_data['brand_list'] = $this->_brand_list(0);
		
		$_data['spec_html'] = $this->_spec();
		$_data['submit'] = '下一步，上传商品图片';
		
		$this->assign($_data);
		$this->display('operate');
	}
	/**
	 * 编辑
	 */
	public function edit() {
		$info = $this->_edit('商品信息不存在');
		if(IS_POST) {
			$this->update($info);
			return ;
		}
		$_data['info'] = $info;
		$_data['category_list'] = $this->_category_list($info['sub_category_id']);
		$_data['brand_list'] = $this->_brand_list($info['brand_id']);
		
		$_data['spec_html'] = $this->_spec($info);
		$_data['submit'] = '提交';
		
		$this->assign($_data);
		$this->display('operate');
	}
	/**
	 * 规格
	 */
	protected function _spec($product_info = []) {
		$_data = $this->obj->spec($product_info);
		$_data['sku_list'] = $this->obj->get_sku_list($product_info['id']);
		$this->assign($_data);
		return $this->fetch('spec');
	}
	/**
	 * 分类
	 * @param unknown $category_id
	 * @return string
	 */
	protected function _category_list($category_id) {
		return array_to_select($this->obj->cache_category(), $category_id);
	}
	/**
	 * 品牌
	 * @param unknown $brand_id
	 * @return string
	 */
	protected function _brand_list($brand_id) {
		return array_to_select($this->obj->cache_brand(), $brand_id, ['prefix'=> '', 'add_prefix'=> '']);
	}
	/**
	 * 更新
	 * @param unknown $info
	 */
	protected function update($info = []) {
		$result = $this->obj->update($info);
		if(is_numeric($result)) {
			if($info) $this->success('商品信息更新成功', U('index'));
			
			$this->success('商品信息更新成功', U('photo', ['ids'=> $result]));
		}
		
		$this->error($result);
	}
	/**
	 * 商品图片
	 */
	public function photo() {
		$_data = [];
		
		$info = $this->_edit('商品信息不存在');
		$_data = $this->obj->photo_spec_value($info);
		if(IS_POST) {
			$result = $this->obj->photo_color_update($info, $_data);
			if($result === true) $this->success('商品图片信息更新成功', U('index'));
			
			$this->error('商品图片信息更新失败');
		}
		
		$_data['info'] = $info;
		
		$this->assign($_data);
		$this->display();
	}
	/**
	 * 图片删除
	 */
	public function ajaxDelete_product_image() {
		if(IS_POST) {
			$result = $this->obj->delete_image();
			if($result === true) $this->success('删除成功');
			
			$this->error('删除失败');
		}
	}
	/**
	 * 上架
	 */
	public function sale_on($field = 'is_sale') {
		$this->change_field_value($field, 1);
	}
	/**
	 * 下架
	 */
	public function sale_off($field = 'is_sale') {
		$this->change_field_value($field, 0);
	}
	/**
	 * 推荐 - 首页
	 * @param string $field
	 */
	public function recommend($field = 'recommend') {
		$this->change_field_value($field, 1);
	}
	/**
	 * 取消推荐 - 首页
	 * @param string $field
	 */
	public function recommend_cancel($field = 'recommend') {
		$this->change_field_value($field, 0);
	}
}