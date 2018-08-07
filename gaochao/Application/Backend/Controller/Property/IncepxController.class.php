<?php
namespace Backend\Controller\Property;
/**
 * 报销
 * @author llf
 * @time 2016-06-30
 */
class IncepxController extends IndexController {
    
    /**
     * 表名
     * @var string
     */
    protected $table = 'incepx';
     protected $table_view   = 'IncepxView';
    
    /**
     * 列表函数
     */
    public function index(){
        
        $map = $this->_search();

        $result = $this->page(D($this->table_view),$map,'add_time desc');

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

        $post              = I();
        $map               = array('is_del'=>0);

        $district_id       = I('district_id','','int');
        if(!empty($district_id)){
            $map['district_id']   = $district_id;
        }else{
            if(!empty(session('district_ids'))){
                $map['district_id'] = array('IN',session('district_ids'));
            }
        }

        $start = !empty($post['start_date'])?$post['start_date']:0;
        $end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
        $map['add_time'] = array('BETWEEN',array($start,$end));

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
    protected function operate(){
        $info = get_info($this->table, array('id'=>I('ids')));
        $data['info'] = $info;

        //获取对应的账号地下的所有小区信息
        $map  = array('is_del'=>0, 'is_hid'=>0);
        if(!empty(session('district_ids'))){
            $map['id'] = array('IN',session('district_ids'));
        }
        $district_list  = M('district')->where($map)->field('id,title')->select();
        $data['pid']    = array_to_select($district_list, $info['district_id']);     

        $this->assign($data);
        $this->display('operate');
    }
    
    /**
     * 修改
     * @author llf
     * @time 2016-05-31
     */
    protected function update(){

        $post = I('post.');

        $add_time = date('Y-m-d H:i:s', strtotime(I('salary_time','','trim')));

        /*基本信息*/

        $data = array(
            'district_id'     => I('district_id',0,'int'),
            'credence'        =>  I('credence','','trim'),
            'income'          =>  I('income','','int'),
            'expenses'        =>  I('expenses','','int'),
            'remark'          =>  I('remark',0,'trim'),
            'add_time'        => date('Y-m-d H:i:s')
        );

        if($post['ids']){
            $data['id'] = intval($post['ids']);
        }

        // `id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
        // `district_id`  int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '小区ID' ,
        // `credence`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '凭证编号' ,
        // `income`  decimal(15,2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '收入' ,   
        // `expenses`  decimal(15,2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '支出' ,
        // `total`  decimal(15,2) NOT NULL DEFAULT 0.00 COMMENT '累计金额' ,
        // `remark`  mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL ,
        // `status`  tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '收入还是支出:1收入,2支出,0中间值' ,
        // `is_hid`  tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否禁用:0否,1是' ,
        // `is_del`  tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除:0否,1是' ,
        // `add_time`  datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ,

        // if($data['income'])

        if(empty($data['income']) && empty($data['expenses'])){
            $this->error('收入和支出不能同时为空');
        }

        if(!empty($data['income']) && !empty($data['expenses'])){
            $this->error('收入和支出不能同时存在');
        }

        if(!empty($data['income'])){
            $data['status'] = 1;
        }

        if(!empty($data['expenses'])){
            $data['status'] = 2;
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

    public function del(){
        F('owner_no_hid',null);
        F('owner_no_del',null);
        parent::del();
    }
}

