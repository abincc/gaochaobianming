<?php
/**
 * 版块管理
 */
namespace Backend\Controller\Forum;

use Backend\Org\Forum\Forum;

class ForumController extends IndexController{
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'forum_forum';
	
	protected $obj;
	
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Backend\Controller\Base\AdminController::_init()
	 */
	public function _init() {
		parent::_init();
		$this->obj = new Forum();
	}
	
	public function index() {
		$_data = [];
		
		$_data = $this->obj->_list();
		
		$this->assign($_data);
		$this->display();
	}
	/**
	 * 添加
	 */
	public function add() {
		$_data = [];
	
		if(IS_POST) {
			$this->partition();
			return ;
		}
	
		$_data['info'] = get_info($this->table, []);
		$_data['info']['sort'] = 0;
	
		$this->assign($_data);
		$this->display();
	}
	/**
	 * 添加版块
	 */
	public function add_forum() {
		$_data = [];
		
		if(IS_POST) {
			$this->_forum();
			return ;
		}
		
		$_data['info'] = get_info($this->table, []);
		$_data['info']['sort'] = 0;
		$_data['partition_list'] = array_to_select($this->obj->cache_partition(), 0, ['prefix'=> '', 'add_prefix'=> '']);
		
		$this->assign($_data);
		$this->display();
	}
	/**
	 * 编辑
	 */
	public function edit() {
		$_data = [];
		
		$info = $this->_edit('版块信息不存在');
		
		if(IS_POST) {
			if($info['pid'] > 0) {
				$this->_forum($info);
			}else {
				$this->partition($info);
			}
			return ;
		}
		
		$_data['info'] = $info;
		$tpl = 'add';
		if($info['pid'] > 0) {
			$tpl = 'add_forum';
			$_data['partition_list'] = array_to_select($this->obj->cache_partition(), $info['pid'], ['prefix'=> '', 'add_prefix'=> '']);
		}
		
		$this->assign($_data);
		$this->display($tpl);
	}
	/**
	 * 添加分区
	 */
	protected function partition($info = []) {
		$result = $this->obj->partition($info);
		if(is_numeric($result)) {
			$this->call_back_change($result);
			$this->success('版块信息更新成功', U('index'));
		}
		$this->error($result);
	}
	/**
	 * 添加版块
	 */
	protected function _forum($info = []) {
		$result = $this->obj->_forum($info);
		if(is_numeric($result)) {
			$this->call_back_change($result);
			$this->success('版块信息更新成功', U('index'));
		}
		$this->error($result);
	}
	/**
	 * 删除图片
	 */
	public function ajaxDelete_forum_forum() {
		$this->ajax_delete_image(['icon']);
	}
	/**
	 * 更新回调
	 */
	public function clean_cache() {
		F($this->table.'_no_del', null);
		F($this->table.'_no_hid', null);
		F($this->table.'_recommend_no_hid', null);
		F($this->table.'_partition_no_hid', null);
	}
}