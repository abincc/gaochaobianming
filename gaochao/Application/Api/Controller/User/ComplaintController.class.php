<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 投诉反馈
	 */
class ComplaintController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table='Complaint';

	/**
	 * 投诉
	 * @time 2017-09-12
	 * @author llf
	 **/
	public function index(){
		
		if(!IS_POST)			$this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));

		$post = I('post.');
		$data = array();
		$data['member_id'] 	 = $this->member_id;
		$data['content']	 = trim($post['content']);

		$upload = new \Think\Upload();
		$upload->rootPath  = './';
		$upload->maxSize   = 3145728;  
		$upload->exts      = array('jpg', 'gif', 'png', 'jpeg');    
		$upload->savePath  = 'Uploads/Complaint/'; 
		$upload->saveName  = array('uniqid','');
		$upload->replace   = true; 
		$upload->subName   = array('date','Ymd');

		/* 获取用户小区ID */
		$data['district_id'] = M('Member')->where('id='.$this->member_id)->getField('district_id');
		if(empty($data['district_id'])){
			$this->api_error('用户小区获取失败');
		}
		try {
			$M = M();
			$M->startTrans();

			$result = update_data(D('Complaint'),[],[],$data);
			if(!is_numeric($result)){
				throw new \Exception($result);
			}

			$images[] = $_FILES['images'];
			if(empty($_FILES['images']))	$this->apiReturn(array('status'=>'0','msg'=>'请上传图片'));

			/* 处理图片上传*/
			if(!empty($images)){
				$info   =   $upload->upload($images);
				if(!$info) {
					throw new \Exception($upload->getError());
				}
				$imgs = array();
				foreach($info as $file){  
					$img_size = getimagesize('./'.$file['savepath'].$file['savename']);
					$imgs[] = array(
						'complaint_id'=>$result,
						'image'		  =>$file['savepath'].$file['savename'],
						'add_time'	  =>date('Y-m-d H:i:s'),
						'width'	 	  =>intval($img_size[0]),
						'height'	  =>intval($img_size[1]),
					);
				}
				$sql = $this->addSql($imgs,'sr_complaint_image');
				$res = M()->execute($sql);
				if(!is_numeric($res)){
					/*清除图片文件*/
					array_walk($imgs, function($a){
						@unlink ( $a['image'] );
					});
					unset($info);
					unset($imgs);
					unset($images);
					throw new \Exception('图片上传失败');
				}
			}
		} catch (\Exception $e) {
			$M->rollback();
			$this->apiReturn(array('status'=>'0','msg'=>$e->getMessage()));
		}
		$M->commit();
		$this->apiReturn(array('status'=>'1','msg'=>'提交成功'));
	}

}
