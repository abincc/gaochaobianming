<?php
namespace Backend\Model;
use Think\Model\ViewModel;

/**
 * 库存视图模型
 */
class SkuViewModel extends ViewModel{
    public $viewFields = array(
        'product_sku'=>array(
            'id',
            'product_id',
            'price',
            'inventory',
            'image',
            'spec_value',
            'total_sales',
            '_as'   =>'s',
            '_type' =>'LEFT',
        ),
        'product'=>array(
            'title',
            'spec',
            'sub_category_id',
            'category_id',
            '_as'   =>'p',
            '_on'   =>'p.id=s.product_id',
        )
    ); 
}