<?php
namespace Api\Controller\Common;
use Api\Controller\Base\BaseController;
use Api\Controller\Base\HomeController;

/**
 * 废品回收
 * @author bxiao
 * @time 2018-02-07
 */
class WasteController extends BaseController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Waste';
	protected $table_order = 'WasteOrder';
	protected $table_image = 'sr_waste_image';
    protected $image = 'WasteImage';
	protected $map			= array('is_del'=>0, 'is_hid'=>0);
	
	/**
	 * 废品种类列表
	 */
	public function index(){

		$post = I();
		$map  = $this->map;
		/**禁用*/
		if(strlen(I('is_hid'))){
			$map['is_hid'] = I('is_hid');
		}
		/**手机|昵称|姓名*/
		if(strlen(trim(I('keywords')))) {
			$map['title|price'] = array('like','%' . trim(I('keywords')) . '%');
		}

		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		$result = $this->page($this->table,$map,'sort desc,add_time desc');

        if(!empty($result)){
            array_walk($result, function(&$a){
                $a['image'] = file_url($a['image']);
            });
        }

        $this->api_success('ok',(array)$result);
	}

	/**
     * 生成废品订单
     */
	public function create()
    {
        if (!IS_POST) $this->api_error('请求方式错误');
        $address_id = I('address_id', 0, 'int');
        $details = I('details', '', 'trim');
        $date = I('date', '', 'trim');
//        $address_id = intval(215);
//        $details = "ssd";
//        $member_id = 209;
        write_debug(I(), '废品下单提交参数');
        if (!$address_id) {
            $this->api_error('请选择收货地址');
        }

        $member_info = M('Member')->where('id='.$this->member_id)->field('id,district_id,building_no,room_no')->find();
        if(empty($member_info)){
            $this->api_error('用户信息不存在');
        }

        $address_info = D('Address')->get_address($this->member_id, $address_id);
        if (empty($address_info)) {
            $this->api_error('地址信息不存在');
        }

        $data['order_no']		= build_order_no();
        $data['details'] = trim($details);
        $data['date'] = trim($date);
        $data['mobile'] = trim($address_info['mobile']);
        $data['district_id']	= intval($member_info['district_id']);
        $data['building_no'] 	= trim($member_info['building_no']);
        $data['room_no'] 		= trim($member_info['room_no']);
        $data['add_time'] = date('Y-m-d H:i:s');
        $data['member_id'] = $this->member_id;
        $data['address'] = serialize($address_info);
        $img[] = $_FILES['img'];

        try {
            $M = M();
            $M->startTrans();
            $id = update_data(D($this->table_order),[],[],$data);
            if(!is_numeric($id)){
                throw new \Exception($id);
            }
            if(empty($_FILES['img'])){
                throw new \Exception('请上传图片');
            }
             if(count($img) > 9){
             	throw new \Exception('上传图片数量不能超过9张');
             }
            $config = array(
                'maxSize'    => 3145728,
                'rootPath'   => './',
                'savePath'   => 'Uploads/Waste/',
                'saveName'   => array('uniqid',''),
                'exts'       => array('jpg', 'gif', 'png', 'jpeg'),
                'autoSub'    => true,
                'subName'    => array('date','Ymd'),
            );
            /*  处理上传图片*/
            $img_info = $this->upload_files($img,$config);
            if(empty($img_info)){
                throw new \Exception($this->error);
            }
            $images = array();
            foreach($img_info as $file){
                // $img_size = getimagesize('./'.$file['savepath'].$file['savename']);
                $images[] = array(
                    'pid' 		 =>$id,
                    'image'		 =>$file['savepath'].$file['savename'],
                    'add_time'	 =>date('Y-m-d H:i:s'),
                );
            }
            $sql = addSql($images,$this->table_image);
            $res = M()->execute($sql);
            if(!is_numeric($res)){
                /*清除图片文件*/
                array_walk($images, function($a){
                    @unlink ( $a['image'] );
                });
                unset($img_info);
                unset($images);
                unset($img);
                throw new \Exception('图片上传失败');
            }

        } catch (\Exception $e) {
            $M->rollback();
            $this->api_error($e->getMessage());
        }
        $M->commit();

        //消息推送
        D('Message')->send($data['member_id'], 2, '废品订单生成成功', '生成订单，订单编号为：' . $data['order_no']);

        $this->api_success("提交成功");
    }

    /**
     * 废品订单列表
     */
    public function all(){
        $map   = array(
            'member_id'	=> $this->member_id,
            'status'	=> array('IN',array(10,20,30)),
            'is_del'	=> 0,
            'is_hid'	=> 0,
        );

        $field  = array('id','order_no','add_time','details','status','date');
        $list  = $this->page($this->table_order,$map,'add_time desc',$field,12);
        if(!empty($list)){
            $status = get_table_state($this->table,'status');
            foreach ($list as $k => $v) {
                $list[$k]['status_title'] = $status[$v['status']]['title'];
            }
        }
        $this->api_success('ok',(array)$list);
    }

    /**
     * 废品已处理订单列表
     */
    public function deal(){
        $map   = array(
            'member_id'	=> $this->member_id,
            'status'	=> array('IN',array(20)),
            'is_del'	=> 0,
            'is_hid'	=> 0,
        );

        $field  = array('id','order_no','add_time','details','status','date');
        $list  = $this->page($this->table_order,$map,'add_time desc',$field,12);
        if(!empty($list)){
            $status = get_table_state($this->table,'status');
            foreach ($list as $k => $v) {
                $list[$k]['status_title'] = $status[$v['status']]['title'];
            }
        }
        $this->api_success('ok',(array)$list);
    }

    /**
     * 废品未处理订单列表
     */
    public function undeal(){
        $map   = array(
            'member_id'	=> $this->member_id,
            'status'	=> array('IN',array(10)),
            'is_del'	=> 0,
            'is_hid'	=> 0,
        );

        $field  = array('id','order_no','add_time','details','status','date');
        $list  = $this->page($this->table_order,$map,'add_time desc',$field,12);
        if(!empty($list)){
            $status = get_table_state($this->table,'status');
            foreach ($list as $k => $v) {
                $list[$k]['status_title'] = $status[$v['status']]['title'];
            }
        }
        $this->api_success('ok',(array)$list);
    }

    /**
     * 废品已取消订单列表
     */
    public function cancel(){
        $map   = array(
            'member_id'	=> $this->member_id,
            'status'	=> array('IN',array(30)),
            'is_del'	=> 0,
            'is_hid'	=> 0,
        );

        $field  = array('id','order_no','add_time','details','status','date');
        $list  = $this->page($this->table_order,$map,'add_time desc',$field,12);
        if(!empty($list)){
            $status = get_table_state($this->table,'status');
            foreach ($list as $k => $v) {
                $list[$k]['status_title'] = $status[$v['status']]['title'];
            }
        }
        $this->api_success('ok',(array)$list);
    }

    /**
     * 废品订单信息
     */
    public function info(){
        $map 	= array(
            'member_id'	=> $this->member_id,
            'id'		=> intval(I("id")),
        );

        $O		= D('WasteOrder');
        $field  = array('id','order_no','member_id','mobile','waste_type','district_id','building_no','room_no','address','add_time','details','status');
        $info 	= $O->where($map)->field($field)->find();
        if(empty($info)){
            $this->api_error('订单信息不存在');
        }
        $status = get_table_state('waste','status');
        $info['status_title']	 = $status[$info['status']]['title'];
        $info['address'] = (array)unserialize($info['address']);
        $this->api_success('ok',$info);
    }

    /**
     * 废品订单图片信息
     */
    public function imagesInfo(){

        $map 	= array(
            'pid'	=> intval(I("id")),
            'is_del'	=> 0,
            'is_hid'	=> 0,
        );

        $field = array('image','id','pid');

        $list  = $this->page($this->image,$map,'add_time desc',$field,9);

        if(!empty($list)){
            array_walk($list, function(&$a){
                $a['image'] = file_url($a['image']);
            });
        }
        $this->api_success('ok',$list);
    }

    /**
     * 取消订单
     */

    public function delOrder(){
        if(!IS_POST)	$this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));

        $data = array();
        $data['id']	= I('post.id','','int');
        $data['member_id']	= $this->member_id;
        $data['status']  = 30;

        $has = D('WasteOrder')->where('member_id='.$data['member_id'])->where('id='.$data['id'])->field(array('id'))->find();
        if(empty($has)){
            $this->apiReturn(array('status'=>'0','msg'=>'11'));
        }

        $result = update_data(D('WasteOrder'),[],[],$data);
        if(!is_numeric($result)){
            $this->apiReturn(array('status'=>'0','msg'=>$result));
        }

        $this->apiReturn(array('status'=>'1','msg'=>'订单取消成功'));
    }

}

