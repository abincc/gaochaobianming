<?php
namespace Api\Controller\User;
use Api\Controller\Base\CkBaseController;

class CkAccountController extends CkBaseController{
	
	
	/**
	 * 上传会员头像
	 * @author llf  2017-08-19
	 */
	public function upload_headimg(){

		$headimg = $_FILES['headimg'];
		if(empty($headimg)){
			$this->apiReturn(array('status'=>'0','msg'=>'请上传图片'));
		}

		$path = 'Uploads/CkHeadimg';
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

		$field = array('username','district_id','nickname','sex','age','signature');
		$info = D('CkMember')->where('id='.$this->member_id)->field($field)->find();
		if(empty($info)){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}
		$district_list   = get_no_del('district');
		$info['headimg'] = ck_get_avatar($this->member_id);
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

		$data['id']			= $this->member_id;

		$data = array();
		$data['nickname']	= I('post.nickname','','trim');
		// $data['username']	= I('post.username','','trim');
		// $data['district_id']= I('post.district_id','','cloud_id_parser');
		$data['sex'] 		= I('post.sex','','int');
		$data['birthday'] 		= I('post.birthday','','trim');
		$data['signature'] 	= I('post.signature','','trim');
		$data['id']			= $this->member_id;

		$has = D('CkMember')->where('id='.$this->member_id)->field(array('id'))->find();
		if(empty($has)){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}

		$result = update_data(D('CkMember'),[],[],$data);
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
		$has = M('CkMember')->where('id='.$this->member_id)->field('id,password')->find();
		if(empty($has)){
			$this->apiReturn(array('status'=>'0','msg'=>'用户信息不存在'));
		}
		if(!sys_password_verify($data['oldpassword'],$has['password'])){
			$this->apiReturn(array('status'=>'0','msg'=>'原密码不正确'));
		}
		if($data['password'] === $data['oldpassword']){
			$this->apiReturn(array('status'=>'0','msg'=>'新密码与旧密码不能相同'));
		}
		$result = M('CkMember')->where('id='.$this->member_id)->setField('password',sys_password_encrypt($data['password']));
		if(!is_numeric($result)){
			$this->apiReturn(array('status'=>'0','msg'=>'设置失败'));
		}
		$this->apiReturn(array('status'=>'1','msg'=>'修改成功'));  

	}
}