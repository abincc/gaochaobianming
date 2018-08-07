<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;

class PublicController extends ApiController{
	
	
	//公共模块控制器
	public function index(){
		die('公共模块控制器');
	}

	//公共模块获取地区数据接口
	public function area(){

		$pid = I('pid','','int');
		// bug($pid);
		$area_cache = get_no_hid('area');
		if(empty($area_cache)){
			$this->apiReturn(array('status'=>'0','msg'=>'地区信息获取失败'));
		}

		if(I('pid') != ''){
			$data = array();
			foreach ($area_cache as $k => $v) {
				if($v['pid'] == $pid){
					$data[] = $v;
				}
			}
			$area_cache = $data;
		}

		$area_cache = array_values($area_cache);
		foreach ($area_cache as $key => $val) {
			unset($area_cache[$key]['group']);
			unset($area_cache[$key]['sort']);
			unset($area_cache[$key]['addtime']);
			unset($area_cache[$key]['is_hid']);
			unset($area_cache[$key]['is_del']);
			unset($area_cache[$key]['number']);
		}

		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>$area_cache));
	}


	//获取token
	public function get_token(){

		$ticket = I('ticket','','trim');

		if(empty($ticket)){
			$this->apiReturn(array('status'=>'0','msg'=>'ticket 参数必须'));
		}

		$Member = D('Member');
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

	//测试
	public function check_token(){

		$posts  = I('post.');
		$Member = D('Member');
		$result = $Member->check_token($posts);

		if(!$result){
			$status = '0';
			if(!empty($Member->status)){
				$status = $Member->status;
			}
			$this->apiReturn(array('status'=>$status,'msg'=>$Member->getError()));
		}

		die('OK');

	}

	/**
	* 获取保洁维修服务类型
	* @author llf
	* @time 2016-05-31
	*/
	public function get_clean_service(){

		$data = get_no_del('clean_service','sort desc,id asc');
		if(empty($data)){
			$this->apiReturn(array('status'=>'0','msg'=>'保洁维修服务类型信息获取失败'));
		}
		foreach($data as $k=>$v){
			$data[$k]['clean_service_id'] = $v['id'];
			unset($data[$k]['id']);
			unset($data[$k]['sort']);
			unset($data[$k]['is_del']);
			unset($data[$k]['is_hid']);
			unset($data[$k]['add_time']);
		}
		$data = array_values($data);
		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>$data));
	}

   /**
	* 获取小事服务类型
	* @author llf
	* @time 2016-05-31
	*/
	public function get_trifle_service(){

		$data = get_no_del('trifle_service','sort desc,id asc');
		if(empty($data)){
			$this->apiReturn(array('status'=>'0','msg'=>'小事服务类型信息获取失败'));
		}

		foreach($data as $k=>$v){
			$data[$k]['trifle_service_id'] = $v['id'];
			unset($data[$k]['id']);
			unset($data[$k]['sort']);
			unset($data[$k]['is_del']);
			unset($data[$k]['is_hid']);
			unset($data[$k]['add_time']);
		}
		$data = array_values($data);
		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>$data));
	}

   /**
	* 获取关于我们页面地址
	* @author llf
	* @time 2016-05-31
	*/
	public function get_about_url(){
		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>U('Home/Index/Index/about',[],true,true)));
	}

   /**
	* 获取服务电话
	* @author llf
	* @time 2016-05-31
	*/
	public function get_service_telphone(){
		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>C('US_TELPHONE')));
	}
}

