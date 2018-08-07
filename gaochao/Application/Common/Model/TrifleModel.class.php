<?php
namespace Common\Model;
use Think\Model;

/**
 * 小事模型
 */
class TrifleModel extends Model {

    protected $tableName  = 'trifle';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(

        array('district_id', 'require', '请选择小区', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('trifle_service_id', 'require', '请选择服务类型', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('content', 'require', '请输入您需要帮忙的具体事件', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('content', '1,200', '事件内容长度为1~250个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('member_id', 'require', '用户参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('member_id', 'is_numeric', '用户参数错误', self::EXISTS_VALIDATE, 'function', self::MODEL_BOTH),
        array('status',array(1,2,3),'状态格式不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
        array('reply', 'require', '请输入回复内容', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('reply', '1,200', '回复内容长度为1~250个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('is_reply',array(0,1),'回复状态格式不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),

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
    * 获取封面图片
    * @param  array      ID 多个数组    查询条件
    * @return array     
    * @author llf
    */
    public function get_covers($ids){

        $res = M('trifle_image')->where(array('pid'=>array('IN',$ids)))->field(['pid','image'])->group('pid')->select();
        if(!empty($res)){
            $res = array_column($res,null,'pid');
        }
        return $res;
    }

    /**
     * 获取相册图片
     * @param  milit     $map   查询条件
     * @param  boolean   $field 查询字段
     * @return array     分类信息
     * @author llf
     */
    public function get_image($pid){
        return (array)M('trifle_image')->where('pid='.intval($pid))->field(['id as image_id','image','width','height'])->order('add_time desc')->select();
    }

    /**
    * 处理问题
    * @param  milit      $map   查询条件
    * @param  boolean    $field 查询字段
    * @return array     分类信息
    * @author llf
    */
    public function deal($id,$status,$member_id=0){

        $data = array(
                'status'        => intval($status),
                'deal_time'     => date('Y-m-d H:i:s'),
                'remember_id'   => intval($member_id),
            );
        if(!$this->create($data)){
            return false;
        }
        return is_numeric($this->where('id='.intval($id))->save($data));
    }

    /**
    * 设为取消状态
    * @param  milit      $map   查询条件
    * @param  boolean    $field 查询字段
    * @return array     分类信息
    * @author llf
    */
    public function cancel($id){
        $map = array(
                'id'    => intval($id),
            );
        return is_numeric($this->where($map)->setField('status',3));
    }

    /**
     * 获取回复图片
     * @param  milit     $map   查询条件
     * @param  boolean   $field 查询字段
     * @return array     分类信息
     * @author llf
     */
    public function get_reply_image($pid){
        return (array)M('trifle_reply_image')->where('pid='.intval($pid))->field(['id as image_id','image','width','height'])->order('add_time desc')->select();
    }
}
