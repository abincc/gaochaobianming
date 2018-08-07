<?php
namespace Backend\Controller\Coupon;
/**
 * 卡卷记录
 * @author llf
 * @time 2017-11-2
 */
class CardController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Coupon_card';
	protected $table_view	= 'CardView';
	protected $map			= array('is_del'=>0);
	
	/**
	 * 列表函数
	 */
	public function index(){
		$post = I();
		$map  = $this->map;
		/**禁用*/
		if(strlen(I('is_hid'))){
			$map['is_hid'] = I('is_hid');
		}
		/**手机|昵称|姓名*/
		if(strlen(trim(I('keywords')))) {
			$map['mobile|username|code'] = array('like','%' . trim(I('keywords')) . '%');
		}
		if(strlen(I('status'))){
			$map['status'] = I('status',0,'int');
		}

		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		$result = $this->page(D($this->table_view),$map,'add_time desc');

		$result['status'] = get_table_state($this->table,'status'); 

		$result['coupon_list'] = array_to_select(get_no_hid('coupon'),$info['coupon_id']);

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

		$info['username'] = M('member')->where('id='.intval($info['member_id']))->getField('username');
		$info['mobile']   = M('member')->where('id='.intval($info['member_id']))->getField('mobile');
		$info['order_no'] = M('order')->where('id='.intval($info['order_id']))->getField('order_no');

		$data['info'] = $info;
		$data['coupon_info'] = M('coupon')->where('id='.intval($info['coupon_id']))->find();
		$data['coupon_list'] = array_to_select(get_no_hid('coupon'),$info['coupon_id']);
		$data['status'] = get_table_state($this->table,'status'); 
		
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
		$type = I('post.type',0,'int');
		if(!in_array($type,array(1,2))){
			$this->error('获取方式错误');
		}

		/*基本信息*/
		$post = array();
		$post['coupon_id'] 	= I('coupon_id','','int');
		$post['status'] 	= 1;

		if($type == 1){

			$post['member_id']		= I('member_id','','int');
			$post['get_time']		= date('Y-m-d H:i:s');
		}
		if($type == 2){
			$post['code']			= I('code','','trim');
		}

		$Card = D('Card');
		if(is_numeric($data['id'])){
			$has = $Card->where('id='.intval($data['id']))->find();
			if(!empty($has)){
				$post['id'] = intval($has['id']);
			}
		}
		try {
            $M = M();
            $M->startTrans();
		
			/*基本信息*/
			$result = update_data($Card, [], [], $post);
			if(!is_numeric($result)){
				throw new \Exception($Card->getError());
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}

		$M->commit();
		$this->success('操作成功',U('index'));
	}
	
}

