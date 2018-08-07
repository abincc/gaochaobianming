<?php
namespace Backend\Controller\Order;
/**
 * 订单管理
 * @author llf
 * @time 2016-06-30
 */
class IndexController extends OrderController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Order';
	protected $table_view 	= 'OrderView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map 	= $this->_search();
		$result = $this->page(D($this->table_view),$map,'add_time desc');
		$result['status'] = get_table_state($this->table,'status');

		$this->assign($result);
		$this->display();
	}


	/**
	 * 列表 搜索
	 */
	private function _search(){

		$post		= I();
		$map		= array('is_del'=>0,'is_hid'=>0);
		$keyword	= I('keywords','','trim');
		$status		= I('status',0,'int');
		$is_cod		= I('is_cod','','trim');
		
		if(!empty($keyword)){
			$map['mobile|username|order_no']   = array('LIKE',"%$keyword%");
		}
		if($status){
			$map['status'] = $status;
		}
		if('on' == $is_cod){
			$map['type'] = 1;
		}
		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		return $map;
	}


	/**
	 * 详情
	 * @author llf
	 * @time 2017-09-07
	 */
	public function detail(){
		$this->operate();
	}

	
	/**
	 * 显示
	 * @author llf
	 * @time 2017-09-07
	 */
	protected function operate(){
		
		$order_id = I('ids',0,'int');
		$map	  = array('is_del'=>0,'is_hid'=>0);
		$map['id']= $order_id;     

		$info = D($this->table_view)->where($map)->find();
		$data['info'] = $info;
		if(empty($info)){
			$this->error('订单信息不存在');
		}
		$data['order_log']		= D($this->table)->get_log($order_id);
		$data['order_detail']	= M('order_detail')->where('order_id='.$order_id)->field()->select();
		$data['status']  		= get_table_state($this->table,'status');
		$data['pay_type'] 		= get_table_state($this->table,'pay_type');

		$this->assign($data);
		$this->display('operate');
	}

	/**
	 * 发货
	 * @author llf
	 * @time 2017-09-07
	 */
	public function deliver(){

		$order_id = I('ids',0,'int');
		$map	  = array('is_del'=>0,'is_hid'=>0);
		$map['id']= $order_id;     

		$O = D($this->table);
		$info = $O->where($map)->find();
		if(empty($info)){
			$this->error('订单信息不存在');
		}
		if(!$O->deliver($order_id)){
			$this->error('操作失败');
		}
		$O->insert_log($info['member_id'],$order_id,30,'系统确认支付并发货');
		$this->success('操作成功');

	}

	/**
	 * 确认退货
	 * @author llf
	 * @time 2017-09-07
	 */
	public function confirm_returns(){

		$order_id = I('ids',0,'int');
		$map	  = array('is_del'=>0,'is_hid'=>0);
		$map['id']= $order_id;     

		$O = D($this->table);
		$info = $O->where($map)->find();
		if(empty($info)){
			$this->error('订单信息不存在');
		}
		if(!$O->confirm_returns($order_id)){
			$this->error('操作失败');
		}
		$O->insert_log($info['member_id'],$info['id'],70,'系统确认退货');
		$this->success('操作成功');
	}

	/**
	 * 确认退货
	 * @author llf
	 * @time 2017-09-07
	 */
	public function reapply_returns(){

		$order_id = I('ids',0,'int');
		$map	  = array('is_del'=>0,'is_hid'=>0);
		$map['id']= $order_id;     

		$O = D($this->table);
		$info = $O->where($map)->find();
		if(empty($info)){
			$this->error('订单信息不存在');
		}
		if(!$O->reapply_returns($order_id)){
			$this->error('操作失败');
		}
		$O->insert_log($info['member_id'],$info['id'],20,'系统取消退货');
		$this->success('操作成功');
	}

	/**
	 * 确认到付
	 * @author llf
	 * @time 2017-10-11
	 */
	public function confirm_cod(){

		$order_id = I('ids',0,'int');
		$map	  = array('is_del'=>0,'is_hid'=>0);
		$map['id']= $order_id;     

		$O = D($this->table);
		$info = $O->where($map)->find();
		if(empty($info)){
			$this->error('订单信息不存在');
		}
		//应付实付金额
		$amount = bcadd(floatval($info['money_total']),floatval($info['money_reduction'],2));
		if(!$O->confirm_cod($order_id,$amount)){
			$this->error('操作失败');
		}
		$O->insert_log($info['member_id'],$info['id'],$info['status'],'系统确认到付订单实付成功');
		$this->success('操作成功');
	}

	
	/**
	 * 取消订单
	 * @author llf
	 * @time 2017-09-07
	 */
	public function cancel(){
		
		$order_id = I('ids',0,'int');
		$map	  = array('is_del'=>0,'is_hid'=>0);
		$map['id']= $order_id;     

		$O = D($this->table);
		$info = $O->where($map)->find();
		if(empty($info)){
			$this->error('订单信息不存在');
		}
		if(!$O->cancel($order_id)){
			$this->error('操作失败');
		}
		$O->insert_log($info['member_id'],$info['id'],5,'系统取消订单');
		$this->success('操作成功');
	}

	/**
     * 订单导出
     * @author llf
     * @param
     */
    public function export(){

		$map 	= $this->_search();
		$result = get_result(D($this->table_view), $map,'add_time desc');

		if(!empty($result)){
			$status = get_table_state($this->table,'status');
			array_walk($result, function(&$a) use($status){
				$a['status_title'] = $status[$a['status']]['title'];
				$a['receipt_address'] = unserialize($a['receipt_address']);
				$a['receipt_name'] = $a['receipt_address']['name'];
				$a['receipt_address_str'] = $a['receipt_address']['province_title']. $a['receipt_address']['city_title']. $a['receipt_address']['area_title'].' '.$a['receipt_address']['address_detail'];
				$a['product_info'] = M('order_detail')->where(['order_id'=>$a['id']])->field("GROUP_CONCAT(CONCAT(product_title,'-',product_num,'件') separator '-|-') as c")->select();
				$a['product_info'] = trim($a['product_info'][0]['c']);
			});
		}
		/*填充数据*/
		$data['result']    = $result;
		/*填充表名*/
		$data['sheetName'] = 'order_export_'.date('Ymd_His');

		$export_config = array(
			array('title' => '订单编号', 'field' => 'order_no'),
			array('title' => '订单描述', 'field' => 'detail_title'),
			array('title' => '下单用户', 'field' => 'username'),
			array('title' => '下单用户手机', 'field' => 'mobile'),
			array('title' => '订单金额', 'field' => 'money_total'),
			array('title' => '订单状态', 'field' => 'status_title'),
			array('title' => '下单时间', 'field' => 'add_time'),
			array('title' => '收货人', 'field' => 'receipt_name'),
			array('title' => '收货电话', 'field' => 'receipt_mobile'),
			array('title' => '收货地址', 'field' => 'receipt_address_str'),
			array('title' => '订单商品明细', 'field' => 'product_info'),
		);
		A('Common/Excel','',1)->export_data($export_config,$data);
    }
}

