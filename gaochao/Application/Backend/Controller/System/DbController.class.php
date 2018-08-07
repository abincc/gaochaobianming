<?php
	/**
	 * 数据库控制器
	 * @package 
	 * @author 王淳熙 
	 */
namespace Backend\Controller\System;
	/**
	 * 数据库控制器
	 * @package 
	 * @author 王淳熙 
	 */
class DbController extends IndexController{
	/**
	 *功能：查询数据库下的所有表
	 *@author 王淳熙
	 */
	public function index(){
		/*获取数据库的配置文件*/
		$db = C('DB_NAME');
		$map = array(
			'table_schema'=> $db
		);
		$keyword = I('get.keyword', '', '/\S+/');
		if(!empty($keyword)) {
			$map['table_name|table_comment'] = array('like', trim($keyword));
		}
		$sql = $this->generate_sql('INFORMATION_schema.tables', 'table_name,table_comment', $map, '', true);
		$count_sql = $this->generate_sql('INFORMATION_schema.tables', 'count(table_name) as tp_count', $map);
		$data = $this->page_sql($sql, $count_sql);
		/*传递文件到前端*/
		$this->assign($data);
		/*加载页面*/
		$this->display();
	}
	/**
	 *功能：查询指定表下所有字段加内容，带分页
	 *		点击各字段会进行不同的排序
	 *		带搜索可以根据关键字模糊搜索
	 *@author 王淳熙
	 */
	public  function structure(){
		$field = 'column_name,column_type,is_nullable,column_default,extra,column_comment';
		$data = $this->table_columns($field);
		$this->assign($data);
		$this ->display();
	}
	/**
	 *功能：查询指定表下指定字段的内容，带分页
	 *		点击各字段会进行不同的排序
	 *		带搜索可以根据关键字模糊搜索
	 *@author 王淳熙
	 */
	public function data_list() {
		$map = array();
		$data = $this->table_columns();
		$fields = array_column($data['columns'], 'column_name');
		$field = I('get.field', null);
		if(is_string($field)) {
			$field = explode(',', $field);
		}
		$data['field'] = $field = array_intersect($fields, $field);
		$keyword = I('get.keyword', false, '/\S+/');
		if($field && $keyword) {
			$field = implode('|', $field);
			$map[$field] = array('LIKE', '%'.$keyword.'%');
		}
		/*调用分页函数*/
		$this->page($data['table_name'],$map);
		$this->assign($data);
		/*加载页面*/
		$this->display();
	}
	
