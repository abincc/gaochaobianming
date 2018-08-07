<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 小事帮忙
	 */
class ParkingController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table 		='Parking';

	/**
	 * 列表 - 小区车位列表
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function index() {

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map   = array(
				'district_id' 	=> intval($member_info['district_id']),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = array('id as parking_id','title','image');
		$list  = $this->page($this->table,$map,'sort desc,add_time desc',$field);

		if(!empty($list)){
			array_walk($list, function(&$a){
				$a['image'] = file_url($a['image']);
			});
		}

		$this->api_success('ok',(array)$list);
		
	}

}
