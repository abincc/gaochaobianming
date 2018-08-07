<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 订单
	 */
class OrderController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table  = 'Order';
	protected $order_status = array();

	/**
	 * 个人订单列表 --- 全部
	 * @time 2017-08-23
	 * @author llf
	 **/
	public function index() {
		
		$map   = array(
				'member_id'	=> $this->member_id,
				'status'	=> array('IN',array(5,10,20,30,40,50,60,70)),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		if(!empty($this->order_status)){
			$map['status'] = array('IN',$this->order_status);
		}
		if(I('status')){
			$map['status'] = I('status',0,'int');
		}
		$field = array('id as order_id','order_no','money_total','status','add_time');
		$list  = $this->page($this->table,$map,'add_time desc',$field,12);
		//M($this->table)->where($map)->order('add_time desc')->field($field)->select();
		if(!empty($list)){
			$order_ids    = array_column($list,'order_id');
			$condition    = array(
								'order_id' => array('IN',$order_ids),
							);
			$field = array('id as order_detail_id','order_id','product_id','product_title','product_price','product_num','product_image','product_spec_value','comment_status');
			$order_detail_list = M('Order_detail')->where($condition)->field($field)->select();
			$temp = array();
			foreach ($order_detail_list as $key => $val) {
				$val['product_image'] 		= file_url($val['product_image']);
				$val['product_spec_value']  = implode(' ', unserialize($val['product_spec_value']));
				$temp[$val['order_id']][] 	= $val;
			}
			$status = get_table_state($this->table,'status');
			foreach ($list as $k => $v) {
				$list[$k]['product_info'] = $temp[$v['order_id']]; 
				$list[$k]['status_title'] = $status[$v['status']]['title'];
				$list[$k]['add_time']	  = date('Y-m-d H:i',strtotime($v['add_time']));
			}
		}

		$this->api_success('ok',(array)$list);
	}

	/**
	 * 待支付
	 * @time 2017-08-23
	 * @author llf
	 **/
	public function unpaid(){
		$this->order_status = array(10);
		$this->index();
	}

	/**
	 * 待收货
	 * @time 2017-08-23
	 * @author llf
	 **/
	public function received(){
		$this->order_status = array(20,30);
		$this->index();
	}

	/**
	 * 待评价
	 * @time 2017-08-23
	 * @author llf
	 **/
	public function evaluat(){
		$this->order_status = array(40);
		$this->index();
	}


	/**
	 * 个人订单列表
	 * @time 2017-08-23
	 * @author llf
	 **/
	public function info(){
		$map 	= array(
				'member_id'	=> $this->member_id,
				'id'		=> I('order_id',0,'int'),
				'status'	=> array('EGT',5),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$O		= D('Order'); 
		$field  = array('id as order_id','order_no','money_total','money_reduction','receipt_address','status','add_time','pay_time','ship_time','confirm_time');
		$info 	= $O->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('订单信息不存在');
		}
		$status = get_table_state('order','status');
		$info['status_title']	 = $status[$info['status']]['title'];
		$info['address'] = (array)unserialize($info['receipt_address']);
		unset($info['receipt_address']);

		//获取子订单
		$condition = array('order_id' => $info['order_id']);
		$field = array('id as order_detail_id','product_id','product_title','product_price','product_num','product_image','product_spec_value','comment_status');
		$info['product_info'] = M('Order_detail')->where($condition)->field($field)->select();
		if(empty($info['product_info'])){
			$this->api_error('订单商品信息不存在');
		}
		array_walk($info['product_info'], function(&$a){
			$a['product_image'] = file_url($a['product_image']);
			$a['product_spec_value'] = implode(' ', unserialize($a['product_spec_value']));
		});

		//获取订单日志
		$info['log'] = (array)$O->get_log($info['order_id']);

		if($info['pay_time'] == '0000-00-00 00:00:00')		$info['pay_time'] = '';
		if($info['ship_time'] == '0000-00-00 00:00:00')		$info['ship_time'] = '';
		if($info['confirm_time'] == '0000-00-00 00:00:00')	$info['confirm_time'] = '';

		//订单实付
		$info['money_total'] = bcsub(floatval($info['money_total']),floatval($info['money_reduction']),2);

		$this->api_success('ok',$info);
	}


	/**
	 * 生成订单
	 * @time 2017-08-23
	 * @author llf
	 **/
	public function create(){
		
		if(IS_POST){
			$address_id = I('post.address_id',0,'int');
			$cart_ids   = I('post.cart_ids','');
			$remark		= I('post.remark','','trim');
			$card_id    = I('post.card_id',0,'int');

			write_debug(I(),'购物车下单提交参数');
			if(!$address_id){
				$this->api_error('请选择收货地址');
			}
			$address_info = D('Address')->get_address($this->member_id,$address_id);
			if(empty($address_info)){
				$this->api_error('地址信息不存在');
			}
			$cart_list = D('Cart')->get_cart_selcet($this->member_id,$cart_ids);
			if(empty($cart_list)){
				$this->api_error('选定购车商品信息不存在');
			}
			/*验证库存*/
			// $sku_ids = array_filter(array_column($cart_list,'sku_id'));
			// if(empty($sku_ids)){
			// 	$this->api_error('库存验证失败');
			// }
			// $inventory_list = M('product_sku')->where(array('id'=>array('IN',$sku_ids)))->getField('id,inventory');
			// array_walk($cart_list, function(&$a) use($inventory_list){
			// 	if($a['product_num'] > $inventory_list['id']){
			// 		$this->api_error($a['product_title'].' '.implode(' ', array_values(unserialize($a['product_spec_value']))).' 库存不足');
			// 	}
			// });	

			/**优惠劵*/
			if($card_id){
				$condition = array(
						'is_del'	=>0,
						'is_hid'	=>0,
						'id'		=>$card_id,
						'member_id'=>$this->member_id,
					);
				$card_info = D('CardView')->where($condition)->field('id,amount,status')->find();
				if(empty($card_info)){
					$this->api_error('优惠劵信息不存在');
				}
				if($card_info['status'] != 1){
					$this->api_error('优惠不可使用');
				}
			}

			$O = D('Order');
			//计算物流
			$freight = $O->get_freight(array_column($cart_list,'product_id'));
			//计算件数、小计、总计
			$data = $O->calculate($cart_list);
			$data['freight']		= $freight;
			$data['money_total']	= bcadd($data['subtotal'],$freight,2);
			$data['money_reduction'] = 0.00;
			if(!empty($card_info['amount'])){
				if(floatval($card_info['amount']) >= $data['money_total']){
					$this->api_error('优惠券使用门槛超出订单金额');
				}
				$data['money_reduction'] = floatval($card_info['amount']);
				$data['card_id']		 = intval($card_info['id']);
			}
			$data['order_no']		= build_order_no();

			$data['detail_title']   = implode('、', array_unique(array_column($cart_list,'product_title')));
			$data['receipt_mobile'] = $address_info['mobile'];
			$data['receipt_address']= serialize($address_info);
			$data['member_id']		= $this->member_id;
			$data['remark']			= $remark;
			$data['status']			= 10;

			try {
				$M = M();
				$M->startTrans();
				$order_id     = $O->create_order($data);
				if(!is_numeric($order_id)){
					throw new \Exception($O->getError());
				}
				//生成子订单
				$order_detail = $O->create_order_detail($order_id,$cart_list);
				if(!$order_detail){
					throw new \Exception('订单详情生成失败');
				}
				if($data['card_id']){
					M('coupon_card')->where('id='.$data['card_id'])->setField('status',2);
				}
				//插入订单日志
				$O->insert_log($data['member_id'],$order_id,10,'客户下单');

			} catch (\Exception $e) {
				$M->rollback();
				$this->api_error($e->getMessage());
			}
			$M->commit();

			//清空选择的购物车
			D('Cart')->del($data['member_id'],$cart_ids);

			//消息推送
			D('Message')->send($data['member_id'],2,'订单的生成成功','生成订单，订单编号为：'.$data['order_no']);

			$info = array(
					'order_no' 			=> $data['order_no'],
					'money'				=> bcsub(floatval($data['money_total']),floatval($data['money_reduction']),2),
					'desc'				=> '订单支付描述',
					'order_id'			=> $order_id,
					'callback_url_ali'	=> C('CALLBACK_URL_ALI'),
					'callback_url_wx'	=> C('CALLBACK_URL_WX'),
				);
			$this->api_success('订单生成成功',$info);

		}else{

			$cart_ids = I('cart_ids','');
			if(empty($cart_ids))		$this->api_error('未选定购物车中的商品');
			if(!is_array($cart_ids))	$this->api_error('提交参数错误');

			//获取购车数据
			$cart_list = D('Cart')->get_cart_selcet($this->member_id,$cart_ids);

			if(empty($cart_list)){
				$this->api_error('选定商品信息不存在');
			}
			array_walk($cart_list, function(&$a){
				$a['product_spec_value'] 	= implode(' ', unserialize($a['product_spec_value']));
				$a['product_image']			= file_url($a['product_image']);
			});

			//获取默认地址
			$default_address = D('Address')->get_address($this->member_id);

			//计算物流
			$freight 		 = D('Order')->get_freight(array_column($cart_list,'product_id'));

			//计算件数、小计、总计
			$data = D('Order')->calculate($cart_list);
			$data['freight'] 			= $freight;
			$data['money_total']		= bcadd($data['subtotal'],$freight,2);
			if(!empty($default_address))	$data['address'] = $default_address;
			$data['cart_list'] 			= $cart_list;

			/*优惠劵*/
			$data['card_num'] = D('Card')->usable_count($this->member_id,$data['money_total']);

			$this->api_success('OK',$data);
		}
	}

	/**
	 * 立即购买
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function buy_now(){
		
		if(IS_POST){

			$sku_id = I('sku_id',0,'int');
			$num 	= abs(I('num',0,'int'));
			$address_id = I('post.address_id',0,'int');
			$card_id    = I('post.card_id',0,'int');

			if(!$sku_id)			$this->api_error('商品sku_id 必须');
			if(!$num)				$this->api_error('购买数量必须大于0');
			if(!$address_id)		$this->api_error('请选择收货地址');
		
			//获取购车数据
			$field = array('id as sku_id','product_id','price','inventory','image','spec_value');
			$product_sku_info = M('product_sku')->where('id='.$sku_id)->field($field)->find();
			if(empty($product_sku_info)){
				$this->api_error('选定商品规格信息不存在');
			}
			if($product_sku_info['inventory'] < $num){
				$this->api_error('商品库存不足');
			}
			$condition = array(
					'id'	 =>intval($product_sku_info['product_id']),
					'is_del' =>0,
					'is_hid' =>0,	
				);
			$field = array('id as product_id','title','freight','is_sale');
			$product_info = M('Product')->where($condition)->field($field)->find();
			if(empty($product_info)){
				$this->api_error('选定商品信息不存在');
			}
			if(!$product_info['is_sale']){
				$this->api_error('选定商品已下架');
			}
			//地址
			$address_info = D('Address')->get_address($this->member_id,$address_id);
			if(empty($address_info)){
				$this->api_error('地址信息不存在');
			}
			//优惠劵
			if($card_id){
				$condition = array(
						'is_del'	=>0,
						'is_hid'	=>0,
						'id'		=>$card_id,
						'member_id'=>$this->member_id,
					);
				$card_info = D('CardView')->where($condition)->field('id,amount,status')->find();
				if(empty($card_info)){
					$this->api_error('优惠劵信息不存在');
				}
				if($card_info['status'] != 1){
					$this->api_error('优惠不可使用');
				}
			}

			//计算物流
			$freight 		 = $product_info['freight'];

			//计算件数、小计、总计
			$data = array();
			$data['num_total'] 			= $num;
			$data['subtotal']  			= ($num * floatval($product_sku_info['price']));
			$data['freight'] 			= $freight;
			$data['money_total']		= bcadd($data['subtotal'],$freight,2);
			$data['money_reduction'] 	= 0.00;
			// $data['default_address'] 	= $default_address;

			if(!empty($card_info['amount'])){
				if(floatval($card_info['amount']) >= $data['money_total']){
					$this->api_error('优惠券使用门槛超出订单金额');
				}
				$data['money_reduction'] = floatval($card_info['amount']);
				$data['card_id']		 = intval($card_info['id']);
			}


			$data['order_no']		= build_order_no();
			$data['detail_title']   = $product_info['title'];
			$data['receipt_mobile'] = $address_info['mobile'];
			$data['receipt_address']= serialize($address_info);
			$data['member_id']		= $this->member_id;
			$data['remark']			= $remark;
			$data['status']			= 10;

			$cart_list   = array();
			$cart_list[] = array(
	                'product_id'            => intval($product_sku_info['product_id']),
	                'product_title'         => $product_info['title'],
	                'product_price'         => $product_sku_info['price'],
	                'product_num'           => $num,
	                'product_spec_value'    => $product_sku_info['spec_value'],
	                'product_image'         => $product_sku_info['image'],
				);

			$O = D('Order');
			try {
				$M = M();
				$M->startTrans();

				$order_id     = $O->create_order($data);
				if(!is_numeric($order_id)){
					throw new \Exception($O->getError());
				}
				//生成子订单
				$order_detail = $O->create_order_detail($order_id,$cart_list);
				if(!$order_detail){
					throw new \Exception('订单详情生成失败');
				}
				//标记卡券已使用
				if($data['card_id']){
					M('coupon_card')->where('id='.$data['card_id'])->setField('status',2);
				}
				//插入订单日志
				$O->insert_log($data['member_id'],$order_id,10,'客户下单');
				
			} catch (\Exception $e) {
				$M->rollback();
				$this->api_error($e->getMessage());
			}
			$M->commit();

			//消息推送
			D('Message')->send($data['member_id'],2,'订单的生成成功','生成订单，订单编号为：'.$data['order_no']);

			$info = array(
					'order_no' 	=> $data['order_no'],
					'money'		=> bcsub(floatval($data['money_total']),floatval($data['money_reduction']),2),
					'desc'		=> '订单支付描述',
					'order_id'	=> $order_id,
					'callback_url_ali'	=> C('CALLBACK_URL_ALI'),
					'callback_url_wx'	=> C('CALLBACK_URL_WX'),
				);
			$this->api_success('订单生成成功',$info);

		}else{

			$sku_id = I('sku_id',0,'int');
			$num 	= abs(I('num',0,'int'));

			if(!$sku_id)	$this->api_error('商品sku_id 必须');
			if(!$num)		$this->api_error('购买数量必须大于0');
		
			//获取购车数据
			$field = array('id as sku_id','product_id','price','inventory','image','spec_value');
			$product_sku_info = M('product_sku')->where('id='.$sku_id)->field($field)->find();
			if(empty($product_sku_info)){
				$this->api_error('选定商品规格信息不存在');
			}
			if($product_sku_info['inventory'] < $num){
				$this->api_error('商品库存不足');
			}
			$condition = array(
					'id'	 =>intval($product_sku_info['product_id']),
					'is_del' =>0,
					'is_hid' =>0,	
				);
			$field = array('id as product_id','title','freight','is_sale');
			$product_info = M('Product')->where($condition)->field($field)->find();
			if(empty($product_info)){
				$this->api_error('选定商品信息不存在');
			}
			if(!$product_info['is_sale']){
				$this->api_error('选定商品已下架');
			}
			//获取默认地址
			$default_address = D('Address')->get_address($this->member_id);

			//计算物流
			$freight		 = $product_info['freight'];

			//计算件数、小计、总计
			$data = array();
			$data['num_total']			= $num;
			$data['subtotal']			= ($num * floatval($product_sku_info['price']));
			$data['freight']			= $freight;
			$data['money_total']		= bcadd($data['subtotal'],$freight,2);

			if(!empty($default_address))	$data['address'] = $default_address;

			unset($product_sku_info['is_sale']);
			unset($product_sku_info['inventory']);

			$product_sku_info['product_title'] = $product_info['title'];
			$product_sku_info['product_num']   = $num;
			$product_sku_info['image'] 	   	   = file_url($product_sku_info['image']);
			unset($product_info);
			$product_sku_info['spec_value']    = implode(' ', unserialize($product_sku_info['spec_value']));
			$data['product_info']			   = $product_sku_info;
			
			/*优惠劵*/
			$data['card_num'] = D('Card')->usable_count($this->member_id,$data['money_total']);
			$this->api_success('OK',$data);
		}

	}

	/**
	 * 商品评价
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function comment(){

		if(!IS_POST)	$this->api_error('提交方式错误');	
		$data = array();
		$data['order_detail_id'] = I('order_detail_id',0,'int');
		$data['content'] 		 = I('content','','trim');
		$data['member_id']		 = $this->member_id;
		$data['star']			 = I('star','','floatval');

		$map = array(
				'id'		=> $data['order_detail_id'],
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$order_detail_info = M('order_detail')->where($map)->find();
		if(empty($order_detail_info))				$this->api_error('订单信息不存在');
		if($order_detail_info['comment_status'])	$this->api_error('商品已评价');
		$product_info = M('product')->where('id='.intval($order_detail_info['product_id']))->find();
		if(empty($order_detail_info))	$this->api_error('商品信息不存在');

		$data['product_id'] = $order_detail_info['product_id'];
		
		//验证主订单用户
		$condition = array(
				'member_id'	=> $this->member_id,
				'id'		=> intval($order_detail_info['order_id']),
			);
		$O = D('Order');
		$order_info = M('Order')->where($condition)->field(array('id as order_id','status','member_id'))->find();
		if($order_info['member_id'] != $this->member_id){
			if(empty($order_detail_info))	$this->api_error('您无权评价');
		}

		$img[] = $_FILES['img'];

		if(!empty($_FILES['img']))	$data['is_img'] = 1;
		
		try {
			$M = M();
			$M->startTrans();
			$comment_id	= update_data(D('OrderComment'),[],[],$data);
			if(!is_numeric($comment_id)){
				throw new \Exception($comment_id);
			}
			//变更子订单评价状态
			$is_comment = M('order_detail')->where($map)->setField('comment_status',1);
			if(!is_numeric($is_comment)){
				throw new \Exception('评价状态更新失败');
			}

			//判断整个订单是否全部评价
			D('OrderComment')->order_commented($order_detail_info['order_id']);

			if(!empty($_FILES['img'])){
				// if(count($img) > 9){
				// 	throw new \Exception('上传图片数量不能超过9张');
				// }
				$config = array(
						'maxSize'    => 3145728,
						'rootPath'   => './',
						'savePath'   => 'Uploads/Order/Comment/', 
						'saveName'   => array('uniqid',''),
						'exts'       => array('jpg', 'gif', 'png', 'jpeg'),
						'autoSub'    => true,    
						'subName'    => array('date','Ymd'),
					);
				/*  处理上传图片*/
				$img_info = $this->upload_files($img,$config);
				if(empty($img_info)){
					throw new \Exception($this->error);
				}
				$images = array();
				foreach($img_info as $file){  
					// $img_size = getimagesize('./'.$file['savepath'].$file['savename']);
					$images[] = array(
						'pid' 		 =>$comment_id,
						'image'		 =>$file['savepath'].$file['savename'],
						'add_time'	 =>date('Y-m-d H:i:s'),
					);
				}
				$sql = addSql($images,'sr_order_commont_img');
				$res = M()->execute($sql);
				if(!is_numeric($res)){
					/*清除图片文件*/
					array_walk($images, function($a){
						@unlink ( $a['image'] );
					});
					unset($img_info);
					unset($images);
					unset($img);
					throw new \Exception('图片上传失败');
				}
			}
			//插入订单日志
			// $O->insert_log($this->member_id,$order_info['order_id'],$order_info['status'],'订单评价成功');
		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('商品评价成功');

	}


	/**
	 * 取消订单
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function cancel(){

		$map 	= array(
				'member_id'	=> $this->member_id,
				'id'		=> I('order_id',0,'int'),
				'status'	=> array('EQ',10),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$O		= D('Order'); 
		$field  = array('id');
		$info 	= $O->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('订单信息不存在');
		}

		try {
			$M = M();
			$M->startTrans();

			if(!$O->cancel($info['id'])){
				throw new \Exception('操作失败');
			}

			//写入订单操作日志
			$O->insert_log($this->member_id,$info['id'],5,'用户取消订单');

		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('操作成功');
	}


	/**
	 * 确认收货
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function confirm_receipt(){

		$map 	= array(
				'member_id'	=> $this->member_id,
				'id'		=> I('order_id',0,'int'),
				'status'	=> array('EQ',30),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$O		= D('Order'); 
		$field  = array('id');
		$info 	= $O->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('订单信息不存在');
		}

		try {
			$M = M();
			$M->startTrans();

			if(!$O->confirm_receipt($info['id'])){
				throw new \Exception('操作失败');
			}
			//写入订单操作日志
			$O->insert_log($this->member_id,$info['id'],40,'用户确认收货');

		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('操作成功');
	}


	/**
	 * 申请退款
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function apply_returns(){

		$map 	= array(
				'member_id'	=> $this->member_id,
				'id'		=> I('order_id',0,'int'),
				'status'	=> array('EQ',20),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$O		= D('Order'); 
		$field  = array('id','type','pay_type');
		$info 	= $O->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('订单信息不存在');
		}

		//如果订单是到货订单则需要判断订单是否实付完成;
		if((1 == $info['type']) && (3 == $info['pay_type'])){
			$this->api_error('订单未实付');
		}

		try {
			$M = M();
			$M->startTrans();

			if(!$O->apply_returns($info['id'])){
				throw new \Exception('操作失败');
			}
			//写入订单操作日志
			$O->insert_log($this->member_id,$info['id'],60,'用户申请退款');

		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('操作成功');
	}

	/**
	 * 将订单标识为到货付款【支付类型：到货付款】
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function cod(){

		$order_id = I('order_id',0,'int');
		$map 	= array(
				'member_id'	=> $this->member_id,
				'id'		=> I('order_id',0,'int'),
				'status'	=> array('EQ',10),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$O		= D('Order'); 
		$field  = array('id');
		$info 	= $O->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('订单信息不存在');
		}
		$res = $O->where('id='.$order_id)->save(array('pay_type'=>3,'type'=>1,'status'=>20));

		if(is_numeric($res)){
			$O->insert_log($info['member_id'],$order_id,20,'买家选择到货付款');
			$this->api_success('操作成功');
		}else{
			$this->api_error('操作失败');
		}

	}


   /**
	* 支付宝APP支付生成 orderString 对象
	* @time 2017-09-27
	* @author llf
	**/
	public function ali_pay(){

		if(!IS_POST)	$this->api_error('提交方式错误');	
		/*用户ID*/
		$order_id  = I('post.order_id',0,'int');
		/*检查订单信息*/
		$map = array(
				'id'		=>$order_id,
				'member_id'	=>$this->member_id,
				'is_del'	=>0,
				'is_hid'	=>0,
			);
		$order_info = M('Order')->where($map)->find();
		if(empty($order_info)){
			$this->api_error('订单信息不存在');
		}
		if($order_info['status'] != '10'){
			$this->api_error('订单不可支付');
		}

		$amount = (floatval($order_info['money_total']) - floatval($order_info['money_reduction']));
		vendor('alipay.AopClient');
		vendor('alipay.request.AlipayTradeAppPayRequest');
		$aop = new \AopClient();
		$aop->gatewayUrl = "https://openapi.alipay.com/gateway.do";
		$payment = C('payment');
		$aop->appId		 = $payment['alipay']['app_id'];
		$aop->rsaPrivateKey = file_get_contents($payment['alipay']['ali_private_key_path']);
		// $aop->format = "json";
		// $aop->charset = "UTF-8";
		$aop->signType = "RSA2";
		$aop->alipayrsaPublicKey = file_get_contents($payment['alipay']['ali_public_key_path']);

		// bug(array($payment['alipay']['ali_private_key_path'],$aop->rsaPrivateKey));
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		$request = new \AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数
		$content = array(
				'body'=>$order_info['detail_title'],
				'subject'=>'高超便民订单支付',
				'out_trade_no'=>$order_info['order_no'],
				'timeout_express'=>'30m',
				'total_amount'=>$amount,
				'product_code'=>'QUICK_MSECURITY_PAY',
			);
		$bizcontent = json_encode($content);
		$request->setNotifyUrl(C('CALLBACK_URL_ALI'));
		$request->setBizContent($bizcontent);
		//这里和普通的接口调用不同，使用的是sdkExecute
		$response = $aop->sdkExecute($request);
		//htmlspecialchars是为了输出到页面时防止被浏览器将关键参数html转义，实际打印到日志以及http传输不会有这个问题
		// echo htmlspecialchars($response);//就是orderString 可以直接给客户端请求，无需再做处理。
		// echo  $response;
		// die();
		$this->api_success('OK', $response);
	}

   /**
	* 预支付ID
	*/
	public function wx_ios_pay(){

		write_debug('-------','请求微信预支付ID--被请求了');
		if(!IS_POST)	$this->api_error('提交方式错误');	
		/*用户ID*/
		$order_id  = I('post.order_id',0,'int');
		/*检查订单信息*/
		$map = array(
				'id'		=>$order_id,
				'member_id'	=>$this->member_id,
				'is_del'	=>0,
				'is_hid'	=>0,
			);
		$order_info = M('Order')->where($map)->find();
		write_debug($order_info['order_no'],'请求微信预支付ID');
		if(empty($order_info)){
			$this->api_error('订单信息不存在');
		}
		if($order_info['status'] != 10){
			$this->api_error('订单不可支付');
		}
		$wechat = & load_wechat('Pay');
		/** 生成预支付ID */
		$amount = (floatval($order_info['money_total']) - floatval($order_info['money_reduction']))*100;
		$payId  = $wechat->getPrepayId(
			'',  						 	//需要支付的用户open_id
			$order_info['detail_title'],	//商品标题
			$order_info['order_no'],   		//我们的订单ID
			$amount,						//我们的订单价格，微信支付默认为分，所以需要*100
			C('CALLBACK_URL_WX'),			//支付成功后的回调地址
			'APP'
		);

		// write_debug(array(
		// 	'',  						 	//需要支付的用户open_id
		// 	'测试支付',        				//商品标题
		// 	$order_info['order_no'],   		//我们的订单ID
		// 	$amount, 						//我们的订单价格，微信支付默认为分，所以需要*100
		// 	C('CALLBACK_URL_WX'),         	//支付成功后的回调地址
		// 	'APP'
		// 	),'微信预支付ID');
		if(!$payId){
			$this->api_error('微信预支付ID生成失败');
		}

		$data = $wechat->create_sign($payId);
		// unset($data['appId']);
		unset($data['timeStamp']);
		// unset($data['signType']);
		$data['prepayid'] = $payId;
		$this->api_success('OK',$data);
	}
}
