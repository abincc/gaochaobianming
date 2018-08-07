<?php
/**
 * 论坛
 */
namespace Api\Controller\Forum;

use Api\Org\Forum\Forum;

class ForumController extends IndexController{
	protected $table = 'forum';
	protected $obj;
	protected $member_id = 8;
	
	/**
	 * 初始化
	 */
	public function __init() {
		parent::__init();
		$this->obj = new Forum();
	}
	/**
	 * 发帖
	 */
	public function thread_add() {
		if(IS_POST) {
			$this->get_member_info();
			$result = $this->obj->thread_add($this->member_info['id'], $this->member_info['nickname']);
			if(is_numeric($result)) $this->api_success('发帖成功');
			
			$this->api_error($result);
		}
	}
	/**
	 * 点赞
	 */
	public function thread_like() {
		if(IS_POST) {
			$this->get_member_info();
			$result = $this->obj->thread_like($this->member_info['id'], $this->member_info['nickname']);
			if(is_numeric($result)) $this->api_success('点赞成功');
			
			$this->api_error($result);
		}
	}
	/**
	 * 取消点赞
	 */
	public function thread_like_cancel() {
		if(IS_POST) {
			try {
				$result = $this->obj->thread_like_cancel($this->member_id);
				if($result) $this->api_success('取消成功');
					
				$this->api_error($result);
			}catch(\Exception $e) {
				$this->api_success('取消成功');
			}
		}
	}
	/**
	 * 评论
	 */
	public function post_add() {
		if(IS_POST) {
			$this->get_member_info();
			$result = $this->obj->post_add($this->member_info['id'], $this->member_info['nickname']);
			if(is_numeric($result)) $this->api_success('评论成功');
			
			$this->api_error($result);
		}
	}
	/**
	 * 回复
	 */
	public function reply_add() {
		if(IS_POST) {
			$this->get_member_info();
			$result = $this->obj->reply_add($this->member_info['id'], $this->member_info['nickname']);
			if(is_numeric($result)) $this->api_success('回复成功');
			
			$this->api_error($result);
		}
	}
	/**
	 * 点赞
	 */
	public function post_like() {
		if(IS_POST) {
			$this->get_member_info();
			$result = $this->obj->post_like($this->member_info['id'], $this->member_info['nickname']);
			if(is_numeric($result)) $this->api_success('点赞成功');
			
			$this->api_error($result);
		}
	}
	/**
	 * 取消点赞
	 */
	public function post_like_cancel() {
		if(IS_POST) {
			try {
				$result = $this->obj->post_like_cancel($this->member_id);
				if($result) $this->api_success('取消成功');
					
				$this->api_error($result);
			}catch(\Exception $e) {
				$this->api_success('取消成功');
			}
		}
	}
	/**
	 * 主题 - 删除
	 */
	public function thread_delete() {
		if(IS_POST) {
			$result = $this->obj->thread_delete($this->member_id);
			if($result === true) $this->api_success('删除成功');
			
			$this->api_error($result);
		}
	}
	/**
	 * 帖子 - 删除
	 */
	public function post_delete() {
		if(IS_POST) {
			$result = $this->obj->post_delete($this->member_id);
			if($result === true) $this->api_success('删除成功');
				
			$this->api_error($result);
		}
	}
	/**
	 * 消息列表
	 */
	public function message_list() {
		$_data = [];
		
		$_field = 'id,member_id,type,from_id,from_name,thread_id,post_id,data_id,thread_title,unread,add_time';
		$list = $this->page($this->obj->table_message, ['member_id'=> $this->member_id], 'add_time desc', $_field);
		if($list) $_data = $this->obj->message_list($list);
		try {
			$this->obj->message_num_update($this->member_id);
		}catch(\Exception $e) {	
		}
		
		$this->api_success('ok', $_data);
	}
}