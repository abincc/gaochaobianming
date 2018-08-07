<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;

	/**
	 * 体检报告
	 */
class ReportController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table		= 'Report';
	protected $table_image  = 'sr_report_image';
	protected $arr			= array(
								1=>'一',
								2=>'二',
								3=>'三',
								4=>'四',
							);

	/**
	 * 列表 - 我的体检报告列表
	 * @time 2017-11-23
	 * @author llf
	 **/
	public function index() {

		$member_info = M('Member')->where('id='.$this->member_id)->field('id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map   = array(
				'member_id' 	=> $this->member_id,
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$field = array('id as report_id','ope_date');
		$list  = $this->page($this->table,$map,'ope_date desc',$field);

		if(!empty($list)){
			foreach ($list as $k => $v) {
				$m = date('n',strtotime($v['ope_date']));
				$list[$k]['title'] = $v['ope_date'].' (第'.$this->arr[ceil((date($m))/3)].'季度)';
				// if(!$v['admin_id']){{
				// 	$list[$k]['title'] .= '(用户自传)'; 
				// }
				unset($list[$k]['ope_date']);
			}
		}

		$this->api_success('ok',(array)$list);
		
	}

	/**
	 * 详情
	 * @time 2017-11-23
	 * @author llf
	 **/
	public function detail(){

		$report_id = I('report_id',0,'int'); 

		$member_info = M('Member')->where('id='.$this->member_id)->field('id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息错误');
		}
		$map   = array(
				'id' 			=> $report_id,
				'is_del'		=> 0,
				'is_hid'		=> 0,
			);
		$Report = D('Report');
		$field  = array('id as report_id','ope_date','add_time');
		$info   = $Report->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('详情信息不存在');
		}
		$m = date('n',strtotime($info['ope_date']));
		$info['title']  = $info['ope_date'].' (第'.$this->arr[ceil((date($m))/3)].'季度)';
		$info['images'] = $Report->get_image($report_id);

		if(!empty($info['images'])){
			array_walk($info['images'], function(&$a){
				$a['image'] = file_url($a['image']);
			});
		}

		$this->api_success('ok',$info);
	}


	/**
	 * 上传报告
	 * @time 2017-11-23
	 * @author llf
	 **/
	public function add() {

		if(!IS_POST)		$this->api_error('请求方式错误');
		$data = array();
		$data['ope_date']	= I('ope_date','','trim');
		$data['member_id'] 	= $this->member_id;

		$member_info = M('Member')->where('id='.$this->member_id)->field('id')->find();
		if(empty($member_info)){
			$this->api_error('用户信息不存在');
		}

		$img[] = $_FILES['img'];

		try {
			$M = M();
			$M->startTrans();
			$id = update_data(D($this->table),[],[],$data);
			if(!is_numeric($id)){
				throw new \Exception($id);
			}
			if(empty($_FILES['img'])){
				throw new \Exception('请上传图片格式报告');
			}
			// if(count($img) > 9){
			// 	throw new \Exception('上传图片数量不能超过9张');
			// }
			$config = array(
					'maxSize'    => 3145728,
					'rootPath'   => './',
					'savePath'   => 'Uploads/Report/', 
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
					'report_id'	 =>$id,
					'image'		 =>$file['savepath'].$file['savename'],
					'add_time'	 =>date('Y-m-d H:i:s'),
					'width'		 =>floatval($img_size[0]),
					'height'	 =>floatval($img_size[1]),
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

}
