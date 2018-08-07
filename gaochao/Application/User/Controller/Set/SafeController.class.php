<?php
namespace User\Controller\Set;
/**
 * 安全设置
 */
class SafeController extends IndexController { 
	/**
	 * 表名
	 * @var string
	 */
	protected $table='member';
	
	/**
	 * 列表
	 * @author 秦晓武
	 * @time 2016-08-16
	 */
	public function index(){
		$this->display();
	}

	
	/**
	 * 通过手机修改登录密码
	 */
	public function login_password_mobile(){
		if(!IS_POST){
			return $this->display();
		}
		$member_info = session('member_info');
		if(!session('mobile_code')){
			$this->error('请先获取验证码');
		}
		if(session('mobile_code') != I('verify')){
			$this->error('验证码错误');
		}
		/*验证手机号*/
		if(session('mobile_key') != $member_info['mobile']){
			$this->error('请重新获取验证码');
		}
		/*将新密码存入member表中*/
		$data['id'] = $member_info['id'];
		$data['password_1'] = I('password');
		$data['password_2'] = I('repassword');
		$rules=array(
			array('password_1','6,16','请输入6-16位密码','0','length'),
			array('password_2','password_1','确认密码不正确',0,'confirm'),
		);

		$salt = get_rand_char(6);
		$data['salt'] = $salt;
		$data['password'] = md5(md5(I('password')).$salt);
		$result = update_data($this->table, $rules, array(), $data);
	 
		if(is_numeric($result)){
			session(null);
			$this->success("修改成功，请重新登录",U('User/Account/Member/login'));
		}else{
			$this->error($res);
		}
	}
	
	/**
	 * 通过邮箱修改登录密码
	 */
	public function login_password_email(){
		if(!IS_POST){
			return $this->display();
		}
		$member_info = session('member_info');
		if(!session('email_code')){
			$this->error('请先获取验证码');
		}
		if(session('email_code') != I('verify')){
			$this->error('验证码错误');
		}
		/*验证邮箱*/
		if(session('email_key') != $member_info['email']){
			$this->error('请重新获取验证码');
		}
		/*将新密码存入member表中*/
		$data['id'] = $member_info['id'];
		$data['password_1'] = I('password');
		$data['password_2'] = I('repassword');
		$rules=array(
			array('password_1','6,16','请输入6-16位密码','0','length'),
			array('password_2','password_1','确认密码不正确',0,'confirm'),
		);

		$salt = get_rand_char(6);
		$data['salt'] = $salt;
		$data['password'] = md5(md5(I('password')).$salt);
		$result = update_data($this->table, $rules, array(), $data);
	 
		if(is_numeric($result)){
			session(null);
			$this->success("修改成功，请重新登录",U('User/Account/Member/login'));
		}else{
			$this->error($res);
		}
	}
	/**
	 * 通过邮箱修改登录密码
	 */
	public function login_password_deal(){
		if(!IS_POST){
			return $this->display();
		}
		$member_info = get_info($this->table, array('id'=>session('member_info')['id']));
		if($member_info['deal_password'] != md5(md5(I('deal_password')).$member_info['deal_salt'])){
			$this->error('支付密码错误');
		}
		/*将新密码存入member表中*/
		$data['id'] = $member_info['id'];
		$data['password_1'] = I('password');
		$data['password_2'] = I('repassword');
		$rules=array(
			array('password_1','6,16','请输入6-16位密码','0','length'),
			array('password_2','password_1','确认密码不正确',0,'confirm'),
		);

		$salt = get_rand_char(6);
		$data['salt'] = $salt;
		$data['password'] = md5(md5(I('password')).$salt);
		$result = update_data($this->table, $rules, array(), $data);
	 
		if(is_numeric($result)){
			session(null);
			$this->success("修改成功，请重新登录",U('User/Account/Member/login'));
		}else{
			$this->error($res);
		}
	}
	
