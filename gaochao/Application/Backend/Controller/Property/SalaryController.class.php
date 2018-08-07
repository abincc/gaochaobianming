<?php
namespace Backend\Controller\Property;
/**
 * 报销
 * @author llf
 * @time 2016-06-30
 */
class SalaryController extends IndexController {
    
    /**
     * 表名
     * @var string
     */
    protected $table = 'salary';
     protected $table_view   = 'SalaryView';
    
    /**
     * 列表函数
     */
    public function index(){
        
        $map = $this->_search();

        $result = $this->page(D($this->table_view),$map,' district_id,salary_time desc');

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
        $keyword           = I('keywords','','trim'); 

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
        $map['salary_time'] = array('BETWEEN',array($start,$end));

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
            'uid'   => I('uid',0,'int'),
            'district_id'     => I('district_id',0,'int'),
            'username'        =>  I('username','','trim'),
            'mobile'          =>  I('mobile','','trim'),
            'job'             =>  I('job','','trim'),
            'entry_time'      =>  date('Y-m-d',strtotime(I('entry_time','','trim'))),
            'day_num'         => I('day_num',0,'int'),
            'all_work'        =>  I('all_work','','trim'),
            'in_work'         =>  I('in_work','','trim'),
            'salary'          =>  I('salary','','trim'),
            'achievements'    =>  I('achievements','','trim'),
            'allowance'       =>  I('allowance','','trim'),
            'subsidy'         =>  I('subsidy','','trim'),
            'overtime_total'  =>  I('overtime_total','','trim'),
            'ready_total'     => I('ready_total','','trim'),
            'total'           => I('total','','trim'),
            'debit'           => I('debit','','trim'),
            'real_total'      => I('real_total','','trim'),
            'salary_time'     =>  date('Y-m-d',strtotime(I('salary_time','','trim'))),
            'remark'          => I('remark','','trim'),
            'company_id'      => session('company_id')  
        );


        if($post['ids']){
            $data['id'] = intval($post['ids']);
        }

