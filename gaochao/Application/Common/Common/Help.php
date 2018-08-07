<?php 
/**
 * 自定义助手类
 * @time 2016-11-11
 * @author 陶君行<Silentlytao@outlook.com>
 */
class Help {
    /**
     * 缓存位置
     * @var type
     */
    static public $cachepath;
    /**
     * 友好的数据显示信息
     * @param $data 需要展示的数据 ，默认使用print_r()
     * @param $dump 是否用TP的dump打印数据       0=>不使用      1=>使用
     * @param $flag 是否设置断点                              0=>不设置      1=>设置
     * @time 2016-9-10
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function show($data,$dump = 0,$flag = 0){
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
    /**
     * CURL请求页面
     */
    static public function curl_info($url,$params,$method = 'GET',$header = array(),$multi = false)
    {
        /**检测是否是完成URL*/
        if(!filter_var($url,FILTER_VALIDATE_URL)){
            //$url = U($url,'',true,true);
            // $temp = 'http://127.0.0.1';
            $url = $temp . U($url);
        }
        $data = json_decode(self::http($url, $params,$method,$header,$multi),true);
        return $data;
    }
    /**
     * 发送HTTP请求方法，目前只支持CURL发送请求
     * @param  string $url    请求URL
     * @param  array  $params 请求参数
     * @param  string $method 请求方法GET/POST
     * @return array  $data   响应数据
     * @time 2016-9-5
     * @author THINKPHP
     */
    static public function http($url, $params, $method = 'GET', $header = array(), $multi = false){
        $opts = array(
            CURLOPT_TIMEOUT        => 30,
            CURLOPT_RETURNTRANSFER => 1,
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_SSL_VERIFYHOST => false,
            CURLOPT_HTTPHEADER     => $header
        );
    
        /** 根据请求类型设置特定参数 */
        switch(strtoupper($method)){
            case 'GET':
                $opts[CURLOPT_URL] = $url . '?' . http_build_query($params);
                break;
            case 'POST':
                //判断是否传输文件
                $params = $multi ? $params : http_build_query($params);
                $opts[CURLOPT_URL] = $url;
                $opts[CURLOPT_POST] = 1;
                $opts[CURLOPT_POSTFIELDS] = $params;
                break;
            case 'DEL':
                $opts[CURLOPT_URL] = $url;
                $opts[CURLOPT_CUSTOMREQUEST] = 'DELETE';
                break;
            default:
                throw new Exception('不支持的请求方式！');
        }
        /** 初始化并执行curl请求 */
        $ch = curl_init();
        curl_setopt_array($ch, $opts);
        $data  = curl_exec($ch);
        $error = curl_error($ch);
        curl_close($ch);
        if($error) throw new Exception('请求发生错误：' . $error);
        return  $data;
    }
    /**
     * 生成缓存RUNTIME/Weixin/目录下
     * 设置缓存
     * @param type $name    缓存名字
     * @param type $value   缓存值
     * @param type $expired 缓存有效时间
     * @return type
     * @time 2016-11-3
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function create_cache($name, $value, $expired = 0)
    {
        $data = serialize(array('value' => $value, 'expired' => $expired > 0 ? time() + $expired : 0));
        return self::check() && file_put_contents(self::$cachepath . $name . '.json', $data);
    }
    /**
     * 读取缓存RUNTIME/Weixin/目录下
     * @param type $name    缓存名称
     * @time 2016-11-3
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function get_cache($name)
    {
        if (self::check() && ($file = self::$cachepath . $name . '.json') && file_exists($file) && ($data = file_get_contents($file)) && !empty($data)) {
            $data = unserialize($data);
            if (isset($data['expired']) && ($data['expired'] > time() || $data['expired'] === 0)) {
                return isset($data['value']) ? $data['value'] : null;
            }
        }
        return null;
    }
    /**
     * 删除缓存RUNTIME/Weixin/目录下
     * @param type $name    缓存名称
     * @time 2016-11-3
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function del_cache($name)
    {
        return self::check() && unlink(self::$cachepath . $name . '.json');
    }
    /**
     * 输出内容到日志
     * @param type $line
     * @param type $filename
     * @return type
     */
    static public function put($line, $filename = '') {
        empty($filename) && $filename = date('Ymd') . '.log';
        return self::check() && file_put_contents(self::$cachepath . $filename . '.json', '[' . date('Y/m/d H:i:s') . "] {$line}\n", FILE_APPEND);
    }
    /**
     * 转换真实路径
     * @time 2016-9-22
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function get_true_path($file)
    {
        $file_path = getcwd() . '/' . ltrim($file,'/');
        /**linux路径转换*/
        if(DIRECTORY_SEPARATOR == '/'){
            $file_path = str_replace('\\', '/', $file_path);
        }
        return $file_path;
    }
    /**
     * 生成随机GUID码
     * @example b83ecb52-dda5-4063-b21b-a9659be79186
     * @return [string] [36位的GUID值]
     */
    static public function create_guid()
    {
        $charid = strtolower(md5(uniqid(mt_rand(), true)));
        $hyphen = chr(45);// "-"
        $uuid = substr($charid, 0, 8).$hyphen
                .substr($charid, 8, 4).$hyphen
                .substr($charid,12, 4).$hyphen
                .substr($charid,16, 4).$hyphen
                .substr($charid,20,12);
        return $uuid;
    }
    /**
     * 根据传递的值，生成固定GUID码
     * @time 2016-11-11
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function create_static_guid($str)
    {
        $charid = strtolower(md5($str));
        $hyphen = chr(45);// "-"
        $uuid = substr($charid, 0, 8).$hyphen
                .substr($charid, 8, 4).$hyphen
                .substr($charid,12, 4).$hyphen
                .substr($charid,16, 4).$hyphen
                .substr($charid,20,12);
        return $uuid;
    }
    /**
     * 字符串隐藏
     * @param   $[str] [<字符串>]
     * @param   $[start] [<起始位置>]
     * @param   $[len] [<隐藏的长度>]
     * @param   $[show] [<显示的字符>]
     * @time 2016-11-11
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function hidden_str($str, $start = 3, $len = 4 ,$show = '*')
    {
        /** 如果是邮箱，那么就隐藏@符号前面的字符串 */
        if(filter_var($str,FILTER_VALIDATE_EMAIL)){
            $str_arr = explode("@",$str);
            $str = $str_arr['0'];
            /** 如果起始位置大于邮箱长度，那么就从第一位开始截取 */
            $length = strlen($str);
            if($length <= $start)    $start = 1;
            /** 如果没有设置$len,那么就默认截取到@之前 */
            if($len == 4)   $len = $length - 1;
            $change_str = str_pad($show,$len,$show,STR_PAD_BOTH);
            $str = substr_replace($str,$change_str,$start,$len);
            return $str . $str_arr['1'];
        }
        $change_str = str_pad($show,$len,$show,STR_PAD_BOTH);
        return substr_replace($str,$change_str,$start,$len);
    }
    /**
     * 检查字符串长度
     * @param  [string]  $str [字符串]
     * @param  integer $mix [最小长度]
     * @param  integer $max [最大长度]
     * @return [bool]       [在判断范围内返回true 否则返回false]
     */
    static public function check_str_len($str,$mix = 1,$max = 10)
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
     * 时间戳转换 刚刚、几分钟、几小时、昨天、前天、
     * @param int $time
     * @return string
     */
    static public function transfer_time($time)
    {
        $rtime = date("Y-m-d",$time);
        $htime = date("H:i",$time);
    
        $time = time() - $time;
    
        if ($time < 60)
        {
            $str = '刚刚';
        }
        elseif ($time < 60 * 60)
        {
            $min = floor($time/60);
            $str = $min.'分钟前';
        }
        elseif ($time < 60 * 60 * 24)
        {
            //          $h = floor($time/(60*60));
            //          $str = $h.'小时前 '.$htime;
            $str = '今天' . $htime;
        }
        elseif ($time < 60 * 60 * 24 * 3)
        {
            $d = floor($time/(60*60*24));
            if($d==1){
                $str = '昨天 '.$htime;
            }else{
                $str = '前天 '.$htime;
            }
        }
        else
        {
            $str = $rtime;
        }
        return $str;
    }
    /**
     * 传入图片地址,得到图片的Base64编码
     * @param  [string] $img_file [图片路径 Uploads/avate/2.jpg]
     * @return [string]           [Base64编码]
     */
    static public function img_base64($img_file,$flag = true){
        $img_base64 = '';
        $app_img_file = self::get_true_path($img_file);                 //组合出真实的绝对路径
        $fp = fopen($app_img_file,"r");                     //图片是否可读权限
        if($fp){
            $img_base64 = chunk_split(base64_encode(fread($fp,filesize($app_img_file))));//base64编码
            fclose($fp);
        }
        /** 如果为true,就给出base64图片的头信息*/
        if($flag){
            $img_info = getimagesize($app_img_file);            //取得图片的大小，类型等
            switch($img_info[2]){           //判读图片类型
                case 1:$img_type="gif";break;
                case 2:$img_type="jpg";break;
                case 3:$img_type="png";break;
            }
            $img_base64 = 'data:image/'.$img_type.';base64,'. $img_base64;
        }
        return $img_base64;         //返回图片的base64
    }
    /**
     * 导出EXCEL
     * @param   $[config]
     * [格式：$config = array(
     *              array('title'    =>'姓名','name'     =>'username','size'     =>15,'callback' =>''),
     array('title'    =>'性别','name'     =>'sex','size'     =>10,'callback' =>''),
     *              );
     * $config里面的字段值(username),应该是$data里面显示的字段值
     * @param $data array()  获取的二维数组（不能是树形结构）
     * @time 2016-10-10
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function create_excel($config,$data)
    {
        /** 实例化PHPExcel对象，并配置文件基本信息*/
        vendor("PHPEXCEL.PHPExcel");
        $objPHPExcel=new \PHPExcel();
        $objPHPExcel->getProperties()->setCreator("sunrun")
        ->setLastModifiedBy("sunrun")
        ->setKeywords("sunrun")
        ->setCategory("zstyle");
        /**设置保存的表名*/
        $data['sheetName'] = $data['sheetName'] ? $data['sheetName'] : 'sheet1';
        /**总页数集合*/
        $cellName = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','AA','AB','AC','AD','AE','AF','AG','AH','AI','AJ','AK','AL','AM','AN','AO','AP','AQ','AR','AS','AT','AU','AV','AW','AX','AY','AZ');
        /** 统计工作表展示的列数*/
        $count_config = count($config);
        /**设置操作的工作表*/
        $objPHPExcel->setActiveSheetIndex('0');
        /**设置工作表的名字*/
        $objPHPExcel->getActiveSheet()->setTitle($data['sheetName']);
    
        for($i = 0; $i< $count_config;$i++){
            /**设置工作表的第一栏标题*/
            $objPHPExcel->setActiveSheetIndex('0')->setCellValue($cellName[$i] . '1',$config[$i]['title']);
            /**设置表格行高*/
            $objPHPExcel->getActiveSheet()->getColumnDimension($cellName[$i])->setWidth($config[$i]['size']);
        }
        // $objPHPExcel->getActiveSheet()->getColumnDimension()->setAutoSize(true);
        /**设置列的对齐方式*/
        //$objPHPExcel->getActiveSheet()->getStyle(C)->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_RIGHT);
        /**设置列的输出格式*/
        //$objPHPExcel->getActiveSheet()->getStyle(A)->getNumberFormat()->setFormatCode(\PHPExcel_Style_NumberFormat::FORMAT_TEXT);
        $i = 2;
        foreach($data['result'] as $key => $item){
            /**设置单个元的值*/
            for($j = 0 ; $j< $count_config;$j++){
                $objPHPExcel->getActiveSheet()->setCellValue($cellName[$j] . $i, $item[$config[$j]['name']]);
            }
            $i ++;
        }
        /**导出EXCEL*/
        $outputFileName = $data['sheetName'] . '.xls';
        $xlsWriter=new \PHPExcel_Writer_Excel5 ($objPHPExcel);
        header("Content-Type: application/force-download");
        header("Content-Type: application/octet-stream");
        header("Content-Type: application/download");
        header('Content-Disposition:inline;filename="'.$outputFileName.'"');
        header('Cache-Control: max-age=0');
        header("Pragma: no-cache");
        $xlsWriter->save("php://output");
        exit;
    }
    /**
     * 导入EXCEL
     * @param   $[config]
     * [格式：$config = array(
     *              ['昵称','nickname','15'],
     *              ['联系方式','mobile','20'],
     *              );
     * @param $file_path 文件路径
     * @param $method [<回调处理函数>]
     * @time 2016-10-10
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function load_excel($config,$file_path = '')
    {
        /** 引入PHPEXCEL类*/
        vendor("PHPEXCEL.PHPExcel");
        /**总页数集合*/
        /**xls、xlsx格式都可导入 */
        $objPHPRead =  PHPExcel_IOFactory::createReaderForFile($file_path);
        /**忽略单元格格式*/
        $objPHPRead->setReadDataOnly(true);
        /**实例化PHPExcel对象*/
        $objPHPExcel = $objPHPRead->load($file_path);
        /** 获取当前工作表*/
        $objWorksheet = $objPHPExcel->getActiveSheet();
        /**获取行数*/
        $highestRow = $objWorksheet->getHighestRow();
        /**获取最后一列的列数*/
        $highestColumn = $objWorksheet->getHighestColumn();
        /**获取最后一列对应的列数 数字*/
        $highestColumnIndex = PHPExcel_Cell::columnIndexFromString($highestColumn);
    
        $excelData = array();
        for ($row = 2; $row <= $highestRow; $row++) {
            for ($col = 0; $col < $highestColumnIndex; $col++) {
                if($config[$col]['callback']){
                    $excelData[$row][$config[$col]['name']] = $config[$col]['callback']((string)$objWorksheet->getCellByColumnAndRow($col, $row)->getValue());
                }else{
                    $excelData[$row][$config[$col]['name']] = (string)$objWorksheet->getCellByColumnAndRow($col, $row)->getValue();
                }
    
            }
        }
        if(empty($excelData) || count($excelData['2']) !== count($config)){
            return false;
        }
        return array_values($excelData);
    }
    /**
     * 下载文件
     * @param string    $file_dir   文件绝对路径
     * @param string    $save_name  下载保存的文件名
     * @time 2016-11-11
     * @author 陶君行<Silentlytao@outlook.com>
     */
    static public function down($file_dir,$save_name)
    {
        /** 转换绝对路径*/
        $file_dir = self::get_true_path($file_dir);
        if(!is_file($file_dir)){
            /** 返回之前的URL*/
            echo"<script>alert('文件不存在');history.go(-1);</script>";
            exit;
        }
        /** 修改保存文件名*/
        if(empty($save_name)){
            $save_name = basename($file_dir);
        }else{
            $last_name = pathinfo($file_dir, PATHINFO_EXTENSION | PATHINFO_FILENAME);
            $save_name = $save_name.'.'.$last_name;
        }
        //header("Content-Type: application/force-download");
        header('Content-Transfer-Encoding: binary');
        header('Content-Length: '. filesize($file_dir));
        header('Content-Type: application/octet-stream');
        //header("Content-Type: application/download");
        if (preg_match ( '/MSIE/', $_SERVER ['HTTP_USER_AGENT'] )) { // for IE
				header ( 'Content-Disposition: attachment; filename="' . rawurlencode ( $save_name ) . '"' );
			} else {
				header ( 'Content-Disposition: attachment; filename="' . $save_name . '"' );
			}
        //header('Cache-Control: max-age=0');
        header('Pragma: no-cache');
        readfile($file_dir);
    }
    /**
     * 数组键名替换，无限级
     * @param array | string $old 待替换数组
     * @param array $new 替换后数组
     * @param array $change 新老键名对数组array('old_key_1'=>'new_key_1',...)
     * @return 引用传递，直接调用第二个参数$new即可
     * @time 2016-11-11
     * @author 秦晓武
     */
    static public function change_key(&$old,&$new,&$change){
        if(!is_array($old)){
            return $new = $old;
        }
        foreach($old as $k => $v){
            $k_1 = !isset($change[$k]) ? $k : $change[$k];
            self::change_key($v,$new[$k_1],$change);
        }
    }
    /**
     * 多维数组取值
     * @param   $arr    要查询的数组
     * @param   $key    要查询的键值
     * @param   $val    要查询的值
     * @example   $arr = array(
     *                  '0' => array(
     *                          'id' =>'1',
     *                          'value' =>'123',
     *                         ),
     *                  '1' => array(
     *                          'id' =>'2',
     *                          'value' =>'1234',
     *                         ),
     *                  '2' => array(
     *                          'id' =>'3',
     *                          'value' =>'12345',
     *                         ),
     *              );
     *              print_r(find_arr($arr,'id','2'));
     *              结果是  Array(
     *                           [0] => Array(
     *                                   [id] => 2
     *                                   [value] => 1234
     *                                  )
     *                          )
     * @time 2016-6-30
     * @author  陶君行
     */
    static public function find_arr($arr = array(),$key,$val){
        $res = array();
        $str = json_encode($arr,JSON_UNESCAPED_UNICODE);
        preg_match_all("/\{[^\{]*\"".$key."\"\:\"".$val."\"[^\}]*\}/",$str, $m);
        if($m && $m[0]){
            foreach($m[0] as $val) $res[] = json_decode($val,true);
        }
        return $res;
    }
    
    
    
    
    
    
    /**
     * 检查缓存目录
     * @return boolean
     */
    static protected function check() {
        /** 根目录Runtime下面的weixin文件夹*/
        empty(self::$cachepath) && self::$cachepath = getcwd() . DIRECTORY_SEPARATOR . 'Runtime' . DIRECTORY_SEPARATOR . 'Weixin' . DIRECTORY_SEPARATOR;
        self::$cachepath = rtrim(self::$cachepath, '/\\') . DIRECTORY_SEPARATOR;
        if (!is_dir(self::$cachepath) && !mkdir(self::$cachepath, 0755, TRUE)) {
            return FALSE;
        }
        return TRUE;
    }
    /***************************私有方法 -S********************************/
    
    /***************************私有方法 -E********************************/
}
/**
 * RSA加密,解密类
 */
