<?php
namespace Home\Controller\Cart;
use Home\Controller\Base\AdminController;
class OrderController extends AdminController {
	/*
     * 确认订单信息
     * 陈梦 2016/06/07
     */
    public function confirm(){
	    $this->display();
    }
    /**
     * 订单生成
     */
    public function generate() {
        if(IS_POST) {
            $url = U('Home/Cart/Cart/pay');
            $this->success('订单生成成功', $url);
        }
    }

    /**
     * 余额支付
     */
    public function pay_balance() {
        if(IS_POST) {
            $this->success('',U('Home/Cart/Order/pay_success'));
        }else{
            $this->$this->display();
        }
    }
}