<?php
namespace Backend\Controller\Property;
/**
 * 保洁维修管理
 * @author llf
 * @time 2016-06-30
 */
class SmController extends IndexController {
	
	/**
	 * 列表函数
	 */
	public function index(){
		$this->display();	
	}

	/**
     * 电话号码群发
     * @author xiaobin
     * @param type
     */
    public function import(){

        if(IS_POST){
            $sendfail = array();
            //上传导入文件
            $config = array(
                'maxSize'    => 3145728,
                'rootPath'   => './',				//相对网站根目录
                'savePath'   => 'Uploads/Tbook/Tbook/',	//文件具体保存的目录
                'saveName'   => array('uniqid',''),
                'exts'       => array('csv','xlsx','xls'),
                'autoSub'    => true,
                'subName'    => array('date','Ymd'),
            );
            $Upload 	 = new \Think\Upload($config);        /* 实例化上传类 */
            $file_info   = $Upload->upload();
            if(!$file_info){
                $this->error("导入文件上传失败");
            }
            // $file =  'C:\Users\cnsunrun\Downloads\sku_export_20170502_182536.xls';
            // $file = './Uploads/Product/Sku/20170502/59085cb6df670.xls';
            $file = './'.$file_info['file']['savepath'].$file_info['file']['savename'];

            $import_config = array(
                array('title' => 'title', 'name' => 'title'),
                array('title' => 'name', 'name' => 'name'),
                array('title' => 'phone', 'name' => 'phone'),
            );
            $data = A('Common/Excel','',1)->im_excel($import_config, $file);
            // var_dump($data);
            // die();
	        if(empty($data)){
	            throw new \Exception("导入数据内容未空或导入文件解析失败");
	        }
            
	        foreach ($data as $k => $v) {

	                $tel = $v['phone'];
	                if(empty($tel)){
	                	continue;
	                }
	                $code = "群发推广";
	                send_sms($tel,'SMS_139982787', 0, $code);
	        }
            $this->success('发送成功');
        }else{
            $this->display();
        }

    }
}

