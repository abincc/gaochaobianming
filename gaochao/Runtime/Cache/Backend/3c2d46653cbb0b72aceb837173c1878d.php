<?php if (!defined('THINK_PATH')) exit();?><form action="<?=U('set')?>" method="post" class="dialog_form">
	<input type="hidden" name="id" value="<?=$info['id']?>">
	<table class="table js_table table-bordered js_set_adspace">
		<tr>
			<td align="center" widtd="100" style="background:#efefef;">行数</td>
			<td style="background:#efefef;">广告比例（中间使用:分割）</td>
			<td style="background:#efefef;"></td>
		</tr>
		<?php
 $scale_arr=json_decode($info['scale']); $index=1; if($scale_arr[0]==''){ $val=1; }else{ $val=$scale_arr[0]; } ?>
		<tr>
			<td align="center"><?=$index?></td>
			<td><input type="text" name="scale[]" class="form-control" style="border:none;" value="<?=$val?>"></td>
			<td>默认</td>
		</tr>
		<?php
 foreach ($scale_arr as $key => $val){ $index++; if($key>0){ ?>
		<tr>
			<td align="center"><?=$index?></td>
			<td><input type="text" name="scale[]" class="form-control" style="border:none;" value="<?=$val?>"></td>
			<td><div class="delLine js_delLine">删除</div></td>
		</tr>
		<?php
 } } ?>
		<tr>
			<td align="right" colspan="3">
				<input type="button" class="btn btn-primary js_add_adspace" style="margin-right:5px;" value="增加一行">
				<input type="submit" class="btn btn-default ajax-post" target-form="dialog_form" value="提交">
			</td>
		</tr>
	</table>
</form>