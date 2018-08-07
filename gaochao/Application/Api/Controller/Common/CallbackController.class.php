<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;

  /*支付回调*/
class CallbackController extends ApiController{
	/**
	 * 表名
	 * @var string
	 */
	protected $table  = 'Order';

	/**
	* 支付宝回调
	* 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
	* 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）
	* 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据
	* 的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	* 4、验证app_id是否为该商户本身。
	* 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，
	* 支付宝才会认定为买家付款成功
	* 支付宝验签通过，本地必须验证这四条，想加班的话就不用验证
	*/
	/**
	* 支付宝验签（新版RSA2）
	*
	*/
	public function ali_notify(){

		$notify  = $_POST;

    	write_debug(serialize($notify),'支付宝回调请求001');
		$apitype = 'alipay';
		// $payment = C('payment');
		$key_path = './Application/Common/Conf/Certs/alipay_public_key.pem';
		$key = file_get_contents($key_path);
		vendor('alipay.AopClient');
		$aop = new \AopClient();
		$aop->alipayrsaPublicKey = $key;
		// $notify = 'a:26:{s:10:"gmt_create";s:19:"2017-09-28 17:52:59";s:7:"charset";s:5:"UTF-8";s:12:"seller_email";s:17:"1182209951@qq.com";s:7:"subject";s:15:"App支付测试";s:4:"sign";s:344:"QTAFLh1GTdOM8TjyykIqurxgu94REoH1gJBS4ycIpt6CBcHbHjwwkzbwum62dfCbxRtraUXW/d/fYHWsUxS/LKyx8Rt1huVEMfhSDMwh3JejzkYtnLFU9WeKICMbKp02+C6+Ie/S+i5YdbMtaOZFv/yBgSiuEyAJ7wPBGM/2v8gQSR6CgVmPLmbkIOJ3jWdEYQKz7QYVCVuBYf08ta9pRcniMvaZ4bVB6AaeIbuJFM3pG3Kimdh582RLFaH2q6jPYjMwi13zxxNMTi402K28Eytl1+yNTUhgo1t7bYoa4D3iYjKrFE2EGAIdBeSEZN8zbT31YLpI3NfXNka8OOsuGA==";s:4:"body";s:18:"我是测试数据";s:8:"buyer_id";s:16:"2088302651732997";s:14:"invoice_amount";s:4:"0.01";s:9:"notify_id";s:34:"4400628e745eac421fe648af6d885b3nn2";s:14:"fund_bill_list";s:49:"[{"amount":"0.01","fundChannel":"ALIPAYACCOUNT"}]";s:11:"notify_type";s:17:"trade_status_sync";s:12:"trade_status";s:13:"TRADE_SUCCESS";s:14:"receipt_amount";s:4:"0.01";s:6:"app_id";s:16:"2016042901347956";s:16:"buyer_pay_amount";s:4:"0.01";s:9:"sign_type";s:4:"RSA2";s:9:"seller_id";s:16:"2088911997056539";s:11:"gmt_payment";s:19:"2017-09-28 17:53:00";s:11:"notify_time";s:19:"2017-09-28 17:53:00";s:7:"version";s:3:"1.0";s:12:"out_trade_no";s:16:"2017092854534897";s:12:"total_amount";s:4:"0.01";s:8:"trade_no";s:28:"2017092821001004990204169333";s:11:"auth_app_id";s:16:"2016042901347956";s:14:"buyer_logon_id";s:13:"747***@qq.com";s:12:"point_amount";s:4:"0.00";}';
		// $notify = unserialize($notify);
		//进行验签
		$result = $aop->rsaCheckV1($notify, NULL, "RSA2");
		write_debug($result,'支付宝验签');
		if($result && $notify['trade_status'] == 'TRADE_SUCCESS') {
			/** 用验签返回的订单号查询本地数据库*/
			$map  = array(
					'order_no'=>$notify['out_trade_no'],
				);
			$O    = D($this->table);
			$info = $O->where($map)->find();
			if(!$info['id']){
				write_debug(serialize($notify),'订单不存在');
				exit;
			}
			if(intval($info['status']) != 10){
				write_debug(serialize($notify),'订单状态不可支付-'.$info['status']);
				exit;
			}
			// write_debug(serialize($info),'支付宝订单信息');
			/** 判断本地金额和验签金额是否一致&&校验通知中的seller_id是否是该订单的操作方*/
			$total_amount = bcadd(floatval($order_info['money_total']),floatval($order_info['money_reduction'],2));
			//$total_amount == $notify['total_amount'] && 
			if($info['status'] == '10'){
				//核销卡券
				if($info['card_id']){
					$card_cancel = D('Card')->card_cancel($info['card_id'],$info['id']);
					if(!$card_cancel){
						write_debug($info['card_id'],'卡券核销失败');
					}
				}
				// write_debug($info['type'],'订单类型');
				$return = $O->pay($info['id'],1,$notify['trade_no']);
				if($return){
					$O->insert_log($info['member_id'],$info['id'],20,'订单支付成功(支付宝支付)');
					//消息推送
					D('Message')->send($info['member_id'],2,'订单的支付成功','订单编号为：'.$info['order_no'].'(支付宝)');

					echo 'success';exit;
				}else{
					echo 'fail';exit;
				}
			}else{
				write_debug('111','支付宝失败');
				echo 'fail';exit;
			}
			
		}
	}

