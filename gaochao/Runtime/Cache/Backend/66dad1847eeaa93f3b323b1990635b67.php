<?php if (!defined('THINK_PATH')) exit(); $cur_path=MODULE_NAME.'/'.CONTROLLER_NAME.'/'.ACTION_NAME; $menu_info['path'] = ''; $menu_list = get_menu_list (); foreach ( $menu_list as $key => $value ) { if ($cur_path == $value ['url']) { $menu_info = $value; } } $menu_path = explode("-",$menu_info['path']); $menu_1 = array(); $menu_2 = array(); $menu_3 = array(); foreach($menu_result as $row_1){ if(!$row_1["child"]) continue; $first = array_slice($row_1["child"],0,1); $second = array_slice($first[0]["child"],0,1); $row_1['data']['url'] = $second[0]['data']['url']; if($row_1['data']['id']==$menu_path[2]){ $row_1['data']['class'] = "cur"; foreach($row_1['child'] as $row_2){ $third = array_slice($row_2["child"],0,1); $row_2['data']['url'] = $third[0]['data']['url']; if($row_2['data']['id']==$menu_path[3]){ $row_2['data']['class'] = "active"; foreach($row_2['child'] as $row_3){ $row_3['data']['class'] = ''; $fourth = array_slice($row_3,0,1); if($fourth['data']['id']==$menu_path[4]){ $row_3['data']['class'] = 'active'; } $menu_3[] = $row_3['data']; } } if($row_2['data']['url']){ $menu_2[] = $row_2['data']; } } } if($row_1['data']['url']){ $menu_1[] = $row_1['data']; } } ?>
<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

<?php
 if(!empty($seo['title'])){ $seo['title'] = $seo['title'].' | '.C('SITE_TITLE'); }else{ $seo['title'] = C('SITE_TITLE'); } if(empty($seo['keywords'])){ $seo['keywords']=C('SITE_KEYWORD'); } if(empty($seo['description'])){ $seo['description']=C('SITE_DESCRIPTION'); } $member_info=session("member_info"); ?>
<title><?=$seo['title']?></title>
<meta name="keywords" content="<?=$seo['keywords']?>" />
<meta name="description" content="<?=$seo['description']?>" />
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script>
var _ROOT_='/gaochao';
var _STATIC_='/gaochao/Public/Static';
var _PLUGIN_='/gaochao/Public/Plugins';
var _JS_='/gaochao/Public/Backend/js';
var _IMG_='/gaochao/Public/Backend/img';
var _CSS_='/gaochao/Public/Backend/css';
</script>
<link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/common.css?1518508939" />
<link rel="stylesheet" type="text/css" href="/gaochao/Public/Plugins/select2/select2.css?1518509003" />
<link rel="stylesheet" type="text/css" href="/gaochao/Public/Backend/css/admin.min.css?1518508931" />
<link rel="stylesheet" type="text/css" href="/gaochao/Public/Backend/css/backend.css?1518508931" />

<script type="text/javascript" src="/gaochao/Public/Static/js/common.js?1518508939"></script>
<script type="text/javascript" src="/gaochao/Public/Backend/js/common.js?1518508938"></script>
<!--[if lt IE 9]>
  <script type="text/javascript" src="/gaochao/Public/Static/js/ie9.js?1518508939"></script>
<![endif]-->
<!--[if lte IE 6]>
<link rel="stylesheet" type="text/css" href="/gaochao/Public/Static/css/ie6.css?1518508939" />
<script type="text/javascript" src="/gaochao/Public/Static/js/ie6.js?1518508939"></script>
<![endif]-->


</head>
<body class="skin-gray">
	<header class="main-header">
	<a href="<?=U('/Backend')?>" class="logo">
		<span class="logo-mini"><b>尚软</b></span>
		<span class="logo-lg"> <i>CNSunRun</i>
	</span>
	</a>

	<nav class="navbar navbar-static-top">
		<a href="#" class="sidebar-toggle topbar-toggle js_collapse" data-toggle="collapse" data-target=".nav_box" role="button"><img src="/gaochao/Public/Backend/img/main_menu.png" alt=""></a>
		<div class="nav_box">
			<?php
 foreach($menu_1 as $row){ ?>
			<a href="<?=U($row['url'])?>" class="<?=$row['class']?>">
				<?=$row['title']?>
			</a>
			<?php
 } ?>
		</div>
		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
