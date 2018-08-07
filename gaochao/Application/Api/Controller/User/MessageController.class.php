<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 消息模块接口
	 */
class MessageController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table 		='Message';
	protected $type			= 0;

	/**
	 * 消息列表
	 * @time 2017-09-08
	 * @author llf
	 */
	public function index() {

		$post 		= I();
		$member_id  = $this->member_id;
		$map  		= array('member_id'=>$member_id,'is_del'=>0,'is_hid'=>0); 
		$keyword 	= trim($post['keyword']);

		if(!empty($this->$type)){
			$map['type']  = $this->type;
		}
		if(!empty($keyword)){
			$map['title'] = array('LIKE',array("%$keyword%"));
		}
        $start = !empty($post['start_date'])?$post['start_date']:0;
        $end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
        $map['add_time'] = array('BETWEEN',array($start,$end));

        $field = array('id as message_id,title,is_read,add_time');
		$list  = $this->page($this->table,$map,'id desc',$field);

		// bug($map);

		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>(array)$list));
	}

	/**
	 * 订单消息列表
	 * @time 2017-09-08
	 * @author llf
	 */
	public function order(){
		$this->type = 2;
		$this->index();
	}

	/**
	 * 物业消息列表
	 * @time 2017-09-08
	 * @author llf
	 */
	public function property(){
		$this->type = 1;
		$this->index();
	}

	/**
	 * 订单消息列表
	 * @time 2017-09-08
	 * @author llf
	 */
	public function arrears(){
		$this->type = 3;
		$this->index();
	}

	/**
	 * 消息详情
	 * @time 2017-09-08
	 * @author llf
	 */
	public function info(){

		$map  = array(
			'id'	=>intval(I('message_id',0,'int')),
			'is_del'=>0,
			'is_hid'=>0,
			'member_id'=>$this->member_id,
			);
		$info = get_info($this->table,$map,'id as message_id,type,is_read,title,content,add_time');
		if(empty($info)){
			$this->apiReturn(array('status'=>'0','msg'=>'消息不存在'));
		}
		//标记已读
/*	 	$map = array(
				'id' =>intval($info['message_id']),
	 		);
	 	M($this->table)->where($map)->setField('is_read',1);*/
	 	if(!$info['is_read']){
			D($this->table)->set_read($this->member_id,$info['type'],$info['message_id']);
	 	}

	 	unset($info['type']);
	 	unset($info['is_read']);
		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>$info));
	}

	/**
	 * 标记已读-- 弃用
	 * @time 2017-09-08 
	 * @author llf 
	 */
	 public function set_read(){

	 	$this->apiReturn(array('status'=>'0','msg'=>'接口弃用'));

	 	$ids = I('post.message_ids');
	 	if(empty($ids)){
	 		$this->apiReturn(array('status'=>'0','msg'=>'参数错误'));
	 	}
	 	$map = array(
	 			'member_id'	=>$this->member_id,
	 			'is_del'	=>0,     
	 		);
	 	if(is_numeric($ids)){
	 		$map['id'] 		= intval($ids);
	 	}
	 	if(is_array($ids)){
	 		$map['id']		= array('IN',$ids);
	 	}
	 	$result = M($this->table)->where($map)->setField('is_read',1);

	 	if(is_numeric($result) && $result){
	 		$this->apiReturn(array('status'=>'1','msg'=>'操作成功'));
	 	}else{
	 		$this->apiReturn(array('status'=>'0','msg'=>'操作失败'));
	 	}
	 }

	/**
	 * 删除 -- 弃用
	 * @time 2017-09-08
	 * @author llf
	 */
	public function del(){

		$this->apiReturn(array('status'=>'0','msg'=>'接口弃用'));
	 	$ids = I('message_ids');
	 	if(empty($ids)){
	 		$this->apiReturn(array('status'=>'0','msg'=>'参数错误'));
	 	}
	 	$map = array(
	 			'member_id'	=>$this->member_id,
	 			'is_del'	=>0,
	 			'is_read'	=>1,     
	 		);
	 	if(is_numeric($ids)){
	 		$map['id'] 		= intval($ids);
	 	}
	 	if(is_array($ids)){
	 		$map['id']		= array('IN',$ids);
	 	}

	 	$result = M($this->table)->where($map)->setField('is_hid',1);

	 	if(is_numeric($result) && $result){
	 		$this->apiReturn(array('status'=>'1','msg'=>'操作成功'));
	 	}else{
	 		$this->apiReturn(array('status'=>'0','msg'=>'操作失败'));
	 	}

	 }

}
