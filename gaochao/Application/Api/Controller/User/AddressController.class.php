<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 地址
	 */
class AddressController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table='Member_address';

	/**
	 * 个人地址列表
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function index() {
		
		$map = array(
				'member_id'	=> $this->member_id,
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$field = array('id as address_id','name','mobile','province','city','area','address_detail','is_default');
		$list  = M($this->table)->where($map)->order('add_time desc')->field($field)->select();

		if(!empty($list)){
			$area_cahce = get_no_hid('area');
			array_walk($list, function(&$a) use($area_cahce){
				$a['province_title'] 	= $area_cahce[$a['province']]['title'];
				$a['city_title'] 		= $area_cahce[$a['city']]['title'];
				$a['area_title'] 		= $area_cahce[$a['area']]['title'];
			});	
		}

		$this->api_success('ok',(array)$list);
	}

	/**
	 * 添加地址
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function add(){
		$this->update();
	}


	/**
	 * 添加地址
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function edit(){
		$this->update();
	}


	/**
	 * 删除地址
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function del(){

		$res = D('Address')->del($this->member_id,I('address_id',0,'int'));
		if(!$res){
			$this->api_error('操作失败');
		}
		$this->api_success('操作成功');
	}

	/**
	 * 设置为默认地址
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function set_default(){
		
		if(!IS_POST)	$this->api_error('请求方式错误');
		$map = array(
					'id'		=> I('address_id',0,'int'),
					'member_id'	=> $this->member_id,
					'is_del'	=> 0,
					'is_hid'	=> 0,
				);
		$Address = D('Address');
		$has = $Address->where($map)->field(array('id'))->find();
		if(empty($has)){
			$this->api_error('地址信息不存在');
		}

		if(!$Address->set_default($this->member_id,$has['id'])){
			$this->api_error('地址设置失败');
		}else{
			$this->api_success('操作成功');
		}
	}

    /**
     * 获取默认地址
     */
	public  function  getDefaultAddress(){
        $map = array(
            'member_id'	=> $this->member_id,
            'is_del'	=> 0,
            'is_hid'	=> 0,
            'is_default' =>1,
        );
        $Address = D('Address');
        $list = $Address->where($map)->find();
        $this->api_success('ok',(array)$list);
    }

	/**
	 * 更新
	 * @time 2017-08-22
	 * @author llf
	 **/
	protected function update(){
		
		if(!IS_POST)	$this->api_error('请求方式错误');
		
		$data = array();
		$data['member_id']		= $this->member_id;
		$data['is_default'] 	= I('is_default',0,'int');
		$data['province'] 		= I('province','','int');
		$data['city'] 			= I('city','','int');
		$data['area'] 			= I('area',0,'int');
		$data['address_detail'] = I('address_detail','','trim');

		$data['name']			= I('name','','trim');
		$data['mobile']			= I('mobile','','trim');

		$Address = D('Address');

		/* 验证地址是否存在*/
		if(I('address_id')){
			$map = array(
					'id'		=> I('address_id',0,'int'),
					'member_id'	=> $this->member_id,
					'is_del'	=> 0,
					'is_hid'	=> 0,
				);
			$has = $Address->where($map)->find();
			if(!empty($has)){
				$data['id'] = I('address_id',0,'int');
			}
		}
		$result = update_data($Address,[],[],$data);

		if(!is_numeric($result)){
			$this->api_error($result);
		}
		if($data['is_default'])		$Address->set_default($this->member_id,$result);

		$this->api_success('操作成功');
	}

}
