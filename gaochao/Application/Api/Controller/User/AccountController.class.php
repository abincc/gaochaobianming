<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

class AccountController extends BaseController{
	

	/**
	 * 上传会员头像
	 * @author llf  2017-08-19
	 */
	public function upload_headimg(){

		$headimg = $_FILES['headimg'];
		if(empty($headimg)){
			$this->apiReturn(array('status'=>'0','msg'=>'请上传图片'));
		}

		$path = 'Uploads/Headimg';
		$upload = new \Think\Upload();
		$upload->rootPath  = 	 './';
		$upload->maxSize   =     3145728 ;  
		$upload->exts      =     array('jpg', 'gif', 'png', 'jpeg');    
		$upload->savePath  =     $path; 
		$upload->saveName  =     'H_' . $this->member_id; 
		$upload->replace   =     true; 
		$upload->subName   =     array('file_savename',$this->member_id); 

		$info   =   $upload->uploadOne($headimg);
		if(!$info) {
			$this->apiReturn(array('status'=>'0','msg'=>$upload->getError()));
		}

		/* 处理图片后缀格式统一*/
		$url		= $info['savepath'].$info['savename'];
		$temp		= explode('.', $url);
		$temp[1]	= 'jpg';

		$image = new \Think\Image();
		$image->open($url);

		$temp_str = $temp[0];
		$temp[0]  = $temp_str.'_S';
		$image->thumb(60, 60)->save(implode('.', $temp));
		$image->open($url);
		$temp[0]  = $temp_str.'_M';
		$image->thumb(100, 100)->save(implode('.', $temp));
		$image->open($url);
		$temp[0]  = $temp_str.'_L';
		$image->thumb(200, 200)->save(implode('.', $temp));
		@unlink($url);
		$this->apiReturn(array('status'=>'1','msg'=>'提交成功'));  

	}

	/**
	 * 获取个人资料
	 * @author llf  2017-08-19
	 */
	public function get_user_info(){

		$field = array('username','district_id','nickname','building_no','room_no','idcard','sex','age','signature','register_code');
		$info = D('Member')->where('id='.$this->member_id)->field($field)->find();
		if(empty($info)){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}
		$district_list   = get_no_del('district');
		$info['headimg'] = get_avatar($this->member_id);
		$info['district_title'] = trim($district_list[$info['district_id']]['title']);
		$info['district_id'] = intval(M('district')->where('id='.$info['district_id'])->getField('cloud_id'));

		$this->apiReturn(array('status'=>'1','msg'=>'ok','info'=>$info));
	}


