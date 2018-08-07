<?php
namespace Home\Controller\Article;
	/**
	 * 帮助中心
	 */
class HelpController extends IndexController {
	/**
	 * 列表
	 */
	public function index(){
		$category = get_result('help_category','','sort desc');
		$category = array_format($category);
		$help = get_result('help','','sort desc');
		$help = array_format($help);
		/*内容绑定到分类*/
		foreach($help as $row){
			$category[$row['category_id']]['_child'][] = $row;
		}
		$current_id = I('id');
		/*移除没有数据的分类，默认指向第一条*/
		foreach($category as $key => &$row){
			if(!isset($row['_child'])){
				unset($category[$key]);
				continue;
			}
			if(!$current_id){
				$first = array_slice($row['_child'],0,1);
				$current_id = $first[0]['id'];
			}
			$row['class'] .= 'has_child';
			foreach($row['_child'] as &$row_2){
				if($current_id == $row_2['id']){
					$row['active'] = 'active';
					$row['class'] .= ' cur';
					$row_2['active'] = 'active';
					$row_2['class'] .= ' cur';
				}
			}
		};
		$data["category"] = $category;
		$data["content"] = $help[$current_id];
		$this->assign($data);
		$this->display();
	}
}
