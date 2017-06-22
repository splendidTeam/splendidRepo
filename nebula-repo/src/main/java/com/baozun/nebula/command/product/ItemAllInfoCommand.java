package com.baozun.nebula.command.product;

import java.util.List;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;

public class ItemAllInfoCommand extends ItemCommand{

    private static final long serialVersionUID = 1L;

    /** 动态属性 */
    private List<ItemProperties> itemProperties;

    /** sku信息， */
    private List<Sku> sku;

    /** sku库存 */
    private List<SkuInventory> SkuInventory;

    /** 商品图片 */
    private List<ItemImage> itemImage;

    /** 商品分类 */
    private List<ItemCategory> itemCategory;

    public List<ItemProperties> getItemProperties(){
        return itemProperties;
    }

    public void setItemProperties(List<ItemProperties> itemProperties){
        this.itemProperties = itemProperties;
    }

    public List<Sku> getSku(){
        return sku;
    }

    public void setSku(List<Sku> sku){
        this.sku = sku;
    }

    public List<SkuInventory> getSkuInventory(){
        return SkuInventory;
    }

    public void setSkuInventory(List<SkuInventory> skuInventory){
        SkuInventory = skuInventory;
    }

    public List<ItemImage> getItemImage(){
        return itemImage;
    }

    public void setItemImage(List<ItemImage> itemImage){
        this.itemImage = itemImage;
    }

    public List<ItemCategory> getItemCategory(){
        return itemCategory;
    }

    public void setItemCategory(List<ItemCategory> itemCategory){
        this.itemCategory = itemCategory;
    }

}
