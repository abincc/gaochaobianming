<?php
namespace User\Controller\Account;
/**
 * 忘记密码
 */
class ForgetController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table='member';

	/*
	 * 忘记密码
	 * @time 2016-03-17
	 * @author 康利民 <3027788306@qq.com>
	 */
	public function step_1() {
		if(session('member_id')){
			header("location:".U('User/Index/Index/index'));
		}
		if(!IS_POST){
			session("valid_account",false);
			return $this->display();
		}
		
		$mobile = trim(I('account'));
		if(!session('mobile_code')){
			$this->error('请先获取验证码');
		}
		if(session('mobile_code') != I('verify')){
			$this->error('验证码错误');
		}
		/*验证手机号*/
		if(!preg_match(MOBILE, $mobile)){
			$this->error('手机号格式有误');
		}
		if(session('mobile_key') != $mobile){
			$this->error('请重新获取验证码');
		}
		$info=get_info('member',array('mobile'=>$mobile));
		if(!$info['mobile']){
			$this->error('账号不存在');
		}
		session("valid_account",$info['id']);
		$this->success("验证通过",U("step_2"));
	}
	/**
	 * 设置新密码
	 * @流程分析
	 * 1、如果没有STEP_1 是不能访问到这个页面的
	 * @time 2016-03-17
	 * @author 康利民 <3027788306@qq.com>
	 */
	public function step_2() {
		if(!session("valid_account")){
			header("location:".U('step_1'));
		}
		if(!IS_POST){
			return $this->display();
		}
		/*将新密码存入member表中*/
		$data['id'] = session("valid_account");
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
			$this->success("新密码设置成功",U("step_3"));
		}else{
			$this->error($result);
		}
	}
	/*
	 * 密码修改成功后的提示页面
	 * @time 2016-03-21
	 * @author 康利民 <3027788306@qq.com>
	 */
	public function step_3() {
		$this->display ();
	}
}
