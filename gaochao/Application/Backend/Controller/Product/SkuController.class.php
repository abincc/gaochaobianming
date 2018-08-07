<?php
/**
 * 商品 SKU 列表
 */
namespace Backend\Controller\Product;

class SkuController extends IndexController {

	protected $table 		= 'product';
	protected $table_view 	= 'sku_view';

	public function index() {

		$map = $this->get_list_map();
		$_data = [];		
		$_data = $this->page(D($this->table_view), $map, 'product_id asc');
		if($_data['list']) $_data['list'] = $this->handle_index($_data['list']);
		$_data['category'] = array_to_select(get_no_hid('product_category','add_time desc'), I('category_id'));

		$this->assign($_data);
		$this->display();
	}
	
	/**
	 * 处理首页数据
	 */
	protected function handle_index($list, $glue = '<br />') {
		$_data = [];
		
		$spec = get_no_del('product_spec');
		$spec_value = get_no_del('product_spec_value');
		// bug($list);
		foreach($list as $v) {
			$spec_spec_value = [];
			$_spec = unserialize($v['spec']);
			$_spec_value = unserialize($v['spec_value']);
			$_spec_value_key = array_keys($_spec_value);
			$i = 0;
			foreach($_spec as $k=> $v_spec) {
				$spec_spec_value[] = $spec[$k]['title'].'：'.$spec_value[$_spec_value_key[$i]]['title'];
				$i++;
			}
			$v['spec_value_title'] = implode($glue, $spec_spec_value);
			if(empty($v['spec_value_title'])){
				$v['spec_value_title'] = '~';
			}
			$_data[] = $v;
		}
		
		return $_data;
	}


	/**
     * 库存导入
     * @author llf
     * @param
     */
    public function import(){

		if(IS_POST){
			$type = intval(I('type'));
			if(!$type && !in_array(1,2)){
				//1-覆盖 2-累加
				$this->error('请选择库存导入模式');
			}
			//上传导入文件
			$config = array(
				'maxSize'    => 3145728,    
				'rootPath'   => './',				//相对网站根目录
				'savePath'   => 'Uploads/Product/Sku/',	//文件具体保存的目录
				'saveName'   => array('uniqid',''),    
				'exts'       => array('csv','xlsx','xls'),    
				'autoSub'    => true,    
				'subName'    => array('date','Ymd'),
			); 
			$Upload 	 = new \Think\Upload($config);        /* 实例化上传类 */
			$file_info   = $Upload->upload();
			if(!$file_info){
				$this->error("导入文件上传失败");
			}
			// $file =  'C:\Users\cnsunrun\Downloads\sku_export_20170502_182536.xls';
			// $file = './Uploads/Product/Sku/20170502/59085cb6df670.xls';
			$file = './'.$file_info['file']['savepath'].$file_info['file']['savename'];
			$import_config = array(
				array('title' => 'sku_id', 'name' => 'id'),
				array('title' => 'product name', 'name' => 'title'),
				array('title' => 'specification', 'name' => 'spec_value_title'),
				array('title' => 'Stock', 'name' => 'inventory'),
				array('title' => 'price', 'name' => 'price'),
			);
			$data = A('Common/Excel','',1)->load_excel($import_config, $file);
			// echo '<pre>';
			// var_dump($data);
			// die();
			try{
				if(empty($data)){
				    throw new ExceptionNew("导入数据内容未空或导入文件解析失败");
				}
				//开启事务进行循环插入操作
				$SKU = M("product_sku"); 
				$M = M();
				$M->startTrans();
				foreach ($data as $k => $v) {
					if($type == 1){
							$d = array(
								'inventory'	=>intval($v['inventory']),
								'price'		=>floatval($v['price']),
							);

						$res = $SKU->where(array('id'=>$v['id']))->setField($d);
						if($res === false){
							throw new ExceptionNew("库存信息更新失败");
						}
					}
					if($type == 2){
						$sql = 'UPDATE `sr_product_sku` SET `price` = `price`+'.floatval($v['price']).',`inventory` = `inventory` + '.intval($v['inventory'])." WHERE `id` = ".$v['id'];
						$res = $SKU->execute ( $sql );
						if(!is_numeric($res)){
							throw new ExceptionNew("库存信息更新失败");
						}
					}

				}
			}catch (Exception $e){
				$M->rollback();
				$this->error($e->getMessage());
			}
			$M->commit();
			$this->success('导入数据成功');
		}else{
			$this->display();
		}
        
    }

	/**
     * 库存、价格 导出
     * @author llf
     * @param
     */
    public function export(){

		$map 	= $this->get_list_map();
		$result = get_result(D($this->table_view), $map,'product_id desc');

		if(!empty($result)) $result = $this->handle_index($result,'-');
		/*填充数据*/
		$data['result']    = $result;
		/*填充表名*/
		$data['sheetName'] = 'sku_export_'.date('Ymd_His');

		$export_config = array(
			array('title' => 'sku_id', 'field' => 'id'),
			array('title' => 'product name', 'field' => 'title'),
			array('title' => 'specification', 'field' => 'spec_value_title'),
			array('title' => 'Stock', 'field' => 'inventory'),
			array('title' => 'price', 'field' => 'price'),
		);
		A('Common/Excel','',1)->export_data($export_config,$data);
    }

	/**
	 * 导出搜索条件
	 * @author llf
	 * @param   
	 */
	private function get_list_map(){

		$map 		= array();
		$keywords 	= trim(I('keywords'));
		$ids 		= array_filter(I('ids'));
		$category_id = I('category_id',0,'int');

		if(I('keywords') != ''){
			$map['you_sku|title'] = array('LIKE',"%$keywords%");
		}
		if(is_array($ids) && !empty($ids)){
			$map['id'] = array('IN',$ids);
		}
		if(!empty($category_id)){
			$forum = get_no_hid('product_category','sort desc');
			$category_ids = array($category_id);
			array_walk($forum, function($a) use(&$category_ids,$category_id){
				if($a['pid'] == $category_id){
					$category_ids[] = $a['id'];
				}
			});
			$map['sub_category_id'] = array('IN',$category_ids);
		}
	    return $map;
	}

}