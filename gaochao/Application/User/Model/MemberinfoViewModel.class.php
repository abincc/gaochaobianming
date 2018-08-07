<?php 
namespace User\Model;
use Think\Model\ViewModel;
/**
 * 根据ID获取会员ID获取店铺
 */
class MemberinfoViewModel extends ViewModel{
		public $viewFields = array(
			'member'=> array(
				'*',
				'_as'=>'a',
				'id'=>'m_id',
				'_type' => 'left' ,
			),
		
			'member_info'=> array(
				'_as'=>'b',
				'balance',
				'realname',
				'birthday',
				'regtime',
				'regip',
				'descriptionso',
				'gender',
				'integral',
				'sign_time',
				'_on' => 'a.id=b.member_id',
			),
			
		);
}	