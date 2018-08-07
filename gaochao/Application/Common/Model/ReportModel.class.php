<?php
namespace Common\Model;
use Think\Model;

/**
 * 体检报告模型模型 
 */
class ReportModel extends Model {

    protected $tableName  = 'report';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('member_id', 'require', '请指选择用户', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('ope_date', 'require', '请选择体检日期', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('ope_date', 'check_date', '日期格式不正确', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
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
     * 获取相册图片
     * @param  milit     $map   查询条件
     * @param  boolean   $field 查询字段
     * @return array     分类信息
     * @author llf
     */
    public function get_image($pid){
        return (array)M('Report_image')->where('report_id='.intval($pid))->field(['id as image_id','image','width','height'])->order('add_time desc')->select();
    }
}
