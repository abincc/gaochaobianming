<?php
namespace Common\Model;
use Think\Model;

/**
 * 订单模型
 */
class OrderModel extends Model {

    protected $tableName  = 'Order';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(

        // array('product_title', 'require', '商品标题信息必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        // array('product_title', '1,120', '商品标题长度为1~120个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        // array('product_price', 'require', '商品价格必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        // array('product_price', DECIMAL, '商品价格格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        // array('product_spec_value', '1,250', '商品属性值长度为1~250个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        // array('product_num', 'is_numeric', '商品数量参数格式', self::EXISTS_VALIDATE , 'function', self::MODEL_BOTH),
        // array('product_image', '1,250', '路径长度为1~250个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        // array('sku_id', 'require', 'sku 参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        // array('product_id', 'require', '商品参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        // array('member_id', 'require', '用户参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
        array('add_time', 'now_date_time', self::MODEL_INSERT,'function'),
        array('update_time', 'now_date_time', self::MODEL_BOTH,'function'),
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
    * 获取物流费用
    * @param  array     prodict_id   商品ID
    * @return float     物流费用
    * @author llf
    */
    public function get_freight($product_ids){

        if(empty($product_ids)){
            return 0.00;
        } 
        $map = array(
                'id' => array('IN',$product_ids),
            );
        $freight = M('Product')->where($map)->order('freight desc')->field('freight')->find();

        return floatval($freight['freight']);

    }

    /**
    * 生成订单
    * @param  int       订单ID
    * @param  array     购车商品
    * @return bool      
    * @author llf
    */
    public function create_order($data){

        if(!$this->create($data)){
            return false;
        }

        $insertId  = $this->add();

        return $insertId;
    }


    /**
    * 生成子订单
    * @param  int       订单ID
    * @param  array     购车商品
    * @return bool      
    * @author llf
    */
    public function create_order_detail($order_id,$cart_list){

        if(empty($cart_list) || empty($order_id)){
            return false;
        }

        $order_detail = array();
        foreach($cart_list as $v){  
            $order_detail[] = array(
                'status'                => 10,
                'order_id'              => $order_id,
                'product_id'            => $v['product_id'],
                'product_title'         => $v['product_title'],
                'product_price'         => $v['product_price'],
                'product_num'           => $v['product_num'],
                'product_spec_value'    => $v['product_spec_value'],
                'product_image'         => $v['product_image'],
                'add_time'              => date('Y-m-d H:i:s'),
                'update_time'           => date('Y-m-d H:i:s'),
            );
        }
        $sql = addSql($order_detail,'sr_order_detail');

        //减少库存
        return is_numeric(M()->execute($sql));
    }

    /**
    * 计算订单金额
    * @param  array     购车商品
    * @return array     费用信息
    * @author llf
    */
    public function calculate($cart_list){

        $num_total = 0;
        $subtotal  = 0.00;
        array_walk($cart_list, function(&$a) use(&$num_total,&$subtotal){
            $num_total += $a['product_num'];
            $subtotal   = bcadd($subtotal,($a['product_num'] * $a['product_price']),3);
            $a['product_image'] = file_url($a['product_image']);
            $a['product_spec_value'] = implode('  ', array_values(unserialize($a['product_spec_value'])));
        });
        $data = array();
        $data['num_total']          = $num_total;
        $data['subtotal']           = $subtotal;

        return $data;
    }

    /**
    * 写入订单日志
    * @param  array     购车商品
    * @return array     费用信息
    * @author llf
    */ 
    public function insert_log($member_id,$order_id=0,$status=0,$remark=''){
        $data = array(
                'order_id'  => intval($order_id),
                'member_id' => intval($member_id),
                'status'    => intval($status),
                'remark'    => trim($remark),
                'add_time'  => date('Y-m-d H:i:s'),
            );
        M('Order_log')->add($data);
    }


    /**
    * 获取订单日志
    * @param  array     购车商品
    * @return array     费用信息
    * @author llf
    */ 
    public function get_log($order_id){
        $map = array(
                'order_id'  => intval($order_id)
            );
        $field = array('id as log_id','status','remark','add_time');
        $log = M('Order_log')->where($map)->field($field)->order('add_time asc')->select();
        
        if(!empty($log)){
            //处理订单状态
            $status = get_table_state('order','status');
            array_walk($log, function(&$a) use($status){
                $a['status_title'] = $status[$a['status']]['title'];
            });
        }
        return (array)$log;
    }

    /**
    * 【操作】取消订单
    * @param  order_id  订单ID
    * @return bool      
    * @author llf
    */ 
    public function cancel($order_id){
        $map = array(
                'status'    => 10,
                'id'        => intval($order_id),
            );
        $res = $this->where($map)->setField('status',5);

        /*判断订单是否存在有优惠劵，有则返*/
        $card_id = $this->where('id='.intval($order_id))->getField('card_id');
        if($card_id){
            M('Coupon_card')->where('id='.$card_id)->setField('status',1);
        }
        return is_numeric($res) && $res;
    }

    /**
    * 【操作】订单支付
    * @param  order_id  订单ID
    * @return array     
    * @author llf
    */ 
    public function pay($order_id,$pay_type,$deal_no){

        $map      = array(
                'id'        => intval($order_id),
                'status'    => 10,
            );
        $data     =  array(
                'status'    => 20,
                'pay_type'  => intval($pay_type),
                'deal_no'   => trim($deal_no),
                'pay_time'  => date('Y-m-d H:i:s'),
            );
        $res = $this->where($map)->save($data);
        return is_numeric($res) && $res;
    }

    /**
    * 【操作】订单发货
    * @param  order_id  订单ID
    * @param  order_id  物流编号
    * @return array     
    * @author llf 
    */ 
    public function deliver($order_id){
        
        $map = array(
                'status'    => 20,
                'id'        => intval($order_id),
            );
        $d   = array(
                'ship_time' => date('Y-m-d H:i:s'),
                'status'    => 30,
            );
        $res = $this->where($map)->save($d);

        return is_numeric($res) && $res;
    }

    /**
    * 【操作】确认收货
    * @param  order_id  订单ID
    * @return array     
    * @author llf
    */ 
    public function confirm_receipt($order_id){
        $map = array(
                'status'    => 30,
                'id'        => intval($order_id),
            );
        $d   = array(
                'confirm_time'  => date('Y-m-d H:i:s'),
                'status'        => 40,
            );
        $res = $this->where($map)->save($d);
        return is_numeric($res) && $res;
    }

    /**
    * 【操作】订单完成
    * @param  order_id  订单ID
    * @return array     
    * @author llf
    */ 
    public function finish($order_id){
        $map = array(
                'status'    => 40,
                'id'        => intval($order_id),
            );
        $res = $this->where($map)->setField('status',50);
        return is_numeric($res) && $res;
    }

    /**
    * 【操作】申请退货/退款
    * @param  order_id  订单ID
    * @return array     
    * @author llf
    */ 
    public function apply_returns($order_id){
        $map = array(
                'status'    => 20,
                'id'        => intval($order_id),
            );
        $res = $this->where($map)->setField('status',60);
        return is_numeric($res) && $res;
    }

    /**
    * 【操作】申请退货/退款
    * @param  order_id  订单ID
    * @return array     
    * @author llf
    */ 
    public function reapply_returns($order_id){
        $map = array(
                'status'    => 60,
                'id'        => intval($order_id),
            );
        $res = $this->where($map)->setField('status',20);
        return is_numeric($res) && $res;
    }

    /**
    * 【操作】确认退货/退款
    * @param  order_id  订单ID
    * @return array     
    * @author llf
    */ 
    public function confirm_returns($order_id){

        $map = array(
                'status'    => 60,
                'id'        => intval($order_id),
            );
        $res = $this->where($map)->setField('status',70);
        return is_numeric($res) && $res;
    }
  
    /**
    * 【操作】确认到付
    * @param  order_id  订单ID
    * @param  amount    实付金额
    * @return array     
    * @author llf
    */ 
    public function confirm_cod($order_id,$amount){

        $map = array(
                'pay_type'  => 3,
                'id'        => intval($order_id),
            );
        $d   = array(
                'pay_type'  => 4,
                'pay_time'  => date('Y-m-d H:i:s'),
                'money_real'=> floatval($amount),
            );
        $res = $this->where($map)->save($d);
        return is_numeric($res) && $res;
    }  

   /**
    * 【操作】扣减库存
    * @param  array  ['id'=>xx,'product_num']
    * @return array     
    * @author llf
    */ 
    private function inventory_reduction($arr){
        
        if(empty($arr))     return false;
        
        $Product_sku = M('product_sku');
        foreach ($arr as $v) {
            
        }

    }
}
