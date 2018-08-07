<?php
namespace Common\Model;
use Think\Model;

/**
 * 短信模型
 */
class CodeModel extends Model {

    protected $tableName  = 'sms_record';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
        array('add_time', 'now_date_time', self::MODEL_INSERT,'function'),
	);
	

    /**
    * 验证验证码
    * @param  mobile        短信接收手机
    * @param  type          业务
    * @param  code_id       验证码ID   
    * @return int/false     验证成功返回短信内容ID
    * @author llf
    */
    public function check($mobile,$code,$type=0,$code_id=0){

        $limit_time  = date('Y-m-d H:i:s',(time()-600));   //取十分钟内有效的记录

        $map = array(
                'type'      => $type,
                'mobile'    => $mobile,
                'is_del'    => 0,
                'state'     => 1,
                'content'   => $code,
                'add_time'  => array('EGT',$limit_time),
            );
        if(!empty($code_id)){
            $map['id']   = intval($code_id);
        }
        write_debug($map,'验证码查询条件');
        $list = $this->where($map)->field(['id','content'])->order('add_time desc')->select();
        if(!empty($list)){
            return $list[0]['id'];
        }else{
            return false;
        }
    }

    /**
    * 验证验证码
    * @param  mobile        短信接收手机
    * @param  type          业务
    * @param  code_id       验证码ID   
    * @return int/false     验证成功返回短信内容ID
    * @author llf
    */
    public function del($code_id){
        $code_id = intval($code_id);
        $res     = $this->where('id='.$code_id)->setField('is_del',1);
        return is_numeric($res) && $res;
    }
}
