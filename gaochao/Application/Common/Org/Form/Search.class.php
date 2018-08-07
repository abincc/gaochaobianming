<?php
/**
 * 搜索
 */
namespace Common\Org\Form;

class Search{
	/**
	 * ID 搜索
	 * @param unknown $_map
	 * @param unknown $field
	 * @param string $verify	
	 */
	public static function int(&$_map, $field) {
		$int = Form::int($field, 'get.', false);
		if(is_numeric($int)) $_map[$field] = $int;
	}
	/**
	 * 字符串搜索
	 * @param unknown $_map
	 * @param unknown $field
	 */
	public static function string(&$_map, $field, $key = 'keywords') {
		$string = Form::string($field, 'get.');
		if($string) $_map[$field] = $string;
	}
	/**
	 * 模糊搜索
	 * @param unknown $_map
	 * @param unknown $field
	 */
	public static function string_like(&$_map, $field, $key = 'keywords') {
		$string = Form::string($key, 'get.');
		if($string) $_map[$field] = ['like', '%'.$string.'%'];
	}
} 