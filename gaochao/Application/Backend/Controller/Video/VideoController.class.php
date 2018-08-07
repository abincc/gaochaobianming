<?php
namespace Backend\Controller\Video;
use Common\Controller\ExcelController;

/**
 * 体检报告
 * @author llf
 * @time 2017-11-2
 */
class VideoController extends IndexController {
	/**
	 * 表名
	 * @var string
	 */
	protected $table 		= 'video';
	protected $map			= array('is_del'=>0, 'is_hide'=>0);
	
	/**
	 * 列表函数
	 */
	public function index(){

		$post = I();
		$map  = $this->map;

		$result = $this->page($this->table,$map,'add_time desc');
		$catch = get_no_hid('district','add_time desc');
		array_unshift($catch, ['id'=>0,'title'=>'-全部小区-']);
		if(strlen(I('district_id'))){
			$result['district'] = array_to_select($catch,I('district_id'));
		}else{
			$result['district'] = array_to_select($catch);
		}
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

		$district_list	  = M('district')->where(array('is_hid'=>0,'is_del'=>0))->select();
		array_unshift($district_list, ['id'=>0,'title'=>'-全部小区']);
		$data['district'] = array_to_select($district_list, $info['district_id']);
		$data['info'] = $info;
		
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
		$data = array();
		$data['ip'] 	 = I('ip','','trim');
		$data['port']  = I('port','','trim');
		$data['channel'] = I('channel','','int');
		$data['update_time'] = date('Y-m-d H:i:s');
		$data['add_time'] = date('Y-m-d H:i:s');

		if(I('id','', 'int')){
            $data['id'] = intval(I('id','', 'int'));
            unset($data['add_time']);
        }
        $map = array(
        	'ip'      => $data['ip'],
        	'port' 	  => $data['port'],
        	'channel' => $data['channel']
        );

        $video = M($this->table)->where($map)->find();
        if(!empty($video)){
        	$this->error($data['ip'].':'.$data['port'].'--'.$data['channel'].'已存在');
        }

        $url='http://'.$data['ip'].':'.$data['port'].'/api/v1/getchannels?channel='.$data['channel'];

        $pone = array();

        $result = curl_post($url,$pone);
		$result = json_decode($result,true);

		if($result['EasyDarwin']['Header']['ErrorNum'] != 200){
			$this->error('获取通道信息失败');
		}

		$data['version'] = 'v1';
		$data['title']   = $result['EasyDarwin']['Body']['Channels'][0]['Name'];

		try {
            $M = M();
            $M->startTrans();
		
			/*基本信息*/
			$result = update_data($this->table, [], [], $data);
			if(!is_numeric($result)){
				throw new \Exception('添加失败');
			}
		} catch (\Exception $e) {
			$M->rollback();
			$this->error($e->getMessage());
		}

		$M->commit();
		$this->success('操作成功',U('index'));
	}

	public function msecdate($tag, $time){
		$a = substr($time,0,10);
		$b = substr($time,10);
		$date = date($tag,$a).'.'.$b;
		return substr($date,0,19);
	}

}

