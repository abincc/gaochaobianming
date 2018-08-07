<?php if (!defined('THINK_PATH')) exit(); $member_info = session('member_info'); ?>
<!DOCTYPE html>
<html>
<head>
	<meta name="renderer" content="webkit">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
	
	<title><?=$seo['title']?></title>
	<meta name="keywords" content="<?=$seo['keywords']?>" /> 
	<meta name="description" content="<?=$seo['description']?>" />
	<script>
	var _ROOT_='/gaochao';
	var _STATIC_='/gaochao/Public/Static';
	var _PLUGIN_='/gaochao/Public/Plugins';
	var _JS_='/gaochao/Public/Home/js';
	var _IMG_='/gaochao/Public/Home/img';
	var _CSS_='/gaochao/Public/Home/css';
	</script>
	<link rel="stylesheet" type="text/css" href="/gaochao/Public/Home/css/mui.min.css?1518508940" />
	<link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/common.css?1518508939" />
	<!--[if lte IE 6]>
	    <link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/ie6.css?1518508939" />
	<![endif]-->

	<link rel="stylesheet" type="text/css" href="/gaochao/Public/Home/css/common.css?1518508940" />
	
	<!-- /* 全局JS调用、加载 */ -->
	<script type="text/javascript" src="/gaochao/Public/Static/js/common.js?1518508939"></script>
	<script type="text/javascript" src="/gaochao/Public/Plugins/bootstrap/bootstrap.min.js?1518508996"></script>
	<script type="text/javascript" src="/gaochao/Public/Home/js/common.js?1518508963"></script>
	<!-- /* 全局JS调用、加载 */ -->

</head>
<body>
<div class="wrapper">

	<div class="body">
		
<style>
.text_detail .content p{ font-size: 14px; color: #333; line-height: 20px;}
</style>
<div class="text_detail bg_fff">
    <div class="content"><?=$content?></div>
</div>

	</div>

</div>

<!-- 悬浮窗 S -->
<!-- <div class="susbox js_susbox">
	<div><a href="javascript:;" style="background-image: url(/gaochao/Public/Home/img/backtop.png);" title="返回顶部"></a></div>
</div> -->
<!-- 悬浮窗 E -->

<!--[if lte IE 6]>
    <link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/ie6.css?1518508939" />
    <script type="text/javascript" src="/gaochao/Public/Static/js/ie6.js?1518508939"></script>
    <script type="text/javascript" src="/gaochao/Public/Home/js/iepng.js"></script>
    <script type="text/javascript">
	    EvPNG.fix('div,ul,img,li,input,a,i');
	    //EvPNG.fix('包含透明PNG图片的标签'); 多个标签之间用英文逗号隔开。
	</script>
<![endif]-->
<!--[if lt IE 9]>
    <script type="text/javascript" src="/gaochao/Public/Static/js/ie9.js?1518508939"></script>
<![endif]-->

    <script>
        $(function(){
            var val=$(window).width()/7.5;
            $('html').css('font-size',val);
            $(window).resize(function(event) {
               var val=$(window).width()/7.5;
                $('html').css('font-size',val);
            });
                   
        })
    </script>

</body>
</html>