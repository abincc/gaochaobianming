<?php
namespace Common\Model;
use Think\Model;

/**
 * 卡券模型 
 */
class CardModel extends Model {

    protected $tableName  = 'coupon_card';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('coupon_id', 'require', '请选选择卡券类型', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('coupon_id', 'checkcoupon', '卡券类型信息不存在', self::EXISTS_VALIDATE, 'callback', self::MODEL_BOTH),
        array('member_id', 'require', '请选选择指定用户', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('member_id', 'checkmember', '该用户信息不存在', self::EXISTS_VALIDATE, 'callback', self::MODEL_BOTH),
        array('order_id', 'checkorder', '该订单信息不存在', self::EXISTS_VALIDATE, 'callback', self::MODEL_BOTH),
        array('code', 'require', '请选输入卡券兑换码', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('code', '10,32', '卡券兑换码长度在10~32个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('code', 'checkcode', '卡券兑换码已存在', self::EXISTS_VALIDATE, 'callback', self::MODEL_BOTH),
        array('status',array(1,2,3),'状态格式不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
        array('cancel_time', 'check_time', '核销时间格式不正确', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
        array('get_time', 'check_time', '获取时间格式不正确', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
        array('add_time', 'now_date_time', self::MODEL_INSERT,'function'),
        array('card_no', 'create_card_no', self::MODEL_BOTH,'callback'),
	);
	
    /**
     * 检查卡券是否存在是否启用
     * @var array
     */
    protected function checkcoupon(){
        // bug($this->data);
        $condition = array(
                'id'        => intval(I('coupon_id',0,'int')),
                'is_del'    => 0,
            );
        $info = M('coupon')->where($condition)->field('id,is_hid')->find();
        if(empty($info)){
            return false;
        }
        if($info['is_hid']){
            $this->error = '卡券类型未启用';
            return false;
        }
        return true;
    }

    /**
     * 生成卡券编号
     * @var array
     */
    protected function create_card_no(){
        return date('ymd').''. str_pad(strval($this->order('id desc')->getField('id')), 11, '0', STR_PAD_LEFT);
    }

    /**
     * 检查该用户是否存在
     * @var array
     */
    protected function checkmember(){
        $condition = array(
                'id'        => intval(I('member_id',0,'int')),
                'is_del'    => 0,
            );
        return !empty(M('member')->where($condition)->getField('id'));
    }

    /**
     * 检查兑换是否存在
     * @var array
     */
    protected function checkcode(){
        $condition = array(
                'code' => I('code','','trim'),
            );
        return empty($this->where($condition)->getField('id'));
    }

    /**
     * 检查订单是否存在是否与用户对应
     * @var array
     */
    protected function checkorder(){
        $condition = array(
                'id'        => intval(I('order_id',0,'int')),
                'is_del'    => 0,
            );
        $member_id = M('order')->where($condition)->getField('member_id');
        if(empty($member_id)){
            return false;
        }
        if($member_id != I('member_id',0,'int')){
            $this->error = '卡券所用订单不属于该用户';
            return false;
        }
        return true;
    }


    /**
     * 判断最低消费门槛是否大于抵扣金额
     * @var array
     */
    protected function checkdate(){
        return strtotime(I('begin_date','','trim')) <= strtotime(I('end_date','','trim'));
    }


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
    * 获取详细信息
    * @param  milit      $map   查询条件
    * @param  boolean    $field 查询字段
    * @return array     分类信息
    * @author llf
    */
    public function exchange($card_id,$member_id){
        $map      = array(
                'id'        => intval($card_id),
                'status'    => 1,
            );
        $data     =  array(
                'member_id' => intval($member_id),
                'get_time'  => date('Y-m-d H:i:s'),
            );
        $res = $this->where($map)->save($data);
        return is_numeric($res) && $res;
    }

    /**
     * 可用优惠劵数量
     * @time 2017-11-03
     * @author llf
     **/
    public function usable_count($member_id,$order_amount){

        //获取小于该金额的优惠劵
        $map    = array(
                'member_id' => intval($member_id),
                'minimum'   => array('lt',floatval($order_amount)),
                'status'    => 1,
                'is_del'    => 0,
                'is_hid'    => 0,
            );
        return D('CardView')->where($map)->count();
    }

    /**
     * 核销卡券
     * @time 2017-11-03
     * @author llf
     **/
    public function card_cancel($card_id,$order_id){
        $map      = array(
                'id'        => intval($card_id),
            );
        $data     =  array(
                'status'        => 2,
                'order_id'      => intval($order_id),
                'cancel_time'   => date('Y-m-d H:i:s'),
            );
        $res = $this->where($map)->save($data);
        return is_numeric($res) && $res;
    }
}
