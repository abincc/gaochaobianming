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
				
	<section class="content">
		<!-- 基本信息 S -->
		<div class="row">
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box">
					<span class="info-box-icon bg-aqua"><img src="/gaochao/Public/Backend/img/home/order.png" /></span>
					<div class="info-box-content">
						<span class="info-box-text">待发货订单</span>
						<span class="info-box-number"><?=$order_num['tp_sum']?> 个</span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box">
					<span class="info-box-icon bg-red"><img src="/gaochao/Public/Backend/img/home/money.png" /></span>
					<div class="info-box-content">
						<span class="info-box-text">总收入</span>
						<span class="info-box-number">¥ <?=$total['tp_sum']?></span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box">
					<span class="info-box-icon bg-green"><img src="/gaochao/Public/Backend/img/home/member.png" /></span>
					<div class="info-box-content">
						<span class="info-box-text">用户数量</span>
						<span class="info-box-number"><?=$member_num['tp_sum']?></span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box">
					<span class="info-box-icon bg-yellow"><img src="/gaochao/Public/Backend/img/home/product.png" /></span>
					<div class="info-box-content">
						<span class="info-box-text">商品总数</span>
						<span class="info-box-number"><?=$product_num['tp_sum']?> 个</span>
					</div>
				</div>
			</div>
		</div>
		<!-- 基本信息 E -->
		<!-- 月度订单报表 S -->
		<div class="row">
			<div class="col-md-12">
				<div class="box">
					<!-- 报表 S -->
					<div class="box-body">
						<div class="row">
							<!-- 报表曲线图 S -->
							<div class="col-md-12">
								<div class="chart">
									<!-- 报表图  -->
									<div id="echarts" style="height:260px;"></div>
								</div>
							</div>
							<!-- 报表曲线图 S -->
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 月度订单报表 E -->
		<div class="row">
			<div class="col-md-6">
				<!-- 最新订单 S -->
				<div class="box box-info">
					<div class="box-header with-border">
						<h3 class="box-title">最新订单</h3>
					</div>
					<div class="box-body">
						<div class="table-responsive">
							<table class="table no-margin">
								<thead>
								<tr>
									<th>订单号</th>
									<th>订单金额</th>
									<th>状态</th>
									<th>下单时间</th>
								</tr>
								</thead>
								<tbody>
									<?php foreach($new_order as $row){ ?>
										<tr>
											<td><a href="<?=U('Backend/Order/Index/detail',array('ids'=>$row['id']))?>"><?=$row['order_no']?></a></td>
											<td><?=$row['money_total']?></td>
											<?php if($row['status'] == 10){ ?>
												<td><span class="label label-info">待付款</span></td>
											<?php }else if($row['status'] == 20){ ?>
												<td><span class="label label-danger">已付款</span></td>
											<?php }else if($row['status'] == 40){ ?>
												<td><span class="label label-warning">已收货</span></td>
											<?php }else if($row['status'] == 70){ ?>
												<td><span class="label label-success">已退款</span></td>
											<?php } ?>
											<td><?=$row['add_time']?></td>
										</tr>
									<?php } ?>
								</tbody>
							</table>
						</div>
					</div>
					<div class="box-footer clearfix">
						<a href="<?=U('Backend/Order/Index/index')?>" class="btn btn-sm btn-default btn-flat pull-right">查看所有</a>
					</div>
				</div>
				<!-- 最新订单 E -->
			</div>
			<div class="col-md-6">
				<!-- 最新订单 S -->
				<div class="box box-info">
					<div class="box-header with-border">
						<h3 class="box-title">最新注册</h3>
					</div>
					<div class="box-body">
						<div class="table-responsive">
							<table class="table no-margin">
								<thead>
								<tr>
									<th>手机号</th>
									<th>姓名</th>
									<th>注册地区</th>
									<th>注册时间</th>
								</tr>
								</thead>

								<tbody>
									<?php foreach($near_register as $row){ ?>
										<tr>
											<td><a href="<?=U('Backend/Member/Member/detail',array('ids'=>$row['id']))?>"><?=$row['mobile']?></a></td>
											<td><?=$row['username']?></td>
											<td><?=ip_to_location($row['register_ip'])?></td>
											<td><?=$row['register_time']?></td>
										</tr>
									<?php } ?>
								</tbody>
							</table>
						</div>
					</div>
					<div class="box-footer clearfix">
						<a href="<?=U('Backend/Member/Member/index')?>" class="btn btn-sm btn-default btn-flat pull-right">查看所有</a>
					</div>
				</div>
				<!-- 最新订单 E -->
			</div>
		</div>
	</section>
	<script type="text/javascript" src="/gaochao/Public/Plugins/echarts/echarts.js?1518509001"></script>
	<script type="text/javascript">
	var myChart = echarts.init(document.getElementById('echarts'));
	var title = <?=json_encode(array_values($order_count_date['status']))?>;
	var option = {
	    title: {
	        text: "月度订单报表",
	        <?php
 $date_line = array_values($order_count_date['date_line']); ?>
	        subtext: "统计时间: <?=array_shift($date_line)?> -<?=end($date_line)?>"
	    },
		tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data: title,
	        x: "right"
	    },
	    toolbox: {
	        show : true,
	        feature : {
	        }
	    },
	    calculable: true,
	    xAxis: [
	        {
	            type: "category",
	            data: <?=json_encode(array_values($order_count_date['date_line']))?>,
	            boundaryGap: false,
	            splitLine: {
	                show: false
	            }
	        }
	    ],
	    yAxis: [
	        {
	            type: "value",
	            splitLine: {
	                show: true
	            }
	        }
	    ],
	    series: [
	        {
	            name: title[0],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_5']))?>,
	            smooth: true
	        },
	        {
	            name: title[1],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_10']))?>,
	            smooth: true
	        },
	        {
	            name: title[2],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_20']))?>,
	            smooth: true
	        },
	        {
	            name: title[3],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_30']))?>,
	            smooth: true
	        },
	        {
	            name: title[4],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_40']))?>,
	            smooth: true
	        },
	        {
	            name: title[5],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_50']))?>,
	            smooth: true
	        },
	        {
	            name: title[6],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_60']))?>,
	            smooth: true
	        },
	        {
	            name: title[7],
	            type: "line",
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    areaStyle: {
	                        type: "default"
	                    }
	                }
	            },
	            data: <?=json_encode(array_values($order_count_date['order_70']))?>,
	            smooth: true
	        } 
	    ],
	    grid: {
	        x: 50,
	        x2: 30,
	        y2: 30,
	        y: 60,
	        borderWidth: 0
	    }
	};
	myChart.setOption(option);
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