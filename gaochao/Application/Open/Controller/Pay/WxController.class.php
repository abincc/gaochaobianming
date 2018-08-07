<?php
namespace Open\Controller\Pay;
	/**
	 * 微信支付
	 */
class WxController extends IndexController {
	/**
	 * 微信回调处理方法
	 * 
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-20
	 */
	public function notify(){
		define('WXAPP', 1);
		/** 日志文件*/
		vendor('Wxpay.WxPay#Log');
		$log_file = dirname(C('LOG_PATH')).'/Pay/WeiXin/';
		$log_file .= date('y_m_d').'.log';
		$logHandler= new \CLogFileHandler($log_file);
		$log = \Log::Init($logHandler, 15);
		\Log::DEBUG("begin notify");
		/** 微信支付类引入*/
		vendor('Wxpay.WxPay#PayNotifyCallBack');
		//\WxPayConfig::$config = C('payment.wxpay');
		
		$PayNotify = new \PayNotifyCallBack();
		/** 验证签名，获取返回数据*/
		$data = $PayNotify->Handle(false);
		
		\Log::DEBUG("call back:" . json_encode($data));
		if(!is_array($data)) {
			$PayNotify->NotifyReturn(false);
			return ;
		}
		$msg = 'OK';
		if(!$PayNotify->NotifyProcess($data, $msg)) {
			$PayNotify->NotifyReturn(false);
			return ;
		}
		
		$param=json_decode($data['attach'],true);
		$map['order_no']=$data['out_trade_no'];
		$order_info = get_info('capital_record', $map);

		if(!$order_info||$order_info['status']!= 10||$data['total_fee']!=(string)($order_info['money']*100)){
			$temp['member_id'] = $param['member_id'];
			$temp['order_no'] = $data['out_trade_no'];
			$temp['money'] = $data['total_fee']/100;
			$temp['type'] = 12;
			$temp['addtime'] = date('Y-m-d H:i:s');
			$flag = update_data('capital_error', array(), array(), $temp);
			$this->error("充值异常！",U('Home/Index/Index/index'));
		}
		$result = $this->funds_order($order_info,$data);
		
		if($result) {
			$PayNotify->NotifyReturn(false);
			return ;
		}
		$PayNotify->NotifyReturn(true, false);
	}
	
	/**
	 * 微信二维码生成
	 * 
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-18
	 */
	public function create_code(){
		vendor("Wxpay.phpqrcode.phpqrcode");
		$url = urldecode($_GET["data"]);
		\QRcode::png($url, false, QR_ECLEVEL_L, 8);
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
			'deal_no'=>$pay_info['transaction_id'],
			'money'=>$order_info['money'],
			'status'=>16,
			'update_time'=>$pay_info['time_end'],
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
