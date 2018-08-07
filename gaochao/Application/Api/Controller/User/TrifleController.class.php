<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 小事帮忙
	 */
class TrifleController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table 		='Trifle';
	protected $table_view 	='TrifleView';
	protected $service 		='trifle_service';
	protected $table_image	='sr_trifle_image';

	/**
	 * 列表 - 小事帮忙
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function index() {

		$map 	= array(
				'member_id'	=> $this->member_id,
				'status'	=> array('IN',array(1,2)),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$field = array('t.id as trifle_id','trifle_service_id','content','status','add_time');
		$list  = $this->page(D($this->table_view),$map,'add_time desc',$field);

		if(!empty($list)){
			$service = get_no_del('trifle_service','sort desc,id asc');
			$status  = get_table_state($this->table,'status');
			$images  = D($this->table)->get_covers(array_column($list,'trifle_id'));

			foreach ($list as $k => $v) {
				$list[$k]['trifle_service_title'] = $service[$v['trifle_service_id']]['title'];
				$list[$k]['status_title'] 		  = $status[$v['status']]['title'];
				$list[$k]['image'] 		  		  = file_url($images[$v['trifle_id']]['image']);
				unset($list[$k]['trifle_service_id']);
				unset($list[$k]['status']);
			}
		}

		$this->api_success('ok',(array)$list);
		
	}

	/**
	 * 发布小事帮忙详情信息
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function info(){

		$id  = I('trifle_id',0,'int');

		$map = array(
				'id'		=> $id,
				'member_id'	=> $this->member_id,
				'status'	=> array('IN',array(1,2)),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);

		$field = array('id as trifle_id','trifle_service_id','content','status','add_time','deal_time');
		$info  = M($this->table)->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('详情信息不存在');
		}
		$service = get_no_del('trifle_service','sort desc,id asc');
		$status  = get_table_state($this->table,'status');
		$info['trifle_service_title'] 	= $service[$info['trifle_service_id']]['title']; 
		$info['status_title'] 			= $status[$info['status']]['title'];
		$info['images']					= D($this->table)->get_image($info['trifle_id']);
		if(!empty($info['images'])){
			array_walk($info['images'], function(&$a) {
				$a['image'] = file_url($a['image']);
			});
		}
		if($info['status'] == 1){
			$info['deal_time'] = '';
		}

		$this->api_success('ok',$info);

	}

	/**
	 * 发布小事帮忙信息
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function add() {

		if(!IS_POST)		$this->api_error('请求方式错误');
		$data = array();
		$data['trifle_service_id'] 	= I('trifle_service_id','','int');
		// $data['district_id'] 	= I('district_id','','int');
		$data['content'] 			= I('content','','trim');

		$data['status'] 	=  1;
		$data['member_id'] 	= $this->member_id;

		$member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id,building_no,room_no')->find();
		if(empty($member_info)){
			$this->api_error('用户信息不存在');
		}
		$data['district_id']	= intval($member_info['district_id']);
		$data['building_no'] 	= trim($member_info['building_no']);
		$data['room_no'] 		= trim($member_info['room_no']);

		$img[] = $_FILES['img'];

		try {
			$M = M();
			$M->startTrans();
			$id = update_data(D($this->table),[],[],$data);
			if(!is_numeric($id)){
				throw new \Exception($id);
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
					'savePath'   => 'Uploads/Trifle/', 
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
				// $img_size = getimagesize('./'.$file['savepath'].$file['savename']);
				$images[] = array(
					'pid' 		 =>$id,
					'image'		 =>$file['savepath'].$file['savename'],
					'add_time'	 =>date('Y-m-d H:i:s'),
				);
			}
			$sql = addSql($images,$this->table_image);
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
	 * 取消操作
	 * @time 2017-08-27
	 * @author llf
	 **/
	public function cancel(){

		$id  = I('trifle_id',0,'int');
		$map = array(
			'id'		=> $id,
			'status'	=> array('IN',array(1,2)),
			'member_id'	=> $this->member_id,
			'is_del'	=> 0,
			'is_hid'	=> 0,
			);
		$has = M($this->table)->where($map)->field(['id'])->find();
		if(empty($has)){
			$this->api_error('信息不存在');
		}
		$res = D($this->table)->cancel($has['id']);
		if(!$res){
			$this->api_error('操作失败');
		}

		$this->api_success('操作成功');

	}

}
