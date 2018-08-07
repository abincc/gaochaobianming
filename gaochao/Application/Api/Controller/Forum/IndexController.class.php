<?php
/**
 * 论坛 - 主控制器
 */
namespace Api\Controller\Forum;

use Api\Controller\Base\BaseController;

class IndexController extends BaseController{
	protected $table_member = 'member';
	protected $member_info = [];
	
	/**
	 * 获取用信息
	 * @return string[]|unknown
	 */
	protected function get_member_info() {
		/* 接口测试使用，用后请立即删除 */
//		$this->member_id = I('post.member_id');
		$_map = [
			'id'=> $this->member_id,	
		];
		$this->member_info = get_info($this->table_member, $_map, 'id,nickname');
		
		return $this->member_info;
	}
}