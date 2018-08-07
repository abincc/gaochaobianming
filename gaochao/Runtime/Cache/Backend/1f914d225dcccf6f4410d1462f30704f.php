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
				
	<script type="text/javascript" src="/gaochao/Public/Plugins/uploadify/jquery.uploadify.min.js?v=<?=time().rand(0,9999)?>"></script>
	<script type="text/javascript" src="/gaochao/Public/Plugins/my97date/WdatePicker.js?1518508971"></script>
	<div class="box box-body">
		<form action="<?=U('')?>" method="post" class="operate_form">
			<input type="hidden" name="id" value="<?=$info['id']?>"/>
			<input type="hidden" name="adspace_id" value="<?=I('adspace_id')?>"/>
			<input type="hidden" name="old_image" value="<?=$info['save_path']?>"/>
			<div class="form-group">
				<div class="control-label">广告类型</div>
				<div>
					<select name="type" class="form-control width_auto js_type">
						<option value="image" <?php if($info['type']=='image' || !$info['type']){echo "selected";}?>>图片</option>
						<option value="code" <?php if($info['type']=='code'){echo "selected";}?>>HTML代码</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<div class="control-label">广告标题</div>
				<div><input type="text" class="form-control width_auto" name="title" value="<?=$info['title']?>"></div>
			</div>
			<div class="form-group js_image" <?php if($info['type']=='code'){echo 'style="display:none;"';}?>>
				<div class="control-label">上传图片</div>
				<div>
					<?php
 $config=array( 'table' =>'banner', 'name' =>'image', 'val_key' =>"save_path" ); echo W("Common/Img/index",array($config,$info['id'])); ?>
				</div>
			</div>
			<div class="form-group js_image" <?php if($info['type']=='code'){echo 'style="display:none;"';}?>>
				<div class="control-label">广告链接</div>
				<div><input type="text" class="form-control width_auto" name="url" value="<?=$info['url']?>"></div>
			</div>

			<div class="form-group js_code" <?php if($info['type']=='image' || !$info['type']){echo 'style="display:none;"';}?>>
				<div class="control-label">HTML代码</div>
				<div><textarea class="form-control width_auto" name="content"><?=$info['content']?></textarea></div>
			</div>
			<div class="form-group">
				<div class="control-label">排序</div>
				<div class="form-inline"><input type="text" class="form-control" name="sort" value="<?=$info['sort']?>"></div>
			</div>
			<div class="form-group">
				<div class="control-label">起止时间</div>
				<div class="form-inline">
					<?php
 if($info['start_time']){ $start_time=$info['start_time']; }else{ $start_time=date("Y-m-d",time()); } ?>
					<input type="text" class="form-control" size="10" name="start_time" value="<?=$start_time?>" id="start_time" readonly onClick="WdatePicker()">
					-
					<?php
 if($info['end_time']){ $end_time=$info['end_time']; }else{ $end_time=date("Y-m-d",strtotime("+1 day")); } ?>
					<input type="text" class="form-control" size="10" name="end_time" value="<?=$end_time?>" id="end_time" readonly onClick="WdatePicker()">
				</div>
			</div>
			<div class="form-group">
				<div class="control-label">状态</div>
				<div class="form-inline">
					<select name="is_hid" class="form-control">
						<option value="1" <?php if($info['is_hid']=='1' || !$info['is_hid']){echo "selected";}?>>启用</option>
						<option value="0" <?php if($info['is_hid']=='0'){echo "selected";}?>>禁用</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<input type="submit" class="btn btn-primary ajax-post" target-form="operate_form" value="提交">
				<a href="<?=U('lists',array('adspace_id'=>I('adspace_id')))?>" class="btn btn-default cancel">取消并返回</a>
			</div>
		</form>
	</div>

			</section>
		</div>
		<!-- Main Footer -->
<?php  ?>

<script type="text/javascript">
$(document).ready(function(){
	$(".js_type").change(function(){
		if($(this).val()=='image'){
			$(".js_code").hide();
			$(".js_image").show();
		}else{
			$(".js_code").show();
			$(".js_image").hide();
		}
	})
})
</script>

		
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