<?php
/**
 * 表单数据过滤类
 */
namespace Common\Org\Form;

class Form{
	/**
	 * 正整数
	 * @param unknown $key
	 * @param string $method
	 * @param unknown $data
	 * @return mixed|NULL
	 */
	public static function int($key, $method = 'post.', $default = 0, $data = null) {
		return I($method.$key, $default, 'int,abs', $data);
	}
	/**
	 * 浮点数
	 * @param unknown $key
	 * @param string $method
	 * @param unknown $data
	 * @return mixed|NULL
	 */
	public static function float($key, $method = 'post.', $default = 0.00, $data = null) {
		return I($method.$key, $default, 'float,abs', $data);
	}
	/**
	 * 字符串，去除 html 和 php 标签，去除 左右空字符
	 * @param unknown $key
	 * @param string $method
	 * @param unknown $data
	 * @return mixed|NULL
	 */
	public static function string($key, $method = 'post.', $default = '', $data = null) {
		return I($method.$key, $default, 'strip_tags,trim', $data);
	}
	/**
	 * E-mail
	 * @param unknown $key
	 * @param string $method
	 * @param unknown $data
	 * @return mixed|NULL
	 */
	public static function email($key, $method = 'post.', $default = '', $data = null) {
		return I($method.$key, $default, FILTER_VALIDATE_EMAIL, $data);
	}
	/**
	 * 手机
	 */
	public static function mobile($key, $method = 'post.', $default = '', $data = null) {
		return I($method.$key, $default, [Regex::mobile()], $data);
	}
	/**
	 * 单字母
	 * @param unknown $key
	 * @param string $method
	 * @param string $default
	 * @param unknown $data
	 * @return mixed|NULL
	 */
	public static function abc_single($key, $method = 'post.', $default = '', $data = null) {
		return I($method.$key, $default, [Regex::abc_single()], $data);
	}
	/**
	 * 0 和  1
	 * @param unknown $key
	 * @param string $method
	 * @param number $default
	 * @param unknown $data
	 * @return mixed|NULL
	 */
	public static function zero_one($key, $method = 'post.', $default = 0, $data = null) {
		return I($method.$key, $default, [Regex::zero_one()], $data);
	}
}