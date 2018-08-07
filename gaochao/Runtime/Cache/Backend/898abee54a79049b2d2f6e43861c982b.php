<?php if (!defined('THINK_PATH')) exit();?><form action="<?=U('import')?>" name="stock_form" class="pull-left  stock_form js_form" method="post" enctype="multipart/form-data" >
	<div class="form-group">  
		<div class="control-label">
			<span class="required">*</span>
			上传文件（导出文件格式）
		</div>
		<div>		
			<input type="file" name="file" >
		</div>
    </div>
	<div class="form-group">  
		<div class="control-label">
			<span class="required">*</span>
			选择导入模式
		</div>
		<div>		
			<select name="type" class="form-control" datatype="n" nullmsg="请选择导入模式" errormsg="请选择导入模式">
				<option value="">请选择导入模式</option>
				<option value="1">累加</option>				
			</select>
		</div>
	</div>
	<div class="form-btn clearfix">
		<input type="submit" class="pull-left btn btn-primary" value="提交" />
		<div class="js_validTips pull-left" style="height:34px;line-height:34px;">
			<span class="js_tipContent Validform_checktip"></span>
		</div>
	</div> 
</form>