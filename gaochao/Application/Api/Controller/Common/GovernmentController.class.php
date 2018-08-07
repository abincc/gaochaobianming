<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;

  /*政务公开*/
class GovernmentController extends ApiController{
	/**
	 * 表名
	 * @var string
	 */
	protected $table  = 'Government';

   /**
	* 平台公告--列表
	* @author llf
	* @dtime 2017-06-06
	*/
	public function index(){
		$map 	= array(
				'is_del'=>0,
				'is_hid'=>0,
			);
		$district_id = I('district_id',0,'cloud_id_parser');
		if(!$district_id){
			$map['district_id'] = 0;
		}else{
			$map['district_id'] = array('IN',[0,$district_id]);
		}
		$field 	= array('id as government_id','title','cover','description','content','add_time');
		$list	= $this->page($this->table, $map, 'add_time desc' ,$field , 12);

		if(!empty($list)){
			array_walk($list, function(&$a){
				$a['cover'] = file_url($a['cover']);
				$a['url']   = U('Home/Index/Index/government_info',array('government_id'=>$a['government_id']),true,true);
			});
		}

		$this->api_success('ok',(array)$list);
	}

   /**
	* 平台公告--详情
	* @author llf
	* @dtime 2017-06-06
	*/
	public function info(){

		$government_id = I('government_id',0,'int');
		$map = array(
				'is_del'=>0,
				'is_hid'=>0,
				'id'	=>$government_id,	
			);

		$field = array('id as government_id','title','description','content','add_time');
		$info  = M($this->table)->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('公告信息不存在');
		}
		$this->api_success('ok',$info);
	}

}