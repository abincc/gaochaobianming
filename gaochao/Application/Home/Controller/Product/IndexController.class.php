<?php
namespace Home\Controller\Product;
use Home\Controller\Base\AdminController;
class IndexController extends AdminController {
	protected $table="product";
	/**
	 * 商品列表（根据1级分类下所有分类的搜索），详细列表
	 * @流程：
	 * 1、接收分类参数或搜索参数
	 * 2、列举定价属性，品牌等分类参数
	 * 3、列出查询结果集
	 * @author 邹义来
	 * @time 2016-6-23
	 */
	public function index() {
		$this->display();
	}
    /**
     * 商品详情
     * @time 2016-6-23
     * @author 邹义来
     */
    public function detail() {
    	if(IS_POST){
    		$this->error("没有评论");
    	}
    	$this->display();
    }
    
	/**
	 * 产品评论详情
	 * @return [type] [description]
	 * @time   2016.7.25
	 */
    public function _get_product_comment($_map=array()){
    	/*获取产品id*/
    	$product_id = intval(I('get.id'));
    	if(empty($product_id)){
    		$this->error('访问错误');
    	}
	    $map['product_id']= $product_id;
	    $map['pid']=0;
	    $map['is_hid']=0;
	    $map['is_del']=0;
	    if($_map){
	    	$map=array_merge($map,$_map);
	    }
    	$res=$this->page(D('CommentListView'),$map,'addtime desc',true,'5');
    	return $this->comment_html($res);  
    }

	protected function comment_list(){
		$this->display();
	}
	/**
	 * 评论统计
	 */
	public function count_comment($map=array()){
		$_map['is_hid']=0;
		$_map['is_del']=0;
		$_map['pid']=0;
		$_map['product_id']=I('id','','int');
		return  M('comment')->where($_map)->where($map)->count();
	}
	/**
	 * 商家回复列表
	 * @param  [type] $_id [description]
	 * @return [type]      [description]
	 */
	public function get_reply($_id){
		$map['pid']=array('in',$_id);
		$res=M('comment')->where($map)->select();
		return $res;
	}
	/**
	* 获取评论图片
	* @time 2016-6-23
	* @author 邹义来
	*/
	public function get_image($_id){
		$map['comment_id']=array('in',$_id);
		return get_result('comment_image',$map);
	}


    /**
     * 店铺推荐商品
     * @time 2016-6-23
	 * @author 邹义来
     * @return [type] [description]
     */
    public function recommend_product($shop_id){
    	$list=$this->sort_product($shop_id,"product.sales desc",5);
        $data['list']=$list;
    	$this->assign($data);
    	$tpl=$this->fetch('recommend');
    	return $tpl;
    }

    /**
     * 商品详情图片html
     * @return [type] [description]
     */
    public function image_html($data){
        $data['photo_list']=$data;
    	$this->assign($data);
    	$tpl=$this->fetch('image');
    	return $tpl;
    }

    /**
     * 购买商品声明html
     * @return [type] [description]
     */
    public function statement_html(){
    	$tpl=$this->fetch('statement');
    	return $tpl;
    }
}