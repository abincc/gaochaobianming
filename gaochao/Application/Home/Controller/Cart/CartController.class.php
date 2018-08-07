<?php
namespace Home\Controller\Cart;
use Home\Controller\Base\AdminController;
class CartController extends AdminController {
	/*
     * 购物车
     * 陈梦 2016/06/07
     */
    public function index(){
	    $this->display();
    }
	/*
     * 购物车
     * 陈梦 2016/06/07
     */
    public function edit(){
	    $this->success();
    }

    /**
     * 余额支付
     */
    public function pay_balance() {
        if(IS_POST) {
            $this->success('', U('/Order/Order/pay_success'));
        }
    }
}