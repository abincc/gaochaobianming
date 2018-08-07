<?php
namespace Api\Controller\User;
use Api\Controller\Base\CkBaseController;
use Api\Controller\Base\HomeController;

class CkClockController extends CkBaseController{
	

	protected $table = 'CkClock';
	protected $table_image	='sr_reimburse_image';
	protected $table_buy_image	='sr_ck_buy_image';
	/**
	 * 判定今天打卡状态
	 * @author abincc
	 * @time 2018-07-05
	 */
	public function  checkClock(){
		if(!IS_POST)  $this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));

		// 校验打卡时间区域

		$check_time = substr(date('Y-m-d'), 0, 10);

		$map = array(
			'is_del'   => 0,
			'is_hid'   => 0,
			'member_id'=>$this->member_id,
			'add_time' => array('LIKE', "$check_time%")
		);

		$ckMember = M('ck_member')->where(array('is_del'=>0, 'is_hid'=>0, 'id'=>$this->member_id))->field('id, district_id,company_id')->find();

		if(empty($ckMember)){
			$this->apiReturn(array('status'=>'1', 'msg'=>'该用户不存在', 'info'=>array('state'=> -1)));
		}

		$count = M($this->table)->where($map)->count();
		if($count >= 2){
			$this->apiReturn(array('status'=>'1', 'msg'=>'今日已完成打卡', 'info'=>array('state'=> 2)));
		}

		$map['type'] =2;
		$countx = M($this->table)->where($map)->count();
		if($countx > 0){
			$this->apiReturn(array('status'=>'1', 'msg'=>'已打下班卡', 'info'=>array('state'=> 2)));
		}

		$re = array(
			'district_id' => $ckMember['district_id'],
			'is_del'      => 0
		);
		$ckModelCount = M('ck_model')->where($re)->count();
		if($ckModelCount != 2){
			$this->apiReturn(array('status'=>'1', 'msg'=>'管理员未设置打卡模式或时间区域', 'info'=>array('state'=> 3)));
		}

		$info['ck_model'] = M('ck_model')->where($re)->select();

		// 获取打卡经纬度信息
		$where = array(
			'is_del' => 0,
			'is_hid' => 0,
			'district_id' =>  $ckMember['district_id'],
			'company_id'  =>  $ckMember['company_id']
		);

		$rangeCount = M('CkRange')->where($where)->count();
		if($rangeCount == 0){
			$this->apiReturn(array('status'=>'0', 'msg'=>'小区管理员未设置管理员打卡方式', 'info'=>array('state'=> 4)));
		}

		$info['ck_range'] = M('CkRange')->where($where)->select();

		$info['ck_clock'] = $count;

		$info['date'] = date('Y-m-d H:i:s');

		$info['state'] = 1;

		$this->apiReturn(array('status'=>'1', 'msg'=>'请打卡', 'info' => $info));
	}


	/**
	 * 两个时间比对
	 * @author abincc
	 * @time 2018-07-05
	 */
	public function compareTime($hour1, $min1, $hour2, $min2){
		if(intval($hour1) > intval($hour2)){
			return true;
		}else if(intval($hour1) > intval($hour2)){
			if(intval($min1) > intval($min2)){
				return true;
			}else{
				return false; 
			}
		}else{
			return false;
		}
	}


	/**
	 * 今日打卡
	 * @author abincc
	 * @time 2018-07-05
	 */
	public function clock(){
		if(!IS_POST)  $this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));

		//获取当前小时
		$hour= date("G");
		$min = date('i');

		//获取用户对应的小区
		$ckMember = M('ck_member')->where(array('is_del'=>0, 'is_hid'=>0, 'id'=>$this->member_id))->field('id, district_id,company_id')->find();
		if(empty($ckMember)){
			$this->apiReturn(array('status'=>'0', 'msg'=>'该用户不存在'));
		}

		$re = array(
			'district_id' => $ckMember['district_id'],
			'is_del'      => 0
		);
		$ckModelCount = M('ck_model')->where($re)->count();
		if($ckModelCount != 2){
			$this->apiReturn(array('status'=>'0', 'msg'=>'管理员未设置打卡模式或时间区域'));
		}

		//判断是否已经完成打卡

		$check_time = substr(date('Y-m-d'), 0, 10);

		$map = array(
			'is_del'   => 0,
			'is_hid'   => 0,
			'member_id'=>$this->member_id,
			'district_id' => $ckMember['district_id'],
			'add_time' => array('LIKE', "$check_time%"),
			'company_id' => $ckMember['company_id']
		);

		$count = M($this->table)->where($map)->count();
		if($count >= 2){
			$this->apiReturn(array('status'=>'0', 'msg'=>'今日已完成打卡'));
		}

		$map['type'] =2;
		$countx = M($this->table)->where($map)->count();
		if($countx > 0){
			$this->apiReturn(array('status'=>'0', 'msg'=>'已打下班卡'));
		}

		//获取该用户对应小区的打卡时间
		$ckModel = M('ck_model')->where($re)->order('clock_time asc')->select();

		if(empty(I('type', '', 'trim'))){
			if($hour >= 12){
				$type = 2;
			}else{
				if($count == 1){
					$type = 2;
				}else{
					$type = 1;
				}
			}
		}else{
			$type = 3;
		}

		if($hour >= 12){
			if($this->compareTime($hour, $min, substr($ckModel[1]['clock_time'], 0, 2), substr($ckModel[1]['clock_time'], 3, 5))){
				$status = 1;
			}else{
				$status = 2;
			}
		}else{
			if($type == 1){
				if($this->compareTime($hour, $min, substr($ckModel[0]['clock_time'], 0, 2), substr($ckModel[0]['clock_time'], 3, 5))){
					$status = 3;
				}else{
					$status = 1;
				}
			}else if($type == 2){
				if($this->compareTime($hour, $min, substr($ckModel[1]['clock_time'], 0, 2), substr($ckModel[1]['clock_time'], 3, 5))){
					$status = 1;
				}else{
					$status = 2;
				}
			}
		}


		// $map['type'] = $type;
		// $clock = M($this->table)->where($map)->find();
		// if(!empty($clock)){
		// 	$this->apiReturn(array('status'=>'0', 'msg'=>$type == 1?'已打上班卡，请勿重复打卡':'已打下班卡，请勿重复打卡'));
		// }

		$data['district_id'] = $ckMember['district_id'];
		$data['company_id'] = $ckMember['company_id'];
		$data['status'] = $status;
		$data['type'] = $type;
		$data['member_id'] = $this->member_id;
		$data['add_time'] = date('Y-m-d H:i:s');
		$data['check_time'] = date('Y-m-d');

		try {
			$M = M();
			$M->startTrans();
			
			$result = update_data(D('CkClock'),[],[],$data);
			if(!is_numeric($result)){
				$this->api_error('打卡失败',array('status'=> '0'));
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();

		$this->api_success('打卡成功', array('status'=> '1'));
	}

	/**
	  * 考勤类型
	  * @author abincc
	  * @time 2018-07-06
	  */
	public function clockType(){
		if(!IS_POST)		$this->api_error('请求方式错误');

		$map = array(
			'is_del' => 0,
			'is_hid' => 0,
			'id'     => $this->member_id
		);
		$member = M('ck_member')->where($map)->field('id,username,mobile,district_id,company_id')->find();

		if(empty($member)){
			$this->api_error('该用户不存在');
		}

		$where = array(
			'is_hid'      => 0,
			'is_del'      => 0,
			'district_id' => $member['district_id'],
			'company_id'  => $member['company_id']
		);

		$list = M('ck_type')->where($where)->select();

		$this->api_success('o~k', $list);	
	}


	/**
	  *  考勤
	  *  @author abincc
	  *  @time 2018-07-06
	  */
	public function workClock(){
		if(!IS_POST)		$this->api_error('请求方式错误');

		$map = array(
			'is_del' => 0,
			'is_hid' => 0,
			'id'     => $this->member_id
		);

		$member = M('ck_member')->where($map)->field('id,username,mobile,district_id,company_id')->find();

		if(empty($member)){
			$this->api_error('该用户不存在');
		}

		$data = array();

		//获取考勤类型  1- 保安寻根  2 - 保洁
		$data['type'] = I('type', '','int');
		$data['add_time']  = date('Y-m-d H:i:s');
		$data['member_id'] = $this->member_id;
		$data['district_id'] = $member['district_id'];
		$data['company_id'] = $member['company_id'];
		try {
			$M = M();
			$M->startTrans();

			$config = array(
				'maxSize'    => 31457280,    
				'rootPath'   => './',				//相对网站根目录
				'savePath'   => 'Uploads/Work/',	//文件具体保存的目录
				'saveName'   => array('uniqid',''),    
				'exts'       => array('jpg', 'gif', 'png', 'jpeg'),
				'autoSub'    => true,    
				'subName'    => array('date','Ymd'),
			); 

			$Upload 	 = new \Think\Upload($config);        /* 实例化上传类 */
			$file_info   = $Upload->upload();

			if(!$file_info) {
				$this->error('上传失败');
			}else{  
				$data['image'] = $file_info['img']['savepath'].$file_info['img']['savename'];

				$image = new \Think\Image(); 
				$image->open('./'.$data['image'])->water('./logo.jpg',\Think\Image::IMAGE_WATER_NORTHWEST,10)->save($data['image']); 
			}

			$result = update_data(D('ck_work'),[],[],$data);
			if(!is_numeric($result)){
				@unlink($data['image']);
				throw new \Exception($this->error);
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('提交成功');
	}



	/**
	 *  报销情况查询
	 *  @author abincc
	 *  @time 2018-07-11
	 */
	public function reimburseList(){
		if(!IS_POST)		$this->api_error('请求方式错误');

		$map = array(
			'is_del' => 0,
			'is_hid' => 0,
			'id'     => $this->member_id
		);

		$member = M('ck_member')->where($map)->field('id,username,mobile,district_id,company_id')->find();

		if(empty($member)){
			$this->api_error('该用户不存在');
		}

		$where = array(
			'is_del' => 0,
			'is_hid' => 0,
			'mobile' => $member['mobile'],
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id']
		);

		$field = array('id as reimburse_id','district_id','username', 'mobile', 'type', 'reason', 'occur_time', 'cost', 'is_confirm', 'remark');
		$list  = $this->page(D('reimburse'),$where,'occur_time desc',$field);
		if(!empty($list)){
			$type = get_table_state('reimburse','type');
			foreach ($list as $k => $v) {
				$list[$k]['type'] = $type[$v['type']]['title'];
			}
		}else{
			$list = [];
		}

		$this->api_success('o~k', $list);
	}


	/**
	 *  报销详情
	 *	@author abincc
	 *	@time 2018-07-11
	 */
	public function reimburseDetail(){
		if(!IS_POST)		$this->api_error('请求方式错误');

		$reimburse_id = I('reimburse_id', '', 'int');
		$field = array('id as reimburse_id','district_id','username', 'mobile', 'type', 'reason', 'occur_time', 'cost', 'is_confirm', 'remark');
		$reimburse = M('reimburse')->where('id='.$reimburse_id)->field($field)->find();
		if(empty($reimburse)){
			$this->api_error('报销信息不存在');
		}

		$type = get_table_state('reimburse','type');
		$reimburse['type'] = $type[$reimburse['type']]['title'];

		$reimburse['image'] = M('reimburse_image')->where('pid='.$reimburse_id)->select();
		$this->api_success('o~k', $reimburse);
	}


	/**
	 * 添加报销
	 * @author abincc
	 * @time 2018-07-11
	 */
	public function reimburseAdd(){
		if(!IS_POST){
			$t_array = [];
			$type = get_table_state('reimburse','type');
			foreach ($type as $k => $v) {
				array_push($t_array, $v);
			}
			$this->api_success('o~k', $t_array);
		}else{

			$member = M('ck_member')->where('id='.$this->member_id)->field('id, mobile,district_id,username,company_id')->find();
			if(empty($member)){
				$this->api_error('用户不存在');
			}

			$data = array(
	            'district_id'  =>  $member['district_id'],
	            'company_id'   =>  $member['company_id'],
	            'username'     =>  $member['username'],
	            'mobile'       =>  $member['mobile'],
	            'type'         =>  I('type','','int'),
	            'reason'       =>  I('reason','','trim'),
	            'occur_time'   =>  I('occur_time','','trim'),
	            'cost'         =>  I('cost','','trim'),
	            'add_time'     =>  date('Y-m-d H:i:s'),
	            'remark'       =>  I('remark','','trim'),
	        );


	        if(I('reimburse_id','','int') != 0){
				$data['id'] = I('reimburse_id','','int');
			}

	        $img[] = $_FILES['img'];

			try {
				$M = M();
				$M->startTrans();
				$id = update_data(D('reimburse'),[],[],$data);
				if(!is_numeric($id)){
					throw new \Exception($id);
				}
				if(empty($_FILES['img'])){
					throw new \Exception('请上传图片');
				}
				if(count($img) > 5){
					throw new \Exception('上传图片数量不能超过9张');
				}
				$config = array(
						'maxSize'    => 3145728,
						'rootPath'   => './',
						'savePath'   => 'Uploads/Parking/', 
						'saveName'   => array('uniqid',''),
						'exts'       => array('jpg', 'gif', 'png', 'jpeg'),
						'autoSub'    => true,    
						'subName'    => array('date','Ymd'),
					);
				/*  处理上传图片*/
				$img_info = $this->upload_files($img,$config);
				if(empty($img_info)){
					throw new \Exception($this->error);
				}
				$images = array();
				foreach($img_info as $file){  
					// $img_size = getimagesize('./'.$file['savepath'].$file['savename']);
					$images[] = array(
						'pid' 		 =>$id,
						'image'		 =>$file['savepath'].$file['savename'],
						'add_time'	 =>date('Y-m-d H:i:s'),
					);
				}
				$sql = addSql($images,$this->table_image);
				$res = M()->execute($sql);
				if(!is_numeric($res)){
					/*清除图片文件*/
					array_walk($images, function($a){
						@unlink ( $a['image'] );
					});
					unset($img_info);
					unset($images);
					unset($img);
					throw new \Exception('图片上传失败');
				}
				
			} catch (\Exception $e) {
				$M->rollback();
				$this->api_error($e->getMessage());
			}
			$M->commit();
			$this->api_success('提交成功',array('reimburse_id'=>$id));
		}
	}

	/**
	 * 个人薪资列表
	 *	@author abincc
	 *  @time 2018-07-11
	 */
	public function salaryList(){

		if(!IS_POST)  $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id, mobile,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$where = array(
			'is_del' => 0,
			'is_hid' => 0,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'mobile' => $member['mobile']
		);

		$field = array('id as salary_id','mobile','username','real_total','salary_time');
		$list  = $this->page(D('salary'),$where,'salary_time desc',$field);
		if(!empty($list)){
			foreach ($list as $k => $v) {
				$salary = M('salary')->where('id='.$v['salary_id'])->find();
				if(!empty($salary)){
					$list[$k]['salary'] = $salary;
				}
			}
		}else{
			$list = [];
		}

		$this->api_success('o~k', $list);
	}

	/**
	 * 薪资详情
	 * @author abincc
	 * @time 2018-07-11
	 */
	public function salaryDetail(){
		if(!IS_POST)		$this->api_error('请求方式错误');

		$salary_id = I('salary_id', '', 'int');
		$salary = M('salary')->where('id='.$salary_id)->find();
		if(empty($salary)){
			$this->api_error('薪资信息不存在');
		}

		$this->api_success('o~k', $salary);
	}


	/**
	 *  请假
	 *  @author abincc
	 *  @time 20180711
	 */
	public function leave(){
		if(!IS_POST){
			$t_array = [];
			$type = get_table_state('ck_leave','type');
			foreach ($type as $k => $v) {
				array_push($t_array, $v);
			}
			$this->api_success('o~k', $t_array);
		}else{

			$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
			if(empty($member)){
				$this->api_error('用户不存在');
			}

			$map = array(
				'member_id'  => $this->member_id,
				'district_id'=> $member['district_id'],
				'company_id'=> $member['company_id'],
				'type'       => I('type', '','int'),
				'start_time' => I('start_time','','trim'),
				'end_time'   => I('end_time','','trim'),
				'num_day'    => I('num_day','','trim'),
				'remark'     => I('remark','','trim'),
				'status'     => 1,
				'add_time'   => date('Y-m-d H:i:s')
 			);

 			if(empty($map['start_time']) || empty($map['type']) || empty($map['end_time']) || empty($map['num_day'])){
 				$this->api_error('必填不为空');
 			}

			if(strtotime($map['start_time']) > strtotime($map['end_time'])){
				$this->api_error('结束时间应大于等于开始时间');
			}

			$where = array(
				'member_id'   => $this->member_id,
				'district_id' => $member['district_id'],
				'status'      => array('IN', ['1', '2', '3']),
				'is_del'      => 0,
				'is_hid'      => 0
			);
			$leave_info = M('ck_leave')->where($where)->order('id desc')->find();

			if(strtotime($map['start_time']) <= strtotime($leave_info['end_time'])){
				$this->api_error('该时间段内，您已请假!');
			}

 			try {
				$M = M();
				$M->startTrans();

				$result = update_data(D('ck_leave'),[],[],$map);
				if(!is_numeric($result)){
					throw new \Exception($this->error);
				}

			} catch (\Exception $e) {
				$M->rollback();
				$this->api_error($e->getMessage());
			}
			$M->commit();
			$this->api_success('提交成功');
		}
	}

	/**
	 *  请假详情
	 *  @author abincc
	 *  @time 2018-07-11
	 */
	public function leaveDetail(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$map = array(
			'id'        => I('leave_id','','int'),
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'    => 0,
			'is_hid'    => 0
		);

		$list = M('ck_leave')->where($map)->find();
		$type = get_table_state('ck_leave','type');

		if(empty($list)){
			$list['type'] = $type[$list['type']]['title']; 
		}

		if($list['check_id'] != 0){
			$check_member = M('ck_member')->where('id='.$list['check_id'])->field('id,mobile,username')->find();
			if(!empty($check_member)){
				$list['check_member'] = $check_member;
			}
		}

		if($list['pass_id'] != 0){
			$pass_member = M('ck_member')->where('id='.$list['pass_id'])->field('id,mobile,username')->find();
			if(!empty($pass_member)){
				$list['pass_member'] = $pass_member;
			}
		}

		$this->api_success('^ok^', $list);
	}


	/**
	 *  采购
	 *  @author abincc
	 *	@time 20180712
	 */
	public function buy(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$map = array(
			'member_id'    => $this->member_id,
			'district_id'  => $member['district_id'],
			'company_id'  => $member['company_id'],
			'reason'       => I('reason','','trim'),
			'deliver_time' => I('deliver_time', '', 'trim'),
			'add_time'     => date('Y-m-d H:i:s'),
			'status'       => 1
 		);

 		if(empty($map['reason']) || empty($map['deliver_time'])){
 			$this->api_error('必填不为空');
 		}

 		// var_dump(I('buy_id','','int'));
 		// die();
 		if(I('buy_id','','int') != 0){
			$map['id'] = I('buy_id','','int');
		}

 		$postArray = I('details', '', 'trim');

 		$img[] = $_FILES['img'];

 		try {
			$M = M();
			$M->startTrans();

			//保持采购信息
			$result = update_data(D('ck_buy'),[],[],$map);
			if(!is_numeric($result)){
				throw new \Exception($this->error);
			}

			//采购明细
			if(I('buy_id','','int') == 0){

				$de_json = json_decode($postArray,TRUE);
				$count_json = count($de_json);
				for ($i = 0; $i < $count_json; $i++)
				{	
					$data['pid'] = $result;
					$data['name'] = $de_json[$i]['name'];
					$data['model_spec'] = $de_json[$i]['model_spec'];
					$data['num'] = $de_json[$i]['num'];
					$data['money'] = $de_json[$i]['money'];
					$data['remark'] = $de_json[$i]['remark'];
					$data['add_time'] = date('Y-m-d H:i:s');

					if(empty($data['name']) || empty($data['model_spec']) || empty($data['num']) || empty($data['money'])){
						throw new \Exception('必填不为空');
					}

					$res = update_data(D('ck_buy_detail'),[],[], $data);
					if(!is_numeric($res)){
						throw new \Exception($this->error);
					}
				}
			}

			//附件处理
			if(empty($_FILES['img'])){
				throw new \Exception('请上传图片');
			}

			$config = array(
					'maxSize'    => 3145728,
					'rootPath'   => './',
					'savePath'   => 'Uploads/Buy/', 
					'saveName'   => array('uniqid',''),
					'exts'       => array('jpg', 'gif', 'png', 'jpeg'),
					'autoSub'    => true,    
					'subName'    => array('date','Ymd'),
				);
			/*  处理上传图片*/
			$img_info = $this->upload_files($img,$config);
			if(empty($img_info)){
				throw new \Exception($this->error);
			}
			$images = array();
			foreach($img_info as $file){  
				// $img_size = getimagesize('./'.$file['savepath'].$file['savename']);
				$images[] = array(
					'pid' 		 =>$result,
					'image'		 =>$file['savepath'].$file['savename'],
					'add_time'	 =>date('Y-m-d H:i:s'),
				);
			}
			$sql = addSql($images,$this->table_buy_image);
			$res = M()->execute($sql);
			if(!is_numeric($res)){
				/*清除图片文件*/
				array_walk($images, function($a){
					@unlink ( $a['image'] );
				});
				unset($img_info);
				unset($images);
				unset($img);
				throw new \Exception('图片上传失败');
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('ok',array('buy_id'=>$result));
	}

	/**
	 *  采购详情
	 *	@author abincc
	 *  @time 20180712
	 */
	public function buy_detail(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$buy_id = I('id', '', 'int');

		$map = array(
			'id' 		  => $buy_id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_hid'      => 0,
			'is_del'      => 0 
		);

		//获取采购数据
		$buy = M('ck_buy')->where($map)->field('id,reason,deliver_time,add_time,status')->find();
		if(empty($buy)){
			$this->api_error('采购数据不存在');
		}
		$data['buy'] = $buy;

		//获取采购详情
		$detail = M('ck_buy_detail')->where('pid='.$data['buy']['id'])->field('id,pid,name,model_spec,num,money,remark,add_time')->select();
		if(empty($detail)){
			$this->api_error('采购数据详情不存在');
		}
		$data['detail'] = $detail;

		// 获取附件信息
		$images = M('ck_buy_image')->where('pid='.$data['buy']['id'])->field('id,image')->select();
		if(!empty($images)){
			$data['images'] = $images;
		}

		$this->api_success('ok', $data);
	}


	/**
	 *  我的申请
	 *  @author abincc 
	 *  @time 2018-07-11
	 */
	public function my_apply(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$tab_index = I('tab', '', 'int');

		$map = array(	
			'member_id'   =>$this->member_id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'      => 0,
			'is_hid'      => 0
		);

		if($tab_index == 1){
			// 我的请假申请
			$type = get_table_state('ck_leave','type');
	        $data = M('ck_leave')->where($map)->field('id,type,start_time,end_time,num_day, remark,status')->select();
	        if(!empty($data)){
	        	foreach ($data as $k => $v) {
	        		$data[$k]['type'] = $type[$v['type']]['title'];
	        	}
	        }
		}else if($tab_index == 2){
			//我采购申请
			$data = M('ck_buy')->where($map)->field('id,reason,deliver_time,status')->select();
		}else{
			$this->api_error('未知错误');
		}

		if(empty($data)){
			$data = [];
		}

		$this->api_success('ok', $data);
	}


	/**
	 *  我的审批
	 *  @author abincc
	 *  @time 2018-07-11
	 */
	public function my_approval(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$map = array(	
			'member_id'   =>$this->member_id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'      => 0,
			'is_hid'      => 0
		);

		$type = get_table_state('ck_leave','type');
		$member_job = M('ck_job')->where($map)->find();
		
		$where = array(	
			'member_id'   => array('NEQ', $this->member_id),
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'      => 0,
			'is_hid'      => 0
		);

		$tab_index = I('tab', '', 'int');

		if(!empty($member_job) && in_array($member_job['job_id'], ['1','2'])){

			if($tab_index == 1){
			//请假审批
				$data['approval'] = M('ck_leave')->where($where)->field('id,member_id,type,start_time,end_time,num_day, remark,status')->select();
		        if(!empty($data['approval'])){
		        	foreach ($data['approval'] as $k => $v) {
		        		$data['approval'][$k]['type'] = $type[$v['type']]['title'];
		        		$member = M('CkMember')->where('id='.$v['member_id'])->field('mobile,nickname,username')->find();
		        		if(!empty($member)){
		        			$data['approval'][$k]['member'] = $member;
		        		}
		        		if($member_job['job_id'] == 1){
		        			if($data['approval'][$k]['status'] != 2){
		        				$data['approval'][$k]['c_status'] = 0;
		        			}else{
		        				$data['approval'][$k]['c_status'] = 1;
		        			}
		        		}else if($member_job['job_id'] == 2){
		        			if($data['approval'][$k]['status'] != 1){
		        				$data['approval'][$k]['c_status'] = 0;
		        			}else{
		        				$data['approval'][$k]['c_status'] = 1;
		        			}
		        		}
		        	}
		        }
			}else if($tab_index == 2){
				$data['approval'] = M('ck_buy')->where($where)->field('id,member_id,reason,deliver_time,status')->select();
				if(!empty($data['approval'])){
					foreach ($data['approval'] as $k => $v) {
		        		$member = M('CkMember')->where('id='.$v['member_id'])->field('mobile,nickname,username')->find();
		        		if(!empty($member)){
		        			$data['approval'][$k]['member'] = $member;
		        		}
		        		if($member_job['job_id'] == 1){
		        			if($data['approval'][$k]['status'] != 2){
		        				$data['approval'][$k]['c_status'] = 0;
		        			}else{
		        				$data['approval'][$k]['c_status'] = 1;
		        			}
		        		}else if($member_job['job_id'] == 2){
		        			if($data['approval'][$k]['status'] != 1){
		        				$data['approval'][$k]['c_status'] = 0;
		        			}else{
		        				$data['approval'][$k]['c_status'] = 1;
		        			}
		        		}
		        	}
				}
			}else{
				$this->api_error('未知错误');
			}
		}

		if(empty($data)){
			$data = [];
		}
		$data['job'] = $member_job;
		$this->api_success('ok', $data);
	}


	/**
	 *  审批  -- 通过
	 *	@author abincc
	 *	@time 2018-07-11
	 */
	public function approval(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$map = array(	
			'member_id'   =>$this->member_id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'      => 0,
			'is_hid'      => 0
		);

		$member_job = M('ck_job')->where($map)->find();
		if(empty($member_job) || !in_array($member_job['job_id'], ['1','2'])) $this->api_error('没有审批权限');

		$tab_index = I('tab', '', 'int');
		$id = I('id','','int');
		$where = array(
			'id' => $id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_hid' => 0,
			'is_del' => 0
		);
		if($tab_index == 1){
			//请假信息
			$info = M('ck_leave')->where($where)->find();
		}else if($tab_index == 2){
			$info = M('ck_buy')->where($where)->find();
		}else{
			$this->api_error('未知错误');
		}


		if(empty($info))  $this->api_error($tab_index == 1?'请假数据不存在':'采购数据不存在');
		if(!in_array($info['status'], ['1','2']))  $this->api_error($tab_index == 1?'请假已处理':'采购已处理');

		$rs = array(
			'id'   => $id,
		);

		if($member_job['job_id'] == 1){
			if($info['status'] != 2){
				$this->api_error($tab_index == 1?'请假未校核':'采购未校核');
			}
			$rs['status']  = 3;
			$rs['pass_id'] = $this->member_id; 
		}

		if($member_job['job_id'] == 2){
			if($info['status'] != 1){
				$this->api_error($tab_index == 1?'请假已校核':'采购已校核');
			} 
			$rs['status']  = 2;
			$rs['check_id'] = $this->member_id; 
		}

		try {
			$M = M();
			$M->startTrans();

			$result = update_data($tab_index == 1?D('ck_leave'):D('ck_buy'),[],[],$rs);
			if(!is_numeric($result)){
				throw new \Exception($this->error);
			}
		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('ok');
	}

	/**
	 *  审批 -- 拒绝
	 *	@author abincc
	 *	@time 2018-07-11
	 */
	public function refuse(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$map = array(	
			'member_id'   =>$this->member_id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'      => 0,
			'is_hid'      => 0
		);

		$member_job = M('ck_job')->where($map)->find();
		if(empty($member_job) || !in_array($member_job['job_id'], ['1','2'])) $this->api_error('没有审批权限');

		$tab_index = I('tab', '', 'int');
		$id = I('id','','int');
		$where = array(
			'id' => $id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_hid' => 0,
			'is_del' => 0
		);
		if($tab_index == 1){
			//请假信息
			$info = M('ck_leave')->where($where)->find();
		}else if($tab_index == 2){
			$info = M('ck_buy')->where($where)->find();
		}else{
			$this->api_error('未知错误');
		}


		if(empty($info))  $this->api_error($tab_index == 1?'请假数据不存在':'采购数据不存在');
		if(!in_array($info['status'], ['1','2']))  $this->api_error($tab_index == 1?'请假已处理':'采购已处理');

		$rs = array(
			'id'   => $id,
		);

		if($member_job['job_id'] == 1){
			if($info['status'] != 2){
				$this->api_error($tab_index == 1?'请假未校核':'采购未校核');
			}
			$rs['status']  = 5;
			$rs['pass_id'] = $this->member_id; 
		}

		if($member_job['job_id'] == 2){
			if($info['status'] != 1){
				$this->api_error($tab_index == 1?'请假已校核':'采购已校核');
			} 
			$rs['status']  = 4;
			$rs['check_id'] = $this->member_id; 
		}

		try {
			$M = M();
			$M->startTrans();

			$result = update_data($tab_index == 1?D('ck_leave'):D('ck_buy'),[],[],$rs);
			if(!is_numeric($result)){
				throw new \Exception($this->error);
			}
		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('ok');
	}


	/**
	  *  获取华庭-小区 公告类别
	  *  @author abincc
	  *	 @time 20180719
	  */
	public function ckHomeType(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$type = get_table_state('ck_home','type');

		if(!is_array($type)){
			$this->api_error('内部错误');
		}

		if(count($type) <= 0){
			$this->api_error('类别不能为空,请物业添加类别');
		}

		$t_array = [];
		foreach ($type as $k => $v) {
			array_push($t_array, $v);
		}
		$this->api_success('o~k', $t_array);
	}

	/** 
	  *  根据类型获取对应小区的公告信息
	  *	 @author abincc
	  *  @time  20180719
	  */
	public function getHomeList(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$type = I('type','','int');

		if(empty($type)){
			$this->api_error('类别不能为空');
		}

		$map = array(
			'is_del' => 0,
			'is_hid' => 0,
			'type'   => $type,
			'company_id' => $member['company_id']
		);

		$field = array('id','type','district_id','title','desc','cover','add_time');

		if(in_array($type, ['1','2'])){
			$map['district_id'] = 0;
			$list = M('ck_home')->where($map)->field($field)->select();
		}else{
			$map['district_id'] = $member['district_id'];
			$list = M('ck_home')->where($map)->field($field)->select();
		}
		if(empty($list)){
			$list = [];
		}

		$this->api_success('ok', $list);
	}

	/**
	 *  华庭-小区信息公告详情
	 *  @author abincc
	 *  @time 20180719
	 */
	public function home_detail(){
		if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$id = I('id','','int');
		$map = array(
			'is_del' => 0,
			'is_hid' => 0,
			'id'     => $id
		);
		if(empty($map['id'])){
			$this->error('公告错误');
		}

		$field = array('id','type','district_id','title','desc','cover','add_time','remark');
		$detail = M('ck_home')->where($map)->field($field)->find();
		if(empty($detail)){
			$this->error('公告不存在');
		}
		
		$this->api_success('ok', $detail);
	}

	/**
	  *  广告 
	  * 
	  *
	  */
    public function banner(){

    	$data = get_adspace_cache(8);
    	if(empty($data)){
    		$data = [];
    	}
    	
    	$this->api_success('ok', $data);
    }


    /**
     *  签到用户列表查询
     *  @author abincc
     *  @time 20180723
     */
    public function  member_list(){
    	if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id,company_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}


		$map = array(	
			'member_id'   =>$this->member_id,
			'district_id' => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'      => 0,
			'is_hid'      => 0
		);

		$member_job = M('ck_job')->where($map)->find();
		if(empty($member_job) || !in_array($member_job['job_id'], ['1','2'])) $this->api_success('ok',[]);

		$where = array(
			'id'           => array('NEQ',$this->member),
			'district_id'  => $member['district_id'],
			'company_id' => $member['company_id'],
			'is_del'       => 0,
			'is_hid'       => 0
		);

		if(intval($member['district_id']) == 0){
			unset($where['district_id']);
		}

		$member_list = M('ck_member')->where($where)->field('id, nickname, username, mobile, district_id')->select();
		if(empty($member_list)){
			$this->api_success('ok', []);
		}

		$this->api_success('ok', $member_list);
		
    }


    /**
     *  签到信息查询
     *  @author abincc
     *  @time 20180723
     */
    public function  clock_list(){
    	if(!IS_POST)   $this->api_error('请求方式错误');

		$member = M('ck_member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member)){
			$this->api_error('用户不存在');
		}

		$map = array();
		$member_id = I('member_id', '', 'int');
		$time = substr(date("Y-m-d H:i:s",strtotime(I('time'))), 0, 7);
		$time_now = substr(date("Y-m-d"), 0, 7);

		$map['member_id'] = (empty($member_id)?$this->member_id:$member_id);

		$map['add_time'] = array('LIKE', (empty(I('time'))?$time_now:$time).'%');

		$map['is_del'] = 0;

		$state = 1;

		$clock_list = M('ck_clock')->where($map)->field('check_time, count("check_time") as count')->group('check_time')->select();
		
		if(!empty($clock_list)){
			foreach ($clock_list as $k => $v) {
				$where = array(
					'member_id'   => (empty($member_id)?$this->member_id:$member_id),
					'check_time'  => $v['check_time'],
					'is_del'      => 0
				);
				$clock_desc = M('ck_clock')->where($where)->order('id asc')->select();

				if($clock_list['count'] < 2){
					$clock_list[$k]['state'] = 0;
				}else{
					foreach ($clock_desc as $m => $n) {
						if($n['status'] != 1){
							if($state != 0){
								$state = 0;
							}
						}
					}
					$clock_list[$k]['state'] = $state;
				}

				$clock_list[$k]['desc'] = $clock_desc;				
			}
		}else{
			$this->api_success('ok', []);
		}

		$this->api_success('ok', $clock_list);
    }
}