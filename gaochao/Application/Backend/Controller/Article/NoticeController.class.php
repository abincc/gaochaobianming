<?php
/**
 * 平台公告
 * @author 秦晓武
 * @time 2016-06-01
 */
namespace Backend\Controller\Article;
/**
 * 平台公告
 */
class NoticeController extends ArticleController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'notice';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map    = $this->get_list_map();
		$result = $this->page($this->table,$map,'add_time desc');
		$catch = get_no_hid('district','add_time desc');
		array_unshift($catch, ['id'=>0,'title'=>'-全部小区-']);
		if(strlen(I('district_id'))){
			$result['district'] = array_to_select($catch,I('district_id'));
		}else{
			$result['district'] = array_to_select($catch);
		}
		$this->assign($result);
		$this->display();
	}
	
	/**
	 * 列表 搜索
	 */
	private function get_list_map(){

		$post 			   = I();
		$map 			   = array('is_del'=>0);
		$keyword 		   = I('keywords','','trim');
		$is_hid 		   = I('is_hid','','int');

		if(!empty($keyword)){
			$map['title']   = array('LIKE',"%$keyword%");
		}
		if(!empty($is_hid)){
			$map['is_hid']	= $is_hid;
		}
		/**小区*/
		if(strlen(I('district_id'))){
			$map['district_id'] = I('district_id',0,'int');
		}
		if(session('is_owner')){
			$map['district_id'] = array('IN',session('district_ids'));
		}
		
		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		return $map;

	}

	/**
	 * 添加
	 * @author 秦晓武
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
	 * @author 秦晓武
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
	 * 显示
	 * @author 秦晓武
	 * @time 2016-05-31
	 */
	protected function operate(){
		$info = get_info($this->table, array('id'=>I('ids')));
		$district_list	  = M('district')->where(array('is_hid'=>0,'is_del'=>0))->select();
		array_unshift($district_list, ['id'=>0,'title'=>'-全部小区']);
		$data['district'] = array_to_select($district_list, $info['district_id']);
		$data['info'] = $info;
		$this->assign($data);
		$this->display('operate');
	}
	
	/**
	 * 修改
	 * @author 秦晓武
	 * @time 2016-05-31
	 */
	protected function update(){

		$post = I('post.');
		/*获取前台传递的添加参数*/
		$data = array();
		$data['title']		 = trim($post['title']);
		$data['content']	 = $_POST['content'];
		$data['add_time']	 = date('Y-m-d H:i:s');
		$data['district_id'] = I('district_id',0,'int');	
		if($post['ids']){
			$data['id']      = intval($post['ids']);
			unset($data['add_time']);
		}
		/*验证参数*/
		$rules[] = array('title','require','标题必须填',3);
		$rules[] = array('title','1,50','标题不能超过50字符',3,'length');
		$rules[] = array('content','require','内容必须填',1);

		$result = update_data($this->table, $rules, array(), $data);
		if(is_numeric($result)){
			$this->success('操作成功',U('index'));
		}else{
			$this->error($result);
		}
	}
	
}
