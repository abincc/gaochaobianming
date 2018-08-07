<?php
namespace Common\Model;
use Think\Model;

/**
 * 电话本模型 
 */
class TbookModel extends Model {

    protected $tableName  = 'tbook';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('title', 'require', '请选输入电话名称', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('title', '1,120', '电话名称长度范围在1~120位', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('number', 'require', '请选输入电话号码', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('number', '1,16', '电话号码长度范围在1~16位', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('address', '0,26', '地址长度范围在0~26位', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('sort', '/^\d{0,6}$/', '排序值范围在0~999999', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
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

}
