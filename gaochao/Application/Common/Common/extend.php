<?php
/**扩展函数库*/
    
    /**
     * 获取微信操作对象
     * @staticvar array $wechat
     * @param type $type
     * @return WechatReceive
     */
    function & load_wechat($type = '') {
        !class_exists('Wechat\Loader', FALSE) && Vendor('Wechat.Loader'); 
        static $wechat = array();
        $index = md5(strtolower($type));
        if (!isset($wechat[$index])) {
            // 从数据库查询配置参数
            $config = M('wechat_config')->where(array('type'=>'1'))->field('token,appid,appsecret,encodingaeskey,mch_id,partnerkey,ssl_cer,ssl_key,qrc_img')->find();
            // 设置SDK的缓存路径
            $config['cachepath'] = CACHE_PATH . 'Weixin/Server/';
            $wechat[$index] = & \Wechat\Loader::get_instance($type, $config);
        }
        return $wechat[$index];
    }
    /**
     * 获取企业号微信操作对象
     * @staticvar array $wechat
     * @param type $type
     * @return WechatReceive
     */
    function & load_company_wechat($type = '') {
        !class_exists('Wechat\Loader', FALSE) && Vendor('Wechat.Loader');
        static $wechat_conpany = array();
        $index = md5(strtolower($type));
        if (!isset($wechat_conpany[$index])) {
            // 从数据库查询配置参数
            $config = M('wechat_config')->where(array('type'=>'2'))->field('token,appid,appsecret,encodingaeskey,mch_id,partnerkey,ssl_cer,ssl_key,qrc_img')->find();
            // 设置SDK的缓存路径
            $config['cachepath'] = CACHE_PATH . 'Weixin/Company/';
            $wechat_conpany[$index] = & \Wechat\Loader::get_instance($type, $config);
        }
        return $wechat_conpany[$index];
    }

    /**
     * 检查字符串长度
     * @param  [string]  $str [字符串]
     * @param  integer $mix [最小长度]
     * @param  integer $max [最大长度]
     * @return [bool]       [在判断范围内返回true 否则返回false]
     */
    function check_str_len($str,$mix = 1,$max = 10)
    {
        if(function_exists('mb_strlen')){
            $len = mb_strlen($str);
        }else{
            preg_match_all("/./us", $str, $match);
            $len = count($match['0']);
        }
        return $len >= $mix && $len <=$max ? $len : false;
    }
    /**
     * 处理文件路径,获取基于网址的绝对路径
     * @return string         是否成功
     * @time 2016-07-4
     * @author 陆龙飞
     */
    function file_url($url){
        if(empty($url)) return '';
        if(filter_var($url,FILTER_VALIDATE_URL)){
            return $url;
        }
        return U('/','',true,true) . $url;
    }
    /**
     * 生成批量插入sql
     * @param     data     二维数组
     * @param     table    表名
     * @author    陆龙飞
     * @date      2016-01-19
     * @return    string
     * */
    
    function addSql($data,$table)
    {
        if(empty($data) || empty($table))  return '';
        $value = implode('`,`',array_keys($data[0]));
        $sql   = 'INSERT INTO `'.$table.'` (`'.$value.'`) VALUES ';
        foreach($data as $val){
            $sql .= "(";
            foreach($val as $k=>$v){
                is_numeric($v)?$sql .= $v : $sql .= "'".$v."'";
                $sql .= ',';
            }
            $sql = rtrim($sql,',');
            $sql .= '),';
        }
        $sql = rtrim($sql,',');
    
        return $sql;
    }
    /**
     * 获取微信模板消息字段
     * @time 2016-11-21
     * @author 陶君行<Silentlytao@outlook.com>
     */
    function find_wx_template($arr)
    {
        if(is_array($arr)){
            $arr = json_encode($arr);
        }
        preg_match_all('/\{\{([\w]*)\.DATA\}\}/', $arr,$m);
        return $m['1'];
    }
		
		/**
	 * L函数的扩展
	 * @param string $key L函数所需KEY,格式：分类[_二级分类[_三级分类...]]_关键字
	 * @param array $set L函数所需VALUE
	 * @return string 翻译后的值
	 */
	function LL($key, $value=null){
		$param = explode('_',$key);
		$type = $param[0];
		switch($type){
			case 'DB': //数据库类型（DB_表名_需翻译字段_数据ID）
				$result = L($key,$value);
				if($result == $key){
					$table = str_replace('-','_',strtolower($param[1]));
					$field = strtolower($param[2]);
					$map = array();
					$map['id'] = $param[3];
					$info = get_info($table,$map,array($field));
					if(empty($info[$field])){
						return null;
					}
					$result = LL($info[$field]);
				}
				break;
			//支持简写转换：LL('%REQUIRE_参数')
			case '%REQUIRE': 
			case '%UNIQUE':
			case '%VALUE':
			case '%ILLEGAL':
			case '%CONFIRM':
				$key = 'TIP_' . str_replace('%','',$type) . '_FIELD';
				$result = LL($key,array('field' => LL('LL_' . $param[1])));
				break;
			case '%CHARACTER':
				$key = 'TIP_' . str_replace('%','',$type) . '_FIELD';
				$result = LL($key,array('field' => $param[1]));
				break;
			default:
				$result = LL_EXT(L($key,$value));
		}
		return $result;
		
	}
	
	/**
	 * 对翻译后语言进行额外替换
	 * @param string $value L函数翻译后的值
	 * @return string 加样式后的值
	 */
	function LL_EXT($value = ''){
		//样式参数替换
		$pattern  =  '/{<(.*)>}(.*){<\/\1>}/iU' ;
		$value = preg_replace_callback($pattern, function($matches){
			return '<span class="' . $matches[1] . '">' . $matches[2] . '</span>';
		}, $value);
		
		//配置参数替换
		$pattern  =  '/{\$CC_(\w*)}/iU' ;
		$value = preg_replace_callback($pattern, function($matches){
			return C($matches[1]);
		},$value);
		return $value;
	}

	/*
	 * 获取语言包分类
	 * @time 2015-05-18
	 * @author	秦晓武  <525249206@qq.com>
	 * */
	function get_language_type(){
		$language_type = array(
			array('id' =>'0', 'title' => '公共', 'path' => (APP_PATH . 'Common/Lang/')),
			array('id' =>'1', 'title' => '前台', 'path' => (APP_PATH . 'Home/Lang/')),
			array('id' =>'2', 'title' => '后台', 'path' => (APP_PATH . 'Backend/Lang/')),
		);
		return $language_type;
	}

    /**
     * 获取对应表的对应字段的状态的信息
     * @param  $table   string  表名
     * @param  $field   string  表字段名称
     * @param  $index   int     状态值
     * @return array|string
     * @author llf 
     */
    function get_table_state($table,$field,$index=null){

        if(empty($table) || empty($field))
        {
            return fasle;
        }
        $type  = array();
        $table = strtolower($table);
        $field = strtolower($field);
        $type  = S('state_'.$table.'_'.$field);
        if(empty($type)) {
            update_state_cache();
            $type  = S('state_'.$table.'_'.$field);
        }
        if($index){
            return $type[$index]['title'];
        }else{
            return $type;
        }
    }

    /**
     * 更新状态缓存操作  将各表的各个字段状态分别缓存 'state_'+ 表名 + '_' + 字段名
     * @param  null
     * @return null
     * @author llf 
     */
    function update_state_cache(){
        
        $table = 'state_map';
        $all   = get_result($table, array('is_del'=>0),'r_value');
        if(empty($all)) return ;
        $temp  = array();
        foreach ($all as $k => $v) {
            $temp[$v['r_table']][$v['r_field']][] = $v;  
        }
        foreach ($temp as $key => $val) {
            foreach ($val as $ks => $vs) {
                S('state_'.$key.'_'.array_keys($val,$vs)[0],array_column($vs,null,'r_value'));
                //列:S('state_capital_record_status')
            }
        }
    }

    /******阿里云oss-s*******/
    /**
     * 实例化阿里云oos
     * @return object 实例化得到的对象
     */
    function new_oss(){
        vendor('Aliyunoss.autoload');
        $config=C('ALI_OSS_CONFIG');
        $oss = new \OSS\OssClient($config['OSS_ACCESS_ID'],$config['OSS_ACCESS_KEY'],$config['OSS_ENDPOINT']);
        return $oss;
    }
     
    /**
     * 上传文件到oss并删除本地文件
     * @param  string $path 文件路径
     * @return bollear      是否上传
     */
    function oss_upload($path,$oss_path=''){

        if(empty($path)){
            return false;
        }
        if(!file_exists($path)){
            return false; //需要上传到OSS的文件不存在
        }
        /* 先统一去除左侧的.或者 再添加 */
        if(empty($oss_path)){
            $oss_path=ltrim($path,'./');
        }
        $path='./'.$path;
        
        /* 上传到oss */
        try {   
            $ossClient = new_oss();
            $ossClient->uploadFile(C('ALI_OSS_CONFIG.OSS_TEST_BUCKET'),$oss_path,$path);
            
        } catch (OssException $e) {
        /*  如需上传到oss后 自动删除本地的文件 则删除下面的注释 */
            unlink($path); 
            // write_log($e->getMessage(),'oss文件上传失败');
            return false;
        }
        //$url = C('ALI_OSS_CONFIG.OSS_ENDPOINT').''.$path; oss文件上传地址
        return true;
    }

    /**
     * 获取完整网络连接  -- 阿里云文件路径
     * @param  string $path 文件路径不带 './'
     * @return string       http连接
     */
    function oss_url($path){
        // 如果是空；返回空
        if (empty($path)) {
            return '';
        }
        // 如果已经有http直接返回
        if (strpos($path, 'http://')!==false) {
            return $path;
        }
        // 获取bucket
        $bucket=C('ALI_OSS_CONFIG.OSS_URL');
        return 'http://'.$bucket.'/'.$path;
    }

    /**
     * 删除oss上指定文件
     * @param  string $path 文件路径 例如删除 /Public/README.md文件  传Public/README.md 即可
     * @return bool   成功/失败
     */
    function oss_del($path){
        
        if(empty($path)) return false;
        try {
            $ossClient = new_oss();
            $ossClient->deleteObject(C('ALI_OSS_CONFIG.OSS_TEST_BUCKET'),ltrim($path,'./'));
        } catch (OssException $e) {
            return false;
        }
        return true;
    }
    
    /**
     * 【客户端直传模式使用】
     * 获取oss上传文件凭证  使用前请先设置 getProfile 方法参数
     * @param format 格式
     */
    function aly_sts($format = null){
     
        vendor('aliyunsts.sts');

        // 你需要操作的资源所在的region，STS服务目前只有杭州节点可以签发Token，签发出的Token在所有Region都可用
        // 只允许子用户使用角色
        $iClientProfile = DefaultProfile::getProfile("cn-hangzhou", "", "");
        $client = new DefaultAcsClient($iClientProfile);

        // 角色资源描述符，在RAM的控制台的资源详情页上可以获取
        $roleArn = "acs:ram::1596549380845555:role/aliyunosstokengeneratorrole";

        // 在扮演角色(AssumeRole)时，可以附加一个授权策略，进一步限制角色的权限；
        // 详情请参考《RAM使用指南》
        // 此授权策略表示读取所有OSS的只读权限
$policy=<<<POLICY
{
  "Statement": [
    {
      "Action": "oss:*",
      "Effect": "Allow",
      "Resource": "*"
    }
  ],
  "Version": "1"
}
POLICY;

        $request = new \Sts\Request\V20150401\AssumeRoleRequest();
        // RoleSessionName即临时身份的会话名称，用于区分不同的临时身份
        // 您可以使用您的客户的ID作为会话名称
        $request->setRoleSessionName("client_name");
        $request->setRoleArn($roleArn);
        $request->setPolicy($policy);
        $request->setDurationSeconds(3600);
        if($format == 'js'){
            $response = $client->doAction($request);
        }else{
            $response = $client->getAcsResponse($request);
            $response = (array)$response;
        }

        return $response;
    }
    /******阿里云oss-e*******/
    /**
     * 文件转存，从临时表转存到正式表
     * @param unknown $md5
     * @param unknown $folder
     * @param unknown $table
     * @param unknown $field_key
     * @param unknown $value_key
     * @param unknown $field_image
     */
    function file_upload($id, $folder, $table, $field_key, $value_key, $field_image = 'image') {
    	/* 创建目录 */
    	$folder = trim($folder, '/').'/'.date('Y-m-d');
    	$_folder = mkdir_chmod($folder);
    	if($_folder == false) return '目录创建失败';
    	/* 查询临时数据 */
    	$info = get_info('file', ['id'=> $id], 'file_name,save_path');
    	if(!$info || !file_exists($info['save_path'])) return false;
    	/* 复制临时文件到正式位置 */
    	$value_image = $folder.'/'.basename(trim($info['save_path']));
    	$result_copy = copy($info['save_path'], $value_image);
    	if($result_copy == false) return false;
    	$_data = [
    		$field_key=> $value_key,
    		$field_image=> $value_image,
    	];
    	$result = update_data($table, [], [], $_data);
    	if(is_numeric($result)) {
    		delete_data('file', ['id'=> $id]);
    		unlink($info['save_path']);
    		return true;
    	}
    		
    	return false;
    }
    /**
     * 文件转存，从临时表转存到正式表
     * @param unknown $md5
     * @param unknown $folder
     * @param unknown $table
     * @param unknown $field_key
     * @param unknown $value_key
     * @param unknown $field_image
     */
    function file_upload_more($ids, $folder, $table, $field_key, $value_key, $field_image = 'image') {
    	/* 创建目录 */
    	$folder = trim($folder, '/').'/'.date('Y-m-d');
    	$_folder = mkdir_chmod($folder);
    	if($_folder == false) return '目录创建失败';
    	/* 查询临时数据 */
    	$list = get_result('file', ['id'=> ['in', $ids]], 'id asc', 'id,file_name,save_path');
    	if($list) {
    		$file_exists = [];
    		$file_temp = [];
    		$file_ids = [];
	    	foreach($list as $v) {
		    	if(!file_exists($v['save_path'])) {
		    		delete_file($file_exists);
		    		return false;
		    	}
		    	/* 复制临时文件到正式位置 */
		    	$value_image = $folder.'/'.basename(trim($v['save_path']));
		    	$result_copy = copy($v['save_path'], $value_image);
		    	if($result_copy == false) return false;
		    	$_data = [
		    		$field_key=> $value_key,
		    		$field_image=> $value_image,
		    	];
		    	$result = update_data($table, [], [], $_data);
		    	if(is_numeric($result)) {
		    		$file_exists[] = $value_image;
		    		$file_temp[] = $v['save_path'];
		    		$file_ids[] = $v['id'];
		    	}else {
		    		delete_file($file_exists);
		    		return false;
		    	}
	    	}
	    	delete_file($file_temp);
	    	delete_data('file', ['id'=> ['in', $file_ids]]);
	    	return true;
    	}
    	return false;
    }
    /**
     * 文件夹创建并且给权限
     * @param string $path 文件夹路径
     * @param string $mode 权限,默认'0777'
     */
    function mkdir_chmod($path, $mode = 0777){
    	if(dir($path)) {
    		return true;
    	}
    	$result = mkdir($path, $mode, true);
    	if($result) {
    		$path_arr = explode('/',$path);
    		$path_str = '';
    		foreach($path_arr as $val){
    			$path_str .= $val.'/';
    			$a = chmod($path_str,$mode);
    		}
    	}
    	return $result;
    }
    /**
     * 删除文件
     * @param unknown $files
     */
    function delete_file($files) {
    	foreach($files as $v) {
    		if(file_exists($v)) unlink($v);
    	}
    	return true;
    }
    /**
     * 生成缩略图
     * $image 传入的图片路径
     * $width 宽度
     * $height 高度
     * $method 缩略方法（3：居中裁剪）
     * $thumb_path 缩略图保存位置
     * $default 没有图片时，默认返回图片
     * @author 许东
     * @time	2016-06-07
     */
    function _thumb($image, $width = 220, $height = 220, $method = 3, $default = 'Uploads/Product/Default/default.png') {
    	if(file_exists($image)) {
    		$prefix = 'T'.$width.'x'.$height.'_';
    		$image = trim($image, '/');
    		$name = basename($image);
    		$thumb_image = dirname($image).'/'.$prefix.$name;
    		if(!file_exists($thumb_image)) {
    			$_thumb = new \Think\Image();
    			$_thumb->open($image);
    			$_thumb->thumb($width, $height, $method)->save($thumb_image, null, 100);
    		}
    		return __ROOT__.'/'.$thumb_image;
    	}else {
    		$default = trim($default, '/');
    		return __ROOT__.'/'.$default;
    	}
    }
    /**
     * 删除缩略图
     * @param unknown $dirName
     * @return boolean
     */
    function delete_thumb_file($dirname = 'Uploads') {
    	if(!file_exists($dirname)) {
    		return false;
    	}
    
    	$dir = opendir($dirname);
    	while($filename = readdir($dir)) {
    		$file = $dirname.'/'.$filename;
    		if($filename != '.' && $filename != '..') {
    			if(is_dir($file)){
    				delete_thumb_file($file);
    			}else {
    				if(strpos($filename, 'T') !== false && strpos($filename, '_') !== false) unlink($file);
    			}
    		}
    	}
    	closedir($dir);
    	return true;
    }

    /**
     * 加密字符串
     * @param  string  $str  '用户id'_'用户最后登录时间'
     * @return string  
     */
    function encrypt_key($str){
        return Think\Crypt::encrypt($str ,'wuye', 0);
    }

    /**
     * 解密字符串
     * @param  string  $str
     * @return string  '110_2017-07-21';
     */
    function decrypt_key($str){
        return Think\Crypt::decrypt($str,'wuye');
    }

    /**
     * 系统加密方法 
     * @time 2017-03-10
     * @author llf 
     * @return string 生成 60位字符串加密密钥 
     */
    function sys_password_encrypt($password){
        return password_hash($password,PASSWORD_BCRYPT,['cost'=>12]);
    }

    /**
     * 系统密码验证 
     * @time 2017-03-10
     * @author llf 
     * @param  $password    密码明文
     * @param  $hash        加密后的密码字符串
     * @return bool         
     */
    function sys_password_verify($password,$hash){
        return password_verify($password,$hash);
    }

    /**
     * 检查时间格式 
     * @return date('Y-m-d H:i:s')
     * @author llf 
     */
    function now_date_time($int=0){
        if($int){
            return date('Y-m-d H:i:s',$int);
        }
        return date('Y-m-d H:i:s');
    }
    
    /**
     * 检查日期格式
     * @param  datetime 
     * @return bool
     * @author llf 
     */
    function check_date($date){
        return ($date === date('Y-m-d',strtotime($date)))? true : false;
    }

    /**
     * 检查时间格式
     * @param  datetime 
     * @return bool
     * @author llf 
     */
    function check_datetime($datetime){
        return ($datetime === date('Y-m-d H:i:s',strtotime($datetime)))? true : false;
    }
    /**
     * 格式化时间戳 - 论坛
     * @param unknown $timestamp
     * @param string $uformat
     */
    function format_time($timestamp, $uformat = '', $dformat = 'Y-m-d', $tformat = 'H:i') {
    	static $time_stamp,$_date;
    	if($time_stamp === null) {
    		$time_stamp = time();
    		$_date = L('date');
    	}
    	$format = $uformat ? $uformat : $dformat.' '.$tformat;
    	$s = date($format, $timestamp);
    	$todaytimestamp = $time_stamp - $time_stamp % 86400;
    	$time = $time_stamp - $timestamp;
    	if($timestamp >= $todaytimestamp) {
    		if($time >= 3600) {
				$return = intval($time / 3600).$_date['hour'].$_date['before'];
			}elseif($time >= 60) {
				$return = intval($time / 60).$_date['min'].$_date['before'];
			}elseif($time > 0) {
				$return = $time.$_date['sec'].$_date['before'];
			}elseif($time == 0) {
				$return = $_date['now'];
			}else {
				$return = $s;
			}
    	}elseif(($days = intval(($todaytimestamp - $timestamp) / 86400)) >= 0 && $days < 7) {
    		if($days == 0) {
    			$return = $_date['yday'].date($tformat, $timestamp);
    		} elseif($days == 1) {
    			$return = $_date['byday'].date($tformat, $timestamp);
    		} else {
    			$return = ($days + 1).$_date['day'].$_date['before'];
    		}
    	}else {
    		$return = $s;
    	}
    	return $return;
    }

    /**
     * 获取用户头像地址
     * @param  member_id    用户ID 
     * @param  size         头像尺寸  S - 60*60  M - 100*100  L - 200*200 
     * @return URL
     * @author llf 
     */
    function get_avatar($member_id,$size='M'){

        if($size !== 'S' || $size !== 'M' || $size !== 'L'){
            $size = 'M';
        }
        $url = 'Uploads/Headimg' . file_savename($member_id) . '/H_' . $member_id . '_'. $size . '.jpg';
        if(!file_exists($url)){
            $url = 'Public/Static/img/avatar_'.$size.'.jpg';
        }

        return file_url($url).'?time='.time();
    }

    function ck_get_avatar($member_id,$size='M'){

        if($size !== 'S' || $size !== 'M' || $size !== 'L'){
            $size = 'M';
        }
        $url = 'Uploads/CkHeadimg' . file_savename($member_id) . '/H_' . $member_id . '_'. $size . '.jpg';
        if(!file_exists($url)){
            $url = 'Public/Static/img/avatar_'.$size.'.jpg';
        }

        return file_url($url).'?time='.time();
    }

    /**
     * 获取广告缓存
     */
    function get_adspace_cache($adspace_id){
    
        $temp    = get_no_hid('banner');
        $adspace = array();
        if(!empty($temp)){
            foreach ($temp as $k => $v) {
                $adspace[$v['adspace_id']][] = $v;
            }
        }
        if($adspace_id){
            return $adspace[$adspace_id];
        }
        return $adspace;
    }

   /**
    * 验证是否是手机号
    * @static
    * @access public
    * @param  string/int    $mobile 需要转换的字符串
    * @return bool
    * @author 陆龙飞
    * @date   2015-10-20
    */
    function is_mobile($mobile){
        $mobile_str  = strval($mobile);
        return  preg_match(MOBILE,$mobile_str);
    }

   /**
    * 验证是否是手机号
    * @static
    * @access public
    * @param  string/int    $mobile 需要转换的字符串
    * @return bool
    * @author 陆龙飞
    * @date   2015-10-20
    */
    function is_email($email){
        $email_str  = strval($email);
        return  preg_match(EMAIL,$email_str);
    }
    
    /**
     * 小区ID解析
     * APP那边传的是 cloud_id 业务处理需要将其 解析为对应的 district_id
     * @param   
     * @param   item        string  名称
     * @author  llf 2017-06-19
     */
    function cloud_id_parser($cloud_id){
        
        $cloud_id       = intval($cloud_id);
        $district_list  = get_no_del('district');
        $district_list  = array_column($district_list,null,'cloud_id');   

        return  intval($district_list[$cloud_id]['id']);
    }
 
     /**
     * 友好的数据显示信息
     * @param $data 需要展示的数据 ，默认使用print_r()
     * @param $dump 是否用TP的dump打印数据       0=>不使用      1=>使用
     * @param $flag 是否设置断点                              0=>不设置      1=>设置
     * @time 2016-9-10
     * @author 陶君行<Silentlytao@outlook.com>
     */
    function data_show($data,$dump = 0,$flag = 0){
        header("Content-type: text/html; charset=utf-8");
        /**定义样式,源于bootstarp*/
        echo '<pre style="display: block;padding: 9.5px;margin: 44px 0 0 0;
                    font-size: 13px;line-height: 1.42857;color: #333;
                    word-break: break-all;word-wrap: break-word;background-color: #F5F5F5;
                    border: 1px solid #CCC;border-radius: 4px;">';
        /** 如果是boolean或者null直接显示文字；否则print */
        if (is_bool($data)) {
            $show_data=$data ? 'true' : 'false';
        }elseif (is_null($data)) {
            $show_data='null';
        }else{
            $show_data= $dump ?  var_dump($data)   :   print_r($data,true) ;
        }
        echo $show_data;
        echo '</pre>';
        if($flag) die();
    }

    //获取业主邀请码
    function get_register_code($district_id){
        $district_id = intval($district_id);
        $yz_count    = M('Member')->where(['district_id'=>$district_id,'type'=>2])->max('id');
        $code        = 'yz'.rand(1,9).rand(1,9).rand(1,9).rand(1,9).rand(1,9).rand(1,9); 
        $has         = M('Member')->where(['register_code'=>$code])->find('id');
        if($has){
            get_register_code($district_id);
        }
        return  $code; 
    }