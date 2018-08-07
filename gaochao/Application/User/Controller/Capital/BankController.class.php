<?php
namespace User\Controller\Capital;
	/**
	 * 银行卡管理
	 */
class BankController extends IndexController{
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'member_bank';
	/**
	 * 银行卡列表页
	 */
	public function index(){
		$map['member_id'] = session('member_id');
		$data['result']= $this->page($this->table,$map,'id desc');
		$data['bank'] = get_no_hid('bank');
		$this->assign($data);
		$this->display();
	}
	
	/**
	 * 银行卡信息删除
	 */
	public function del(){
		$bank_id = I('id');
		if(!$bank_id){
			$this->error('缺少参数！');
		}
		/*1、获取当前银行卡信息id*/
		$map['id'] = $bank_id;
		$map['member_id'] = session('member_id');
		$data['is_del'] = 1;
		/*2、删除数据表中对应的银行卡信息*/
		$result = update_data($this->table,'',$map,$data);
		if(is_numeric($result)){
			$this->success('删除成功！');
		}else{
			$this->error('删除失败！');
		}
	}
	
	/**
	 * 银行卡添加
	 */
	public function add(){
		if(IS_POST){
			$this->update();
		}
		else{
			$this->operate();
		}
	}
	/**
	 * 银行卡修改
	 */
	public function edit(){
		if(IS_POST){
			$this->update();
		}
		else{
			$this->operate();
		}
	}
	/**
	 * 银行卡信息更新
	 */
	private function operate(){
		$data['info'] = get_info($this->table, array('id' => I('id')));
		$data['bank'] = get_no_hid('bank');
		$this->assign($data);
		$this->display('operate');
	}
	/**
	 * 银行卡信息更新
	 */
	private function update(){
		/*1、获取提交过来的数据*/
		$_POST['member_id'] = session('member_id');
		/*2、添加验证信息*/
		/*验证开户人等信息*/
		$rules = array(
			array('username','1,10','开户人姓名字数为1~10个字符','0','length'),
			array('bank_id','require','未选择银行！'),
			array('account','16,19','银行卡号为16到19位','0','length'),
			array('account','number','银行卡号必须是数字！'),
			array('bank_adress','require','开户支行不能为空！'),
		);

		/*3、将获取到的数据添加到数据表中*/
		$result = update_data($this->table,$rules);
		if(is_numeric($result)){
			$this->success('修改成功！',$_SERVER['HTTP_REFERER']);
		}else{
			$this->error($result);
		}
	}
}
