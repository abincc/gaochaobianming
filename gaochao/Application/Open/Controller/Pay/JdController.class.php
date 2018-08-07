<?php
namespace Open\Controller\Pay;
	/**
	 * 京东支付
	 */
class JdController extends IndexController {
	/**
	 * 异步回调处理方法
	 * @param array data 
			"version":"V2.0",
			"merchant":"110239072003",
			"result":{
				"code":"000000",
				"desc":"success"
			},
			"tradeNum":"2016101456485149",
			"tradeType":"0",
			"sign":"EOr8dkcbWBu9pf6NPb5+rbIfuUyFCK1rW\/4QY\/y84dl6ljJB264KgGhy4Fxmf8d1FWzqh6rVHlSZsY7Kpxr9EiRDhd8mPGtvaH0e3rPtGQY++hs4h58\/noayeQ84gNddQ2GALxFnzi5YzdjAzVO85uCFvFof+kz1uzzv4iqMlHw=",
			"amount":"1",
			"status":"2",
			"payList":{
				"pay":{
					"payType":"0",
					"amount":"1",
					"currency":"CNY",
					"tradeTime":"20161014111934",
					"detail":{
						"cardHolderName":"*\u6653\u6b66",
						"cardHolderMobile":"134****2024",
						"cardHolderType":"0",
						"cardHolderId":"****281X",
						"cardNo":"621483****1581",
						"bankCode":"CMB",
						"cardType":"1"
					}
				}
			}
	 * @author	秦晓武
	 * @date	2016-10-13
	 */
	public function notify(){
		$xml = $GLOBALS['HTTP_RAW_POST_DATA'];
		$Pay = new \Think\Pay('jdpay', C('payment.jdpay'));
		$result = $Pay->verifyNotify($xml);
		$data = $Pay->getInfo();
		if(!$result){
			update_data('capital_error', array(), array(), ['remark'=>'验签失败:' . json_encode($data)]);
			return false;
		}
		/*验证状态*/
		if($data['result']['code'] !== '000000' || $data['result']['desc'] !== 'success' || $data['status'] != 2){
			update_data('capital_error', array(), array(), [
				'order_no' => $data['tradeNum'],
				'remark' => '状态错误:' . json_encode($data['result'])
			]);
			return false;
		}
		$order_info = get_info('capital_record', array('order_no' => $data['tradeNum']));
		
		/*记录错误*/
		if(!$order_info||$order_info['status']!= '10'||($data['amount']!=(string)($order_info['money']*100))){
			$temp['member_id'] = $order_info['to_member_id'];
			$temp['order_no'] = $data['tradeNum'];
			$temp['money'] = $data['amount']/100;
			$temp['type'] = 13;
			$remark['sql'] = M('capital_record')->getLastSql();
			$remark['order_info'] = $order_info;
			$remark['data'] = $data;
			$temp['remark'] = json_encode($remark);
			$temp['add_time'] = date('Y-m-d H:i:s');
			update_data('capital_error', array(), array(), $temp);
			return false;
		}
		/*更新数据*/
		$res = $this->funds_order($order_info, $data);
		if($res) {
			$this->error($res, U('Home/Index/Index/index'));
		}
		$Pay->notifySuccess();
	}
	
	/**
	 * 同步回调处理方法
	 * 
	 * @author	秦晓武
	 * @date	2016-10-13
	 */
	public function callback(){
		$config = C('payment.jdpay');
		$desKey = $config["desKey"];
		$keys = base64_decode($desKey);
		$param;
		vendor('Jdpay.Util.TDESUtil','','.class.php');
		vendor('Jdpay.Util.SignUtil','','.class.php');
		vendor('Jdpay.Util.RSAUtils','','.class.php');
		if($_POST["tradeNum"] != null && $_POST["tradeNum"]!=""){
			$param["tradeNum"]=\TDESUtil::decrypt4HexStr($keys, $_POST["tradeNum"]);
		}
		if($_POST["amount"] != null && $_POST["amount"]!=""){
			$param["amount"]=\TDESUtil::decrypt4HexStr($keys, $_POST["amount"]);
		}
		if($_POST["currency"] != null && $_POST["currency"]!=""){
			$param["currency"]=\TDESUtil::decrypt4HexStr($keys, $_POST["currency"]);
		}
		if($_POST["tradeTime"] != null && $_POST["tradeTime"]!=""){
			$param["tradeTime"]=\TDESUtil::decrypt4HexStr($keys, $_POST["tradeTime"]);
		}
		if($_POST["note"] != null && $_POST["note"]!=""){
			$param["note"]=\TDESUtil::decrypt4HexStr($keys, $_POST["note"]);
		}
		if($_POST["status"] != null && $_POST["status"]!=""){
			$param["status"]=\TDESUtil::decrypt4HexStr($keys, $_POST["status"]);
		}
		
		$sign =  $_POST["sign"];
		$strSourceData = \SignUtil::signString($param, array());
		//echo "strSourceData=".htmlspecialchars($strSourceData)."<br/>";
		//$decryptBASE64Arr = base64_decode($sign);
		$decryptStr = \RSAUtils::decryptByPublicKey($sign);
		//echo "decryptStr=".htmlspecialchars($decryptStr)."<br/>";
		$sha256SourceSignString = hash ( "sha256", $strSourceData);
		//echo "sha256SourceSignString=".htmlspecialchars($sha256SourceSignString)."<br/>";
		if($decryptStr!=$sha256SourceSignString){
			$this->error("验证签名失败！");
		}else{
			$_SESSION["tradeResultRes"]=$param;
			$this->success("支付成功",U('User/Capital/Recharge/pay_funds_success'));
		}
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
			'deal_no'=>$pay_info['tradeNum'],
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
