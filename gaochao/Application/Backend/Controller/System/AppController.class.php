<?php
namespace Backend\Controller\System;
	/**
	 * app 下载配置
	 * @time 2016-10-12
	 * @author 秦晓武
	 */
class AppController extends IndexController {

	/**
	 * 配置APP 版本 和下载地址 （安卓）
	 */
	public function index() {
		
	
		if(IS_POST){
			$version 	= I('post.version');

			//上传导入文件
			$config = array(
				'maxSize'    => 31457280,    
				'rootPath'   => './',				//相对网站根目录
				'savePath'   => 'Uploads/App/',	//文件具体保存的目录
				'saveName'   => array('uniqid',''),    
				'exts'       => array('apk'),    
				'autoSub'    => true,    
				'subName'    => array('date','Ymd'),
			); 
			$Upload 	 = new \Think\Upload($config);        /* 实例化上传类 */
			$file_info   = $Upload->upload();
			if(!$file_info) {
				$this->error($Upload->getError());
			}else{
				// bug($file_info);
				M('config')->where(array('name'=>'APP_VERSION'))->setField('value',$version);
				M('config')->where(array('name'=>'APP_UPLOAD_URL'))->setField('value',$file_info['app']['savepath'].''.$file_info['app']['savename']);
				// c('APP_VERSION',$version);
				// c('APP_UPLOAD_URL',$file_info['app']['savepath'].''.$file_info['app']['savename']);
				// var_dump($file_info);
				// die();
				$this->success('上传成功',U('index'));
			}
		}else{
			// bug(C('version'));
			/*显示版本信息以及文件下载地址*/
			$this->display();
		}
	}
	
}
