<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 商品收藏
	 */
class ProductCollectionController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table 	  ='Product_collection';
	protected $table_view ='ProductCollectionView';

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

		$field = array('c.id as collect_id','product_id','title','price','image','description','add_time');
		$list  = $this->page(D($this->table_view),$map,'add_time desc',$field);
		if(!empty($list)){
			array_walk($list, function(&$a){
				$a['image'] = file_url($a['image']);
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

		if(!IS_POST)	$this->api_error('请求方式错误');
		
		$data = array();
		$data['member_id']		= $this->member_id;
		$data['product_id'] 	= I('product_id',0,'int');

		if(!$data['product_id'])	$this->api_error('product_id 参数错误');

		$Collect = D('ProductCollection');
		/* 验证商品是否存在*/
		$condition = array(
				'id'		=> $data['product_id'],
				'is_del'	=> 0,
				'is_hid'	=> 0,
				'is_sale'	=> 1,
			);
		$product_id = M('Product')->where($condition)->getField('id');
		if(empty($product_id)){
			$this->api_error('商品信息不存在');
		}

		/* 验证是否已收藏*/
		$map = array(
				'product_id'=> $data['product_id'],
				'member_id'	=> $this->member_id,
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$has = $Collect->where($map)->find();
		if(!empty($has)){
			$this->api_error('已经收藏过了');
		}

		$result = update_data($Collect,[],[],$data);
		if(!is_numeric($result)){
			$this->api_error($result);
		}

		$this->api_success('收藏成功');
	}

	/**
	 * 删除
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function cancel(){

		if(empty(I('collect_id')) && empty(I('product_id')))	$this->api_error('参数错误');

		if(I('collect_id')){
			$res = D('ProductCollection')->del($this->member_id,I('collect_id'));
		}
		if(I('product_id')){
			$condition = array(
					'member_id'	=> $this->member_id,
					'product_id'=> intval(I('product_id')),
				);
			$res = D('ProductCollection')->where($condition)->setField('is_del',1);
		}
		
		if(!$res){
			$this->api_error('操作失败');
		}
		$this->api_success('操作成功');
	}

}
