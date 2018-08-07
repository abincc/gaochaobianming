<?php
/**
 * 论坛 - 无需登录
 */
namespace Api\Controller\Forum;

use Api\Org\Forum\Forum;

class ForumPublicController extends IndexPublicController{
	protected $obj;
	/**
	 * 初始化
	 * {@inheritDoc}
	 * @see \Api\Controller\Base\HomeController::__init()
	 */
	protected function __init(){
		parent::__init();
		$this->obj = new Forum();
	}
	/**
	 * 主页
	 */
	public function index() {
		$_data = [
			'ad'=> [],
			'forum'=> [],
			'list'=> [],
		];
		
		$_map = ['is_del'=>0,'is_hid'=>0];
				
		/* 广告 */
		$_data['ad'] = $this->obj->_ad();
		/* 推荐版块 */
		$_data['forum'] = $this->obj->_forum_recommend();
		/* 推荐主图 */
		// $_map = $this->obj->thread_map_recommend();
		$_order = 'add_time desc,id desc';
		$_field = 'id,forum_id,member_id,member_nickname,title,content,views,replies,likes,lastpost_time,add_time';
		$list = $this->page($this->obj->table_thread, $_map, $_order, $_field);
		if($list) $_data['list'] = $this->obj->thread_list($list);
		
		$this->api_success('ok', $_data);
	}
	/**
	 * 全部版块
	 */
	public function forum_list() {
		$this->api_success('ok', $this->obj->forum_list());
	}
	/**
	 * 搜索
	 */
	public function search() {
		$_data = [
			'forum_list'=> [],
			'thread_list'=> [],
		];

		$_data['forum_list'] = $this->obj->search_forum();
		$_map = $this->obj->search_forum_thread();
		$_order = 'views desc,replies desc,likes desc,lastpost_time desc,add_time desc,id desc';
		$_field = 'id,forum_id,member_id,member_nickname,title,content,views,replies,likes,add_time';
		$list = $this->page($this->obj->table_thread, $_map, $_order, $_field);
		if($list) $_data['thread_list'] = $this->obj->search_list($list);
		
		$this->api_success('ok', $_data);
	}
	/**
	 * 版块主页
	 */
	public function forum_index() {
		$_data = [];
		
		if($_data = $this->obj->forum_index()) {
			$this->api_success('ok', $_data);
		}
		$this->api_error('版块信息不存在');
	}
	/**
	 * 全部主题
	 */
	public function thread_all() {
		$_data = [];
		if($forum_info = $this->obj->forum_index()) {
			$_map = $this->obj->thread_map_all($forum_info['id']);
			$_order = 'lastpost_time desc,views desc,replies desc,likes desc,add_time desc,id desc';
			$_field = 'id,forum_id,member_id,member_nickname,title,content,views,replies,likes,lastpost_time,add_time';
			$list = $this->page($this->obj->table_thread, $_map, $_order, $_field);
			if($list) $_data = $this->obj->thread_list($list);
			
			$this->api_success('ok', $_data);
		}
		$this->api_error('版块信息不存在');
	}
	/**
	 * 精华帖
	 */
	public function thread_digest() {
		$_data = [];
		
		if($forum_info = $this->obj->forum_index()) {
			$_map = $this->obj->thread_map_digest($forum_info['id']);
			$_order = 'lastpost_time desc,views desc,replies desc,likes desc,add_time desc,id desc';
			$_field = 'id,forum_id,member_id,member_nickname,title,content,views,replies,likes,lastpost_time,add_time';
			$list = $this->page($this->obj->table_thread, $_map, $_order, $_field);
			if($list) $_data = $this->obj->thread_list($list);
			
			$this->api_success('ok', $_data);
		}
		$this->api_error('版块信息不存在');
	}
	/**
	 * 主题详情
	 */
	public function thread_detail() {
		$_data = [];
		
		$member = D('Member');
		if($_data = $this->obj->thread_detail($member->check_token(I('get.')))) {
			$this->api_success('ok', $_data);
		}

		$this->api_error('主题信息不存在');
	}

	/**
	 * 主题分享
	 */
	public function share_detail(){//184
		$_data = [];
		
		$member = D('Member');
		if($_data = $this->obj->thread_detail($member->check_token(I('get.')))) {

			$data = array(
					'id'		=> $_data['id'],
					'title' 	=> $_data['title'],
					'content'	=> msubstr($_data['content'],0,36),
					'image'		=> $_data['image_list'][0]['image'],
					'url'		=> U('Home/Index/Index/thread_share_info',array('id'=>$_data['id']),true,true),
				);

			$this->api_success('ok', $data);
		}
		$this->api_error('主题信息不存在');
	}

	/**
	 * 帖子列表
	 */
	public function post_list() {
		$_data = [
			'post_list'=> [],
		];
		
		if($thread_info = $this->obj->post_thread()) {
			$_map = $this->obj->post_map_list($thread_info['id']);
			$_field = 'id,post_id,member_id,member_nickname,content,likes,position,post_info,add_time';
			$list = $this->page($this->obj->table_post, $_map, 'add_time asc', $_field);
			$member = D('Member');
			if($list) $_data['post_list'] = $this->obj->post_list($list, $member->check_token(I('get.')));
			
			$this->api_success('ok', $_data);
		}
		$this->api_error('主题信息不存在');
	}
	/**
	 * 个人主页
	 */
	public function home() {
		$_data = [];
		if($info = $this->obj->verify_member('id,nickname,signature,register_time,login_time')) {
			$member_count = $this->obj->get_memeber_count($info['id'], 'threads,posts,likes');
			$_data = array_merge($info, $member_count);
			$_data['login_time'] = date('Y-m-d H:i', strtotime($_data['login_time']));
			$_data['register_time'] = date('Y-m-d H:i', strtotime($_data['register_time']));
			$_data['avatar'] = get_avatar($info['id'], 'L');
			
			$this->api_success('ok', $_data);
		}
		$this->api_error('用户信息不存在');
	}
	/**
	 * 个人主页 - 主题
	 */
	public function home_thread() {
		$_data = [];
		
		if($info = $this->obj->verify_member()) {
			$_field = 'id,forum_id,title,views,replies,likes,add_time';
			$list = $this->page($this->obj->table_thread, ['member_id'=> $info['id']], 'add_time desc', $_field);
			if($list) $_data = $this->obj->member_thread_list($list);
			
			$this->api_success('ok', $_data);
		}
		$this->api_error('用户信息不存在');
	}
	/**
	 * 个人主页 - 评论
	 */
	public function home_post() {
		$_data = [];
	
		if($info = $this->obj->verify_member()) {
			$_field = 'id,forum_id,thread_id,content,add_time';
			$list = $this->page($this->obj->table_post, ['member_id'=> $info['id']], 'add_time desc', $_field);
			if($list) $_data = $this->obj->member_post_list($list);
				
			$this->api_success('ok', $_data);
		}
		$this->api_error('用户信息不存在');
	}
	/**
	 * 个人主页 - 点赞
	 */
	public function home_like() {
		$_data = [];
	
		if($info = $this->obj->verify_member()) {
			$_field = 'id,thread_id';
			$list = $this->page($this->obj->table_thread_like, ['member_id'=> $info['id']], 'id desc', $_field);
			if($list) $_data = $this->obj->member_thread_like_list($list);
		
			$this->api_success('ok', $_data);
		}
		$this->api_error('用户信息不存在');
	}
}