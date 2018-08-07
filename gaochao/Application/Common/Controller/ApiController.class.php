<?php
namespace Common\Controller;
use Think\Controller;
use Think\Crypt;

class ApiController extends Controller {

	/**
	 * 前台控制器初始化
	 */
	protected function _initialize() {
		/* 读取配置 */
		if (! F ( 'config' )) {
			$config = M ( 'config' )->getField ( 'name,value' );
			F ( 'config', $config );
		} else {
			$config = F ( 'config' );
		}
		C ( $config ); // 合并配置参数到全局配置
		$this->__autoload ();
	}
    
	/**
	 * 自动加载
	 */
	protected function __autoload(){
            
	}
	/*
	 * 分页功能
	 * @time 2014-12-26
	 * @author 郭文龙 <2824682114@qq.com>
	 */
	public function page($model,$map=array(),$order='',$field=array(),$limit='',$page='',$group = ''){
		if(is_string($model)) $model  = M($model);
		if(!$limit)           $limit  = $_REQUEST['r']?$_REQUEST['r']:20;
		if(!$page) 			  $page   = intval($_REQUEST['p']);

		/* 进行分页数据查询 注意page方法的参数的前面部分是当前的页数使用 $_GET[p]获取 */
		$list = $model->where($map)->field($field)->order($order)->group($group)->page("$page,$limit")->select();
		session('sql',$model->getLastSql());
		$data['count']=$count=$model->where($map)->count();   /* 查询满足要求的总记录数 */
		$data['page_count'] = ceil($count/$limit);         	   /* 计算总页码数 */
		$Page       	= new \Think\Page($count,$limit);  		   /* 实例化分页类 传入总记录数和每页显示的记录数 */
		$Page->rollPage = 7;
		$data['page']   = $Page->show();  				   /* 分页显示输出 */
		$this->assign($data); 								   /* 赋值分页输出 */
		return $list;
	}
	
	
	/* 单文件上传 站内上传 tp默认不允许上传到更目录外去*/
    public function upload_files($files,$config){  
   
   
/*      $config = array(    
			'maxSize'    =>    3145728,    
			'savePath'   =>    './Public/Uploads/',    
			'saveName'   =>    array('uniqid',''),    
			'exts'       =>    array('jpg', 'gif', 'png', 'jpeg'),    
			'autoSub'    =>    true,    
			'subName'    =>    array('date','Ymd'),
		); */
		$upload = new \Think\Upload($config);        /* 实例化上传类 */	
		
		/* 上传单个文件      */
		$info   =   $upload->upload($files);
		if(!$info) {
			$this->error = $upload->getError();
			return false;
		}else{
			return $info;
		}
	}
	

	public function upload_one($image,$path){    
		
		$upload = new \Think\Upload();
		$upload->rootPath  = 	 './';
		$upload->maxSize   =     3145728 ;  
		$upload->exts      =     array('jpg', 'gif', 'png', 'jpeg');    
		$upload->savePath  =     $path; 
		$upload->saveName  =     array('uniqid','' ); 
		$upload->replace   =     false; 
		$upload->subName   =     ''; 

		$info   =   $upload->uploadOne($_FILES[$image]);
		if(!$info) {
			$this->error($upload->getError());    
			return false;
		}else{    
			return $info['savepath'].$info['savename'];    
		}
	}

	
	
	/**
	 * 判断用户登录时间是否过期
	 * @param     string   $key   md5加密的登录时间
	 * @param     int      $id    数据id
	 * @author    陆龙飞
	 * @date      2015-09-30
	 * @return    bool
	 * */
	public function isLoginExpire($key,$id){

		if(empty($key) || !is_numeric($id)) return false;
		
		$map 	= array('id'=>intval($id));
		$result = get_info('member',$map,$field="login_time");
		
		if(empty($result)) return false;
		if($key !== $this->get_user_key($id,$result['login_time'])){
			return false;
		}else{
			return true;
		}
	}
  
	/*
	* 判断是否为app，和用户是否同时登录
	* @time 2015-11-11
	* @author	 llf  <747060156@qq.com>
	* 1.登录     参数  apitype,key,home_member_id
	* 2.不用登录 参数  apitype
	*/
	public function auth($posts){

		$Member 		 = D('Member');
		$this->member_id = $Member->check_token($posts);
		if(!$this->member_id){
			$status = '0';
			if(!empty($Member->status)){
				$status = $Member->status;
			}
			$this->apiReturn(array('status'=>$status,'msg'=>$Member->getError()));
		}
		
	} 

	
	/* 接口封装输出 */
	public function apiReturn($data,$type='json'){
		
		$defult = array('status'=>'0','msg'=>'error !');
		if(!isset($data)){
			$data = $defult; 
		}else{
		    $data = array_merge($defult,$data);	
		}	
		/* 匿名函数递归*
		$filter = function(&$a) use(&$filter){
			if($a === null) 	$a = '';		//过滤null
			if(is_numeric($a))  $a = strval($a);//过滤数值型为字符串
			if(is_array($a) && !empty($a)){		//过滤子集数组中的以上过滤项
				array_walk($a,$filter);
			}
		}; */
		array_walk_recursive($data,function(&$a) use(&$data){
			// if(is_array($a) && empty($a)) 	$a = null;
			if(is_numeric($a))  			$a = strval($a);
			if($a === null) 				$a = '';
		});	
		$this->ajaxReturn($data,$type);
	}


	/**
	 * 生成批量插入sql
	 * @param     data     二维数组
	 * @param     table    表名
	 * @author    陆龙飞
	 * @date      2016-01-19
	 * @return    string
	 * */
	 
	public function addSql($data,$table){
		if(empty($data) || empty($table))  return '';
		$value = implode('`,`',array_keys($data[0]));
		$sql   = 'INSERT INTO `'.$table.'` (`'.$value.'`) VALUES ';
		foreach($data as $val){
			$sql .= "(";
			foreach($val as $k=>$v){
				is_numeric($v)?$sql .= $v : $sql .= "'".$v."'";
				$sql .= ',';
			}
			$sql = rtrim($sql,',');
			$sql .= '),';
		}
		$sql = rtrim($sql,',');
		
		return $sql;
	}


	/**
	 * 公共json返回--成功
	 * @param     msg   	string   消息内容
	 * @param     info      array    数据项
 	 * @param     status    int      返回状态值
	 * @date      2017-08-22
	 * @return    string    json
	 * */
	public function api_success($msg='success',$info=null,$status='1'){
		$data = array(
				'status'	=> $status,
				'msg'		=> $msg,
			);
		if(!is_null($info)){
			$data['info'] = $info;
		}
		$this->apiReturn($data);
	}

	/**
	 * 公共json返回--失败
	 * @param     msg   	string   消息内容
	 * @param     info      array    数据项
 	 * @param     status    int      返回状态值
	 * @date      2017-08-22
	 * @return    string    json
	 * */
	public function api_error($msg='error',$info=null,$status='0'){
		$data = array(
				'status'	=> $status,
				'msg'		=> $msg,
			);
		if(!is_null($info)){
			$data['info'] = $info;
		}
		$this->apiReturn($data);
	}
}