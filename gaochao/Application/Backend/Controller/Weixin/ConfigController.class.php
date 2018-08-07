<?php
/**
 * 微信配置操作父类
 */
namespace Backend\Controller\Weixin;

class ConfigController extends IndexController
{
    /**
     * 读取表操作类型
     */
    protected $table = 'wechat_config';
    
    /**
     * 读取配置
     */
    public function index()
    {
        $this->page($this->table);
        $this->display('Weixin/Config/index');
    }
    /**
     * 添加
     */
    public function add()
    {
        if(IS_POST){
            unset($_POST['id']);
            $this->update();
        }else{
            $this->operate();
        }
    }
    /**
     * 修改
     */
    public function edit()
    {
        if(IS_POST){
            $this->update();
        }else{
            $this->operate();
        }
    }
    /**
     * 更新
     */
    private function update()
    {
        $data = I('post.');
        $rule = $this->rule();
        $data['type'] = 1;
        $res = update_data($this->table,$rule,array(),$data);
        if(is_numeric($res)){
            multi_file_upload($data['qrc_img'], 'Uploads/Weixin/Qrcode', $this->table, 'id',$res,'qrc_img');
            $this->success('操作成功',U('index'));
        }else{
            $this->error($res);
        }
    }
    /**
     * 读取配置
     */
    private function operate()
    {
        $ids = I('ids');
        $info = get_info($this->table,array('id'=>$ids));
        $this->assign('info',$info);
        $this->display('Weixin/Config/operate');
    }
    /**
     * 验证规则
     */
    private function rule()
    {
        $rule = array(
            array('token','require','请填写微信key'),
            array('appid','require','请填写微信appid'),
            array('appsecret','require','请填写微信appsecret'),
            array('encodingaeskey','require','请填写微信encodingaeskey'),
            array('token','1,36','请输入1-36位的token',1,'length'),
            array('appid','1,36','请输入1-36位的appid',1,'length'),
            array('appsecret','1,50','请输入1-50位的appsecret',1,'length'),
            array('encodingaeskey','1,50','请输入1-50位的encodingaeskey',1,'length'),
            array('mch_id','1,50','请输入1-50位的mch_id',0,'length'),
            array('partnerkey','1,50','请输入1-50位的partnerkey',0,'length'),
            array('ssl_cer','1,250','请输入1-250位的ssl_cer',0,'length'),
            array('ssl_key','1,250','请输入1-250位的ssl_key',0,'length'),
        );
        return $rule;
    }
    
}