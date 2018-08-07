<?php
/**
 * 首页
 */
namespace Home\Controller\Index;
use Home\Controller\Base\AdminController;
use Api\Org\Forum\Forum;

class IndexController extends AdminController {
	
	public function index(){
        $info['US_ADDRESS'] 	= M('config')->where(array('name'=>'US_ADDRESS'))->getField('value');
        $info['US_EMAIL'] 	= M('config')->where(array('name'=>'US_EMAIL'))->getField('value');
        $info['US_TELPHONE'] 	= M('config')->where(array('name'=>'US_TELPHONE'))->getField('value');
        $info['CONTACT_NUMBER'] 	= M('config')->where(array('name'=>'CONTACT_NUMBER'))->getField('value');
        $info['COPY_RIGHT'] 	= M('config')->where(array('name'=>'COPY_RIGHT'))->getField('value');
        $info['WEB_URL'] 	= M('config')->where(array('name'=>'WEB_URL'))->getField('value');
        $data['info'] = $info;
        $this->assign($data);
		$this->display();
	}

	/**
	 * 修改数据表字符集
	 */
	public function character() {
		$sql = 'show tables';
		$list = M()->query($sql);
		$_sql = '';
		foreach($list as $v) {
			$_sql .= 'alter table '.current($v).' convert to character set utf8mb4 collate utf8mb4_general_ci;';
		}
		M()->execute($_sql);
	}

	/**
	 * 商品详情内容 H5 页面
	 * @author llf
	 */
	public function product_detail_content(){

		$product_id = I('product_id',0,'int');

		$content = M('Product')->where('id='.$product_id)->getField('content');

		if(!empty($content)){
			$this->assign('content',$content);
		}
		$this->display('detail');
	}


	/**
	 * 商品分享链接页面
	 * @author llf
	 */
	public function product_share_info(){

		$product_id = I('product_id',0,'int');
		$map = array(
				'id'		=>$product_id,
				'is_del'	=>0,
				'is_hid'	=>0,
				'is_sale'	=>1,
			);
		$P 		= D('Product');
		$field 	= array('id as product_id','title','price','description','image as cover','content','spec','spec_value','inventory');
		$info 	= $P->where($map)->field($field)->find();
		if(empty($info)){
			$this->error('商品信息不存在');
		}		
		$info['cover']			= file_url($info['cover']);

		$info['images']			= $P->get_image($info['product_id']);

		/* 商品评论个数*/
		$info['comment_number'] = $P->comment_number($info['product_id']);

		/* 商品评论内容*/
		$maps 	= array(
				'product_id'	=>$product_id,
				'is_del'		=>0,
				'is_hid'		=>0,
			);
		$field 	= array('id as comment_id','content','star','member_id','reply','add_time');
		
		$_GET['p'] = I('p',0,'int');
		$list   = $this->page('Order_comment',$maps,'add_time desc',$field);
		$list   = $list['list'];
		if(!empty($list)){
			$comment_ids    = array_column($list,'comment_id');
			$condition    	= array(
								'pid' => array('IN',$comment_ids),
							);
			$field  = array('id as image_id','pid as comment_id','image');
			$images = M('order_commont_img')->where($condition)->field($field)->select();

			if(empty($image)){
				$temp = array();
				foreach ($images as $k => $v) {
					$temp[$v['comment_id']][] = file_url($v['image']);
				}
				foreach ($list as $key => $val) {
					$list[$key]['nickname']= 'test';
					$list[$key]['headimg']= get_avatar($val['member_id']);
					$list[$key]['images'] = (array)$temp[$val['comment_id']];
					unset($list[$key]['member_id']);
				}

			}

		}
		$info['comment'] = $list;

		$this->assign('info',$info);

		$this->display('info');
	}

	/**
	 * 商品分享链接页面
	 * @author llf
	 */
	public function thread_share_info(){
		$_data = [];
		$this->obj = new Forum();
		$member = D('Member');
		if($_data = $this->obj->thread_detail($member->check_token(I('get.')))) {
			$_data['post_list'] = [];

			if($thread_info = $this->obj->post_thread()) {
				$_map	= $this->obj->post_map_list(I('get.'));
				$_field	= 'id,post_id,member_id,member_nickname,content,likes,position,post_info,add_time';
				$list	= $this->page($this->obj->table_post, $_map, 'add_time asc', $_field);
				// if($list) $_data['post_list'] = $this->obj->post_list($list, $member->check_token(I('get.')));
			}
			$this->assign($_data);
			$this->display('index0');
		}else{
			$this->error('帖子信息不存在');
		}
	}

	/**
	 * 下载APP  安卓
	 * @author llf
	 */
	public function download_app(){

		$version 	= M('config')->where(array('name'=>'APP_VERSION'))->getField('value');
		$url 		= M('config')->where(array('name'=>'APP_UPLOAD_URL'))->getField('value');

		//检查文件是否存在
		if(!file_exists($url)){
			$this->error('文件不存在');
		}
		//下载文件
		\Org\Net\Http::download(ltrim($url,'./'), 'gcbm_'.$version.'.apk');

	}


	public function government_info(){

		$government_id = I('government_id',0,'int');
		
		$info = M('government')->where(['id'=>$government_id])->find();

		$this->assign('info',$info);
		$this->display('government');

	}
	
	/**
     * 邮件发送
     */
    public function send(){
        $name = I("name");
        $email = I("email");
        $tel = I("tel");
        $content = I("content");
        if(sendMail('1414041706@qq.com',$name.'-'.$email.'-'.$tel, '这是一篇测试邮件正文！'.$content)){
            $this->success('邮件发送成功', U('index'));
        }
        else{
            $this->error('邮件发送失败',U('index'));
        }
    }
}