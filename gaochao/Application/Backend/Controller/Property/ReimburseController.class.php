<?php
namespace Backend\Controller\Property;
/**
 * 报销
 * @author llf
 * @time 2016-06-30
 */
class ReimburseController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'reimburse';
     protected $table_view   = 'ReimburseView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page(D($this->table_view),$map,'district_id asc, add_time desc');
        $result['type_s'] = get_table_state($this->table,'type');

        $map1  = array('is_del'=>0, 'is_hid'=>0);
        if(!empty(session('district_ids'))){
            $map1['id'] = array('IN',session('district_ids'));
        }
        $district_list  = M('district')->where($map1)->field('id,title')->select();
        $result['pid']    = array_to_select($district_list, $info['district_id']);  

        $this->assign($result);
        $this->display();
	}

	/**
	 * 列表 搜索
	 */
	private function _search(){

		$post 			   = I();
		$map 			   = array('is_del'=>0);
		$keyword 		   = I('keywords','','trim'); 
		$month         = I('month','','trim'); 
		$status 		   = I('status',0,'int');

        $district_id       = I('district_id','','int');
        if(!empty($district_id)){
            $map['district_id']   = $district_id;
        }else{
            if(!empty(session('district_ids'))){
                $map['district_id'] = array('IN',session('district_ids'));
            }
        }

		if(!empty($keyword)){
            $map['username']   = array('LIKE',"%$keyword%");
        }

        $start = !empty($post['start_date'])?$post['start_date']:0;
        $end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
        $map['occur_time'] = array('BETWEEN',array($start,$end));

		return $map;
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
     * 显示
     * @author llf
     * @time 2016-05-31
     */
    protected function operate($tpl='operate'){
        if($tpl == 'confirm'){
           try {
                $M = M();
                $M->startTrans();

                $type = get_table_state($this->table,'type');

                $info = get_info($this->table, array('id'=>I('ids')));
                if(intval($info['is_confirm']) == 1){
                    $this->error('该报销已确认');
                }
                if(intval($info['is_hid']) == 1){
                    $this->error('该报销已禁用');
                }
                $data = array(
                    'id'         => $info['id'],
                    'is_confirm' => 1
                );
                $result = update_data($this->table, [], [], $data);
                if(!is_numeric($result)){
                    $this->error('报销确认失败');
                }

                $incepx = M('Incepx')->where(array('district_id'=>$info['district_id']))->order('id desc')->limit(1)->select();
                if(empty($incepx)){
                    $map = array(
                        'expenses' => $info['cost'],
                        'district_id' => $info['district_id'],
                        'total' => -1 * floatval($info['cost']),
                        'remark'=> $info['username'].'--报销'.$type[$info['type']]['title'].'--说明：'.$info['remark'],
                        'status' => 2,
                        'add_time' => date('Y-m-d H:i:s'),
                    );
                }else{
                    $map = array(
                        'expenses' => $info['cost'],
                        'district_id' => $info['district_id'],
                        'total' => floatval($incepx[0]['total']) - floatval($info['cost']),
                        'remark'=> $info['username'].'--报销'.$type[$info['type']]['title'].'--说明：'.$info['remark'],
                        'status' => 2,
                        'add_time' => date('Y-m-d H:i:s'),
                    );
                }
                $result1 = update_data('Incepx', [], [], $map);
                if(!is_numeric($result1)){
                    throw new \Exception($result1);
                }
                

            } catch (\Exception $e) {
                $M->rollback();
                $this->error($e->getMessage());
            }
            $M->commit();
            $this->error('操作成功');

        }else{
            if($tpl == 'detail'){
                $info = get_info($this->table, array('id'=>I('ids')));
                if(!empty($info)){
                    $info['images'] = M('ReimburseImage')->where(['pid'=>I('ids')])->select();
                }
                $map['id'] = $info['district_id'];
                $data['info'] = $info;
                $data['status'] = get_table_state($this->table,'type');
                $data['district'] = M('district')->where($map)->field('id,title')->select();

            }else{
                $info = get_info($this->table, array('id'=>I('ids')));
                if(intval($info['is_confirm']) == 1){
                    $this->error('该报销已确认,无法修改');
                }
                $data['info'] = $info;
                $result['status'] = get_table_state($this->table,'type');

                //报销类型获取
                $options = '';
                foreach ($result['status'] as $k => $v) {
                    $options = $options.'<option value="'.$k.'">'.$v['remark'].'</option>';
                    
                }
                $data['options'] = $options;

                //获取对应的账号地下的所有小区信息
                $map  = array('is_del'=>0, 'is_hid'=>0);
                if(!empty(session('district_ids'))){
                    $map['id'] = array('IN',session('district_ids'));
                }
                $district_list  = M('district')->where($map)->field('id,title')->select();
                $data['pid']    = array_to_select($district_list, $info['district_id']);     

                // $map  = array('is_del'=>0, 'is_hid'=>0);
                //  if(!empty(session('district_ids'))){
                //     $map['id'] = array('IN',session('district_ids'));
                // }
                // $map['id'] = array('IN',session('district_ids'));
                $district_list  = M('reimburse')->where($map)->field('id')->select();
            }

            $this->assign($data);
            $this->display($tpl);
        }
    }


    public function detail(){
        $this->operate('detail');
    }


    public function confirm(){
        $this->operate('confirm');
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
            'district_id'  => I('district_id',0,'int'),
            'username'     => I('username','','trim'),
            'mobile'       => I('mobile','','trim'),
            'type'         => I('type','','int'),
            'reason'       => I('reason','','trim'),
            'occur_time'   => I('occur_time','','trim'),
            'cost'         => I('cost','','trim'),
            'add_time'     => date('Y-m-d H:i:s'),
            'remark'       => I('remark','','trim'),
            'company_id'   => session('company_id') 
        );
        if($post['ids']){
            $data['id'] = intval($post['ids']);
        }

        // var_dump($data);
        // die();

        try {
            $M = M();
            $M->startTrans();

            $result = update_data($this->table, [], [], $data);
            if(!is_numeric($result)){
                throw new \Exception($result);
            }
            if(empty(I('image')) && !$data['id']){
                throw new \Exception('请上传图片');
            }
            if(!empty($has) && empty($has['image']) && empty(I('image'))){
                throw new \Exception('请上传图片');
            }
            multi_file_upload(I('image'),'Uploads/Parking','reimburse_image','pid',$result,'image');

        } catch (\Exception $e) {
            $M->rollback();
            $this->error($e->getMessage());
        }
        $M->commit();
        $this->success('操作成功',U('index'));
    }

    public function del(){
        F('owner_no_hid',null);
        F('owner_no_del',null);
        parent::del();
    }
	
    /**
     * 报销导出
     * @author llf
     * @param
     */
    public function export(){

        $map    = $this->_search();

        $result = get_result(D($this->table_view), $map,'occur_time desc');

        /*填充数据*/
        $data['result']  = $result;

        /*填充表名*/
        $data['sheetName'] = 'reim_export'.date('Ymd_His');


        $export_config = array(
            array('title' => '小区', 'field' => 'district_title', 'width' => '20'),
            array('title' => '姓名', 'field' => 'username', 'width' => '20'),
            array('title' => '电话号码', 'field' => 'mobile', 'width' => '20'),
            array('title' => '报销类型', 'field' => 'type', 'width' => '20'),
            array('title' => '报销事由', 'field' => 'reason', 'width' => '20'),
            array('title' => '发生时间', 'field' => 'occur_time', 'width' => '20'),
            array('title' => '费用', 'field' => 'cost', 'width' => '20'),
            array('title' => '内容', 'field' => 'remark', 'width' => '50'),
            array('title' => '图片路径', 'field' => 'image', 'width' => '50'),
        );
        A('Common/Excel','',1)->export_data($export_config,$data);
    }


    /**
     * 删除图片
     */
    public function ajaxDelete_reimburse_image() {      
        if(IS_POST) {
            $image_id   = I('image_id',0,'int');
            $pid        = I('id',0,'int');
            $has        = M('reimburse_image')->where(['id'=>$image_id,'pid'=>$pid])->find();
            @unlink($has['image']);
            $result = M('reimburse_image')->where(['id'=>$image_id,'pid'=>$pid])->delete();
            if($result == true) $this->success('删除成功');
            $this->error('删除失败');
        }
    }
}

