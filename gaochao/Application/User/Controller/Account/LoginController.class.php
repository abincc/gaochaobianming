<?php
namespace User\Controller\Account;
/**
 * 登录
 */
class LoginController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table='member';
	/**
	 * 登录
	 */
	public function index(){
		if(session('member_id')){
	 		header("location:".U('/'));
		}
		/*POST为登录，否则为显示*/
		if(IS_POST){
			$info = $this->check_login();
			return $this->after_login($info);
		}
		else{
			return $this->display();
		}
	}
	/**
	 * 退出登录
	 */
	public function login_out(){
		/*将退出登录前的URL重新赋值给变量*/
		$login_url = session("history_url") ? session("history_url") : '/';
		/*清空所有session*/
		session(null);
		/*跳转回退出前的所在页面*/
		header("location:" . $login_url);
	}
	/**
	 * 第三方登录后进行绑定账号处理
	 * @param string $type 请求第三方登录的类型
	 * @param string $code 上一步请求到的code
	 * 流程：
	 * 		1、先请求第三方登录，获取open_id
	 * 		2、判断用户是否存在账号
	 * 		3、存在就判断资料是否完整，不完整就跳转账号绑定页面
	 * 		4、没有账号跳转到账号绑定页面
	 */
	public function callback() {
		$type = I('type');
		$code = I('code');
		$opt = I('opt');
		/*获取第三方登录请求类型*/
		if(empty($type) || empty($code)){
			return $this->error('参数错误',U('index'));
		}
		if(!$this->get_sdk_type($type)){
			return $this->error('登录类型错误',U('index'));
		}
		/*实例化请求的第三方登录接口*/
		import('Org.ThinkSDK.ThinkOauth');
		$sns = \ThinkOauth::getInstance($type);
		/*获取第三方登录的OPNEID 和TOKEN 存入session*/
		$tokenArr = $sns->getAccessToken($code);
		if (!$tokenArr['openid']) {
			return $this->error('系统出错;请稍后再试！',U('index'));
		}
		$openid = $tokenArr['openid'];
		$token = $tokenArr['access_token'];
		session("sdk_openid",$openid);
		session("sdk_type", $type.'_open_id');
		session("sdk_access", $token);
		/*判断当前请求第三方的OPENID是否绑定账号*/
		$map[$type.'_'.'open_id'] = $openid;
		$info = get_info($this->table,$map);
		if($info){
			if($opt == 'bind'){
				$this->error('该账户已绑定');
			}
			return $this->after_login($info);
		}else{
			/*已登录用户绑定账号成功后跳转到账户首页*/
			if(session('member_id')){
				$data['id']=session('member_id');
				$data[session('sdk_type')] = session('sdk_openid');
				$result=update_data($this->table,array(),array(),$data);
				return $this->redirect('/');
			}
			$this->get_sdk_info($sns,$type);
			return $this->redirect('bind');
		}
	}
	/**
	 * 登录验证
	 */
	private function check_login(){
		$username = I('account');
		$password = I('password');
		if($username == ''){
			$this->error('请输入用户名');
		}
		if($password == ''){
			$this->error('请输入密码');
		}
		if($this->check_verify(I('verify')) != 1){
			$this->error('验证码不正确');
		}
		$info = get_info($this->table,array('mobile'=>$username));
		if(!$info['id'] || ($info['password'] != md5(md5($password).$info['salt']))){
			$this->error('用户名或密码错误');
		}
		if($info['is_hid'] !== '0'){
			$this->error('账户已禁用');
		}
		return $info;
	}
	/**
	 * 通过登录验证后处理
	 * @param array $info 用户信息
	 * @param array $info 用户信息
	 */
	private function after_login($info){
		$member_id = $info['id'];
		/** 更新登录时间、登录IP及OPENID */
		$data['id'] = $member_id;
		$info['login_time'] = $data['login_time'] = time();
		$info['login_ip'] = $data['login_ip'] = get_client_ip();
		/*更新第三方绑定信息*/
		if(session('sdk_openid') && !$info[session('sdk_type')]){
			$data[session('sdk_type')] = session('sdk_openid');
		}
		$result = update_data($this->table, array(), array(),$data);
		if(!is_numeric($result)){
			$this->error('登录失败，请重试');
		}
		session('member_info',$info);
		session('member_id',$member_id);
		
		$this->clean_sdk_session();
		if(I('remember')){
			cookie('account',I('account'));
			cookie('password',I('password'));
			cookie('remember',I('remember'));
		}
		else{
			cookie('account',null);
			cookie('password',null);
			cookie('remember',null);
		}
		if(session('history_url')){
			$this->success('登录成功',session('history_url'));
		}else{
			$this->success('登录成功',U('/'));
		}
	}

	/**
	 * 第三方登录请求
	 * @param string $type 第三方登录的种类
	 */
	public function sdk_login($type){
		if(I('promoter_id')){
			$promoter_id=intval(I('promoter_id'));
		}
		/*将promoter_id存入session，第三方回调完成后处理*/
		session('promoter_id',$promoter_id);
		/*更改第三方回调地址，传入promoter_id*/
		empty($type) && $this->error('参数错误');
		import('Org.ThinkSDK.ThinkOauth');
		$sns =\ThinkOauth::getInstance($type);
		redirect($sns->getRequestCodeURL());
	}
	
	
	/**
	 * 获取第三方数据
	 * @param string $sns 第三方SDK对象
	 * @param string $type 第三方登录的种类
	 */
	private function get_sdk_info($sns,$type = ''){
		$data = array();
		switch($type){
			case 'qq':
				$data = $sns->call('user/get_user_info');
				/*
				ret	返回码
				msg	如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
				nickname	用户在QQ空间的昵称。
				figureurl	大小为30×30像素的QQ空间头像URL。
				figureurl_1	大小为50×50像素的QQ空间头像URL。
				figureurl_2	大小为100×100像素的QQ空间头像URL。
				figureurl_qq_1	大小为40×40像素的QQ头像URL。
				figureurl_qq_2	大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
				gender	性别。 如果获取不到则默认返回"男"
				is_yellow_vip	标识用户是否为黄钻用户（0：不是；1：是）。
				vip	标识用户是否为黄钻用户（0：不是；1：是）
				yellow_vip_level	黄钻等级
				level	黄钻等级
				is_yellow_year_vip	标识是否为年费黄钻用户（0：不是； 1：是）
				*/
				session('sdk_img',$data['figureurl_qq_1']);
				session('sdk_nickname',$data['nickname']);
				break;
			case 'weixin':
				$data = $sns->call('sns/userinfo');
				/*
				openid	普通用户的标识，对当前开发者帐号唯一
				nickname	普通用户昵称
				sex	普通用户性别，1为男性，2为女性
				province	普通用户个人资料填写的省份
				city	普通用户个人资料填写的城市
				country	国家，如中国为CN
				headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
				privilege	用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
				unionid	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
				*/
				session('sdk_img',$data['headimgurl']);
				session('sdk_nickname',$data['nickname']);
				break;
			case 'wechat':
				$data = $sns->call('sns/userinfo');
				/*
				openid	普通用户的标识，对当前开发者帐号唯一
				nickname	普通用户昵称
				sex	普通用户性别，1为男性，2为女性
				province	普通用户个人资料填写的省份
				city	普通用户个人资料填写的城市
				country	国家，如中国为CN
				headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
				privilege	用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
				unionid	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
				*/
				session('sdk_nickname',$data['nickname']);
				session('sdk_img',$data['headimgurl']);
				break;
			case 'sina':
				$data = $sns->call('users/show', array('uid'=> session('sdk_openid')));
				/*
					id	int64	用户UID
					idstr	string	字符串型的用户UID
					screen_name	string	用户昵称
					name	string	友好显示名称
					province	int	用户所在省级ID
					city	int	用户所在城市ID
					location	string	用户所在地
					description	string	用户个人描述
					url	string	用户博客地址
					profile_image_url	string	用户头像地址（中图），50×50像素
					profile_url	string	用户的微博统一URL地址
					domain	string	用户的个性化域名
					weihao	string	用户的微号
					gender	string	性别，m：男、f：女、n：未知
					followers_count	int	粉丝数
					friends_count	int	关注数
					statuses_count	int	微博数
					favourites_count	int	收藏数
					created_at	string	用户创建（注册）时间
					following	boolean	暂未支持
					allow_all_act_msg	boolean	是否允许所有人给我发私信，true：是，false：否
					geo_enabled	boolean	是否允许标识用户的地理位置，true：是，false：否
					verified	boolean	是否是微博认证用户，即加V用户，true：是，false：否
					verified_type	int	暂未支持
					remark	string	用户备注信息，只有在查询用户关系时才返回此字段
					status	object	用户的最近一条微博信息字段 详细
					allow_all_comment	boolean	是否允许所有人对我的微博进行评论，true：是，false：否
					avatar_large	string	用户头像地址（大图），180×180像素
					avatar_hd	string	用户头像地址（高清），高清头像原图
					verified_reason	string	认证原因
					follow_me	boolean	该用户是否关注当前登录用户，true：是，false：否
					online_status	int	用户的在线状态，0：不在线、1：在线
					bi_followers_count	int	用户的互粉数
					lang	string	用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
				*/
				session('sdk_img',$data['profile_image_url']);
				session('sdk_nickname',$data['screen_name']);
				break;
			default:
				;
		}
	}
	/**
	 * 个人中心绑定账号
	 */
	public function bind(){
		if(!session('sdk_openid')){
			$this->error('请用第三方登录',U('login'));
		}
		if(!IS_POST){
			$data['type'] = $this->get_sdk_type(session('sdk_openid'));
			$this->assign($data);
			return $this->display();
		}
		$post = I('post.');
		$map['mobile'] = $post['account'];
		$map['is_del'] = 0;
		$info=get_info($this->table,$map);
		/*判断是否查询到相关信息，如果$info为空则表示用户不存在或密码错误*/
		if(!$info['id']){
			$this->error('账号不存在');
		}
		$post['password'] = md5(md5($post['password']).$info['salt']);
		if($post['password'] !== $info['password']){
			$this->error('密码错误');
		}
		/*判断帐号是否被禁用*/
		if($info['is_hid']==1){
			$this->error("账号已被冻结，不允许登录");
		}
		$openid = session('sdk_openid');
		$data[$openid['type']] = $openid['openid'];
		$data['id'] = $info['id'];
		$res=update_data($this->table, array(), array(),$data);
		if(is_numeric($res)){
			session('sdk_openid',null);
			session("member_id",$info['id']);
			session("member_info",$info);
			$this->success('登录成功！',U('User/Index/Index/index'));
		}else{
			$this->error('登录失败！');
		}
	}
	
/**
 * 判断第三方绑定名称
 * @param 第三方返回的标识 $type
 * @return string
 */
	private function get_sdk_type($type){
		switch($type){
			case 'weixin': 
				return '微信';
			case 'qq': 
				return 	'QQ';
			case 'sina': 
				return '微博';
			case 'wechat': 
				return '微信';
			default:
				return '';
		}
	}
	/**
	 * 清空第三方SESSION
	 */
	private function clean_sdk_session(){
		session('sdk_img','');
		session('sdk_nickname','');
		session('sdk_openid','');
		session("sdk_access", '');
	}
}
