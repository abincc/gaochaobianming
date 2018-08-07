<?php
namespace Common\Model;
use Think\Model;

/**
 * 投诉模型
 */
class ComplaintModel extends Model {

    protected $tableName  = 'complaint';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('district_id', 'require', '小区参数获取失败', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('content', 'require', '请输入投诉或建议的具体内容', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('content', '1,200', '内容长度为1~200个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
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
        return (array)M('complaint_image')->where('complaint_id='.intval($pid))->field(['id as image_id','image'])->order('add_time desc')->select();
    }
}