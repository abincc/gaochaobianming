<?php
/**
 * 商品规格
 */
namespace Backend\Controller\Product;

use Backend\Org\Product\Spec;

class SpecController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'product_spec';
	
	protected $obj;
	
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Backend\Controller\Base\AdminController::_init()
	 */
	public function _init() {
		parent::_init();
		$this->obj = new Spec();
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
		$this->display();
	}
	/**
	 * 编辑
	 */
	public function edit() {
		$_data = [];
		
		$info = $this->_edit('规格信息不存在');
		
		if(IS_POST) {
			$this->update($info);
			return ;
		}
		
		$_data['info'] = $info;
		
		$this->assign($_data);
		$this->display('edit');
	}
	/**
	 * 更新
	 */
	protected function update($info = []) {
		$result = $this->obj->update($info);
		if(is_numeric($result)) {
			$this->call_back_change($result);
			$this->success('规格信息更新成功', U('index'));
		}
		$this->error($result);
	}
	/**
	 * 清除缓存
	 * @param string 当前ID
	 * @time 2015-06-14
	 * @author 秦晓武
	 */
	public function clean_cache($id = ''){
		F($this->table . '_no_del', null);
		F($this->table . '_no_hid', null);
		F('product_spec_value' . '_no_del', null);
		F('product_spec_value' . '_no_hid', null);
	}
}