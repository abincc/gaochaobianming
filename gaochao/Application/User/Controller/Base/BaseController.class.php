<?php
namespace User\Controller\Base;
use Home\Controller\Base\AdminController;
class BaseController extends AdminController {
	
	/**
	 * 初始化
	 */
	protected function _init(){
		
		/**
		 * @TODO: 模拟登录
		 */
		/*$info = get_info('member', array('id'=>array('gt',0)));
		session("member_info",$info);
		session("member_id",$info['id']);
		session("last_login_time",time());
		session("last_login_ip","last_login_ip");
		session("login_time","login_time");
		session("login_ip","login_ip");
		*/
		//session(null);
		if(!session('member_id')){
			header("location:".U('User/Account/Login/index'));
		}
		$this->assign_menu();
		
	}
	/**
	 * 配置导航
	 */
	private function get_menu(){
		/**
		 * icon图片
		 * 来源：www.iconfont.cn
		 * 规格：18*18	颜色：#f60
		 */
		$icon_path=__ROOT__.'/Public/User/img/side_icon/';
		$menu=array(
			array(
				'title'=>'账号管理',
				'url'=>'#',
				'icon'=>$icon_path.'account.png',
				'_child'=>array(
					array(
						'title'=>'安全设置',
						'url'=>U("User/Set/Safe/index"),
						'_child'=>array(
							array(
								'title'=>'修改登录密码',
								'url'=>U("User/Set/Safe/login_password_mobile"),
							),
							array(
								'title'=>'修改登录密码',
								'url'=>U("User/Set/Safe/login_password_email"),
							),
							array(
								'title'=>'修改登录密码',
								'url'=>U("User/Set/Safe/login_password_deal"),
							),
							array(
								'title'=>'修改支付密码',
								'url'=>U("User/Set/Safe/deal_password_mobile"),
							),
							array(
								'title'=>'验证老手机',
								'url'=>U("User/Set/Safe/change_mobile"),
							),
							array(
								'title'=>'绑定新手机',
								'url'=>U("User/Set/Safe/bind_mobile"),
							),
							array(
								'title'=>'验证老邮箱',
								'url'=>U("User/Set/Safe/change_email"),
							),
							array(
								'title'=>'绑定新邮箱',
								'url'=>U("User/Set/Safe/bind_email"),
							),
						)
					),
					array(
						'title'=>'帐号绑定',
						'url'=>U("User/Set/Bind/index")
					),
					array(
						'title'=>'设置资料',
						'url'=>U("User/Set/Info/index"),
						'_child'=>array(
							array(
								'title'=>'个人资料',
								'url'=>U("User/Set/Info/index"),
							),
							array(
								'title'=>'头像照片',
								'url'=>U("User/Set/Info/avatar"),
							)
						)
					)
				),
			),
			array(
				'title'=>'订单管理',
				'url'=>'#',
				'icon'=>$icon_path.'order.png',
				'_child'=>array(
					array(
						'title'=>'商城订单',
						'url'=>U("User/Order/Order/index")
					),
					array(
						'title'=>'一元购订单',
						'url'=>U("User/Order/Order/one")
					),
					array(
						'title'=>'抽奖订单',
						'url'=>U("User/Order/Order/lottery")
					)
				),
			),
			array(
				'title'=>'评论管理',
				'url'=>'#',
				'icon'=>$icon_path.'comment.png',
				'_child'=>array(
					array(
						'title'=>'商品评论',
						'url'=>U("User/Comment/Comment/index")
					),
					array(
						'title'=>'一元购评论',
						'url'=>U("User/Comment/Comment/one")
					),
					array(
						'title'=>'装修评论',
						'url'=>U("User/Comment/Comment/service_comment")
					),
				),
			),
			array(
				'title'=>'售后列表',
				'url'=>U("User/Service/Service/index"),
				'icon'=>$icon_path.'service.png',
				'_child'=>array(
					array(
						'title'=>'商城订单售后',
						'url'=>U("User/Service/Service/index")
					),
					array(
						'title'=>'装修订单售后',
						'url'=>U("User/Service/Service/renovation")
					),
					array(
						'title'=>'投诉列表',
						'url'=>U("User/Service/Complaint/index")
					),
				)
			),
			array(
				'title'=>'资金管理',
				'url'=>'#',
				'icon'=>$icon_path.'capital.png',
				'_child'=>array(
					array(
						'title'=>'资金充值',
						'url'=>U("User/Capital/Recharge/index")
					),
					array(
						'title'=>'余额提现',
						'url'=>U("User/Capital/Withdraw/index")
					),
					array(
						'title'=>'用户转账',
						'url'=>U("User/Capital/Transfer/index")
					),
					array(
						'title'=>'收支记录',
						'url'=>U("User/Capital/Record/index")
					),
					array(
						'title'=>'银行卡管理',
						'url'=>U("User/Capital/Bank/index")
					)
				)
			),
			array(
				'title'=>'收货地址',
				'url'=>U("User/Address/Address/index"),
				'icon'=>$icon_path.'address.png'
			),
			array(
				'title'=>'我的收藏',
				'url'=>'#',
				'icon'=>$icon_path.'collect.png',
				'_child'=>array(
					array(
						'title'=>'服务商店铺',
						'url'=>U("User/Collect/Collect/shop")
					),
					array(
						'title'=>'商城店铺',
						'url'=>U("User/Collect/Collect/mall")
					),
					array(
						'title'=>'商品收藏',
						'url'=>U("User/Collect/Collect/product")
					)
				),
			),
			array(
				'title'=>'消息中心',
				'url'=>'#',
				'icon'=>$icon_path.'message.png',
				'_child'=>array(
					array(
						'title'=>'列表页',
						'url'=>U("User/Message/Receive/system"),
						'_child'=>array(
							array(
								'title'=>'详情页',
								'url'=>U("User/Message/Receive/system_detail")
							),
						),
					),
				),
			),
			array(
				'title'=>'我的推广',
				'url'=>U("User/Index/Index/fenxiao"),
				'icon'=>$icon_path.'share.png'
			),
		);
		return $menu;
	}
	
	/**
	 * 处理导航
	 */
	private function assign_menu(){
		$menu = $this->get_menu();
		$current_url = U('');
		$data = array('crumbs_array' => array());
		/*递归寻找当前选中菜单并设置样式*/
		foreach($menu as $key => &$row){
			$row['class'] = '';
			if(isset($row['_child'])){
				$row['class'] = 'has_child';
			}
		};
		$format = null;
		$format = function(&$menu) use (&$format,&$data,$current_url){
			foreach($menu as &$row){
				if(isset($row['_child'])){
					$format($row['_child']);
				}
				if(!isset($data['crumbs']) && $row['url'] == $current_url){
					$data['crumbs'] = $row['title'];
				}
				if(isset($data['crumbs'])){
					$row['active'] = 'active';
					$row['class'] = isset($row['class']) ? $row['class'] . ' cur' : ' cur';
					array_unshift($data['crumbs_array'],$row);
					return true;
				}
			}
		};
		$format($menu,$data);
		$data["sideMenu"] = $menu;
		array_unshift($data['crumbs_array'], array("title"=>"个人中心","url"=>U("Index/Index")));
		$this->assign($data);
	}
}
