<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;

	/**
	 * 电话本
	 */
class TbookController extends ApiController{

	/**
	 * 表名
	 * @var string
	 */
	protected $table = 'Tbook';

	/**
	 * 列表
	 * @time 2017-11-23
	 * @author llf
	 **/
	public function index() {

		$map = array(
				'is_del'=>0,
				'is_hid'=>0,
			);
		$keywords = I('keywords','','trim');
		if(!empty($keywords)){
			$map['title|number'] = array('LIKE',"%$keywords%");
		}
		$field = array('id as tbook_id','title','number','address');
		$list  = $this->page($this->table,$map,'sort desc,add_time desc',$field);
		
		$this->api_success('ok',(array)$list);
		
	}
	
}
