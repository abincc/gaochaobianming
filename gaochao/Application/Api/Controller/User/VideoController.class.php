<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 小事帮忙
	 */
class VideoController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'VideoDevice';

	/**
	 * 智能监控
	 * @time 2017-08-27
	 * @author abincc
	 **/
	public function deviceList() {

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map = array(
				'district_id' 	=> intval($member_info['district_id']),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);

		$list  = $this->page($this->table,$map,'add_time desc');

		foreach ($list as $k => $v) {
			$video = M('Video')->where(array('id'=>$v['uid']))->field('id,title,appkey,access_token,appsecret')->find();
			$list[$k]['video'] = $video;
		}

		$this->api_success('ok',(array)$list);
	}

}