	/**
	 *功能：各字段进行不同的排序
	 *		带搜索可以根据关键字模糊搜索
	 *@author 王淳熙
	 */
	public function sorting(){
		/*判断是否是GET请求提交*/
		if(IS_GET){
			/*获取前台函数*/
			$table_name=I('get.table_name');
			$type=I('get.type');
			$sorting =I('get.sorting');
			/*判断前台排序是升序还是降序*/
			if($sorting == "asce"){
				$sorting ="desc";
				$result = $this->page("$table_name", array(),$order="$type $sorting");
			}else{
				$sorting ="asce";
				$result = $this->page("$table_name", array());
			}
			/*传递数据到前台*/
			$data['table_name']=$table_name;
			$data['sorting']= $sorting;
			$this->assign($data);
		}
		/*加载页面*/
		$this ->display();
	}
	/**
	 *功能：对数据库进行指定搜索
	 *@author 王淳熙
	 */
	public function search(){
		if(IS_GET){
			/*接收前台传递的信息*/
			$table_name=I("table_name");
			$ckes=I('ckes');
			$keywords=I('keywords');
			/*组合成搜索条件*/
			$map[$ckes]=array("like","$keywords%");
			/*调用分页函数*/
			$this->page("$table_name",$map);
			/*传递数据到前台*/
			$data['table_name']=$table_name;
			$data['keywords']=$keywords;
			$data['ckes']=explode('|',$ckes);
			$this->assign($data);
		}
		/*加载视图*/
		$this ->display();
	}
	/**
	 *功能：数据分页显示输出
	 *@author 王淳熙
	 *@param $sql sql语句
	 *@param $count_sql 
	 *@param $limit 
	 */
	protected function page_sql($sql, $count_sql, $limit = 15) {
		$Model = M();
		$data['list'] = $Model->query($sql);
		$count = $Model->query($count_sql);
		$data['count'] = $count[0]['tp_count'];// 查询满足要求的总记录数
		$Page = new \Think\Page($data['count'], $limit);// 实例化分页类 传入总记录数和每页显示的记录数
		$Page->setConfig('prev', "上一页");//上一页
		$Page->setConfig('next', '下一页');//下一页
		$Page->setConfig('first', '首页');//第一页
		$Page->setConfig('last', '尾页');//最后一页
		$Page->lastSuffix = false;
		$data['page'] = $Page->show();// 分页显示输出
		
		return $data;
	}
	/**
	 *功能：生成SQL语句
	 *@param $table   
	 *@param $field   
	 *@param $where   
	 *@param $order   
	 *@param $limit   
	 *@author 王淳熙
	 */
	protected function generate_sql($table, $field = '*', $where = '', $order = false, $limit = false) {
		$search = array('%FIELD%', '%TABLE%', '%WHERE%', '%ORDER%', '%LIMIT%');
		$sql = 'Select %FIELD% from %TABLE%%WHERE%%ORDER%%LIMIT%';
		$where = self::where($where);
		if($limit !== false) {
			$limit = self::limit();
		}
		$replace = array($field, $table, $where, $order, $limit);
		$sql = str_replace($search, $replace, $sql);
		
		return $sql;
	}
	/**
	 *功能：生成where语句
	 *@param $where 
	 *@author 王淳熙
	 */
	protected function where($where) {
		$string = '';
		if(is_string($where)) {
			$string .= ' where '.$where;
		}elseif(is_array($where)) {
			$string .= ' where ';
			foreach($where as $key=> $value) {
				if(strpos($key, '|')) {
					$field = explode('|', $key);
					foreach($field as $v) {
						$arr[] = self::_where($v, $value);
					}
					$str = '('.implode(' or ', $arr).')';
				}else{
					$str = self::_where($key, $value);
				}
				$array[] = $str;
			}
			$_logic = isset($where['_logic']) ? $where['_logic'] : 'and';
			$string .= implode(' '.$_logic.' ', $array);
		}
		
		return $string;
	}
	/**
	 *功能：where语句赋值
	 *@param $key 
	 *@param $value 
	 *@author 王淳熙
	 */
	protected function _where($key, $value) {
		if(is_array($value)) {
			$value[1] = addslashes($value[1]);
			switch($value[0]) {
				case 'like':
					$str = $key.' like '."'%".$value[1]."%'";
					break;
				default:
					$str = $key.$value[0].$value[1];
					break;
			}
		}elseif(is_string($value)) {
			$value = addslashes($value);
			$str = $key.'='."'{$value}'";
		}
		
		return $str;
	}
	/**
	 *功能：生成limit语句
	 *@param $limit
	 *@author 王淳熙
	 */
	protected function limit($limit = 10) {
		$limit = isset($_REQUEST['r']) ? $_REQUEST['r'] : $limit;
		$page = isset($_GET['p']) ? intval($_GET['p']) : 1;
		$string = ' limit '.(($page-1) * $limit).','.$limit;
		return $string;
	}
	/**
	 *功能：查询指定表里的所有字段的属性
	 *@param $field
	 *@author 王淳熙
	 */
	protected function table_columns($field = 'column_name') {
		$table_name = I('ids', null, '/^[a-zA-Z_]+$/');
		empty($table_name) ? $this->error('参数错误') : false;
		$Model = M();
		$db = C('DB_NAME');
		$data['columns'] = $Model->query("select $field from information_schema.columns where table_schema='$db' and table_name='$table_name'");
		!$data['columns'] ? $this->error('参数错误') : false;
		/*调用mysql查询语句 查询指定表里的所有字段的属性*/
		$db_prefix = C('DB_PREFIX');
		$data['table_name'] = preg_replace('/'.$db_prefix.'/', '', $table_name, 1);
		
		return $data;
	}


