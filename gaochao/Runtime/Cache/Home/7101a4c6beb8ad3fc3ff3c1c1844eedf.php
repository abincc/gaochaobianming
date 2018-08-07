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
	
<style type="text/css">
.choose_city{padding:10px 0;border:3px solid #f60;margin:20px 0;}
.margin_b30{padding:0 20px;}
.title_type1,.title_type2{width:100px;float:left;font-size:14px;line-height:34px;}
.xzcs_dt,.letter_list{width:850px;float:right;line-height:30px;}
.xzcs_dt a,.letter_list a{display:block;padding:0 5px;float:left;margin:2px 0;margin-right:5px;}
.xzcs_dt a:hover,.xzcs_dt a.hover,.letter_list a.hover{background:#f60;color:#fff;}
.letter_search{background:#eee;padding:5px 20px;border-top:1px solid #ccc;border-bottom:1px solid #ccc;}
.letter_list a{display:block;padding:0 5px;float:left;margin:2px;}
.letter_list a:hover,.letter_list a.hover{background:none;color:#f60;}
@media (min-width:1200px) {
    .xzcs_dt,.letter_list,.letter_search .letter_list{width:1030px;}
}
</style>
<script type="text/javascript">
leftMenuSlide=true;
</script>

	<!-- /* 全局JS调用、加载 */ -->
	<script type="text/javascript" src="/gaochao/Public/Static/js/common.js?1510045440"></script>
	<script type="text/javascript" src="/gaochao/Public/Plugins/bootstrap/bootstrap.min.js?1510045380"></script>
	<script type="text/javascript" src="/gaochao/Public/Home/js/common.js?1510045380"></script>
	<!-- /* 全局JS调用、加载 */ -->

</head>
<body>
<div class="wrapper">

	<div class="body">
		
<div class="container">
	<div class="choose_city">
        <div class="margin_t10 clearfix" style="padding:5px 20px;">
            <div class="title_type1" style="margin-right: 25px;">快速查找</div>
            <form action="<?=U('subsite_form')?>" method="post" class="form-inline" >
                <select name="province" class="form-control input-sm js-province" onchange="province_change(this)">
                    <option value="">请选择省份</option>
                    <?php  foreach($province_array as $k=>$v){ ?>
                    <option value="<?=$k?>"><?=$v['first_char']?> <?=$v['title']?></option>
                    <?php  } ?>
                </select>
                <select name="subsite_id" class="form-control input-sm js-city" id="subsite_city">
                    <option value="">请选择城市</option>
                </select>
                <input type="submit" class="btn btn-orange btn-sm js-subsite" value="进入">
            </form>
        </div>
        <!--热门城市-->
        <div class="clearfix" style="padding:0 20px;">
            <div class="title_type1">热门城市</div>
            <div class="xzcs_dt">
                <?php  foreach($host_city as $v){ ?>
                <a href="#" title="<?=$v['title']?>" target="_blank" first-char="<?=getfirstchar($v['title'])?>"><?=$v['title']?></a>
                <?php  } ?>
                <span style="cursor:default;color:#444;">共开通了<strong class="hover"><?=$city_nums?></strong>个城市站</span>
            </div>
        </div>
        <!--快速查找-->
        <!--拼音查找城市-->
        <div class="letter_search margin_b10 margin_t10 clearfix">
            <div class="title_type1">拼音首字母筛选</div>
            <div class="letter_list">
                <a href="javascript:;" class="">A</a>
                <a href="javascript:;" class="">B</a>
                <a href="javascript:;" class="">C</a>
                <a href="javascript:;" class="">D</a>
                <a href="javascript:;" class="">E</a>
                <a href="javascript:;" class="">F</a>
                <a href="javascript:;" class="">G</a>
                <a href="javascript:;" class="">H</a>
                <a href="javascript:;" class="">J</a>
                <a href="javascript:;" class="">K</a>
                <a href="javascript:;" class="">L</a>
                <a href="javascript:;" class="">M</a>
                <a href="javascript:;" class="">N</a>
                <a href="javascript:;" class="">O</a>
                <a href="javascript:;" class="">P</a>
                <a href="javascript:;" class="">Q</a>
                <a href="javascript:;" class="">R</a>
                <a href="javascript:;" class="">S</a>
                <a href="javascript:;" class="">T</a>
                <a href="javascript:;" class="">W</a>
                <a href="javascript:;" class="">X</a>
                <a href="javascript:;" class="">Y</a>
                <a href="javascript:;" class="">Z</a>
            </div>
        </div>
        <?php foreach($area_city as $k=>$item){ ?>
        <?php if($item){ ?>
        <div class="margin_b30 clearfix">
            <div class="title_type2"><?=$k?></div>
            <div class="xzcs_dt">
                <?php foreach($item as $city){ ?>
                <a href="http://<?=$city['url']?>/baiji/" title="<?=$city['title']?>" first-char="<?=getfirstchar($city['title'])?>" target="_blank"><?=$city['title']?></a>
                <?php } ?>
            </div>
        </div>
        <?php } ?>
        <?php } ?>
        <!--华东-->
        <div class="margin_b30 clearfix">
            <div class="title_type2">华东地区</div>
            <div class="xzcs_dt">
                <a href="#" first-char="S">上海</a>
                <a href="#" first-char="N">南京</a>
                <a href="#" first-char="T">泰安</a>
                <a href="#" first-char="F">抚州</a>
                <a href="#" first-char="J">吉安</a>
                <a href="#" first-char="J">景德镇</a>
            </div>
        </div>

        <div class="margin_b30 clearfix">
            <div class="title_type2">华南地区</div>
            <div class="xzcs_dt">
                <a href="#" first-char="G">广州</a>
                <a href="#" first-char="S">深圳</a>
                <a href="#" first-char="D">东莞</a>
                <a href="#" first-char="Y">玉林</a>
                <a href="#" first-char="H">河池</a>
                <a href="#" first-char="Q">琼海</a>
                <a href="#" first-char="N">南平</a>
            </div>
        </div>

        <div class="margin_b30 clearfix">
            <div class="title_type2">中西部地区</div>
            <div class="xzcs_dt">
                <a href="#" first-char="C">重庆</a>
                <a href="#" first-char="X">西安</a>
                <a href="#" first-char="W">武汉</a>
                <a href="#" first-char="L">漯河</a>
                <a href="#" first-char="D">大理</a>
                <a href="#" first-char="C">楚雄</a>
                <a href="#" first-char="H">呼伦贝尔</a>
                <a href="#" first-char="X">西双版纳</a>
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

<script>
    function province_change(obj){
        var province_id=$(obj).val();
        $.ajax({
            type:"GET",
            url:"<?=U('ajax_get_city')?>",
            data:{province_id:province_id},
            datatype:"json",
            success:function(data){
                if(data.status){
                    $('#subsite_city').html(data.info);
                }else{
                    $('#subsite_city').html('<option value="">暂无分站城市</option>')
                }
            }
        });
    }

    //提交检查方法
    $(function(){
        $('.js-subsite').click(function(){
            var province = $('.js-province option:selected').val();
            var city = $('.js-city option:selected').val();
            if(province && city){
                return true;
            }else{
                return false;
            }
        });
    });

    //提交检查方法
    // function checkInput(obj){
    //     //console.log($(obj));
    //     var a = $(obj).find('.js-province option:selected').val();
    //     var b = $(obj).find('.js-city option:selected').val();
    //     if(a && b){

    //         $(obj).submit();
    //     }else{
    //         return false;
    //     }
    // }

    //js实现点击高亮
    $(function(){
        $('.letter_list').on('click','a',function(){
            //获取点击拼音的首字母
            var first = $(this).html();
            //alert(first);
            $('.xzcs_dt a').each(function(){
                if(first == $(this).attr('first-char')){
                   $(this).css({'color':'orange'});
                }else{
                   $(this).css({'color':''});
                }
            });
        });
    });

</script>

</body>
</html>