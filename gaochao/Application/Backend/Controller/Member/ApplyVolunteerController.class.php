<?php
namespace Backend\Controller\Member;
/**
 * 志愿者申请
 * @author llf
 * @time 2017-09-12
 */
class ApplyVolunteerController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table		= 'Apply_volunteer';
	protected $table_view	= 'VolunteerView';	

	/**
	 * 列表
	 */
	public function index(){

		$map = $this->_search();
		
		$result = $this->page(D($this->table_view),$map,'add_time desc');

		// $result['district'] = array_to_select(get_no_hid('district','sort desc'), I('district_id'));

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
		$district_id	   = I('district_id','','int');

		if(!empty($keyword)){
			$map['mobile|username']	= array('LIKE',"%$keyword%");
		}
		// if(!empty($district_id)){
		// 	$map['district_id']		= $district_id;
		// }
		// if(session('is_owner')){
		// 	$map['district_id'] = intval(session('district_id'));
		// }

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
	 * @time 2017-09-12
	 */
	protected function operate(){

		$info = get_info(D($this->table_view), array('id'=>I('ids')));

		$data['info'] = $info;
		$this->assign($data);
		$this->display('operate');
	}
	
}

