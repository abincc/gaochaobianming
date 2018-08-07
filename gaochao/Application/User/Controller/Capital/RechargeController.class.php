<?php
namespace User\Controller\Capital;
/**
 * 充值
 */
class RechargeController extends IndexController{
	/**
	 * 充值记录表
	 * @var string
	 */
	protected $table = 'capital_record';
	/**
	 * 表单
	 */
	public function index(){
		if(IS_POST){
			$this->recharge();
		}else{
			$this->display();
		}
	}
	/**
	 * 微信
	 */
	public function weixin(){
		$this->display();
	}
	/**
	 * 处理充值
	 * @pay_type
	 * 		12: 微信
	 * 		13：支付宝
	 * 		14：京东支付
	 */
	public function recharge(){
		$posts = I('post.');
		if(!in_array($posts['pay_type'], array(12,13,14))){
			$this->error('请选择支付方式',U('recharge'));
		}
		if(!preg_match('/^[1-9]\d{0,3}(\.\d{1,2})?$|^0(\.\d{1,2})?$/',$posts['price'])){
			$this->error('请输入0-10000之间的合法金额',U('recharge'));
		}
		/*先创建订单*/
		$data = array(
			'type'=>$posts['pay_type'],
			'order_no'=>build_order_no(),
			'money'=>$posts['price'],
			'from_member_id'=>0,
			'to_member_id'=>session('member_id'),
			'status'=> 10,
			'add_time'=>date('Y-m-d H:i:s'),
		);
		$id = update_data($this->table, array(), array(),$data);
		if(is_numeric($id)){
			switch($posts['pay_type']){
				/*微信充值*/
				case 12:
					$this->wxpay($id);
					break;
				/*支付宝充值*/
				case 13:
					$this->alipay($id);
					break;
				/*京东充值*/
				case 14:
					$this->jdpay($id);
					break;
				default:
					$this->error('系统错误');
			}
		}else{
			$this->error('订单生成失败,请重试');
		}
	}
	
	/**
	 * 资金充值信息
	 * @param int $id 支付记录ID
	 * @return array 配置数组
	 */
	public function check_funds($id){
		/** 查找支付记录 */
		$order_info = get_info($this->table, array('id' => $id));
		if($order_info['status'] != 10) {
			$this->error("订单不存在或者无法支付！");
		}
		/** 必要的一些信息，用来产生动态时调用相关数据 */
		$config['param'] = array(
			'order_id'=> $order_info['id'],
			'member_id'=> $order_info['member_id'],
		);
		/** 商品标题 */
		$config['title']="充值";
		
		/** 支付金额 */
		$config['price']=$order_info['money'];
		/** 本平台支付订单号， */
		$config['no']=$order_info['order_no'];
		/** 支付完成后的跳转地址 */
		$config['url']=U('pay_funds_success');

		$config['order_info']=$order_info;
		return $config;
	}

	/**
	 * 支付宝支付 
	 * @param int $id	订单ID
	 * @return string 跳转支付宝支付页
	 * 
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-17
	 */
	public function alipay($id){
		$config=$this->check_funds($id);
		$method = array(
			'directPay' => '支付宝账户余额',
			'cartoon' => '卡通',
			'bankPay' => '网银',
			'cash' => '现金',
			//'creditCardExpress' => '信用卡快捷',
			'debitCardExpress' => '借记卡快捷',
			'coupon' => '红包',
			'point' => '积分',
			'voucher' => '购物券'
		);
		$PayVo = new \Think\Pay\PayVo();
		$PayVo->setType('alipay')
			->setBody("支付成功后请不要关闭窗口，等待自动跳转")
			->setFee($config['price']) 	/** 支付金额 */
			->setOrderNo($config['no'])	/** 本平台支付订单号， */
			->setTitle($config['title'])				/** 商品标题 */
			->setCallback('http://'.I('server.HTTP_HOST').U('Open/Pay/Ali/notify'))		/** 支付完成后的后续操作接口 */
			->setUrl('http://'.I('server.HTTP_HOST').U("pay_funds_success",array('id'=>$id)))		/** 支付完成后的跳转地址 */
			->setPayMethod(array_keys($method))		/** 支付渠道 */
			->setParam(implode(',', $config['param'])); 			/** 必要的一些信息，用来产生动态时调用相关数据 */
		$payment_conf = C('payment.alipay');
		$Pay = new \Think\Pay('alipay', $payment_conf);
		echo $Pay->buildRequestForm($PayVo);
		exit;
	}
	
