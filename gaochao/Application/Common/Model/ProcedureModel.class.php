<?php
namespace Common\Model;
use Think\Model;

/**
 * 议事堂-事件模型
 */
class ProcedureModel extends Model {

    protected $tableName  = 'procedure';
    protected $type       = array(
                                1 => 'agree',
                                2 => 'disagree',
                                3 => 'abstain',
                            );
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('district_id', 'require', '小区参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('title', 'require', '议事事件标题必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('description', 'require', '请输入内容描述', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('description', '1,255', '描述长度为1~250个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('end_date', 'require', '请选择投票截止时间', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('abstain', '/^\d{0,10}$/', '弃权票数参数数格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('agree', '/^\d{0,10}$/', '同意票数参数格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('disagree', '/^\d{0,10}$/', '不同意票数参数格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('sort', '/^\d{0,10}$/', '排序参数格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
        array('add_time', 'now_date_time', self::MODEL_INSERT,'function'),
	);

    /**
     * 获取相册图片
     * @param  milit     $map   查询条件
     * @param  boolean   $field 查询字段
     * @return array     分类信息
     * @author llf
     */
    public function get_image($pid){
        return (array)M('procedure_image')->where('pid='.intval($pid))->field(['id as image_id','image','width','height'])->order('add_time desc')->select();
    }

    /**
     * 同意
     * @param  int      $member_id      用户ID
     * @param  int      $procedure_id   议事ID
     * @param  int      $status         议事ID
     * @return bool
     * @author llf
     */
    public function vote($member_id,$procedure_id,$type){
        
        $member_id      = intval($member_id);
        $procedure_id   = intval($procedure_id);
        $type           = intval($type);

        if(!in_array($type, array_keys($this->type))){
            $this->error = '投票类型错误';
            return false;
        }

        $member_info = M('member')->where(['id'=>$member_id])->find();
        if(empty($member_info) || $member_info['is_del']){
            $this->error = '用户信息不存在';
            return false;
        }
        if($member_info['is_hid']){
            $this->error = '当前账户已禁用';
            return false;
        }
        if($member_info['type'] != 2){
            $this->error = '小区业主才可进行投票';
            return false;
        }

        //验证议事是否过截止时间
        $has = $this->where(['id'=>$procedure_id])->find();
        if(empty($has) || $has['is_del']){
            $this->error = '议事信息不存在';
            return false;
        }
        if($has['is_hid']){
            $this->error = '议事信息被禁用';
            return false;
        }

        //验证用户是否有权限投票(小区是否一致、用户是否是业主)

        //查询用户是否已经投过票了
        $exists = M('procedure_vote')->where(['member_id'=>$member_id,'procedure_id'=>$procedure_id])->find();
        if(!empty($exists)){
            $this->error = '该账户已投过票了';
            return false;
        }

        //插入投票记录
        $post = array(
                'member_id'     => $member_id,
                'procedure_id'  => $procedure_id,
                'type'          => $type,
                'add_time'      => now_date_time(),
            );
        $res = M('procedure_vote')->add($post);
        $upd = $this->where(['id'=>$procedure_id])->setInc($this->type[$type],1);

        if(!$res || !$upd){
            $this->error = intval($res).'投票失败'.intval($upd);
            return false;
        }
        return true;
    }
}