	/**
	 * 通过手机修改支付密码
	 */
	public function deal_password_mobile(){
		if(!IS_POST){
			return $this->display();
		}
		$member_info = session('member_info');
		if(!session('mobile_code')){
			$this->error('请先获取验证码');
		}
		if(session('mobile_code') != I('verify')){
			$this->error('验证码错误');
		}
		/*验证手机号*/
		if(session('mobile_key') != $member_info['mobile']){
			$this->error('请重新获取验证码');
		}
		/*将新密码存入member表中*/
		$data['id'] = $member_info['id'];
		$data['password_1'] = I('password');
		$data['password_2'] = I('repassword');
		$rules=array(
			array('password_1','6,16','请输入6-16位密码','0','length'),
			array('password_2','password_1','确认密码不正确',0,'confirm'),
		);

		$salt = get_rand_char(6);
		$data['deal_salt'] = $salt;
		$data['deal_password'] = md5(md5(I('password')).$salt);
		$result = update_data($this->table, $rules, array(), $data);
	 
		if(is_numeric($result)){
			session('mobile_code',null);
			$this->success("修改成功",U('index'));
		}else{
			$this->error($res);
		}
	}
	
	/**
	 * 修改绑定手机
	 */
	public function change_mobile(){
		if(!IS_POST){
			return $this->display();
		}
		
		$member_info = session('member_info');
		if(!session('mobile_code')){
			$this->error('请先获取验证码');
		}
		if(session('mobile_code') != I('verify')){
			$this->error('验证码错误');
		}
		/*验证手机号*/
		if(session('mobile_key') != $member_info['mobile']){
			$this->error('请重新获取验证码');
		}
		session('mobile_code',null);
		session('mobile_key',null);
		session('change_mobile',true);
		$this->success("成功",U('bind_mobile'));
	}

	/**
	 * 绑定手机
	 */
	public function bind_mobile(){
		$member_info = get_info($this->table, array('id'=>session('member_id')));
		/*有手机必须先验证*/
		if($member_info['mobile'] && !session('change_mobile')){
			$this->redirect('change_mobile');
		}
		if(!IS_POST){
			return $this->display();
		}
		if(!session('mobile_code')){
			$this->error('请先获取验证码');
		}
		if(session('mobile_code') != I('verify')){
			$this->error('验证码错误');
		}
		
		/*验证手机号*/
		if(session('mobile_key') != I('mobile')){
			$this->error('请重新获取验证码');
		}
		$data['id'] = $member_info['id'];
		$data['mobile'] = I('post.mobile');
		$rules = array(
			array('mobile',MOBILE,'请填写手机号',1,'regex'),
			array('mobile','','手机号已存在',0,'unique')
		);
		$result = update_data($this->table,$rules,array(),$data);
		if(is_numeric($result)){
			session('change_mobile',false);
			$this->success("成功",U('index'));   
		}else{
			$this->error($result);
		}
	}
	/**
	 * 修改绑定邮箱
	 */
	public function change_email(){
		if(!IS_POST){
			return $this->display();
		}
		
		$member_info = session('member_info');
		if(!session('email_code')){
			$this->error('请先获取验证码');
		}
		if(session('email_code') != I('verify')){
			$this->error('验证码错误');
		}
		/*验证手机号*/
		if(session('email_key') != $member_info['email']){
			$this->error('请重新获取验证码');
		}
		session('email_code',null);
		session('change_email',true);
		$this->success("成功",U('bind_email'));
	}
	/**
	 * 绑定邮箱
	 */
	public function bind_email(){
		$member_info = get_info($this->table, array('id'=>session('member_id')));
		/*有手机必须先验证*/
		if($member_info['email'] && !session('change_email')){
			$this->redirect('change_email');
		}
		if(!IS_POST){
			return $this->display();
		}
		if(!session('email_code')){
			$this->error('请先获取验证码');
		}
		if(session('email_code') != I('verify')){
			$this->error('验证码错误');
		}
		
		/*验证手机号*/
		if(session('email_key') != I('email')){
			$this->error('请重新获取验证码');
		}
		$data['id'] = $member_info['id'];
		$data['email'] = I('post.email');
		$rules = array(
			array('email',EMAIL,'请填写邮箱'),
			array('email','','邮箱已存在',0,'unique')
		);
		$result = update_data($this->table,$rules,array(),$data);
		if(is_numeric($result)){
			session('change_email',false);
			$this->success("成功",U('index'));   
		}else{
			$this->error($result);
		}
	}
}
