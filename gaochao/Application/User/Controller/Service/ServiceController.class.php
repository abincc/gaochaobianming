<?php
namespace User\Controller\Service;
use User\Controller\Base\BaseController;
class ServiceController extends BaseController {
	/**
	 * 售后列表
	 * @see Mall/widget/RefundWidget
	 * @author 邹义来
	 */
	public function index(){
        $this->display();
    }
    /**
     * 售后详情页
     * @see Mall/widget/RefundWidget
     * @author 邹义来
     */
    public function details(){
    	$this->display();
    }

    /**
     * 买家申请售后，已确认收货
     * @return [type] [description]
     */
    public function apply(){
		if(IS_POST) {
			$this->success('操作成功！');
		}
		$this->display();
    }
    /**
     * 申请售后成功
     * @return [type] [description]
     */
   	public function apply_success() {
		$this->display();
	}
	/**
	 * 相册图片删除
	 */
	public function ajaxDelete_refund_image() {
		$map['refund_id']=I('id','','int');
		$map['id']=I('image_id','','int');
		$info=get_info('refund_image',$map);
		if($info){
			$res=M('refund_image')->where($map)->delete();
			if($res){
				@unlink($info['image']);
				$this->success("删除成功！");
			}else{
				$this->error("删除失败！");
			}
		}else{
			$this->error("图片不存在");
		}
	}
	/**
	 * 售后申请修改
	 */
	public function apply_edit() {
		if(IS_POST){
			$this->success("修改成功",U('index'));
		}
		$this->display();
	}
	/**
	 * 申请售后，已确认收货，数据处理
	 * @流程：
	 * 1、添加售后信息
	 * 2、修改订单状态
	 * 3、修改详情状态
	 * 4、记录操作日志
	 * @param  [type]  $info   订单信息
	 * @param  integer $status 要设置的状态值
	 * @return [type]          [description]
	 */
	protected function _apply($info, $status = 50) {
		return $refund_id;
	}

	public function recede($status = 52) {
		if(IS_POST) {
			$this->error('数据错误');
		}
	}

	/**
	 * 买家取消订单
	 */
	public function cancel(){
		$this->success("取消成功！");
	}
	/**
	 * 买家申请退款，未发货
	 */
	public function apply_refund() {
		$this->success('申请成功', U('index'));
	}
	/**
	 * 买家确认收货
	*/
	public function confirm_receipt(){
		$this->success("操作成功！");
	}

	/**
	 * 装修订单详情
	 * @return [type] [description]
	 */
	public function renovation_details(){
		$this->display();
	}
}