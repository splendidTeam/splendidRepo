package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.servlet.http.CookieUtil;

public abstract class AbstractShoppingcartResolver implements ShoppingcartResolver{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractShoppingcartResolver.class);

    @Autowired
    private SdkSkuManager       sdkSkuManager;

    @Autowired
    private SdkItemManager      sdkItemManager;

    @Autowired
    private SdkEngineManager    sdkEngineManager;

    /**
     * merge购物车之后做的事情.通常
     * {@link #addShoppingCart(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model)}
     * ,
     * {@link #updateShoppingCartCount(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model)}
     * ,
     * {@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse, Model)}
     * 都需要调用他
     *
     * @since 5.3.1
     */
    private void afterMergeShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    HttpServletRequest request,
                    HttpServletResponse response){
        // 将购物车数量塞到Cookie 里面去
        int totalCount = Validator.isNullOrEmpty(shoppingCartLineCommandList) ? 0
                        : CollectionsUtil.sum(shoppingCartLineCommandList, "quantity").intValue();
        CookieUtil.addCookie(CookieKeyConstants.SHOPPING_CART_COUNT, "" + totalCount, response);
    }

    @Override
    public ShoppingcartResult deleteShoppingCartLine(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    HttpServletRequest request,
                    HttpServletResponse response){

        // 获取购物车行信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        // 主賣品(剔除 促銷行 贈品) 剔除之后 下次load会补全最新促销信息
        List<ShoppingCartLineCommand> mainlines = CollectionsUtil.select(shoppingCartLineCommandList, new MainLinesPredicate());
        // 找不到 就抛
        ShoppingCartLineCommand shoppingCartLineCommand = CollectionsUtil.find(mainlines, "id", shoppingcartLineId);
        if (Validator.isNullOrEmpty(shoppingCartLineCommand)){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        ShoppingcartResult deleteShoppingCartShoppingcartResult = doDeleteShoppingCartLine(
                        memberDetails,
                        shoppingCartLineCommandList,
                        shoppingCartLineCommand,
                        request,
                        response);

        if (null != deleteShoppingCartShoppingcartResult){
            return deleteShoppingCartShoppingcartResult;
        }

        List<ShoppingCartLineCommand> shoppingCartLineCommandListAfterDelete = CollectionsUtil
                        .remove(shoppingCartLineCommandList, shoppingCartLineCommand);

        afterMergeShoppingCart(memberDetails, shoppingCartLineCommandListAfterDelete, request, response);

        return ShoppingcartResult.SUCCESS;
    }

    @Override
    public ShoppingcartResult selectShoppingCartLine(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    Integer settlementState,
                    HttpServletRequest request,
                    HttpServletResponse response){

        // 是否是全选操作
        boolean allCheckFlag = Validator.isNullOrEmpty(shoppingcartLineId);

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        ;
        // 主賣品(剔除 促銷行 贈品)
        List<ShoppingCartLineCommand> mainlines = CollectionsUtil.select(shoppingCartLineCommandList, new MainLinesPredicate());
        // 找不到 就抛
        if (Validator.isNullOrEmpty(mainlines)){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        // 找到实际需要操作的行
        List<ShoppingCartLineCommand> needChangeCheckedCommandList = mainlines;
        if (!allCheckFlag){
            // 非全选就找到当前操作的购物车行 找不到 就抛
            needChangeCheckedCommandList = CollectionsUtil.select(mainlines, "id", shoppingcartLineId);
            if (Validator.isNullOrEmpty(needChangeCheckedCommandList)){
                return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
            }
        }

        List<String> extentionCodeList = new ArrayList<String>();
        for (ShoppingCartLineCommand needChangeCheckedCommand : needChangeCheckedCommandList){
            // 跳过已经是该状态的购物车行
            if (settlementState.toString().equals(needChangeCheckedCommand.getSettlementState() + "")){
                continue;
            }

            // ********* 商品验证***********
            Long skuId = needChangeCheckedCommand.getSkuId();
            Sku sku = sdkSkuManager.findSkuById(skuId);
            ShoppingcartResult commandValidateShoppingcartResult = doCommandValidate(sku, needChangeCheckedCommand.getQuantity());

            if (null != commandValidateShoppingcartResult){
                return commandValidateShoppingcartResult;
            }

            // ********* 库存验证***********
            SkuInventory inventory = sdkItemManager.getSkuInventoryByExtentionCode(sku.getOutid());
            Integer stock = CollectionsUtil
                            .sum(CollectionsUtil.select(shoppingCartLineCommandList, "extentionCode", sku.getOutid()), "quantity")
                            .intValue();

            // 如果超过库存量
            if (stock > inventory.getAvailableQty()){
                return ShoppingcartResult.MAX_THAN_INVENTORY;
            }

            extentionCodeList.add(needChangeCheckedCommand.getExtentionCode());
            needChangeCheckedCommand.setSettlementState(settlementState);

        }

        if (Validator.isNullOrEmpty(extentionCodeList)){
            return ShoppingcartResult.SUCCESS;
        }

        // ********* 改变选中状态***********
        ShoppingcartResult checkShoppingCartShoppingcartResult = doSelectShoppingCartLine(
                        memberDetails,
                        settlementState,
                        extentionCodeList,
                        needChangeCheckedCommandList,
                        request,
                        response);

        if (null != checkShoppingCartShoppingcartResult){
            return checkShoppingCartShoppingcartResult;
        }

        return ShoppingcartResult.SUCCESS;
    }

    @Override
    public ShoppingcartResult updateShoppingCart(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response){

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        ;

        // 找不到 就抛
        ShoppingCartLineCommand shoppingCartLineCommand = CollectionsUtil.find(shoppingCartLineCommandList, "id", shoppingcartLineId);

        if (null == shoppingCartLineCommand){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        Long skuId = shoppingCartLineCommand.getSkuId();
        Sku sku = sdkSkuManager.findSkuById(skuId);
        ShoppingcartResult commandValidateShoppingcartResult = doCommandValidate(sku, count);

        if (null != commandValidateShoppingcartResult){
            return commandValidateShoppingcartResult;
        }

        // ****************************************************************************************

        // 验证库存
        shoppingCartLineCommand.setQuantity(count);

        SkuInventory inventory = sdkItemManager.getSkuInventoryByExtentionCode(sku.getOutid());
        Integer stock = CollectionsUtil
                        .sum(CollectionsUtil.select(shoppingCartLineCommandList, "extentionCode", sku.getOutid()), "quantity").intValue();

        // 如果超过库存量
        if (stock > inventory.getAvailableQty()){
            return ShoppingcartResult.MAX_THAN_INVENTORY;
        }

        // FIXME
        ShoppingCartLineCommand currentLine = CollectionsUtil.find(
                        CollectionsUtil.select(shoppingCartLineCommandList, new MainLinesPredicate()),
                        "extentionCode",
                        sku.getOutid());

        // 封装购物车行数据
        sdkEngineManager.packShoppingCartLine(currentLine);

        ShoppingcartResult updateShoppingCartShoppingcartResult = doUpdateShoppingCart(
                        memberDetails,
                        shoppingCartLineCommandList,
                        currentLine,
                        request,
                        response);

        if (null != updateShoppingCartShoppingcartResult){
            return updateShoppingCartShoppingcartResult;
        }

        afterMergeShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);

        return ShoppingcartResult.SUCCESS;
    }

    protected abstract ShoppingcartResult doUpdateShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response);

    protected abstract ShoppingcartResult doDeleteShoppingCartLine(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response);

    protected abstract ShoppingcartResult doSelectShoppingCartLine(
                    MemberDetails memberDetails,
                    Integer settlementState,
                    List<String> extentionCodeList,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    HttpServletRequest request,
                    HttpServletResponse response);

    @Override
    public ShoppingcartResult addShoppingCart(
                    MemberDetails memberDetails,
                    Long skuId,
                    Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response){

        Sku sku = sdkSkuManager.findSkuById(skuId);
        ShoppingcartResult commandValidateShoppingcartResult = doCommandValidate(sku, count);

        if (null != commandValidateShoppingcartResult){
            return commandValidateShoppingcartResult;
        }

        // ****************************************************************************************

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        if (null == shoppingCartLineCommandList){
            shoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();
        }
        // 主賣品(剔除 促銷行 贈品)
        List<ShoppingCartLineCommand> mainLines = CollectionsUtil.select(shoppingCartLineCommandList, new MainLinesPredicate());

        // 购物车里面相同的 extentionCode 从main 里面找
        ShoppingCartLineCommand sameExtentionCodeInCartShoppingCartLineCommand = CollectionsUtil
                        .find(mainLines, "extentionCode", sku.getOutid());

        boolean isExist = null != sameExtentionCodeInCartShoppingCartLineCommand;
        if (!isExist){
            // 最大行数验证
            // 校验是否超过购物车规定的商品行数
            if ((mainLines.size() + 1) > Constants.SHOPPING_CART_SKU_MAX_COUNT){
                return ShoppingcartResult.MAIN_LINE_MAX_THAN_COUNT;
            }

            // 构造一条 塞进去
            ShoppingCartLineCommand shoppingCartLineCommand = buildShoppingCartLineCommand(sku.getId(), count, sku.getOutid());
            shoppingCartLineCommandList.add(shoppingCartLineCommand);
        }else{// 存在

            // FIXME 限购验证
            sameExtentionCodeInCartShoppingCartLineCommand
                            .setQuantity(sameExtentionCodeInCartShoppingCartLineCommand.getQuantity() + count);
        }

        // 验证库存
        SkuInventory inventory = sdkItemManager.getSkuInventoryByExtentionCode(sku.getOutid());
        Integer stock = CollectionsUtil
                        .sum(CollectionsUtil.select(shoppingCartLineCommandList, "extentionCode", sku.getOutid()), "quantity").intValue();

        // 如果超过库存量
        if (stock > inventory.getAvailableQty()){
            return ShoppingcartResult.MAX_THAN_INVENTORY;
        }

        // FIXME
        ShoppingCartLineCommand currentLine = CollectionsUtil.find(
                        CollectionsUtil.select(shoppingCartLineCommandList, new MainLinesPredicate()),
                        "extentionCode",
                        sku.getOutid());

        // 封装购物车行数据
        sdkEngineManager.packShoppingCartLine(currentLine);

        ShoppingcartResult addShoppingCartShoppingcartResult = doAddShoppingCart(
                        memberDetails,
                        shoppingCartLineCommandList,
                        currentLine,
                        request,
                        response);

        if (null != addShoppingCartShoppingcartResult){
            return addShoppingCartShoppingcartResult;
        }
        afterMergeShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);
        return ShoppingcartResult.SUCCESS;
    }

    protected abstract ShoppingcartResult doAddShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 公共的校验
     * 
     * @param skuId
     * @param count
     * @return 如果检验没有错 返回null
     */
    private ShoppingcartResult doCommandValidate(Sku sku,Integer count){
        // ******************************validate**********************************************************

        // 数量不能小于1
        Validate.isTrue(count >= 1, "count:%s can not <1", count);

        // 判断sku是否存在
        if (Validator.isNullOrEmpty(sku)){
            return ShoppingcartResult.SKU_NOT_EXIST;
        }

        // ********************************************************************************************

        // TODO
        // 判断sku生命周期
        if (!sku.getLifecycle().equals(Sku.LIFE_CYCLE_ENABLE)){
            return ShoppingcartResult.SKU_NOT_ENABLE;
        }

        // ********************************************************************************************
        ItemCommand item = sdkItemManager.findItemCommandById(sku.getItemId());
        // item生命周期验证
        Integer lifecycle = item.getLifecycle();

        if (!com.baozun.nebula.sdk.constants.Constants.ITEM_ADDED_VALID_STATUS.equals(String.valueOf(lifecycle))){
            // 还没上架
            LOGGER.error("item id:{}, status is :{} can not operate in shoppingcart", item.getId(), lifecycle);
            return ShoppingcartResult.ITEM_STATUS_NOT_ENABLE;
        }

        // ********************************************************************************************
        if (!checkActiveBeginTime(item)){
            // 还没上架
            // TODO log
            return ShoppingcartResult.ITEM_NOT_ACTIVE_TIME;
        }

        // 赠品验证
        if (ItemInfo.TYPE_GIFT.equals(item.getType())){
            return ShoppingcartResult.ITEM_IS_GIFT;
        }
        return null;
    }

    /**
     * 
     * @author 何波 @Description: 检查商品是否上架 @param skuId @return Boolean @throws
     */
    private Boolean checkActiveBeginTime(ItemCommand item){
        Date activeBeginTime = item.getActiveBeginTime();
        return null == activeBeginTime ? true : activeBeginTime.before(new Date());
    }

    /**
     * 转换为ShoppingCartLineCommand对象
     * 
     * @param extensionCode
     * @param quantity
     * @param skuId
     * @return
     */
    private ShoppingCartLineCommand buildShoppingCartLineCommand(Long skuId,Integer quantity,String extensionCode){
        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
        shoppingCartLineCommand.setExtentionCode(extensionCode);
        shoppingCartLineCommand.setQuantity(quantity);
        shoppingCartLineCommand.setSkuId(skuId);
        shoppingCartLineCommand.setCreateTime(new Date());
        shoppingCartLineCommand.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
        return shoppingCartLineCommand;
    }

    // /**
    // * 游客的memboIds
    // *
    // * @return
    // */
    // private Set<String> getMemboIds() {
    // return sdkEngineManager.getCrowdScopeListByMemberAndGroup(null, null);
    // }

}
