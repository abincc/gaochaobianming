<?php
namespace Common\Model;
use Think\Model;

/**
 * 申请志愿者模型 
 */
class VolunteerModel extends Model {

    protected $tableName  = 'apply_volunteer';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('member_id', 'require', '请指定用户', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('skill', 'require', '请选输入个人专长', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('skill', '3,120', '专长信息长度范围在3~120个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('intention', 'require', '请选输入工作意向', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('intention', '3,120', '工作意向长度范围在3~120个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('start_date', 'require', '请选择志愿起始日期', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('start_date', 'check_date', '起始日期格式不正确', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
        array('end_date', 'require', '请选择志愿结束日期', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('end_date', 'check_date', '结束日期格式不正确', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
        array('end_date', 'check_end_date', '结束日期必须大于或等于起始日期', self::EXISTS_VALIDATE, 'callback', self::MODEL_BOTH),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
        array('add_time', 'now_date_time', self::MODEL_INSERT,'function'),
	);

    /**
    * 检查结束日期是否大于或等于起始日期
    * @return bool
    * @author llf
    */
    protected function check_end_date(){

        $start_date = I('start_date','','trim');
        $end_date   = I('end_date','','trim');

        if(!$start_date || !$end_date){
            return false;
        }

        return  strtotime($end_date) >= strtotime($start_date) ;
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