	/**
	* 用户端微信回调
	*/
	public function wx_notify(){

		// $dd = 'a:16:{s:5:"appid";s:18:"wx6a40b01968ddd184";s:9:"bank_type";s:3:"CFT";s:8:"cash_fee";s:1:"1";s:8:"fee_type";s:3:"CNY";s:12:"is_subscribe";s:1:"N";s:6:"mch_id";s:10:"1486266702";s:9:"nonce_str";s:32:"6e923226e43cd6fac7cfe1e13ad000ac";s:6:"openid";s:28:"oOiyF0feIH7Skb5NsLMuDeMXJw8M";s:12:"out_trade_no";s:16:"2017072797481005";s:11:"result_code";s:7:"SUCCESS";s:11:"return_code";s:7:"SUCCESS";s:4:"sign";s:32:"B1A90E469F8B22697A547D20B57B9279";s:8:"time_end";s:14:"20170727154538";s:9:"total_fee";s:1:"1";s:10:"trade_type";s:3:"APP";s:14:"transaction_id";s:28:"4002572001201707272937264884";}';
		// $d = unserialize($dd);
		// print_r($d);exit;
		// write_debug(111,'微信支付回调信息1');

		$wechat = & load_wechat('Pay');
		$notifyInfo = $wechat->getNotify();
		//记录微信回调的数据，以便纠错 write_debug是我们项目常用的数据库调试方法，可以换成你们项目自己的
		write_debug($notifyInfo,'微信支付回调信息');
		// $notifyInfo = unserialize('{"appid":"wx5b6158570eb80fe3","bank_type":"HXB_DEBIT","cash_fee":"10","fee_type":"CNY","is_subscribe":"N","mch_id":"1252188801","nonce_str":"4c7z8xjpl375f2x63o8g1irmgjp623ny","openid":"opfEPuHbJ7mXMSNVAgAgwtjhhnCA","out_trade_no":"2017092853575151","result_code":"SUCCESS","return_code":"SUCCESS","sign":"A8D2BB6D5336383FCBF6F5582D9EC817","time_end":"20170928112039","total_fee":"10","trade_type":"APP","transaction_id":"4200000022201709284709960077"}');
		//如果微信推送数据验签成功
		if($notifyInfo['return_code']=='SUCCESS' && $notifyInfo['result_code']=='SUCCESS'){
			/* 查询订单是否存在 */
			$map  = array(
					'order_no'=>$notifyInfo['out_trade_no'],
				);
			$O    = D($this->table);
			$info = $O->where($map)->find();
			if(empty($info)){
				write_debug(serialize($notifyInfo),'订单不存在');
				$wechat->notifyReturn(false);
			}
			if(intval($info['status']) != 10){
				write_debug(serialize($notifyInfo),'订单状态不可支付'.$info['status']);
				$wechat->notifyReturn(false);
			}
			/* 比对金额是否一致 */
			// $wx_total = $notifyInfo['total_fee'] / 100;
			// if($info['money_total'] != $wx_total){
			// 	$wechat->notifyReturn(false);
			// }
			/** 验证通过更改订单状态,并通知微信*/
			$return = $O->pay($info['id'],2,'wx'.$notifyInfo['out_trade_no']);

			/* 通知微信 */
			if($return){
				//核销卡券
				if($info['card_id']){
					$card_cancel = D('Card')->card_cancel($info['card_id'],$info['id']);
					if(!$card_cancel){
						write_debug($info['card_id'],'卡券核销失败');
					}
				}
				$O->insert_log($info['member_id'],$info['id'],20,'订单支付成功(微信支付)');
				//消息推送
				D('Message')->send($info['member_id'],2,'订单的支付成功','订单编号为：'.$info['order_no'].'(微信支付)');

				$returnData['return_code'] = 'SUCCESS';
				$returnData['return_msg'] = 'OK';
				$wechat->replyXml($returnData);
			}else{
				write_debug(array($info['id'],2,'wx'.$notifyInfo['out_trade_no']),'return订单状态,变更失败');
				$wechat->notifyReturn(false);
			}
			
		}else{
			$wechat->notifyReturn(false);
		}
	}

	public function test(){

		$res=D('Message')->send(6,2,'订单的支付成功','订单编号为：1111111111111111111111111(支付宝)');
		bug($res);
	}

}