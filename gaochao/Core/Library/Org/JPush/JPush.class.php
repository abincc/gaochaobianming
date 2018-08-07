<?php
/**
 * 极光推送
 */
namespace Org\JPush;

use Org\JPush\Exceptions\JPushException;

class JPush{
	public static $app_key = 'de6955b12b65ccabdd7c0e67';
	public static $master_secret = 'b5da3074046a722979ed1ccf';
	public static $log = './Runtime/jpush.log';
	
	/**
	 * 推送
	 * @param unknown $alias			推送用户，别名推送，字符串或数组（值为字符串），或 all 推送所有
	 * @param unknown $title			标题
	 * @param unknown $content			内容
	 * @param array $extras				扩展字段
	 * @param array $extras_ios			iOS 扩展字段，为空则使用扩展值
	 * @param array $extras_android		Android 扩展字段，为空则使用扩展值
	 */
	public static function push($alias, $title, $content, $extras = [], $extras_ios = [], $extras_android = []) {
		$app_key = C('JPUSH_APP_KEY') ? C('JPUSH_APP_KEY') : static::$app_key;
		$master_secret = C('JPUSH_MASTER_SECRET') ? C('JPUSH_MASTER_SECRET') : static::$master_secret;
		
		$client = new Client($app_key, $master_secret, static::$log);
		$push = $client->push();
		/* 推送到所有平台 */
		$push->setPlatform('all');
		/* 使用别名推送 */
		if($alias === 'all') {
			$push->addAllAudience();
		}else {
			$push->addAlias($alias);
		}
		/* iOS 个性化设置 */
		$push->iosNotification($content, [
			'sound'=> 'sound.caf',								//提示音
			'badge'=> '+1',										//角标数字
			'content-available'=> true,							//推送唤醒
			'extras'=> $extras_ios ? $extras_ios : $extras,		//扩展字段
		]);
		/* Android 个性化设置 */
		$push->androidNotification($content, [
			'title'=> $title,											//标题
			'extras'=> $extras_android ? $extras_android : $extras,		//扩展字段
		]);
		/*  */
		$push->message($content, [
			'title'=> $title,				//标题
			'content_type'=> 'text',		//消息类型
			'extras'=> $extras,				//扩展字段
		]);
		$push->options([
			'time_to_live'=> 86400,			//离线消息保留时长
			'apns_production'=> true,		//推送环境，true：生成环境，false：开发环境
		]);
		try {
			$push->send();
			return true;
		}catch(JPushException $e) {
			return false;
		}
	}
}