<?php
namespace Backend\Controller\Property;
/**
 * 报销
 * @author llf
 * @time 2016-06-30
 */
class JobController extends IndexController {
    
    /**
     * 表名
     * @var string
     */
    protected $table = 'CkJob';
     protected $table_view   = 'CkJobView';
    
    /**
     * 列表函数
     */
    public function index(){
        
        $map = $this->_search();

        $result = $this->page(D($this->table_view),$map,'district_id, id desc');

        $map1  = array('is_del'=>0, 'is_hid'=>0);
        if(!empty(session('district_ids'))){
            $map1['id'] = array('IN',session('district_ids'));
        }
        $district_list  = M('district')->where($map1)->field('id,title')->select();

        foreach ($result['list'] as $k => $v) {
            $member = M('ck_member')->where('id='.$v['member_id'])->field('id,username, mobile')->find();   
            $result['list'][$k]['member'] = $member;

            $info = get_info('admin',array('id'=>$v['change_id']),array('id', 'username'));
            $result['list'][$k]['admin'] = $info;

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


        $result['type_s'] = get_table_state('ck_job','type');
        $result['district'] = array_to_select(get_no_hid('district','add_time desc'), I('district_id'));

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
        $map['company_id'] = session('company_id');
        $keyword           = I('keywords','','trim'); 
        $district_ids       = session('district_ids');
        array_push($district_ids ,'0');

        $district_id       = I('district_id','','int');
        if(!empty($district_id)){
            $map['district_id']   = $district_id;
        }else{
            if(!empty(session('district_ids'))){
                $map['district_id'] = array('IN',$district_ids);
            }
        }
        
        if(!empty($keyword)){
            $map['username']   = array('LIKE',"%$keyword%");
        }

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
    protected function operate($tpl = 'operate'){
        $info = get_info($this->table, array('id'=>I('ids')));
        $data['info'] = $info;

        //获取对应的账号地下的所有小区信息
        // $map  = array('is_del'=>0, 'is_hid'=>0);
        // if(!empty(session('district_ids'))){
        //     $map['id'] = array('IN',session('district_ids'));
        // }
        // $district_list  = M('district')->where($map)->field('id,title')->select();
        // $data['pid']    = array_to_select($district_list, $info['district_id']);  

        $result['status'] = get_table_state('ck_job','type');

        //职位类型获取
        $options = '';
        foreach ($result['status'] as $k => $v) {
            if($v['r_value'] == $info['job_id']){
                $options = $options.'<option value="'.$k.'" selected = "selected">'.$v['title'].'</option>';
            }else{
                $options = $options.'<option value="'.$k.'">'.$v['title'].'</option>';
            }
            
        }
        $data['job'] = $options;  



        $map  = array('is_del'=>0, 'is_hid'=>0);
        $district_ids = session('district_ids');
        array_push($district_ids, '0');
        if(!empty(session('district_ids'))){
            $map['district_id'] = array('IN',$district_ids);
        }
        $member = M('ck_member')->where($map)->field('id,username')->select();

        //用户获取
        $options_m = '';
        foreach ($member as $k => $v) {
            if($v['id'] == $info['member_id']){
                $options_m = $options_m.'<option value="'.$v['id'].'" selected = "selected">'.$v['username'].'</option>';
            }else{
                $options_m = $options_m.'<option value="'.$v['id'].'">'.$v['username'].'</option>';
            }
            
        }
        $data['member'] = $options_m;


        $this->assign($data);
        $this->display($tpl);
    }
    
    /**
     * 修改
     * @author llf
     * @time 2016-05-31
     */
    protected function update(){

        $post = I('post.');

        $data = array(
            'job_id'      =>  I('job_id','','trim'),
            'member_id'   =>  I('member_id','','trim'),
            'change_id'   =>  session('member_id'),
            'add_time'    =>  date('Y-m-d H:i:s'),
        );

        $member = M('ck_member')->where('id='.$data['member_id'])->field('id,district_id')->find();
        if(empty($member)){
            $this->error('用户不存在');
        }
        $data['district_id'] = $member['district_id'];

        if(intval($member['district_id']) == 0){
            if(intval($data['job_id']) != 1){
                $this->error('该用户只能任命为总经理');
            }
        }

        if(intval($data['job_id']) == 1){
             if(intval($member['district_id']) != 0){
                $this->error('该用户不能任命该职位');
             }
        }

        $map = array(
            'member_id' => $data['member_id'],
            'is_del'    => 0 
        );
        $member_job = M('ck_job')->where($map)->find();

        if($post['ids']){
            $data['id'] = intval($post['ids']);
        }else{
            if(!empty($member_job)){
                $this->error('该员工已担任职位');
            }
        }

        try {
            $M = M();
            $M->startTrans();
            $data['company_id'] = session('company_id');

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

