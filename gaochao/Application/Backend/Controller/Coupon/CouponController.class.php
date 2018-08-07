<?php
namespace Backend\Controller\Coupon;
/**
 * 卡卷类型管理
 * @author llf
 * @time 2017-11-2
 */
class CouponController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Coupon';
	protected $map			= array('is_del'=>0);
	
	/**
	 * 列表函数
	 */
	public function index(){

		$map = $this->map;
		/**禁用*/
		if(strlen(I('is_hid'))){
			$map['is_hid'] = I('is_hid');
		}
		/**手机|昵称|姓名*/
		if(strlen(trim(I('keyword')))) {
			$map['title'] = array('like','%' . trim(I('keyword')) . '%');
		}
		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		$result = $this->page($this->table,$map,'add_time desc');

		$this->assign($result);
		$this->display();
	}

	/**
	 * 添加
	 * @author llf
	 * @time 2017-09-14
	 */
	public function add(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}

	/**
	 * 编辑
	 * @author llf
	 * @time 2017-09-14
	 */
	public function edit(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}

	/**
	 * 显示
	 * @author llf
	 * @time 2017-09-14
	 */
	public function detail(){
		$this->operate('detail');
	}

	/**
	 * 显示
	 * @author llf
	 * @time 2017-09-14
	 */
	protected function operate($tpl='operate'){
		$info = get_info($this->table, array('id'=>I('ids')));

		$data['info'] = $info;
		$this->assign($data);
		$this->display($tpl);
	}
	
	/**
	 * 修改
	 * @author llf
	 * @time 2017-09-14
	 */
	protected function update(){

		if(!IS_POST)	$this->error('提交方式错误');

		$data = I('post.');

		/*基本信息*/
		$post = array();

		$post['title']			= I('title','','trim');
		$post['description']	= I('description','','trim');
		$post['amount']			= I('amount','','floatval');
		$post['minimum']		= I('minimum','','floatval');
		$post['begin_date']		= I('begin_date','','trim');
		$post['end_date']		= I('end_date','','trim');

		$Coupon = D($this->table);
		if(is_numeric($data['id'])){
			$has = $Coupon->where('id='.intval($data['id']))->find();
			if(!empty($has)){
				$post['id'] = intval($has['id']);
			}
		}
		try {
            $M = M();
            $M->startTrans();
		
			/*基本信息*/
			$result = update_data($Coupon, [], [], $post);
			if(!is_numeric($result)){
				throw new \Exception($Coupon->getError());
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}

		$M->commit();
		F('coupon_no_hid',null);
		F('coupon_no_del',null);
		$this->success('操作成功',U('index'));
	}
	
}

