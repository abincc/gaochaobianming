<?php
/**
 * 商品品牌
 */
namespace Backend\Controller\Product;

use Backend\Org\Product\Brand;

class BrandController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'product_brand';
	
	protected $obj;
	
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Backend\Controller\Base\AdminController::_init()
	 */
	public function _init() {
		parent::_init();
		$this->obj = new Brand();
	}
	
	public function index() {
		$_data = [];
		
		$_map = $this->obj->search();
		$_data = $this->page($this->table, $_map, 'sort desc,add_time desc');
		
		$this->assign($_data);
		$this->display();
	}
	/**
	 * 添加
	 */
	public function add() {
		$_data = [];
	
		if(IS_POST) {
			$this->update();
			return ;
		}
	
		$_data['info'] = get_info($this->table, []);
		$_data['info']['sort'] = 0;
	
		$this->assign($_data);
		$this->display('operate');
	}
	/**
	 * 编辑
	 */
	public function edit() {
		$_data = [];
		
		$info = $this->_edit('品牌信息不存在');
		
		if(IS_POST) {
			$this->update($info);
			return ;
		}
		
		$_data['info'] = $info;
		
		$this->assign($_data);
		$this->display('operate');
	}
	/**
	 * 更新
	 */
	protected function update($info = []) {
		$result = $this->obj->update($info);
		if(is_numeric($result)) {
			$this->call_back_change($result);
			$this->success('品牌信息更新成功', U('index'));
		}
		$this->error($result);
	}
	/**
	 * 删除图片
	 */
	public function ajaxDelete_product_brand() {
		$this->ajax_delete_image(['image']);
	}
}