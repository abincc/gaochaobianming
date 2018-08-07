<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;




/*  公共模块商品相关接口*/
class ProductController extends ApiController{
	
	/**
	 * 表名
	 * @var string
	 */
	protected $table 			= 'Product';
	protected $comment 			= 'Order_comment';

	/**
	 * APP 商品列表接口
	 * @time 2017-08-22
	 **/
	public function index(){
		$map 	= $this->_search();
		$order	= array();
		$order_type = array(
				1=>'desc',
				2=>'asc',
			);
		if(intval(I('order_sales'))){
			$order[] = ' sales '. $order_type[intval(I('order_sales'))];
		}
		if(intval(I('order_price'))){
			$order[]= ' price '. $order_type[intval(I('order_price'))];
		}	
		$order = implode(',', $order);
		if(empty($order)) 	$order 	= 'sort desc,sales desc,collect desc';

		$field 	= array('id as product_id','title','image','price','description');
		$list	= $this->page($this->table, $map, $order ,$field , 12);

		if(!empty($list)){
			array_walk($list, function(&$a){
				$a['image'] = file_url($a['image']);
			});
		}
		$this->api_success('ok',(array)$list);
	}

    /**
     * 获取推荐商品列表
     */
    public function  getRecommendList(){
        $map = array(
            'is_del'	=>0,
            'is_hid'	=>0,
            'is_sale'	=>1,
            'recommend'=>1,
        );
        $product = M($this->table);
        $field 	= array('id as product_id','title','image','price','description');
        $list = $product->where($map)->field($field)->limit(3)->select();
        $this->api_success('ok',(array)$list);
    }

	/**
	 * 商品搜索参数
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function _search(){

		$map = array(
				'is_del'	=>0,
				'is_hid'	=>0,
				'is_sale'	=>1,
			);
		$keyword 	 = trim(I('keyword'));
		$category_id = I('categroy_id',0,'int');

		if(!empty(I('keyword'))){
			$map['title']	= array('LIKE',"%$keyword%");
		}
		if(!empty($category_id)){
			$map['sub_category_id'] = $category_id;
		}
		// bug($map);
		return $map;
	}


	/**
	 * 商品详情
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function info(){

		$product_id = I('product_id',0,'int');
		$map = array(
				'id'		=>$product_id,
				'is_del'	=>0,
				'is_hid'	=>0,
				'is_sale'	=>1,
			);
		$P 		= D('Product');
		$field 	= array('id as product_id','title','price','description','image as cover','content','spec','spec_value','inventory');
		$info 	= $P->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('商品信息不存在');
		}
		$info['cover'] = file_url($info['cover']);

		/* 处理属性格式*/
		if(!empty($info['spec']))	{
			$info['spec'] 		= unserialize($info['spec']);
			$info['spec_value'] = unserialize($info['spec_value']);
			if(!empty($info['spec'])){
				$temp = array();
				$i = 1;
				foreach ($info['spec'] as $k => $v) {
					$temp[] = array(
							'index'			=> $i,
							'spec_id'		=> $k,
							'spec_title'	=> $v,
						);
					$i++;
				}
				$info['spec'] = $temp;
			}
			if(!empty($info['spec_value'])){
				$temp = array();
				foreach ($info['spec_value'] as $kk => $vv) {
					foreach ($vv as $key => $val) {
						$temp[$kk][] = array(
							'spec_value_id'		=> $key,
							'spec_value_title'	=> $val,
						);
					}
				}
				$info['spec_value'] = $temp;
			}
			// bug($info['spec_value']);
			if(!empty($info['spec'])){
				foreach ($info['spec'] as $a => $b) {
					$info['spec'][$a]['spec_value_list'] = array_values($info['spec_value'][$b['spec_id']]);
				}
			}
		}
		/* 处理sku数据格式*/
		$info['sku'] 			= $P->sku($info['product_id']);
		if(!empty($info['sku'])){
			$temp = array();
			foreach ($info['sku'] as $keys => $vals) {
				$temp[] = array(
					'sku_id'		 => $vals['sku_id'],
					'spec_value_ids' => implode('-', array_keys($vals['spec_value'])),
					'price' 		 => $vals['price'],
					);
			}
			$info['sku'] = $temp;
		}