	/**
	 * 微信支付提交(模式二，获取二维码)
	 * @param int $id	订单ID
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-17
	 */
	public function wxpay($id){
		/*生成配置信息*/
		$config=$this->check_funds($id);
		/*微信支付类引入*/
		define('WXAPP', 1);
		vendor('Wxpay.WxPay#NativePay');
		vendor('Wxpay.log');
		$attach = json_encode($config['param']);
		$NativePay = new \NativePay();
		$input = new \WxPayUnifiedOrder();
		
		$input->SetBody($config['title']);
		$input->SetAttach($attach);
		$input->SetOut_trade_no($config['no']);

		$price = ($config['price']) * 100;
		$input->SetTotal_fee($price);
		$input->SetTime_start(date("YmdHis"));
		$input->SetTime_expire(date("YmdHis", time() + 1800));

		$input->SetGoods_tag("充值");
		$input->SetNotify_url('http://'.I('server.HTTP_HOST').U("Open/Pay/Wx/notify"));
		$input->SetTrade_type("NATIVE");
		$input->SetProduct_id("123456789");
		/*禁止信用卡*/
		$input->SetLimit_pay('no_credit');
		$result = $NativePay->GetPayUrl($input);

		$result['show_url'] = 'http://'.I('server.HTTP_HOST').U('Open/Pay/Wx/create_code').'?data='.urlencode($result["code_url"]);
		$this->assign('result', $result);
		$this->assign('info', $config);	
		$this->display('weixin');
	}
	
	/**
	 * 京东支付提交
	 * @param int $id	订单ID
	 * @return string 跳转京东支付页
	 * @author	秦晓武
	 * @date	2016-10-13
	 */
	public function jdpay($id){
		$config=$this->check_funds($id);
		
		$PayVo = new \Think\Pay\PayVo();
		$PayVo->setType('jdpay')
			->setFee($config['price']) 	/** 支付金额 */
			->setOrderNo($config['no'])	/** 本平台支付订单号， */
			->setTitle($config['title']) /** 商品标题 */
			->setCallback('http://'.I('server.HTTP_HOST').U('Open/Pay/Jd/notify'))		/** 支付完成后的后续操作接口 */
			->setUrl('http://'.I('server.HTTP_HOST').U("pay_funds_success",array('id'=>$id)))		/** 支付完成后的跳转地址 */
			->setParam(implode(',', $config['param'])); 			/** 必要的一些信息，用来产生动态时调用相关数据 */
		$payment_conf = C('payment.jdpay');
		$Pay = new \Think\Pay('jdpay', $payment_conf);
		echo $Pay->buildRequestForm($PayVo);
		exit;
	}
	/**
	 * 测试支付提交
	 * @param int $id	订单ID
	 * @return string 跳转京东支付页
	 * @author	秦晓武
	 * @date	2016-10-13
	 */
	public function cspay($id){
		$config=$this->check_funds($id);
		
		$PayVo = new \Think\Pay\PayVo();
		$PayVo->setType('cspay')
			->setFee($config['price']) 	/** 支付金额 */
			->setOrderNo($config['no'])	/** 本平台支付订单号， */
			->setTitle($config['title']) /** 商品标题 */
			->setCallback('http://'.I('server.HTTP_HOST').U('Open/Pay/Cs/notify'))		/** 支付完成后的后续操作接口 */
			->setUrl('http://'.I('server.HTTP_HOST').U("pay_funds_success",array('id'=>$id)))		/** 支付完成后的跳转地址 */
			->setParam(implode(',', $config['param'])); 			/** 必要的一些信息，用来产生动态时调用相关数据 */
		$payment_conf = C('payment.cspay');
		$Pay = new \Think\Pay('cspay', $payment_conf);
		echo $Pay->buildRequestForm($PayVo);
		exit;
	}
	/**
	 * 订单状态查询
	 * 
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-18
	 */
	public function order_status(){
		$member_id = session('member_id');
		if(!IS_POST || !$member_id || !is_numeric(I('id'))) {
			$this->error('数据错误');
		}
		$map = array(
			'id'=> I('id'),
			'member_id'=> $member_id,
			'status'=> 16,
		);
		$order_info = get_info('capital_record',$map);
		if($order_info['id']) {
			$this->success('支付成功', U('pay_funds_success?id='.$id));
			return ;
		}
		$this->error('未支付成功');
	}
	
	/**
	 * 微信支付成功跳转页
	 * 
	 * @author	李东<947714443@qq.com>
	 * @date	2016-04-20
	 */
	public function pay_funds_success(){
		$id = I('id');
		$order_info = get_info('capital_record',array('id'=>$id));
		$data['order_info']=$order_info;
		$this->assign($data);
		$this->display();
	}
}
