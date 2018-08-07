<?php
/**
 * 小区管理
 */
namespace Backend\Controller\Property;

use Backend\Org\Property\District;

class DistrictController extends IndexController{
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'District';
	
	protected $obj;
	
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Backend\Controller\Base\AdminController::_init()
	 */
	public function _init() {
		parent::_init();
		$this->obj = new District();
	}
	
	public function index() {
		$_data = [];
		
		$_map = $this->obj->search();
		$_data = $this->page($this->table, $_map, 'add_time desc');
		$_data['cache'] = $this->obj->cache_area();
		
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
		$_data['cache'] = $this->obj->cache_area();
	
		$this->assign($_data);
		$this->display('operate');
	}
	/**
	 * 编辑
	 */
	public function edit() {
		$_data = [];
		
		$info = $this->_edit('小区信息不存在');
		
		if(IS_POST) {
			$this->update($info);
			return ;
		}
		
		$_data['info'] = $info;
		$_data['cache'] = $this->obj->cache_area();
		
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
			$this->success('小区信息更新成功', U('index'));
		}
		F('district_no_hid',null);
		F('district_no_del',null);
		$this->error($result);
	}
}