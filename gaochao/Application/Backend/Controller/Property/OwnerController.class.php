<?php
namespace Backend\Controller\Property;
/**
 * 住户管理
 * @author llf
 * @time 2016-06-30
 */
class OwnerController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'owner';
    protected $district ='district';
    protected $table_view   = 'OwnerView';
    protected $payOrder ='payOrder';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page(D($this->table_view),$map,'district_id asc, add_time desc, uid asc');

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

		$post 			   = I();
		$map 			   = array('is_del'=>0);
		$keyword 		   = I('keywords','','trim'); 
		$month         = I('month','','trim'); 


		if(!empty($keyword)){
			$map['mobile|username|room_no']   = array('LIKE',"%$keyword%");
		}
        if(!empty($month)){
            $month = str_pad(intval($month),2,'0',STR_PAD_LEFT);
            $map['pay_start_time']   = array('LIKE',"_____$month%");
        }
		
		$district_id       = I('district_id','','int');
        if(!empty($district_id)){
            $map['district_id']   = $district_id;
        }else{
            if(!empty(session('district_ids'))){
                $map['district_id'] = array('IN',session('district_ids'));
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
    * 批量生成订单
    */
    public function addBatchOrder(){
        $this->operate('addBatchOrder');
    }

    
    /**
     * 显示
     * @author llf
     * @time 2016-05-31
     */
    protected function operate($tpl='operate'){

        if($tpl == 'addBatchOrder'){
            $ids = I('ids');
            if (empty($ids)){
                $this->error('请选择要操作的数据!');
            }
            $this->error(batchOrder($ids));
        }else if($tpl == 'export'){
            $map = $this->_search();
            
            $result = get_result(D($this->table_view), $map,'uid desc');

            /*填充数据*/
            $data['result']    = $result;
            /*填充表名*/
            $data['sheetName'] = 'owner_'.date('Ymd_His');

            $export_config = array(
                array('title' => '序号', 'field' => 'uid'),
                array('title' => '小区', 'field' => 'district_title'),
                array('title' => '姓名', 'field' => 'username'),
                array('title' => '联系方式', 'field' => 'mobile'),
                array('title' => '房号', 'field' => 'room_no'),
                array('title' => '交房日期', 'field' => 'room_time'),
                array('title' => '面积', 'field' => 'area'),
                array('title' => '楼层单价', 'field' => 'house_unit_price'),
                array('title' => '物业费/年', 'field' => 'property_price'),
                array('title' => '物业费', 'field' => 'property_money'),
                array('title' => '物业费起始日期', 'field' => 'start_time'),
                array('title' => '缴费起', 'field' => 'pay_start_time'),
                array('title' => '缴费止', 'field' => 'pay_end_time'),
                array('title' => '装修押金', 'field' => 'deposit'),
                array('title' => '垃圾清理费', 'field' => 'waste_fee'),
                array('title' => '拆墙垃圾费', 'field' => 'wall_waste_fee'),
                array('title' => '其他费用', 'field' => 'other_fee'),
                array('title' => '是否装修', 'field' => 'is_renovation'),
                array('title' => '是否退押金', 'field' => 'is_deposit'),
                array('title' => '欠费原因', 'field' => 'arrears_content'),
                array('title' => '备注', 'field' => 'remark')
            );

            A('Common/Excel','',1)->export_data($export_config,$data);
        }else{
            $info = get_info($this->table, array('id'=>I('ids')));
            $data['info'] = $info;
            $map  = array('is_del'=>0, 'is_hid'=>0);
            if(!empty(session('district_ids'))){
                $map['id'] = array('IN',session('district_ids'));
            }
            $district_list  = M('district')->where($map)->field('id,title')->select();
            $data['pid']    = array_to_select($district_list, $info['district_id']);

            $this->assign($data);
            $this->display($tpl);
        }
    }
    
    /**
     * 修改
     * @author llf
     * @time 2016-05-31
     */
    protected function update(){

        $post = I('post.');

        /*基本信息*/

        if(strlen(I('mobile','','trim'))  != 11){
             $this->error("手机号码为11位");
        } 

        $data = array(
            'district_id'   => I('district_id',0,'int'),
            'username'     =>  I('username','','trim'),
            'mobile'    =>I('mobile','','trim'),
            'room_no'   => I('room_no','','trim'),
            'area'   => I('area','','trim'),
            'house_unit_price'   => I('house_unit_price','','trim'),
            'property_price'   => I('property_price','','trim'),
            'property_money'   => I('property_money','','trim'),
            'pay_start_time'   => date('Y-m-d',strtotime(I('pay_start_time','','trim'))),
            'pay_end_time'   => date('Y-m-d',strtotime(I('pay_end_time','','trim'))),
            'house_time'   => date('Y-m-d',strtotime(I('house_time','','trim'))),
            'deposit'   => I('deposit','','trim'),
            'waste_fee'   => I('waste_fee','','trim'),
            'wall_waste_fee'   => I('wall_waste_fee','','trim'),
            'arrears_content'  => I('arrears_content','','trim'),
            'other_fee'  => I('other_fee','','trim'),
            'remark'  => I('remark','','trim'),
            'is_renovation' => I('is_renovation','','int'),
            'is_deposit' => I('is_deposit','','int'),
        );

        if(!checkDateTime2(I('room_time','','trim'))){
            $data['room_time'] = date('Y-m-d','0');
        }else{
             $data['room_time'] = date('Y-m-d',strtotime(I('room_time','','trim')));
        }

        if(!checkDateTime2(I('start_time','','trim'))){
            $data['start_time'] = date('Y-m-d','0');
        }else{
             $data['start_time'] = date('Y-m-d',strtotime(I('start_time','','trim')));
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


     /**
     * 生成订单  -- 02号
     */
    public function addPayOrder(){
        $_data = [];
        $rules = array();
        $info = $this->_edit('住户信息不存在');
       
        if(!IS_POST) {
            
            $order = array(
            'status'        => 1,
            'is_del' => 0,
            'uid'     =>$info['id']
            );


            $order_list  = M('payOrder')->where($order)->field('id')->select();
            if(count($order_list) > 0){
                $this->error('已存在未处理订单');
            }else{

                $data = array(
                'uid'         => $info['id'],
                'type'         => 0,
                'order_no'     => build_order_no(),
                'detail_title' => '物业费',
                'money_total'  =>    intval($info['property_money']) + intval($info['waste_fee']) + intval($info['wall_waste_fee']) + intval($info['other_fee']),
                'district_id'     => $info['district_id'],
                'mobile'     => $info['mobile'],
                'username'     => $info['username'],
                'room_no'     => $info['room_no'],
                'pay_start_time'     => $info['pay_start_time'],
                'pay_end_time'     => $info['pay_end_time'],
                'add_time'     => date('Y-m-d H:i:s'),
                );

                $result = update_data($this->payOrder, $rules, $map, $data);
                // var_dump($result.'----:::::----'.is_numeric($result));
                if(is_numeric($result)){
                     $this->error('操作成功');
                }else{
                    $this->error('操作失败');
                }
            }
               
        }else{
            $this->error('操作失败');
        }
    }


    /**
     * 提前通知物业缴费信息
     */
    public function notice(){

            $date = array(
                'is_del'        => 0,
                'is_hid'        => 0,
                'month'         => intval(date('m'))
            );

            if(!empty(session('district_ids'))){
                $date['district_id'] = array('IN',session('district_ids'));
            }

            $member_votes = M('owner')->where($date)->getField('mobile',true);
                  
            $condition = array(
                'is_del'        => 0,
                'is_hid'        => 0,
                'state'         => 2,
                'type'          => 2,
            );

            if(!empty(session('district_ids'))){
                $condition['district_id'] = array('IN',session('district_ids'));
            }

            if(!empty($member_votes)){
                $condition['mobile'] = array('in',$member_votes);
            }else{
                 $this->error('暂无可通知人员');
            }
            $district_member_ids = M('Member')->where($condition)->getField('id',true);

            if(empty($district_member_ids)){
                $this->error('暂无可通知人员');
            }
            $Message = D('Message');
            $title   = '物业缴费通知';
            $content = '小区物业发起物业缴费通知，尊敬的业主您好, '.date('Y').'物业费将要开始缴纳,在此提前通知请您在接下来的几天中尽快通过高超便民APP或者前往物业办公室缴清物业费用. 如已缴费请忽略此通知，谢谢各位业主积极配合！';
            foreach ($district_member_ids as $k => $v) {
                $Message->send($v,1,$title,$content);
            }
            $this->error('通知成功');
    }



    /**
     * 业主导出
     * @author llf
     * @param
     */
    public function export(){
        $this->operate('export');
    }


	 /**
     * 业主导入
     * @author xiaobin
     * @param type
     */
    public function import(){

        $district_ids = session('district_ids');
        if(empty($district_ids)){
            $this->error('该类型账号不能导入');
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
                'rootPath'   => './',				//相对网站根目录
                'savePath'   => 'Uploads/Tbook/Tbook/',	//文件具体保存的目录
                'saveName'   => array('uniqid',''),
                'exts'       => array('csv','xlsx','xls'),
                'autoSub'    => true,
                'subName'    => array('date','Ymd'),
            );
            $Upload 	 = new \Think\Upload($config);        /* 实例化上传类 */
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
                array('title' => 'room_no', 'name' => 'room_no'),
                array('title' => 'username', 'name' => 'username'),
                array('title' => 'mobile', 'name' => 'mobile'),
                array('title' => 'room_time', 'name' => 'room_time'),
                array('title' => 'area', 'name' => 'area'),
                array('title' => 'house_unit_price', 'name' => 'house_unit_price'),
                array('title' => 'property_price', 'name' => 'property_price'),
                array('title' => 'property_money', 'name' => 'property_money'),
                array('title' => 'start_time', 'name' => 'start_time'),
                array('title' => 'pay_start_time', 'name' => 'pay_start_time'),
                array('title' => 'pay_end_time', 'name' => 'pay_end_time'),
                array('title' => 'deposit', 'name' => 'deposit'),
                array('title' => 'waste_fee', 'name' => 'waste_fee'),
                array('title' => 'is_renovation', 'name' => 'is_renovation'),
                array('title' => 'is_deposit', 'name' => 'is_deposit'),
                array('title' => 'other_fee', 'name' => 'other_fee'),
                array('title' => 'wall_waste_fee', 'name' => 'wall_waste_fee'),
                array('title' => 'arrears_content', 'name' => 'arrears_content'),
                array('title' => 'remark', 'name' => 'remark'),
            );
            $data = A('Common/Excel','',1)->im_excel($import_config, $file);
//             var_dump($data);
//             die();
            try{
                if(empty($data)){
                    throw new \Exception("导入数据内容未空或导入文件解析失败");
                }
                //开启事务进行循环插入操作
                $SKU = M("owner");
                $M = M();
                $M->startTrans();
                if($type == 1){
                    foreach ($data as $k => $v) {
                        if(in_array($v['district_id'],$district_ids)){
                            if(empty($v['username']) || empty($v['mobile']) || empty($v['district_id']) || empty($v['property_price'])  ||  empty($v['pay_start_time']) || empty($v['pay_end_time']) ||  empty($v['uid'])){
                                continue;
                            }else{
                                
                                $datasql = array(
                                'uid'              => intval($v['uid']),
                                'district_id'      => $v['district_id'],
                                'room_no'          => strval($v['room_no']),
                                'username'         => $v['username'],
                                'mobile'           => $v['mobile'],
                                'area'             => empty($v['area'])?'0':$v['area'],
                                'house_unit_price' => empty($v['house_unit_price'])?'0':$v['house_unit_price'],
                                'property_price'   => $v['property_price'],
                                'property_money'   => empty($v['property_money'])?'0':$v['property_money'],
                                'pay_start_time'   => $v['pay_start_time'],
                                'pay_end_time'     => $v['pay_end_time'],
                                'deposit'          => empty($v['deposit'])?'0':$v['deposit'],
                                'waste_fee'        => empty($v['waste_fee'])?'0':$v['waste_fee'],
                                'is_renovation'    => empty($v['is_renovation'])?'0':$v['is_renovation'],
                                'is_deposit'       => empty($v['is_deposit'])?'0':$v['is_deposit'],
                                'wall_waste_fee'   => empty($v['wall_waste_fee'])?'0':$v['wall_waste_fee'],
                                'arrears_content'  => empty($v['arrears_content'])?'0':$v['arrears_content'],
                                'other_fee'        => empty($v['other_fee'])?'0':$v['other_fee'],
                                'remark'           => empty($v['remark'])?'':$v['remark'],
                                'room_time'        =>  empty($v['room_time'])?date('Y-m-d','0'):$v['room_time'],
                                'start_time'       =>  empty($v['start_time'])?date('Y-m-d','0'):$v['start_time'],
                                );
                             
                                $res = $SKU->add($datasql);
                                if(!is_numeric($res)){
                                    throw new \Exception("库存信息更新失败");  
                                }
                            }
                        }else{
                            continue;
                        }
                    }
                }
            }catch (Exception $e){
                $M->rollback();
                // $this->error($e->getMessage());
                $this->error('导入数据失败');
            }
            $M->commit();
            $this->success('导入数据成功');
        }else{
            $this->display();
        }

    }
	
}