class Rsa{
    private static $PRIVATE_KEY;
    private static $PUBLIC_KEY;
    function __construct($pubKey = '', $privKey = '') {
        self::$PUBLIC_KEY = $pubKey;
        self::$PRIVATE_KEY = $privKey;
    }

    /**
     * Decode a string with URL-safe Base64.
     *
     * @param string $input A Base64 encoded string
     *
     * @return string A decoded string
     */
    public static function urlsafeB64Decode($input)
    {
        $remainder = strlen($input) % 4;
        if ($remainder) {
            $padlen = 4 - $remainder;
            $input .= str_repeat('=', $padlen);
        }
        return base64_decode(strtr($input, '-_', '+/'));
    }

    /**
     * Encode a string with URL-safe Base64.
     *
     * @param string $input The string you want encoded
     *
     * @return string The base64 encode of what you passed in
     */
    public static function urlsafeB64Encode($input)
    {
        return base64_encode($input);
        //return str_replace('=', '', strtr(base64_encode($input), '+/', '-_'));
    }

    /**
    *返回对应的私钥(内部类调用)
    */
    private static function getPrivateKey(){
        $privKey = self::$PRIVATE_KEY;
        return openssl_pkey_get_private($privKey);
    }

    /**
    *返回对应的公钥(内部类调用)
    */
    private static function getPublicKey(){
        $pubKey = self::$PUBLIC_KEY;
        return openssl_pkey_get_public($pubKey);
    }
 
    /**
     * 私钥加密
     */
    public static function privEncrypt($data)
    {
        if(!is_string($data)){
            return null;
        }
        return openssl_private_encrypt($data,$encrypted,self::getPrivateKey())? self::urlsafeB64Encode($encrypted) : null;
    }
    
    /**
     * 私钥解密
     */
    public static function privDecrypt($encrypted)
    {
        if(!is_string($encrypted)){
            return null;
        }
        return (openssl_private_decrypt(self::urlsafeB64Decode($encrypted), $decrypted, self::getPrivateKey()))? $decrypted : null;
    }

    /**
     * 公钥加密
     */
    public static function pubEncrypt($data)
    {
        if(!is_string($data)){
            return null;
        }
        return openssl_public_encrypt($data,$encrypted,self::getPublicKey())? self::urlsafeB64Encode($encrypted) : null;
    }
    
    /**
     * 公钥解密
     */
    public static function pubDecrypt($encrypted)
    {
        if(!is_string($encrypted)){
            return null;
        }
        return (openssl_public_decrypt(self::urlsafeB64Decode($encrypted), $decrypted, self::getPublicKey()))? $decrypted : null;
    }
}