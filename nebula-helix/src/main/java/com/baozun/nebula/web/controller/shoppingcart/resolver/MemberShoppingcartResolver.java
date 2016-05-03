package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.web.MemberDetails;

/**
 * 会员购物车操作
 * 
 * @author weihui.tang
 *
 */
@Component("memberShoppingcartResolver")
public class MemberShoppingcartResolver extends AbstractShoppingcartResolver{

    @Autowired
    private SdkShoppingCartManager sdkShoppingCartManager;

    @Override
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request){
        return sdkShoppingCartManager.findShoppingCartLinesByMemberId(memberDetails.getMemberId(), null);
    }

    @Override
    protected ShoppingcartResult doAddShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        currentLine.setMemberId(memberDetails.getMemberId());

        boolean result = sdkShoppingCartManager.merageShoppingCartLineById(memberDetails.getMemberId(), currentLine);
        if (!result){
            return ShoppingcartResult.OPERATE_ERROR;
        }
        return null;
    }

    @Override
    protected ShoppingcartResult doUpdateShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        currentLine.setMemberId(memberDetails.getMemberId());

        boolean result = sdkShoppingCartManager.merageShoppingCartLineById(memberDetails.getMemberId(), currentLine);
        if (!result){
            return ShoppingcartResult.OPERATE_ERROR;
        }
        return null;
    }

    @Override
    protected ShoppingcartResult doDeleteShoppingCartLine(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        Integer result = sdkShoppingCartManager.removeShoppingCartLineById(memberDetails.getMemberId(), currentLine.getId());
        if (com.baozun.nebula.sdk.constants.Constants.FAILURE.equals(result)){
            return ShoppingcartResult.OPERATE_ERROR;
        }
        return null;

    }

    @Override
    protected ShoppingcartResult doToggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    List<String> extentionCodeList,
                    List<ShoppingCartLineCommand> needChangeCheckedCommandList,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response){
        sdkShoppingCartManager.updateCartLineSettlementState(memberDetails.getMemberId(), extentionCodeList, checkStatus ? 1 : 0);
        return null;
    }
}