		unset($info['spec_value']);
		if(empty($info['spec']))	$info['spec'] = array();
		if(empty($info['sku']))		$info['sku']  = array();

		$posts = I();
		if(!empty($posts['token'])){
			$Member 		 = D('Member');
			$this->member_id = $Member->check_token($posts);
		}

		/* 是否收藏*/
		$info['is_collect'] 	= $P->is_collect($this->member_id,$info['product_id']);
		/* 商品相册*/
		$info['images']			= (array)$P->get_image($info['product_id']);
		/* 商品评论个数*/
		$info['comment_number'] = $P->comment_number($info['product_id']);

		$info['content']		= U('Home/Index/Index/product_detail_content',array('product_id'=>$info['product_id']),true,true);

		$this->api_success('ok',$info);

	}


	/**
	 * 商品分类
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function categroy(){

		$categroy_list = get_no_hid('product_category');

		if(empty($categroy_list)){
			$this->api_error('分类数据不存在');
		}
		// bug($categroy_list);
		foreach ($categroy_list as $k => $v) {
			$categroy_list[$k]['categroy_id'] = $v['id'];
			unset($categroy_list[$k]['id']);
			unset($categroy_list[$k]['type_id']);
			unset($categroy_list[$k]['level']);
			unset($categroy_list[$k]['description']);
			unset($categroy_list[$k]['sort']);
			unset($categroy_list[$k]['is_hid']);
			unset($categroy_list[$k]['is_del']);
			unset($categroy_list[$k]['add_time']);
			unset($categroy_list[$k]['update_time']);
			$categroy_list[$k]['icon'] = file_url($v['icon']);
		}
		$categroy_list = list_to_tree($categroy_list,'categroy_id','pid','child');

		$this->api_success('ok',$categroy_list);
	}

	/**
	 * 商品评价信息
	 * @time 2017-08-22
	 * @author llf
	 **/
	public function comment(){

		$product_id = I('product_id',0,'int');
		$map 	= array(
				'product_id'	=>$product_id,
				'is_del'		=>0,
				'is_hid'		=>0,
			);
		$field 	= array('v.id as comment_id','content','star','member_id','reply','add_time','username as nickname');
		
		$_GET['p'] = I('p',0,'int');
		$list   = $this->page(D('OrderCommentView'),$map,'add_time desc',$field);

		if(!empty($list)){
			$comment_ids    = array_column($list,'comment_id');
			$condition    	= array(
								'pid' => array('IN',$comment_ids),
							);
			$field  = array('id as image_id','pid as comment_id','image');
			$images = M('order_commont_img')->where($condition)->field($field)->select();
			if(empty($image)){
				$temp = array();
				foreach ($images as $k => $v) {
					$temp[$v['comment_id']][] = file_url($v['image']);
					// array(
					// 								'image_id' => $v['image_id'],
					// 								'image'	   => file_url($v['image']),
					// 							);
				}
				foreach ($list as $key => $val) {
					// $list[$key]['nickname']= 'test';
					$list[$key]['headimg']= get_avatar($val['member_id']);
					$list[$key]['images'] = (array)$temp[$val['comment_id']];
					unset($list[$key]['member_id']);
				}

			}

		}

		$this->api_success('ok',(array)$list);
	}

	/**
	 * 商品分享信息
	 * @time 2017-09-15
	 * @author llf
	 **/
	public function share(){

		$product_id 	= I('product_id',0,'int');
		$map			= array(
							'is_del' => 0,
							'is_hid' => 0,
						);
		$product_info 	= M($this->table)->where('id='.$product_id)->find();
		if(empty($product_info)){
			$this->api_error('商品信息不存在');
		}
		if(!$product_info['is_sale']){
			$this->api_error('商品已下架');
		}
		$share = array(
				'product_id'=> $product_id,
				'title'		=> $product_info['title'],
				'content'	=> $product_info['description'],
				'image'		=> file_url($product_info['image']),
				'url'		=> U('Home/Index/Index/product_share_info',array('product_id'=>$product_id),true,true),
			);
		$this->api_success('ok',(array)$share);
	}

}