	/**
	 * 修改个人资料
	 * @author llf  2017-08-19
	 */
	public function edit_user_info(){

		if(!IS_POST)	$this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));  

		$data = array();
		$data['nickname']	= I('post.nickname','','trim');
		// $data['username']	= I('post.username','','trim');
		// $data['district_id']= I('post.district_id','','cloud_id_parser');
		$data['building_no']= I('post.building_no','','trim');
		$data['room_no']	= I('post.room_no','','trim');
		// $data['idcard'] 	= I('post.idcard','','trim');
		$data['sex'] 		= I('post.sex','','int');
		$data['age'] 		= I('post.age','','int');
		$data['signature'] 	= I('post.signature','','trim');
		$data['id']			= $this->member_id;

		$has = D('Member')->where('id='.$this->member_id)->field(array('id'))->find();
		if(empty($has)){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}

		$result = update_data(D('Member'),[],[],$data);
		if(!is_numeric($result)){
			$this->apiReturn(array('status'=>'0','msg'=>$result));
		}

		$this->apiReturn(array('status'=>'1','msg'=>'提交成功'));  

	}

	/**
	 * 修改密码
	 * @author llf  2017-08-19
	 */
	public function change_password(){

		if(!IS_POST)	$this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));
		$data = array();
		$data['oldpassword'] = I('post.oldpassword','');
		$data['password'] 	 = I('post.password','');
		$data['repassword']	 = I('post.repassword','');

		if(empty($data['oldpassword']))		$this->apiReturn(array('status'=>'0','msg'=>'请输入原密码'));
		if(empty($data['password']))		$this->apiReturn(array('status'=>'0','msg'=>'请输入新密码'));
		if(empty($data['repassword']))		$this->apiReturn(array('status'=>'0','msg'=>'请重新输入新密码'));
		if($data['password'] !== $data['repassword']){
			$this->apiReturn(array('status'=>'0','msg'=>'两次输入新密码不一致'));
		}

		//验证原始密码
		$has = M('Member')->where('id='.$this->member_id)->field('id,password')->find();
		if(empty($has)){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}
		if(!sys_password_verify($data['oldpassword'],$has['password'])){
			$this->apiReturn(array('status'=>'0','msg'=>'原密码不正确'));
		}
		if($data['password'] === $data['oldpassword']){
			$this->apiReturn(array('status'=>'0','msg'=>'新密码与旧密码不能相同'));
		}
		$result = M('Member')->where('id='.$this->member_id)->setField('password',sys_password_encrypt($data['password']));
		if(!is_numeric($result)){
			$this->apiReturn(array('status'=>'0','msg'=>'设置失败'));
		}
		$this->apiReturn(array('status'=>'1','msg'=>'修改成功'));  

	}


	/**
	 * 修改手机-第一步
	 * @author llf  2017-08-19
	 */
	public function change_mobile_step1(){

		if(!IS_POST)	$this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));

		$code 	   = I('post.code','','trim');
		$oldmobile = I('post.oldmobile','','trim');
		if(empty($oldmobile))				$this->apiReturn(array('status'=>'0','msg'=>'请输入原手机号'));
		if(!preg_match(MOBILE,$oldmobile))	$this->apiReturn(array('status'=>'0','msg'=>'手机号格式不正确'));
		if(empty($code))					$this->apiReturn(array('status'=>'0','msg'=>'请输入验证码'));

		/*验证短信验证码*/
		$Codo    = D('Code');
		$code_id = $Codo->check($oldmobile,$code,3);
		if(!is_numeric($code_id)){
			$this->api_error('验证码错误');
		}
		//验证原始手机号
		$has = M('Member')->where('id='.$this->member_id)->field('id,is_hid,is_del,mobile')->find();
		if(empty($has) || $has['is_del']){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}
		if($has['is_hid']){
			$this->apiReturn(array('status'=>'0','msg'=>'账户被禁用'));
		}
		if($has['mobile'] != $oldmobile){
			$this->apiReturn(array('status'=>'0','msg'=>'原始手机号不正确'));
		}
		$this->apiReturn(array('status'=>'1','msg'=>'验证通过')); 
	}


	/**
	 * 修改手机-第二步
	 * @author llf  2017-08-19
	 */
	public function change_mobile_step2(){

		if(!IS_POST)	$this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));

		$code 	   	= I('post.code','','trim');
		$mobile 	= I('post.mobile','','trim');
		if(empty($mobile))				$this->apiReturn(array('status'=>'0','msg'=>'请输入新手机号'));
		if(!preg_match(MOBILE,$mobile))	$this->apiReturn(array('status'=>'0','msg'=>'手机号格式不正确'));
		if(empty($code))				$this->apiReturn(array('status'=>'0','msg'=>'请输入验证码'));

		/*验证短信验证码*/
		$Codo    = D('Code');
		$code_id = $Codo->check($mobile,$code,4);
		if(!is_numeric($code_id)){
			$this->api_error('验证码错误');
		}

		//验证原始手机号
		$has = M('Member')->where('id='.$this->member_id)->field('id,is_hid,is_del,mobile')->find();
		if(empty($has) || $has['is_del']){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}
		if($has['is_hid']){
			$this->apiReturn(array('status'=>'0','msg'=>'账户被禁用'));
		}
		if($has['mobile'] === $mobile){
			$this->api_error('新手机不能与旧手机相同');
		}
		$data = array(
				'id'		=> $this->member_id,
				'mobile'	=> $mobile,
			);
		$res  = update_data(D('Member'),[],[],$data);
		if(!is_numeric($res)){
			$this->api_error($res);
		}
		$this->api_success('设置成功');
	}

	/**
	 * 申请开店
	 * @author llf  2017-08-19
	 */
	public function apply_shop(){

		if(!IS_POST)			$this->api_error('请求方式错误');

		$data = array();
		write_debug(I(),'申请开店请求参数');
		$data['shop_name'] 	 	= I('shop_name','','trim');
		$data['district_id'] 	= I('district_id','','cloud_id_parser');
		$data['building_no'] 	= I('building_no','','trim');
		$data['username'] 	 	= I('username','','trim');
		$data['mobile'] 	 	= I('mobile','','trim');
		$data['email'] 		 	= I('email','','trim');
		$data['idcard'] 	 	= I('idcard','','trim');
		$data['front_card']  	= '';
		$data['negative_card'] 	= '';

		if(empty($data['idcard'])){
			unset($data['idcard']);
		}
		if(empty($data['district_id'])){
			unset($data['district_id']);
		}		
		if(empty($data['building_no'])){
			unset($data['building_no']);
		}

		/* 处理图片上传*/
		$front_card 	= $_FILES['front_card'];
		$negative_card 	= $_FILES['negative_card'];

		$path = 'Uploads/ApplyShop/';
		$upload = new \Think\Upload();
		$upload->rootPath  = './';
		$upload->maxSize   = 3145728 ;  
		$upload->exts      = array('jpg', 'gif', 'png', 'jpeg');
		$upload->savePath  = $path; 
		$upload->saveName  = array('uniqid',''); 
		$upload->replace   = true;
		$upload->subName   = array('date','Ymd');

		$front_card_file   = $upload->uploadOne($front_card);
		if(!$front_card_file) {
			$front_card_error 	= $upload->getError();
		}
		if(file_exists($front_card_file['savepath'].$front_card_file['savename'])){
			$data['front_card'] 	= $front_card_file['savepath'].$front_card_file['savename'];
		}
		$negative_card_file   	= $upload->uploadOne($negative_card);
		if(!$negative_card_file) {
			$negative_card_error = $upload->getError();
		}
		if(file_exists($negative_card_file['savepath'].$negative_card_file['savename'])){
			$data['negative_card'] 	= $negative_card_file['savepath'].$negative_card_file['savename'];
		}

		try {
			$M = M();
			$M->startTrans();
			$apply_id = update_data(D('ApplyShop'),[],[],$data);
			if(!is_numeric($apply_id)){
				throw new \Exception($apply_id);
			}
			if(!empty($front_card_error)){
				throw new \Exception($front_card_error);
			}
			if(!empty($negative_card_error)){
				throw new \Exception($negative_card_error);
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('申请成功');

	}

	/**
	 * 申请志愿者
	 * @author llf  2017-11-23
	 */
	public function apply_volunteer(){

		if(!IS_POST)		$this->api_error('请求方式错误');
		$data = array();
		$data['skill'] 		= I('skill','','trim');
		$data['intention']	= I('intention','','trim');
		$data['start_date']	= I('start_date','','trim');
		$data['end_date']	= I('end_date','','trim');
		$data['member_id'] 	= $this->member_id;

		$member_info = M('Member')->where('id='.$this->member_id)->field('id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息不存在');
		}
		try {
			$M = M();
			$M->startTrans();
			$id = update_data(D('Volunteer'),[],[],$data);
			if(!is_numeric($id)){
				throw new \Exception($id);
			}
		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('提交成功');

	}

}