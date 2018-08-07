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
	<link rel="stylesheet" type="text/css" href="/gaochao/Public/Home/css/mui.min.css?1510045380" />
	<link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/common.css?1510045440" />
	<!--[if lte IE 6]>
	    <link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/ie6.css?1510045440" />
	<![endif]-->

	<link rel="stylesheet" type="text/css" href="/gaochao/Public/Home/css/common.css?1510045380" />
	
	<!-- /* 全局JS调用、加载 */ -->
	<script type="text/javascript" src="/gaochao/Public/Static/js/common.js?1510045440"></script>
	<script type="text/javascript" src="/gaochao/Public/Plugins/bootstrap/bootstrap.min.js?1510045380"></script>
	<script type="text/javascript" src="/gaochao/Public/Home/js/common.js?1510045380"></script>
	<!-- /* 全局JS调用、加载 */ -->

</head>
<body>
<div class="wrapper">

	<div class="body">
		
<style type="text/css">
    .mui-preview-image.mui-fullscreen {
        position: fixed;
        z-index: 20;
        background-color: #000;
    }
    .mui-preview-header,
    .mui-preview-footer {
        position: absolute;
        width: 100%;
        left: 0;
        z-index: 10;
    }
    .mui-preview-header {
        height: 44px;
        top: 0;
    }
    .mui-preview-footer {
        height: 50px;
        bottom: 0px;
    }
    .mui-preview-header .mui-preview-indicator {
        display: block;
        line-height: 25px;
        color: #fff;
        text-align: center;
        margin: 15px auto 4;
        width: 70px;
        background-color: rgba(0, 0, 0, 0.4);
        border-radius: 12px;
        font-size: 16px;
    }
    .mui-preview-image {
        display: none;
        -webkit-animation-duration: 0.5s;
        animation-duration: 0.5s;
        -webkit-animation-fill-mode: both;
        animation-fill-mode: both;
    }
    .mui-preview-image.mui-preview-in {
        -webkit-animation-name: fadeIn;
        animation-name: fadeIn;
    }
    .mui-preview-image.mui-preview-out {
        background: none;
        -webkit-animation-name: fadeOut;
        animation-name: fadeOut;
    }
    .mui-preview-image.mui-preview-out .mui-preview-header,
    .mui-preview-image.mui-preview-out .mui-preview-footer {
        display: none;
    }
    .mui-zoom-scroller {
        position: absolute;
        display: -webkit-box;
        display: -webkit-flex;
        display: flex;
        -webkit-box-align: center;
        -webkit-align-items: center;
        align-items: center;
        -webkit-box-pack: center;
        -webkit-justify-content: center;
        justify-content: center;
        left: 0;
        right: 0;
        bottom: 0;
        top: 0;
        width: 100%;
        height: 100%;
        margin: 0;
        -webkit-backface-visibility: hidden;
    }
    .mui-zoom {
        -webkit-transform-style: preserve-3d;
        transform-style: preserve-3d;
    }
    .mui-slider .mui-slider-group .mui-slider-item img {
        width: auto;
        height: auto;
        max-width: 100%;
        max-height: 100%;
    }
    .mui-android-4-1 .mui-slider .mui-slider-group .mui-slider-item img {
        width: 100%;
    }
    .mui-android-4-1 .mui-slider.mui-preview-image .mui-slider-group .mui-slider-item {
        display: inline-table;
    }
    .mui-android-4-1 .mui-slider.mui-preview-image .mui-zoom-scroller img {
        display: table-cell;
        vertical-align: middle;
    }
    .mui-preview-loading {
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;
        display: none;
    }
    .mui-preview-loading.mui-active {
        display: block;
    }
    .mui-preview-loading .mui-spinner-white {
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -25px;
        margin-top: -25px;
        height: 50px;
        width: 50px;
    }
    .mui-preview-image img.mui-transitioning {
        -webkit-transition: -webkit-transform 0.5s ease, opacity 0.5s ease;
        transition: transform 0.5s ease, opacity 0.5s ease;
    }
    @-webkit-keyframes fadeIn {
        0% {
            opacity: 0;
        }
        100% {
            opacity: 1;
        }
    }
    @keyframes fadeIn {
        0% {
            opacity: 0;
        }
        100% {
            opacity: 1;
        }
    }
    @-webkit-keyframes fadeOut {
        0% {
            opacity: 1;
        }
        100% {
            opacity: 0;
        }
    }
    @keyframes fadeOut {
        0% {
            opacity: 1;
        }
        100% {
            opacity: 0;
        }
    }
    p img {
        max-width: 100%;
        height: auto;
    }
