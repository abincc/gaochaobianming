<?php
namespace Common\Model;
use Think\Model\ViewModel;

/**
 * 商品收藏视图模型
 */
class ProductCollectionViewModel extends ViewModel{
    public $viewFields = array(
        'product_collection'=>array(
            '*',
            '_as'=>'c',
            '_type'=>'LEFT'
        ), 
        'product'=>array(
            'title',
            'description',
            'price',
            'image',
            '_as'=>'p',
            '_on'=>'p.id=c.product_id',
        )
    ); 
}