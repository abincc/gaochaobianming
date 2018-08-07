<?php
/**
 * 正则表达式
 */
namespace Common\Org\Form;

class Regex{
	/**
	 * 手机号
	 * @return string
	 */
	public static function mobile() {
		return '/^1[3|4|5|7|8][0-9]{9}$/';
	}
	/**
	 * 单个字母
	 * @return string
	 */
	public static function abc_single() {
		return '^[a-zA-Z]$';
	}
	/**
	 * 0 和 1
	 * @return string
	 */
	public static function zero_one() {
		return '/^[0-1]$/';
	}
}