<?php
namespace Backend\Controller\Base;
use Common\Controller\CommonController;
use Backend\Org\Form\Form;
	/**
	 * 后台总父类
	 */
class AdminController extends CommonController {
/**
 * 表名
 * @var string
 */
	protected $table = '';
	
	/**
	 * 全局加载
	 * @time 2014-12-26
	 * @author 郭文龙 <2824682114@qq.com>
	 */
	protected function __autoload() {
		if (!session('member_id')) {
			header ( "location:" . __ROOT__ . "/Backend" );
		}
		/**获取菜单*/
		$menu_result = get_menu_list();
		$menu_arr = array_column($menu_result,'url');
		/**添加无权限控制的页面*/
		$menu_arr[] = MODULE_NAME . '/' . CONTROLLER_NAME . '/' . 'uploadPicture';
		$menu_arr[] = MODULE_NAME . '/' . CONTROLLER_NAME . '/' . 'uploadFile';
		$menu_arr[] = MODULE_NAME . '/' . CONTROLLER_NAME . '/' . 'delTempFile';
		$menu_arr[] = 'Backend/Base/Index/index';
		$menu_arr[] = 'Backend/Index/Index';
		$menu_arr[] = 'Backend/Demo/Index/index';
		$menu_arr[] = 'Backend/Base/Info/edit';
		
		$url = MODULE_NAME . '/' . CONTROLLER_NAME . '/' . ACTION_NAME;
		if (!in_array($url, $menu_arr) && !stristr($url, "ueditor" ) && ! stristr ( $url, "ajaxDelete_" )) {
			$this->error ( '未授权的访问' );
		}
		/*预定义模版变量，避免Noitce错误*/
		$data = array(
			'search' => '',
			'start_date' => '',
			'stop_date' => '',
			'page' => '',
		);
		if($this->table){
			foreach(M($this->table)->getDbFields() as $key){
				$data[$key] = ''; 
			}
		}
		$data['menu_result'] = array_to_tree($menu_result);
		$this->assign($data);
		$this->_init();
		if(!IS_POST && !IS_AJAX){
			action_log();
		}
	}
	/**
	 * 初始化，用于继承
	 */
	protected function _init() {
	}
	/**
	 * 分割线，操作处理函数
	 * @author 秦晓武
	 * @time 2016-06-14
	 */
	private function ________ACTION________(){}
	
	/**
	 * 处理排序
	 * @param array $sort 需要排序的字段数组
	 * @param string $order 默认排序
	 * @return string 处理后排序
	 * @author 秦晓武
	 * @time 2016-06-14
	 */
	function get_order($sort = array(),$order = 'id desc'){
		$data = array();
		/*遍历需要排序的字段数组*/
		foreach($sort as $field){
			$data[$field]['class'] = '';
			$by = 'asc';
			/*匹配URL参数处理对应URL及CLASS*/
			if(I('get.order') == $field){
				$by = I('get.by') == 'asc' ? 'desc' : 'asc';
				$data[$field]['class'] = I('get.by');
				$order = I('get.order') . ' ' . I('get.by');
			}
			$data[$field]['url'] = U('',array_merge(I('get.'), array('order'=>$field,'by'=>$by)));
		}
		$this->assign('sort',$data);
		return $order;
	}
	/**
	 * 启用
	 */
	function enable() {
		$this->change_field_value('is_hid',0);
	}
	/**
	 * 禁用
	 */
	function disable() {
		$this->change_field_value('is_hid',1);
	}
	/**
	 * 不推荐
	 */
	function no_recommend() {
		$this->change_field_value('recommend',0);
	}
	/**
	 * 推荐
	 */
	function recommend() {
		$this->change_field_value('recommend',1);
	}
	
	/**
	 * 删除
	 */
	function del() {
		$this->change_field_value('is_del',1);
	}

	/**
	 * 更新单个字段，可批量，主键ID，对应参数IDS
	 * @param string $field 字段名
	 * @param string $key_field 值
	 * @time 2015-06-14
	 * @author 秦晓武
	 */
	protected function change_field_value($field = '', $value = null){
		if(!$field){
			return '';
		}
		$ids = I('ids');
		if (empty($ids)){
			$this->error('请选择要操作的数据!');
		}
		$value = is_null($value) ? intval(I($field)) : $value;
		if (is_array($ids)) {
			$_POST = array($field => $value);
			$map[M($this->table)->getPk()] = array('in',$ids );
			$result = update_data($this->table, array(), $map);
		}else {
			$ids = intval($ids);
			$_POST = array(M($this->table)->getPk() => $ids, $field => $value);
			$result = update_data($this->table);
			if(!is_numeric($result)){
				$this->error($result);
			}
		}
		if(isset($_GET['ids'])){
			unset($_GET['ids']);
		}
		$this->success('操作成功', U('index', I('get.')));
	}
	
	/**
	 * 更新后的回调函数
	 * @param string 当前ID
	 * @return string 更新结果
	 * @time 2015-06-14
	 * @author 秦晓武
	 */
	public function call_back_change($id = ''){
		/*写入日志*/
		action_log($id);
		/*清除缓存*/
		$this->clean_cache($id);
		return true;
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
	}
	
	/**
	 * 通用格式转换
	 * @time 2016-9-18
	 * @author 秦晓武
	 */
	public function format_value(&$row){
		if(isset($row['is_hid'])){
			$row['is_hid'] = $row['is_hid'] ? '隐藏' : '显示';
		}
		if(isset($row['recommend'])){
			$row['recommend'] = $row['recommend'] ? '是' : '否';
		}
	}
	/**
	 * 删除图片
	 */
	protected function ajax_delete_image($fields = []) {
		$id = Form::int('id');
		$field = Form::string('name');
		if($id && in_array($field, $fields)) {
			$_map = [
				'id'=> $id,	
			];
			$info = get_info($this->table, $_map, 'id,'.$field);
			if($info) {
				$_data = [
					'id'=> $info['id'],
					$field=> '',
				];
				update_data($this->table, [], [], $_data);
				if(file_exists($info[$field])) unlink($info[$field]);
			}
			$this->success('删除成功');
		}
		$this->error('删除失败');
	}
}