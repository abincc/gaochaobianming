<?php
namespace Backend\Controller\Property;
/**
 * 议事事件管理
 * @author llf
 * @time 2016-06-30
 */
class ProcedureController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Procedure';
	protected $table_view 	= 'ProcedureView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page(D($this->table_view),$map,'add_time desc');
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
		$status 		   = I('status',0,'int');

		if(!empty($keyword)){
			$map['title']   = array('LIKE',"%$keyword%");
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

		$info = get_info(D($this->table_view), array('id'=>I('ids')));
		$info['building_no'] = unserialize($info['building_no']);
		$info['room_no'] = unserialize($info['room_no']);
		if(!empty($info)){
			$info['images'] = D($this->table)->get_image($info['id']);
		}
		$district_list	= M('district')->where(array('is_hid'=>0,'is_hid'=>0))->field('id,title')->select();
		$data['district']  	= array_to_select($district_list, $info['district_id']);
		
		if(session('is_owner') && !empty(session('district_ids'))){
			$list = get_no_hid('district','add_time desc');
			$district_ids = session('district_ids');
			array_walk($list, function($a) use($district_ids,&$html){
				if(in_array($a['id'], $district_ids)){
					$html .= "<option value='{$a['id']}'>{$a['title']}</option>";
				}
			});
			$data['district'] = $html;
		}	

		// echo $info['building_no'].":--:".$info['room_no'];

		$data['info'] = $info;
		$this->assign($data);
		$this->display('operate');
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
	 * 详情页
	 * @author llf
	 * @time 2018-01-30
	 */	
	public function info(){

		$procedure_id = I('ids',0,'int');
		$post = I();

		$Procedure = D($this->table);
		$info = $Procedure->where(['id'=>$procedure_id])->find();
		if(empty($info)){
			$this->error('议事信息不存在');
		}
		$info	 = $info;
		$images	 = $Procedure->get_image($info['id']);

		//获取投票列表信息
		$data	= $this->page(D('VoteView'),$this->_search_info(),'add_time desc','',6);

		$data['info']	= $info;
		$data['images']	= $images; 
		$data['type']	= get_table_state('Procedure_vote','type');

		$this->assign($data);
		$this->display();
	}

	/**
	 * 投票明细搜索
	 */
	private function _search_info(){

		$map		= array('procedure_id'=>I('ids',0,'int'));
		$type		= I('type',0,'int');
		$keywords 	= I('keywords','','trim');
		if($type){
			$map['type'] = $type;
		}
		if($type){
			$map['type'] = $type;
		}
		if(!empty($keywords)){
			$map['username|mobile'] = array('LIKE',"%$keywords%");
		}
		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		return $map;
	}

	/**
	 * 导出
	 * @author llf
	 * @time 2018-01-30
	 */
	public function export(){
		$map 	= $this->_search_info();
		$result = get_result(D('VoteView'), $map,'add_time desc');
		if(!empty($result)){
			$type = get_table_state('Procedure_vote','type');
			array_walk($result, function(&$a) use($type){
				$a['type_title'] = $type[$a['type']]['title'];
			});
		}else{
			$this->error('无数据可导出');
		}
		/*填充数据*/
		$data['result']    = $result;
		/*填充表名*/
		$data['sheetName'] = 'info_export_'.date('Ymd_His');

		$export_config = array(
			array('title' => '投票用户', 'field' => 'username'),
			array('title' => '用户手机号', 'field' => 'mobile'),
			array('title' => '意见', 'field' => 'type_title'),
			array('title' => '投票时间', 'field' => 'add_time'),
		);
		A('Common/Excel','',1)->export_data($export_config,$data);
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
				'description'	=> I('description','','trim'),
				'end_date'		=> I('end_date','','trim'),
				'sort'			=> I('sort',0,'int'),
				);
		$data['building_no'] = serialize($post['buildings']);
		$data['room_no'] = serialize($post['rooms']);
		$Procedure = D($this->table);
		if($post['ids']){
			$has = $Procedure->where('id='.$post['ids'])->find();
			if(empty($has)){
				$this->error('议事信息不存在');
			}
			$images = $Procedure->get_image($has['id']);
			$data['id'] = intval($post['ids']);
			unset($data['add_time']);
			unset($data['district_id']);
		}else{
			$data['admin_id']	= session('member_id');
			if(empty($data['district_id'])){
				$data['district_id'] = I('district_id',0,'int');
			}
		}
		try {
			$M = M();
			$M->startTrans();

			$result = update_data($Procedure, [], [], $data);
			if(!is_numeric($result)){
				throw new \Exception($Procedure->getError());
			}
			if(empty(I('image')) && !$data['id']){
				throw new \Exception('请上传图片信息');
			}
			if(!empty($has) && empty($images) && empty(I('image'))){
				throw new \Exception('请上传图片信息');
			}
			multi_file_upload(I('image'),'Uploads/Procedure','procedure_image','pid',$result,'image');

		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}
		$M->commit();
		$this->success('操作成功',U('index'));
	}


	/**
	 * 通知未参与的业主
	 */
	public function notice(){

		$count = 0;
		
		$procedure_id = I('ids',0,'int');

		$Procedure = D($this->table);
		$has = $Procedure->where('id='.$procedure_id)->find();
		if(empty($has)){
			$this->error('议事信息不存在');
		}
		$member_votes = M('Procedure_vote')->where(['procedure_id'=>$procedure_id])->getField('member_id',true);
		$district_id  = $has['district_id'];

		$buildings = split(',',unserialize($has['building_no']),-1); 
		$rooms = split(',',unserialize($has['room_no']),-1);
		
		if(count($buildings) > 0 && $buildings[0] != null){
				if(count($rooms) > 0 && $rooms[0] != null){
					foreach ($buildings as $build) {
						foreach ($rooms as $key => $room) {
							 $condition = array(
							'district_id'	=> $district_id,
							'building_no'   => $build,
							'room_no'       => $room,
							'is_del'		=> 0,
							'is_hid'		=> 0,
							'state'			=> 2,
							'type'			=> 2,
								);
							if(!empty($member_votes)){
								$condition['id'] = array('not in',$member_votes);
							}
							$district_member_ids = M('Member')->where($condition)->getField('id',true);
							if(empty($district_member_ids)){
								continue;
							}
							$Message = D('Message');
							$title 	 = '小区议事通知';
							$content = '小区发起名为:“'.$has['title'].'” 的议事通知，您可去议事堂参与投票！';
							foreach ($district_member_ids as $k => $v) {
								$Message->send($v,1,$title,$content);
								$count = $count + 1;
							} 
						}  
					} 
					if($count <=0){
						$this->error('暂无可通知人员'); 
					}else{
						$this->error('通知成功');
					}
				}else{
					foreach ($buildings as $build) {
						 $condition = array(
						'district_id'	=> $district_id,
						'building_no'   => $build,
						'is_del'		=> 0,
						'is_hid'		=> 0,
						'state'			=> 2,
						'type'			=> 2,
						);
						if(!empty($member_votes)){
							$condition['id'] = array('not in',$member_votes);
						}
						$district_member_ids = M('Member')->where($condition)->getField('id',true);
						if(empty($district_member_ids)){
							continue;
						}
						$Message = D('Message');
						$title 	 = '小区议事通知';
						$content = '小区发起名为:“'.$has['title'].'” 的议事通知，您可去议事堂参与投票！';
						foreach ($district_member_ids as $k => $v) {
							$Message->send($v,1,$title,$content);
							$count = $count + 1;
						} 
					}
					if($count <=0){
						$this->error('暂无可通知人员'); 
					}else{
						$this->error('通知成功');
					}
				}
		}else{
			$condition = array(
				'district_id'	=> $district_id,
				'is_del'		=> 0,
				'is_hid'		=> 0,
				'state'			=> 2,
				'type'			=> 2,
			);
			if(!empty($member_votes)){
				$condition['id'] = array('not in',$member_votes);
			}
			$district_member_ids = M('Member')->where($condition)->getField('id',true);
			if(empty($district_member_ids)){
				$this->error('暂无可通知人员');
			}
			$Message = D('Message');
			$title 	 = '小区议事通知';
			$content = '小区发起名为:“'.$has['title'].'” 的议事通知，您可去议事堂参与投票！';
			foreach ($district_member_ids as $k => $v) {
				$Message->send($v,1,$title,$content);
			}
			$this->error('通知成功');
		}
	}



	/**
	 * 删除图片
	 */
	public function ajaxDelete_procedure_image() {		
		if(IS_POST) {
			$image_id	= I('image_id',0,'int');
			$pid		= I('id',0,'int');
			$has		= M('procedure_image')->where(['id'=>$image_id,'pid'=>$pid])->find();
			@unlink($has['image']);
			$result = M('procedure_image')->where(['id'=>$image_id,'pid'=>$pid])->delete();
			if($result == true) $this->success('删除成功');
			$this->error('删除失败');
		}
	}
}

