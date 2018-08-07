<?php
namespace Common\Model;
use Think\Model;
use Org\JPush\JPush;
/**
 * 消息模型
 */
class MessageModel extends Model {

    protected $tableName  = 'message';
    protected $type       = array(
    							'1'=>'message_property',
    							'2'=>'message_order',
    							'3'=>'message_arrears',
    						);
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
		array('title', 'require', '请输入消息标题', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
		array('title', '1,30', '消息标题长度范围1~30个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('member_id', 'require', '请输选择接受用户', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
		array('add_time', 'now_date_time', self::MODEL_INSERT,'function'),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	public function send($member_id,$type,$title,$content){

		$data = array(
				'member_id'	=>intval($member_id),
				'type'		=>intval($type),
				'title'		=>$title,
				'content'	=>$content,
			);
		
		write_debug($data,'消息数据3');
        if(!$this->create($data)){
			write_debug($this->getError(),'消息数据');
            return false;
        }
        $insertId  = $this->add();
		# 更新消息数量
		$field = $this->type[$type];
		if(empty($field)){
			$this->error = '消息类型错误';
			write_debug($this->getError(),'消息数据');
			return false;
		}
		$condition = array(
					'member_id'=>$member_id,
				);
		$upd = M('member_count')->where($condition)->setInc($field);
		if(!is_numeric($upd) || !$upd){
			$this->error = '消息数增加失败';
			return false;
		}

        # 极光推送
        $extras = array(
        		'type' 	=> $type,
        		'id'	=> $insertId,
        	);
		$res = JPush::push(trim($member_id),$title,$content,$extras);
		if(!$res){
			write_debug($res,'极光推送消息失败');
		}

        return $insertId;

	}

	/**
	 * 模型自动完成
	 * @var array
	 */
	public function set_read($member_id,$type,$message_ids){

	 	if(empty($message_ids)){
	 		return false;
	 	}
	 	$map = array(
	 			'type'		=>$type,
	 			'member_id'	=>$member_id,
	 			'is_del'	=>0,
	 		);
	 	if(is_numeric($message_ids)){
	 		$map['id'] 		= intval($message_ids);
	 	}
	 	if(is_array($message_ids)){
	 		$map['id']		= array('IN',$message_ids);
	 	}
	 	$result = $this->where($map)->setField('is_read',1);
	 	// bug($result);
	 	if($result > 0 && is_numeric($result)){
			$condition = array(
	 				'member_id'=>$member_id,
	 			);
			$field = $this->type[$type];
	 		if(!empty($field)){
	 			M('member_count')->where($condition)->setDec($field,$result);
	 		}
	 	}
	 	return is_numeric($result) && $result;
	}


}
