<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;
use Api\Controller\Base\HomeController;

/**
 * 议事堂
 */
class ProcedureController extends BaseController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table 		='Procedure';
	protected $table_view 	='ProcedureView';
	protected $table_vote	='Procedure_vote';
	protected $table_image 	='Procedure_image';


	/**
	 * 列表
	 * @time 2018-01-25
	 * @author llf
	 **/
	public function index() {

		$building_no = M('member')->where('id='.$this->member_id)->getField('building_no');
		$room_no = M('member')->where('id='.$this->member_id)->getField('room_no');

		$i = 0;
		$map = array(
				'district_id' 	=> I('district_id',0,'cloud_id_parser'),
				'is_del'		=> 0,
				'is_hid'		=> 0,
		);

		$field = array('id as procedure_id','title','description','(`agree`+`disagree`+`abstain`) as number','end_date','is_del as type', 'building_no', 'room_no');
		$list1  = $this->page($this->table,$map,'sort desc,add_time desc',$field);

  		foreach ( $list1 as $unit ){
  			$flag = true;
			 $buildings = split(',',unserialize($unit['building_no']),-1); 
		     $rooms = split(',',unserialize($unit['room_no']),-1);
   			 if(count($buildings) > 0 && $buildings[0] != null){
				if(count($rooms) > 0 && $rooms[0] != null){
					foreach ($buildings as $build) {
						foreach ($rooms as $room) {
							if($build == $building_no && $room_no == $room ){
								$flag = false;
								break;
							}
						}  
					} 
					if($flag == true){
						unset($list1[$i]);
					}
				}else{
					foreach ($buildings as $build) {
						if($build == $building_no){
							$flag = false;
							break;
						}
					}
					if($flag  == true){
						unset($list1[$i]);
					}
				}
			}

			$i += 1;
 		}


		$procedure_ids = array_column($list1,'procedure_id');
		if(!empty($procedure_ids)){
			$map2 = array(
				'district_id' 	=> I('district_id',0,'cloud_id_parser'),
				'is_del'		=> 0,
				'is_hid'		=> 0,
				'id'            => array("IN", $procedure_ids),
			);
 			$list  = $this->page($this->table,$map2,'sort desc,add_time desc',$field);

			if(!empty($list)){
				//获取图片
				$procedure_ids = array_column($list,'procedure_id');
				$images = M($this->table_image)->where(['pid'=>array('IN',$procedure_ids)])->field('id as image_id,pid,image,width,height')->select();
				if(!empty($images)){
					$temp = array();
					foreach ($images as $k => $v) {
						$v['image'] = file_url($v['image']);
						$temp[$v['pid']][] = $v;
					}
					array_walk($list, function(&$a) use($temp){
						$a['images'] = $temp[$a['procedure_id']];
					});
					unset($temp);
				}
				unset($images);

				//验证用户是否已投票
				if($this->member_id){
					$condition = array(
									'member_id' 	=> $this->member_id,
									'procedure_id'	=> array('IN',$procedure_ids),
								);
					$has_votes = M($this->table_vote)->where($condition)->field('procedure_id,type')->group('procedure_id')->select();
					if(!empty($has_votes)){
						$votes = array_column($has_votes,NULL,'procedure_id');
						array_walk($list, function(&$a) use($votes){
							$a['type'] = intval($votes[$a['procedure_id']]['type']);
						});
					}
				}
				$type  = get_table_state($this->table_vote,'type');
				array_walk($list, function(&$a) use($type){
					$a['type_title'] = $type[$a['type']]['title'];
				});
			}
		}

		$this->api_success('ok',(array)$list);
		
	}

	/**
	 * 发布小事帮忙详情信息
	 * @time 2018-01-25
	 * @author llf
	 **/
	public function info(){

		$map 	= array(
				'id' 		=> I('procedure_id',0,'int'),
				'is_del'	=> 0,
				'is_hid'	=> 0,
			);
		$field = array('p.id as procedure_id','title','description','agree','disagree','abstain','(`agree`+`disagree`+`abstain`) as number','end_date','p.is_del as type','district_title');
		$info  = D($this->table_view)->where($map)->field($field)->find();
		if(empty($info)){
			$this->api_error('详情信息不存在');
		}
		//如果存在用户则判断用户是否存在投票信息
		$type  = get_table_state($this->table_vote,'type');
		if($this->member_id){
			$condition = array(
					'member_id'		=> $this->member_id,
					'procedure_id'	=> $info['procedure_id'],
				);
			$info['type'] 	= (int)M($this->table_vote)->where($condition)->getField('type'); 
		}
		$info['type_title'] = $type[$info['type']]['title'];

		//图片信息
		$info['images']	= D($this->table)->get_image($info['procedure_id']);
		if(!empty($info['images'])){
			array_walk($info['images'], function(&$a) {
				$a['image'] = file_url($a['image']);
			});
		}

		$this->api_success('ok',$info);

	}

	/**
	 * 投票操作
	 * @time 2018-01-26
	 * @author llf
	 **/
	public function vote(){

		if(!IS_POST)	$this->api_error('请求方式错误');

		$procedure_id = I('procedure_id',0,'int');
		$type = I('type',0,'int');

		try{
			$M = M();
			$M->startTrans();
			$Procedure = D($this->table);
			$res = $Procedure->vote($this->member_id,$procedure_id,$type);
			if(!$res){
				throw new \Exception($Procedure->getError());
			}
		} catch (\Exception $e) {
			$M->rollback();
			$this->api_error($e->getMessage());
		}
		$M->commit();
		$this->api_success('操作成功');
	}	

}
