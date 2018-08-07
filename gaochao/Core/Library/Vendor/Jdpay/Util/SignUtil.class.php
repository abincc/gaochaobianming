<?php
require_once "RSAUtils.class.php";
/**
 * 签名   
 */
class SignUtil {
	/**
	 * 签名   
	 * $param array $params 参数数组
	 * $param array $unSignKeyList 要移除的键名
	 * $return string 加密后的字符串
	 */
	public static function signWithoutToHex($params,$unSignKeyList) {
		ksort($params);
		$sourceSignString = self::signString ( $params, $unSignKeyList );
		$sha256SourceSignString = hash ( "sha256", $sourceSignString);	
		return \RSAUtils::encryptByPrivateKey ($sha256SourceSignString);
	}
	
	public static function sign($params,$unSignKeyList) {
		ksort($params);
		$sourceSignString = self::signString ( $params, $unSignKeyList );
		error_log($sourceSignString, 0);
		$sha256SourceSignString = hash ( "sha256", $sourceSignString);
		error_log($sha256SourceSignString, 0);
		return \RSAUtils::encryptByPrivateKey ($sha256SourceSignString);
	}
	
	/**
	 * 签名   
	 * $param array $params 参数数组
	 * $param array $unSignKeyList 要移除的键名
	 * $return string 移除后的参数字符串
	 */
	public static function signString($data, $unSignKeyList) {
		$linkStr="";
		$isFirst=true;
		ksort($data);
		foreach($data as $key=>$value){
			if($value==null || $value==""){
				continue;
			}
			$bool=false;
			foreach ($unSignKeyList as $str) {
				if($key."" == $str.""){
					$bool=true;
					break;
				}
			}
			if($bool){
				continue;
			}
			if(!$isFirst){
				$linkStr.="&";
			}
			$linkStr.=$key."=".$value;
			if($isFirst){
				$isFirst=false;
			}
		}
		return $linkStr;
	}
}
