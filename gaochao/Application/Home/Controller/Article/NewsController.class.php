<?php
namespace Home\Controller\Article;
	/**
	 * 新闻模块
	 */
class NewsController extends IndexController {
	protected $table = 'news';
	protected $table_category = 'news_category';
	/**
	 * 列表
	 */
	public function index(){
		$result = $this->page($this->table, array(),'sort desc,add_time desc');
		$this->display();
	}
	/**
	 * 详情
	 */
	public function detail(){
		$info = get_info($this->table, array('id' => I('id')));
		if(!$info['id']){
			return $this->redirect('index');
		}
		M($this->table)->where('id=' . $info['id'])->setInc('views',1);
		$info['up'] = get_info($this->table, array('id'=>array('lt',I('id'))));
		$info['down'] = get_info($this->table, array('id'=>array('gt',I('id'))));
		$data['info'] = $info;
		$recommend = get_result($this->table, array('recommend'=>1),'sort desc');
		$data['recommend'] = $recommend;
		$this->assign($data);
		$this->display();
	}
}