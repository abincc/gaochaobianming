<?php
namespace Api\Controller\User;
use Api\Controller\Base\BaseController;
use Org\JPush\JPush;

	/**
	 * 消息模块接口
	 */
class ChatController extends BaseController{

	protected $table  ='MemberRoom';
	
	/**
	 * 聊天信息发送
	 * @time 2018-06-12 
	 * @author abincc 
	 */
	 public function sendMessage(){

	 	// if(!IS_POST)   $this->apiReturn(array('status'=>'0', 'msg'=>'请求类型错误'));
	 	
	 	$uid = I('uid', '', 'trim');//发送的用户id
	 	$mname = I('mname', '', 'trim');//发送者名字
	 	$content = I('content', '', 'trim');// 内容
	 	$roomid = I('rid', '','int');// 房号
	 	$type = I('type', '', 'int');//聊天类别

	 	//上一个时间
	 	$dateTime = I('dataTime','','');

	 	$extras = array(
    		'id'         => 2,
    		'type' 	     => 5,
    		'rid'        => $roomid,
    		'mid'        => $this->member_id,
    		'content'    => $content
    	);

	 	$startdate=$dateTime;

		$enddate= date('Y-m-d H:i:s');


		$minute=floor((strtotime($enddate) - strtotime($startdate))%86400/60);

		$date=floor((strtotime($enddate) - strtotime($startdate))/86400);
		 
		$hour=floor((strtotime($enddate)-strtotime($startdate))%86400/3600);

		if($minute < 5 && $date == 0 && $hour == 0){
			try {
				$M = M();
				$M->startTrans();
				
				$data['type'] = $type;
				$data['rid'] = $roomid;
				$data['uid'] = $this->member_id;
				$data['content'] = $content;
				$data['time'] = date('Y-m-d H:i:s');
				$result = update_data(D('MemberMessage'),[],[],$data);
				if(!is_numeric($result)){
					$this->api_error('信息保存失败',array('status'=> '0'));
				}

				 # 极光推送
				$res = JPush::push($uid, $mname, $content,$extras);
				if(!$res){
					$this->api_error('信息推送失败',array('status'=> '0'));
				}
			} catch (\Exception $e) {
				$M->rollback();
				$this->api_error($e->getMessage());
			}
			$M->commit();

			$this->api_success('信息已发出', array('status'=> '1'));
		}else{

			try {
				$M = M();
				$M->startTrans();

				$map['type'] = 5;
				$map['rid'] = $roomid;
				$map['uid'] = $this->member_id;
				$map['content'] = '';
				$map['time'] = date('Y-m-d H:i:s');
				$result = update_data(D('MemberMessage'),[],[],$map);
				if(!is_numeric($result)){
					$this->api_error('信息保存失败',array('status'=> '0'));
				}

				$data['type'] = $type;
				$data['rid'] = $roomid;
				$data['uid'] = $this->member_id;
				$data['content'] = $content;
				$data['time'] = date('Y-m-d H:i:s');
				$result = update_data(D('MemberMessage'),[],[],$data);
				if(!is_numeric($result)){
					$this->api_error('信息保存失败',array('status'=> '0'));
				}

				$extras['time'] = date('Y-m-d H:i:s');

				 # 极光推送
				$res = JPush::push($uid, $mname, $content,$extras);
				if(!$res){
					$this->api_error('信息推送失败',array('status'=> '0'));
				}
			} catch (\Exception $e) {
				$M->rollback();
				$this->api_error($e->getMessage());
			}
			$M->commit();
			$this->api_success('信息已发出', array('status'=> '2'));
		}
	 }


