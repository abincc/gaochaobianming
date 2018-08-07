<?php
namespace Common\Model;
use Think\Model;

/**
 * 用户地址模型
 */
class AddressModel extends Model {

    protected $tableName  = 'member_address';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('name', 'require', '请输入收货人', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('name', '1,20', '收货人长度为1~20个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('mobile', 'require', '请输入收货人手机号', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('mobile', MOBILE, '收货人手机格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        //检查小区ID是否存在
        array('province', 'require', '请选择地区', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('city', 'require', '请选择地区', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('area', 'require', '请选择地区', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('address_detail', 'require', '请输入地址详情', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
		array('address_detail', '1,200', '地址详情长度为1~200个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),

        array('is_default',array(1,0),'默认参数格式不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
        array('zipcode', ZIPCODE, '邮编格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('remak', '1,200', '备注信息长度为1~200个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('member_id', 'require', '用户参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),

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
     * 设置默认
     * @param  address_id           地址ID
     * @return bool                 
     */
    public function set_default($member_id,$address_id){

        $map = array(
                'member_id' => intval($member_id),
                'id'        => intval($address_id),
            );
        
        $res = $this->where($map)->setField('is_default',1);

        unset($map['id']);
        $map['id'] = array('neq',intval($address_id));
        $this->where($map)->setField('is_default',0);

        return is_numeric($res);
    }

    /**
     * 删除默认
     * @param  address_id           地址ID
     * @return bool                 
     */
    public function del($member_id,$address_id){

        $map = array(
                'member_id' => intval($member_id),
                'id'        => intval($address_id)
            );

        $res = $this->where($map)->setField('is_del',1);

        return is_numeric($res);
    }   


    /**
     * 获取默认地址
     * @param  member_id            用具
     * @return bool                 
     */
    public function get_address($member_id,$address_id=0){

        $map = array(
                'member_id'     => intval($member_id),
                'is_default'    => 1,
                'is_hid'        => 0,
                'is_del'        => 0,
            );
        if($address_id){
            $map['id'] = intval($address_id);
            unset($map['is_default']); 
        }
        $field = array('id as address_id','province','city','area','address_detail','mobile','name');
        $info  = $this->where($map)->field($field)->find();
        if(!empty($info)){
            $area_cahce = get_no_hid('area');
            $info['province_title'] = $area_cahce[$info['province']]['title'];
            $info['city_title']     = $area_cahce[$info['city']]['title'];
            $info['area_title']     = $area_cahce[$info['area']]['title'];
        }

        return (array)$info;
    }    
}
