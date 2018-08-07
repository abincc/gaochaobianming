<?php
require_once "RSAUtils.class.php";
require_once "TDESUtil.class.php";
class XMLUtil{
	public static function arrtoxml($arr,$dom=0,$item=0){
		//ksort($arr);
		if (!$dom){
			$dom = new \DOMDocument("1.0","UTF-8");
		}
		if(!$item){
			$item = $dom->createElement("jdpay");
			$item = $dom->appendChild($item);
		}
		foreach ($arr as $key=>$val){
			$itemx = $dom->createElement(is_string($key)?$key:"item");
			$itemx = $item->appendChild($itemx);
			if (!is_array($val)){
				$text = $dom->createTextNode($val);
				$text = $itemx->appendChild($text);
				 
			}else {
				self::arrtoxml($val,$dom,$itemx);
			}
		}
		return $dom;
	}
	
	public static function xmlToString($dom){
		$xmlStr = $dom->saveXML();
		$xmlStr = str_replace("\r", "", $xmlStr);
		$xmlStr = str_replace("\n", "", $xmlStr);
		$xmlStr = str_replace("\t", "", $xmlStr);
		$xmlStr = preg_replace("/>\s+</", "><", $xmlStr);
		$xmlStr = preg_replace("/\s+\/>/", "/>", $xmlStr);
		$xmlStr = str_replace("=utf-8", "=UTF-8", $xmlStr);
		return $xmlStr;
	}
	
	public static function encryptReqXml($param){
		$dom = self::arrtoxml($param);
		$xmlStr = self::xmlToString($dom);
		$sha256SourceSignString = hash("sha256", $xmlStr);
		$sign = \RSAUtils::encryptByPrivateKey($sha256SourceSignString);
		$rootDom = $dom->getElementsByTagName("jdpay");
		$signDom = $dom->createElement("sign");
		$signDom = $rootDom[0]->appendChild($signDom);
		$signText = $dom->createTextNode($sign);
		$signText = $signDom->appendChild($signText);
		$data = self::xmlToString($dom);
		
		$config = C('payment.jdpay');
		$desKey = $config["desKey"];
		$keys = base64_decode($desKey);
		$encrypt = \TDESUtil::encrypt2HexStr($keys, $data);
		$encrypt = base64_encode($encrypt);
		$reqParam;
		$reqParam["version"]=$param["version"];
		$reqParam["merchant"]=$param["merchant"];
		$reqParam["encrypt"]=$encrypt;
		$reqDom = self::arrtoxml($reqParam,0,0);
		$reqXmlStr = self::xmlToString($reqDom);
		//echo htmlspecialchars($reqXmlStr)."<br/>";
		return $reqXmlStr;
	}
	
	public static function decryptResXml($resultData,&$resData){
		$resultXml = simplexml_load_string($resultData);
		$resultObj = json_decode(json_encode($resultXml),TRUE);
		$encryptStr = $resultObj["encrypt"];
		$encryptStr=base64_decode($encryptStr);
		$config = C('payment.jdpay');
		$desKey = $config["desKey"];
		$keys = base64_decode($desKey);
		$reqBody = \TDESUtil::decrypt4HexStr($keys, $encryptStr);
		// echo "请求返回encrypt Des解密后:".$reqBody."\n";
		
		$bodyXml = simplexml_load_string($reqBody);
		$resData = json_decode(json_encode($bodyXml),TRUE);
		$inputSign = $resData["sign"];

		$startIndex = strpos($reqBody,"<sign>");
		$endIndex = strpos($reqBody,"</sign>");
		$xml;
		
		if($startIndex!=false && $endIndex!=false){
			$xmls = substr($reqBody, 0,$startIndex);
			$xmle = substr($reqBody,$endIndex+7,strlen($reqBody));
			$xml=$xmls.$xmle;
		}
		
		//echo "本地摘要原串:".$xml."\n";
		$sha256SourceSignString = hash("sha256", $xml);
		// echo "本地摘要:".$sha256SourceSignString."\n";
		
		$decryptStr = \RSAUtils::decryptByPublicKey($inputSign);
		// echo "解密后摘要:".$decryptStr."<br/>";
		$flag;
		if($decryptStr==$sha256SourceSignString){
			//echo "验签成功<br/>";
			$flag=true;
		}else{
			//echo "验签失败<br/>";
			$flag=false;
		}
		$resData["version"]=$resultObj["version"];
		$resData["merchant"]=$resultObj["merchant"];
		$resData["result"]=$resultObj["result"];
		// echo var_dump($resData);
		return $resData;
	}
}
?>