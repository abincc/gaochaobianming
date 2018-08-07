<?php
namespace User\Controller\Order;
use User\Controller\Base\BaseController;
class OrderController extends BaseController {
	/*
	 * 商城订单列表页
	 */
	public function index(){
        $this->display();
    }
	/**
	 * 物流追踪
	 */
	public function tracking() {
		$this->success("操作成功！");
	}
	/**
	 * 商城订单详情
	 * @return [type] [description]
	 */
	public function details(){
		$this->display();
	}

	/**
	 * 添加商城评论
	 */
	public function comment(){
		$this->display();
	}

	/**
	 * 装修订单 展示
	 */
	public function renovation(){
		$this->display();
	}
	/**
	 * 服务订单评论
	 * @return [type] [description]
	 * @author 邹义来
	 */
	public function service_comment(){
		$this->display();
	}
	
	/**
	 * 买家申请售后
	 * @return [type] [description]
	 * @author 邹义来
	 * @time 2016-07-29
	 */
	public function apply_refund(){
		$this->display();
	}


	/**
	 * 设置状态值
	 * @param  [type] $field        操作人 seller_id,member_id
	 * @param  [type] $set_status   要设置的状态值
	 * @param  [type] $check_status 要验证的状态值，即目前的状态值
	 * @param  [type] $remark 操作日志记录操作
	 * @return [type]               [description]
	 * @author 邹义来
	 */
	public function update_status($field,$set_status,$check_status,$remark){
			$this->success("操作成功！");
	}
}