        try {
            $M = M();
            $M->startTrans();

            if(empty($data['id'])){
                $info = M('Incepx')->where(array('district_id'=>$data['district_id']))->order('id desc')->limit(1)->select();
                if(empty($info)){
                    $map = array(
                        'expenses' => $data['real_total'],
                        'district_id' => $data['district_id'],
                        'total' => -1 * floatval($data['real_total']),
                        'remark'=> $data['username'].'--'.$data['salary_time'].'--薪资：'.$data['real_total'].'--说明：'.$data['remark'],
                        'status' => 2,
                        'add_time' => date('Y-m-d H:i:s'),
                    );
                }else{
                    $map = array(
                        'expenses' => $data['real_total'],
                        'district_id' => $data['district_id'],
                        'total' => floatval($info[0]['total']) - floatval($data['real_total']),
                        'remark'=> $data['username'].'--'.$data['salary_time'].'--薪资：'.$data['real_total'].'--说明：'.$data['remark'],
                        'status' => 2,
                        'add_time' => date('Y-m-d H:i:s'),
                    );
                }
                $result1 = update_data('Incepx', [], [], $map);
                if(!is_numeric($result1)){
                    throw new \Exception($result1);
                }
            }else{
                $res = M($this->table)->where('id='.$data['id'])->find();

                if(empty($res)){
                    throw new \Exception('该薪资数据不存在');
                }

                if(floatval($res['real_total']) > floatval($data['real_total'])){
                    $info = M('Incepx')->where(array('district_id'=>$data['district_id']))->order('id desc')->limit(1)->select();
                    if(empty($info)){
                        $map = array(
                            'income' => floatval($res['real_total']) - floatval($data['real_total']),
                            'district_id' => $data['district_id'],
                            'total' => 1 * (floatval($res['real_total']) - floatval($data['real_total'])),
                            'remark'=> $data['username'].'--'.$data['salary_time'].'--修改薪资(调低)：'.(floatval($res['real_total']) - floatval($data['real_total'])).'--说明：'.$data['remark'],
                            'status' => 1,
                            'add_time' => date('Y-m-d H:i:s'),
                        );
                    }else{
                        $map = array(
                            'income' => floatval($res['real_total']) - floatval($data['real_total']),
                            'district_id' => $data['district_id'],
                            'total' => floatval($info[0]['total']) + (floatval($res['real_total']) - floatval($data['real_total'])),
                            'remark'=> $data['username'].'--'.$data['salary_time'].'--修改薪资(调低)：'.(floatval($res['real_total']) - floatval($data['real_total'])).'--说明：'.$data['remark'],
                            'status' => 1,
                            'add_time' => date('Y-m-d H:i:s'),
                        );
                    }

                    $result1 = update_data('Incepx', [], [], $map);
                    if(!is_numeric($result1)){
                        throw new \Exception($result1);
                    }
                }else if(floatval($res['real_total']) < floatval($data['real_total'])){
                    $info = M('Incepx')->where(array('district_id'=>$data['district_id']))->order('add_time desc')->limit(1)->select();
                    if(empty($info)){
                        $map = array(
                            'expenses' => floatval($data['real_total']) - floatval($res['real_total']),
                            'district_id' => $data['district_id'],
                            'total' => -1 * (floatval($data['real_total']) - floatval($res['real_total'])),
                            'remark'=> $data['username'].'--'.$data['salary_time'].'--修改薪资(调高)：'.(floatval($data['real_total']) - floatval($res['real_total'])).'--说明：'.$data['remark'],
                            'status' => 2,
                            'add_time' => date('Y-m-d H:i:s'),
                        );
                    }else{
                        $map = array(
                            'expenses' => floatval($data['real_total']) - floatval($res['real_total']),
                            'district_id' => $data['district_id'],
                            'total' => floatval($info[0]['total']) - (floatval($data['real_total']) - floatval($res['real_total'])),
                            'remark'=> $data['username'].'--'.$data['salary_time'].'--修改薪资(调高)：'.(floatval($data['real_total']) - floatval($res['real_total'])).'--说明：'.$data['remark'],
                            'status' => 2,
                            'add_time' => date('Y-m-d H:i:s'),
                        );
                    }

                    $result1 = update_data('Incepx', [], [], $map);
                    if(!is_numeric($result1)){
                        throw new \Exception($result1);
                    }
                }
            }

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

                $salary = M($this->table)->where(array('id'=>$v,'is_del'=>0))->find();
                if(empty($salary)){
                    $this->error($v.'-薪资不存在');
                }

                $result = update_data('salary', [], [], $map);
                if(!is_numeric($result)){
                    throw new \Exception('删除'.$v.'失败');
                }

                $incepx = array(
                    'district_id' => $salary['district_id'],
                    'income'      => $salary['real_total'],
                    'remark'      => '薪资删除--'.$salary['username'].' 薪资时间 '.$salary['salary_time'].' 薪资错误-删除, 返回薪资'.$salary['real_total'],
                    'status'      => 1,
                    'add_time'    => date('Y-m-d H:i:s')  
                );

                $incepx_result = update_data('incepx', [], [], $incepx);
                if(!is_numeric($incepx_result)){
                    throw new \Exception($salary['username'].'薪资时间 '.$salary['salary_time'].' 薪资回滚失败');
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
     * 薪资导出
     * @author llf
     * @param
     */
    public function export(){

        $map = $this->_search();

        $result = get_result(D($this->table_view), $map,'salary_time desc');

        /*填充数据*/
        $data['result']    = $result;
        /*填充表名*/
        $data['sheetName'] = 'sal_export_'.date('Ymd_His');

        $export_config = array(
            array('title' => '序号', 'field' => 'uid'),
            array('title' => '小区', 'field' => 'district_title'),
            array('title' => '姓名', 'field' => 'username'),
            array('title' => '手机号', 'field' => 'mobile'),
            array('title' => '职位', 'field' => 'job'),
            array('title' => '入职时间', 'field' => 'entry_time'),
            array('title' => '本月天数', 'field' => 'day_num'),
            array('title' => '应出勤天数', 'field' => 'all_work'),
            array('title' => '实际出勤天数', 'field' => 'in_work'),
            array('title' => '基本工资', 'field' => 'salary'),
            array('title' => '绩效', 'field' => 'achievements'),
            array('title' => '岗位津贴', 'field' => 'allowance'),
            array('title' => '节日补助', 'field' => 'subsidy'),
            array('title' => '加班工资', 'field' => 'overtime_total'),
            array('title' => '预发工资', 'field' => 'ready_total'),
            array('title' => '应发工资', 'field' => 'total'),
            array('title' => '扣款', 'field' => 'debit'),
            array('title' => '实发工资', 'field' => 'real_total'),
            array('title' => '薪资时间', 'field' => 'salary_time'),
            array('title' => '描述', 'field' => 'remark')
        );
        A('Common/Excel','',1)->export_data($export_config,$data);
    }


     /**
     * 薪资导入
     * @author xiaobin
     * @param type
     */
    public function import(){

        $district_ids = session('district_ids');
        if(empty($district_ids)){
            $this->error('该类型账号不能导入薪资');
        }

        if(IS_POST){
            $type = intval(I('type'));
            if(!$type && !in_array(1)){
                //1-覆盖 2-累加
                $this->error('请选择库存导入模式');
            }
            //上传导入文件
            $config = array(
                'maxSize'    => 3145728,
                'rootPath'   => './',               //相对网站根目录
                'savePath'   => 'Uploads/Tbook/Tbook/', //文件具体保存的目录
                'saveName'   => array('uniqid',''),
                'exts'       => array('csv','xlsx','xls'),
                'autoSub'    => true,
                'subName'    => array('date','Ymd'),
            );
            $Upload      = new \Think\Upload($config);        /* 实例化上传类 */
            $file_info   = $Upload->upload();
            if(!$file_info){
                $this->error("导入文件上传失败");
            }
            // $file =  'C:\Users\cnsunrun\Downloads\sku_export_20170502_182536.xls';
            // $file = './Uploads/Product/Sku/20170502/59085cb6df670.xls';
            $file = './'.$file_info['file']['savepath'].$file_info['file']['savename'];

            $import_config = array(
                array('title' => 'uid', 'name' => 'uid'),
                array('title' => 'district_id', 'name' => 'district_id'),
                array('title' => 'username', 'name' => 'username'),
                array('title' => 'mobile', 'name' => 'mobile'),
                array('title' => 'job', 'name' => 'job'),
                array('title' => 'entry_time', 'name' => 'entry_time'),
                array('title' => 'day_num', 'name' => 'day_num'),
                array('title' => 'all_work', 'name' => 'all_work'),
                array('title' => 'in_work', 'name' => 'in_work'),
                array('title' => 'salary', 'name' => 'salary'),
                array('title' => 'achievements', 'name' => 'achievements'),
                array('title' => 'allowance', 'name' => 'allowance'),
                array('title' => 'subsidy', 'name' => 'subsidy'),
                array('title' => 'overtime_total', 'name' => 'overtime_total'),
                array('title' => 'ready_total', 'name' => 'ready_total'),
                array('title' => 'total', 'name' => 'total'),
                array('title' => 'debit', 'name' => 'debit'),
                array('title' => 'real_total', 'name' => 'real_total'),
                array('title' => 'salary_time', 'name' => 'salary_time'),
                array('title' => 'remark', 'name' => 'remark'),
            );
            $data = A('Common/Excel','',1)->im_excel($import_config, $file);

            try{
                if(empty($data)){
                    throw new \Exception("导入数据内容未空或导入文件解析失败");
                }
                //开启事务进行循环插入操作
                $SKU = M("salary");
                $M = M();
                $M->startTrans();;
                if($type == 1){
                    foreach ($data as $k => $v) {
                        // 序号  小区  姓名  基本工资 实发工资 工资时间不能为空 为空跳过
                        if(in_array($v['district_id'],$district_ids)){
                            if(empty($v['uid']) || empty($v['mobile']) || empty($v['username'])  || empty($v['district_id'])  || empty($v['salary']) || empty($v['real_total']) || empty($v['salary_time'])){
                                continue;
                            }else{

                                $where = array(
                                    'mobile'      => $v['mobile'],
                                    'salary_time' => date('Y-m-d H:i:s',strtotime($v['salary_time']))
                                );
                                $salary = M('salary')->where($where)->find();
                                if(!empty($salary)){
                                    $this->error('用户'.$v['mobile'].'--'.date('Y-m-d H:i:s',strtotime($v['salary_time'])).'已发工资或者存在重复');
                                }

                                $datasql = array(
                                    'uid'             =>  $v['uid'],
                                    'district_id'     =>  $v['district_id'],
                                    'username'        =>  $v['username'],
                                    'mobile'          =>  $v['mobile'],
                                    'job'             =>  $v['job'],
                                    'entry_time'      =>  empty($v['entry_time'])?date('Y-m-d','0'):date('Y-m-d H:i:s',strtotime($v['entry_time'])),
                                    'day_num'         =>  $v['day_num'],
                                    'all_work'        =>  $v['all_work'],
                                    'in_work'         =>  $v['in_work'],
                                    'salary'          =>  $v['salary'],
                                    'achievements'    =>  $v['achievements'],
                                    'allowance'       =>  $v['allowance'],
                                    'subsidy'         =>  $v['subsidy'],
                                    'overtime_total'  =>  $v['overtime_total'],
                                    'ready_total'     =>  $v['ready_total'],
                                    'total'           =>  $v['total'],
                                    'debit'           =>  $v['debit'],
                                    'real_total'      =>  $v['real_total'],
                                    'salary_time'     =>  date('Y-m-d H:i:s',strtotime($v['salary_time'])),
                                    'remark'          =>  $v['remark'],
                                    'company_id'      => session('company_id')  
                                );      

                                $res = $SKU->add($datasql);
                                if(!is_numeric($res)){
                                    throw new \Exception("薪资添加失败");  
                                }

                                $info = M('Incepx')->where(array('district_id'=>$v['district_id']))->order('id desc')->limit(1)->select();
                                if(empty($info)){
                                    $map = array(
                                        'expenses' => $v['real_total'],
                                        'district_id' => $v['district_id'],
                                        'total' => -1 * floatval($v['real_total']),
                                        'remark'=> $datasql['username'].'--薪资：'.$datasql['real_total'].'--说明：'.$datasql['remark'],
                                        'status' => 2,
                                        'add_time' => date('Y-m-d H:i:s'),
                                    );
                                }else{
                                    $map = array(
                                        'expenses' => $v['real_total'],
                                        'district_id' => $v['district_id'],
                                        'total' => floatval($info[0]['total']) - floatval($v['real_total']),
                                        'remark'=> $datasql['username'].'--薪资：'.$datasql['real_total'].'--说明：'.$datasql['remark'],
                                        'status' => 2,
                                        'add_time' => date('Y-m-d H:i:s'),
                                    );
                                }
                                $result1 = update_data('Incepx', [], [], $map);
                                if(!is_numeric($result1)){
                                    throw new \Exception('收支信息添加失败');
                                }
                            }
                        }else{
                            continue;
                        }
                    }
                }
            }catch (Exception $e){
                $M->rollback();
                $this->error($e->getMessage());
            }
            $M->commit();
            $this->success('导入数据成功');
        }else{
            $this->display();
        }

    }
}

