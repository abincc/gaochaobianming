<?php
namespace Backend\Controller\Base;
	/**
	 * 后台入口
	 */
class IndexController extends AdminController {

	/*产品表*/
	protected $tables = [
		'Product' => 'product',
		'Member'  => 'member',
		'Order'	  => 'order'
	];
	protected $table = 'capital_record';

	/**
	 * 首页
	 */
	public function index() {

		if(session('is_owner')){
			$this->owner();
			exit;
		}

		$data = [];
		$order_condition = array(
				'status' => array('EQ',20),
				'is_del' => 0,
				'is_hid' => 0, 
			);
		$data['order_num']		= M($this->tables['Order'])->where($order_condition)->field('count(id) tp_sum')->find();
		$order_condition['status'] = array('GT',20,30,40,50,60);
		$data['total']			= M($this->tables['Order'])->where($order_condition)->field('SUM(`money_total`) tp_sum')->find();
		$data['member_num']		= M($this->tables['Member'])->field('count(id) tp_sum')->find();
		$data['product_num']	= M($this->tables['Product'])->field('count(id) tp_sum')->find();

		//最近订单
		$order_map = array(
				'is_del'=>0,
				'is_hid'=>0,
				'status'=>array('IN',array(10,20,40,70)),
			);
		$field = array('id','order_no','username','money_total','add_time','status');
		$data['new_order']	  = get_result(D('OrderView'),$order_map,'add_time desc',$field,7);
		$data['order_status'] = get_table_state($this->tables['Order'],'status');

		//最近注册用户
		$field = array('id','username','mobile','register_ip','register_time');
		$map   = array('is_del'=>0,'is_hid'=>0);
		$data['near_register'] = get_result($this->tables['Member'],$map,'register_time desc',$field,7);

		//月度订单报表
		$data['order_count_date'] = $this->order_count_date();

		// bug($data['near_order']);
		$this->assign($data);
		$this->display();
	}

	/**
	 * 首页---(物业管理员)
	 */
	public function owner(){

		$data = [];

		$map1  = array('is_del'=>0, 'is_hid'=>0);
        $map1['id'] = array('IN',session('district_ids'));

		//最近注册用户
		$field = array('id','username','mobile','register_ip','register_time');
		$map   = array('is_del'=>0,'is_hid'=>0,'district_id'=>array('IN',session('district_ids')));
		$data['near_register'] = get_result($this->tables['Member'],$map,'register_time desc',$field,7);

		$district_list  = M('district')->where($map1)->field('id,title')->select();
        $data['pid']    = array_to_select($district_list, $info['district_id']); 


		//会员饼状图
		$data['member_level_count'] = $this->member_level_count();

		$data['property_service_count'] = $this->property_service_count();

		$data['wuye_pay_order'] = $this->wuye_pay_order();


		$data['wuye_income_expenses'] = $this->wuye_income_expenses();

		$data['income_total'] = $this->income_total();

		$data['pay_total'] = $this->pay_total();

		$data['owner'] = session('is_owner');


		// var_dump($this->pay_total());/

		$this->assign($data);
		$this->display('owner');
	}

	/**
	 * 会员等级数量统计
	 * @author llf
	 */
	public function member_level_count(){

		$map = array(
			'is_del'=>0
		);

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map['district_id'] = I('district_id','','int');
		}else{
			$map['district_id'] = array('IN',session('district_ids'));	
		}

		$count = get_result('member',$map,'',['count(`id`) as c','type'],'','type');
		$type  = get_table_state('member','type');
		$d     = array();
		$count = array_column($count,null,'type');
		foreach ($type as $k => $v) {
			if(!empty($count[$v['r_value']])){
				$d[] = array('value'=>$count[$v['r_value']]['c'],'name'=>$v['title']);
			}else{
				$d[] = array('value'=>0,'name'=>$v['title']);
			}
		}	

