<?php
namespace User\Controller\Set;
	/**
	 * 资料设置
	 */
class InfoController extends IndexController { 
	/**
	 * 表名
	 * @var string
	 */
	protected $table='member';
	
	/**
	 * 个人资料修改
	 * @author 秦晓武
	 * @time 2016-08-16
	 */
	public function index(){
		if(!IS_POST){
			$data['info'] = get_info('member_info', array('member_id' => session('member_id')));
			$this->assign($data);
			return $this->display();
		}
		
		$flag = trans(function(){
			/*昵称存入member表中*/
			$data = array(
				'id' => session('member_id'),
				'nickname' => I('post.nickname'),
			);
			$rules = array(
				['nickname','1,10','昵称字数为1到10个字符','0','length'],
			);
			$result[] = update_data('member',$rules,array(),$data);
			
			/*其他存入member_info表中*/
			$info = get_info('member_info', array('member_id' => session('member_id')));
			$data = array(
				'birthday' => strtotime(I('post.birthday')),
				'gender' => I('post.gender'),
				'realname' => I('post.realname'),
			);
			/*有信息更新，没信息创建*/
			if($info['id']){
				$data['id'] = $info['id'];
			}
			else{
				$data['member_id'] = session('member_id');
			}
			$rules = array(
				['realname','1,6','真实姓名为1到6个字','0','length'],
				['gender','require','请选择性别'],
				['birthday','require','请输入生日'],
			);
			$result[] = update_data('member_info',$rules,array(),$data);
			return $result;
		});
		if($flag){
			$this->error($flag);
		}
		$member_info = get_info($this->table, array('id' => session('member_id')));
		session('member_info',$member_info);
		$this->success('保存成功');
	}

	/**
	 * 裁剪并保存用户头像
	 * @author 秦晓武
	 * @time 2016-08-16
	 */
	public function avatar(){
		if(!IS_POST){
			return $this->display();
		}
		/*图片裁剪数据*/
		$params = I();                       
		/*裁剪参数*/
		if(!isset($params) && empty($params)){
			return '';
		}
		/*头像后缀*/
		$ext = pathinfo($params['picName'],PATHINFO_EXTENSION);
		if($ext==''){
			$this->error("图片格式错误");
		}
		/*头像临时目录地址*/
		$temp_path = "./".$params['picName'];
		if(!file_exists($temp_path)){
			$this->error("文件不存在");
		}
		/*要保存的图片*/
		$real_path = './Uploads/Avatar/'.session("member_id").'.'.$ext;

		/*头像最后目录地址*/
		$end_path = './Uploads/Avatar/';
		if(!is_dir($end_path) && !mkdir($end_path)){
			$this->error("目录不存在");
		}
		$has1=$end_path.session("member_id").'_200.jpg';
		$has2=$end_path.session("member_id").'_200.png';
		$has3=$end_path.session("member_id").'_200.gif';
		/*删除已存在同名图片*/
		if(file_exists($has1)){
			unlink($has1);
		}
		if(file_exists($has2)){
			unlink($has2);
		}
		if(file_exists($has3)){
			unlink($has3);
		}
		$ThinkImage = new \Think\Image();
		/*裁剪原图*/
		$ThinkImage->open($temp_path)->crop($params['w'],$params['h'],$params['x'],$params['y'])->save($real_path);
		/*生成缩略图*/
		$ThinkImage->open($real_path)->thumb(200,200, 1)->save($end_path.session("member_id").'_200.'.$ext);
		$ThinkImage->open($real_path)->thumb(100,100, 1)->save($end_path.session("member_id").'_100.'.$ext);
		$ThinkImage->open($real_path)->thumb(60,60, 1)->save($end_path.session("member_id").'_60.'.$ext);
		unlink($temp_path);
		$this->success("保存成功");
	}
    
}
