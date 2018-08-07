<?php
	/**
	 * 获取后台管理菜单
	 * 康利民 2015-06-12
	 */
	function get_menu_list(){
		if(session("menu_result")) return session("menu_result");
		/**如果是系统管理员拥有所有权限*/
		$map = array();
		$member_id = session('member_id');
		/**如果是超级用户 即member_id==1，拥有所有权限*/
		if($member_id !== '1'){
			$info_admin = session('member_info');
			$info_role = session('member_role');
			$roles = array_merge(explode(',',$info_admin['rules']),explode(',',$info_role['menu_ids']));
			$map['id'] = array('in',$roles);
		}
		$map['is_hid'] = '0';
		$menu_result = get_result('menu', $map, 'sort desc,id asc');
		/**把对应角色的功能菜单放入session*/
		session("menu_result",$menu_result);
		return session("menu_result");
	}
	/**
	* 根据ID获取所有子集
	* @param $id 菜单id
	* @return array 该菜单对应的子集菜单数组
	* @author 康利民 2015-06-12
	*/
	function get_child_menu_list($id = 0) {
		$menu_result = get_menu_list ();
		$result = array();
		foreach ( $menu_result as $key => $value ) {
			if ($id == $value ['pid']) {
				$result[] = $value;
			}
		}
		return $result;
	}
	/**
	* 根据当前选中菜单返回头部对应操作按钮
	* @param $menu_id 当前选中菜单ID
	* @param $pid pid 参数，可选
	* @author 康利民 2015-06-16
	*/
	function get_top_btn($menu_id = 0, $pid = '') {
		$child = get_child_menu_list($menu_id);
		$result = '';
		foreach($child as $v) {
			/*只显示启用的操作，如果为 1 或者3  就是ajax-post*/
			if(!$v['is_hid'] && ($v['display_position'] == 1 || $v['display_position'] == 3)) {
				$class = str_replace('ajax-get', 'ajax-post', $v['class']);
				$result .= ' <a href="'.U($v['url'], I('get.')).'" class="btn btn-sm '.$class.'" target-form="ids" title="'.$v["title"].'">'.$v["title"].'</a> ';
			}
		}
		return $result;
	}
	/**
	* 根据当前选中菜单返回列表对应操作按钮
	* 自动隐藏当前无需进行的操作（比如已禁用的数据无需显示禁用按钮）
	*	@param	int $menu_id 	当前选中菜单ID
	*	@param	array $row 	当前数据数组
	*	@param	array $remove 	要移除的配置数组，格式参照$default
	* @author	秦晓武 2016-08-10
	*/
	function get_list_btn($menu_id, $row = array(), $remove = array(),$extar=array()) {
		/*默认移除配置*/
		$default = array(
			'is_hid' => array(
				'0'=>U("enable"),
				'1'=>U("disable"),
			),
			'recommend' => array(
				'1'=>U("recommend"),
				'0'=>U("no_recommend"),
			),
		);
		/*合并*/
		$set = array_merge($default,$remove);
		$url = array();
		/*生成要移除的URL*/
		foreach($set as $key => $value){
			if(isset($row[$key])){
				if(is_array($value[$row[$key]])){
					$url = array_merge($url,$value[$row[$key]]);
				}
				else{
					$url[] = $value[$row[$key]];
				}
			}
		}
		/*遍历对应菜单id的子集菜单，获取所有操作*/
		$child = get_child_menu_list($menu_id);
		$result = '';
		foreach($child as $v) {
			/*只显示列表或全局操作*/
			if(!in_array($v['display_position'], array(2,3)) || $v['is_hid']){
				continue;
			}
			$cur_url = U($v['url']);
			/*屏蔽移除操作*/
			if(in_array($cur_url,$url)){
				continue;
			}
			$class = str_replace('ajax-post', 'ajax-get', $v['class']);
			$array = array_merge(I('get.'), array('ids'=> $row['id']));
			if(!empty($extar) && !empty($array)){
				$array = array_merge($array,$extar);
			}
			$result .= ' <a href="'.U($v['url'], $array).'" class="btn btn-xs '.$class.'" title="'.$v["title"].'">'.$v['title'].'</a> ';
			
		}
		echo $result;
	}
	
	/**
	 * 添加日志
	 * @param string $id 操作记录ID
	 * @time 2016-07-08
	 * @author 秦晓武
	 */
	function action_log($id = 0){
		/*当前操作*/
		$menu_url = MODULE_NAME . '/' . CONTROLLER_NAME . '/' . ACTION_NAME;
		/*所有菜单*/
		$menu_result = get_no_del('menu');
		$action = array('id'=>'','title'=>'','level'=>'');
		$title = array('url'=>'','title'=>'');
		/*匹配信息*/
		foreach($menu_result as $k => $row){
			if($menu_url == $row['url']){
				$action = $row;
			}
		}
		/*获取父级*/
		foreach($menu_result as $k => $row){
			if(isset($action['pid']) && $action['pid'] == $row['id']){
				$title = $row;
			}
		}
		$data['url'] = $title['url'];
		$data['username'] = session('username');
		$data['menu_id'] = $action['id'];
		$data['title'] = $title['title'] .' > '. $action['title'];
		$data['title'] = array_to_crumbs($menu_result,$action['id']);
		$data['action'] = '浏览';
		if($action['level'] == '3'){
			$data['action'] = $action['title'];
			if($id){
				$data['action'] .=  ' | ' . $id;
			}
		}
		$data['ip'] = get_client_ip();
		$data['admin_id'] = session('member_id');
		$data['add_time'] = date('Y-m-d H:i:s');
		if(!$data['url']){
			switch($menu_url){
				case 'Backend/Base/Public/login':
					$data['url'] = 'Backend/Base/Public/login';
					$data['title'] = '';
					$data['menu_id'] = 0;
					$data['action'] = '登录';
					break;
				default:
					$data['url'] = $menu_url;
					;
			}
		}
		M('action_log')->add($data);
	}

	/**
	* PHP 中判断一个字符串是否是合法的日期模式
	*/

	function checkDateTime1($data){
		if(date('Y-m-d H:i:s', strtotime($data)) == $data){
			return true;
		}else{
			return false;
		}
	}

	function checkDateTime2($data){
		if(date('Y-m-d', strtotime($data)) == $data){
			return true;
		}else{
			return false;
		}
	}


	/**
     * 批量生成订单  -- 01号
     */
    function batchOrder($ids){
    	$i = 0;
        try{
           
            //开启事务进行循环插入操作
            $SKU = M("payOrder");
            $M = M();
            $M->startTrans();
            
            foreach ($ids as $k => $v) {
                $info = M('owner')->where('id='.$v)->select();
   
		        $order = array(
		           'status'     => 1,
		           'is_del' => 0,
		           'uid'     =>$info['0']['id']
		        );

		        $order_list  = M('payOrder')->where($order)->field('id')->select();
		        if(count($order_list) > 0){
		            continue;
		        }else{

		            $data = array(
		            'uid'         => $info['0']['id'],
		            'type'         => 0,
		            'order_no'     => build_order_no(),
		            'detail_title' => '物业费',
		            'money_total'  =>    intval($info['0']['property_money']) + intval($info['0']['waste_fee']) + intval($info['0']['wall_waste_fee']) + intval($info['0']['other_fee']),
		            'district_id'     => $info['0']['district_id'],
		            'mobile'     => $info['0']['mobile'],
		            'username'     => $info['0']['username'],
		            'room_no'     => $info['0']['room_no'],
		            'pay_start_time'     => $info['0']['pay_start_time'],
		            'pay_end_time'     => $info['0']['pay_end_time'],
		            'add_time'     => date('Y-m-d H:i:s'),
		            );

		            $result = update_data('payOrder', [], [], $data);
		            if(is_numeric($result)){
	                     $i = $i + 1;
	                }
		        }
            }
        }catch (Exception $e){
            $M->rollback();
            // $this->error($e->getMessage());
            return '订单生成失败';
        }
        $M->commit();
         return $i.'条订单生成成功';
        
    }





	
