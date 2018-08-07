<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

class IndexController extends BaseController{
	

   /**
	* 用户个人中心
	* @author llf
	* @dtime 2017-06-06
	*/
	public function index(){


		//获取头像

		//获取用户昵称

		//获取消息数量

		//获取订单数量
		// die(get_avatar(6));

		$data 		= array();
		$data['headimg'] 	= get_avatar($this->member_id);
		$data['nickname']	= M('member')->where('id='.$this->member_id)->getField('nickname');
		$field 		= array('message_forum','message_order','message_property','message_arrears');
		$message 	= M('member_count')->where('member_id='.$this->member_id)->field($field)->find();

		$data['message_forum'] 		= intval($message['message_forum']);
		$data['message_order'] 		= intval($message['message_order']);
		$data['message_property'] 	= intval($message['message_property']);
		$data['message_arrears'] 	= intval($message['message_arrears']);

		$this->api_success('ok',$data);
		// $this->apiReturn(array('status'=>'0','msg'=>'=='));
	
	}


	/**
     * 获取app 版本号
     */
	public function get_version_info(){
        $versionCode 	= M('config')->where(array('name'=>'APP_VERSION'))->getField('value');
        $data 		= array();
        $data['code'] = $versionCode;
        $this->api_success('ok',$data);
    }

}