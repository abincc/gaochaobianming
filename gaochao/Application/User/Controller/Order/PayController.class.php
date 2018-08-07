<?php
namespace User\Controller\Order;
use User\Controller\Base\BaseController;
class PayController extends BaseController {
	/**
	 * 展示支付页
	 */
	public function pay() {
		$this->display();
	}


	/**
	 * 余额支付
	 */
	public function pay_balance() {
		if(IS_POST) {
			$this->success('');
		}
	}
	
	/**
	 * 支付成功
	 * @return [type] [description]
	 */
	public function  pay_success(){
		/*支付成功 涨对应的积分值*/
		$this->display();
	}
	/**
	 * 订单在线支付
	 * 
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-20
	 */
	public function pay_online(){
		if(IS_POST){
			$this->error('请选择支付方式');
		}else{
			$this->display();
		}
	}
	
}