		return array_values($d);
	}


	/**
	 * 收入类型统计
	 * @author abincc
	 */
	public function income_total(){

		$year = I('year','','trim');
		$month = I('month','','trim');

		$map = array(
            'is_del'=>0,
        );

		// $map['district_id'] = array('IN',session('district_ids'));

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map['district_id'] = I('district_id','','int');
		}else{
			$map['district_id'] = array('IN',session('district_ids'));	
		}

		if(!empty($month)){
			$month1 = str_pad($month,2,"0",STR_PAD_LEFT);
		}

		if(!empty($year) && !empty($month1)){
			$zon = $year.'-'.$month1;
        	$map['cost_time'] = array('LIKE',"%$zon%");
        }else if(!empty($year)){
        	$map['cost_time'] = array('LIKE',"$year%");
        }else if(!empty($month1)){
        	$map['cost_time'] = array('LIKE',"%$month1%");
        }

		$d = array();

		$count = get_result('comprehensive',$map);
		$type  = get_table_state('comprehensive','type');

		foreach ($count as $k => $v) {
			if(!empty($v['type'])){
				if(count($d) > 0){
					foreach ($d as $m => $n) {
						if($type[$v['type']]['remark'] == $n['name']){
							$d[$m]['value'] = intval($d[$m]['value']) + $v['cost'];
						}
						// return $type[intval($v['type'])]['remark'] == $n['name'];
					}
					if(!in_array($type[$v['type']]['remark'], array_column($d,'name'))){
						$d[] = array('value'=>$v['cost'],'name'=>$type[$v['type']]['remark']);
					}
				}else{
					$d[] = array('value'=>$v['cost'],'name'=>$type[$v['type']]['remark']);
				}
			}else{
				continue;
			}
		}	


		$map1 = array(
            'is_del'=>0,
            'status'=>2
        );

		if(!empty($year) && !empty($month1)){
			$zong = $year.'-'.$month1;
        	$map1['pay_start_time'] = array('LIKE',"%$zong%");
        }else if(!empty($year)){
        	$map1['pay_start_time'] = array('LIKE',"$year%");
        }else if(!empty($month1)){
        	$map1['pay_start_time'] = array('LIKE',"%$month1%");
        }


		// $map1['district_id'] = array('IN',session('district_ids'));

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map1['district_id'] = I('district_id','','int');
		}else{
			$map1['district_id'] = array('IN',session('district_ids'));	
		}
		$pay_count_1 = get_info('payOrder',$map1, ['sum(`money_total`) as c']);

		$d[] = array('value'=>$pay_count_1['c'],'name'=>'物业费');

		return array_values($d);
	}


	/**
	 * 支出类型统计
	 * @author abincc
	 */
	public function pay_total(){

		$year = I('year','','trim');
		$month = I('month','','trim');

		$map = array(
            'is_del'=>0,
        );

		// $map['district_id'] = array('IN',session('district_ids'));

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map['district_id'] = I('district_id','','int');
		}else{
			$map['district_id'] = array('IN',session('district_ids'));	
		}

		if(!empty($month)){
			$month1 = str_pad($month,2,"0",STR_PAD_LEFT);
		}

		if(!empty($year) && !empty($month1)){
			$zon = $year.'-'.$month1;
        	$map['occur_time'] = array('LIKE',"%$zon%");
        }else if(!empty($year)){
        	$map['occur_time'] = array('LIKE',"$year%");
        }else if(!empty($month1)){
        	$map['occur_time'] = array('LIKE',"%$month1%");
        }

		$d = array();

		$count = get_result('reimburse',$map);
		$type  = get_table_state('reimburse','type');

		foreach ($count as $k => $v) {
			if(!empty($v['type'])){
				if(count($d) > 0){
					foreach ($d as $m => $n) {
						if($type[$v['type']]['remark'] == $n['name']){
							$d[$m]['value'] = intval($d[$m]['value']) + $v['cost'];
						}
						// return $type[intval($v['type'])]['remark'] == $n['name'];
					}
					if(!in_array($type[$v['type']]['remark'], array_column($d,'name'))){
						$d[] = array('value'=>$v['cost'],'name'=>$type[$v['type']]['remark']);
					}
				}else{
					$d[] = array('value'=>$v['cost'],'name'=>$type[$v['type']]['remark']);
				}
			}else{
				continue;
			}
		}	


		$map1 = array(
            'is_del'=>0,
        );

		if(!empty($year) && !empty($month1)){
			$zong = $year.'-'.$month1;
        	$map1['salary_time'] = array('LIKE',"%$zong%");
        }else if(!empty($year)){
        	$map1['salary_time'] = array('LIKE',"$year%");
        }else if(!empty($month1)){
        	$map1['salary_time'] = array('LIKE',"%$month1%");
        }


		// $map1['district_id'] = array('IN',session('district_ids'));

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map1['district_id'] = I('district_id','','int');
		}else{
			$map1['district_id'] = array('IN',session('district_ids'));	
		}

		$pay_count_1 = get_info('salary',$map1, ['sum(`real_total`) as c']);

		$d[] = array('value'=>$pay_count_1['c'],'name'=>'薪资额度');

		return array_values($d);
		// return $type;
	}



	/**
	 * 物业缴费-信息
	 * @author abincc
	 */
	public function wuye_pay_order(){

		$year = I('year','','trim');

		$d     = array();
		$a = [0,0,0,0,0,0,0,0,0,0,0,0];
		$b = [0,0,0,0,0,0,0,0,0,0,0,0];  

		$map1 = array(
            'is_del'=>0,
            'status'=>2
        );

        $map2 = array(
            'is_del'=>0,
        );

        if(!empty($year)){
        	$map1['pay_start_time'] = array('LIKE',"$year%");
        	$map2['pay_start_time'] = array('LIKE',"$year%");
        }

		// $map1['district_id'] = array('IN',session('district_ids'));
		// $map2['district_id'] = array('IN',session('district_ids'));

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map1['district_id'] = I('district_id','','int');
		}else{
			$map1['district_id'] = array('IN',session('district_ids'));	
		}

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map2['district_id'] = I('district_id','','int');
		}else{
			$map2['district_id'] = array('IN',session('district_ids'));	
		}	
        
        //已缴费
		$pay_count_1 = get_result('payOrder',$map1);
		//应缴费人数
		$pay_count_2 = get_result('payOrder',$map2);
		
		foreach ($pay_count_1 as $k => $v) {
			if(intval(date("m",strtotime($v['pay_start_time']))) == 1){
				$a[0] = intval($a[0]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 2){
				$a[1] = intval($a[1]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 3){
				$a[2] = intval($a[2]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 4){
				$a[3] = intval($a[3]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 5){
				$a[4] = intval($a[4]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 6){
				$a[5] = intval($a[5]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 7){
				$a[6] = intval($a[6]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 8){
				$a[7] = intval($a[7]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 9){
				$a[8] = intval($a[8]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 10){
				$a[9] = intval($a[9]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 11){
				$a[10] = intval($a[10]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 12){
				$a[11] = intval($a[11]) + 1;
			}
		} 

		foreach ($pay_count_2 as $k => $v) {
			if(intval(date("m",strtotime($v['pay_start_time']))) == 1){
				$b[0] = intval($b[0]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 2){
				$b[1] = intval($b[1]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 3){
				$b[2] = intval($b[2]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 4){
				$b[3] = intval($b[3]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 5){
				$b[4] = intval($b[4]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 6){
				$b[5] = intval($b[5]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 7){
				$b[6] = intval($b[6]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 8){
				$b[7] = intval($b[7]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 9){
				$b[8] = intval($b[8]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 10){
				$b[9] = intval($b[9]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 11){
				$b[10] = intval($b[10]) + 1;
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 12){
				$b[11] = intval($b[11]) + 1;
			}
		} 

		$d[]  = array('value'=>$a,'name'=>"已缴费人数");
		$d[]  = array('value'=>$b,'name'=>"应缴费人数");
		
		return array_values($d);
		// return count($pay_count_2);
		
	}

	/**
	 * 物业收支-信息
	 * @author abincc
	 */
	public function wuye_income_expenses(){

		
		$year = I('year','','trim');

		$d     = array();
		$a = [0,0,0,0,0,0,0,0,0,0,0,0];
		$b = [0,0,0,0,0,0,0,0,0,0,0,0];  

		$map1 = array(
            'is_del'=>0,
            'status'=>2
        );

        $map2 = array(
            'is_del'=>0,
        );


        $map3 = array(
            'is_del'=>0,
        );

        if(!empty($year)){
        	$map1['pay_start_time'] = array('LIKE',"$year%");
        	$map2['salary_time'] = array('LIKE',"$year%");
        	$map3['occur_time'] = array('LIKE',"$year%");
        }

		// $map1['district_id'] = array('IN',session('district_ids'));
		// $map2['district_id'] = array('IN',session('district_ids'));
		// $map3['district_id'] = array('IN',session('district_ids'));

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map1['district_id'] = I('district_id','','int');
		}else{
			$map1['district_id'] = array('IN',session('district_ids'));	
		}

		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map2['district_id'] = I('district_id','','int');
		}else{
			$map2['district_id'] = array('IN',session('district_ids'));	
		}


		if(I('district_id','','int') != null && I('district_id','','int') != ''){
			$map3['district_id'] = I('district_id','','int');
		}else{
			$map4['district_id'] = array('IN',session('district_ids'));	
		}
        
        //收入
		$pay_count_1 = get_result('payOrder',$map1);
		//支出 薪资
		$pay_count_2 = get_result('salary',$map2);
		//支出 报销
		$pay_count_3 = get_result('reimburse',$map3);
		
		
		foreach ($pay_count_1 as $k => $v) {
			if(intval(date("m",strtotime($v['pay_start_time']))) == 1){
				$a[0] = intval($a[0]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 2){
				$a[1] = intval($a[1]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 3){
				$a[2] = intval($a[2]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 4){
				$a[3] = intval($a[3]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 5){
				$a[4] = intval($a[4]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 6){
				$a[5] = intval($a[5]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 7){
				$a[6] = intval($a[6]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 8){
				$a[7] = intval($a[7]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 9){
				$a[8] = intval($a[8]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 10){
				$a[9] = intval($a[9]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 11){
				$a[10] = intval($a[10]) + intval($v['money_total']);
			}else if(intval(date("m",strtotime($v['pay_start_time']))) == 12){
				$a[11] = intval($a[11]) + intval($v['money_total']);
			}
		} 

		foreach ($pay_count_2 as $k => $v) {
			if(intval(date("m",strtotime($v['salary_time']))) == 1){
				$b[0] = intval($b[0]) + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 2){
				$b[1] = intval($b[1]) + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 3){
				$b[2] = intval($b[2]) + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 4){
				$b[3] = intval($b[3]) + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 5){
				$b[4] = intval($b[4])  + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 6){
				$b[5] = intval($b[5])  + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 7){
				$b[6] = intval($b[6]) + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 8){
				$b[7] = intval($b[7])+ intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 9){
				$b[8] = intval($b[8])+ intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 10){
				$b[9] = intval($b[9]) + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 11){
				$b[10] = intval($b[10]) + intval($v['real_total']);
			}else if(intval(date("m",strtotime($v['salary_time']))) == 12){
				$b[11] = intval($b[11]) + intval($v['real_total']);
			}
		} 


		foreach ($pay_count_3 as $k => $v) {
			if(intval(date("m",strtotime($v['occur_time']))) == 1){
				$b[0] = intval($b[0]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 2){
				$b[1] = intval($b[1]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 3){
				$b[2] = intval($b[2]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 4){
				$b[3] = intval($b[3]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 5){
				$b[4] = intval($b[4]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 6){
				$b[5] = intval($b[5]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 7){
				$b[6] = intval($b[6]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 8){
				$b[7] = intval($b[7]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 9){
				$b[8] = intval($b[8]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 10){
				$b[9] = intval($b[9]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 11){
				$b[10] = intval($b[10]) + intval($v['cost']);
			}else if(intval(date("m",strtotime($v['occur_time']))) == 12){
				$b[11] = intval($b[11]) + intval($v['cost']);
			}
		} 

		$d[]  = array('value'=>$a,'name'=>"收入");
		$d[]  = array('value'=>$b,'name'=>"支出");
		
		return array_values($d);
		
	}


	/**
	 * 小事帮忙-保洁维修-投诉建议(已完成-未完成) 数量统计
	 * @author abincc
	 */
	public function property_service_count(){ 
		//小事帮忙(未处理)
		$a = [0,0,0,0,0,0,0,0,0,0,0,0];
		$trifle_count_no = get_result('trifle',['is_del'=>0, 'status'=>1,'district_id'=>array('IN',session('district_ids'))]);
		
		foreach ($trifle_count_no as $k => $v) {
			if(intval(date('m', strtotime($v['add_time']))) == 1 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[0] = intval($a[0]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 2 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[1] = intval($a[1]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 3 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[2] = intval($a[2]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 4 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[3] = intval($a[3]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 5 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[4] = intval($a[4]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 6 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[5] = intval($a[5]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 7 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[6] = intval($a[6]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 8 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[7] = intval($a[7]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 9 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[8] = intval($a[8]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 10 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[9] = intval($a[9]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 11 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[10] = intval($a[10]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 12 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$a[11] = intval($a[11]) + 1;
			}
		} 

		//小事帮忙(已处理)
		$b = [0,0,0,0,0,0,0,0,0,0,0,0];
		$trifle_count_yes = get_result('trifle',['is_del'=>0, 'status'=>2,'district_id'=>array('IN',session('district_ids'))]);
		
		foreach ($trifle_count_yes as $k => $v) {
			if(intval(date('m', strtotime($v['add_time']))) == 1 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[0] = intval($b[0]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 2 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[1] = intval($b[1]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 3 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[2] = intval($b[2]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 4 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[3] = intval($b[3]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 5 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[4] = intval($b[4]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 6 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[5] = intval($b[5]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 7 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[6] = intval($b[6]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 8 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[7] = intval($b[7]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 9 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[8] = intval($b[8]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 10 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[9] = intval($b[9]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 11 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[10] = intval($b[10]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 12 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$b[11] = intval($b[11]) + 1;
			}
		}


		//保洁维修(未处理)  
		$c = [0,0,0,0,0,0,0,0,0,0,0,0];
		$trifle_count_yes = get_result('clean',['is_del'=>0, 'status'=>1,'district_id'=>array('IN',session('district_ids'))]);
		
		foreach ($trifle_count_yes as $k => $v) {
			if(intval(date('m', strtotime($v['add_time']))) == 1 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[0] = intval($c[0]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 2 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[1] = intval($c[1]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 3 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[2] = intval($c[2]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 4 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[3] = intval($c[3]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 5 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[4] = intval($c[4]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 6 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[5] = intval($c[5]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 7 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[6] = intval($c[6]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 8 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[7] = intval($c[7]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 9 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[8] = intval($c[8]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 10 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[9] = intval($c[9]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 11 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[10] = intval($c[10]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 12 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$c[11] = intval($c[11]) + 1;
			}
		}

		//保洁维修(已处理)   
		$d = [0,0,0,0,0,0,0,0,0,0,0,0];
		$trifle_count_yes = get_result('clean',['is_del'=>0, 'status'=>2,'district_id'=>array('IN',session('district_ids'))]);
		
		foreach ($trifle_count_yes as $k => $v) {
			if(intval(date('m', strtotime($v['add_time']))) == 1 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[0] = intval($d[0]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 2 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[1] = intval($d[1]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 3 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[2] = intval($d[2]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 4 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[3] = intval($d[3]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 5 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[4] = intval($d[4]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 6 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[5] = intval($d[5]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 7 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[6] = intval($d[6]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 8 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[7] = intval($d[7]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 9 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[8] = intval($d[8]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 10 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[9] = intval($d[9]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 11 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[10] = intval($d[10]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 12 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$d[11] = intval($d[11]) + 1;
			}
		}

		//投诉与建议()  
		$e = [0,0,0,0,0,0,0,0,0,0,0,0];
		$trifle_count_yes = get_result('complaint',['is_del'=>0,'district_id'=>array('IN',session('district_ids'))]);
		
		foreach ($trifle_count_yes as $k => $v) {
			if(intval(date('m', strtotime($v['add_time']))) == 1 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[0] = intval($e[0]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 2 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[1] = intval($e[1]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 3 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[2] = intval($e[2]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 4 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[3] = intval($e[3]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 5 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[4] = intval($e[4]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 6 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[5] = intval($e[5]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 7 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[6] = intval($e[6]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 8 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[7] = intval($e[7]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 9 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[8] = intval($e[8]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 10 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[9] = intval($e[9]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 11 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[10] = intval($e[10]) + 1;
			}else if(intval(date('m', strtotime($v['add_time']))) == 12 && intval(date('Y', strtotime($v['add_time']))) == intval(date('Y'))){
				$e[11] = intval($e[11]) + 1;
			}
		}


		
		$f     = array();
		$f[]  = array('value'=>$a,'name'=>"小事帮忙未处理");
		$f[]  = array('value'=>$b,'name'=>"小事帮忙已处理");
		$f[]  = array('value'=>$c,'name'=>"保洁维修未处理");
		$f[]  = array('value'=>$d,'name'=>"保洁维修已处理");
		$f[]  = array('value'=>$e,'name'=>"投诉与建议");
		return array_values($f) ;
	}



	/**
	 * 订单统计报表数据
	 * @author llf
	 */
	public function order_count_date(){
		
		$count = array();
		$now   = date('Y-m-d');
		$begin = new \DateTime( date('Y-m-d',strtotime("$now -30 day")) );
		$end   = new \DateTime( $now );
		$end   = $end->modify( '+1 day' ); 

		$interval  = new \DateInterval('P1D');
		$daterange = new \DatePeriod($begin, $interval ,$end);

		$map 		= array('is_del'=>0,'is_hid'=>0,'status'=>array('IN',array(5,10,20,30,40,50,60,70)));
		$add_time 	= array();
		foreach($daterange as $date){
			$date_format 			= $date->format("Y-m-d");
			$count['date_line'][] 	= $date_format;
		    $add_time[] 			= $date_format.'%';
		}
		$map['add_time'] = array('LIKE',$add_time,'or');
		$filed  = array('count(*) as c','status','DATE_FORMAT(`add_time`, "%Y-%m-%d") as date');		
		$list   = get_result('order',$map,'',$filed,'','`date`,`status`');
		$status = array(
				'5'  => '已取消',
				'10' => '待付款',
				'20' => '已付款',
				'30' => '已发货',
				'40' => '已收货',
				'50' => '已评价',
				'60' => '申请退款',
				'70' => '已退款',
			);
		$count['status'] = $status;
		if(!empty($list)){
		    $data = array();
			array_walk($list, function($a) use(&$data){
				$data[$a['date']][$a['status']] = $a['c'];
			});	
			$real = array();
			foreach ($count['date_line'] as $m => $n) {
				$real[$n] = $data[$n];
			}
			foreach ($real as $k => $v) {
				foreach ($status as $key => $value) {
					$count['order_'.$key][] = intval($v[$key]);
				}
			}
		}
		return $count;
	}

}