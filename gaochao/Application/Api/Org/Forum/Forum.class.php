<?php
/**
 * 论坛
 */
namespace Api\Org\Forum;

use Common\Org\Forum\Forum as _Forum;
use Api\Org\Form\FileUpload;
use Api\Org\Form\Form;
use Api\Org\Form\Rules;
use Api\Org\Form\Search;
use Org\JPush\JPush;

class Forum extends _Forum{	
	/**
	 * 发帖
	 */
	public function thread_add($member_id, $nickname) {
		$_data = [];
		$_rules = [];
		$_error = '发帖失败，请稍后重试';
		
		$this->field_forum_id($_data, $_rules);
		$this->field_member_id($_data, $member_id);
		$this->field_member_nickname($_data, $nickname);
		$this->field_title($_data, $_rules);
		$this->field_content($_data, $_rules);
		$this->field_lastpost_time($_data, $_rules);
		$M = M();
		$M->startTrans();
		/* 主题表数据写入 */
		$result = update_data($this->table_thread, $_rules, [], $_data);
		if(!is_numeric($result)) {
			$M->rollback();
			return $result;
		}
		/* 更新版块统计数据 */
		$result_forum = $this->update_thread_forum($_data, $result);
		if(!is_numeric($result_forum)) {
			$M->rollback();
			return $_error;
		}
		/* 用户统计数据更新 */
		$result_member_count = $this->member_count_field_inc($member_id, 'threads');
		if(!is_numeric($result_member_count)) {
			$M->rollback();
			return $_error;
		}
		/* 主题表图片表数据写入 */
		$result_image = $this->thread_image($result, $member_id);
		if($result_image === false) {
			$M->rollback();
			return $result_image;
		}
		
		$M->commit();
		
		return $result;
	}
	/**
	 * 版块
	 */
	protected function field_forum_id(&$_data, &$_rules) {
		$forum_id = Form::int('forum_id');
		if($forum_id) {
			$cache = $this->cache_forum_recommend();
			if(isset($cache[$forum_id]) && !get_all_child_ids($cache, $forum_id, false)) {
				$_data['forum_id'] = $forum_id;
				return ;
			}
		}
		$_data['forum_id'] = '';
		$_rules[] = Rules::_require('forum_id', '请选择版块');
	}
	/**
	 * 用户ID
	 * @param unknown $_data
	 */
	protected function field_member_id(&$_data, $member_id) {
		$_data['member_id'] = $member_id;
	}
	/**
	 * 用户昵称
	 * @param unknown $_data
	 * @param unknown $member_nickname
	 */
	protected function field_member_nickname(&$_data, $member_nickname) {
		$_data['member_nickname'] = $member_nickname;
	}
	/**
	 * 标题
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_title(&$_data, &$_rules, $field = 'title', $key = 'title') {
		$_data[$field] = Form::string($key);
		$_rules[] = Rules::_require($field, '请输入标题');
		$_rules[] = Rules::length($field, '您的标题字数不能少于5', 5, 25);
	}
	/**
	 * 内容
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function field_content(&$_data, &$_rules, $field = 'content', $key = 'content') {
		$_data[$field] = Form::string($key);
		$_rules[] = Rules::_require($field, '请输入内容');
		$_rules[] = Rules::length($field, '您的内容字数不能少于10', 10, 100000);
	}
	/**
	 * 最后更新时间
	 * @param unknown $_data
	 * @param unknown $_rules
	 * @param string $field
	 */
	protected function field_lastpost_time(&$_data, &$_rules, $field = 'lastpost_time') {
		$_data[$field] = date('Y-m-d H:i:s');
	}
	/**
	 * 发帖 - 图片
	 * @param unknown $thread_id
	 */
	protected function thread_image($thread_id, $member_id) {
		if(!$_FILES) return true;
		
		$image = $this->_upload('Forum/ForumThread/');
		if(is_array($image)) {
			$_data = [];
			foreach($image as $v) {
				$_data[] = [
					'thread_id'=> $thread_id,
					'member_id'=> $member_id,
					'image'=> $v['filename'],
					'filesize'=> $v['size'],
					'width'=> $v['width'],
					'height'=> $v['height'],
					'add_time'=> date('Y-m-d H:i:s'),
				];
			}
			$result = M($this->table_thread_image)->addAll($_data);
			if(is_numeric($result)) return true;
			$this->_delete($image);
			return '图片上传失败';
		}
		return $image;
	}
	/**
	 * 文件上传
	 * @param unknown $save_path
	 * @param string $key
	 */
	protected function _upload($save_path, $key = 'file', $limit = 9) {
		$upload = new FileUpload();
		$upload->set('savePath', $save_path);
		$upload->_file($key);
		$upload->_limit($key, $limit);
		$list = $upload->upload();
		if(is_array($list) && $list[$key]) {
			if($upload->_count($key, $list[$key])) {
				return $list[$key];
			}
			return '图片上传失败';
		}
		return $list;
	}
	/**
	 * 删除
	 * @param unknown $list
	 */
	protected function _delete($list) {
		foreach($list as $v) {
			if(file_exists($v['filename'])) unlink($v['filename']);
		}
	}
	/**
	 * 主题 - 更新版块信息
	 */
	protected function update_thread_forum($data, $thread_id) {
		$_data = [];
		
		$_data['id'] = $data['forum_id'];
		$this->field_inc($_data, 'threads');
		$this->field_inc($_data, 'posts');
		$this->field_lastpost($thread_id, $data['title'], $data['member_id'], $data['member_nickname'], $_data);
		
		return update_data($this->table, [], [], $_data);
	}
	/**
	 * 最后发表
	 * @param unknown $thread_id
	 * @param unknown $thread_title
	 * @param unknown $member_id
	 * @param unknown $nickname
	 * @param unknown $_data
	 */
	protected function field_lastpost($thread_id, $thread_title, $member_id, $nickname, &$_data) {
		$lastpost = [
			'id'=> $thread_id,
			'title'=> $thread_title,
			'member_id'=> $member_id,
			'member_nickname'=> $nickname,
			'add_time'=> date('Y-m-d H:i:s'),
		];
		$_data['lastpost'] = serialize($lastpost);
	}
	/**
	 * 自增
	 */
	protected function field_inc(&$_data, $field) {
		$_data[$field] = ['exp', $field.'+1'];
	}
	/**
	 * 获取主题
	 */
	protected function get_thread($thread_id, $field = 'id') {
		if($thread_id) {
			$_map = [
				'id'=> $thread_id,	
			];
			return get_info($this->table_thread, $_map, $field);
		}
		return false;
	}
	/**
	 * 点赞
	 */
	public function thread_like($member_id, $nickname) {
		$_error = '点赞失败，请稍后重试';
		
		$thread_id = Form::int('id');
		$info = $this->get_thread($thread_id, 'id,member_id,title');
		if(!$info) return '主题信息不存在';
		$M = M();
		$M->startTrans();
		/* 点赞记录 */
		$result = $this->_thread_like($thread_id, $member_id);
		if(!is_numeric($result)) {
			$M->rollback();
			return $_error;
		}
		/* 更新主题点赞数 */
		$result_thread = $this->thread_inc($thread_id, 'likes');
		if(!is_numeric($result_thread)) {
			$M->rollback();
			return $_error;
		}
		/* 更新用户统计表数据 */
		$result_member_count = $this->_update_member_count($member_id, 'likes');
		if(!is_numeric($result_member_count)) {
			$M->rollback();
			return $_error;
		}
		/* 消息 */
		if($member_id != $info['member_id']) {
			$result_message = $this->message_add($info['member_id'], 2, $member_id, $nickname, $info['id'], 0, $result, $info['title'], false);
			if($result_message !== true) {
				$M->rollback();
				return $_error;
			}
		}
		
		$M->commit();
		
		return $result;
	}
	/**
	 * 主题 - 点赞
	 */
	protected function _thread_like($thread_id, $member_id) {
		$_data = [];
		
		$this->field_thread_id($_data, $thread_id);
		$this->field_member_id($_data, $member_id);
		
		try {
			return update_data($this->table_thread_like, [], [], $_data);
		}catch(\Exception $e) {
			return false;
		}
	}
	/**
	 * 主题 ID
	 * @param unknown $_data
	 * @param unknown $thread_id
	 */
	protected function field_thread_id(&$_data, $thread_id) {
		$_data['thread_id'] = $thread_id;
	}
	/**
	 * 主题 - 单字段自增
	 */
	protected function thread_inc($thread_id, $field) {
		$_map = [
			'id'=> $thread_id,
		];
		return M($this->table_thread)->where($_map)->setInc($field);
	}
	/**
	 * 取消点赞
	 */
	public function thread_like_cancel($member_id) {
		$_error = '取消点赞失败，请稍后重试';
		
		$thread_id = Form::int('id');
		$info = $this->get_thread($thread_id);
		if(!$info) return '主题信息不存在';
		$M = M();
		$M->startTrans();
		/* 删除点赞记录 */
		$_map = [
			'thread_id'=> $thread_id,
			'member_id'=> $member_id,
		];
		$result = delete_data($this->table_thread_like, $_map);
		if(!$result) {
			$M->rollback();
			return $_error;
		}
		/* 更新主题表点赞数 */
		$result_thread = $this->thread_dec($thread_id, 'likes');
		if(!is_numeric($result_thread)) {
			$M->rollback();
			return $_error;
		}
		/* 用户统计数据更新 */
		$result_member_count = $this->member_count_field_dec($member_id, 'likes');
		if(!is_numeric($result_member_count)) {
			$M->rollback();
			return $_error;
		}
		
		$M->commit();
		
		return $result;
	}
	/**
	 * 主题 - 单字段自减
	 */
	protected function thread_dec($thread_id, $field) {
		$_map = [
			'id'=> $thread_id,
		];
		return M($this->table_thread)->where($_map)->setDec($field);
	}
	/**
	 * 主题 - 评论
	 */
	public function post_add($member_id, $nickname) {
		$_data = [];
		$_rules = [];
		$error = '评论失败，请稍后重试';
		
		$thread_id = Form::int('id');
		$info = $this->get_thread($thread_id, 'id,member_id,forum_id,title');
		if(!$info) return '主题信息不存在';
		
		$_data['forum_id'] = $info['forum_id'];
		$this->field_thread_id($_data, $thread_id);
		$this->field_member_id($_data, $member_id);
		$this->field_member_nickname($_data, $nickname);
		$this->_post_field_content($_data, $_rules);
		$M = M();
		$M->startTrans();
		$result = update_data($this->table_post, $_rules, [], $_data);
		if(!is_numeric($result)) {
			$M->rollback();
			return $result;
		}
		/* 处理图片 */
		$result_image = $this->post_image($thread_id, $result, $member_id);
		if($result_image === false) {
			$M->rollback();
			return $result_image;
		}
		/* 更新版块信息 */
		$result_forum = $this->update_post_forum($info['forum_id'], $info['id'], $info['title'], $_data);
		if(!is_numeric($result_forum)) {
			$M->rollback();
			return $error;
		}
		/* 更新主题信息 */
		$result_thread = $this->update_post_thread($thread_id, $nickname);
		if(!is_numeric($result_thread)) {
			$M->rollback();
			return $error;
		}
		/* 更新帖子楼层 */
		$result_position = $this->update_position($thread_id, $result);
		if(!is_numeric($result_position)) {
			$M->rollback();
			return $error;
		}
		/* 更新用户统计表数据 */
		$result_member_count = $this->_update_member_count($member_id);
		if(!is_numeric($result_member_count)) {
			$M->rollback();
			return $error;
		}
		/* 消息 */
		if($member_id != $info['member_id']) {
			$result_message = $this->message_add($info['member_id'], 0, $member_id, $nickname, $info['id'], 0, $result, $info['title']);
			if($result_message !== true) {
				$M->rollback();
				return $error;
			}
		}
		
		$M->commit();
		
		return $result;
	}
	/**
	 * 评论 - 图片
	 * @param unknown $thread_id
	 * @param unknown $post_id
	 * @param unknown $member_id
	 * @return boolean|string|string|unknown|\Common\Org\Form\unknown|boolean|\Common\Org\Form\mixed[]|\Common\Org\Form\string[]
	 */
	protected function post_image($thread_id, $post_id, $member_id) {
		if(!$_FILES) return true;
		
		$image = $this->_upload('Forum/ForumPost/');
		if(is_array($image)) {
			$_data = [];
			foreach($image as $v) {
				$_data[] = [
					'thread_id'=> $thread_id,
					'post_id'=> $post_id,
					'member_id'=> $member_id,
					'image'=> $v['filename'],
					'filesize'=> $v['size'],
					'width'=> $v['width'],
					'height'=> $v['height'],
					'add_time'=> date('Y-m-d H:i:s'),
				];
			}
			$result = M($this->table_post_image)->addAll($_data);
			if(is_numeric($result)) return true;
			$this->_delete($image);
			return '图片上传失败';
		}
		return $image;
	}
	/**
	 * 评论 - 更新版块信息
	 */
	protected function update_post_forum($forum_id, $thread_id, $title, $data) {
		$_data = [];
	
		$_data['id'] = $forum_id;
		$this->field_inc($_data, 'posts');
		$this->field_lastpost($thread_id, $title, $data['member_id'], $data['member_nickname'], $_data);
	
		return update_data($this->table, [], [], $_data);
	}
	/**
	 * 评论 - 更新主题信息
	 */
	protected function update_post_thread($thread_id, $lastposter) {
		$_data = [];
		
		$_data['id'] = $thread_id;
		$_data['lastposter'] = $lastposter;
		$_data['lastpost_time'] = date('Y-m-d H:i:s');
		$this->field_inc($_data, 'replies');
		
		return update_data($this->table_thread, [], [], $_data);
	}
	/**
	 * 更新帖子楼层
	 */
	protected function update_position($thread_id, $post_id) {
		$table = C('DB_PREFIX').$this->table_post;
		$sql = "Update {$table} a,(Select position from {$table} where thread_id={$thread_id} order by position desc limit 1) b set a.position=b.position+1 where id={$post_id}";
		
		return M($this->table_post)->execute($sql);
	}
	/**
	 * 主题 - 评论 - 回复
	 */
	public function reply_add($member_id, $nickname) {
		$_data = [];
		$_rules = [];
		$_error = '回复失败，请稍后重试';
	
		$thread_id = Form::int('thread_id');
		$info_thread = $this->get_thread($thread_id, 'id,forum_id,title');
		if(!$info_thread) return '主题信息不存在';
		$post_id = Form::int('id');
		$info = $this->get_post($thread_id, $post_id, 'id,forum_id,member_id,member_nickname,content,add_time');
		if(!$info) return '评论信息不存在';
	
		$_data['forum_id'] = $info_thread['forum_id'];
		$this->field_thread_id($_data, $thread_id);
		$this->field_post_id($_data, $post_id);
		$this->field_member_id($_data, $member_id);
		$this->field_member_nickname($_data, $nickname);
		$this->field_content($_data, $_rules);
		$this->field_post_info($info['id'], $info['member_id'], $info['member_nickname'], $info['content'], $info['add_time'], $_data);
		$M = M();
		$M->startTrans();
		$result = update_data($this->table_post, $_rules, [], $_data);
		if(!is_numeric($result)) {
			$M->rollback();
			return $result;
		}
		/* 处理图片 */
		$result_image = $this->post_image($thread_id, $result, $member_id);
		if($result_image === false) {
			$M->rollback();
			return $result_image;
		}
		/* 更新版块信息 */
		$result_forum = $this->update_post_forum($info['forum_id'], $info['id'], $info['title'], $_data);
		if(!is_numeric($result_forum)) {
			$M->rollback();
			return $_error;
		}
		/* 更新主题信息 */
		$result_thread = $this->update_post_thread($thread_id, $nickname);
		if(!is_numeric($result_thread)) {
			$M->rollback();
			return $_error;
		}
		/* 更新帖子楼层 */
		$result_position = $this->update_position($thread_id, $result);
		if(!is_numeric($result_position)) {
			$M->rollback();
			return $_error;
		}
		/* 更新用户统计表数据 */
		$result_member_count = $this->_update_member_count($member_id);
		if(!is_numeric($result_member_count)) {
			$M->rollback();
			return $error;
		}
		/* 消息 */
		if($member_id != $info['member_id']) {
			$result_message = $this->message_add($info['member_id'], 1, $member_id, $nickname, $info_thread['id'], $info['id'], $result, $info_thread['title']);
			if($result_message !== true) {
				$M->rollback();
				return $_error;
			}
		}
	
		$M->commit();
	
		return $result;
	}
	/**
	 * 获取主题
	 */
	protected function get_post($thread_id, $post_id, $field = 'id') {
		if($post_id && $thread_id) {
			$_map = [
				'id'=> $post_id,
				'thread_id'=> $thread_id,
			];
			return get_info($this->table_post, $_map, $field);
		}
		return false;
	}
	/**
	 * 帖子ID
	 */
	protected function field_post_id(&$_data, $post_id) {
		$_data['post_id'] = $post_id;
	}
	/**
	 * 被回复帖子信息
	 */
	protected function field_post_info($post_id, $member_id, $nickname, $content, $add_time, &$_data) {
		$post_info = [
			'id'=> $post_id,
			'member_id'=> $member_id,
			'member_nickname'=> $nickname,
			'content'=> $content,
			'add_time'=> $add_time,
		];
		$_data['post_info'] = serialize($post_info);
	}
	/**
	 * 点赞
	 */
	public function post_like($member_id) {
		$_error = '点赞失败，请稍后重试';
		
		$thread_id = Form::int('thread_id');
		$info_thread = $this->get_thread($thread_id, 'id,title');
		if(!$info_thread) return '主题信息不存在';
		$post_id = Form::int('id');
		$info = $this->get_post($thread_id, $post_id, 'id,member_id');
		if(!$info) return '评论信息不存在';
		
		$M = M();
		$M->startTrans();
		/* 点赞记录 */
		$result = $this->_post_like($thread_id, $post_id, $member_id);
		if(!is_numeric($result)) {
			$M->rollback();
			return $_error;
		}
		/* 更新帖子点赞数 */
		$result_thread = $this->post_inc($post_id, 'likes');
		if(!is_numeric($result_thread)) {
			$M->rollback();
			return $_error;
		}
		/* 用户统计数据更新 */
		$result_member_count = $this->member_count_field_inc($member_id, 'message_forum');
		if(!is_numeric($result_member_count)) {
			$M->rollback();
			return $_error;
		}
		/* 消息 */
		if($member_id != $info['member_id']) {
			$result_message = $this->message_add($info['member_id'], 3, $member_id, $nickname, $info_thread['id'], $info['id'], $result, $info_thread['title'], false);
			if($result_message !== true) {
				$M->rollback();
				return $_error;
			}
		}
		
		$M->commit();
	
		return $result;
	}
	/**
	 * 评论（帖子） - 单字段自增
	 */
	protected function post_inc($post_id, $field) {
		$_map = [
			'id'=> $post_id,
		];
		return M($this->table_post)->where($_map)->setInc($field);
	}
	/**
	 * 评论（帖子） - 单字段自减
	 */
	protected function post_dec($post_id, $field) {
		$_map = [
			'id'=> $post_id,
		];
		return M($this->table_post)->where($_map)->setDec($field);
	}
	/**
	 * 评论（帖子） - 点赞
	 */
	protected function _post_like($thread_id, $post_id, $member_id) {
		$_data = [];
	
		$this->field_thread_id($_data, $thread_id);
		$this->field_post_id($_data, $post_id);
		$this->field_member_id($_data, $member_id);
	
		try {
			return update_data($this->table_post_like, [], [], $_data);
		}catch(\Exception $e) {
			return false;
		}
	}
	/**
	 * 取消点赞
	 */
	public function post_like_cancel($member_id) {
		$_error = '取消点赞失败，请稍后重试';
		
		$thread_id = Form::int('thread_id');
		$info_thread = $this->get_thread($thread_id, 'id');
		if(!$info_thread) return '主题信息不存在';
		$post_id = Form::int('id');
		$info = $this->get_post($thread_id, $post_id, 'id');
		if(!$info) return '评论信息不存在';
		
		$M = M();
		$M->startTrans();
		/* 删除点赞记录 */
		$_map = [
			'thread_id'=> $thread_id,
			'post_id'=> $post_id,
			'member_id'=> $member_id,
		];
		$result = delete_data($this->table_post_like, $_map);
		if(!$result) {
			$M->rollback();
			return $_error;
		}
		/* 更新帖子点赞数 */
		$result_post = $this->post_dec($post_id, 'likes');
		if(!is_numeric($result_post)) {
			$M->rollback();
			return $_error;
		}
		$M->commit();
	
		return $result;
	}
	/**
	 * 广告
	 */
	public function _ad($adspace_id = 2, $limit = 9) {
		$_data = [];
		
		$list = array_slice($this->cache_ad()[$adspace_id], 0, $limit);
		foreach($list as $v) {
			$_data[] = [
				'save_path'=> $v['save_path'],
				'url'=> $v['url'],
			];
		}
		
		return $_data;
	}
	/**
	 * 广告缓存
	 */
	protected function cache_ad() {
		$_data = [];
		$cache = get_no_hid('banner');
		foreach($cache as $v) {
			$v['save_path'] = U($v['save_path'], '', false, true);
			$_data[$v['adspace_id']][] = $v;		
		}
		
		return $_data;
	}
	/**
	 * 推荐版块
	 */
	public function _forum_recommend($limit = 4) {
		return array_slice($this->_cache_forum(), 0, $limit);
	}
	/**
	 * 版块缓存
	 * @return unknown[][]|string[][]
	 */
	protected function _cache_forum() {
		$_data = [];
		
		$cache = $this->cache_forum_recommend();
		foreach($cache as $v) {
			$_data[$v['id']] = $this->_forum_value($v);
		}
		
		return $_data;
	}
	/**
	 * 推荐主题
	 */
	public function thread_map_recommend() {
		$map = [
			'recommend'=> 1,	
		];
		return $map;
	}
	/**
	 * 主题数据处理
	 */
	public function thread_list($list) {
		$_data = [];
		$cache_forum = $this->cache_forum_recommend();
		
		$ids = array_column($list, 'id');
		$image = $this->get_thread_image($ids);
		foreach($list as $v) {
			$v['image_list'] = isset($image[$v['id']]) ? $image[$v['id']] : [];
			$v['forum_title'] = $cache_forum[$v['forum_id']]['title'];
			$v['avatar'] = get_avatar($v['member_id'], 'L');
			$v['add_time'] = format_time(strtotime($v['add_time']));
			$v['lastpost_time'] = $v['lastpost_time'] != '0000-00-00 00:00:00' ? format_time(strtotime($v['lastpost_time'])) : $v['add_time'];
			$_data[] = $v;
		}
		
		return $_data;
	}
	/**
	 * 主题图片
	 */
	protected function get_thread_image($thread_id) {
		$_data = [];
		
		$_map = [
			'thread_id'=> is_array($thread_id) ? ['in', $thread_id] : $thread_id,
		];
		$list = get_result($this->table_thread_image, $_map, 'id asc', 'id,thread_id,image,width,height');
		foreach($list as $v) {
			$v['image'] = U($v['image'], '', false, true);
			if(is_array($thread_id)) {
				$_data[$v['thread_id']][] = $v;
			}else {
				$_data[] = $v;
			}
		}
		
		return $_data;
	}
	/**
	 * 全部版块
	 */
	public function forum_list() {
		return $this->_forum_list($this->cache_partition(), $this->cache_forum_recommend());
	}
	/**
	 * 全部版块
	 * @param unknown $partition
	 * @param unknown $forum
	 */
	protected function _forum_list($partition, $forum) {
		$_data = [];
		
		foreach($partition as $v) {
			$_data[$v['id']] = [
				'id'=> $v['id'],
				'title'=> $v['title'],
			];
			foreach($forum as $_k=> $_v) {
				if($_v['pid'] == $v['id']) {
					$_data[$v['id']]['forum_list'][] = $this->_forum_value($_v);
					unset($forum[$_k]);
				}
			}
		}
		
		return array_values($_data);
	}
	/**
	 * 处理版块数据
	 * @param unknown $value
	 * @return unknown[]|string[]
	 */
	protected function _forum_value($value) {
		$_data = [
			'id'=> $value['id'],
			'title'=> $value['title'],
			'icon'=> $value['icon'] ? U($value['icon'], '', false, true) : '',
		];
		
		return $_data;
	}
	/**
	 * 版块搜索
	 */
	public function search_forum() {
		$_data = [];
		
		$_map = [];
		Search::string_like($_map, 'title');
		$list = get_result($this->table, $_map, 'posts desc,sort desc,add_time desc,id desc', 'id,title,icon');
		if($list) {
			foreach($list as $v) {
				$_data[] = $this->_forum_value($v);
			}
		}
		
		return $_data;
	}
	/**
	 * 主题搜索
	 */
	public function search_forum_thread() {		
		$_map = ['is_del'=>0,'is_hid'=>0];
		Search::string_like($_map, 'title');
		
		return $_map;
	}
	/**
	 * 搜索结果主题列表
	 * @param unknown $list
	 */
	public function search_list($list) {
		$_data = [];
		$cache_forum = $this->cache_forum_recommend();
		
		foreach($list as $v) {
			$v['forum_title'] = $cache_forum[$v['forum_id']]['title'];
			$v['forum_icon'] = U($cache_forum[$v['forum_id']]['icon'], '', false, true);
			$v['add_time'] = format_time(strtotime($v['add_time']));
			$_data[] = $v;
		}
		
		return $_data;
	}
	/**
	 * 版块主页
	 */
	public function forum_index() {
		$_data = [];
		
		$id = Form::int('id', 'get.');
		if($id && $info = get_info($this->table, ['id'=> $id], 'id,title,icon,posts')) {
			/* 版块信息 */
			$_data = $this->_forum_value($info);
			$_data['posts'] = $info['posts'];
		}
		return $_data;
	}
	/**
	 * 全部主题查询条件
	 */
	public function thread_map_all($forum_id) {
		$_map = [
			'forum_id'=> $forum_id,
			'is_del'=>0,
			'is_hid'=>0
		];
		
		return $_map;
	}
	/**
	 * 精华帖
	 * @param unknown $forum_id
	 * @return unknown[]
	 */
	public function thread_map_digest($forum_id) {
		$_map = [
			'forum_id'=> $forum_id,
			'digest'=> 1,
			'is_del'=>0,
			'is_hid'=>0
		];
		
		return $_map;
	}
	/**
	 * 主题详情
	 */
	public function thread_detail($member_id = 0) {
		$_data = [];
		
		$id = Form::int('id', 'get.');
		$_field = 'id,forum_id,member_id,member_nickname,title,content,views,replies,likes,lastpost_time,add_time';
		$cache_forum = $this->cache_forum_recommend();
		if($id && ($info = get_info($this->table_thread, ['id'=> $id], $_field)) && isset($cache_forum[$info['forum_id']])) {
			$_data = $info;
			$_data['forum_title'] = $cache_forum[$info['forum_id']]['title'];
			$_data['avatar'] = get_avatar($info['member_id'], 'L');
			$_data['lastpost_time'] = format_time(strtotime($info['lastpost_time']));
			$_data['add_time'] = format_time(strtotime($info['add_time']));
			$_data['image_list'] = $this->get_thread_image($info['id']);
			$_data['is_like'] = $member_id ? $this->_thread_is_like($info['id'], $member_id) : 0;
			$_data['url'] = U('Home/Index/Index/thread_share', ['id'=> $info['id']], true, true);
			
			$this->thread_inc($info['id'], 'views');
		}
		return $_data;
	}
	/**
	 * 是否已赞
	 */
	protected function _thread_is_like($thread_id, $member_id) {
		$_map = [
			'thread_id'=> $thread_id,
			'member_id'=> $member_id,
		];
		if(get_info($this->table_thread_like, $_map, 'id')) {
			return 1;
		}
		return 0;
	}
	/**
	 * 帖子主题信息
	 */
	public function post_thread() {
		$id = Form::int('id', 'get.');
		
		return get_info($this->table_thread, ['id'=> $id], 'id');
	}
	/**
	 * 帖子列表查询条件
	 * @param unknown $thread_id
	 */
	public function post_map_list($thread_id) {
		$_map = ['is_del'=>0,'is_hid'=>0];
		
		$_map['thread_id'] = $thread_id;
		
		return $_map;
	}
	/**
	 * 帖子列表
	 */
	public function post_list($list, $member_id = 0) {
		$_data = [];
		
		$ids = array_column($list, 'id');
		$image = $this->get_post_image($ids);
		if($member_id) $post_is_like = $this->_post_is_like($ids, $member_id);
		foreach($list as $v) {
			$v['position'] = $v['position'].'楼';
			$v['image_list'] = isset($image[$v['id']]) ? $image[$v['id']] : [];
			$v['avatar'] = get_avatar($v['member_id'], 'L');
			$v['add_time'] = format_time(strtotime($v['add_time']));
			$v['post_info'] = $v['post_id'] && unserialize($v['post_info']) ? unserialize($v['post_info']) : (object)[];
			$v['is_like'] = ($member_id && in_array($v['id'], $post_is_like)) ? 1 : 0; 
			$_data[] = $v;
		}
		return $_data;
	}
	/**
	 * 主题图片
	 */
	protected function get_post_image($post_id) {
		$_data = [];
	
		$_map = [
			'post_id'=> is_array($post_id) ? ['in', $post_id] : $post_id,
		];
		$list = get_result($this->table_post_image, $_map, 'id asc', 'id,post_id,image');
		foreach($list as $v) {
			$v['image'] = U($v['image'], '', false, true);
			if(is_array($post_id)) {
				$_data[$v['post_id']][] = $v;
			}else {
				$_data[] = $v;
			}
		}
	
		return $_data;
	}
	/**
	 * 是否已赞
	 */
	protected function _post_is_like($post_id, $member_id) {
		$_map = [
			'post_id'=> ['IN', $post_id],
			'member_id'=> $member_id,
		];
		$list = get_result($this->table_post_like, $_map, 'post_id asc', 'post_id');
		
		if($list) return array_column($list, 'post_id');
		
		return [];
	}
	/**
	 * 主题 - 删除
	 */
	public function thread_delete($member_id) {
		$id = Form::int('id');
		if($id && $info = get_info($this->table_thread, ['id'=> $id, 'member_id'=> $member_id], 'id')) {
			if(is_numeric($this->thread_delete_thread($info['id'])) && is_numeric($this->thread_delete_post($info['id']))) {
				return true;
			}else {
				return '主题删除失败，请稍后重试';
			}
			
		}
		return '主题信息不存在';
	}
	/**
	 * 主题删除 - 假删除
	 * @param unknown $thread_id
	 */
	protected function thread_delete_thread($thread_id) {
		$_map = [
			'id'=> $thread_id,
		];
		$_data = [
			'is_del'=> 1,	
		];
		
		return update_data($this->table_thread, [], $_map, $_data);
	}
	/**
	 * 主题删除  - 帖子 - 假删除
	 * @param unknown $thread_id
	 */
	protected function thread_delete_post($thread_id) {
		$_map = [
			'thread_id'=> $thread_id,
		];
		$_data = [
			'is_del'=> 1,
		];
	
		return update_data($this->table_post, [], $_map, $_data);
	}
	/**
	 * 帖子 - 删除
	 */
	public function post_delete($member_id) {
		$id = Form::int('id');
		if($id && $info = get_info($this->table_post, ['id'=> $id, 'member_id'=> $member_id], 'id')) {
			if(is_numeric($this->post_delete_post($info['id']))) {
				return true;
			}else {
				return '评论删除失败，请稍后重试';
			}
				
		}
		return '评论信息不存在';
	}
	/**
	 * 帖子删除 - 假删除
	 * @param unknown $thread_id
	 */
	protected function post_delete_post($post_id) {
		$_map = [
			'id'=> $post_id,
		];
		$_data = [
			'is_del'=> 1,
		];
	
		return update_data($this->table_post, [], $_map, $_data);
	}
	/**
	 * 用户信息
	 */
	public function verify_member($field = 'id') {
		$id = Form::int('id', 'get.');
		if($id && $info = get_info($this->table_member, ['id'=> $id], $field)) {
			return $info;
		}
		
		return false;
	}
	/**
	 * 个人主页 - 主题
	 * @param unknown $member_id
	 */
	public function member_thread_list($list) {
		return $this->search_list($list);
	}
	/**
	 * 个人主页 - 帖子
	 */
	public function member_post_list($list) {
		return $this->_member_post_list($list);
	}
	/**
	 * 个人主页 - 帖子
	 * @param unknown $list
	 */
	protected function _member_post_list($list) {
		$_data = [];
		
		$thread_id = array_column($list, 'thread_id');
		
		$thread_list = array_format(get_result($this->table_thread, ['id'=> ['IN', $thread_id]], 'id asc', 'id,title,forum_id'));
		$cache_forum = $this->cache_forum_recommend();
		foreach($list as $v) {
			$v['thread_info'] = $thread_list[$v['thread_id']];
			$v['thread_info']['forum_title'] = $cache_forum[$v['thread_info']['forum_id']]['title'];
			$v['add_time'] = format_time(strtotime($v['add_time']));
			$_data[] = $v;
		}
		
		return $_data;
	}
	/**
	 * 个人主页 - 点赞
	 */
	public function member_thread_like_list($list) {
		return $this->_member_thread_like_list($list);
	}
	/**
	 * 个人主页 - 点赞
	 * @param unknown $list
	 */
	protected function _member_thread_like_list($list) {
		$_data = [];
	
		$thread_id = array_column($list, 'thread_id');
	
		$thread_list = array_format(get_result($this->table_thread, ['id'=> ['IN', $thread_id]], 'id asc', 'id,title,forum_id,add_time'));
		$cache_forum = $this->cache_forum_recommend();
		foreach($list as $v) {
			if(isset($thread_list[$v['thread_id']])) {
				$thread_info = $thread_list[$v['thread_id']];
				$thread_info['forum_title'] = $cache_forum[$thread_info['forum_id']]['title'];
				$thread_info['forum_icon'] = U($cache_forum[$thread_info['forum_id']]['icon'], '', false, true);
				$thread_info['add_time'] = format_time(strtotime($thread_info['add_time']));
				$_data[] = $thread_info;
			}
		}
	
		return $_data;
	}
	/**
	 * 消息 - 添加
	 */
	protected function message_add($member_id, $type, $from_id, $from_name, $thread_id, $post_id, $data_id, $thread_title, $push = true) {
		$_data = [];
		
		$this->field_set('member_id', $member_id, $_data);
		$this->field_set('type', $type, $_data);
		$this->field_set('from_id', $from_id, $_data);
		$this->field_set('from_name', $from_name, $_data);
		$this->field_set('thread_id', $thread_id, $_data);
		$this->field_set('post_id', $post_id, $_data);
		$this->field_set('data_id', $data_id, $_data);
		$this->field_set('thread_title', $thread_title, $_data);
		$this->field_set('message', $this->get_message_content($type, $from_name, $thread_title), $_data);
		$this->field_inc($_data, 'unread');
		$this->field_set('add_time', date('Y-m-d H:i:s'), $_data);
		$this->field_message_id($member_id, $type, $thread_id, $_data);
		
		$result = update_data($this->table_message, [], [], $_data);
		if(!is_numeric($result)) return false;
		if($push) JPush::push($member_id, '论坛消息', $_data['message'], ['type'=> 0, 'id'=> $thread_id]);
		
		return true;
	}
	/**
	 * 设置字段值
	 */
	protected function field_set($field, $value, &$_data) {
		$_data[$field] = $value;
	}
	/**
	 * 获取消息内容
	 */
	protected function get_message_content($type, $from_name, $thread_title) {
		switch($type) {
			case 2:
				return $from_name.' 在主题 '.$thread_title.' 赞了你';
				break;
			default:
				return $from_name.' 在主题 '.$thread_title.' 回复了你';
		}
	}
	/**
	 * 消息 ID
	 */
	protected function field_message_id($member_id, $type, $thread_id, &$_data) {
		$_map = [];
		
		switch($type) {
			case 0:
			case 2:
			case 3:
				$_map['member_id'] = $member_id;
				$_map['type'] = $type;
				$_map['thread_id'] = $thread_id;
				break;
			default:
				return ;
		}
		$info = get_info($this->table_message, $_map, 'id');
		if($info) $_data['id'] = $info['id'];
	}
	/**
	 * 用户数据统计表单字段自增
	 */
	protected function member_count_field_inc($member_id, $field, $step = 1) {
		return M($this->table_member_count)->where(['member_id'=> $member_id])->setInc($field, $step);
	}
	/**
	 * 用户数据统计表单字段自减
	 */
	protected function member_count_field_dec($member_id, $field, $step = 1) {
		return M($this->table_member_count)->where(['member_id'=> $member_id])->setDec($field, $step);
	}
	/**
	 * 更新用户统计数据
	 */
	protected function _update_member_count($member_id, $field = 'posts') {
		$_data = [];
		
		$this->field_inc($_data, $field);
		$this->field_inc($_data, 'message_forum');
		
		return update_data($this->table_member_count, [], ['member_id'=> $member_id], $_data);
	}
	/**
	 * 获取用户统计数据
	 */
	public function get_memeber_count($member_id, $field = 'id') {
		return get_info($this->table_member_count, ['member_id'=> $member_id], $field);
	}
	/**
	 * 消息列表
	 * @param unknown $list
	 */
	public function message_list($list) {
		$_data = [];
		
		foreach($list as $v) {
			switch($v['type']) {
				case 0:
				case 1:
					$v['center'] = '回复了您的帖子';
					$v['ending'] = '';
					break;
				case 2:
					$v['center'] = '赞了您的帖子';
					$v['ending'] = '';
					break;
				case 3:
					$v['center'] = '赞了您在帖子';
					$v['ending'] = '下的回复';
					break;
			}
			$v['is_red'] = $v['unread'] > 0 ? 1 : 0;
			$v['add_time'] = format_time(strtotime($v['add_time']));
			unset($v['unread']);
			$_data[] = $v;
		}
		
		return $_data;
	}
	/**
	 * 更新消息数量
	 */
	public function message_num_update($member_id) {
		$number = M($this->table_message)->where(['member_id'=> $member_id])->sum('unread');
		if($number) {
			$M = M();
			$M->startTrans();
			$result = update_data($this->table_message, [], ['member_id'=> $member_id], ['unread'=> 0]);
			if(!is_numeric($result)) {
				$M->rollback();
				return ;
			}
			$result_member_count = $this->member_count_field_dec($member_id, 'message_forum', $number);
			if(!is_numeric($result_member_count)) {
				$M->rollback();
				return ;
			}
			
			$M->commit();
		}
	}
	/**
	 * 帖子内容
	 * @param unknown $_data
	 * @param unknown $_rules
	 */
	protected function _post_field_content(&$_data, &$_rules, $field = 'content', $key = 'content') {
		$_data[$field] = Form::string($key);
		$_rules[] = Rules::_require($field, '请输入内容');
		$_rules[] = Rules::length($field, '您的内容字数不能少于3', 3, 200);
	}
}