<?php
namespace Api\Controller\Common;
use Common\Controller\ApiController;

/*
 * 便民服务
 */
class BianminController extends ApiController{

    private $table = "bain_min_category";

    /**
     * 获取便民服务类型
     */
    public function  category(){
        $M = M($this -> table);
        $data = array(
            "is_del" => 0,
        );
        $result = $M->where($data)->select();
        $this->apiReturn(array('status'=>'1','info'=>$result));
    }

    /**
     * 获取便民类型服务列表
     */
    public function getCategoryInfo(){
        $M = M("bian_min");
        $parentId = I("category_id",0);
        if($parentId == 0){
            $this->apiReturn(array('status'=>'1','msg'=>"category_id参数必须"));
        }
        $data['bain_min_category_id'] = $parentId;
        $result = $M->where($data)->select();
        $this->apiReturn(array('status'=>'1','info'=>$result));
    }

   /**
	* 便民服务列表
	* @author llf
	* @dtime 2017-06-06
	*/
	public function index(){

		$data = get_no_del('bian_min','sort desc,id asc');
		if(empty($data)){
			$this->apiReturn(array('status'=>'0','msg'=>'便民服务信息获取失败'));
		}

		foreach($data as $k=>$v){
			$data[$k]['bianmin_id'] = $v['id'];
			$data[$k]['image']      = file_url($v['image']);
			unset($data[$k]['id']);
			unset($data[$k]['sort']);
			unset($data[$k]['is_del']);
			unset($data[$k]['is_hid']);
			unset($data[$k]['add_time']);
			unset($data[$k]['update_time']);
		}
		$data = array_values($data);
		$this->apiReturn(array('status'=>'1','msg'=>'OK','info'=>$data));
	}

}