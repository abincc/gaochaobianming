<?php
/**
 * EXCEL处理类
 * @作用：配置EXCEL信息，处理EXCEL
 * @time 2016-9-18
 * @author 秦晓武
 */
namespace Common\Controller;
use Think\Controller;

class ExcelController extends Controller {

	/**
	 * 实例化PHPExcel对象，并配置文件基本信息
	 * @return PHPExcel对象
	 */
	public function excel_obj_config(){
		vendor("PHPEXCEL.PHPExcel");
		$objPHPExcel=new \PHPExcel();
		$objPHPExcel->getProperties()
								->setCreator("sunrun")
								->setLastModifiedBy("sunrun")
								->setKeywords("sunrun")
								->setCategory("zstyle");
		return $objPHPExcel;
	}
	/**
	 * 输出Excel表格
	 * @param  [type] $objPHPExcel PHPExcel对象，即数据集
	 * @param  [type] $fileName    导出的文件名
	 * @return [type]
	 */
	public function excel_out($objPHPExcel,$fileName){
		$_type=I('_type');
		switch ($_type) {
			case '5':
				$_type=".xls";
				$xlsWriter=new \PHPExcel_Writer_Excel5 ($objPHPExcel);
				break;
			case '2007':
				$_type=".xlsx";
				$xlsWriter=new \PHPExcel_Writer_Excel2007 ($objPHPExcel);
				break;
			default:
				$_type=".xls";
				$xlsWriter=new \PHPExcel_Writer_Excel5 ($objPHPExcel);
		}
		$outputFileName = $fileName.$_type;
		header("Content-Type: application/force-download");
		header("Content-Type: application/octet-stream");
		header("Content-Type: application/download");
		header('Content-Disposition:inline;filename="'.$outputFileName.'"');
		header('Cache-Control: max-age=0');
		header("Pragma: no-cache");
		$xlsWriter->save("php://output");
	}
	/**
	 * 导出$data的信息
	 * @param   $[config] [格式：$config = array(
	 *                                array(
	 *                                        'title' => '昵称',
	 *                                        'field' => 'nickname',
	 *                                        'width' => '15', //单元格宽度
	 *                                    ),
	 *                                array(
	 *                                        'title' => '联系方式',
	 *                                        'field' => 'mobile',
	 *                                        'width' => '20' //单元格宽度
	 *                                    ),
	 *
	 *                             )]
	 * @time 2016-9-18
	 * @author 秦晓武
	 */
	public function export_data($config,$data){
		/**总页数集合*/
		$cellName = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','AA','AB','AC','AD','AE','AF','AG','AH','AI','AJ','AK','AL','AM','AN','AO','AP','AQ','AR','AS','AT','AU','AV','AW','AX','AY','AZ');
		/**导出Excel*/
		$objPHPExcel=$this->excel_obj_config();

		/**设置操作的工作表*/
		//$objPHPExcel->setActiveSheetIndex('0');
		$sheet = $objPHPExcel->getActiveSheet();

		/**设置工作表的名字*/
		$sheet->setTitle($data['sheetName']);

		/**设置工作表的第一栏标题及列宽*/
		for($i = 0; $i< count($config);$i++){
			$sheet->setCellValue($cellName[$i] . '1',$config[$i]['title']);
			$sheet->getColumnDimension($cellName[$i])->setWidth($config[$i]['width']);
		}
		$i = 2;
		foreach($data['result'] as $key => $item){
			/**设置单个元的值*/
			for($j = 0 ; $j< count($config);$j++){
				$sheet->setCellValue($cellName[$j] . $i, $item[$config[$j]['field']]);
			}
			$i++;
		}
		//$objPHPExcel->createSheet();
		$this->excel_out($objPHPExcel,$data['sheetName'],$type);
	}
	/**
	 * 导入excel文件
	 * @param  string $file excel文件路径
	 * @return array        excel文件内容数组
	 */
	public function import_excel($file){
		/*判断文件是什么格式*/
		$type = pathinfo($file);
		$type = strtolower($type["extension"]);
		$type=$type==='csv' ? $type : 'Excel5';
		ini_set('max_execution_time', '0');
		Vendor('PHPExcel.PHPExcel');
		/*判断使用哪种格式*/
		$objReader = PHPExcel_IOFactory::createReader($type);
		$objPHPExcel = $objReader->load($file);
		$sheet = $objPHPExcel->getSheet(0);
		/*取得总行数*/
		$highestRow = $sheet->getHighestRow();
		/*取得总列数*/
		$highestColumn = $sheet->getHighestColumn();
		/*循环读取excel文件,读取一条,插入一条*/
		$data=array();
		/*从第一行开始读取数据*/
		for($j=1;$j<=$highestRow;$j++){
				/*从A列读取数据*/
				for($k='A';$k<=$highestColumn;$k++){
						/*读取单元格*/
						$data[$j][]=$objPHPExcel->getActiveSheet()->getCell("$k$j")->getValue();
				}
		}
		return $data;
	}

/**
     * 导入EXCEL
     * 
     * @param $[config] [格式：$config
     *            = array(
     *            ['昵称','nickname','15'],
     *            ['联系方式','mobile','20'],
     *            );
     * @param $file_path 文件路径            
     * @param $method [<回调处理函数>]
     *            @time 2016-10-10
     * @author 陶君行<Silentlytao@outlook.com>
     */
    public function load_excel($config, $file_path = '')
    {

        /**
         * 引入PHPEXCEL类
         */
        vendor("PHPEXCEL.PHPExcel");
        /**
         * 总页数集合
         */
        /**
         * xls、xlsx格式都可导入
         */

        $objPHPRead = \PHPExcel_IOFactory::createReaderForFile($file_path);
        /**
         * 忽略单元格格式
         */
        $objPHPRead->setReadDataOnly(true);
        /**
         * 实例化PHPExcel对象
         */
        $objPHPExcel = $objPHPRead->load($file_path);
        /**
         * 获取当前工作表
         */
        $objWorksheet = $objPHPExcel->getActiveSheet();
        /**
         * 获取行数
         */
        $highestRow = $objWorksheet->getHighestRow();
        /**
         * 获取最后一列的列数
         */
        $highestColumn = $objWorksheet->getHighestColumn();
        /**
         * 获取最后一列对应的列数 数字
         */
        $highestColumnIndex = \PHPExcel_Cell::columnIndexFromString($highestColumn);
        
        $excelData = array();
        for ($row = 2; $row <= $highestRow; $row ++) {
            for ($col = 0; $col < $highestColumnIndex; $col ++) {
                if ($config[$col]['callback']) {
                    $excelData[$row][$config[$col]['name']] = $config[$col]['callback']((string) $objWorksheet->getCellByColumnAndRow($col, $row)->getValue());
                } else {
                	// dump($config[$col]);
                    $excelData[$row][$config[$col]['name']] = (string) $objWorksheet->getCellByColumnAndRow($col, $row)->getValue();
                }
            }
        }
        if (empty($excelData) || count($excelData['2']) !== count($config)) {
            return false;
        }
        return array_values($excelData);
    }

