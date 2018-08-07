<?php
namespace Open\Controller\Pay;
	/**
	 * 支付宝支付
	 */
class AliController extends IndexController {
	/**
	 * 支付宝回调处理方法
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-17
	 */
	public function notify(){
		$data = I();
		if(!$data){
			exit('缺少信息');
		}
		$Pay = new \Think\Pay('alipay', C('payment.alipay'));
		$result = $Pay->verifyNotify($data);
		if(!$result) {
			$this->error('异常错误',U('Home/Index/Index/index'));
		}
		$info = $Pay->getInfo();
		if(!$info['status']) {
			$this->error('支付失败');
		}
		$order_info = get_info('capital_record', array('order_no' => $data['out_trade_no']));
		if(!$order_info||$order_info['status']!= '10'||$data['total_fee']!=(string)$order_info['money']){
			$temp['member_id'] = $order_info['to_member_id'];
			$temp['order_no'] = $data['out_trade_no'];
			$temp['money'] = $data['total_fee'];
			$temp['type'] = 13;
			$temp['remark'] = M('capital_record')->getLastSql();
			$temp['add_time'] = date('Y-m-d H:i:s');
			update_data('capital_error', array(), array(), $temp);
			$this->error("充值异常！",U('Home/Index/Index/index'));
		}
		$res = $this->funds_order($order_info, $data);
		if($res) {
			$this->error($res, U('Home/Index/Index/index'));
		}
		$Pay->notifySuccess();
	}
	/**
	 * 充值订单更新
	 * 1、改变订单状态
	 * 2、更新钱包
	 * 
	 * @param array $order_info 订单信息
	 * @param array $pay_info 第三方支付返回的信息
	 * @return string 事务结果
	 */
	public function funds_order($order_info, $pay_info){
		/**改变订单状态*/
		$data_1 = array(
			'id'=>$order_info['id'],
			'deal_no'=>$pay_info['trade_no'],
			'money'=>$order_info['money'],
			'status'=>16,
			'update_time'=>$pay_info['notify_time'],
		);
		/** 开启事务 */
		$flag = trans(function() use ($data_1,$order_info){
			/**资金记录*/
			$result[] = update_data('capital_record', array(), array(),$data_1);
			/**修改余额*/
			$result[] = M('member')->where(array('id'=>$order_info['to_member_id']))->setInc('balance',$order_info['money']);
			return $result;
		});
		return $flag;
	}
}