<!-- 				<li>
					<a href="javascript:void(0)" class="message js_message_btn">
						<img src="/gaochao/Public/Backend/img/alarm.png" alt=""><div class="message_num js_message_num">10</div>
					</a>
					<div class="message_box treeview-menu js_message_box">
						<div class="header text-center">你有 <span class="js_message_num">10</span> 个待查看消息</div>
						<ul class="message_list">
							<li>
								<a href="#">
									<h6>新检测任务通知<small>5 分钟前</small></h6>
								</a>
							</li>
							<li>
								<a href="#">
									<h6>检测异常通知<small>5 分钟前</small></h6>
								</a>
							</li>
							<li>
								<a href="#">
									<h6>新采样任务通知<small>5 分钟前</small></h6>
								</a>
							</li>
			            </ul>
			            <div class="footer"><a href="#">查看所有消息</a></div>
					</div>
				</li>
				<li>
					<a href="javascript:void(0)" class="message js_message_btn">
						<img src="/gaochao/Public/Backend/img/email.png" alt=""><div class="message_num js_message_num">10</div>
					</a>
					<div class="message_box treeview-menu js_message_box">
						<div class="header text-center">你有 <span class="js_message_num">10</span> 个待查看消息</div>
						<ul class="message_list">
							<li>
								<a href="#">
									<h6>新检测任务通知<small>5 分钟前</small></h6>
								</a>
							</li>
							<li>
								<a href="#">
									<h6>检测异常通知<small>5 分钟前</small></h6>
								</a>
							</li>
							<li>
								<a href="#">
									<h6>新采样任务通知<small>5 分钟前</small></h6>
								</a>
							</li>
			            </ul>
			            <div class="footer"><a href="#">查看所有消息</a></div>
					</div>
				</li> -->
				<li><a href="/gaochao/">查看首页</a></li>
				<li><a href="<?=U('Backend/Base/Public/logout')?>" class="ajax-get">退出登录</a></li>
			</ul>
		</div>
	</nav>
</header>
<div class="message_bg js_message_bg"></div>
	<aside class="main-sidebar">
	<section class="sidebar">
		<!-- Sidebar user panel (optional) -->
		<div class="sidebar_top clearfix">
			<a href="<?=U('Backend/Base/Info/edit')?>" class="image">
				<img src="/gaochao/Public/Backend/img/login_logo.png" alt="<?=session('username')?>的头像" />
				<div><?=session('username')?></div>
			</a>
		</div>
		<ul class="sidebar-menu">
			<?php
 foreach($menu_2 as $row){ ?>
			<li class="menu">
				<a class="<?=$row['class']?>" href="<?=U($row['url'])?>"><?=$row['title']?></a>
			</li>
			<?php
 } ?>
			<li style="display:none;">
				<a href="<?=U('Backend/Demo/Index/index')?>">列表</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'form'))?>">表单</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'form_tab'))?>">表单带选项卡</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'editor'))?>">编辑器调用</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'dialog'))?>">弹窗</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'file'))?>">文件上传</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'down'))?>">下载</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'imgDel'))?>">图片删除</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'tips'))?>">提示</a>
				<a href="<?=U('Backend/Demo/Index/index',array('tpl'=>'timeline'))?>">时间轴</a>
			</li>
		</ul>
	</section>
	<footer class="main-footer clearfix">
		<div>版权所有 &copy; 2015-<?=date("Y")?></div>
		<div><a href="http://www.cnsunrun.com" target="_blank">武汉尚软科技</a></div>
	</footer>
