<?php
namespace Backend\Controller\Property;
/**
 * 政务公开
 */
class CkHomeController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'CkHome';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map    = $this->get_list_map();
		$result = $this->page($this->table,$map,'add_time desc');

		//小区信息处理
		$map1  = array('is_del'=>0, 'is_hid'=>0);
        if(!empty(session('district_ids'))){
            $map1['id'] = array('IN',session('district_ids'));
        }
        $district_list  = M('district')->where($map1)->field('id,title')->select();
        foreach ($result['list'] as $k => $v) {
            if(intval($v['district_id']) == 0){
                $result['list'][$k]['district_title'] = '全部小区';
                continue;
            }else{
                foreach ($district_list as $m => $n) {
                    if($v['district_id'] == $n['id']){
                        $result['list'][$k]['district_title'] = $n['title'];
                    }
                }
            }
        }

        $option = '<option value=0>全部小区</option>';
        foreach ($district_list as $k => $v) {
            if(I('district_id','','int') == $v['id']){
                $option .= "<option selected='selected' value='{$v['id']}'>{$v['title']}</option>";
            }else{
                $option .= "<option value='{$v['id']}'>{$v['title']}</option>";
            }
        }
        $result['district'] = $option;

		//物业公告类型处理
		$type = get_table_state('ck_home','type');
		$options = '';
        foreach ($type as $k => $v) {
        	if(!empty(I('type')) && I('type','', 'int') == $v['r_value']){
        		$options = $options.'<option value="'.$v['r_value'].'" selected = "selected">'.$v['title'].'</option>';
        	}else{
        		$options = $options.'<option value="'.$v['r_value'].'">'.$v['title'].'</option>';
        	}
        }
        $result['type'] = $options;
		$result['type_s'] = $type;
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
		$type              = I('type','','int');
		$map['company_id'] = session('company_id');
		$district_ids       = session('district_ids');
        array_push($district_ids ,'0');

		if(!empty($keyword)){
			$map['title']   = array('LIKE',"%$keyword%");
		}
		if(!empty($is_hid)){
			$map['is_hid']	= $is_hid;
		}
		/**小区*/
		if(!empty(I('district_id'))){
			$map['district_id'] = I('district_id',0,'int');
		}else{
			if(!empty(session('district_ids'))){
                $map['district_id'] = array('IN',$district_ids);
            }
		}

		if(!empty($type)){
			$map['type'] = $type;
		}

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

		//获取对应的账号地下的所有小区信息
        $map  = array('is_del'=>0, 'is_hid'=>0);
        if(!empty(session('district_ids'))){
            $map['id'] = array('IN',session('district_ids'));
        }
        $district_list  = M('district')->where($map)->field('id,title')->select();
        $option = intval($info['district_id']) == 0?'<option  selected="selected" value=0>全部小区</option>':'<option value=0>全部小区</option>';
        foreach ($district_list as $k => $v) {
            if($info['district_id'] == $v['id']){
                $option .= "<option selected='selected' value='{$v['id']}'>{$v['title']}</option>";
            }else{
                $option .= "<option value='{$v['id']}'>{$v['title']}</option>";
            }
        }
        $data['district'] = $option;  


		//物业公告类型处理
		$type = get_table_state('ck_home','type');
		$options = '';
        foreach ($type as $k => $v) {
        	if(!empty($info['type']) && $info['type'] == $v['r_value']){
        		$options = $options.'<option value="'.$v['r_value'].'" selected = "selected">'.$v['title'].'</option>';
        	}else{
        		$options = $options.'<option value="'.$v['r_value'].'">'.$v['title'].'</option>';
        	}
        }
        $data['type'] = $options;
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

		/*基本信息*/
		$data = array(
				'title' 		=> I('title','','trim'),
				'type'          => I('type','','int'),
				'desc'	        => I('desc','','trim'),
				'remark'		=> $_POST['remark'],
				'district_id'	=> I('district_id',0,'int'),
				'add_time'		=> date('Y-m-d H:i:s'),
				'update_time'	=> date('Y-m-d H:i:s'),
				'company_id'	=> session('company_id')
				);


		if(intval($data['district_id']) == 0){
			if(!in_array(intval($data['type']), [1,2])){
				$this->error('小区和类型不匹配');
			}
		}

		if($post['ids']){
			$has = M($this->table)->where('id='.$post['ids'])->find();
			if(empty($has)){
				$this->error('信息不存在');
			}
			$data['id'] = intval($post['ids']);
			unset($data['add_time']);
		}


		try {
			$M = M();
			$M->startTrans();

			$result = update_data($this->table, [], [], $data);
			if(!is_numeric($result)){
				throw new \Exception($result);
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}
		$M->commit();
		$this->success('操作成功',U('index'));
	}
	
	/**
	 * 删除图片
	 */
	public function ajaxDelete_ck_home() {
		$this->ajax_delete_image(['cover']);
	}
}
