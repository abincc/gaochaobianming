<?php
namespace Backend\Controller\Debug;
/**
 * 调试类
 */
use Backend\Controller\Base\AdminController;

class IndexController extends AdminController
{
    protected $table = 'debug';
    /**
     * 列表
     */
    public function index()
    {
        $this->page($this->table,array(),'id desc',array(),30);
        $this->display();
    }
    /**
     * 详情
     */
    public function details()
    {
        $ids = I('ids');
        $map = array(
          'id'=>$ids  
        );
        $info = get_info($this->table,$map);
        $info['content'] = json_decode($info['content'],true);
        $this->assign('info',$info);
        $this->display('operate');
    }
}

?>