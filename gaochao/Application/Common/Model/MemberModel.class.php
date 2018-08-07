<?php
namespace Common\Model;
use Think\Model;

/**
 * 用户模型
 */
class MemberModel extends Model {

    protected $tableName  = 'member';

    protected $expires_in = 1800;

    protected $statsu     = '';
	
    /**
	 * 自动验证规则
	 * @var array
	 */
    protected $_validate = array(
        array('district_id', 'require', '请选择小区', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        // array('district_id', '0', '请选择小区', self::EXISTS_VALIDATE,'equal', self::MODEL_BOTH),
        //检查小区ID是否存在
		array('username', 'require', '请输入真实姓名', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
		array('username', '1,22', '实姓名长度范围1~22个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('nickname', 'require', '请输入昵称', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('nickname', '1,22', '昵称名长度范围1~22个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('idcard', 'require', '请输入身份证号', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('building_no', 'require', '请输入楼号', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('building_no', '1,20', '楼号长度范围1~20个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('room_no', 'require', '请输入房号', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('room_no', '1,20', '房号长度范围1~20个字符', self::EXISTS_VALIDATE , 'length', self::MODEL_BOTH),
        array('sex',array(1,2),'性别格式不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
 		array('mobile', 'require', '请输入手机号', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
		array('mobile', MOBILE, '手机格式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
		array('mobile', '', '手机已经存在啦', self::VALUE_VALIDATE, 'unique', self::MODEL_BOTH),
        array('password', 'require', '请输入密码', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('password', NUM_CHAR_SPECIAL,'密码格式不正确（8-30位必须包含数字字母）', self::VALUE_VALIDATE , 'regex', self::MODEL_BOTH),
        array('age', '/^\d{0,3}$/', '年龄式不正确', self::EXISTS_VALIDATE , 'regex', self::MODEL_BOTH),
        array('repassword', 'require', '请输入确认密码', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('repassword','password','确认密码不一致', self::EXISTS_VALIDATE,'confirm'),
        array('type',array(1,2,3),'注册类型不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
        array('state',array(0,1,2,3),'审核状态类型不正确',self::EXISTS_VALIDATE, 'in', self::MODEL_BOTH),
        array('register_code', 'require', '请输入业主邀请码', self::EXISTS_VALIDATE, 'regex', self::MODEL_BOTH),
        array('register_code', '', '业主邀请码已经存在', self::VALUE_VALIDATE, 'unique', self::MODEL_BOTH),
	);

	/**
	 * 模型自动完成
	 * @var array
	 */
	protected $_auto = array(
		array('register_time', 'now_date_time', self::MODEL_INSERT,'function'),
		array('login_time', 'now_date_time', self::MODEL_INSERT,'function'),
		array('login_ip','get_client_ip', self::MODEL_INSERT,'function'),
		array('register_ip','get_client_ip', self::MODEL_INSERT,'function'),
		array('password','sys_password_encrypt', self::MODEL_INSERT,'function'),
	);
	

    /**
     * 数据写入之前操作
     * @var array
     */
    protected  function _after_insert($data,$options){
        parent::_after_insert($data, $options);
        $id = $this->getLastInsID();
        $res = M('member_count')->add(array('member_id'=>$id,'add_time'=>date('Y-m-d H:i:s')));
        if(!is_numeric($res)){
            $this->error  = '系统错误 member_count';
            return false;
        }
    }
    
    /**
     * 获取用户详细信息
     * @param  milit   	 $map 	查询条件
     * @param  boolean 	 $field 查询字段
     * @return array     分类信息
     * @author llf
     */
    public function info($map, $field = true){
        return $this->field($field)->where($map)->find();
    }


    /**
     * 获取用户详细信息
     * @param  mobile     string   登录手机号
     * @param  password   string   登录手机密码
     * @param  type       int      登录账号类型   1-供应商 2-采购商
     * @return int/string          返回错误字符串 或 验证成功的用户ID
     * @author llf
     */
    public function check_login($mobile,$password,$type){

        if(empty($mobile))              return '请输入手机号';
        if(!preg_match(MOBILE,$mobile)) return '请输入正确的手机号';
        if(empty($password))            return '请输入密码';

        $has = $this->where(array('mobile'=>$mobile))->field(['id','type','password','is_hid','is_del'])->find();
        if(empty($has) || $has['is_del']){
            return '该手机账号不存在';
        }
        if(!sys_password_verify($password,$has['password'])){
            return '账号密码错误';
        }
        if($has['type'] != $type && $type != 0){
            return '账号类型错误';
        }
        if($has['is_hid']){
            return '账号已被禁用';
        }

        return intval($has['id']);
    }


    /**
     * 登录指定用户数据
     * @param  integer $uid 用户ID
     * @return boolean      ture-登录成功，false-登录失败
     */
    public function login($uid){

        $uid = intval($uid);
        /* 检测是否在当前应用注册 */
        $user = $this->field('id as member_id,type,state,username,nickname,mobile,sex,district_id')->find($uid);
        if(empty($user)){ 
            $this->error  = '获取用户信息为NULL';
            return false;
        }
        if(2 == $user['type']){
            if(0 == intval($user['state'])){
                $this->error  = '您的业主账户未审核';
                return false;
            }
            if(1 == $user['state']){
                $this->error  = '您的业主账户待审核';
                return false;
            }
            if(3 == $user['state']){
                $this->error  = '您的业主账户审核失败';
                return false;
            }        
        }
        unset($user['state']);
       
        $login_time = date('Y-m-d H:i:s');
        if(!$this->where('id='.$uid)->setField('login_time',$login_time)){ //未注册或删除
            $this->error  = '更新登录时间失败';
            return false;
        }

        $ticket_info = $this->get_memnber_ticket($uid,$login_time);
        if(!$ticket_info){
            $this->error  = 'ticket 凭证生成失败';
            return false;
        }
        $user = array_merge($user,$ticket_info);
        $user['headimg'] = get_avatar($uid);
        //记录行为
        //action_log('user_login', 'member', $uid, $uid);

        return $user;

    }


    //获取某用户凭证
    //token 根据凭证进行生成
    public function get_memnber_ticket($uid,$login_time=null){

        if(empty($login_time)){
            $login_time = $this->where('id='.$uid)->getField('login_time');
        }

        if(empty($login_time) || ($login_time !== date('Y-m-d H:i:s',strtotime($login_time)))){

            return false;
        }

        $ticket_info = array();
        $ticket_info['ticket'] = encrypt_key($uid.'@'.$login_time);

        return $ticket_info;
    }


    //生成token
    public function create_token($ticket){

        //对ticket进行解析
        if(empty($ticket)){
            $this->error  = 'ticket 参数错误1';
            return false;
        }
        $ticket_info = explode('@', decrypt_key($ticket));
        if(!is_array($ticket_info) || empty($ticket_info)){
            $this->error  = 'ticket 参数错误2';
            return false;
        }
        //判断ticket是否过期,登录过期
        $login_time = $this->where('id='.intval($ticket_info[0]))->getField('login_time');
        if(empty($login_time)){
            $this->error  = 'ticket 参数错误3';
            return false;
        }
        //解析token参数
        if($login_time !== $ticket_info[1]){
            $this->status = '-1';
            $this->error  = 'ticket 过期,请重新登录获取';
            return false;
        }

        $ticket_info[] = date('Y-m-d H:i:s');
        $ticket_info[] = $this->expires_in;

        $data = array(
                'token'      => encrypt_key(implode('@', $ticket_info)),
                'expires_in' => $this->expires_in,
            );
        return $data;

    }


    /*
    * 判断是否为app，和用户是否同时登录
    * @time 2015-11-11
    * @author    llf  <747060156@qq.com>
    * 1.登录     参数  apitype,key,home_member_id
    * 2.不用登录 参数  apitype
    */
    public function check_token($posts){
        
        /* 手机app接口密钥 */
        $token  = trim($posts['token']);                                    

        if(empty($token)){
            $this->error ='token 参数必须';
            return false;
        }

        $token  = decrypt_key($token);        
        $token  = explode('@', $token);
        // var_dump($token);
        // die();
        $token[0]        = intval($token[0]);
        $last_login_time = date('Y-m-d H:i:s',strtotime($token[1])); 
        $expires_time    = date('Y-m-d H:i:s',strtotime($token[2]));
        $expires_in      = intval($token[3]);

        if($last_login_time !== $token[1] || $expires_time !== $token[2]){
            $this->error ='token 错误';
            return false;
        }

        if((1 > $token[0]) || ($expires_in) < 0){
            $this->status = '-2';
            $this->error ='token 错误';
            return false;
        }

        /* 判断是否过期*/
        // var_dump(array(
        //         strtotime($expires_time),(time() - $expires_in)
        //     ));
        // die();
        // if(strtotime($expires_time) < (time() - $expires_in)){
        //     $this->status = '-2';
        //     $this->error ='token 已过期';
        //     return false;
        // }
        /* 判断登录过期(同时登录) */ 
        $login_time = M('member')->where('id='.$token[0])->getField('login_time');
        if(!$login_time){
            $this->error ='用户信息不存在';
            return false;
        }   
        if($login_time !== $last_login_time){
            $this->status = '-1';
            $this->error ='登录挤掉线了';
            return false;
        }
        
        return $token[0];
    }


}
