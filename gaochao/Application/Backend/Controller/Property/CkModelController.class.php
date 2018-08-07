<?php
namespace Backend\Controller\Property;
/**
 * 设定打卡时间
 * @author llf
 * @time 2016-06-30
 */
class CkModelController extends IndexController {
    
    /**
     * 表名
     * @var string
     */
    protected $table = 'CkModel';
     protected $table_view   = 'CkModelView';
    
    /**
     * 列表函数
     */
    public function index(){
        
        $map = $this->_search();

        $result = $this->page(D($this->table_view),$map,'district_id, id asc');

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
            'district_id' =>  I('district_id',0,'int'),
            'clock_time'   => I('clock_time','','trim'),
            'add_time'    =>  date('Y-m-d H:i:s'),
            'company_id'      => session('company_id')
        );

        $map  = array(
            'district_id' => $data['district_id'],
            'company_id'  => session('company_id'),
            'is_del'      => 0
        );

        $count = M('ck_model')->where($map)->count();
        if($count >= 2){
            $this->error('该小区已设定完打卡模式');
        }

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

