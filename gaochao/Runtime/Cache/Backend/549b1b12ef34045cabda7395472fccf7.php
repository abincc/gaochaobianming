<?php if (!defined('THINK_PATH')) exit();?><!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name='viewport' content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>退出成功</title>
<style>
	body{
		font-family: "Microsoft YaHei",verdana,arial;
	}
	.align_c{
		text-align: center;
	}
	.f18{
		font-size: 18px;
	}
</style>
</head>
<body style="background-color: #e9e4d8;padding-top:50px;">
	<div class="statusbox clearfix" style="text-align: center;">
		<img src="/gaochao/Public/Home/img/status/success.png" />
		<div class="align_c f18" style="padding:20px 0;color:#38b89f;"><?=$message?></div>
	
		<div class="goback clearfix">
			<span style="color:#999;">页面自动<a id="href" style="color:#38b89f;" href="<?=$jumpUrl?>">跳转</a>&nbsp;&nbsp;等待时间：</span>
			<span id="time" style="color:#38b89f;">3</span>
		</div>
	</div>
</body>
<script>
	function countdown(count){
		var clear = setTimeout(function(){
			document.getElementById('time').innerHTML=count;
			count--;
			if(count >= 0){
				countdown(count);
			}else{
				window.clearTimeout(clear);
				window.location.href = document.getElementById('href').href;
			}
		},1000);
	}
	countdown(3);
</script>
</html>