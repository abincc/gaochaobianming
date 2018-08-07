<?php
namespace Backend\Controller\Tbook;
use Common\Controller\ExcelController;

/**
 * 体检报告
 * @author llf
 * @time 2017-11-2
 */
class TbookController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'Tbook';
	protected $map			= array('is_del'=>0);
	
	/**
	 * 列表函数
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
			$map['title|number'] = array('like','%' . trim(I('keywords')) . '%');
		}
		// if(strlen(I('status'))){
		// 	$map['status'] = I('status',0,'int');
		// }

		$start = !empty($post['start_date'])?$post['start_date']:0;
		$end   = !empty($post['stop_date'])?($post['stop_date'].' 23:59:59'):date('Y-m-d H:i:s');
		$map['add_time'] = array('BETWEEN',array($start,$end));

		$result = $this->page($this->table,$map,'sort desc,add_time desc');

		// $result['status'] = get_table_state($this->table,'status'); 

		// $result['coupon_list'] = array_to_select(get_no_hid('coupon'),$info['coupon_id']);

		$this->assign($result);
		$this->display();
	}

	/**
	 * 添加
	 * @author llf
	 * @time 2017-09-14
	 */
	public function add(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}

	/**
	 * 编辑
	 * @author llf
	 * @time 2017-09-14
	 */
	public function edit(){
		if(IS_POST){
			$this->update();
		}else{
			$this->operate();
		}
	}

	/**
	 * 显示
	 * @author llf
	 * @time 2017-09-14
	 */
	public function detail(){
		$this->operate('detail');
	}

	/**
	 * 显示
	 * @author llf
	 * @time 2017-09-14
	 */
	protected function operate($tpl='operate'){
		$info = get_info($this->table, array('id'=>I('ids')));

		$info['username'] = M('member')->where('id='.intval($info['member_id']))->getField('username');
		$info['mobile']   = M('member')->where('id='.intval($info['member_id']))->getField('mobile');
		$info['order_no'] = M('order')->where('id='.intval($info['order_id']))->getField('order_no');

		$data['info'] = $info;
		// $data['coupon_info'] = M('coupon')->where('id='.intval($info['coupon_id']))->find();
		// // $data['coupon_list'] = array_to_select(get_no_hid('coupon'),$info['coupon_id']);
		// $data['status'] = get_table_state($this->table,'status'); 
		
		$this->assign($data);
		$this->display($tpl);
	}
	
	/**
	 * 修改
	 * @author llf
	 * @time 2017-09-14
	 */
	protected function update(){

		if(!IS_POST)	$this->error('提交方式错误');

		$data = I('post.');

		/*基本信息*/
		$post = array();
		$post['title'] 	 = I('title','','trim');
		$post['number']  = I('number','','trim');
		$post['address'] = I('address','','trim');
		$post['sort'] 	 = I('sort',0,'int');

		$Tbook = D('Tbook');
		if(is_numeric($data['id'])){
			$has = $Tbook->where('id='.intval($data['id']))->find();
			if(!empty($has)){
				$post['id'] = intval($has['id']);
			}
		}
		try {
            $M = M();
            $M->startTrans();
		
			/*基本信息*/
			$result = update_data($Tbook, [], [], $post);
			if(!is_numeric($result)){
				throw new \Exception($Tbook->getError());
			}

		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}

		$M->commit();
		$this->success('操作成功',U('index'));
	}

    /**
     * 电话号码导入
     * @author xiaobin
     * @param type
     */
    public function import(){

        if(IS_POST){
            $type = intval(I('type'));
            if(!$type && !in_array(1,2)){
                //1-覆盖 2-累加
                $this->error('请选择库存导入模式');
            }
            //上传导入文件
            $config = array(
                'maxSize'    => 3145728,
                'rootPath'   => './',				//相对网站根目录
                'savePath'   => 'Uploads/Tbook/Tbook/',	//文件具体保存的目录
                'saveName'   => array('uniqid',''),
                'exts'       => array('csv','xlsx','xls'),
                'autoSub'    => true,
                'subName'    => array('date','Ymd'),
            );
            $Upload 	 = new \Think\Upload($config);        /* 实例化上传类 */
            $file_info   = $Upload->upload();
            if(!$file_info){
                $this->error("导入文件上传失败");
            }
            // $file =  'C:\Users\cnsunrun\Downloads\sku_export_20170502_182536.xls';
            // $file = './Uploads/Product/Sku/20170502/59085cb6df670.xls';
            $file = './'.$file_info['file']['savepath'].$file_info['file']['savename'];

            $import_config = array(
                array('title' => 'title', 'name' => 'title'),
                array('title' => 'number', 'name' => 'number'),
                array('title' => 'address', 'name' => 'address'),
                array('title' => 'sort', 'name' => 'sort'),
            );
            $data = A('Common/Excel','',1)->im_excel($import_config, $file);
//             var_dump($data);
//             die();
            try{
                if(empty($data)){
                    throw new \Exception("导入数据内容未空或导入文件解析失败");
                }
                //开启事务进行循环插入操作
                $SKU = M("tbook");
                $M = M();
                $M->startTrans();;
                if($type == 1){
                    foreach ($data as $k => $v) {
                        $datasql = array(
                            'title'     => strval($v['title']),
                            'number'    => $v['number'],
                            'sort'      => $v['sort'],
                            'address'   => $v['address'],
                            'add_time'  => date('Y-m-d H:i:s'),
                        );
                        $res = $SKU->add($datasql);
                        if(!is_numeric($res)){
                            throw new \Exception("库存信息更新失败");
                        }
                    }
                }else if($type == 2){
                    foreach ($data as $k => $v) {
                        $datasql = array(
                            'title'     => strval($v['title']),
                            'number'    => $v['number'],
                            'sort'      => $v['sort'],
                            'address'   => $v['address'],
                            'add_time'  => date('Y-m-d H:i:s'),
                        );
                        $res = $SKU->add($datasql);
                        if(!is_numeric($res)){
                            throw new \Exception("库存信息更新失败");
                        }
                    }
                }

            }catch (Exception $e){
                $M->rollback();
                $this->error($e->getMessage());
            }
            $M->commit();
            $this->success('导入数据成功');
        }else{
            $this->display();
        }

    }
}