    /**
     * 导入EXCEL -- 兼容  xls  xlsx
     *
     * @param $[config] [格式：$config
     *            = array(
     *            ['昵称','nickname','15'],
     *            ['联系方式','mobile','20'],
     *            );
     * @param $file_path 文件路径
     * @param $method [<回调处理函数>]
     *            @time 2016-10-10
     * @author 陶君行<Silentlytao@outlook.com>
     */
    public function im_excel($config, $file_path = '')
    {

        /**
         * 引入PHPEXCEL类
         */
        vendor("PHPEXCEL.PHPExcel");
        /**
         * 总页数集合
         */
        /**
         * xls、xlsx格式都可导入
         */
        //使用 PHPExcel_IOFactory 来鉴别文件应该使用哪一个读取类
        $inputFileType = \PHPExcel_IOFactory::identify($file_path);
        $objPHPRead = \PHPExcel_IOFactory::createReader($inputFileType);

        /**
         * 忽略单元格格式
         */
        $objPHPRead->setReadDataOnly(true);
        /**
         * 实例化PHPExcel对象
         */
        $objPHPExcel = $objPHPRead->load($file_path);
        /**
         * 获取当前工作表
         */
        $objWorksheet = $objPHPExcel->getActiveSheet();
        /**
         * 获取行数
         */
        $highestRow = $objWorksheet->getHighestRow();
        /**
         * 获取最后一列的列数
         */
        $highestColumn = $objWorksheet->getHighestColumn();
        /**
         * 获取最后一列对应的列数 数字
         */
        $highestColumnIndex = \PHPExcel_Cell::columnIndexFromString($highestColumn);

        $excelData = array();
        for ($row = 2; $row <= $highestRow; $row ++) {
            for ($col = 0; $col < $highestColumnIndex; $col ++) {
                if ($config[$col]['callback']) {
                    $excelData[$row][$config[$col]['name']] = $config[$col]['callback']((string) $objWorksheet->getCellByColumnAndRow($col, $row)->getValue());
                } else {
                    // dump($config[$col]);
                    $excelData[$row][$config[$col]['name']] = (string) $objWorksheet->getCellByColumnAndRow($col, $row)->getValue();
                }
            }
        }
        if (empty($excelData) || count($excelData['2']) !== count($config)) {
            return false;
        }
        return array_values($excelData);
    }
}
