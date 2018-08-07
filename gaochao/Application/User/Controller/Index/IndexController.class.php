<?php
namespace User\Controller\Index;
use User\Controller\Base\BaseController;
/**
 * 个人中心首页
 */
class IndexController extends BaseController { 

	/**
	 * 个人中心首页
	 */
	public function index(){
		$member_id = session('member_id');  

		/*取出订单总数*/
		//$order_nums = count_data('order',array('member_id'=>$member_id,'is_del'=>0,'is_hid'=>0,'status'=>array('in',array('10','20','30','40','49','90'))));
		$data['order_nums'] = intval($order_nums);
		/*取出收藏数量*/
		//$collect_nums = count_data('collect',array('member_id'=>$member_id,'is_del'=>0,'is_hid'=>0));
		$data['collect_nums'] = intval($collect_nums);
		$this->assign($data);
		$this->display();
	}
}