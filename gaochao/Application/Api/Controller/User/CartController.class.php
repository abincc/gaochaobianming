<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 地址
	 */
class CartController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'Cart';

	/**
	 * 个人告购物车列表
	 * @time 2017-08-23
	 * @author llf
	 **/
	public function index() {
		
		$map   = array(
				'member_id'		=> $this->member_id,
				'product_num'	=> array('GT',0),
			);
		$field = array('id as cart_id','sku_id','product_id','product_title','product_price','product_spec_value','product_num','product_image');
		$list  = M($this->table)->where($map)->order('add_time desc')->field($field)->select();

		if(!empty($list)){
			array_walk($list, function(&$a) {
				$a['product_image'] = file_url($a['product_image']);
				$a['product_spec_value'] = implode(' ', array_values(unserialize($a['product_spec_value'])));
			});	
		}

		$this->api_success('ok',(array)$list);
	}

	/**
	 * 添加
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function add(){

		if(!IS_POST)	$this->api_error('请求方式错误');
		$sku_id = I('sku_id',0,'int');
		$num    = I('num',0,'int');
		if(!$sku_id)	$this->api_error('sku_id参数错误');
		if(0 >= $num)	$this->api_error('数量参数必须大于0');

		$Cart = D('Cart');
		$res  = $Cart->add_cart($this->member_id,$sku_id,$num);
		if(!$res){
			$this->api_error($Cart->getError());
		}
		$this->api_success('添加成功');
	}

	/**
	 * 删除地址
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function del(){
		
		if(!IS_POST)	$this->api_error('请求方式错误');
		$res = D('Cart')->del($this->member_id,I('cart_ids'));
		if(!$res){
			$this->api_error('操作失败');
		}
		$this->api_success('操作成功');
	}

	/**
	 * 设置数量
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function set_num(){

		if(!IS_POST)	$this->api_error('请求方式错误');
		$product_num = I('product_num',0,'int');
		$cart_id 	 = I('cart_id',0,'int');

		if(0 >= $product_num)		$this->api_error('商品数量必须大于0');
		if(!$cart_id)				$this->api_error('cart_id参数必须');

		$res = D('Cart')->set_num($this->member_id,$cart_id,$product_num);
		if(!$res){
			$this->api_error('操作失败');
		}
		$this->api_success('操作成功');
	}

}
