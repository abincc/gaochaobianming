<?php
namespace Common\Model;
use Think\Model;

/**
 * 申请开店模型
 */
class ApplyShopModel extends Model {

    protected $tableName  = 'apply_shop';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('shop_name', 'require', '请输入店铺名称', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('shop_name', '1,32', '店铺名称长度为1~32个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),

        array('district_id', 'require', '请选择小区', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('building_no', 'require', '请输入楼号', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('username', 'require', '请输入联系人姓名', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('username', '1,20', '店铺名称长度为1~20个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('mobile', 'require', '请输入联系人手机', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('mobile', MOBILE, '手机格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('email', 'require', '请输入电子邮箱', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('email', EMAIL, '邮箱格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('idcard', 'require', '请输入身份证号', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        // array('idcard','check_idcard','身份证号格式不正确',self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
        array('front_card', 'require', '请上传身份证正面', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('negative_card', 'require', '请上传身份证反面', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('status',array(0,1,2),'状态格式不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
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
