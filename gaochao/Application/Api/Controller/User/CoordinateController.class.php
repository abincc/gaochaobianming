<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;
use Api\Controller\Base\HomeController;

	/**
	 * 坐标
	 */
class CoordinateController extends BaseController{
	/**
	 * 个人坐标
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function index() {
		
		$map = array(
				'is_del'	=> 0,
				'is_hid'	=> 0,
				'state'     => array('IN', array('0','2'))
			);
		$map['id'] = array('NEQ', $this->member_id);
		$map['district_id'] = I('district_id','','int');

		$field = array('id as member_id','mobile','nickname','sex','headimg', 'district_id');

		$list  = $this->page('member',$map,'type desc, login_time desc',$field,12);

		foreach ($list as $k => $v) {
			$list[$k]['headimg'] = get_avatar($v['member_id']);

	    	$where_son1['mid']  = $this->member_id;
		    $where_son1['oid']  = $v['member_id'];
		    	
		    $where_son2['mid'] = $v['member_id'];
		    $where_son2['oid'] = $this->member_id;
		    
		    $where_main['_complex'] = array(
		        $where_son1,
		        $where_son2,
		        '_logic' => 'or'
		    );

		    $member_info = M('MemberRoom')->where($where_main)->find();
		    if(empty($member_info)){
		    	$msg['id'] = 0;
				$msg['content'] = '暂无';
				$list[$k]['msg'] = $msg;
		    }else{
		    	//获取最后一个聊天信息
		    	$where_mm['rid'] =  $member_info['id'];
		    	$where_mm['type'] = array('NEQ', 5);
				$m_msg = M('MemberMessage')->where($where_mm)->field('id, content')->order('time desc')->limit(1)->select();
				if(!empty($m_msg)){
					$list[$k]['msg'] = $m_msg[0];
				}else{
					$msg['id'] = 0;
					$msg['content'] = '暂无';
					$list[$k]['msg'] = $msg;
				}
		    }
		}

		$this->api_success('ok',(array)$list);
	}

}
