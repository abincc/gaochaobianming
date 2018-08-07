<?php
/**
 * 商品分类
 */
namespace Backend\Controller\Product;

use Backend\Org\Product\Category;

class CategoryController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'product_category';
	
	protected $obj;
	
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Backend\Controller\Base\AdminController::_init()
	 */
	public function _init() {
		parent::_init();
		$this->obj = new Category();
	}
	
	public function index() {
		$_data = $this->obj->index();
		
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
		$_data['info']['pid'] = array_to_select(get_no_del($this->table), 0);
	
		$this->assign($_data);
		$this->display('operate');
	}
	/**
	 * 编辑
	 */
	public function edit() {
		$_data = [];
		
		$info = $this->_edit('分类信息不存在');
		
		if(IS_POST) {
			$this->update($info);
			return ;
		}
		
		$_data['info'] = $info;
		$_data['info']['pid'] = array_to_select(get_no_del($this->table), $info['pid']);
		
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
			$this->success('分类信息更新成功', U('index'));
		}
		$this->error($result);
	}
	/**
	 * 删除图片
	 */
	public function ajaxDelete_product_category() {
		$this->ajax_delete_image(['icon']);
	}
}