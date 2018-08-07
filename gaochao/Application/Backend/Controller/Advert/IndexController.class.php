<?php
/**广告管理*/
namespace Backend\Controller\Advert;
use Backend\Controller\Base\AdminController;
	/**
	 * 广告管理
	 */
class IndexController extends AdminController {
	/**
	 * 广告表名
	 * @var string
	 */
	protected $adspace_table='adspace';
	/**
	 * 表名
	 * @var string
	 */
	protected $table='banner';
	/**
	 * 广告位列表
	 * @param string $type 类型
	 * @time 2015-03-12
	 * @author	康利民  <2824682114@qq.com>
	 * */
	public function index($type="home"){
		$map['page']=$type;
		$list=get_result($this->adspace_table,$map,'sort desc,id asc');
		$data['_list']=$list;
		$this->assign('meta_title',"首页广告管理");
		$this->assign($data);
		$this->display();
	}
	/**
	 * 设置广告位
	 * @param int $id 当前数据ID
	 * @time 2015-03-12
	 * @author	康利民  <2824682114@qq.com>
	 * */
	public function set($id=''){
		if(IS_POST){
			$posts=I("post.");
			if($posts['scale']==''){
				$posts['scale']='1';
			}
			$_POST['scale']=json_encode($posts['scale']);

			$result=update_data($this->adspace_table,$rules);
			if(is_numeric($result)){
				F("banner_".I('post.site_id'),null);
				$this->success('提交成功！',U('index'));
			}
		}else{
			if($id==''){
				$this->error("缺少参数，无法访问");
			}
			$map['id']=$id;
			$info=get_info($this->adspace_table,$map);
			$this->assign('info',$info);
			$this->display();
		}
	}
	/**
	 * 菜单列表页
	 * @param int $adspace_id 广告位ID
	 * @time 2014-12-26
	 * @author	郭文龙  <2824682114@qq.com>
	 * */
	public function lists($adspace_id = ''){
		$map['adspace_id']=array('eq',$adspace_id);
		if(I('get.site_id')!=''){
			$site_id=intval(I('get.site_id'));
			$map['site_id']=array('in',array(0,$site_id));
		}
		$list=$this->page($this->table,$map,'id desc');
		$data['_list']=$list['list'];
		unset($map);
		$map['id']=array('eq',$adspace_id);
		$adinfo=get_info($this->adspace_table,$map,'title');
		$this->assign('meta_title',"首页".$adinfo['title']."管理");
		$this->assign($data);
		$this->display();
	}
	/**
	 * 添加菜单
	 */
	public function add(){
		if(IS_POST){
			$this->update();
		}else{
			$map['id']=array('eq',I("adspace_id"));
			$adinfo=get_info($this->adspace_table,$map,'title');
			$this->assign('meta_title',"新闻".$adinfo['title']."管理");
			$this->display('operate');
		}
	}
	/**
	 * 修改菜单
	 */
	public function edit(){
		if(IS_POST){
			$info=get_info($this->table,array('id'=>intval(I('id'))));
			F("banner_".$info['site_id'],null);
			$this->update();
		}else{
			$map['id']=intval(I('ids'));
			$info=get_info($this->table,$map);
			$info['start_time']=reset(explode(" ",$info['start_time']));
			$info['end_time']=reset(explode(" ",$info['end_time']));
			$data['info']=$info;
			$this->assign($data);
			$this->display('operate');
		}
	}
	/**
	 * 添加/修改操作
	 */
	public function update(){
		if(IS_POST){
			$posts=$_POST;
			if($_POST['type']=='image' && $_POST['old_image']=='' && I('post.image')==""){
				$this->error("图片不能为空！");
			}
			if($_POST['type']=='code' && I('post.content')==""){
				$this->error("HTML代码不能为空！");
			}
			if($_POST['adspace_id']==''){
				$this->error("非法提交！");
			}
			if($_POST['start_time']==''){
				$_POST['start_time']=date("Y-m-d 00:00:00");
			}
			if($_POST['end_time']==''){
				$_POST['end_time']=date("Y-m-d 00:00:00",strtotime('+1 day'));
			}

			$rules = array( 
				//array('title','require','请填写广告标题！'), //默认情况下用正则进行验证
			);
			$result=update_data($this->table,$rules);
			if(is_numeric($result)){
				if($result>0){
					$id=$result;
				}else{
					$id=intval(I('post.id'));
				}
				if($id>0){
					if(I('post.image')!=''){
						del_thumb($_POST['old_image']);
					}
					multi_file_upload(I('post.image'),'Uploads/Banner','banner','id',$id,'save_path');
				}
				F("banner_".I('post.site_id'),null);
				$this->success('操作成功！',U('lists',array("adspace_id"=>$posts['adspace_id'])));
			}else{
				$this->error($result);
			}
		}else{
			$this->success('违法操作',U('index'));
		}
	}
	
	/**
	 * 删除广告数据，同时删除广告图片
	 * @param int $ids 数据ID
	 * @time 2015-01-13
	 * @author	康利民  <3027788306@qq.com>
	 */
	function del($ids = 0){
		if(is_array($ids)){
			foreach ($ids as $id){
				$this->doDel($id);
			}
		}else{
			$id=$ids;
			$this->doDel($id);
		}
		F("banner_".I('post.site_id'),null);
		$this->success('删除成功');
	}
	/**
	 * 删除处理
	 * @param int $ids 数据ID
	 */
	function doDel($id = 0){
		$map['id'] = $id;
		$info=get_info($this->table,$map);
		if($info && $info['type']=="save_path"){
			del_thumb($info['save_path']);
		}
		$de=delete_data($this->table,$map);
	}
	
	/**
	 * 删除处理
	 */
	public function ajaxDelete_banner(){
		$posts=I("post.");
		$info=get_info($this->table,array("id"=>$posts['id']));
		$path=$info['save_path'];
		$_POST=null;
		F("banner_".I('post.site_id'),null);
		if(file_exists($path)){
			$result=del_thumb($path);
			if($result){
				$this->success("删除成功");
			}else{
				$this->error("删除失败");
			}
		}else{
			$_POST['id']=$posts['id'];
			$_POST['save_path']='';
			update_data($this->table,array("id"=>$posts['id']));
			$this->success("文件不存在，删除失败，数据被清空");
		}
	}

	/**
	 * 改变状态
	 * @param string $field 更新字段
	 */
	function setStatus($field="status"){
		$adspace_id=I("adspace_id");
		$ids=I('ids');
		$field_val=intval(I('get.'.$field));
		if(is_array($ids)){
			$_POST=array(
				$field=>$field_val,
			);
			$map['id']=array('in',$ids);
			M($this->table)->where($map)->save($_POST); // 根据条件更新记录
		}else{
			$ids=intval($ids);
			$_POST=array(
				'id'=>$ids,
				$field=>$field_val,
			);
			$result=update_data($this->table);
			if(!is_numeric($result)){
				$this->error($result);
			}
		}
		F("banner_".I('post.site_id'),null);
		$this->success('操作成功',U('lists',array('adspace_id'=>$adspace_id)));
	}
}
