<?php
namespace User\Controller\Message;
/**
 * 收件箱
 */
class ReceiveController extends IndexController{
	protected $table_send = 'message_send';
	protected $table_receive = 'message_receive';

	public function index(){
		$this->display();
	}
	/**
	 * 系统消息收件箱
	 */
	public function system(){
		$map = array();		$map['member_id'] = session('member_id');
		//$keywords=trim(I('keyword'));
		//$map['title']=array('like',"%$keywords%");
		$start = I('start') ? strtotime($get['start']) : 0;
		$end = I('end') ? strtotime($get['end'].'23:59:59') : time();
		$map['addtime'] = array('between',$start.','.$end);

		$data = $this->page($this->table_receive,$map,'addtime desc');
		unset($map['addtime']);
		$map['is_read'] = 0;
		$data['no_read'] = M($this->table_receive)->where($map)->count();
		$this->assign($data);
		$this->display();
	}


	/**
	 * 系统消息收件箱详情
	 */
	public function system_detail(){
		$id = I('get.id');
		if(!$id){
			$this->redirect('system');
		}else{
			$map['id'] = $id;
			$map['member_id'] = session('member_id');
			$data['info'] = get_info($this->table_receive,$map);
			if(!$data['info']['id']){
				$this->redirect('system');
			}
			/*标记消息已读*/
			update_data($this->table_receive,array(),$map,array('is_read'=>1));
			$this->assign($data);
			$this->display();
		}
	}

	/**
	 * 删除消息
	 */
	public function del(){
		$this->set_status('is_del','1');
	}

	/**
	 * 标记已读
	 * @return [type] [description]
	 */
	public function mark_read(){
		$this->set_status('status','1');
	}
	/**
	 * 修改表状态
	 * @param string $field [description]
	 * @param string $value [description]
	 */
	public function set_status($field='',$value=''){
		$ids = I('ids');
		if(!$ids){
			$this->error('请选择要操作的数据');
		}
		if(is_array($ids)){
			$map['msg_id']=array('in',$ids);
		}else{
			$map['msg_id']=intval($ids);
		}
		$map['member_id']=session('member_id');
		
		$result=update_data($this->table_receive,'',$map,array($field=>$value));

		if($result){
			$this->success('操作成功',U('system'));
		}else{
			$this->error('操作失败！');
		}
	}
}
