<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 我的卡券
	 */
class CardController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table 		='Coupon_card';
	protected $table_view 	='CardView';
	protected $status 		= array(1,2,3);

	/**
	 * 我的卡券列表
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function index() {

		$map 	= array(
				'member_id'	=> $this->member_id,
				'status'	=> array('IN',$this->status),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			); 
		$status = I('status',0,'int');
		if(in_array($status, $this->status)){
			$map['status'] = $status;
		}
		$field = array('c.id as card_id','title','description','amount','minimum','status','begin_date','end_date','get_time');
		$list  = $this->page(D($this->table_view),$map,'get_time desc',$field);

		$status = get_table_state($this->table,'status'); 
		foreach ($list as $k => $v) {
			$list[$k]['add_time'] = $v['get_time'];
			$list[$k]['status_title']  = $status[$v['status']]['title'];
			$list[$k]['minimum_title'] = '订单满'.$v['minimum'].'元可用'; 
			unset($list[$k]['get_time']);
		}

		$this->api_success('ok',(array)$list);
		
	}

	/**
	 * 兑换
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function exchange(){

		if(!IS_POST)		$this->api_error('请求方式错误');

		$code = I('post.code','','trim');
		if(empty($code))	$this->api_error('请输入卡券码');

		$map  = array(
			'code'		=> $code,
			'status'	=> array('IN',$this->status),
			'is_del'	=> 0,
			'is_hid'	=> 0,
			);
		$info = M($this->table)->where($map)->field('id,member_id,status')->find();
		if(empty($info)){
			$this->api_error('兑换码不存在');
		}
		if($info['member_id']){
			$this->api_error('兑换码已兑换');
		}
		if($info['status'] == 2){
			$this->api_error('兑换码已使用');
		}
		if($info['status'] == 3){
			$this->api_error('兑换码已过期');
		}

		$res = D('Card')->exchange($info['id'],$this->member_id);
		if(!$res){
			$this->api_error('兑换失败');
		}
		$this->api_success('兑换成功');

	}

	/**
	 * 可用优惠劵
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function usable(){

		$order_amount = I('money_total',0,'floatval');

		//获取小于该金额的优惠劵
		$map 	= array(
				'member_id'	=> $this->member_id,
				'amount'	=> array('gt',$order_amount),
				'status'	=> 1,
				'is_del'	=> 0,
				'is_hid'	=> 0,
			); 
		$field = array('c.id as card_id','title','description','amount','minimum','status','begin_date','end_date','get_time');
		$list  = $this->page(D($this->table_view),$map,'get_time desc',$field);

		$status = get_table_state($this->table,'status'); 
		foreach ($list as $k => $v) {
			$list[$k]['add_time'] = $v['get_time'];
			$list[$k]['status_title'] = $status[$v['status']]['title'];
			$list[$k]['minimum_title'] = '订单满'.$v['minimum'].'元可用'; 
			unset($list[$k]['get_time']);
		}

		$this->api_success('ok',(array)$list);

	}

}
