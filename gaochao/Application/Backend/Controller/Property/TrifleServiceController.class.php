<?php
namespace Backend\Controller\Property;
/**
 * 小事服务类型管理
 * @author llf
 * @time 2016-06-30
 */
class TrifleServiceController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table  = 'Trifle_service';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page($this->table,$map);
		$this->assign($result);
		$this->display();
	}

	/**
	 * 列表 搜索
	 */
	private function _search(){

		$post 			   = I();
		$map 			   = array('is_del'=>0);
		$keyword 		   = I('keywords','','trim');
		$is_hid 		   = I('is_hid','','int');

		if(!empty($keyword)){
			$map['title']   = array('LIKE',"%$keyword%");
		}
		if(!empty($is_hid)){
			$map['is_hid']	= $is_hid;
		}

		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		return $map;
	}

	/**
	 * 添加
	 * @author llf
	 * @time 2016-05-31
	 */
	public function add(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}
	/**
	 * 编辑
	 * @author llf
	 * @time 2016-05-31
	 */
	public function edit(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}

	
	/**
	 * 显示
	 * @author llf
	 * @time 2016-05-31
	 */
	protected function operate(){
		$info = get_info($this->table, array('id'=>I('ids')));
		$data['info'] = $info;
		$this->assign($data);
		$this->display('operate');
	}
	
	/**
	 * 修改
	 * @author llf
	 * @time 2016-05-31
	 */
	protected function update(){

		$post = I('post.');

		/*基本信息*/
		$data = array(
				'title' 	=> I('title','','trim'),
				'add_time' 	=> date('Y-m-d H:i:s'),
				);
		if($post['id']){
			$data['id'] = intval($post['id']);
			unset($data['add_time']);
		}
		$rules[] = array('title','require','小事类型名称必须',1);
		$rules[] = array('title',1,20,'标题长度范围在1~20字符',3,'length');

		/*基本信息*/
		$result = update_data($this->table, [], [], $data);
		if(!is_numeric($result)){
			$this->success('操作失败',U('index'));
		}
		F('trifle_service_no_hid',null);
		F('trifle_service_no_del',null);
		$this->success('操作成功',U('index'));
	}

	public function del(){
		F('trifle_service_no_hid',null);
		F('trifle_service_no_del',null);
		parent::del();
	}
}

