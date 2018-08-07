<?php
namespace Common\Model;
use Think\Model;

/**
 * 购车模型
 */
class CartModel extends Model {

    protected $tableName  = 'cart';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(

        array('product_title', 'require', '商品标题信息必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('product_title', '1,120', '商品标题长度为1~120个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),

        array('product_price', 'require', '商品价格必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('product_price', DECIMAL, '商品价格格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),

        // array('product_spec_value', '1,250', '商品属性值长度为1~250个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('product_num', 'is_numeric', '商品数量参数格式', self::EXISTS_VALIDATE , 'function', self::MODEL_BOTH),

        array('product_image', '1,250', '路径长度为1~250个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),

        array('sku_id', 'require', 'sku 参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('product_id', 'require', '商品参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('member_id', 'require', '用户参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
      array('add_time', 'now_date_time', self::MODEL_INSERT,'function'),
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
   * 加入购物车
   * @param  member_id    用户ID
   * @param  product_id   商品ID
   * @param  sku_id       商品库存单位ID
   * @param  num          数量
   * @return bool                 
   */
  public function add_cart($member_id,$sku_id,$num){

      $member_id  = intval($member_id);
      $sku_id     = intval($sku_id);
      $num        = abs(intval($num));
      //验证sku_id 是否存在,对应商品是否存在
      $field      = array('product_id','spec_value','image','price','inventory');
      $sku_info   = M('product_sku')->where('id='.$sku_id)->field($field)->find();
      if(empty($sku_info)){
          $this->error = '库存信息不存在';
          return false;
      }
      //验证SKU库存是否足够
      if($num > intval($sku_info['inventory'])){
          $this->error = '商品库存不足';
          return false;
      }
      $condition = array(
          'id'      => intval($sku_info['product_id']),
          'is_del'  => 0,
          'is_hid'  => 0,
        );
      $f = array('id','title','is_sale');
      $product_info = M('Product')->where($condition)->field()->find();
      if(empty($product_info)){
          $this->error = '商品信息不存在';
          return false;
      }
      if(!$product_info['is_sale']){
          $this->error = '商品处于下架状态';
          return false;
      }
      //验证购车是否加过该商品
      $map = array(
          'member_id' => $member_id,
          'sku_id'    => $sku_id
        );
      $field = array('id','product_num');
      $cart_sku_exist = $this->where($map)->find();
      if(!empty($cart_sku_exist)){
          $result = $this->where('id='.$cart_sku_exist['id'])->setInc('product_num',$num);
      }else{
          $data = array(
              'member_id'           => $member_id,
              'sku_id'              => $sku_id,
              'product_id'          => $sku_info['product_id'],
              'product_title'       => $product_info['title'],
              'product_price'       => $sku_info['price'],
              'product_spec_value'  => $sku_info['spec_value'],
              'product_num'         => $num,
              'product_image'       => $sku_info['image'],
            );
          if(!$this->create($data)){
              return false;
          }
          $result = $this->add();
      }
      if(!is_numeric($result)){
          return false;
      }
      return true;
  
  } 


  /**
   * 删除默认
   * @param  cart_id           地址ID
   * @return bool                 
   */
  public function del($member_id,$cart_id){

      $map = array(
              'member_id' => intval($member_id),
          );
      if(is_array($cart_id)){
          $map['id'] = array('IN',$cart_id); 
      } else if(is_numeric($cart_id)){
          $map['id'] = intval($cart_id);
      }else{
          return false;
      }

      $res = $this->where($map)->delete();

      return is_numeric($res);
  }  

    /**
    * 设置商品数量
    * @param  cart_id           地址ID
    * @return bool                 
    */
    public function set_num($member_id,$cart_id,$num){

      $member_id  = intval($member_id);
      $cart_id    = intval($cart_id);
      $num        = abs(intval($num));

      $map = array(
          'member_id' => $member_id,
          'id'        => $cart_id
        );

      $res = $this->where($map)->setField('product_num',$num);

      return is_numeric($res);
    }   


   /**
    * 获取选中购车中的商品数据
    * @param  cart_id           地址ID
    * @return bool                 
    */
    public function get_cart_selcet($member_id,$cart_ids){

        //获取购车数据
        $map   = array(
            'member_id'   => intval($member_id),
            'product_num' => array('GT',0),
            'id'      => array('IN',$cart_ids),
        );
        $field = array('id as cart_id','sku_id','product_id','product_title','product_price','product_spec_value','product_num','product_image');
        $cart_list = $this->where($map)->order('add_time desc')->field($field)->select();

        return (array)$cart_list;
    }


}
