<?php
	/**
	 * 前后台配置
	 * @package 
	 */
$SITE_URL = "http://test.cnsunrun.com/bf/dev";/*TODO:开发地址*/
	 /*TODO:上线配置*/
//define('URL_CALLBACK', "" . $SITE_URL . "/User/Account/Login/callback?type=");
//define('URL_CALLBACK_1', "");

	 /*TODO:测试配置，上线删除*/
define('URL_CALLBACK', "http://test.cnsunrun.com/bf/dev/Open/Login/Index/callback?type=");
define('URL_CALLBACK_1', "" . $SITE_URL . "/User/Account/Login/callback?type=");

/** APPID：绑定支付的APPID（必须配置，开户邮件中可查看） */
define('WX_APPID', 'wx0008eb3738042cc7');
/** MCHID：商户号（必须配置，开户邮件中可查看） */
define('WX_MCHID', '1493988612');
/** KEY：商户支付密钥，参考开户邮件设置（必须配置，登录商户平台自行设置） */
define('WX_KEY', '9smhsrhycn6x15jav5l4iwfqs73r30mq');
/** APPSECRET：公众帐号secert */
define('WX_APPSECRET', '48ff947a048b5a875abd115c4ff8edea');

return array(
	/* 多语言配置 */
	'LANG_SWITCH_ON'   => true,
	'LANG_AUTO_DETECT' => true, // 自动侦测语言 开启多语言功能后有效
	'DEFAULT_LANG'     => 'zh-cn', // 默认语言
	'LANG_LIST'        => 'zh-cn,en-us', // 允许切换的语言列表 用逗号分隔
	'VAR_LANGUAGE'     => 'l', // 默认语言切换变量
	/* 日志设置 */
	'LOG_RECORD' => true, // 默认不记录日志
	'LOG_LEVEL' => 'EMERG,ALERT,CRIT,ERR,WARN,NOTIC,INFO,DEBUG,SQL', // 允许记录的日志级别
	'LOG_EXCEPTION_RECORD' => true, // 是否记录异常信息日志
	/*扩展函数引入*/
	'LOAD_EXT_FILE' => 'image,extend',
	/* 模板相关配置 */
	'TMPL_PARSE_STRING' => array(
		'__STATIC__'	=> __ROOT__ . '/Public/Static',
		'__IMG__'		=> __ROOT__ . '/Public/Home/img',
		'__CSS__'		=> __ROOT__ . '/Public/Home/css',
		'__JS__'		=> __ROOT__ . '/Public/Home/js',
		'__PLUGIN__'	=> __ROOT__ . '/Public/Plugins',
		'__SELLER__'	=> __ROOT__ . '/Public/Seller',
		'__USER__'	    => __ROOT__ . '/Public/User',
	),
	
	// 加载扩展配置文件
	'LOAD_EXT_CONFIG' => 'db',
	'SESSION_PREFIX' => 'front_',
	// '配置项'=>'配置值'
	
	'SHOW_PAGE_TRACE' => false,
	
	/* 数据缓存设置 */
	'DATA_CACHE_PREFIX' => 'sr_', // 缓存前缀
	'DATA_CACHE_TYPE' => 'File', // 数据缓存类型,支持:File|Db|Apc|Memcache|Shmop|Sqlite|Xcache|Apachenote|Eaccelerator
	
	'CONTROLLER_LEVEL' => 2, // 设置1级目录的控制器层
	'MODULE_ALLOW_LIST'=>array('Home','Open','User','Backend','Api'),
	
	'DEFAULT_MODULE' => 'Home',
	'DEFAULT_CONTROLLER' => 'Index/Index',
	/* URL配置 */
	'URL_CASE_INSENSITIVE' => true, // 默认false 表示URL区分大小写 true则表示不区分大小写
	'URL_MODEL' => 2, // URL模式
	'VAR_URL_PARAMS' => '', // PATHINFO URL参数变量
	'URL_PATHINFO_DEPR' => '/', // PATHINFO URL分割符
	                               
	//'TMPL_ACTION_ERROR' => 'Application/Home/View/Status/error.html', // 默认错误跳转对应的模板文件
	'TMPL_ACTION_SUCCESS' => 'Application/Home/View/Status/success.html', // 默认成功跳转对应的模板文件
	//'TMPL_EXCEPTION_FILE' => 'Application/Home/View/Status/exception.html',// 异常页面的模板文件
	
	/* 图片上传相关配置 */
	'IMG_UPLOAD' => array(
		'mimes' => '', // 允许上传的文件MiMe类型
		'maxSize' => 20 * 1024 * 1024, // 上传的文件大小限制 (0-不做限制)
		'exts' => 'jpg,gif,png,jpeg,bmp', // 允许上传的文件后缀
		'autoSub' => true, // 自动子目录保存文件
		'subName' => '', // 子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
		'rootPath' => './Uploads/ImgTemp/', // 保存根路径
		'savePath' => '', // 保存路径
		'saveName' => array('uniqid','' ), // 上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
		'saveExt' => '', // 文件保存后缀，空则使用原后缀
		'replace' => false, // 存在同名是否覆盖
		'hash' => true, // 是否生成hash编码
		'callback' => false 
	), // 检测文件是否存在回调函数，如果存在返回文件信息数组
		
	/* 文件上传相关配置 */
	'FILE_UPLOAD' => array(
		'mimes' => '', // 允许上传的文件MiMe类型
		'exts' => 'jpg,jpeg,bmp,png,rar,zip,7z,doc,docx,rtf,txt', // 允许上传的文件后缀
		'autoSub' => true, // 自动子目录保存文件
		'subName' => '', // 子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
		'rootPath' => './Uploads/FileTemp/', // 保存根路径
		'savePath' => '', // 保存路径
		'saveName' => array('uniqid',''), // 上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
		'saveExt' => '', // 文件保存后缀，空则使用原后缀
		'replace' => false, // 存在同名是否覆盖
		'hash' => true, // 是否生成hash编码
		'callback' => false 
	), // 检测文件是否存在回调函数，如果存在返回文件信息数组
	
	/*支付接口参数配置*/
	'payment' => array(
		/*支付宝配置*/
		'alipay' => array(
			'app_id'	=> '2017090608580337',
			// 'email' => 'gaochaowangluo@sina.cn',			 /* 收款账号邮箱*/
			// 'key' => '9smhsrhycn6x15jav5l4iwfqs73r30mq',	 /*加密key，开通支付宝账户后给予*/
			// 'partner' => '2088721874732931',				 /* 合作者ID，支付宝有该配置，开通易宝账户后给予*/
			// 'ali_public_key_path'=> CONF_PATH.'Certs/alipay_public_key.pem'
			'email' => 'gaochaowangluo@sina.cn',					 /* 收款账号邮箱*/
			'key' => '',	 								 /*加密key，开通支付宝账户后给予*/
			'partner' => '2088721874732931',				 /* 合作者ID，支付宝有该配置，开通易宝账户后给予*/
			'ali_public_key_path'=> CONF_PATH.'Certs/alipay_public_key.pem',
			'ali_private_key_path'=> CONF_PATH.'Certs/alipay_private_key.pem',
		),
		/* 微信支付配置 */
		'wxpay'=>array(		/* 尚软 */
			'appid'=>WX_APPID,										/* 绑定支付的APPID（必须配置，开户邮件中可查看） */
			'mchid'=>WX_MCHID,										/* 商户号（必须配置，开户邮件中可查看） */
			'key'=>WX_KEY,											/* 商户支付密钥，参考开户邮件设置（必须配置，登录商户平台自行设置）;设置地址：https://pay.weixin.qq.com/index.php/account/api_cert */
			'appsecret'=>WX_APPSECRET,								/* APPSECRET：公众帐号secert（仅JSAPI支付的时候需要配置， 登录公众平台，进入开发者中心可设置），获取地址：https://mp.weixin.qq.com/advanced/advanced?action=dev&t=advanced/dev&token=2005451881&lang=zh_CN */
		),
		/* 京东支付配置 */
		'jdpay'=>array(
			//'merchant'=>'110239072003',							/* 商户开通的商户号 */
			//'desKey'=>'06Tvhs5ATBBUcMGwOOOdnoqMheDZQENe',			/* 商户DES密钥 */
			'merchant'=>'110244889002',								/* 商户开通的商户号 */
			'desKey'=>'vBaMGgTpI7mMAUr+ohzf2Y/+cMRUTKuw',			/* 商户DES密钥 */
		),
		/* 银盛支付配置 */
		'cspay'=>array(		/* 尚软 */
			'merchant_no'=>'543006245508110',										/* 商户开通的商户号 */
		)
	),
	'URL_ROUTER_ON' => true,
	'URL_ROUTE_RULES' => array(
		'/^login/' => 'Login/lists',
	),
	//腾讯QQ登录配置
	'THINK_SDK_QQ' => array(
		'APP_KEY' => '101357605', //应用注册成功后分配的 APP ID
		'APP_SECRET' => '15186e5c277448a547c53fc32a5d05b1', //应用注册成功后分配的KEY
		'CALLBACK' => URL_CALLBACK . 'qq&url=' . URL_CALLBACK_1 . 'qq',
	),
	//新浪微博配置
	'THINK_SDK_SINA' => array(
		'APP_KEY' => '3952955463', //应用注册成功后分配的 APP ID
		'APP_SECRET' => 'f7d2640cdd494080563d82d421771d72', //应用注册成功后分配的KEY
		'CALLBACK' => URL_CALLBACK . 'sina&url=' . URL_CALLBACK_1 . 'sina',
	),
	//人人网配置
	'THINK_SDK_RENREN' => array(
		'APP_KEY' => '', //应用注册成功后分配的 APP ID
		'APP_SECRET' => '', //应用注册成功后分配的KEY
		'CALLBACK' => URL_CALLBACK . 'renren&url=' . URL_CALLBACK_1 . 'renren',
	),
	//微信登录
	'THINK_SDK_WEIXIN' => array(
		'APP_KEY'    => 'wx5639343bad4b195d', //应用注册成功后分配的 APP ID
		'APP_SECRET' => '2bc87ee0aeea63cba3c34c734fb85250', //应用注册成功后分配的KEY
		'GrantType'   => 'authorization_code',
		'CALLBACK'   => URL_CALLBACK . 'weixin&url=' . URL_CALLBACK_1 . 'weixin',
	),
		//微信公众登录
	'THINK_SDK_WECHAT' => array(
		'APP_KEY' 	=> WX_APPID, //应用注册成功后分配的 APP ID
		'APP_SECRET' => WX_APPSECRET, //应用注册成功后分配的KEY
		'CALLBACK'   => URL_CALLBACK . 'wechat&url=' . URL_CALLBACK_1 . 'wechat',
	),
	//阿里云OSS配置
	'ALI_OSS_CONFIG'		=> array(
		'OSS_ACCESS_ID'		=> '',						// 阿里云oss key_id    		登录阿里云个人中心控制台-> Access Key管理  设置/获取
		'OSS_ACCESS_KEY'	=> '',						// 阿里云oss key_secret  
		'OSS_ENDPOINT'		=> 'oss-XXXXX', 			// 阿里云oss内网域名地址 endpoint 阿里云控制台->对象存储 oss ->新建Bucket->属性->域名管理  【注意:该设置不包括Bucket名称前缀】
		'OSS_TEST_BUCKET'	=> 'xxxx',					// Bucket 名称 
		'OSS_URL'			=> 'xxxx'					// OSS外网域名 对外访问地址 ,可填写单独配置的域名
	),
	'WEIXINPAY_CONFIG'       => array(
        'APPID'              => 'wxfbf5098b45b0b2b0', // 微信支付APPID
        'MCHID'              => '1500260942', // 微信支付MCHID 商户收款账号
        'KEY'                => 'ue7tq7qSiNPNnFdRwwjQXxEwpD959gib', // 微信支付KEY
        'APPSECRET'          => '4d176d00858b6f15f84406ac244e66a7',  //公众帐号secert
        'NOTIFY_URL'         => 'http://www.abincc.cn/gaochao/Api/User/Weixin/notify', // 接收支付状态的连接
        ),

);