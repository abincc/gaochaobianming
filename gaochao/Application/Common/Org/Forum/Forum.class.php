<?php
/**
 * 论坛 - 基类
 */
namespace Common\Org\Forum;

class Forum{
	public $table = 'forum_forum';
	public $table_thread = 'forum_thread';
	public $table_thread_image = 'forum_thread_image';
	public $table_thread_like = 'forum_thread_like';
	public $table_post = 'forum_post';
	public $table_post_image = 'forum_post_image';
	public $table_post_like = 'forum_post_like';
	public $table_member = 'member';
	public $table_message = 'forum_message';
	public $table_member_count = 'member_count';
	
	public function __construct() {
		
	}
	/**
	 * 缓存
	 * @param string $key
	 */
	public function cache($key = 'hid') {
		$func = 'get_no_'.$key;
	
		return $func($this->table);
	}
	/**
	 * 推荐版块缓存
	 */
	public function cache_forum_recommend() {
		$file = $this->table.'_recommend_no_hid';
		if(!F($file)) {
			$map = [
				'pid'=> ['NEQ', 0],
				'type'=> ['IN', [1, 2]],
				'is_hid'=> 0,
				'is_del'=> 0,
			];
			$all = get_result($this->table, $map, 'recommend desc,sort desc,add_time desc,id desc');
			F($file, array_format($all));
		}
		return F($file);
	}
	/**
	 * 分区缓存
	 */
	public function cache_partition() {
		$file = $this->table.'_partition_no_hid';
		if(!F($file)) {
			$map = [
				'pid'=> 0,
				'type'=> 0,
				'is_hid'=> 0,
				'is_del'=> 0,
			];
			$all = get_result($this->table, $map, 'sort desc,add_time desc,id desc');
			F($file, array_format($all));
		}
		return F($file);
	}
}