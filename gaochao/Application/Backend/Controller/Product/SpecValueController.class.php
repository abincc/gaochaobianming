<?php
/**
 * 商品规格值
 */
namespace Backend\Controller\Product;

use Backend\Org\Product\SpecValue;
use Backend\Org\Form\Form;

class SpecValueController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'product_spec_value';
	
	protected $obj;
	
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Backend\Controller\Base\AdminController::_init()
	 */
	public function _init() {
		parent::_init();
		$this->obj = new SpecValue();
	}
	
	public function index() {
		$_data = [];
		
		$_map = $this->obj->search();
		$_data = $this->page($this->table, $_map, 'sort desc,add_time desc');
		$_data['cache_spec'] = $this->obj->cache_spec('del');
		$_data['spec_list'] = $this->spec_select(Form::int('spec_id', 'get.'));
		
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
		$_data['spec_list'] = $this->spec_select();
	
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
		$_data['spec_list'] = $this->spec_select($info['spec_id']);
		
		$this->assign($_data);
		$this->display('edit');
	}
	/**
	 * 更新
	 */
	protected function update($info = []) {
		if($info) {
			$result = $this->obj->update($info);
		}else {
			$result = $this->obj->add();
		}
		if(is_numeric($result)) {
			$this->call_back_change();
			$this->success('规格信息更新成功', U('index'));
		}
		$this->error($result);
	}
	/**
	 * 规格
	 * @param unknown $spec_id
	 */
	protected function spec_select($spec_id = 0) {
		$cache = $this->obj->cache_spec();
		return array_to_select($cache, $spec_id, ['prefix'=> '', 'add_prefix'=> '']);
	}
}