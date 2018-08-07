<?php
/**
 * 高德 - 云图
 */
namespace Common\Org\Amap;

class YunTu{
	protected $url = 'http://yuntuapi.amap.com/datamanage/data/';
	protected $key = 'ffff8631e4da1f464505b5cd3fd40cb1';
	public $tableid = '5a015576afdf521e86dfba87';
	protected $sign_key = '55c0f45a7508a8b1cb2a9d361f010e26';
	
	public function __construct() {
		
	}
	/**
	 * 添加
	 */
	public function add($name, $location, $address, $contact_number) {
		$url = $this->url.'create';
	
		$data = $this->data($name, $location, $address, $contact_number);
		return $this->curl($url, $data);
	}
	/**
	 * 更新
	 */
	public function update($name, $location, $address, $contact_number, $id) {
		$url = $this->url.'update';
	
		$data = $this->data($name, $location, $address, $contact_number, $id);
		return $this->curl($url, $data);
	}
	/**
	 * 删除
	 */
	public function delete($id) {
		$_data = [
			'ids'=> $id,
		];
	
		$url  = $this->url.'delete';
		$data = $this->data_common($_data);
	
		return $this->curl($url, $data);
	}
	/**
	 * 数据
	 */
	protected function data($name, $location, $address, $contact_number, $id = 0) {
		$_data = [
			'loctype'=> 1,
		];
	
		$data = [
			'_name'=> $name,
			'_location'=> $location,
			'_address'=> $address,
			'contact_number'=> $contact_number,
		];
		if($id > 0) {
			$data['_id'] = $id;
		}
		$_data['data'] = json_encode($data);
	
		return $this->data_common($_data);
	}
	/**
	 * 数据 - 公共
	 */
	protected function data_common($data) {
		$_data = [
			'key'=> $this->key,
			'tableid'=> $this->tableid,
		];
		$_data = array_merge($_data, $data);
		$param = $this->filter_param($_data);
		$param = $this->arg_sort($param);
		$_data['sig'] = $this->md5_sign($param, $this->sign_key);
	
		return $_data;
	}
	/**
	 * 过滤签名和空值
	 */
	protected function filter_param($param) {
		$data = [];
		foreach($param as $k=> $v) {
			if($k == 'sign' || $v == '') {
				continue;
			}
			$data[$k] = $v;
		}
		return $data;
	}
	protected function param_str($param) {
		$_param = [];
		foreach($param as $k=> $v) {
			$_param[] = $k.'='.$v;
		}
		$str = implode('&', $_param);
		return $str;
	}
	/**
	 * 数组排序
	 */
	protected function arg_sort($param) {
		ksort($param);
		reset($param);
		return $param;
	}
	/**
	 * 生成签名
	 */
	protected function md5_sign($param, $key) {
		$str = $this->param_str($param);
		return md5($str.$key);
	}
	/**
	 * 构造请求
	 * @param unknown $url
	 * @param unknown $data
	 * @return boolean|mixed
	 */
	protected function curl($url, $data) {
		$ch = curl_init($url);
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
//		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
		$result = curl_exec($ch);
		$status = curl_getinfo($ch);
		$errno = curl_errno($ch);
		curl_close($ch);
		if($errno || $status['http_code'] != 200) {
			return false;
		}
		$json = json_decode($result, true);
		if($json['status'] == 1) {
			if(isset($json['_id'])) return $json['_id'];
			return true;
		}
		return false;
	}
}