<?php
namespace Backend\Controller\Company;
/**
 * 帮助中心
 * @author 秦晓武
 * @time 2016-06-01
 */
class CompanyController extends IndexController {
/**
 * 表名
 * @var string
 */
	protected $table = 'company';
	/**
	 * 列表函数
	 */
	public function index(){
		$map = array();		$result['list'] = get_result($this->table,$map,'id desc');
		$this->assign($result);
		$this->display();
	}
	/**
	 * 添加
	 * @author 秦晓武
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
	 * @author 秦晓武
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
	 * @author 秦晓武
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
	 * @author 秦晓武
	 * @time 2016-05-31
	 */
	protected function update(){
		// $data = I('post.');

		$data = array(
			'name'  => I('name','','trim'),
			'update_time' => date('Y-m-d H:i:s'),
			'add_time'    => date('Y-m-d H:i:s')
		); 

		if(!empty(I('id'))){
			unset($data['add_time']);
			$data['id'] = I('id');
		}

		/*验证参数*/
		$rules[] = array('name','require','公司名称必须填',1);
		$result = update_data($this->table, $rules, array(), $data);
		if(is_numeric($result)){
			$this->success('操作成功',U('index'));
		}else{
			$this->error($result);
		}
	}
}