</style>

<div class="tiezi_div bg_fff">
    <div class="clearfix user_img">
        <div class="time">2017-02-12</div>
        <div class="img"><img src="/gaochao/Public/Home/img/anonymous.png" alt=""></div>
        <div class="text">
            <div class="name">陌上出黛</div>
            <div class="bk">旅游板块</div>
        </div>
    </div>
</div>

<div class="tz_detail bg_fff">
    <div class="tt">喀纳斯湖游览攻略</div>
    <div class="content">
        湖泊最深处高程为1181.5米，湖深188.5米，是中国最深的冰碛堰塞湖；湖泊两侧崖岩林立，断层痕迹清晰；湖泊平面上北侧呈北东向，南部形态呈北东南西相互交织的锯齿状，与区域北东南西向两组构造线一致，是第四纪冰川和构造断陷共同作用的结果。喀纳斯湖是中国唯一的北冰洋水系，湖水来自奎屯、友谊峰等山的冰川融水和当地降水，从地表或地下泻入喀纳斯湖。
喀纳斯湖年降水量为1000毫米左右，最大降水带为海拔2100米。冬季漫长，降雪丰富，一般8月初开始出现霜冻。海拔1400米以上，8月下旬开始降雪；海拔3000米以上，8月初开始降雪，一直到翌年5月下旬或6月初，降雪期长达8个月左右，积雪深度可达1—2米，降雪日数一般在73天以上，稳定积雪期在200天左右。雪线分布在海拔2850米左右。年蒸发量约1000毫米，与降水量大体持平。

        <img src="/gaochao/Public/Home/img/pro1.jpg" alt="">
    </div>
</div>

<div class="comment_tt clearfix bg_fff">
    <div class="pull-right tj"><span>阅读 454</span> | <span>点赞 54</span></div>
    <span>评论 263</span>
