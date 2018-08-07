<?php
namespace Backend\Controller\Property;
/**
 * 保洁维修管理
 * @author llf
 * @time 2016-06-30
 */
class PayOrderController extends IndexController {
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'payOrder';
    protected $table_view = 'PayView';
    protected $payOrder ='payOrder';
	
	/**
	 * 列表函数
	 */
	public function index(){
		
		$map = $this->_search();

		$result = $this->page(D($this->table_view),$map,'district_id desc, add_time desc');

        $map1  = array('is_del'=>0, 'is_hid'=>0);
        if(!empty(session('district_ids'))){
            $map1['id'] = array('IN',session('district_ids'));
        }
        $district_list  = M('district')->where($map1)->field('id,title')->select();
        $result['pid']    = array_to_select($district_list, $info['district_id']); 

        // var_dump($map);

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

        $map['status']   =  1;

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
     * 完成缴费
     * @author llf
     * @time 2016-05-31
     */

     public function completed(){

        $info = get_info($this->table, array('id'=>I('ids')));
        if(intval($info['status']) == 2){
            $this->error('已缴费');
        } 

        try{
           
            //开启事务
            $M = M();
            $M->startTrans();

            $data['id'] = intval($info['id']);
            $data['status'] = 2;
            $data['pay_time'] = date('Y-m-d H:i:s');
            $result1 = update_data($this->table, [], [], $data);
            if(!is_numeric($result1)){
               $this->error('操作失败');
            }

            $res_dat = M($this->table)->where('id='.$data['id'])->find();


            $owner_info = get_info('owner', array('id'=>intval($info['uid'])));
            $data_owner['id'] = intval($info['uid']);
            $data_owner['pay_start_time'] = date("Y-m-d",strtotime(substr($info['pay_end_time'], 0, 10))); 

            $data_owner['pay_end_time'] = date("Y-m-d",strtotime("+1 year",strtotime(substr($info['pay_end_time'], 0, 10)))); 

            $data_owner['property_money'] = $owner_info['property_price'];
            $data_owner['waste_fee'] = '0';
            $data_owner['wall_waste_fee'] = '0';
            $data_owner['other_fee'] = '0';
            $result2 = update_data('owner', [], [], $data_owner);
            if(!is_numeric($result2)){
                $this->error('操作失败');
            }

            $owner_info = get_info('owner', array('id'=>intval($info['uid'])));
            $order_data = array(
                'uid'         => $owner_info['id'],
                'type'         => 0,
                'order_no'     => build_order_no(),
                'detail_title' => '物业费',
                'money_total'  =>    intval($owner_info['property_money']) + intval($owner_info['waste_fee']) + intval($owner_info['wall_waste_fee']) + intval($owner_info['other_fee']),
                'district_id'     => $owner_info['district_id'],
                'mobile'     => $owner_info['mobile'],
                'username'     => $owner_info['username'],
                'room_no'     => $owner_info['room_no'],
                'pay_start_time'     => $owner_info['pay_start_time'],
                'pay_end_time'     => $owner_info['pay_end_time'],
                'add_time'     => date('Y-m-d H:i:s'),
            );

            $result3 = update_data('payOrder', [], [], $order_data);
            if(!is_numeric($result3)){
               $this->error('操作失败');
            }

            $info = M('Incepx')->where(array('district_id'=>$order_data['district_id']))->order('id desc')->limit(1)->select();
            if(empty($info)){
                $map = array(
                    'income' => $res_dat['money_total'],
                    'district_id' => $res_dat['district_id'],
                    'total' => 1 * floatval($res_dat['money_total']),
                    'remark'=> $res_dat['username'].'--#'.$res_dat['room_no'].'--'.$res_dat['pay_start_time'].'至'.$res_dat['pay_end_time'].'--物业费',
                    'status' => 1,
                    'add_time' => date('Y-m-d H:i:s'),
                );
            }else{
                $map = array(
                    'income' => $res_dat['money_total'],
                    'district_id' => $res_dat['district_id'],
                    'total' => floatval($info[0]['total']) + floatval($res_dat['money_total']),
                    'remark'=> $res_dat['username'].'--#'.$res_dat['room_no'].'--'.$res_dat['pay_start_time'].'至'.$res_dat['pay_end_time'].'--物业费',
                    'status' => 1,
                    'add_time' => date('Y-m-d H:i:s'),
                );
            }
            $result1 = update_data('Incepx', [], [], $map);
            if(!is_numeric($result1)){
                throw new \Exception($result1);
            }
            
        }catch (Exception $e){
            $M->rollback();
            $this->error('操作失败');
        }
        $M->commit();

        $this->error('操作成功');
    }

     /**
     * 物业缴费信息通知
     */
    public function notice(){

            $date = array(
                'is_del'        => 0,
                'is_hid'        => 0,
                'status'         => 1
            );

            if(!empty(session('district_ids'))){
                $date['district_id'] = array('IN',session('district_ids'));
            }
            $date['pay_start_time'] = array('LT', date('Y-m-d H:i:s'));

            $member_votes = M('payOrder')->where($date)->getField('mobile',true);
                  
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
            $content = '小区物业发起物业缴费通知，今年您还有物业费未缴清, 请您尽快通过高超便民APP或前往物业办公室缴清费用. 如已缴费请忽略此通知，谢谢个人业主的配合！';
            foreach ($district_member_ids as $k => $v) {
                $Message->send($v,1,$title,$content);
            }
            $this->error('通知成功');
    }

    
    /**
     * 显示
     * @author llf
     * @time 2016-05-31
     */
    protected function operate(){
        $info = get_info($this->table, array('id'=>I('ids')));
        if(intval($info['status']) == 2){
            $this->error('已缴费');
         } 
        $data['info'] = $info;
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

        if(strlen(I('mobile','','trim'))  != 11){
             $this->error("手机号码为11位");
        }

        $condition = array(
                'is_del'        => 0,
                'is_hid'        => 0,
                'state'         => 2,
                'type'          => 2,
                'mobile'        =>I('mobile','','trim'),
        );

        if(!empty(session('district_ids'))){
            $condition['district_id'] = array('IN',session('district_ids'));
        }

        $data = array(
            'type'         => 0,
            'money_total'  =>    I('money_total',0,'trim'),
            'district_id'     => I('district_id','','trim'),
            'mobile'     => I('mobile','','trim'),
            'username'     => I('username','','trim'),
            'room_no'     => I('room_no','','trim'),
            'pay_start_time'   => date('Y-m-d',strtotime(I('pay_start_time','','trim'))),
            'pay_end_time'   => date('Y-m-d',strtotime(I('pay_end_time','','trim'))),
            'add_time'     => date('Y-m-d H:i:s'),
        );

        if($post['ids']){
            $data['id'] = intval($post['ids']);
        }else{
        	
	        $member_votes = M('Member')->where($condition)->getField('id',true);

        	$data['order_no'] = build_order_no();
        	$data['member_id'] = $member_votes[0];
        	$data['detail_title'] = '物业费';
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
     * 订单导出
     * @author llf
     * @param
     */
    public function export(){

        $map    = $this->_search();

        $result = get_result(D($this->table_view), $map,'add_time desc');

        /*填充数据*/
        $data['result']  = $result;

        /*填充表名*/
        $data['sheetName'] = 'pay_order_export'.date('Ymd_His');


        $export_config = array(
            array('title' => '订单号', 'field' => 'order_no'),
            array('title' => '小区', 'field' => 'district_title'),
            array('title' => '姓名', 'field' => 'username'),
            array('title' => '手机号', 'field' => 'mobile'),
            array('title' => '房号', 'field' => 'room_no'),
            array('title' => '总金额', 'field' => 'money_total'),
            array('title' => '缴费起', 'field' => 'pay_start_time'),
            array('title' => '缴费止', 'field' => 'pay_end_time'),
            array('title' => '订单类型', 'field' => 'detail_title'),
            array('title' => '添加时间', 'field' => 'add_time'),
        );
        A('Common/Excel','',1)->export_data($export_config,$data);
    }

}

