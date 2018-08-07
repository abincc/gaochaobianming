<?php
namespace Backend\Controller\Property;
/**
 * 综合收费
 * @author llf
 * @time 2016-06-30
 */
class ComprehensiveController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'comprehensive';
     protected $table_view   = 'ComprehensiveView';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page(D($this->table_view),$map,'cost_time desc');
        $result['type'] = get_table_state($this->table,'type');

        $map1  = array('is_del'=>0, 'is_hid'=>0);
        if(!empty(session('district_ids'))){
            $map1['id'] = array('IN',session('district_ids'));
        }
        $district_list  = M('district')->where($map1)->field('id,title')->select();
        $result['pid']    = array_to_select($district_list, $info['district_id']);  


        // var_dump($result);

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
        $map['cost_time'] = array('BETWEEN',array($start,$end));

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
     * 编辑
     * @author llf
     * @time 2016-05-31
     */
    public function com_exp(){
         $this->operate('export');
    }


    /**
     * 综合收费导出
     * @author llf
     * @param
     */
    public function com_export(){

        $map    = $this->_search();

        $result = get_result(D($this->table_view), $map,'add_time desc');
        $type_s = get_table_state($this->table,'type');

        foreach ($result as $k => $v) {
            $result[$k]['type'] = $type_s[$v['type']]['title'];
        }

        /*填充数据*/
        $data['result']  = $result;

        /*填充表名*/
        $data['sheetName'] = 'compre_export'.date('Ymd_His');

        $export_config = array(
            array('title' => '小区', 'field' => 'district_title'),
            array('title' => '姓名', 'field' => 'username'),
            array('title' => '费用类型', 'field' => 'type'),
            array('title' => '消费时间', 'field' => 'cost_time'),
            array('title' => '综合金额', 'field' => 'cost'),
            array('title' => '描述', 'field' => 'remark'),
        );
        A('Common/Excel','',1)->export_data($export_config,$data);
    }

    
    /**
     * 显示
     * @author llf
     * @time 2016-05-31
     */
    protected function operate($tpl = 'operate'){
        if($tpl == 'export'){
            $this->com_export();
        }else{
            $info = get_info($this->table, array('id'=>I('ids')));
            $data['info'] = $info;
            $result['type'] = get_table_state($this->table,'type');

            // 费用类型获取
            $option = '';
            foreach ($result['type'] as $k => $v) {
                $option = $option.'<option value="'.$k.'">'.$v['remark'].'</option>';
                
            }
            $data['option'] = $option;

            //获取对应的账号地下的所有小区信息
            $map  = array('is_del'=>0, 'is_hid'=>0);
            if(!empty(session('district_ids'))){
                $map['id'] = array('IN',session('district_ids'));
            }
            $district_list  = M('district')->where($map)->field('id,title')->select();
            $data['pid']    = array_to_select($district_list, $info['district_id']);     

            $district_list  = M('comprehensive')->where($map)->field('id')->select();

            $this->assign($data);
            $this->display('operate');
        }
    }
    
    /**
     * 修改
     * @author llf
     * @time 2016-05-31
     */
    protected function update(){

        $post = I('post.');

        $type = get_table_state($this->table,'type');

        /*基本信息*/

        $data = array(
            'district_id'   => I('district_id',0,'int'),
            'username'     =>  I('username','','trim'),
            'type'    =>I('type','','int'),
            'cost_time'   => I('cost_time','','trim'),
            'cost'   => I('cost','','trim'),
            'remark'   => I('remark','','trim'),
        );
        if($post['ids']){
            $data['id'] = intval($post['ids']);
        }

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
            multi_file_upload(I('image'),'Uploads/Parking','comprehensive','id',$result,'image');

            if(!$post['ids']){
                $info = M('Incepx')->where(array('district_id'=>I('district_id',0,'int')))->order('id desc')->limit(1)->select();
                if(empty($info)){
                    $map = array(
                        'income' => $data['cost'],
                        'district_id' => $data['district_id'],
                        'total' => floatval($data['cost']),
                        'remark'=> $data['username'].'--综合收费'.$type[$data['type']]['title'].'--说明：'.$data['remark'],
                        'status' => 1,
                        'add_time' => date('Y-m-d H:i:s'),
                    );
                }else{
                    $map = array(
                        'income' => $data['cost'],
                        'district_id' => $data['district_id'],
                        'total' => floatval($info[0]['total']) + floatval($data['cost']),
                        'remark'=> $data['username'].'--综合收费'.$type[$data['type']]['title'].'--说明：'.$data['remark'],
                        'status' => 1,
                        'add_time' => date('Y-m-d H:i:s'),
                    );
                }
                $result1 = update_data('Incepx', [], [], $map);
                if(!is_numeric($result1)){
                    throw new \Exception($result1);
                }
            }

        } catch (\Exception $e) {
            $M->rollback();
            $this->error($e->getMessage());
        }
        $M->commit();
        $this->success('操作成功',U('index'));
    }

    // public function del(){
    //     F('owner_no_hid',null);
    //     F('owner_no_del',null);
    //     parent::del();
    // }

    public function del(){
        F('owner_no_hid',null);
        F('owner_no_del',null);
        // parent::del();

        $ids = I('ids');
        if(empty($ids)){
            $this->error('请选择删除的数据');
        }
        if(!is_array($ids)){
            $this->error('内部错误');
        }

        /**
          *  薪资删除-- 对应收支明细出现变动 薪资为支出(删除薪资即为收入)
          *  @author abincc
          *  @time 20180718
          */
        try {
            $M = M();
            $M->startTrans();

            $map = array(
                'is_del' => 1        
            );

            foreach ($ids as $k => $v) {
                $map['id'] = $v;

                $comprehensive = M($this->table)->where(array('id'=>$v,'is_del'=>0))->find();
                if(empty($comprehensive)){
                    $this->error($v.'-综合收费不存在');
                }

                $result = update_data('comprehensive', [], [], $map);
                if(!is_numeric($result)){
                    throw new \Exception('删除'.$v.'失败');
                }

                $incepx = array(
                    'district_id' => $comprehensive['district_id'],
                    'expenses'    => $comprehensive['cost'],
                    'remark'      => '综合收费删除--'.$comprehensive['username'].' 综合收费时间 '.$comprehensive['cost_time'].' 综合收费错误-删除, 退回费用'.$comprehensive['cost'],
                    'status'      => 2,
                    'add_time'    => date('Y-m-d H:i:s')  
                );

                $incepx_result = update_data('incepx', [], [], $incepx);
                if(!is_numeric($incepx_result)){
                    throw new \Exception($comprehensive['username'].'综合收费时间 '.$comprehensive['cost_time'].' 综合收费回滚失败');
                }

            }

        } catch (\Exception $e) {
            $M->rollback();
            $this->error($e->getMessage());
        }
        $M->commit();
        $this->success('操作成功',U('index'));
    }


    /**
     * 收支明细导出
     * @author llf
     * @param
     */
    public function export(){

        if(!empty(session('district_ids'))){
            $map = array(
                'district_id' => array('IN',session('district_ids'))
            );
        }

        $result = M('Incepx as a')->join('sr_district as b on a.district_id = b.id')->field('a.*, b.title')->where($map)->order('district_id, add_time asc')->select();

        foreach ($result as $key => $value) {
            if($key > 0){
                $result[$key]['total'] = floatval($result[$key-1]['total']) + floatval($result[$key]['income']) - floatval($result[$key]['expenses']);
            }else{
                $result[$key]['total'] = floatval($result[$key]['total']);
            }
        }

        /*填充数据*/
        $data['result']  = $result;

        /*填充表名*/
        $data['sheetName'] = 'incexp_export'.date('Ymd_His');

        $export_config = array(
            array('title' => '日期', 'field' => 'add_time'),
            array('title' => '小区', 'field' => 'title'),
            array('title' => '凭证编号', 'field' => 'credence'),
            array('title' => '摘要', 'field' => 'remark'),
            array('title' => '收入', 'field' => 'income'),
            array('title' => '支出', 'field' => 'expenses'),
            array('title' => '累计金额', 'field' => 'total'),
        );
        A('Common/Excel','',1)->export_data($export_config,$data);
    }

    /**
     * 删除图片
     */
    public function ajaxDelete_comprehensive() {      
        if(IS_POST) {
            $id   = I('image_id',0,'int');
            $has  = M('comprehensive')->where(['id'=>$id])->find();
            @unlink($has['image']);
            $data = array(
                'id' => $id,
                'image' => ''
            );

            try {
                $M = M();
                $M->startTrans();

                $result = update_data($this->table, [], [], $data);
                if(!is_numeric($result)){
                    throw new \Exception('删除失败');
                }

            } catch (\Exception $e) {
                $M->rollback();
                $this->error($e->getMessage());
            }
            $M->commit();
            $this->success('删除成功');
        }
    }
}

