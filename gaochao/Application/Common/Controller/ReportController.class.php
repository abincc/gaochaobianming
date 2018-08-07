<?php
/**
 * Reoprt处理类
 * @作用：处理数据统计报表
 * @time 2017-01-11
 * @author 秦晓武
 */
namespace Common\Controller;
use Think\Controller;

class ReportController extends Controller {
	protected $table = array('data'=>'','report'=>'');
	/**
	 * 刷新用户表记录
	 */
	public function refrash_member(){
		$this->table['data'] = 'member';
		$this->table['report'] = 'report_member';
		/*根据最新ID查找最新数据*/
		$info_1 = get_info($this->table['report'],array('is_del'=>0),'max(id) as id');
		$last_report = get_info($this->table['report'],array('id' => $info_1['id']),true);
		/*无数据则初始化*/
		if(!$last_report['id']){
			/*根据最老ID查找最老数据*/
			$temp_info = get_info($this->table['data'],array('register_time'=>array('gt',0)),'min(id) as id');
			$temp_info = get_info($this->table['data'],array('id' => $temp_info['id']),true);
			/*没有数据就返回*/
			
			if(!$temp_info['id']){
				return '';
			};
			$last_report = array(
				/*默认按天统计*/
				'report_time' => date('Y-m-d 00:00:00', $temp_info['register_time']),
				'register_count' => 0,
			);
		}
		$begin_time = $last_report['report_time'];
		$end_time = date('Y-m-d H:i:s', strtotime($begin_time . '+1 day'));
		/*缓存日志超过1天就更新*/
		while($end_time < date('Y-m-d H:i:s')){
			//Cache::echart_line($coin,$virtual,NULL);
			/*
			* 根据每天数据进行统计汇总
			* 缓存每天数量
			*/
			$new_report = array(
				'report_time' => $begin_time,
				'register_count' => 0,
			);
			$map['_string'] = ' register_time >= "' . strtotime($begin_time) . '"
				and FROM_UNIXTIME(register_time,"%Y-%m-%d 00:00:00") = "' . $begin_time . '"
				';
			$log = get_result($this->table['data'],$map);
			foreach($log as $row){
				$new_report['register_count']++; 
			}
			$result = update_data($this->table['report'],array(),array(),$new_report);
			if(is_numeric($result)){
				$last_report = $new_report;
				$begin_time = date('Y-m-d H:i:s', strtotime($last_report['report_time'] . '+1 day'));
				$end_time = date('Y-m-d H:i:s', strtotime($begin_time . '+1 day'));
			}
			else{
				exit;
			}
		}
	}
}
