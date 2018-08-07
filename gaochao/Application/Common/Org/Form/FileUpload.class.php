<?php
/**
 * 文件上传
 */
namespace Common\Org\Form;

use Think\Upload;

class FileUpload {
	/**
	 * 默认配置
	 * @var array
	 */
	protected $config = [
		'maxSize'=> 2097152,
		'exts'=> ['jpg', 'jpeg', 'png'],
		'mimes'=> ['image/jpeg', 'image/png'],
		'savePath'=> 'FileUpload',
	];
	
	/**
	 * 初始化
	 * @param array $config
	 */
	public function __construct($config = []) {
		$this->config = $this->config($config);
	}
	/**
	 * 配置
	 * @param array $config
	 */
	public function config($config = []) {
		$this->config = array_merge($this->config, $config);
		
		return $this->config;
	}
	/**
	 * 配置文件存储路径
	 * @param unknown $save_path
	 */
	public function set($key, $value) {
		$this->config[$key] = $value;
	}
	/**
	 * 文件上传
	 */
	public function upload() {
		return $this->_upload($this->config);
	}
	/**
	 * 单次上传文件个数限制
	 */
	public function _limit($key = 'file', $limit = false) {
		if($limit === false) return ;
		if(isset($_FILES[$key])) {
			$_FILES[$key]['tmp_name'] = array_slice($_FILES[$key]['tmp_name'], 0, $limit);
		}
	}
	/**
	 * 文件上传
	 * @param array $config
	 * @return string|unknown|boolean|mixed[]|string[]
	 */
	protected function _upload($config) {
		$upload = new Upload($config);
		$info = $upload->upload();
		if(!$info) {
			return $upload->getError();
		}
		return $this->_format($info);
	}
	/**
	 * 处理待上传文件
	 * @param string $key
	 */
	public function _file($key = '') {
		if($key !== '') {
			$_file = $_FILES;
			$_FILES = [
				$key=> $_file[$key],	
			];
		}
	}
	/**
	 * 格式化已上传文件数据
	 */
	protected function _format($list) {
		$_data = [];
		foreach($list as $k=> $v) {
			$v['filename'] = 'Uploads/'.$v['savepath'].$v['savename'];
			if($k !== $v['key']) {
				$_data[$v['key']][] = $v;
			}else {
				$_data[$k] = $v;
			}
		}
		
		return $_data;
	}
	/**
	 * 上传前后文件数对比
	 */
	public function _count($key, $list) {
		if(count($_FILES[$key]['tmp_name']) == count($list)) return true;
		
		$this->_delete($list);
		return false;
	}
	/**
	 * 删除
	 * @param unknown $list
	 */
	public function _delete($list) {
		foreach($list as $v) {
			if(file_exists($v['filename'])) unlink($v['filename']);
		}
	}
}