<?php
namespace Backend\Controller\Property;
/**
 * 车位管理管理
 * @author llf
 * @time 2016-06-30
 */
class ParkingController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Parking';
	protected $table_view 	= 'ParkingView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();
		// bug(D($this->table_view));
		$result = $this->page(D($this->table_view),$map,'add_time
			 desc');

		$result['service'] = get_no_hid('trifle_service','sort desc,id asc');
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

		if(!empty($keyword)){
			$map['p.title']   = array('LIKE',"%$keyword%");
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
		$data['info'] 	= $info;
		$district_list	= M('district')->where(array('is_hid'=>0,'is_hid'=>0))->field('id,title')->select();
		$data['pid']  	= array_to_select($district_list, $info['district_id']);

		if(session('is_owner') && !empty(session('district_ids'))){
			$list = get_no_hid('district','add_time desc');
			$district_ids = session('district_ids');
			array_walk($list, function($a) use($district_ids,&$html){
				if(in_array($a['id'], $district_ids)){
					$html .= "<option value='{$a['id']}'>{$a['title']}</option>";
				}
			});
			$data['pid'] = $html;
		}		

		$this->assign($data);
		$this->display('operate');
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
	 * 修改
	 * @author llf
	 * @time 2016-05-31
	 */
	protected function update(){

		$post = I('post.');

		/*基本信息*/
		$data = array(
				'title' 		=> I('title','','trim'),
				'district_id'	=> I('district_id',0,'int'),
				'add_time'		=> date('Y-m-d H:i:s'),
				'update_time'	=> date('Y-m-d H:i:s'),
				);
		if($post['ids']){
			$has = M($this->table)->where('id='.$post['ids'])->find();
			if(empty($has)){
				$this->error('车位信息不存在');
			}
			$data['id'] = intval($post['ids']);
			unset($data['add_time']);
		}else{
			$data['admin_id']	= session('member_id');
		}
		$rules[] = array('district_id','require','请选择小区',3);
		$rules[] = array('title','require','小事类型名称必须',3);
		$rules[] = array('title',1,20,'标题长度范围在1~20字符',3,'length');

		try {
			$M = M();
			$M->startTrans();

			$result = update_data($this->table, [], [], $data);
			if(!is_numeric($result)){
				throw new \Exception($result);
			}
			if(empty(I('image')) && !$data['id']){
				throw new \Exception('请上传车位图片');
			}
			if(!empty($has) && empty($has['image']) && empty(I('image'))){
				throw new \Exception('请上传车位图片');
			}
			multi_file_upload(I('image'),'Uploads/Parking','parking','id',$result,'image');

		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}
		$M->commit();
		$this->success('操作成功',U('index'));
	}

	/**
	 * 删除图片
	 */
	public function ajaxDelete_parking() {
		$this->ajax_delete_image(['image']);
	}

}

