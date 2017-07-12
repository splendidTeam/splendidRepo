package com.baozun.nebula.web.controller.returnapplication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.manager.returnapplication.ReturnApplicationManager;
import com.baozun.nebula.manager.returnapplication.ReturnApplicationLineManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationLineManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;
import com.baozun.nebula.web.controller.returnapplication.resolver.ReturnApplicationResolver;
import com.baozun.nebula.web.controller.returnapplication.validator.ReturnApplicationValidator;
import com.baozun.nebula.web.controller.returnapplication.viewcommand.ReturnApplicationViewCommand;
import com.baozun.nebula.web.controller.returnapplication.viewcommand.ReturnLineViewCommand;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 退货基类控制器
 * <ol>
 * <li>
 * {@link #returnCommit(MemberDetails memberDetails, BindingResult bindingResult, HttpServletRequest request, ReturnOderForm returnOrderForm, Model model)} 退货提交</li>
 * 
 * </ol>
 * 
 * <ol>
 * <li>{@link #turnToReturn(String isTrack, Long orderId, Model model)} 去处理退货</li>
 * </ol>
 * 
 * 
 * <ol>
 * <li>{@link #showReturnDetail(String isTrack, String orderId, Model model)} 退货详情</li>
 * </ol>
 * 
 * 
 * 
 */
public class NebulaOrderReturnController extends BaseController{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderReturnController.class);

    /**
     * <br/>
     * 退货Form的校验器
     */
    @Autowired
    @Qualifier("returnApplicationNormalValidator")
    private ReturnApplicationValidator returnApplicationNormalValidator;

    @Autowired
    private ReturnApplicationResolver returnApplicationResolver;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private SdkReturnApplicationLineManager soReturnLineManager;

    @Autowired
    private SdkOrderLineDao sdkOrderLineDao;

    @Autowired
    private ReturnApplicationManager returnApplicationManager;
    
    @Autowired
    private SdkReturnApplicationManager sdkReturnApplicationManager;

    @Autowired
    private ReturnApplicationLineManager returnLineManager;

    /**
     * 
     * @param memberDetails
     * @param bindingResult
     * @param request
     * @param returnOrderForm
     * @param model
     * @ResponseBody
     * @RequestMapping(value = "/myAccountOrder/commitReturn", method =
     *                       RequestMethod.GET)
     * @return
     */
    public NebulaReturnResult commitReturn(
    // 方法名 ：动词+
                    @LoginMember MemberDetails memberDetails,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    @ModelAttribute(value = "returnOrderForm") ReturnOrderForm returnOrderForm,
                    Model model){
        Validate.notNull(memberDetails, "memberDetails can't be null!");
        // 判断是否为本人进行操作
        SalesOrderCommand salesOrderCommand = orderManager.findOrderByCode(returnOrderForm.getOrderCode(), null);
        boolean isSelfOrder = isSelfOrder(memberDetails, salesOrderCommand);
        Validate.isTrue(isSelfOrder, "order:%s,is not his self:[%s] order", returnOrderForm.getOrderCode(), JsonUtil.format(memberDetails, 0, 0));

        DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
        returnApplicationNormalValidator.validate(returnOrderForm, bindingResult);

        // 如果校验失败，返回错误
        if (bindingResult.hasErrors()){
            LOGGER.error("[returnOrder] {} [{}] returnOrderForm validation error. \"\"");
            return getResultFromBindingResult(bindingResult);
        }

        defaultReturnResult = (DefaultReturnResult) getResultFromBindingResult(bindingResult);

        if (!defaultReturnResult.isResult()){
            defaultReturnResult.setStatusCode("return.validator.errors");
            return defaultReturnResult;
        }
        SalesOrderCommand salesOrder = orderManager.findOrderById(Long.parseLong(returnOrderForm.getOrderId()), null);

        // 如果是换货，需要处理换货物流对象
        ReturnApplicationCommand returnCommand = returnApplicationResolver.toReturnApplicationCommand(memberDetails, returnOrderForm, salesOrder);

        returnCommand = sdkReturnApplicationManager.createReturnApplication(returnCommand, salesOrder);
        defaultReturnResult.setReturnObject(returnCommand);
        if (Validator.isNullOrEmpty(returnCommand)){
            LOGGER.error("[returnOrder] {} [{}] returnOrder save error. \"\"");
            DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
            defaultResultMessage.setMessage("系统异常！请联系管理员！");
            defaultReturnResult.setResultMessage(defaultResultMessage);
            defaultReturnResult.setResult(false);
        }
        return defaultReturnResult;

    }

    /**
     * 
     * @param List
     *            <Long> orderLineIds 订单行id
     * @param model
     * @RequestMapping(value = "/myAccountOrder/turnToReturn")
     *                       MemberDetails需要验证 needlogin
     * @return
     * 
     */
    public String showReturnApplication(@LoginMember MemberDetails memberDetails,@RequestParam(value = "orderLineIds",required = true) List<Long> orderLineIds,Model model){
        // 校验
        Validate.notNull(memberDetails, "memberDetails can't be null!");
        // orderLineIds 是不是一个order
        //校验订单行的的订单id是否一致
        Set<Long> orderIdList = new HashSet<Long>();
        for (Long orderLineId : orderLineIds){
            SalesOrderCommand salesOrderCommand = orderManager.findOrderByLineId(orderLineId);
            orderIdList.add(salesOrderCommand.getId());
            // 判断memberDetails和order id关系
            //订单的用户id与memberDetails的memberId不对应
            boolean isSelfOrder = isSelfOrder(memberDetails, salesOrderCommand);
            if (!isSelfOrder){
                Validate.isTrue(isSelfOrder, "order:%s,is not his self:[%s] order", salesOrderCommand.getCode(), JsonUtil.format(memberDetails, 0, 0));
            }
        }
        //如果orderIdList中只有一个元素，表示订单行归属于同一订单
        if (orderIdList.size() == 1){
            if (Validator.isNotNullOrEmpty(orderLineIds)){
                List<ReturnLineViewCommand> soReturnLineViews = returnLineManager.findReturnLineViewCommandByLineIds(orderLineIds);
                model.addAttribute("soReturnLineVo", soReturnLineViews);
                return "return.return-line";
            }
        }
        return "/errors/500";
    }

    /**
     * 
     * @param orderId
     * @param returnIds
     * @param model
     * @param request
     * @RequestMapping(value = "/myAccountOrder/returnDetail")
     * @return
     */
    public String showReturnDetail(@LoginMember MemberDetails memberDetails,@RequestParam(value = "returnIds") List<Long> returnIds,Model model,HttpServletRequest request){
        // 校验
        Validate.notNull(memberDetails, "memberDetails can't be null!");

        // 判断memberDetails和order id关系
        List<ReturnApplicationCommand> returnApplications = sdkReturnApplicationManager.findReturnApplicationCommandsByIds(returnIds);
        if (Validator.isNotNullOrEmpty(returnApplications)){
            for (ReturnApplicationCommand returnCommand : returnApplications){
                if (Validator.isNotNullOrEmpty(returnCommand.getReturnApplication())){
                    Long memberId = returnCommand.getReturnApplication().getMemberId();
                    //退货单与登录用户不匹配
                    if (memberDetails.getMemberId().longValue() != memberId){
                        return "/errors/500";
                    }
                }

            }
        }
        List<ReturnApplicationViewCommand> returnApplicationViewCommands = returnApplicationManager.findReturnApplicationViewCommand(returnApplications);
        model.addAttribute("returnApplications", returnApplicationViewCommands);
        return "return.returnDetail";
    }

    /**
     * 说明：判断是否可以查询订单
     * 
     * memberid相同 or 收货人姓名相同.
     *
     * @author 张乃骐
     * @param memberDetails
     *            the member details
     * @param salesOrderCommand
     *            the sales order command
     * @return true, if validate order<br>
     * @time：2016年5月23日 下午2:56:06
     */
    protected boolean isSelfOrder(MemberDetails memberDetails,SalesOrderCommand salesOrderCommand){
        Long memberId = salesOrderCommand.getMemberId();
        String name = salesOrderCommand.getName();

        if (null != memberDetails.getMemberId()){
            return memberDetails.getMemberId().longValue() == memberId.longValue();
        }
        if (Validator.isNotNullOrEmpty(memberDetails.getRealName())){
            return memberDetails.getRealName().equals(name);
        }
        return false;
    }

}
