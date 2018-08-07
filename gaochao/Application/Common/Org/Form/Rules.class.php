<?php
/**
 * 验证规则
 */
namespace Common\Org\Form;

class Rules{
	/**
	 * 非空
	 * @param unknown $field
	 * @param unknown $error_msg
	 * @return string[]|unknown[]
	 */
	public static function _require($field, $error_msg) {
		return [$field, 'require', $error_msg];
	}
	/**
	 * 邮箱
	 * @param unknown $field
	 * @param unknown $error_msg
	 */
	public static function email($field, $error_msg) {
		return [$field, 'email', $error_msg];
	}
	/**
	 * url
	 * @param unknown $field
	 * @param unknown $error_msg
	 * @return string[]|unknown[]
	 */
	public static function url($field, $error_msg) {
		return [$field, 'url', $error_msg];
	}
	/**
	 * 货币，含 0
	 * @param unknown $field
	 * @param unknown $error_msg
	 */
	public static function currency($field, $error_msg) {
		return [$field, 'currency', $error_msg];
	}
	/**
	 * 数字
	 * @param unknown $field
	 * @param unknown $error_msg
	 */
	public static function number($field, $error_msg) {
		return [$field, 'number', $error_msg];
	}
	/**
	 * 字符长度
	 * @param unknown $field
	 * @param unknown $error_msg
	 * @param unknown $length
	 * @return number[]|string[]|unknown[]
	 */
	public static function length($field, $error_msg, $min, $max) {
		return [$field, $min.','.$max, $error_msg, 0, 'length'];
	}
}