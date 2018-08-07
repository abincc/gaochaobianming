<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;
// use Common\Plugin\Message;
// use Common\Org\Check;
// use Org\Util\String;


class CkUserController extends ApiController{

	protected $table = 'ck_member';


	/* 用户登录接口 */
	public function login(){
 
		$post	= I('post.');
		// bug($post);
		$Member = D('CkMember');
		$result = $Member->check_login($post['mobile'],$post['password'],0);
		if(!is_numeric($result)){
			$this->apiReturn(array('status'=>'0','msg'=>$result));
		}
		/*登录操作*/
		$member_info = $Member->login($result);
		if(empty($member_info)){

			$this->apiReturn(array('status'=>'0','msg'=>$Member->getError()));
		}

		/* 小区信息 */
		$district_info = M('district')->where('id='.intval($member_info['district_id']))->find();

		$member_info['district_id'] 	= '';
		$member_info['district_title']  = '';

		if(!empty($district_info)){
			$member_info['district_id'] 	= intval($district_info['cloud_id']);
			$member_info['district_title']	= trim($district_info['title']);
		}

		$member_info['wxappid'] = WX_APPID;
		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>$member_info));

	}


	//获取token
	public function get_token(){

		$ticket = I('ticket','','trim');

		if(empty($ticket)){
			$this->apiReturn(array('status'=>'0','msg'=>'ticket 参数必须'));
		}

		$Member = D('CkMember');
		$token  = $Member->create_token($ticket);

		if(!$token){
			$status = '0';
			if(!empty($Member->status)){
				$status = $Member->status;
			}
			$this->apiReturn(array('status'=>$status,'msg'=>$Member->getError()));
		}

		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>$token));
		
	}


	/*
	 * 找回密码第一步
	 */
	public function forgot_step_1(){

		$post = I('post.');

		//接收手机和短信信息进行验证
		$mobile  = $post['mobile'];
		//获取到验证的
		$code    = $post['code'];

		if(empty($mobile))		$this->apiReturn(array('status'=>'0','msg'=>'请输入手机号'));
		if(!preg_match(MOBILE,$mobile)){
			$this->apiReturn(array('status'=>'0','msg'=>'手机号格式不正确'));
		}
		if(empty($code)){
			$this->apiReturn(array('status'=>'0','msg'=>'请输入验证码'));
		}

		/*验证短信验证码*/
		$Codo    = D('Code');
		$code_id = $Codo->check($post['mobile'],trim($code),2);
		if(!is_numeric($code_id)){
			$this->api_error('验证码错误');
		}

		$has = M('Member')->where('mobile='.$mobile)->find();
		if(empty($has) || $has['is_del']){
			$this->apiReturn(array('status'=>'0','msg'=>'手机账户不存在'));
		}
		if($has['is_hid']){
			$this->apiReturn(array('status'=>'0','msg'=>'账户被禁用'));
		}

		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>encrypt_key(serialize(array('code'=>$code,'mobile'=>$mobile,'code_id'=>$code_id)))));

	}


	/*
	 * 找回密码第二步
	 */
	public function forgot_step_2(){

		$post = I('post.');

		//接收安全问题和凭证信息
		$password  	= $post['password'];
		$repassword = $post['repassword'];
		$code_id    = $post['code_id'];

		if(empty($password))			$this->apiReturn(array('status'=>'0','msg'=>'请输入新密码'));
		if(empty($repassword))			$this->apiReturn(array('status'=>'0','msg'=>'请输入确认密码'));
		if($repassword !== $password)	$this->apiReturn(array('status'=>'0','msg'=>'确认密码不一致'));
		if(empty($code_id))				$this->apiReturn(array('status'=>'0','msg'=>'code_id 参数必须'));

		//验证code
		$code_info = unserialize(decrypt_key($code_id));
		if(empty($code_info)){
			$this->api_error('code_id 数据解析错误');
		}
		if(!array_key_exists('mobile', $code_info) || !array_key_exists('code_id',$code_info)){
			$this->api_error('code_id 数据解析失败');
		}
		$Codo    = D('Code');
		$code_id = $Codo->check($code_info['mobile'],$code_info['code'],2,$code_info['code_id']);
		if(!is_numeric($code_id)){
			$this->api_error('验证码错误');
		}
		//查询该用户信是否存
		$has = M('Member')->where('mobile='.$code_info['mobile'])->field('id,is_hid,is_del')->find();
		if(empty($has) || $has['is_del']){
			$this->apiReturn(array('status'=>'0','msg'=>'手机账户不存在'));
		}
		if($has['is_hid']){
			$this->apiReturn(array('status'=>'0','msg'=>'账户被禁用'));
		}
		$data = array(
				'password'		=> sys_password_encrypt($password),
				'login_time'	=> date('Y-m-d H:i:s'),
			);
		$res  = D('Member')->where('id='.intval($has['id']))->save($data);
		if(!is_numeric($res)){
			$this->api_error($res);
		}
		$Codo->del($code_id);
		$this->apiReturn(array('status'=>'1','msg'=>'修改成功'));
	}
}