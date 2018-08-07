<?php
namespace User\Controller\Comment;
use User\Controller\Base\BaseController;
class CommentController extends BaseController {
	/**
	 * 店铺评论
	 * @return [type] [description]
	 */
	public function index(){
        $this->display();
    }
    /**
     * 商品评论
     * @return [type] [description]
     */
	public function shop(){
        $this->display();
    }
    /**
     * 编辑评论
     * @return [type] [description]
     */
    public function edit(){
        $this->display();
    }

    /**
     * 删除评论图片
     */
    public function ajaxDelete_comment_image(){
        $this->error("删除失败！");
    }

    /**
     * 一元购评论
     * @return [type] [description]
     */
	public function one(){
        $this->display();
    }

    /**
     * 装修评论
     */
    public function service_comment(){
        $this->display();
    }
     /**
     * 修改装修订单评论
     * @return [type] [description]
     */
    public function update_service_comment(){
        $this->display();
    }
}