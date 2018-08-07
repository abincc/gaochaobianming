<?php
namespace Common\Model;
use Think\Model;

/**
 * 订单评论模型
 */
class OrderCommentModel extends Model {

    protected $tableName  = 'order_comment';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('content', 'require', '请输入评论内容', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('content', '1,200', '内容长度为1~200个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('star',array(1,2,3,4,5),'评分格式不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
        array('product_id', 'require', '商品参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('member_id', 'require', '用户参数必须', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
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
    * 获取评论图片
    * @param  milit     $map   查询条件
    * @return array     分类信息
    * @author llf
    */
    public function get_image($pid){
        return (array)M('order_commont_img')->where('pid='.intval($pid))->field(['id as image_id','image'])->order('add_time desc')->select();
    }

 
    /**
    * 判断订单商品是否全部评价
    * @param  int       $map   查询条件
    * @return none      无返回
    * @author llf
    */
    public function order_commented($order_id){

        $condition = array(
                'order_id'          =>intval($order_id),
                'comment_status'    =>0,
                'is_del'            =>0,
                'is_hid'            =>0,
            );
        $exists = M('Order_detail')->where($condition)->field(['id'])->select();
        if(empty($exists)){
            $map = array(
                    'id'    =>$order_id,
                    'status'=>40,
                );
            M('Order')->where($map)->setField('status',50);
        }

    }   
}  
