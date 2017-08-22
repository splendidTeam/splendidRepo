package com.baozun.nebula.web.controller.returnapplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodesFoo;
import com.baozun.nebula.manager.returnapplication.ReturnApplicationLineManager;
import com.baozun.nebula.manager.returnapplication.ReturnApplicationManager;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
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
import com.baozun.nebula.web.controller.returnapplication.resolver.ReturnApplicationBuilder;
import com.baozun.nebula.web.controller.returnapplication.resolver.ReturnApplicationResolver;
import com.baozun.nebula.web.controller.returnapplication.validator.ReturnApplicationCreateValidator;
import com.baozun.nebula.web.controller.returnapplication.validator.ReturnApplicationPrivilegeValidator;
import com.baozun.nebula.web.controller.returnapplication.viewcommand.ReturnApplicationViewCommand;
import com.baozun.nebula.web.controller.returnapplication.viewcommand.ReturnLineViewCommand;
import com.feilong.core.Validator;

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
    
    //model key的常量定义
    
    public static final String      MODEL_KEY_RETURN_LINE_VIEW_COMMANDS = "returnLineViewCommands";
    
    public static final String      MODEL_KEY_RETURNAPPLICATIONS_VIEW_COMMANDS = "returnApplications";
    

    //view的常量定义
    
    /** 退换货申请默认页 */
    public static final String      VIEW_RETURNAPPLICATION_APPLY = "returnApplication.apply";
    
    public static final String      VIEW_RETURNAPPLICATION_DETAIL = "returnApplication.detail";
    
    
    /**
     * <br/>
     * 退货Form的校验器
     */
    @Autowired
    @Qualifier("returnApplicationCreateValidator")
    private ReturnApplicationCreateValidator returnApplicationCreateValidator;

    @Autowired
    @Qualifier("returnApplicationPrivilegeValidator")
    private ReturnApplicationPrivilegeValidator returnApplicationPrivilegeValidator;
    
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
    
    @Autowired
    private SdkReturnApplicationLineManager sdkReturnApplicationLineManager;
    
    /** 这个需要在spring.xml中配置上对应的 */
    @Autowired
    private ReturnApplicationBuilder defaultReturnApplicationBuilder;
    
    @Autowired(required = false)
    @Qualifier("customReturnApplicationBuilder")
    private ReturnApplicationBuilder customReturnApplicationBuilder;


    /**
     * 
     * @param memberDetails
     * @param bindingResult
     * @param request
     * @param returnOrderForm
     * @param model
     * @ResponseBody
     * @RequestMapping(value = "/myAccountOrder/commitReturnApplication, method = RequestMethod.POST)
     * @return
     */
    public NebulaReturnResult commitReturnApplication(
                    @LoginMember MemberDetails memberDetails,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    @ModelAttribute(value = "returnOrderForm") ReturnOrderForm returnOrderForm,
                    Model model){
        // 判断是否为本人进行操作
        assert memberDetails != null : "Please Check NeedLogin Annotation";
        LOGGER.info("[NebulaOrderReturnController] {} [{}] \"commit ReturnApplication\"", memberDetails.getLoginName(), new Date());
        Validate.notNull(memberDetails, "memberDetails can't be null!");

        DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
        returnApplicationCreateValidator.validate(returnOrderForm, bindingResult);

        // 如果校验失败，返回错误
        if (bindingResult.hasErrors()){
            LOGGER.error("[NebulaOrderReturnController] returnOrderForm validation error. \"\"");
            return getResultFromBindingResult(bindingResult);
        }

        defaultReturnResult = (DefaultReturnResult) getResultFromBindingResult(bindingResult);

        if (!defaultReturnResult.isResult()){
            defaultReturnResult.setStatusCode("return.validator.errors");
            return defaultReturnResult;
        }
        
        SalesOrderCommand salesOrderCommand = orderManager.findOrderById(returnOrderForm.getOrderId(), null);

        List<ReturnApplicationCommand>  returnApplications = null;
        
        // 官网如果有自己定义builder使用官网的builder
        if ( Validator.isNotNullOrEmpty(customReturnApplicationBuilder) ) {
        	returnApplications = customReturnApplicationBuilder.buildReturnApplicationCommands(memberDetails, returnOrderForm, salesOrderCommand);
        } else {
        	returnApplications = defaultReturnApplicationBuilder.buildReturnApplicationCommands(memberDetails, returnOrderForm, salesOrderCommand);
        }
        
        List<ReturnApplicationCommand> returnAppComs = sdkReturnApplicationManager.createReturnApplications(returnApplications, salesOrderCommand);


        return backNebulaReturnResult(returnAppComs, defaultReturnResult);

    }
    
    
    /**
     * 返回提交退换货订单结果
     * @param returnAppComs
     * @param returnResult
     * @return
     */
    private NebulaReturnResult backNebulaReturnResult( List<ReturnApplicationCommand> returnAppComs, DefaultReturnResult returnResult ){
        returnResult.setReturnObject(returnAppComs);
        if (Validator.isNullOrEmpty(returnAppComs)){
            LOGGER.error("[NebulaOrderReturnController] {} [{}] returnOrder save error. \"\"");
            DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
            defaultResultMessage.setMessage("create.returnApplication.error");
            returnResult.setResultMessage(defaultResultMessage);
            returnResult.setResult(false);
        }
        return returnResult;
    }
    



    /**
     * 退换货申请提交页
     * 
     * @param memberDetails需要验证 needlogin
     * @param orderLineIds
     * @param bindingResult
     * @param model
     * @return
     * @RequestMapping(value = "/myAccountOrder/showApplyReturnApplication")
     */
    public String showApplyReturnApplication(@LoginMember MemberDetails memberDetails,@RequestParam(value = "orderLineIds",required = true) List<Long> orderLineIds, BindingResult bindingResult, Model model){
        // 判断是否为本人进行操作
        assert memberDetails != null : "Please Check NeedLogin Annotation";
        LOGGER.info("[NebulaOrderReturnController] {} [{}] \"show ApplyReturnApplication\"", memberDetails.getLoginName(), new Date());
        Validate.notNull(memberDetails, "memberDetails can't be null!");

        returnApplicationPrivilegeValidator.validate(orderLineIds, bindingResult);
        // 如果校验失败，返回错误
        if (bindingResult.hasErrors()){
            LOGGER.error("[NebulaOrderReturnController] returnOrderForm validation error.");
            getResultFromBindingResult(bindingResult);
            throw new BusinessException(ErrorCodesFoo.ACCESS_DENIED);
        }

        buildReturnApplicationLine(orderLineIds,model);
        
        return getApplyReturnApplicationView(orderLineIds);
    }
    
    /**
     * 构建申请退换货需要数据
     * 
     * @param orderLineIds
     * @param model
     */
    protected void buildReturnApplicationLine(List<Long> orderLineIds,Model model){
        if (Validator.isNotNullOrEmpty(orderLineIds)){
            List<ReturnLineViewCommand> returnLineViewCommands = returnLineManager.findReturnLineViewCommandByLineIds(orderLineIds);
            model.addAttribute(MODEL_KEY_RETURN_LINE_VIEW_COMMANDS, returnLineViewCommands);
        }
    }

    /**
     * 返回退换货申请页面view
     * 
     * @param orderLineIds
     * @return
     */
    protected String getApplyReturnApplicationView(List<Long> orderLineIds){
        return VIEW_RETURNAPPLICATION_APPLY;
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
    public String showReturnApplicationDetail(@LoginMember MemberDetails memberDetails,@RequestParam(value = "returnLineIds") List<Long> returnLineIds,Model model,HttpServletRequest request){
        // 判断是否为本人进行操作
        assert memberDetails != null : "Please Check NeedLogin Annotation";
        LOGGER.info("[MEM_VIEW_PROFILE] {} [{}] \"commit ReturnApplication\"", memberDetails.getLoginName(), new Date());
        Validate.notNull(memberDetails, "memberDetails can't be null!");

        List<ReturnLineCommand> returnLineCommands = sdkReturnApplicationLineManager.findReturnLinesByIds(returnLineIds);
        List<Long> applications = new ArrayList<Long>();
        if(Validator.isNotNullOrEmpty(returnLineCommands)){
            for(ReturnLineCommand command : returnLineCommands){
                applications.add(command.getReturnOrderId());
                ReturnApplication application = sdkReturnApplicationManager.findByApplicationId(command.getReturnOrderId());
                if(Validator.isNotNullOrEmpty(application) && !memberDetails.getMemberId().equals(application.getMemberId())){
                    throw new BusinessException(ErrorCodesFoo.ACCESS_DENIED);
                }
            }
        } else {
            throw new BusinessException(ErrorCodesFoo.PARAMS_ERROR);
        }
        
        List<ReturnApplicationViewCommand> returnApplicationViewCommands = null;
        if(Validator.isNotNullOrEmpty(applications)){
            List<ReturnApplicationCommand> returnApplications = sdkReturnApplicationManager.findReturnApplicationCommandsByIds(applications);
            returnApplicationViewCommands = returnApplicationManager.findReturnApplicationViewCommand(returnApplications);
            model.addAttribute(MODEL_KEY_RETURNAPPLICATIONS_VIEW_COMMANDS, returnApplicationViewCommands);
        }
        return getReturnApplicationDetailView(returnApplicationViewCommands);
    }

    /**
     * 返回退换货详情页view
     * 
     * @param returnApplicationViewCommands
     * @return
     */
    private String getReturnApplicationDetailView(List<ReturnApplicationViewCommand> returnApplicationViewCommands){
        return VIEW_RETURNAPPLICATION_DETAIL;
    }

}