</aside>
	<div class="wrapper">
		<div class="content-wrapper js_content_wrapper">
			<div class="breadcrumb_box">
				<section class="top_menu">
					<ul class="nav nav-tabs">
					<li class="side_menu"><a href="javascript:void(0);" class="sidebar-toggle js_offcanvas"><img src="/gaochao/Public/Backend/img/side_menu.png"></a></li>
					<?php
 $small_tips = ''; foreach($menu_3 as $row){ if($menu_info['id']==$row['id'] || $row['id'] == $menu_path[3]){ $row['class'] = 'active'; $small_title=$row['title']; $small_tips = str_replace("\n",'</br>',$row['description']); } ?>
						<li class="js_tab <?=$row['class']?>"><a href="<?=U($row['url'])?>"><?=$row['title']?></a></li>
					<?php
 } ?>

					<?php
 if($small_tips){ ?>
						<!-- 问号 -S -->
						<li class="pull-right">
							<a href="javascript:void(0)" class="tip_msg_box js_tip_msg_box">
								<img src="/gaochao/Public/Backend/img/tip.png" alt="">
								<div class="tip_msg_up js_tip_msg_up"></div>
							</a>
						</li>
						<!-- 问号 -E -->
					<?php
 } ?>
					</ul>

				</section>
				<!-- 问号 -S -->
				<div class="tip_msg js_tip_msg">
					<h6>提示</h6>
					<?=$small_tips?>
					<div class="colse_dialog js_colse_dialog"></div>
				</div>
				<!-- 问号 -E -->
			</div>
			<section class="content">
				
	<style>
		.detail_title { font-size: 20px;text-align: center;line-height: 40px; }
		.detail_content { font-size: 14px;text-indent: 2em;line-height: 24px;margin-top: 20px; }
		.detail_rank { margin:20px 50px;height: 6px;border-radius: 3px;background-color: red; }
		.detail_agree { float: left;width:100px;height: 6px;border-radius: 3px 0 0 3px;background-color: green; }
		.detail_waiver { float: left;width:100px;height: 6px;background-color: #ddd; }
		.detail_tip { margin-right: 20px; }
		.detail_tip span { display: inline-block;width:20px;height: 6px;margin-left: 5px; }
		.bg_red { background-color: red; }
		.bg_green { background-color: green; }
		.bg_ddd { background-color: #ddd; }
		.ml-50 { margin-left: 50px;margin-bottom: 20px; }

.ry_div{ margin: 0 -10px; }
.ry_div a{ display: block; width:184px; height: 184px; line-height: 160px; text-align: center; position: relative; padding: 10px; border: 1px solid #ddd; float: left; margin: 8px; }
.ry_div a .img{ max-width: 100%; max-height: 100%;  }
.ry_div a .name{ width: 100%; height: 100%; transition: 0.5s; -webkit-transition: 0.5s; transform: scale(0); -webkit-transform: scale(0); opacity: 0; filter:alpha(opacity=0); padding: 0 15px; padding-top: 60px; position: absolute; left: 0; top: 0; background: #000; background: rgba(0,0,0,0.6); line-height: 30px; color: #fff; }
.ry_div a .name img{ height: 30px; }
.ry_div a .name div{ padding-top: 10px; font-size: 14px; }
.ry_div a:hover .name{ transform: scale(1); -webkit-transform: scale(1); opacity: 1;filter:alpha(opacity=100);  }


.big_imgs{display:none; position: fixed; left: 0; top: 0; width: 100%; height: 100%; z-index: 500; }
.big_imgs .box_div{ width: 100%; height: 100%; position: relative; overflow: hidden; }
.big_imgs .boxs{ text-align: center; height: 80%; color: #fff; transform: scale(0.8); -webkit-transform: scale(0.8); opacity: 0; filter:alpha(opacity=0); margin-top: -300px; }
.big_imgs_show .boxs{transform: scale(1); -webkit-transform: scale(1); opacity: 1; filter:alpha(opacity=100);}
.big_imgs .boxs p{ line-height: 24px; font-size: 14px; padding:10px 0; margin: 0 auto; max-width: 600px; }
.big_imgs .boxs img{ max-height: 100%; max-width: 100% }
.big_imgs .btns{ width: 46px; height: 63px; transition: 0.3s; display: block; position: absolute; top: 50%; margin-top: -31px; z-index: 501 }
.big_imgs .btns:hover{ opacity: 0.8 }
.big_imgs .right_btn{ right: 15px; }
.big_imgs .left_btn{ left: 15px; }
.big_imgs .close_btn{ display: block; width: 30px; height: 30px; position: absolute; z-index: 501; right: 20px; top: 20px; }
.big_imgs .close_btn img{ width: 100% }
.big_imgs .close_btn:hover{ transform: rotate(180deg); }
.imgmarks{ display: none; position: fixed; left: 0; top: 0; width: 100%; height: 100%; z-index: 400; background: #000;opacity: 0.8; filter:alpha(opacity=80); }
.transition5{ transition: 0.5s; -webkit-transition: 0.5s; }
	</style>
	<div class="form-group">
		<div class="detail_title"><?=$info['title']?></div>

		<!-- <div class="detail_content">我发加了咖啡就打算了放假 经理说会计分录刷卡缴费 删掉了会计分录山东矿机， 手机就福利科技第三方来看积分第三垃圾分类是否离开家飞洒大立科技拉解放东路萨克解放东路ask交付了刷卡缴费拉克丝激发路口解放拉精神分裂加了罚款经理说开发阶段说了开发。</div> -->
		    <!-- <div class="ry_div clearfix js_pics_div"> -->
        <?php
 $images = []; foreach ($images as $k => $v) { ?>
        <a href="javascript:;" class="boxs">
            <img class="img" src="/gaochao/<?=$v['image']?>" alt="">
            <div class="text-center name">
                <img src="/gaochao/Public/Backend/img/icon2.png" alt="">
                <div><?=$v['title']?></div>
            </div>
        </a>
        <?php
 } ?>
   		<!-- </div> -->
	</div>
	<table class="table js_table table-bordered">
		<thead>
			<tr>
				<th colspan="20" class="clearfix">
					<form action="<?=U('info');?>" class="form-inline pull-left" method="get">
						<input type="text" class="form-control input-sm" style="width: 160px;" placeholder="用户昵称 | 手机号" name="keywords" value="<?=$keywords?>">

						<select name="type" class="form-control input-sm">
							<option value=''>意见类型</option>
							<?php
 $type2 = $type; array_shift($type2); foreach ($type2 as $k => $v) { ?>
							<option value="<?=$v['r_value']?>" <?=(I('type') == $v['r_value'])?'selected="selected"':''?> ><?=$v['title']?></option>
							<?php
 } ?>
						</select>
						<input type="start_date" style="min-width:auto;width:140px!important" onclick="WdatePicker({skin:'backend',dateFmt:'yyyy-MM-dd'})" class="form-control input-sm wdate" placeholder="开始时间" name="start_date" value="<?=$start_date?>">
						<input type="stop_date" style="min-width:auto;width:140px!important" onclick="WdatePicker({skin:'backend',dateFmt:'yyyy-MM-dd'})" class="form-control input-sm wdate" placeholder="结束时间" name="stop_date" value="<?=$stop_date?>">
						<button type="submit" class="btn btn-default btn-sm" target-form="operate_form">搜索</button>
						<input type="hidden" name="ids" value="<?=$info['id']?>" />
					</form>
					<div class="pull-right">
					<a href="<?=U('export',I())?>" class="btn btn-sm bg-purple" target-form="ids" title="导出">导出</a>
					<a href="<?=U('index',I())?>" class="btn btn-default cancel">返回列表</a>
					</div>
				</th>
			</tr>
		</thead>
		<tbody>
			<tr role="row">
				<th width="26%">投票用户</th>
				<th width="26%">用户手机号</th>
				<th width="28%" class="text-center">意见</th>
				<th width="18%" class="text-center">投票时间</th>
			</tr>
			<?php
 if(!$list){ ?>
			<tr>
				<td colspan="80"><?php
 $tips='暂无投票数据'; if(empty($tips)){ $tips = '暂无数据'; } ?>
<div class="noneshowlist text-center" style="height:auto;line-height:normal;">
		<h4><?=$tips?></h4>
</div></td>
			</tr>
			<?php
 } ?>
			<?php
 foreach($list as $row){ ?>
			<tr>
				<td><?=$row['username']?></td>
				<td><?=$row['mobile']?></td>
				<?php
 $color = array(1=>'#4fa76b',2=>'red',3=>'#919a9a'); ?>
				<td class="text-center"><span style="color:<?=$color[$row['type']]?>;"><?=$type[$row['type']]['title']?></span></td>
				<td class="text-center"><?=$row['add_time']?></td>
			</tr>
			<?php
 } ?>
		</tbody>
	</table>
    <div class="big_imgs js_big_imgs" style="display: none;">
        <a href="javascript:;" class="btns right_btn"><img src="/gaochao/Public/Backend/img/banner_rightarrow_hover.png" alt=""></a>
        <a href="javascript:;" class="btns left_btn"><img src="/gaochao/Public/Backend/img/banner_leftarrow_hover.png" alt=""></a>
        <a href="javascript:;" class="close_btn transition5"><img src="/gaochao/Public/Backend/img/close2.png" alt=""></a>
        <div class="box_div">
            <div class="boxs transition5">
                <img src="" alt="">
            </div>
        </div>
    </div>
    <div class="imgmarks js_marks" style="display: none;"></div>
	<div class="pages">
		<?=$page?>
	</div>
	<script type="text/javascript" src="/gaochao/Public/Plugins/select2/select2.full.js?1518509003"></script>
	<script type="text/javascript" src="/gaochao/Public/Plugins/select2/super_select2.js?1518509003"></script>
	<script type="text/javascript">
		$(function(){
			$('.super_select2').super_select2();


            var pic_now=0;
            $('.js_big_imgs .boxs').css('margin-top',$(window).height()*0.1).css('line-height',$(window).height()*0.8+"px");
            $(document).on('click','.js_pics_div a',function(){

                var index=$(".js_pics_div a").index(this);
                var _this=$(this);
                pic_now=index;
                $('.js_big_imgs .boxs img').attr('src',$('.js_pics_div a').eq(pic_now).find('img').attr('src'));
                $('.js_big_imgs .boxs p').html($('.js_pics_div a').eq(pic_now).find('p').html());
                $('.js_marks').fadeIn(300);
                $('.js_big_imgs').fadeIn(300).addClass('big_imgs_show');
            })

            $('.js_big_imgs .close_btn').click(function(){
                $('.js_marks').fadeOut(300);
                $('.js_big_imgs').fadeOut(300).removeClass('big_imgs_show');
            })

            $('.js_big_imgs .right_btn').click(function(){
                pic_now++;
                if(pic_now>=$('.js_pics_div a').length){
                    pic_now=0;
                }
                pic_fun();
            })
            $('.js_big_imgs .left_btn').click(function(){
                pic_now--;
                if(pic_now<=-1){
                    pic_now=$('.js_pics_div a').length-1;
                }
                pic_fun();
            })
            function pic_fun(){
                $('.js_big_imgs').removeClass('big_imgs_show');
                setTimeout(function(){
                    $('.js_big_imgs .boxs img').attr('src',$('.js_pics_div a').eq(pic_now).find('img').attr('src'));
                    $('.js_big_imgs .boxs p').html($('.js_pics_div a').eq(pic_now).find('p').html());
                    $('.js_big_imgs').addClass('big_imgs_show');
                },300)
            }
		});
	</script>	

			</section>
		</div>
		<!-- Main Footer -->
<?php  ?>

		
	</div>
	<!-- 图片弹出框 S -->
<div class="image_dialog">
	<img src=""  style="max-width:100%;" />
</div>
<div class="bg_image_shadow">
	
</div>

<script>
	$(function(){
		var winW = $(window).width();
		var winH = $(window).height();
		$('.image_dialog').css({'max-width':winW*0.8+'px'});

		$('.bg_image_shadow').on('click',function(){
			$('.image_dialog').css({'visibility':'hidden'});
			$('.bg_image_shadow').css({'visibility':'hidden'});
		});
	});
</script>
<!-- 图片弹出框 E -->
</body>
<script type="text/javascript" src="/gaochao/Public/Plugins/select2/select2.full.js?1518509003"></script>
<script type="text/javascript" src="/gaochao/Public/Plugins/select2/i18n/zh-CN.js?1518509003"></script>
<script type="text/javascript" src="/gaochao/Public/Plugins/select2/super_select2.js?1518509003"></script>
<script type="text/javascript" src="/gaochao/Public/Plugins/super_level/conf/area_data.js?1518508999"></script>
<script type="text/javascript" src="/gaochao/Public/Plugins/super_level/conf/select.js?1518508999"></script>
<script type="text/javascript" src="/gaochao/Public/Plugins/super_level/super_tree.js?1518508999"></script>
<script type="text/javascript" src="/gaochao/Public/Plugins/super_level/super_level.js?1518508999"></script>
<script>
$(function(){
	if($('.js_super_level').size()){
		new Super_level(area_data,'select',{set_super: {type: 'area',$obj: $('.js_super_level')}});
	}
	if($('.js_super_select2').size()){
		$('.js_super_select2').super_select2();
	}
	
});
</script>
</html>