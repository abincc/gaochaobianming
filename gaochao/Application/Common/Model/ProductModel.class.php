<?php
namespace Common\Model;
use Think\Model;

/**
 * 产品模型模型
 */
class ProductModel extends Model {

    protected $tableName  = 'product';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
	);
	
    /**
     * 获取详细信息
     * @param  milit   	 $map 	查询条件
     * @param  boolean 	 $field 查询字段
     * @return array     分类信息
     * @author llf
     */
    public function info($map, $field = true){
        return $this->field($field)->where($map)->find();
    }

    /**
     * 获取商品规格信息
     * @param  int      个数
     * @return bool                 
     */
    public function sku($product_id){

        $field = array('id as sku_id','spec_value','price');
        $list  = M('product_sku')->where('product_id='.intval($product_id))->field($field)->select();
        if(!empty($list)){
            array_walk($list, function(&$a){
                $a['spec_value'] = unserialize($a['spec_value']);
            });
        }
        return $list;
    }


    /**
     * 获取商品评论数
     * @param  int      个数
     * @return bool                 
     */
    public function comment_number($product_id){
        return M('order_comment')->where('product_id='.intval($product_id))->count('id');
    }


    /**
     * 推荐商品
     * @param  int      个数
     * @return bool                 
     */
    public function recommend($limit){

        $map = array(
                'recommend' => 1,
                'is_sale'   => 1,
                'is_del'    => 0,
                'is_hid'    => 0
            );

        $field = array('id as product_id','title','image','price','description');

        $list = $this->where($map)->order('sort desc,sales desc,collect desc')->field($field)->limit($limit)->select();

        if(!empty($list)){
            array_walk($list, function(&$a){
                $a['image'] = file_url($a['image']);
            });
        }

        return $list;
    }
 

     /**
     * 获取商品相册图片
     * @param  int       product_id    商品ID
     * @return array     商品图片
     * @author llf
     */
    public function get_image($product_id){
        
        $field = array('id as image_id','image');
        $list  = M('product_image')->where('product_id='.intval($product_id))->order('add_time desc')->field($field)->select();
        if(!empty($list)){
            array_walk($list, function(&$a){
                $a['image'] = file_url($a['image']);
            });
        }
        return $list;
    }

    /**
     * 是否收藏商品
     * @param  int       product_id    商品ID
     * @return boll
     * @author llf
     */
    public function is_collect($member_id,$product_id){
        $map = array(
                'member_id'     => intval($member_id),
                'product_id'    => intval($product_id),
                'is_del'        => 0,
                'is_hid'        => 0,
            );
        if(!is_numeric(M('product_collection')->where($map)->getField('id'))){
            return 0;
        }else{
            return 1;
        }
         
    }
   

}
