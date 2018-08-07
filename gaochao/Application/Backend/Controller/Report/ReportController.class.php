<?php
namespace Backend\Controller\Report;
/**
 * 体检报告
 * @author llf
 * @time 2017-11-2
 */
class ReportController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Report';
	protected $table_view	= 'ReportView';
	protected $map			= array('is_del'=>0);
	
	/**
	 * 列表函数
	 */
	public function index(){

		$post = I();
		$map  = $this->map;
		$ope_date = I('ope_date','','trim');

		/**禁用*/
		if(strlen(I('is_hid'))){
			$map['is_hid'] = I('is_hid');
		}
		/**手机|昵称|姓名*/
		if(strlen(trim(I('keywords')))) {
			$map['mobile|username'] = array('like','%' . trim(I('keywords')) . '%');
		}
		if(!empty($ope_date)){
			$map['ope_date'] = $ope_date;
		}

		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		$result = $this->page(D($this->table_view),$map,'ope_date desc,add_time desc');

		// $result['status'] = get_table_state($this->table,'status'); 
		// $result['coupon_list'] = array_to_select(get_no_hid('coupon'),$info['coupon_id']);

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
			$this->display('operate');
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

		$report_id = I('ids',0,'int');
		$Report = D('Report');
		$info   = $Report->where('id='.$report_id)->find();
		if(empty($info)){
			$this->error('详情信息不存在');
		}
		$info['images'] = $Report->get_image($report_id);

		$result['info'] = $info;
		$result['member'] = M('Member')->where('id='.intval($info['member_id']))->find();

		$this->assign($result);
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
		$post['ope_date'] 	= I('ope_date','','trim');
		$post['member_id'] 	= I('member_id',0,'int');
		$post['admin_id']	= session('member_id');

		$Report = D('Report');
		if(is_numeric($data['id'])){
			$has = $Report->where('id='.intval($data['id']))->find();
			if(!empty($has)){
				$post['id'] = intval($has['id']);
			}
		}
		if(empty($data['image'])){
			$this->error('请上传体检报告');
		}
		try {
            $M = M();
            $M->startTrans();
		
			/*基本信息*/
			$result = update_data($Report, [], [], $post);
			if(!is_numeric($result)){
				throw new \Exception($Report->getError());
			}
			multi_file_upload($data['image'],'Uploads/Report','report_image', 'report_id', $result, 'image');
		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}

		$M->commit();
		$this->success('操作成功',U('index'));
	}
	
}

