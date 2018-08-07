<?php
namespace User\Controller\Capital;
	/**
	 * 提现
	 */
class WithdrawController extends IndexController{
	/**
	 * 充值记录表
	 * @var string
	 */
	protected $table = 'capital_record';
	/**
	 * 提现
	 */
	public function index(){
		$member_id = session('member_id');
		$member_info = get_info('member', array('id' => $member_id));
		$data['balance']=$member_info['balance'];
	
		$bank = get_result('member_bank',array('member_Id' => session('member_id')));
		$bank_list = get_no_hid('bank');
		array_walk($bank,function(&$item) use ($bank_list){
			$item['content'] = $item['account'] . ' | ' . $bank_list[$item['bank_id']]['title'] . ' | ' . $item['username'];
		});
		$data['bank'] = $bank;
		$this->assign($data);
		$this->display();	
	}
	
	/**
	 * 提现申请
	 * 1、检查用户余额是否足够
	 * 2、足够则从用户账户扣除提现金额
	 * 3、生成提现资金记录
	 *
	 * @param int $member_id	提现用户ID
	 * @param double $price	提现金额
	 * @param int $bank_id
	 * @param string $user_info_table	用户信息表(存储用户余额记录的表)
	 * @param string $record_table	资金记录表
	 * @param string $bank_model	用户银行卡模型
	 * @return boolean	执行结果
	 *
	 * @author	李东<947714443@qq.com>
	 * @date	2016-03-17
	 */
	public function withdraw(){
		if(!IS_POST){
			$this->error('缺少数据');
		}
		/*数据验证*/
		$posts = I('post.');
		$money = intval($posts['price']);
		$member_id = session('member_id');
		$member_info = get_info('member', array('id' => $member_id));
		if(md5(md5($posts['deal_password']).$member_info['deal_salt'])!=$member_info['deal_password']){
			$this->error('支付密码错误');
		}
		if($money<=0 || !is_int($money) || ($member_info['balance'] < $money)){
			$this->error('提现金额必须是整数，且在0-' . $member_info['balance'] . '之间');
		}
		if(!preg_match('/^[1-9]\d{0,3}$/',$money) || ($member_info['balance'] < $money)){
			$this->error('请输入0-' . min(10000, $member_info['balance']) . '之间的整数');
		}
		$bank_id = $posts['bank_id'];
		if($bank_id<=0){
			$this->error('请选择收款账号');
		}
		/*生成记录*/
		$bank_info = get_info('member_bank', array('id' => $bank_id));
		$bank_list = get_no_hid('bank');
		$remark = array(
			'username'=>$bank_info['username'],
			'bank'=>$bank_list[$bank_info['bank_id']]['title'],
			'account'=>$bank_info['account'],
			'bank_adress'=>$bank_info['bank_adress'],
		);
		$data_1 = array(
			'remark' => json_encode($remark),
			'from_member_id' => $member_id,
			'to_member_id' => 0,
			'type' => 41,
			'order_no' => build_order_no(),
			'money' => $money,
			'status' => 41,
		);
		$flag = trans(function() use ($data_1){
			$result[] = update_data($this->table,array(),array(),$data_1);
			$result[] = M('member')->where(['id' => session('member_id')])->setDec('balance',$data_1['money']);
			$result[] = M('member')->where(['id' => session('member_id')])->setInc('frozen',$data_1['money']);
			return $result;
		});
		if($flag){
			$this->error('余额不足');
		}
		$this->success('申请成功',U('index'));
	}
	
	/**
	 * 取消提现
	 */
	public function cancel(){
		$gets = I('get.');
		$id = $gets['id'];
		$res = $this->funds->withdraw_cancel($id);
		if($res === true){
			$this->success('操作成功');
		}else{
			$this->error($res);
		}		
	}
	
}