<?php
namespace Backend\Controller\Forum;
/**
 * 帖子管理
 * @author llf
 * @time 2017-09-08
 */
class PostController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table  = 'Forum_post';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page($this->table,$map,'add_time desc');

		$result['thread'] =  array_to_select(get_no_hid('forum_thread','sort desc'), I('thread_id'));

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
		$thread_id 		   = I('thread_id','','int');

		if(!empty($keyword)){
			$map['title|member_nickname']   = array('LIKE',"%$keyword%");
		}
		if(!empty($is_hid)){
			$map['is_hid']	= $is_hid;
		}
		if(!empty($thread_id)){
			$map['thread_id']	= $thread_id;
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

		$id   = I('ids',0,'int');
		$info = M($this->table)->where('id='.$id)->find();
		if(empty($info)){
			$this->error('主题信息不存在');
		}
		$info['post_info'] = unserialize($info['post_info']);
		$condition      = array(
							'is_del'	=>0,
							'post_id'	=>$id,
						);
		$info['images'] = M('forum_post_image')->where($condition)->select();

		$data['info']   = $info;
		$data['forum']  = get_no_hid('forum_forum','add_time desc');

		$this->assign($data);
		$this->display('operate');
	}
	
}

