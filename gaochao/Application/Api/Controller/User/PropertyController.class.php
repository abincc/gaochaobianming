<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 物业管理模块
	 */
class PropertyController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $clean_table 			='Clean';
	protected $clean_table_view 	='CleanView';
	protected $clean_service 		='clean_service';
	protected $clean_table_reply 	='clean_reply_image';
	protected $clean_reply_image 	='sr_clean_reply_image';

	protected $trifle_table			='Trifle';
	protected $trifle_table_view 	='TrifleView';
	protected $trifle_service 		='trifle_service';
	protected $trifle_reply_image 	='sr_trifle_reply_image';

	/**
	 * 列表 - 保洁保修
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function clean() {

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map 	= array(
				'district_id' 	=> intval($member_info['district_id']),
				'status'		=> array('IN',array(1,2)),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = array('c.id as clean_id','clean_service_id','content','status','add_time','c.reply','c.is_reply');
		$_GET['p'] = I('p',1,'int');
		$list  = $this->page(D($this->clean_table_view),$map,'add_time desc',$field);

		if(!empty($list)){
			$service = get_no_del('clean_service','sort desc,id asc');
			$status  = get_table_state($this->clean_table,'status');
			$images  = D($this->clean_table)->get_covers(array_column($list,'clean_id'));

			foreach ($list as $k => $v) {
				$list[$k]['clean_service_title']  = $service[$v['clean_service_id']]['title'];
				$list[$k]['status_title'] 		  = $status[$v['status']]['title'];
				$list[$k]['image'] 		  		  = file_url($images[$v['clean_id']]['image']);
				unset($list[$k]['clean_service_id']);
			}
		}

		$this->api_success('ok',(array)$list);
		
	}


	/**
	 * 列表 - 保洁保修
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function trifle() {

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map 	= array(
				'district_id' 	=> intval($member_info['district_id']),
				'status'		=> array('IN',array(1,2)),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = array('t.id as trifle_id','trifle_service_id','content','status','add_time','t.reply','t.is_reply');
		$_GET['p'] = I('p',1,'int');
		$list  = $this->page(D($this->trifle_table_view),$map,'add_time desc',$field);

		if(!empty($list)){
			$service = get_no_del($this->clean_service,'sort desc,id asc');
			$status  = get_table_state($this->trifle_table,'status');
			$images  = D($this->trifle_table)->get_covers(array_column($list,'trifle_id'));

			foreach ($list as $k => $v) {
				$list[$k]['trifle_service_title'] = $service[$v['trifle_service_id']]['title'];
				$list[$k]['status_title'] 		  = $status[$v['status']]['title'];
				$list[$k]['image'] 		  		  = file_url($images[$v['trifle_id']]['image']);
				unset($list[$k]['trifle_service_id']);
			}
		}

		$this->api_success('ok',(array)$list);
		
	}


	/**
	 * 物业管理详情--保洁详情
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function info_clean(){

		$id  = I('clean_id',0,'int');

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map = array(
				'id'			=> $id,
				'district_id' 	=> intval($member_info['district_id']),
				'status'		=> array('IN',array(1,2)),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = array('c.id as clean_id','clean_service_id','content','status','add_time','username','mobile','building_no','room_no','deal_time','reply','is_reply');
		$info  = D($this->clean_table_view)->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('详情信息不存在');
		}
		$service = get_no_del('clean_service','sort desc,id asc');
		$status  = get_table_state($this->clean_table,'status');
		$info['clean_service_title'] 	= $service[$info['clean_service_id']]['title']; 
		$info['status_title'] 			= $status[$info['status']]['title'];
		$info['images']					= D($this->clean_table)->get_image($info['clean_id']);
		$info['reply_images'] 			= D($this->clean_table)->get_reply_image($info['clean_id']);
		if(!empty($info['images'])){
			array_walk($info['images'], function(&$a) {
				$a['image'] = file_url($a['image']);
			});
		}
		if(!empty($info['reply_images'])){
			array_walk($info['reply_images'], function(&$a) {
				$a['image'] = file_url($a['image']);
			});
		}
		if($info['status'] == 1){
			$info['deal_time'] = '';
		}

		$this->api_success('ok',$info);

	}


	/**
	 * 物业管理详情--小事详情
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function info_trifle(){

		$id  = I('trifle_id',0,'int');

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map = array(
				'id'			=> $id,
				'district_id' 	=> intval($member_info['district_id']),
				'status'		=> array('IN',array(1,2)),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = array('t.id as trifle_id','trifle_service_id','content','status','add_time','username','mobile','building_no','room_no','deal_time','reply','is_reply');
		$info  = D($this->trifle_table_view)->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('详情信息不存在');
		}
		$service = get_no_del($this->trifle_service,'sort desc,id asc');
		$status  = get_table_state($this->trifle_table,'status');
		$info['trifle_service_title'] 	= $service[$info['trifle_service_id']]['title']; 
		$info['status_title'] 			= $status[$info['status']]['title'];
		$info['images']					= D($this->trifle_table)->get_image($info['trifle_id']);
		$info['reply_images'] = D($this->trifle_table)->get_reply_image($info['trifle_id']);
		if(!empty($info['images'])){
			array_walk($info['images'], function(&$a) {
				$a['image'] = file_url($a['image']);
			});
		}
		if(!empty($info['reply_images'])){
			array_walk($info['reply_images'], function(&$a) {
				$a['image'] = file_url($a['image']);
			});
		}
		if($info['status'] == 1){
			$info['deal_time'] = '';
		}

		$this->api_success('ok',$info);

	}

	/**
	 * 物业处理--保洁维修
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function deal_clean(){

		$id  = I('clean_id',0,'int');
		write_debug(I(),'物业保洁维修处理提交');

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map = array(
				'id'			=> $id,
				'district_id' 	=> intval($member_info['district_id']),
				'status'		=> array('IN',array(1)),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = true;
		$info  = D($this->clean_table)->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('处理信息不存在');
		}
		if(!D($this->clean_table)->deal($info['id'],2,$this->member_id)){
			$this->api_error('操作失败');
		}else{
			$service = get_no_del('clean_service','sort desc,id asc');
			$info['clean_service_title'] 	= $service[$info['clean_service_id']]['title']; 
			//消息推送
			D('Message')->send($info['member_id'],1,'保洁维修处理消息','您发布的保洁维修消息‘'.$info['clean_service_title'].'’已处理！');
			$this->api_success('操作成功');
		}

	}



	/**
	 * 物业处理--小事帮忙
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function deal_trifle(){


		$id  = I('trifle_id',0,'int');

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map = array(
				'id'			=> $id,
				'district_id' 	=> intval($member_info['district_id']),
				'status'		=> array('IN',array(1)),
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = true;
		$info  = D($this->trifle_table)->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('处理信息不存在');
		}
		if(!D($this->trifle_table)->deal($info['id'],2,$this->member_id)){
			$this->api_error('操作失败');
		}else{
			$service = get_no_del($this->trifle_service,'sort desc,id asc');
			$info['trifle_service_title'] 	= $service[$info['trifle_service_id']]['title']; 
			//消息推送
			D('Message')->send($info['member_id'],1,'小事帮忙处理消息','您发布的帮忙消息‘'.$info['trifle_service_title'].'’已处理！');
			$this->api_success('操作成功');
		}

	}

	/**
	 * 取消操作 -- 保洁维修
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function cancel_clean(){

		$id  = I('clean_id',0,'int');

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map = array(
			'id'			=> $id,
			'status'		=> array('IN',array(1,2)),
			'district_id' 	=> intval($member_info['district_id']),
			'is_del'		=> 0,
			'is_hid'		=> 0,
			);
		$has = M($this->clean_table)->where($map)->field(['id'])->find();
		if(empty($has)){
			$this->api_error('信息不存在');
		}
		$res = D($this->clean_table)->cancel($has['id']);
		if(!$res){
			$this->api_error('操作失败');
		}
		
		$this->api_success('操作成功');
	}


	/**
	 * 取消操作 -- 小事
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function cancel_trifle(){

		$id  = I('trifle_id',0,'int');

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map = array(
			'id'			=> $id,
			'status'		=> array('IN',array(1,2)),
			'district_id' 	=> intval($member_info['district_id']),
			'is_del'		=> 0,
			'is_hid'		=> 0,
			);
		$has = M($this->trifle_table)->where($map)->field(['id'])->find();
		if(empty($has)){
			$this->api_error('信息不存在');
		}
		$res = D($this->trifle_table)->cancel($has['id']);
		if(!$res){
			$this->api_error('操作失败');
		}

		$this->api_success('操作成功');

	}

	/**
	 * 小事帮忙处理回复
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function reply_trifle(){

		if(!IS_POST)		$this->api_error('请求方式错误');
		$data = array();
		$data['id'] 		= I('trifle_id',0,'int');
		$data['reply']		= I('reply','','trim');
		$data['is_reply'] 	= 1;

		$member_info = M('Member')->where('id='.$this->member_id)->field('id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息不存在');
		}
		$Trifle = D($this->trifle_table);
		$has    = $Trifle->where(['id'=>$data['id'],'is_del'=>0,'is_hid'=>0])->find();
		if(empty($has)){
			$this->api_error('小事帮忙信息不存在');
		}
		if($has['is_reply']){
			$this->api_error('处理信息已回复过了');
		}
		$img[] = $_FILES['img'];
		try {
			$M = M();
			$M->startTrans();

			$id = update_data($Trifle,[],[],$data);
			if(!is_numeric($id)){
				throw new \Exception($Trifle->getError());
			}
			if(empty($_FILES['img'])){
				throw new \Exception('请上传图片');
			}
			// if(count($img) > 9){
			// 	throw new \Exception('上传图片数量不能超过9张');
			// }
			$config = array(
					'maxSize'    => 3145728,
					'rootPath'   => './',
					'savePath'   => 'Uploads/Triflereply/', 
					'saveName'   => array('uniqid',''),
					'exts'       => array('jpg', 'gif', 'png', 'jpeg'),
					'autoSub'    => true,    
					'subName'    => array('date','Ymd'),
				);
			/*  处理上传图片*/
			$img_info = $this->upload_files($img,$config);
			if(empty($img_info)){
				throw new \Exception($this->error);
			}
			$images = array();
			foreach($img_info as $file){  
				$img_size = getimagesize('./'.$file['savepath'].$file['savename']);
				$images[] = array(
					'pid' 		 =>$id,
					'image'		 =>$file['savepath'].$file['savename'],
					'add_time'	 =>date('Y-m-d H:i:s'),
					'add_time'	 =>date('Y-m-d H:i:s'),
					'width'	 	 =>intval($img_size[0]),
					'height'	 =>intval($img_size[1]),
				);
			}
			$sql = addSql($images,$this->trifle_reply_image);
			$res = M()->execute($sql);
			if(!is_numeric($res)){
				/*清除图片文件*/
				array_walk($images, function($a){
					@unlink ( $a['image'] );
				});
				unset($img_info);
				unset($images);
				unset($img);
				throw new \Exception('图片上传失败');
			}

			
		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('提交成功');
	}

	/**
	 * 保洁维修处理回复
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function reply_clean(){
		if(!IS_POST)		$this->api_error('请求方式错误');
		$data = array();
		$data['id'] 		= I('clean_id',0,'int');
		$data['reply']		= I('reply','','trim');
		$data['is_reply'] 	= 1;

		$member_info = M('Member')->where('id='.$this->member_id)->field('id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息不存在');
		}
		$Clean = D($this->clean_table);
		$has   = $Clean->where(['id'=>$data['id'],'is_del'=>0,'is_hid'=>0])->find();
		if(empty($has)){
			$this->api_error('保洁维修信息不存在');
		}
		if($has['is_reply']){
			$this->api_error('处理信息已回复过了');
		}
		$img[] = $_FILES['img'];
		try {
			$M = M();
			$M->startTrans();

			$id = update_data($Clean,[],[],$data);
			if(!is_numeric($id)){
				throw new \Exception($Clean->getError());
			}
			if(empty($_FILES['img'])){
				throw new \Exception('请上传图片');
			}
			// if(count($img) > 9){
			// 	throw new \Exception('上传图片数量不能超过9张');
			// }
			$config = array(
					'maxSize'    => 3145728,
					'rootPath'   => './',
					'savePath'   => 'Uploads/Cleanreply/', 
					'saveName'   => array('uniqid',''),
					'exts'       => array('jpg', 'gif', 'png', 'jpeg'),
					'autoSub'    => true,    
					'subName'    => array('date','Ymd'),
				);
			/*  处理上传图片*/
			$img_info = $this->upload_files($img,$config);
			if(empty($img_info)){
				throw new \Exception($this->error);
			}
			$images = array();
			foreach($img_info as $file){  
				$img_size = getimagesize('./'.$file['savepath'].$file['savename']);
				$images[] = array(
					'pid' 		 =>$id,
					'image'		 =>$file['savepath'].$file['savename'],
					'add_time'	 =>date('Y-m-d H:i:s'),
					'add_time'	 =>date('Y-m-d H:i:s'),
					'width'	 	 =>intval($img_size[0]),
					'height'	 =>intval($img_size[1]),
				);
			}
			$sql = addSql($images,$this->clean_reply_image);
			$res = M()->execute($sql);
			if(!is_numeric($res)){
				/*清除图片文件*/
				array_walk($images, function($a){
					@unlink ( $a['image'] );
				});
				unset($img_info);
				unset($images);
				unset($img);
				throw new \Exception('图片上传失败');
			}
			
		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('提交成功');
	}
}
