<?php
	/**   
	*转载自：http://www.jb51.net/article/56967.htm 
	* @desc 根据两点间的经纬度计算距离  
	* @param float $lat 纬度值  
	* @param float $lng 经度值  
	*/   
	function getDistance($lat1, $lng1, $lat2, $lng2){   
		$earthRadius = 6367000; //approximate radius of earth in meters   
		$lat1 = ($lat1 * pi() ) / 180;   
		$lng1 = ($lng1 * pi() ) / 180;   
		$lat2 = ($lat2 * pi() ) / 180;   
		$lng2 = ($lng2 * pi() ) / 180;   
		$calcLongitude = $lng2 - $lng1;   
		$calcLatitude = $lat2 - $lat1;   
		$stepOne = pow(sin($calcLatitude / 2), 2) + cos($lat1) * cos($lat2) * pow(sin($calcLongitude / 2), 2);   
		$stepTwo = 2 * asin(min(1, sqrt($stepOne)));   
		$calculatedDistance = $earthRadius * $stepTwo;   
		return round($calculatedDistance);   
	}   

	/** 
	 * 对查询结果集进行排序 
	 * @access public 
	 * @param array $list 查询结果 
	 * @param string $field 排序的字段名 
	 * @param array $sortby 排序类型 
	 * asc正向排序 desc逆向排序 nat自然排序 
	 * @return array 
	 */  
	function list_sort_by($list) {  
	    foreach ($list as $k => $v) {
	    	$num[$k] = $v['limit'];
	    }
	    array_multisort($num, SORT_DESC, $list);
	    return $list;
	} 


	