	 /**
	  * 如果房间存在则获取房间信息， 否则者先创建房间，在获取房间信息
	  * @author abincc
	  * @time 2018-06-14
	  */
	 public function getRoom(){
	 	// if(!IS_POST)	$this->apiReturn(array('status'=>'0','msg'=>'请求方式错误'));

	 	$data['oid'] = I('oid', '', 'int');
	 	$data['mid'] = $this->member_id;
	 	$data['time'] = date('Y-m-d H:i:s');

	 	$map = array(
	 		'is_del' => 0,
	 		'mid'    => $this->member_id,
	 		'oid'    => $data['oid']
	 	);

	 	$map1 = array(
	 		'is_del' => 0,
	 		'mid'    => $data['oid'],
	 		'oid'    => $this->member_id
	 	);


	 	$info = M('MemberRoom')->where($map)->find();
	 	if(empty($info)){
	 		$info = M('MemberRoom')->where($map1)->find();
	 		if(empty($info)){
	 			try {
					$M = M();
					$M->startTrans();
					$result = update_data(D('MemberRoom'),[],[],$data);
					if(!is_numeric($result)){
						throw new \Exception($result);
					}
				} catch (\Exception $e) {
					$M->rollback();
					$this->api_error($e->getMessage());
				}
				$M->commit();

				$info = M('MemberRoom')->where($map)->find();
	 		}
	 	}

		$this->api_success('房间信息', $info);
	 }


	 /**
     * @author xiaobin
     * @time 2018-06-08
     */
    public function get_room_user_info(){

    	$map = array(
	 		'is_del' => 0,
	 		'id'     => I('rid', '', 'int')
	 	);

    	$info = M('MemberRoom')->where($map)->find();
    	if(empty($info)){
    		$this->api_error('该房号不存在');
    	}

    	$info1 = M('member')->where(array('id'=>$this->member_id))->field('id, nickname, mobile')->find();
    	$info1['headimg'] = get_avatar($this->member_id);

    	if($info['mid'] == $this->member_id){
    		$info2 = M('member')->where(array('id'=>$info['oid']))->field('id, nickname, mobile')->find();
    		$info2['headimg'] = get_avatar($info['oid']);
    	}else{
    		$info2 = M('member')->where(array('id'=>$info['mid']))->field('id, nickname, mobile')->find();
    		$info2['headimg'] = get_avatar($info['mid']);
    	}

    	$data['me'] = $info1;
    	$data['other'] = $info2;
    	$this->api_success('ok',$data);
    }   


    /** 
     *  根据房号获取对应的聊天信息
     *  @author abincc
     *  @time 2018-06-14
     */
    public function getAllChatMessage(){

    	$map = array(
    		'is_del'  => 0,
    		'is_back' => 0,
    		'rid'     => I('rid', '', 'int')
    	);

    	$list  = M('MemberMessage')->where($map)->order('time asc')->select();

    	$this->api_success('ok',(array)$list);

    }


    /**
      *  获取更多的聊天信息
      *  @author abincc
      *  @time 2018-06-19
      */

    //  待开发






    /**
     *
     *  获取用户对应的聊天房间
     *  @author abincc
     *  @time 2018-06-19
     */
	public function roomList(){

    	$where['mid'] = $this->member_id;
    	$where['oid'] = $this->member_id;
    	$where['_logic'] = 'or';
    	$map['_complex'] = $where;
    	$map['is_del']  = 0;

    	

		$list  = $this->page(D('MemberRoom'),$map,'time desc');
		foreach ($list as $k => $v) {
			if($v['oid'] == $this->member_id){
				$m_id = $v['mid'];
			}else{
				$m_id = $v['oid'];
			}
			$member_info = M('Member')->where('id='.$m_id)->field('id,nickname,mobile')->find();
			$member_info['headimg'] = get_avatar($m_id);
			if(!empty($member_info)){
				$list[$k]['member'] = $member_info;
			}


			//获取最后一个聊天信息
			$m_msg = M('MemberMessage')->where('rid='.$v['id'])->field('id, content')->order('time desc')->limit(1)->select();
			if(!empty($m_msg)){
				$list[$k]['msg'] = $m_msg[0];
			}else{
				$msg['id'] = 0;
				$msg['content'] = '暂无';
				$list[$k]['msg'] = $msg;
			}
		}

		$this->api_success('ok',(array)$list);
    }
}
