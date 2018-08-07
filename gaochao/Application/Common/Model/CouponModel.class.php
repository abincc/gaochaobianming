<?php
namespace Common\Model;
use Think\Model;

/**
 * 卡券模型 
 */
class CouponModel extends Model {

    protected $tableName  = 'coupon';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('title', 'require', '请选输入卡券名称', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('title', '1,30', '卡券名称长度范围在1~30个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('description', 'require', '请选输入卡券名称', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('description', '0,120', '卡券名称长度范围在0~120个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('amount', 'require', '请输入抵扣金额', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('amount', DECIMAL, '抵扣金额格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('minimum', 'require', '请输入消费门槛', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('minimum', DECIMAL, '消费门槛格式必须是金额类型', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('minimum', 'checkMinimum', '最低消费门槛必须大于优惠金额', self::EXISTS_VALIDATE, 'callback', self::MODEL_BOTH),
        array('begin_date', 'require', '请输入有效期的开始日期', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('begin_date', 'check_date', '有效期起始时间格式不正确', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
        array('end_date', 'require', '请输入有效期的结束日期', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('end_date', 'check_date', '有效期结束时间格式不正确', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
        array('end_date', 'checkdate', '有效期结束日期必须大于等于起始日期', self::EXISTS_VALIDATE, 'callback', self::MODEL_BOTH),
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
     * 判断最低消费门槛是否大于抵扣金额
     * @var array
     */
    protected function checkMinimum(){
        return I('amount',0,'floatval') < I('minimum',0,'floatval');
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

}
