<?php
namespace Backend\Controller\Product;
/**
 * 商品评论
 * @author llf
 * @time 2016-06-30
 */
class CommentController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Order_comment';
	protected $table_view 	= 'ProductCommentView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map	= $this->_search();
		$result = $this->page(D($this->table_view),$map,'add_time desc');

		$this->assign($result);
		$this->display();
	}

	/**
	 * 列表 搜索
	 */
	private function _search(){

		$post		= I();
		$map		= array('is_del'=>0);
		$keyword	= I('keywords','','trim');

		if(!empty($keyword)){
			$map['title|nickname']   = array('LIKE',"%$keyword%");
		}
		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		return $map;
	}

	/**
	 * 详情
	 * @author llf
	 * @time 2016-05-31
	 */
	public function detail(){

		$this->operate();
	}

	/**
	 * 显示
	 * @author llf
	 * @time 2016-05-31
	 */
	protected function operate(){

		$info = get_info($this->table, array('id'=>I('ids')));
		if($info['member_id']){
			$info['member_info'] = M('Member')->where('id='.$info['member_id'])->find();
		}
		if($info['product_id']){
			$info['product_info'] = M('Product')->where('id='.$info['product_id'])->find();
		}
		if($info['id']){
			$info['images'] = D('OrderComment')->get_image($info['id']);
		}
		$data['info'] 	= $info;

		$this->assign($data);
		$this->display('operate');
	}
	
	/**
	 * 添加
	 * @author llf
	 * @time 2016-05-31
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
	 * @time 2016-05-31
	 */
	public function edit(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}

	/**
	 * 修改
	 * @author llf
	 * @time 2016-05-31
	 */
	protected function update(){

		$post = I('post.');

		/*基本信息*/
		$data = array(
				'title' 		=> I('title','','trim'),
				'district_id'	=> I('district_id',0,'int'),
				'add_time'		=> date('Y-m-d H:i:s'),
				'update_time'	=> date('Y-m-d H:i:s'),
				);
		if($post['ids']){
			$has = M($this->table)->where('id='.$post['ids'])->find();
			if(empty($has)){
				$this->error('车位信息不存在');
			}
			$data['id'] = intval($post['ids']);
			unset($data['add_time']);
		}else{
			$data['admin_id']	= session('member_id');
		}
		$rules[] = array('district_id','require','请选择小区',3);
		$rules[] = array('title','require','小事类型名称必须',3);
		$rules[] = array('title',1,20,'标题长度范围在1~20字符',3,'length');

		try {
			$M = M();
			$M->startTrans();

			$result = update_data($this->table, [], [], $data);
			if(!is_numeric($result)){
				throw new \Exception($result);
			}
			if(empty(I('image')) && !$data['id']){
				throw new \Exception('请上传车位图片');
			}
			if(!empty($has) && empty($has['image']) && empty(I('image'))){
				throw new \Exception('请上传车位图片');
			}
			multi_file_upload(I('image'),'Uploads/Parking','parking','id',$result,'image');

		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}
		$M->commit();
		$this->success('操作成功',U('index'));
	}

	/**
	 * 系统回复设置
	 * @author llf
	 * @time 2016-05-31
	 */
	public function reply(){
		if(IS_POST){

			$comment_id = I('comment_id',0,'int');
			$reply      = I('reply','','trim');
			$condition  = array(
					'is_del' => 0,
					'id'	 => $comment_id,
				);
			$info  = M($this->table)->where($condition)->find();
			if(empty($info)){
				$this->error('评价信息不存在');
			}
			if(250 < strlen($reply)){
				$this->error('回复内容长度范围0~250个字符');
			}
			$res = M($this->table)->where('id='.$info['id'])->setField('reply',$reply);
			if(is_numeric($res)){
				$this->success('操作成功');
			}else{
				$this->error('操作失败');
			}
			
		}else{

			$comment_id = I('ids',0,'int');
			$condition  = array(
					'is_del' => 0,
					'id'	 => $comment_id,
				);
			$info  = M($this->table)->where($condition)->find();
			if(empty($info)){
				$this->error('评价信息不存在');
			}
			$this->assign('info',$info);
			$this->display();
		}	
	}
}

