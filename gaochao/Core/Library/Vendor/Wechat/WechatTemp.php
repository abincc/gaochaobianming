<?php
/**
 * 微信模板消息管理类
 * @time 2016-11-21
 * @author 陶君行<Silentlytao@outlook.com>
 */
namespace Wechat;

use Wechat\Lib\Common;
use Wechat\Lib\Tools;

class WechatTemp extends Common
{
    /** 模板消息地址*/
    const TEMPLATE_SET_INDUSTRY_URL = '/message/template/api_set_industry?';
    const TEMPLATE_GET_INDUSTRY_URL = '/template/get_industry?';
    const TEMPLATE_ADD_TPL_URL = '/template/api_add_template?';
    const TEMPLATE_SEND_URL = '/message/template/send?';
    const TEMPLATE_GET_ALL = '/template/get_all_private_template?';
    const TEMPLATE_DEL_URL = '/template/del_private_template?';
    /**
     * 模板消息 设置所属行业
     * @param string $id1 公众号模板消息所属行业编号，参看官方开发文档 行业代码
     * @param string $id2 同$id1。但如果只有一个行业，此参数可省略
     * @return bool|mixed
     */
    public function set_industry($id1, $id2 = '') 
    {
        if (!$this->access_token && !$this->getAccessToken()) {
            return false;
        }
        if ($id1) {
            $data['industry_id1'] = $id1;
        }
        if ($id2) {
            $data['industry_id2'] = $id2;
        }
        $result = Tools::httpPost(self::API_URL_PREFIX . self::TEMPLATE_SET_INDUSTRY_URL . "access_token={$this->access_token}", Tools::json_encode($data));
        if ($result) {
            $json = json_decode($result, true);
            if (!$json || !empty($json['errcode'])) {
                $this->errCode = $json['errcode'];
                $this->errMsg = $json['errmsg'];
                return $this->checkRetry(__FUNCTION__, func_get_args());
            }
            return $json;
        }
        return false;
    }
    /**
     * 模板消息 获取设置的行业信息
     */
    public function get_industry()
    {
        if (!$this->access_token && !$this->getAccessToken()) {
            return false;
        }
        $result = Tools::httpGet(self::API_URL_PREFIX . self::TEMPLATE_GET_INDUSTRY_URL . "access_token={$this->access_token}");
        if ($result) {
            $json = json_decode($result, true);
            if (!$json || !empty($json['errcode'])) {
                $this->errCode = $json['errcode'];
                $this->errMsg = $json['errmsg'];
                return $this->checkRetry(__FUNCTION__, func_get_args());
            }
            return $json;
        }
        return false;
    }
    /**
     * 模板消息 添加消息模板
     * 成功返回消息模板的调用id
     * @param string $tpl_id 模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
     * @return bool|string
     */
    public function add_template_message($tpl_id) {
        if (!$this->access_token && !$this->getAccessToken()) {
            return false;
        }
        $data = array('template_id_short' => $tpl_id);
        $result = Tools::httpPost(self::API_URL_PREFIX . self::TEMPLATE_ADD_TPL_URL . "access_token={$this->access_token}", Tools::json_encode($data));
        if ($result) {
            $json = json_decode($result, true);
            if (!$json || !empty($json['errcode'])) {
                $this->errCode = $json['errcode'];
                $this->errMsg = $json['errmsg'];
                return $this->checkRetry(__FUNCTION__, func_get_args());
            }
            return $json['template_id'];
        }
        return false;
    }
    /**
     * 模板消息 删除消息模板
     * 成功返回 "errcode" : 0,"errmsg" : "ok"
     * @return bool|string
     */
    public function del_template_message($tpl_id) {
        if (!$this->access_token && !$this->getAccessToken()) {
            return false;
        }
        $data = array('template_id' => $tpl_id);
        $result = Tools::httpPost(self::API_URL_PREFIX . self::TEMPLATE_DEL_URL . "access_token={$this->access_token}", Tools::json_encode($data));
        if ($result) {
            $json = json_decode($result, true);
            if (!$json || !empty($json['errcode'])) {
                $this->errCode = $json['errcode'];
                $this->errMsg = $json['errmsg'];
                return $this->checkRetry(__FUNCTION__, func_get_args());
            }
            return true;
        }
        return false;
    }
    /**
     * 模板消息 获取该公众号下所有模板列表
     */
    public function get_all_template()
    {
        if (!$this->access_token && !$this->getAccessToken()) {
            return false;
        }
        $result = Tools::httpGet(self::API_URL_PREFIX . self::TEMPLATE_GET_ALL . "access_token={$this->access_token}");
        if ($result) {
            $json = json_decode($result, true);
            if (!$json || !empty($json['errcode'])) {
                $this->errCode = $json['errcode'];
                $this->errMsg = $json['errmsg'];
                return $this->checkRetry(__FUNCTION__, func_get_args());
            }
            return $json['template_list'];
        }
        return false;
    }
    /**
     * 模板消息 发送
     */
    public function send($data)
    {
        if (!$this->access_token && !$this->getAccessToken()) {
            return false;
        }
        $result = Tools::httpPost(self::API_URL_PREFIX . self::TEMPLATE_SEND_URL . "access_token={$this->access_token}", Tools::json_encode($data));
        if ($result) {
            $json = json_decode($result, true);
            if (!$json || !empty($json['errcode'])) {
                $this->errCode = $json['errcode'];
                $this->errMsg = $json['errmsg'];
                return $this->checkRetry(__FUNCTION__, func_get_args());
            }
            return $json;
        }
        return false;
    }
    
}
?>