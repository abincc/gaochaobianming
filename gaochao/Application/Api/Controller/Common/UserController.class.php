<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;
// use Common\Plugin\Message;
// use Common\Org\Check;
// use Org\Util\String;


class UserController extends ApiController{

	protected $table = 'member';


	/* 用户登录接口 */
	public function login(){

		$post	= I('post.');
		// bug($post);
		$Member = D('Member');
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

	/* 	
	 * 注册接口
     */ 
	public function register(){

		$post = I('post.');
		$data = array();

		$data['district_id']= I('district_id','','cloud_id_parser');
		$data['username'] 	= I('username','','trim');
		$data['nickname'] 	= I('username','','trim');
		$data['idcard'] 	= I('idcard','','trim');
		$data['building_no']= I('building_no','','trim');
		$data['room_no'] 	= I('room_no','','trim');
		$data['type']		= I('type','0','int');

		if(empty($data['district_id'])){
			unset($data['district_id']);
		}
		if(empty($data['idcard'])){
			unset($data['idcard']);
		}
		if(empty($data['building_no'])){
			unset($data['building_no']);
		}
		if(empty($data['room_no'])){
			unset($data['room_no']);
		}
		if($data['type'] == 1){
			$data['register_code'] 	= I('register_code','','trim');
		}
		if($data['type'] == 2){
			/* 如果是业主身份登录则需进行审核 1-待审核状态 */
			$data['state'] 	= 1;
		}
		$data['sex'] 		= I('sex',1,'int');
		$data['password']	= I('password','');
		$data['mobile']		= I('mobile','','trim');

		/*验证短信验证码*/
		$Codo    = D('Code');
		$code_id = $Codo->check($post['mobile'],trim($post['code']),1);
		if(!is_numeric($code_id)){
			$this->api_error('验证码错误');
		}

		/*如果存在业主邀请码则查询该业主信息*/
		if(!empty($data['register_code'])){
			$pid = M('Member')->where(array('register_code'=>$data['register_code']))->find();
			if(empty($pid)){
				$this->api_error('邀请码不存在');
			}else{
				$data['pid'] = intval($pid['id']);
				$data['district_id'] = intval($pid['district_id']);
				$data['building_no'] = trim($pid['building_no']);
				$data['room_no'] 	 = trim($pid['room_no']);
				unset($data['register_code']);
			}
		}
		
		try {
			$M = M();
			$M->startTrans();

			$result = update_data(D('Member'),[],[],$data);		
			if($result < 1 || !is_numeric($result)){
				throw new \Exception($result);
			}
			// $res = M('member_count')->add(array('member_id'=>$result));
			// if(!is_numeric($res)){
			// 	throw new \Exception('数据异常,注册失败');
			// }
			$Codo->del($code_id);

		} catch (\Exception $e) {
			$M->rollback();
			$this->apiReturn(array('status'=>'0','msg'=>$e->getMessage()));
		}
		$M->commit();
		$this->apiReturn(array('status'=>'1','msg'=>'注册成功'));
	}


    /**
     * 微信公众号注册接口
     */
	public function weixinRegister(){
        $post = I('post.');
        $data = array();

        $data['district_id']= I('district_id','','cloud_id_parser');
        $data['username'] 	= I('username','','trim');
        $data['nickname'] 	= I('username','','trim');
        $data['idcard'] 	= I('idcard','','trim');
        $data['building_no']= I('building_no','','trim');
        $data['room_no'] 	= I('room_no','','trim');
        $data['county'] = I('countyName','','trim');
        $data['towns'] = I('townName','','trim');

        if(empty($data['district_id'])){
            unset($data['district_id']);
        }
        if(empty($data['idcard'])){
            unset($data['idcard']);
        }
        if(empty($data['building_no'])){
            unset($data['building_no']);
        }
        if(empty($data['room_no'])){
            unset($data['room_no']);
        }

        $data['sex'] 		= I('sex',1,'int');
        $data['age'] 		= I('age',0,'int');
        $data['password']	= sys_password_encrypt(I('password',''));
        $data['mobile']		= I('mobile','','trim');
        if(empty($data['username']))	{
            $this->apiReturn(array('status'=>'0','msg'=>'请输入昵称'));
        }
        if(empty($data['mobile']))	{
            $this->apiReturn(array('status'=>'0','msg'=>'请输入手机号'));
        }
        if(!is_mobile($data['mobile'])){
            $this->apiReturn(array('status'=>'0','msg'=>'手机号格式不正确'));
        }

        try {
            $M = M($this->table);
            $M->create($data);
            $M->add();
        } catch (\Exception $e) {
            $this->apiReturn(array('status'=>'0','msg'=>$e->getMessage()));
        }
        $this->apiReturn(array('status'=>'1','msg'=>'注册成功'));
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