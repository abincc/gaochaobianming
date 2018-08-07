<?php
namespace User\Controller\Address;
use User\Controller\Base\BaseController;
/**
 * 收货地址管理
 */
class AddressController extends BaseController{
	private $table = 'member_address';
	private $cache = 'member_address_cache';
	/**
	 * 用户中心地址信息列表
	 * 需求分析
	 * 		列出当前用户的所有地址信息
	 * 流程分析
	 * 		1、获取用户信息
	 * 		2、查询地理信息
	 * 		3、根据用户信息查找地址表中对应的地址信息
	 * 		4、循环地址信息，取得地址中的省、市、区，并拼接到详细地址上
	 * 		5、处理头部导航栏
	 * 		6、将查询到的信息传到视图中
	 */
	public function index() {
		$map['member_id']=session('member_id');
		/*根据省市区ID取出省市区名称*/
		$address['list'] = get_result($this->table,$map,'is_default desc,id desc');
		/*查询地理信息*/
		$area = get_no_hid('area');
		foreach($address['list'] as $k1 => $v1){
			foreach($area as $k2 => $v2){
				if($v1['province']== $v2['id']){
					$address['list'][$k1]['province']=$v2['title'];
				}
				if( $v1['city']==$v2['id']){
					$address['list'][$k1]['city']=$v2['title'];
				}
				if($v1['area']== $v2['id']){
					$address['list'][$k1]['area']=$v2['title'];
				}
			}
		}
		$data['address']=$address;
		$this->assign($data);
		$this->display();
	}
	
	/*
	 * 用户中心地址信息删除
	 * 需求分析
	 * 		删除当前选中的地址信息
	 * 流程分析
	 * 		1、获取地址信息
	 * 		2、获取动态验证规则和过滤条件
	 * 		3、根据用户信息将地址表中对应数据的status字段做修改
	 * 		4、判断修改的状态，返回提示信息
	 */
	public function del(){
		/*1、获取用户信息*/
		$member_id = session('home_member_id');
		$id = I('get.address_id');
		if ($id){
			/*2、获取动态验证规则和过滤条件*/
			$rules = array(
					array('address_id','require','地址ID不能为空！'),
			);
			$map = array(
					'id' => $id,
					//'member_id' => $member_id,
			);
			$_POST['id'] = $id;
			$_POST['is_del'] = 1;
			/*3、根据用户信息将地址表中对应数据的status字段做修改*/
			$result = update_data($this->table,$rules,$map);
			/*4、判断修改的状态，返回提示信息*/
			if(is_numeric($result)){
				$this->success('删除成功！',U('index'));
			}else {
				$this->error('删除失败！');
			}
		}else {
			$this->error('缺少参数！');
		}
	}
	
	/*
	 * 用户中心设置默认地址
	 * 需求分析
	 * 		设置当前地址为默认地址
	 * 流程分析
	 * 		1、获取地址信息
	 * 		2、根据用户信息将地址表中对应数据的status字段做修改
	 * 		3、判断修改的状态，返回提示信息
	 */
	public function set_default(){
		/*1、获取用户信息*/
		$member_id = session('home_member_id');
		$id = I('get.id');
		$data1['is_default']=1;
		$data2['is_default']=0;
		if ($id){
			/*2、根据用户信息将地址表中对应数据的status字段做修改*/
			$result1=Database::update_data($this->table,'',array('id'=>$id),$data1);
			$result2=Database::update_data($this->table,'',array('id'=>array('neq',$id)),$data2);
			/*3、判断修改的状态，返回提示信息*/
			if(!$result1&&!$result2){

				$this->success('设置成功！');
				
			}else {
				$this->error('设置失败！');
			}
		}else {
			$this->error('缺少参数！');
		}
	}
	
	/**
	 * 添加
	 */
	public function add(){
		if (IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}
	/**
	 * 修改
	 */
	public function edit(){
		if (IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}
	
	/**
	 * 用户中心地址信息显示
	 * 需求分析
	 * 		显示表单
	 * 流程分析
	 * 		1、获取地址信息
	 * 		2、获取地理信息
	 */
	private function operate(){
		/*取出收货地址信息*/
		$info = get_info($this->table, array('id' => intval(I('address_id'))));
		/*查询地理信息*/
		$area = get_no_hid('area');
		$data['address']=$info;
		$data['area'] = $area;
		$this->assign($data);
		$this->display('operate');
	}
	/**
	 * 用户中心地址信息更新
	 * 需求分析
	 * 		设置当前地址为默认地址
	 * 流程分析
	 * 		1、获取地址信息
	 * 		2、根据用户信息将地址表中对应数据的status字段做修改
	 * 		3、判断修改的状态，返回提示信息
	 */
	private function update(){
		$member_id = session('member_id');
		/*验证用户输入信息*/
		$rules = array(
			array('full_name','require','收件人不能为空！'),
			array("mobile",MOBILE,'手机号码格式不正确',1,'regex'),
			array('zipcode',ZIPCODE,'邮政编码不能为空！',1,'regex'),
			array('address','require','详细地址不能为空！'),
			array('province','require','请选择省份！'),
			array('city','require','请选择城市！'),
			array('area','require','请选择区！'),
		);
		$data = array(
			'member_id'=>$member_id,
			'full_name'=>I('full_name'),
			'mobile'=>I('mobile'),
			'zipcode'=>I('zipcode'),
			'address'=>I('address'),
			'province'=>I('province'),
			'city'=>I('city'),
			'area'=>I('area'),
			'title'=>I('title'),
		);
		if(I('id')){
			$data['id']=I('id');
		}
		$result = update_data($this->table,$rules,$map,$data);
		if(!is_numeric($result)){
			$this->error($result);
		}
		$this->success('修改成功！');
		
		
						if($posts['id']){
							$id=$posts['id'];
						}else{
							$id=$result;
						}
						$data1['is_default']=1;
						$data2['is_default']=0;
						if ($posts['is_default']==1){
							$res_1=update_data($this->table,'',array('id'=>$id),$data1);
							$res_2=update_data($this->table,'',array('id'=>array('neq',$id)),$data2);
							if(is_numeric($res_1)&&is_numeric($res_2)){
								M()->commit();
							}else{
								M()->rollback();
							}
						}else{
							$res_3=update_data($this->table,'',array('id'=>$id),$data2);
							if(is_numeric($res_3)){
								M()->commit();
							}else{
								M()->rollback();	
							}
						}
						$this->success('修改成功！');
						M()->commit();

	}
}
