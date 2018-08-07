<?php
namespace User\Controller\Account;
/**
 * 注册
 */
class RegisterController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table='member';

	/**
	 * 判断用户是否存在
	 */
	public function check_username(){
		$username = trim(I('post.param')) ? trim(I('post.param')) : trim(I('post.account'));
		if(!IS_POST || !$username){
			$this->error('非法操作');
		}
		/*如果后台删除了该用户，该手机号或邮箱还是可以作为新账号继续注册*/
		$map = array(
			'mobile'=>$username,
			'is_del'=>0,  
			'is_hid'=>['in','0,1'], 
		);
		$info = get_info('member',$map);
		if(!$info['id']){
			if(trim(I('post.param'))){
				$this->success("用户名合法");
			}
		}else{
			$this->error('用户名已存在');
		}
	}
	/**
	 * 不同用户注册分类入口
	 */
	public function register_choice() {
		if(session('member_id')){
	 		header("location:".U('/'));
		}
		$this->display();
	}

	/*
	 * 注册页
	 * @time 2016-03-17
	 * @author 康利民 <3027788306@qq.com>
	 */
	public function index() {
		if(session('member_id')){
	 		header("location:".U('/'));
		}
		if(!IS_POST){
			return $this->display();
		}
		/*判断用户是否存在*/
		$this->check_username();
		$data = I();
		$data['password_1'] = I('password');
		$salt = get_rand_char(6);
		$data['salt'] = $salt;
		$data['password'] = md5(md5(I('password')).$salt);
			/*用户注册信息存入member表*/
		$data['register_time'] = time();
		$data['register_ip'] = get_client_ip();
		$data['mobile'] = I('account');

		$rules=array(
			//array('mobile,is_hid,is_del','','用户名已存在',1,'unique'),
			array('reader',1,'不同意用户服务协议无法注册',1,'equal'),
			array('verify',session('mobile_code'),'验证码不正确',1,'equal'),
			array('account',session('mobile_key'),'请重新获取验证码',1,'equal'),
			array('mobile',MOBILE,'请填写正确的手机号码！',1,'regex'),
			array('password_1','6,16','请输入6-16位密码',0,'length'),
			array('repassword','password_1','确认密码不正确',0,'confirm'),
		);

		$result=update_data($this->table,$rules, array(),$data);

		if(is_numeric($result)){
			update_data('member_info', array(), array(), array('member_id' => $result));
			session(null);
			/*注册成功后将用户ID、信息存入session*/
			session("member_id",$result);
			$data['id'] = $result;
			session("member_info",$data);
			$this->success("注册成功",U('/'));
		}else{
			$this->error($result);
		}
	}
	
	
}