	/**
	 * 生成mysql数据字典供打印文档
	 */
	public function print_table(){

		error_reporting(0);
		header("Content-type: text/html; charset=utf-8");
		//配置数据库
		$dbserver   = C('DB_HOST');
		$dbusername = C('DB_USER');
		$dbpassword = C('DB_PWD');
		$database   = C('DB_NAME');
		if(!empty(C('DB_PORT'))){
			$dbserver .=':'.C('DB_PORT');
		}else{
			$dbserver .=':3306';
		}
		 
		//其他配置
		$mysql_conn = @mysql_connect("$dbserver", "$dbusername", "$dbpassword") or die("Mysql connect is error.");
		mysql_select_db($database, $mysql_conn);
		mysql_query('SET NAMES utf8', $mysql_conn);
		$table_result = mysql_query('show tables', $mysql_conn);
		 
		$no_show_table = array();    //不需要显示的表
		$no_show_field = array();    //不需要显示的字段
		 
		//取得所有的表名
		while($row = mysql_fetch_array($table_result)){
		    if(!in_array($row[0],$no_show_table)){
		        $tables[]['TABLE_NAME'] = $row[0];
		    }
		}


		// 'DB_TYPE' => 'mysqli', // 数据库类型
		// 'DB_NAME' => 'baseframe', // 数据库名
		// 'DB_PORT' => 7546, // 端口
		// 'DB_PREFIX' => 'sr_', // 数据库表前缀
		// 'DB_CHARSET' => 'utf8', // 字符集
		//                        // 'DB_DEBUG' => TRUE, // 数据库调试模式 开启后可以记录SQL日志 3.2.3新增

		// 'DB_HOST' => 'test.cnsunrun.com', 	// 服务器地址
		// 'DB_USER' => 'baseframe', 			// 用户名
		// 'DB_PWD' => 'vJ3LcWZcFQNeSv8U', 		// 密码

		//替换所以表的表前缀
		// if($_GET['prefix']){
		//     $prefix = 'sr_';
		//     foreach($tables as $key => $val){
		//         $tableName = $val['TABLE_NAME'];
		//         $string = explode('_',$tableName);
		//         if($string[0] != $prefix){  
		//             $string[0] = $prefix;  
		//             $newTableName = implode('_', $string);  
		//             mysql_query('rename table '.$tableName.' TO '.$newTableName);  
		//         }
		//     }
		//     echo "替换成功！";exit();
		// }
		 
		//循环取得所有表的备注及表中列消息
		foreach ($tables as $k=>$v) {
		    $sql  = 'SELECT * FROM ';
		    $sql .= 'INFORMATION_SCHEMA.TABLES ';
		    $sql .= 'WHERE ';
		    $sql .= "table_name = '{$v['TABLE_NAME']}'  AND table_schema = '{$database}'";
		    $table_result = mysql_query($sql, $mysql_conn);
		    while ($t = mysql_fetch_array($table_result) ) {
		        $tables[$k]['TABLE_COMMENT'] = $t['TABLE_COMMENT'];
		    }
		 
		    $sql  = 'SELECT * FROM ';
		    $sql .= 'INFORMATION_SCHEMA.COLUMNS ';
		    $sql .= 'WHERE ';
		    $sql .= "table_name = '{$v['TABLE_NAME']}' AND table_schema = '{$database}'";
		 
		    $fields = array();
		    $field_result = mysql_query($sql, $mysql_conn);
		    while ($t = mysql_fetch_array($field_result) ) {
		        $fields[] = $t;
		    }
		    $tables[$k]['COLUMN'] = $fields;
		}
		mysql_close($mysql_conn);

		$html = '<div class="wrapper"><section class="content">';
		//循环所有表
		foreach ($tables as $k=>$v) {
		    $html .= '  <h3>' . ($k + 1) . '、' . $v['TABLE_COMMENT'] .'  （'. $v['TABLE_NAME']. '）</h3>'."\n";
		    $html .= '  <table width="90%"  class="table js_table table-bordered">'."\n";
		    $html .= '      <tbody>'."\n";
		    $html .= '          <tr>'."\n";
		    $html .= '              <th width="10%">字段名</th>'."\n";
		    $html .= '              <th width="30%">数据类型</th>'."\n";
		    $html .= '              <th width="10%">默认值</th>'."\n";
		    $html .= '              <th width="10%">允许非空</th>'."\n";
		    $html .= '              <th width="10%">自动递增</th>'."\n";
		    $html .= '              <th width="20%">备注</th>'."\n";
		    $html .= '          </tr>'."\n";
		 
		    foreach ($v['COLUMN'] as $f) {
		        if(!is_array($no_show_field[$v['TABLE_NAME']])){
		            $no_show_field[$v['TABLE_NAME']] = array();
		        }
		        if(!in_array($f['COLUMN_NAME'],$no_show_field[$v['TABLE_NAME']])){
		            $html .= '          <tr>'."\n";
		            $html .= '              <td class="c1">' . $f['COLUMN_NAME'] . '</td>'."\n";
		            $html .= '              <td class="c2">' . $f['COLUMN_TYPE'] . '</td>'."\n";
		            $html .= '              <td class="c3">' . $f['COLUMN_DEFAULT'] . '</td>'."\n";
		            $html .= '              <td class="c4">' . $f['IS_NULLABLE'] . '</td>'."\n";
		            $html .= '              <td class="c5">' . ($f['EXTRA']=='auto_increment'?'是':'&nbsp;') . '</td>'."\n";
		            $html .= '              <td class="c6">' . $f['COLUMN_COMMENT'] . '</td>'."\n";
		            $html .= '          </tr>'."\n";
		        }
		    }
		    $html .= '      </tbody>'."\n";
		    $html .= '  </table>'."\n";
		}
		$html .= '</div></div>';

		$this->assign('html',$html);
		$this->display();
	}
}
