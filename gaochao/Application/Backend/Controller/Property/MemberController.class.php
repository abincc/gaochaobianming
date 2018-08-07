<?php
namespace Backend\Controller\Property;
/**
 * 小程序用户信息
 * @author llf
 * @time 2016-06-30
 */
class MemberController extends IndexController {
    
    /**
     * 表名
     * @var string
     */
    protected $table = 'CkMember';
     protected $table_view   = 'CkMemberView';
    
    /**
     * 列表函数
     */
    public function index(){
        
        $map = $this->_search();

        // $field = array('id', 'company_id', 'district_id', 'mobile', 'username', 'sex', 'birthday','is_hid');
        $result = $this->page(D($this->table),$map,'company_id, district_id, id desc');

        $company = M('company')->where(array('is_del'=>0, 'is_hid'=>0))->select();
        foreach ($result['list'] as $k => $v) {
            foreach ($company as $m => $n) {
                if($v['company_id'] = $n['id']){
                    $result['list'][$k]['company'] = $n['name'];
                    break;
                }
            }
        }

        // var_dump($result);
        // die();
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
        $result['pid'] = $option;

        // var_dump($result);
        // die();

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
                $map['district_id'] = array('IN', $district_ids);
            }
        }
        
        if(!empty($keyword)){
            $map['username']   = array('LIKE',"%$keyword%");
        }

        return $map;
    }

    public function sign(){

       $info = get_info($this->table, array('id'=>I('ids')));
       $add_time = I('add_time','','trim');
       $noe_time = substr(date('Y-m-d H:i:s'), 0,10);

       $map = array(
            'company_id' => session('company_id'),
            'member_id'  => $info['id'],
            'district_id'=> $info['district_id'],
            'is_hid'     => 0,
            'is_del'     => 0
       );

       if(!empty($add_time)){
            $map['add_time']   = array('LIKE',"%$add_time%");
       }else{
            $map['add_time']   = array('LIKE',"%$noe_time%");
       }

       $work = M('ck_work')->where($map)->select();

       $data['info'] = $work;

       $this->assign($data);
        $this->display($tpl);
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
        $option = intval($info['district_id']) == 0?'<option  selected="selected" value=0>全部小区</option>':'<option value=0>全部小区</option>';
        foreach ($district_list as $k => $v) {
            if($info['district_id'] == $v['id']){
                $option .= "<option selected='selected' value='{$v['id']}'>{$v['title']}</option>";
            }else{
                $option .= "<option value='{$v['id']}'>{$v['title']}</option>";
            }
        }
        $data['pid'] = $option;  

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

        $data = array(
            'district_id'     =>  I('district_id',0,'int'),
            'username'        =>  I('username','','trim'),
            'mobile'          =>  I('mobile','','trim'),
            'birthday'        =>  I('birthday','','trim'),
            'register_time'   =>  date('Y-m-d H:i:s'),
            'password'        =>  sys_password_encrypt('123456'),
            'sex'             =>  1,
            'company_id'      => session('company_id')
        );

        if($post['ids']){
            $data['id'] = intval($post['ids']);
        }else{
            $res = M($this->table)->where('mobile='.$data['mobile'])->find();
            if(!empty($res)){
                $this->error('该手机号码已经注册');
            }
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

