<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;
use Api\Org\Forum\Forum;

/* APP公共首页*/
class IndexController extends ApiController{
	

	/**
	 * APP 首页
	 * @time 2017-08-22
	 **/
	public function index() {

		$data = array();

		$data['banner'] 		 = array();//D('Banner')->where()->find()->select();

		$data['recommend']		 = D('Product')->recommend(8);

		//notice_id
		$condition = array(
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);

		$district_id = I('district_id',0,'cloud_id_parser');

		if(!$district_id){
			$condition['district_id'] = 0;
		}else{
			$condition['district_id'] = array('IN',[0,$district_id]);
		}

		$data['district_id'] = $condition['district_id'];

		$data['notic']			 = (array)M('Notice')->where($condition)->field(['id as notice_id','title'])->order('add_time desc')->limit(8)->select();

		$data['forum']			 = get_adspace_cache(2);
		// bug($data['forum']);
		$data['banner']			 = get_adspace_cache(1);

		$data['property']		 = get_adspace_cache(4);

		$data['bianmin']		 = get_adspace_cache(6);

		$data['entry']		 	 = get_adspace_cache(7);

		if(!empty($data['banner'])){
			$temp 	  = array();
			foreach ($data['banner'] as $k => $v) {
				$temp[] = array(
						'adspace_id' => $v['adspace_id'],
						'image'		 => file_url($v['save_path']),
						// 'url'	 => $v['url'],
					);
			}
			$data['banner'] = $temp;
		}

		if(!empty($data['forum'])){
			$temp 	  = array();
			foreach ($data['forum'] as $kk => $vv) {
				$temp[] = array(
						'forum_title'   => $vv['title'],
						'image'		 	=> file_url($vv['save_path']),
						'forum_id'		=> $vv['url'],
					);
			}

			$data['forum'] = $temp;
		}

		if(!empty($data['property'])){
			$temp 	  = array();
			foreach ($data['property'] as $kk => $vv) {
				$temp[] = array(
						'property_title'    => $vv['title'],
						'image'		 		=> file_url($vv['save_path']),
						'property_id'		=> $vv['url'],
					);
			}

			$data['property'] = $temp;
		}

		if(!empty($data['bianmin'])){
			$temp 	  = array();
			foreach ($data['bianmin'] as $kk => $vv) {
				$temp[] = array(
						'bianmin_title'	=> $vv['title'],
						'image'			=> file_url($vv['save_path']),
						'bianmin_id'	=> $vv['url'],
					);
			}

			$data['bianmin'] = $temp;
		}

		if(!empty($data['entry'])){
			$temp 	  = array();
			foreach ($data['entry'] as $kk => $vv) {
				$temp[] = array(
						'title'    => $vv['title'],
						'image'		 		=> file_url($vv['save_path']),
						'entry_id'		=> $vv['url'],
					);
			}
			$data['entry'] = array_reverse($temp);
		}		
		
		/* 推荐主题 */
		$forum = new Forum();
		$_map = $forum->thread_map_recommend();
		$_order = 'recommend_time desc,add_time desc,id desc';
		$_field = 'id,forum_id,member_id,member_nickname,title,views,replies,likes,lastpost_time,add_time';
		$list = get_result($forum->table_thread, $_map, $_order, $_field, 6);
		if($list) $data['luntan_list'] = $this->luntan_list($forum->thread_list($list));		
		
		$this->api_success('HI~',$data);
	}	

	/**
	 * 首页推荐主题
	 */
	public function recommend_thread(){

		$data   = [];
		$forum  = new Forum();
		$_map   = $forum->thread_map_recommend();
		$_map['is_del']   = 0;
		$_map['is_hid']   = 0; 
		$_order = 'recommend_time desc,add_time desc,id desc';
		$_field = 'id,forum_id,member_id,member_nickname,title,views,replies,likes,lastpost_time,add_time';
		$list = $this->page($forum->table_thread, $_map, $_order, $_field, 6);
		if($list) $data = $this->luntan_list($forum->thread_list($list));

		$this->api_success('HI~',$data);
	}

	/**
	 * 论坛推荐文章
	 */
	protected function luntan_list($list) {
		$_data = [];
		foreach($list as $v) {
			if($v['image_list']) {
				$v['image'] = $v['image_list'][0]['image'];
				$v['width'] = $v['image_list'][0]['width'];
				$v['height'] = $v['image_list'][0]['height'];
				unset($v['image_list']);
				unset($v['lastpost_time']);
				unset($v['forum_id']);
				unset($v['forum_title']);
				$_data[] = $v;
			}
		}
		return $_data;
	}
}