<?php
/*
 * 产生面包屑导航
 */
function get_crumbs($crumbs=array(),$delimiter=' &gt; '){
	$str='';
	foreach($crumbs as $val){
		$href_str='';
		if($val['url']!=''){
			$href_str='href="'.$val['url'].'"';
		}
		$str.='<a '.$href_str.'>'.$val['title'].'</a>'.$delimiter;
	}
	$str=rtrim($str,$delimiter);
	return $str;
}
?>