</div>
<div class="pro_tab pro_tab_show comment_div comment_div_tz">
    <div class="box clearfix bg_fff">
        <div class="clearfix u_img">
            <div class="img"><img src="/gaochao/Public/Home/img/anonymous.png" alt=""></div>
        </div>
        <div class="right_div">
            <div class="infos_div clearfix">
                <div class="n clearfix"><span>1楼</span>陌上出黛</div>
                <div class="t">2017-02-12</div>
            </div>
            <div class="content">
                驼颈湾位于喀纳斯湖南面一公里位置，是喀纳斯湖的入水口，位于喀纳斯河发源地。喀纳斯河在这里形成了一个恰似驼颈的大拐弯。
            </div>
            <!-- 一行三张图片   imgs3   两张图片   imgs2    一张图片   imgs1 -->
            <div class="imgs clearfix imgs1">
                <div class="img_box">
                    <!-- data-preview-group="1" 第几组评论图片 -->
                    <img src="/gaochao/Public/Home/img/pro1.jpg" alt="" data-preview-src="" data-preview-group="1">
                </div>
            </div>
        </div>
    </div>



    <div class="box clearfix bg_fff">
        <div class="clearfix u_img">
            <div class="img"><img src="/gaochao/Public/Home/img/anonymous.png" alt=""></div>
        </div>
        <div class="right_div">
            <div class="infos_div clearfix">
                <div class="n clearfix"><span>2楼</span>陌上出黛</div>
                <div class="t">2017-02-12</div>
            </div>
            <div class="content">
                驼颈湾位于喀纳斯湖南面一公里位置，是喀纳斯湖的入水口，位于喀纳斯河发源地。喀纳斯河在这里形成了一个恰似驼颈的大拐弯。
            </div>
            <!-- 一行三张图片   imgs3   两张图片   imgs2    一张图片   imgs1 -->
            <div class="imgs clearfix imgs2">
                <div class="img_box">
                    <!-- data-preview-group="1" 第几组评论图片 -->
                    <img src="/gaochao/Public/Home/img/pro1.jpg" alt=""  data-preview-src="" data-preview-group="2">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt=""  data-preview-src="" data-preview-group="2">
                </div>
            </div>
        </div>
    </div>


    <div class="box clearfix bg_fff">
        <div class="clearfix u_img">
            <div class="img"><img src="/gaochao/Public/Home/img/anonymous.png" alt=""></div>
        </div>
        <div class="right_div">
            <div class="infos_div clearfix">
                <div class="n clearfix"><span>3楼</span>陌上出黛</div>
                <div class="t">2017-02-12</div>
            </div>
            <div class="content">
                驼颈湾位于喀纳斯湖南面一公里位置，是喀纳斯湖的入水口，位于喀纳斯河发源地。喀纳斯河在这里形成了一个恰似驼颈的大拐弯。
            </div>
            <!-- 一行三张图片   imgs3   两张图片   imgs2    一张图片   imgs1 -->
            <div class="imgs clearfix imgs3">
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/pro1.jpg" alt="" data-preview-src="" data-preview-group="3">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="3">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="3">
                </div>
            </div>
        </div>
    </div>


    <div class="box clearfix bg_fff">
        <div class="clearfix u_img">
            <div class="img"><img src="/gaochao/Public/Home/img/anonymous.png" alt=""></div>
        </div>
        <div class="right_div">
            <div class="infos_div clearfix">
                <div class="n clearfix"><span>4楼</span>陌上出黛</div>
                <div class="t">2017-02-12</div>
            </div>
            <div class="content">
                驼颈湾位于喀纳斯湖南面一公里位置，是喀纳斯湖的入水口，位于喀纳斯河发源地。喀纳斯河在这里形成了一个恰似驼颈的大拐弯。
            </div>
            <!-- 一行三张图片   imgs3   两张图片   imgs2    一张图片   imgs1 -->
            <div class="imgs clearfix imgs3">
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/pro1.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/pro1.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/pro1.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
                <div class="img_box">
                    <img src="/gaochao/Public/Home/img/44444.jpg" alt="" data-preview-src="" data-preview-group="4">
                </div>
            </div>
        </div>
    </div>
</div>

	</div>

</div>

<!-- 悬浮窗 S -->
<!-- <div class="susbox js_susbox">
	<div><a href="javascript:;" style="background-image: url(/gaochao/Public/Home/img/backtop.png);" title="返回顶部"></a></div>
</div> -->
<!-- 悬浮窗 E -->

<!--[if lte IE 6]>
    <link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/ie6.css?1510045440" />
    <script type="text/javascript" src="/gaochao/Public/Static/js/ie6.js?1510045440"></script>
    <script type="text/javascript" src="/gaochao/Public/Home/js/iepng.js"></script>
    <script type="text/javascript">
	    EvPNG.fix('div,ul,img,li,input,a,i');
	    //EvPNG.fix('包含透明PNG图片的标签'); 多个标签之间用英文逗号隔开。
	</script>
<![endif]-->
<!--[if lt IE 9]>
    <script type="text/javascript" src="/gaochao/Public/Static/js/ie9.js?1510045440"></script>
<![endif]-->

    <script type="text/javascript" src="/gaochao/Public/Home/js/mui.min.js?1510045380"></script>
    <script type="text/javascript" src="/gaochao/Public/Home/js/mui.zoom.js?1510045380"></script>
    <script type="text/javascript" src="/gaochao/Public/Home/js/mui.previewimage.js?1510045380"></script>
    <script>
        mui.init();
        mui.previewImage();
    </script>

</body>
</html>