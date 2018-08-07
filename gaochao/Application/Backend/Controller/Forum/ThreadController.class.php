<?php
namespace Backend\Controller\Forum;
/**
 * 主题管理
 * @author llf
 * @time 2017-09-08
 */
class ThreadController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table  = 'Forum_thread';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page($this->table,$map,'add_time desc');

		$result['forum'] = array_to_select(get_no_hid('forum_forum','add_time desc'), I('forum_id'));

		$this->assign($result);
		$this->display();
	}

	/**
	 * 列表 搜索
	 */
	private function _search(){

		$post 			   = I();
		$map 			   = array('is_del'=>0);
		$keyword 		   = I('keywords','','trim');
		$is_hid 		   = I('is_hid','','int');
		$forum_id 		   = I('forum_id','','int');

		if(!empty($keyword)){
			$map['title|member_nickname']   = array('LIKE',"%$keyword%");
		}
		if(!empty($is_hid)){
			$map['is_hid']	= $is_hid;
		}
		if(!empty($forum_id)){
			$forum = get_no_hid('forum_forum','sort desc');
			$forum_ids = array($forum_id);
			array_walk($forum, function($a) use(&$forum_ids,$forum_id){
				if($a['pid'] == $forum_id){
					$forum_ids[] = $a['id'];
				}
			});
			$map['forum_id'] = array('IN',$forum_ids);
		}
		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		return $map;
	}

	/**
	 * 添加
	 * @author llf
	 * @time 2016-05-31
	 */
	public function add(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}
	/**
	 * 编辑
	 * @author llf
	 * @time 2016-05-31
	 */
	public function edit(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}

	public function detail(){

		$this->operate();
	}
	
	/**
	 * 显示
	 * @author llf
	 * @time 2016-05-31
	 */
	protected function operate(){

		$id	  = I('ids',0,'int');
		$info = M($this->table)->where('id='.$id)->find();
		if(empty($info)){
			$this->error('主题信息不存在');
		}
		$condition	= array(
						'is_del'	=>0,
						'thread_id'	=>$id,
					);
		$info['images'] = M('forum_thread_image')->where($condition)->select();

		$data['info']   = $info;
		$data['forum']  = get_no_hid('forum_forum','add_time desc');

		$this->assign($data);
		$this->display('operate');
	}

	/**
	 * 推荐
	 * @author llf
	 * @time 2016-05-31
	 */
	public function recommend(){

		$ids = I('ids');

		if(empty($ids)){
			$this->error('请勾选需要操作的数据');
		}
		$map = array(
				'id'=>array('IN',$ids),
			);
		$res = M($this->table)->where($map)->setField('recommend',1);
		M($this->table)->where($map)->setField('recommend_time',date('Y-m-d H:i:s'));
		if(is_numeric($res) && $res){
			$this->success('操作成功');
		} else{
			$this->error('操作失败');
		}
	}

	/**
	 * 取消推荐
	 * @author llf
	 * @time 2016-05-31
	 */
	public function disrecommend(){

		$ids = I('ids');

		if(empty($ids)){
			$this->error('请勾选需要操作的数据');
		}
		$map = array(
				'id'=>array('IN',$ids),
			);
		$res = M($this->table)->where($map)->setField('recommend',0);
		if(is_numeric($res) && $res){
			$this->success('操作成功');
		} else{
			$this->error('操作失败');
		}
	}
}

