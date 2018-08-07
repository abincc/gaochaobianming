<?php
namespace Backend\Controller\Property;
/**
 * 欠费消息管理
 * @author llf
 * @time 2016-06-30
 */
class ArrearsController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Message';
	protected $table_view 	= 'MessageView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map 	= $this->_search();

		$result = $this->page(D($this->table_view),$map,'add_time desc');
		$result['type']    = get_table_state($this->table,'type');

		$this->assign($result);
		$this->display();
	}


	/**
	 * 列表 搜索
	 */
	private function _search(){

		$post 			   = I();
		$map 			   = array('is_del'=>0,'is_hid'=>0);
		$keyword 		   = I('keywords','','trim'); 
		$type  	   		   = I('type','','int');

		if(!empty($keyword)){
			$map['mobile|username|title']   = array('LIKE',"%$keyword%");
		}
		if(!empty($type)){
			$map['type'] = $type;
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
		$info = get_info(D($this->table_view), array('id'=>I('ids')));

		$data['type']    = get_table_state($this->table,'type');
		$data['info']    = $info;
		$this->assign($data);
		$this->display('operate');
	}

	/**
	 * 发送消息
	 * @author llf
	 * @time 2016-05-31
	 */
	public function send(){

		if(IS_POST){
			$data = array();
			$data['member_id'] 	= I('member_id',0,'int');
			$data['title'] 		= I('title','','trim');
			$data['content']   	= $_POST['content'];
			$data['type']		= 3;

			$has = M('member')->where('id='.$data['member_id'])->find();
			if(empty($has)){
				$this->error('用户不存在');
			}

			try {
				$M = M();
				$M->startTrans();
				$Message = D($this->table);
				$result  = $Message->send($data['member_id'],$data['type'],$data['title'],$data['content']);

				if(!is_numeric($result)){
					throw new \Exception($Message->getError());
				}

			} catch (\Exception $e) {
				$M->rollback();
				$this->error($e->getMessage());
			}
			$M->commit();
			$this->success('发送成功',U('send'));

		}else{
			// echo '<pre>';
			// var_dump(json_decode('{"platform":"all","audience":{"alias":["6"]},"notification":{"android":{"alert":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","title":"\u6211\u64e6"},"ios":{"alert":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","sound":"sound.caf","badge":"+1","content-available":true}},"message":{"msg_content":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","title":"\u6211\u64e6","content_type":"text"},"options":{"sendno":54246,"time_to_live":86400,"apns_production":false}}',true));
			// die();

			$this->display();
		}

	}

	/**
	 * 群发消息
	 * @author llf
	 * @time 2016-05-31
	 */
	public function mass(){

		if(IS_POST){
			$title		= I('title','','trim');
			$content	= trim($_POST['content']);
			$type		= I('type',0,'int');   //0-全体类型、1-住户、2-业主 3-物业
			$district_id= I('district_id',0,'int');

			$condition = array(
					'is_hid'=>0,
					'is_del'=>0,
				);
			if($district_id){
				$condition['district_id'] = $district_id;
			}
			switch ($type) {
				case 0:
					break;
				case 1:
				case 2:
				case 3:
					$condition['type'] = $type;
					break;
				default:
					$this->error('类型参数错误');
					break;
			}

			$has = M('member')->where($condition)->field('id')->select();
			if(empty($has)){
				$this->error('该条件无可推送用户');
			}
			try {
				$M = M();
				$M->startTrans();
				$Message = D($this->table);

				foreach ($has as $k => $v) {
					$result  = $Message->send($v['id'],1,$title,$content);
					if(!is_numeric($result)){
						throw new \Exception($Message->getError());
					}
				}
			} catch (\Exception $e) {
				$M->rollback();
				$this->error($e->getMessage());
			}
			$M->commit();
			$this->success('发送成功',U('send'));

		}else{	

			$district_list	= M('district')->where(array('is_hid'=>0,'is_hel'=>0))->field('id,title')->select();
			array_unshift($district_list,['id'=>0,'title'=>'-【全体小区】']);
			$data['district']  	= array_to_select($district_list,0);
			
			if(session('is_owner') && !empty(session('district_ids'))){
				$list = get_no_hid('district','add_time desc');
				$district_ids = session('district_ids');
				array_walk($list, function($a) use($district_ids,&$html){
					if(in_array($a['id'], $district_ids)){
						$html .= "<option value='{$a['id']}'>{$a['title']}</option>";
					}
				});
				$data['district'] = $html;
			}

			$this->assign($data);
			$this->display();
		}

	}

}

