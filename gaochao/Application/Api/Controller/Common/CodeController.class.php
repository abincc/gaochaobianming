<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;


class CodeController extends ApiController{
	

   /**
	* 发送手机验证码
	* @author llf
	* @time  2017-06-06
	* @param mobile     手机号
	* @param type       业务类型    1-注册业务  2-找回密码业务 3-修改原始手机 4-修改新手机
	*/
	public function send_mobile_code(){

		$type	  = I('type',0,'int');
		$mobile	  = I('mobile','','trim');
		$code	  = rand(1000,9999);

		/* 验证业务类型*/
		if(empty($mobile))		$this->apiReturn(array('status'=>'0','msg'=>'手机号不能为空'));
		if(!is_mobile($mobile)){

			$this->apiReturn(array('status'=>'0','msg'=>'手机号格式不正确'));
		}

		/* 验证业务类型*/
		if(!$type || !in_array($type,array(1,2,3,4))){
			$this->apiReturn(array('status'=>'0','msg'=>'业务类型不存在'));
		}

		/* 判断是否频繁请求*/
		$condition = array(
				'mobile'	=>$mobile,
				'add_time'	=>array('EGT',date('Y-m-d H:i:s',(time()-60))),
			);
		$near_send = D('Code')->where($condition)->getField('id');
		if(!empty($near_send)){
			$this->api_error('请勿频繁请求');
		}
		$has = M('Member')->where(array('mobile'=>$mobile,'is_del'=>0))->find();
		switch ($type) {
			case 1:
			case 4:
				if($has){
					$this->apiReturn(array('status'=>'0','msg'=>'该手机已注册'));
				}
				break;
			case 2:
			case 3:
				if(!$has){
					$this->apiReturn(array('status'=>'0','msg'=>'该手机尚未已注册'));
				}
				if($has['is_hid']){
					$this->apiReturn(array('status'=>'0','msg'=>'该手机被禁用'));
				}
				# code...
				break;
			default:
				$this->apiReturn(array('status'=>'0','msg'=>'业务类型处理错误'));
				break;
		}

		if(!send_sms($mobile,'SMS_91135056',$type ,$code ,$has['id'])){
			$this->apiReturn(array('status'=>'0','msg'=>'发送失败'));
		}
		$this->apiReturn(array('status'=>'1','msg'=>'发送成功'));
	
	}


	public function test(){

		send_sms('13377850432','SMS_91135056',1,'123456',6); //发送短信
	}

}