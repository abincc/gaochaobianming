<?php
namespace Backend\Controller\Property;
/**
 * 后台小事管理
 * @author llf
 * @time 2016-06-30
 */
class TrifleController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Trifle';
	protected $table_view 	= 'TrifleView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page(D($this->table_view),$map,'add_time desc');

		$result['service'] = get_no_del('trifle_service','sort desc,id asc');
		$result['status']  = get_table_state($this->table,'status');

		$result['district'] = array_to_select(get_no_hid('district','add_time desc'), I('district_id'));


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
		$trifle_service_id = I('trifle_service_id','','int');
		$status 		   = I('status',0,'int');

		if(!empty($keyword)){
			$map['mobile|username']   = array('LIKE',"%$keyword%");
		}
		if(!empty($trifle_service_id)){
			$map['trifle_service_id'] = $trifle_service_id;
		}
		/**小区*/
		if(strlen(I('district_id'))){
			$map['district_id'] = I('district_id',0,'int');
		}
		if(session('is_owner')){
			$map['district_id'] = array('IN',session('district_ids'));
		}
		if($status){
			$map['status'] = $status;
		}

		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		return $map;
	}

	/**
	 * 详情
	 * @author llf
	 * @time 2016-05-31
	 */
	public function detail(){

		$this->operate();
	}

	/**
	 * 显示
	 * @author llf
	 * @time 2016-05-31
	 */
	protected function operate(){

		$info = get_info($this->table, array('id'=>I('ids')));

		if(!empty($info)){
			$info['member_info'] = M('member')->where('id='.$info['member_id'])->find();
			if($info['remember_id']){
				$info['remember_info']	 = M('member')->where('id='.$info['remember_id'])->find();
			}
			$info['district_info'] = M('district')->where('id='.$info['district_id'])->find();

			$info['images'] = D($this->table)->get_image($info['id']);

			$info['reply_image'] = D($this->table)->get_reply_image($info['id']);
		}

		$data['status']  = get_table_state($this->table,'status');
		$data['service'] = get_no_hid('trifle_service','sort desc,id asc');
		$data['info'] = $info;
		$this->assign($data);
		$this->display('operate');
	}
	
}

