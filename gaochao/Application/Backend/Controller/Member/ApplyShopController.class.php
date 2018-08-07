<?php
namespace Backend\Controller\Member;
/**
 * 开店申请
 * @author llf
 * @time 2017-09-12
 */
class ApplyShopController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Apply_shop';	

	/**
	 * 列表函数
	 */
	public function index(){

		$map = $this->_search();
		
		$result = $this->page($this->table,$map,'add_time desc');

		$result['district'] = array_to_select(get_no_hid('district','sort desc'), I('district_id'));

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
			$map['mobile|username|email|shop_name']	= array('LIKE',"%$keyword%");
		}
		if(!empty($district_id)){
			$map['district_id']		= $district_id;
		}
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

		$info = get_info($this->table, array('id'=>I('ids')));

		$data['info'] = $info;
		$this->assign($data);
		$this->display('operate');
	}
	
}

