package com.baozun.nebula.web.controller.salesorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.salesorder.SalesOrderManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.member.Contact;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderCreateManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartCommandBuilder;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.OrderCommand;
import com.baozun.nebula.web.command.OrderRefreshDataCommand;
import com.baozun.nebula.web.command.PtsSalesOrderCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;
import com.google.gson.Gson;

import loxia.dao.Pagination;
import loxia.dao.Sort;

@Controller
public class SalesOrderController extends BaseController{

    @Autowired
    private SalesOrderManager                        salesOrderManager;

    @Autowired
    private ChooseOptionManager                      chooseOptionManager;

    @Autowired
    private SdkShoppingCartManager                   sdkShoppingCartManager;
    @Autowired
    private SdkShoppingCartCommandBuilder            sdkShoppingCartCommandBuilder;

    @Autowired
    private OrderManager                             sdkOrderService;

    @Autowired
    private SdkOrderCreateManager                    sdkOrderCreateManager;

    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    @Autowired
    private SdkMemberManager                         sdkMemberManager;

    @Autowired
    private MemberManager                            memberManager;

    @Autowired
    private SdkItemManager                           sdkItemManager;

    // 货到付款
    private static final String                      COD_STR                = "货到付款";

    // 支付方式
    private static final String                      ORDER_PAYTYPE_TYPE     = "ORDER_PAYTYPE_TYPE";

    // 送货时间
    private static final String                      ORDER_APPOINTTIME      = "ORDER_APPOINTTIME";

    // 发票类型
    private static final String                      RECEIPT_TYPE           = "RECEIPT_TYPE";

    // 发票抬头
    private static final String                      RECEIPT_TITLE          = "RECEIPT_TITLE";

    // 发票内容
    private static final String                      RECEIPT_CONTENT        = "RECEIPT_CONTENT";

    // 促销活动角标
    private final static String                      PROMOTION_LOGO_MARK    = "PROMOTION_LOGO_MARK";

    public static final String                       RESULTCODE             = "resultCode";

    /** 图片服务器地址 */
    @Value("#{meta['upload.img.domain.base']}")
    private String                                   customBaseUrl          = "";

    /** 前台地址 */
    @Value("#{meta['frontend.url']}")
    private String                                   frontendBaseUrl        = "";

    /** 商品图片尺寸 */
    @Value("#{meta['smallSize']}")
    private String                                   smallSize              = "";

    @Value("#{meta['pdpPrefix']}")
    private String                                   pdpPrefix              = "";

    public static final String                       header_xForwardedFor   = "x-forwarded-for";

    public static final String                       header_proxyClientIP   = "Proxy-Client-IP";

    /**
     * WL-Proxy-Client-IP 这个应该是WebLogic前置HttpClusterServlet提供的属性，一般不需要自己处理， 在WebLogic控制台中已经可以指定使用这个属性来覆盖
     */
    public static final String                       header_wLProxyClientIP = "WL-Proxy-Client-IP";

    /** 最多保存10个有效地址 */
    private static final Integer                     MAX_NUM                = 10;

    /**
     * 跳转到订单列表
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/orderList.htm")
    public String orderList(Model model){
        return "/salesorder/order-list";
    }

    /**
     * 订单列表
     * 
     * @param model
     * @param queryBean
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "/order/orderList.json")
    @ResponseBody
    public Pagination<PtsSalesOrderCommand> orderListJson(
                    Model model,
                    @QueryBeanParam QueryBean queryBean,
                    HttpServletRequest request,
                    HttpServletResponse response){
        Sort[] sorts = queryBean.getSorts();

        if (sorts == null || sorts.length == 0){
            Sort sort = new Sort("o.create_time", "desc");
            sorts = new Sort[1];
            sorts[0] = sort;
        }
        encryptName(queryBean);
        Pagination<PtsSalesOrderCommand> result = salesOrderManager
                        .findOrderListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
        List<PtsSalesOrderCommand> list = result.getItems();
        for (PtsSalesOrderCommand ptsSalesOrderCommand : list){
            BigDecimal total = ptsSalesOrderCommand.getTotal();
            BigDecimal actualFreight = ptsSalesOrderCommand.getActualFreight();
            ptsSalesOrderCommand.setActotal(total.add(actualFreight));
        }
        result.setItems(list);
        return result;
    }
    
    /**
     * 加密收货人信息进行查询
     * @param queryBean
     */
	private void encryptName(QueryBean queryBean) {
		Map<String, Object> paraMap = queryBean.getParaMap();
        String name = (String)paraMap.get("name");
        if(paraMap.get("name")!=null){
        	String temp = name.replace("%", "");
        	try {
        		name=name.replace(temp, EncryptUtil.getInstance().encrypt(temp));
			} catch (EncryptionException e) {
				e.printStackTrace();
			}
        	paraMap.put("name", name);
        }
	}

    /**
     * 订单的详细信息
     * 
     * @param model
     * @param orderCode
     * @return
     */
    @RequestMapping(value = "/order/orderDetail.htm")
    public String orderDetail(Model model,@RequestParam("orderCode") String orderCode,@RequestParam("orderId") Long orderId){
        // modify by fangpf 2016-03-06 for use id
        // OrderCommand orderCommand = salesOrderManager.findOrderByCode(orderCode);
        OrderCommand orderCommand = salesOrderManager.findOrderById(orderId);

        if (orderCommand == null){
            throw new BusinessException(ErrorCodes.ORDER_NOT_EXIST, new Object[] { orderCode });
        }

        Address country = AddressUtil.getAddressById(orderCommand.getSalesOrderCommand().getCountryId());
        Address province = AddressUtil.getAddressById(orderCommand.getSalesOrderCommand().getProvinceId());
        Address city = AddressUtil.getAddressById(orderCommand.getSalesOrderCommand().getCityId());
        Address area = AddressUtil.getAddressById(orderCommand.getSalesOrderCommand().getAreaId());
        Address town = AddressUtil.getAddressById(orderCommand.getSalesOrderCommand().getTownId());
        orderCommand.getSalesOrderCommand().setCountry(country == null ? "" : country.getName());
        orderCommand.getSalesOrderCommand().setProvince(province == null ? "" : province.getName());
        orderCommand.getSalesOrderCommand().setCity(city == null ? "" : city.getName());
        orderCommand.getSalesOrderCommand().setArea(area == null ? "" : area.getName());
        orderCommand.getSalesOrderCommand().setTown(town == null ? "" : town.getName());

        model.addAttribute("orderCommand", orderCommand);
        model.addAttribute("customBaseUrl", customBaseUrl);
        model.addAttribute("frontendBaseUrl", frontendBaseUrl);
        model.addAttribute("smallSize", smallSize);
        return "/salesorder/detail";

    }

    /**
     * 根据订单的Id获取物流信息
     * 
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/order/getLogistics.json")
    @ResponseBody
    public LogisticsCommand getLogistics(String orderId){
        LogisticsCommand logistics = null;
        if (StringUtils.isNotBlank(orderId)){
            try{
                logistics = salesOrderManager.findLogisticsByOrderId(Long.valueOf(orderId));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return logistics;

    }


    /**
     * 跳转到订单创建页面
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/createOrder/{createType}.htm")
    public String createOrder(Model model,@PathVariable("createType") String createType){
        // 支付方式
        List<ChooseOption> payTypeList = chooseOptionManager.findEffectChooseOptionListByGroupCode(ORDER_PAYTYPE_TYPE);
        // 快递公司
        List<DistributionMode> distributionModeList = sdkOrderService.getAllDistributionMode();
        // 送货时间
        List<ChooseOption> appointTimeList = chooseOptionManager.findEffectChooseOptionListByGroupCode(ORDER_APPOINTTIME);
        // 发票类型
        List<ChooseOption> receiptTypeList = chooseOptionManager.findEffectChooseOptionListByGroupCode(RECEIPT_TYPE);
        // 发票抬头
        List<ChooseOption> receiptTitleList = chooseOptionManager.findEffectChooseOptionListByGroupCode(RECEIPT_TITLE);
        // 发票内容
        List<ChooseOption> receiptContentList = chooseOptionManager.findEffectChooseOptionListByGroupCode(RECEIPT_CONTENT);

        List<ChooseOption> promotionLogoMarkList = chooseOptionManager.findEffectChooseOptionListByGroupCode(PROMOTION_LOGO_MARK);
        Map<String, String> promotionLogoMarkMap = new HashMap<String, String>();
        for (ChooseOption promptionLogoMark : promotionLogoMarkList){
            promotionLogoMarkMap.put(promptionLogoMark.getOptionValue(), promptionLogoMark.getOptionLabel());
        }

        model.addAttribute("promotionLogoMarkMap", JsonFormatUtil.format(promotionLogoMarkMap));

        model.addAttribute("payTypeList", payTypeList);
        model.addAttribute("distributionModeList", distributionModeList);
        model.addAttribute("appointTimeList", appointTimeList);
        model.addAttribute("receiptTypeList", receiptTypeList);
        model.addAttribute("receiptTitleList", receiptTitleList);
        model.addAttribute("receiptContentList", receiptContentList);

        return "/salesorder/" + createType + "-order-create";

    }

    /**
     * 获取商品详细 在这里会获取商品所有的信息
     * 
     * @return
     */
    @RequestMapping("/product/getItemDetail.json")
    @ResponseBody
    public Object findItemDetail(Model model,@RequestParam("itemId") Long itemId,HttpServletRequest request){
        ItemBaseCommand itemBaseCommand = salesOrderManager.findItemBaseInfo(itemId);

        // 商品动态属性
        Map<String, Object> responseMap = salesOrderManager.findDynamicProperty(itemId);

        List<DynamicPropertyCommand> salePropCommandList = (List<DynamicPropertyCommand>) responseMap.get("salePropCommandList");
        List<SkuCommand> skuCommandList = salesOrderManager.findInventoryByItemId(itemId);
        itemBaseCommand.setSalePropCommandList(salePropCommandList);
        itemBaseCommand.setSkuCommandList(skuCommandList);
        return itemBaseCommand;
    }

    /**
     * 获得客户端ip地址
     * 
     * @param request
     * @return 获得客户端ip地址
     */
    public final static String getClientIp(HttpServletRequest request){
        // WL-Proxy-Client-IP=215.4.1.29
        // Proxy-Client-IP=215.4.1.29
        // X-Forwarded-For=215.4.1.29
        // WL-Proxy-Client-Keysize=
        // WL-Proxy-Client-Secretkeysize=
        // X-WebLogic-Request-ClusterInfo=true
        // X-WebLogic-KeepAliveSecs=30
        // X-WebLogic-Force-JVMID=-527489098
        // WL-Proxy-SSL=false
        String unknown = "unknown";

        String wrap = "\n\t\t";
        StringBuilder logBuilder = new StringBuilder();
        // 是否使用反向代理
        String ipAddress = request.getHeader(header_xForwardedFor);
        logBuilder.append(wrap + "header_xForwardedFor:" + ipAddress);
        if (Validator.isNullOrEmpty(ipAddress) || unknown.equalsIgnoreCase(ipAddress)){
            ipAddress = request.getHeader(header_proxyClientIP);
            logBuilder.append(wrap + "header_proxyClientIP:" + ipAddress);
        }
        if (Validator.isNullOrEmpty(ipAddress) || unknown.equalsIgnoreCase(ipAddress)){
            ipAddress = request.getHeader(header_wLProxyClientIP);
            logBuilder.append(wrap + "header_wLProxyClientIP:" + ipAddress);
        }
        if (Validator.isNullOrEmpty(ipAddress) || unknown.equalsIgnoreCase(ipAddress)){
            ipAddress = request.getRemoteAddr();
            logBuilder.append(wrap + "request.getRemoteAddr():" + ipAddress);
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15){ // "***.***.***.***".length()
                                                               // = 15
            logBuilder.append(wrap + "all:" + ipAddress);
            if (ipAddress.indexOf(",") > 0){
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        // log.debug(logBuilder.toString());
        return ipAddress;
    }

    /**
     * 提交订单
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/order/saveOrder.json")
    @ResponseBody
    public Object saveOrder(
                    SalesOrderCommand salesOrderCommand,
                    @RequestParam(value = "coupon",required = false) String coupon,
                    @RequestParam(value = "manualFlag",required = false) Boolean manualFlag,
                    HttpServletRequest request,
                    HttpServletResponse response){
        BackWarnEntity backWarnEntity = new BackWarnEntity();

        /** 运费所需参数 **/
        CalcFreightCommand calcFreightCommand = new CalcFreightCommand();

        calcFreightCommand.setProvienceId(salesOrderCommand.getProvinceId());
        calcFreightCommand.setCityId(salesOrderCommand.getCityId());
        calcFreightCommand.setCountyId(salesOrderCommand.getAreaId());
        calcFreightCommand.setDistributionModeId(salesOrderCommand.getDistributionModeId());

        salesOrderCommand.setCalcFreightCommand(calcFreightCommand);

        /** 省市区 **/
        Address province = AddressUtil.getAddressById(salesOrderCommand.getProvinceId());
        Address city = AddressUtil.getAddressById(salesOrderCommand.getCityId());
        Address area = AddressUtil.getAddressById(salesOrderCommand.getAreaId());

        salesOrderCommand.setProvince(province == null ? "" : province.getName());
        salesOrderCommand.setCity(city == null ? "" : city.getName());
        salesOrderCommand.setArea(area == null ? "" : area.getName());

        SalesOrderCreateOptions salesOrderCreateOptions = new SalesOrderCreateOptions();

        Long memberId = salesOrderCommand.getMemberId();

        /** 用户名 **/
        if (Validator.isNotNullOrEmpty(memberId)){
            MemberPersonalDataCommand memberPersonalDataCommand = memberManager.findMemberById(memberId);
            salesOrderCommand.setMemberName(memberPersonalDataCommand.getLoginName());
        }

        /** 付款方式 **/
        if (salesOrderCommand.getPaymentStr().equals(COD_STR)){
            salesOrderCommand.setPayment(Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_COD));
        }else{
            salesOrderCommand.setPayment(Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_ALIPAY));
        }

        /** IP **/
        salesOrderCommand.setIp(getClientIp(request));

        /** 订单来源 **/
        salesOrderCommand.setSource(SalesOrder.SO_SOURCE_NORMAL);

        /** 优惠券(用于返回购物车对象) **/
        List<String> codes = null;

        if (Validator.isNotNullOrEmpty(coupon)){
            codes = new ArrayList<String>();
            codes.add(coupon);
        }

        /** 优惠券（用于保存订单） **/
        if (Validator.isNotNullOrEmpty(coupon)){
            List<CouponCodeCommand> list = new ArrayList<CouponCodeCommand>();
            CouponCodeCommand couponCodeCommand = new CouponCodeCommand();
            couponCodeCommand.setCouponCode(coupon);
            list.add(couponCodeCommand);
            salesOrderCommand.setCouponCodes(list);
        }

        // 保存订单相关
        String subOrdinate = null;

        try{
            List<ShoppingCartLineCommand> lines = null;
            Set<String> memComboIds = null;

            ShoppingCartCommand shoppingCartCommand = null;
            if (manualFlag != null && manualFlag){

                /** 后台下单 **/
                salesOrderCreateOptions.setIsBackCreateOrder(true);

                lines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.MANUALBUY_SESSION_SHOPCART);
                shoppingCartCommand = sdkShoppingCartManager.findManualShoppingCart(lines);

                // 手工下单填写运费
                List<ShopCartCommandByShop> summaryShopCartList = shoppingCartCommand.getSummaryShopCartList();
                ShopCartCommandByShop shopCartCommandByShop = summaryShopCartList.get(0);
                shopCartCommandByShop.setOriginShoppingFee(salesOrderCommand.getActualFreight());
                shopCartCommandByShop.setRealPayAmount(shopCartCommandByShop.getRealPayAmount().add(salesOrderCommand.getActualFreight()));

                shoppingCartCommand.setOriginShoppingFee(salesOrderCommand.getActualFreight());
                shoppingCartCommand.setCurrentShippingFee(salesOrderCommand.getActualFreight());
                shoppingCartCommand.setCurrentPayAmount(shoppingCartCommand.getOriginPayAmount().add(salesOrderCommand.getActualFreight()));

                // 不清除购物车
                salesOrderCreateOptions.setIsImmediatelyBuy(true);
                subOrdinate = sdkOrderCreateManager.saveManualOrder(shoppingCartCommand, salesOrderCommand, salesOrderCreateOptions);
            }else{
                if (Validator.isNotNullOrEmpty(memberId)){
                    lines = sdkShoppingCartManager.findShoppingCartLinesByMemberId(memberId, 1);
                    memComboIds = getMemComboIds(memberId);
                }else{
                    memberId = null;
                    lines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);
                    // 不清除购物车
                    salesOrderCreateOptions.setIsImmediatelyBuy(true);
                }
                shoppingCartCommand = sdkShoppingCartCommandBuilder
                                .buildShoppingCartCommand(memberId, lines, calcFreightCommand, codes, memComboIds);
                subOrdinate = sdkOrderCreateManager.saveOrder(shoppingCartCommand, salesOrderCommand, memComboIds, salesOrderCreateOptions);
            }

            // 清空session
            if (manualFlag != null && manualFlag){
                // 手工单
                request.getSession().removeAttribute(Constants.MANUALBUY_SESSION_SHOPCART);
            }else{
                // 游客
                if (Validator.isNullOrEmpty(memberId)){
                    request.getSession().removeAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);
                }
            }

        }catch (BusinessException e){
            String message = "";
            if (e.getArgs() != null){
                message = getMessage(e.getErrorCode(), e.getArgs());
            }else{
                message = getMessage(e.getErrorCode());
            }
            backWarnEntity.setDescription(message);
            backWarnEntity.setIsSuccess(false);
            return backWarnEntity;
        }
        backWarnEntity.setIsSuccess(true);
        backWarnEntity.setDescription(subOrdinate);
        return backWarnEntity;
    }

    /**
     * 清空购物车
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/order/cleanShoppingCart.json")
    @ResponseBody
    public Object cleanShoppingCart(
                    Model model,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @RequestParam(value = "memberId",required = false) Long memberId,
                    @RequestParam(value = "manualFlag",required = false) Boolean manualFlag){
        try{
            if (manualFlag != null && manualFlag){
                request.getSession().removeAttribute(Constants.MANUALBUY_SESSION_SHOPCART);
            }else{
                if (Validator.isNotNullOrEmpty(memberId)){
                    sdkShoppingCartManager.emptyShoppingCart(memberId);
                }else{
                    request.getSession().removeAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);
                }
            }

        }catch (Exception e){
            return FAILTRUE;
        }
        return SUCCESS;
    }

    /**
     * 删除购物车行
     * 
     * @param model
     * @param request
     * @param response
     * @param extentionCode
     * @return
     */
    @RequestMapping("/order/removeShoppingCartLine.json")
    @ResponseBody
    public Object removeShoppingCartLine(
                    Model model,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @RequestParam(value = "extentionCode",required = true) String extentionCode,
                    @RequestParam(value = "memberId",required = false) Long memberId,
                    @RequestParam(value = "manualFlag",required = false) Boolean manualFlag){
        if (manualFlag != null && manualFlag){
            @SuppressWarnings("unchecked")
            List<ShoppingCartLineCommand> lines = (List<ShoppingCartLineCommand>) request.getSession()
                            .getAttribute(Constants.MANUALBUY_SESSION_SHOPCART);

            List<ShoppingCartLineCommand> newLines = new ArrayList<ShoppingCartLineCommand>();
            if (Validator.isNotNullOrEmpty(lines)){
                for (ShoppingCartLineCommand shoppingCartLineCommand : lines){
                    if (!shoppingCartLineCommand.getExtentionCode().equals(extentionCode)){
                        newLines.add(shoppingCartLineCommand);
                    }
                }
            }
            request.getSession().setAttribute(Constants.MANUALBUY_SESSION_SHOPCART, newLines);
        }else{
            if (Validator.isNotNullOrEmpty(memberId)){
                Integer updateCount = sdkShoppingCartManager.removeShoppingCartLine(memberId, extentionCode);
                if (updateCount != 1)
                    return FAILTRUE;
            }else{
                @SuppressWarnings("unchecked")
                List<ShoppingCartLineCommand> lines = (List<ShoppingCartLineCommand>) request.getSession()
                                .getAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);

                List<ShoppingCartLineCommand> newLines = new ArrayList<ShoppingCartLineCommand>();
                if (Validator.isNotNullOrEmpty(lines)){
                    for (ShoppingCartLineCommand shoppingCartLineCommand : lines){
                        if (!shoppingCartLineCommand.getExtentionCode().equals(extentionCode)){
                            newLines.add(shoppingCartLineCommand);
                        }
                    }
                }
                request.getSession().setAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART, newLines);
            }
        }

        return SUCCESS;
    }

    /**
     * 转换为ShoppingCartLineCommand对象
     * 
     * @param extensionCode
     * @param quantity
     * @param skuId
     * @return
     */
    public ShoppingCartLineCommand getShoppingCartLine(String extensionCode,Integer quantity,Long skuId){
        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
        shoppingCartLineCommand.setExtentionCode(extensionCode);
        shoppingCartLineCommand.setQuantity(quantity);
        shoppingCartLineCommand.setSkuId(skuId);
        shoppingCartLineCommand.setCreateTime(new Date());
        shoppingCartLineCommand.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
        return shoppingCartLineCommand;
    }

    public Set<String> getMemComboIds(Long memberId){
        List<Long> groupIds = new ArrayList<Long>();
        List<MemberGroupRelation> memberGroupRelations = sdkMemberManager.findMemberGroupRelationListByMemberId(memberId);
        if (null != memberGroupRelations && memberGroupRelations.size() > 0){
            for (MemberGroupRelation memberGroupRelation : memberGroupRelations){
                groupIds.add(memberGroupRelation.getGroupId());
            }
        }
        Set<String> comboIds = sdkMemberManager.getMemComboIdsByGroupIdMemberId(groupIds, memberId);
        return comboIds;
    }

    /**
     * 添加购物车
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/order/addToShoppingCart.json")
    @ResponseBody
    public Map<String, Object> addToShoppingCart(
                    Model model,
                    @RequestParam(value = "skuId",required = true) Long skuId,
                    @RequestParam(value = "quantity",required = true) Integer quantity,
                    @RequestParam(value = "extentionCode",required = true) String extentionCode,
                    @RequestParam(value = "memberId",required = false) Long memberId,
                    @RequestParam(value = "manualFlag",required = false) Boolean manualFlag,
                    @RequestParam(value = "buyPrice",required = false) BigDecimal buyPrice,
                    HttpServletRequest request,
                    HttpServletResponse response){
        Map<String, Object> result = new HashMap<String, Object>();
        Set<String> memComboIds = null;
        List<ShoppingCartLineCommand> lines = null;

        if (manualFlag != null && manualFlag){
            lines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.MANUALBUY_SESSION_SHOPCART);
            ShoppingCartLineCommand line = new ShoppingCartLineCommand();
            line.setSkuId(skuId);
            line.setQuantity(quantity);
            line.setExtentionCode(extentionCode);
            line.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
            line.setCreateTime(new Date());
            Integer retval = null;
            if (null == lines || lines.size() == 0){
                // 如果为空，则加入
                lines = new ArrayList<ShoppingCartLineCommand>();
            }
            retval = sdkShoppingCartManager.manualBuy(buyPrice, line, lines);
            if (retval == Constants.SUCCESS){
                request.getSession().setAttribute(Constants.MANUALBUY_SESSION_SHOPCART, lines);
            }
            result.put(RESULTCODE, retval);
        }else{
            if (Validator.isNotNullOrEmpty(memberId)){
                memComboIds = getMemComboIds(memberId);
                // 查询会员的购物车列表
                lines = sdkShoppingCartManager.findShoppingCartLinesByMemberId(memberId, null);
                boolean exist = true;// lines列表存在。并且表示该extentionCode也存在
                Integer count = 0;
                if (null == lines || lines.size() == 0){
                    exist = false;
                    lines = new ArrayList<ShoppingCartLineCommand>();
                    // 获取购物车行对象
                    lines.add(getShoppingCartLine(extentionCode, quantity, skuId));
                }else{
                    for (ShoppingCartLineCommand line : lines){
                        if (line.getExtentionCode().equals(extentionCode)){
                            line.setQuantity(line.getQuantity() + quantity);
                            count++;
                        }
                    }
                }
                if (count < 1 && exist){
                    // 如果该行不在,并且历史购物车中不存在
                    lines.add(getShoppingCartLine(extentionCode, quantity, skuId));
                }
                // 会员添加购物车行.游客调用该方法，主要是为了使购物车数据经过限购、有效、库存的检查
                Integer retval = sdkShoppingCartManager.addOrUpdateShoppingCart(memberId, extentionCode, lines, memComboIds, exist, false);
                result.put(RESULTCODE, retval);
            }else{
                lines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);
                ShoppingCartLineCommand line = new ShoppingCartLineCommand();
                line.setSkuId(skuId);
                line.setQuantity(quantity);
                line.setExtentionCode(extentionCode);
                line.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
                line.setCreateTime(new Date());
                Integer retval = null;
                if (null == lines || lines.size() == 0){
                    // 如果为空，则加入
                    lines = new ArrayList<ShoppingCartLineCommand>();
                }
                retval = sdkShoppingCartManager.immediatelyBuy(null, null, line, lines);

                if (retval == Constants.SUCCESS){
                    request.getSession().setAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART, lines);
                }
                result.put(RESULTCODE, retval);

            }
        }

        return result;// result;
    }

    /**
     * 填充数据
     * 
     * @param model
     * @param request
     * @param response
     * @param isBuyNow
     * @param contactId
     * @param coupons
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/order/refreshData")
    @ResponseBody
    public OrderRefreshDataCommand refreshData(
                    Model model,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @RequestParam(value = "provinceId",required = false) Long provinceId,// 省id
                    @RequestParam(value = "cityId",required = false) Long cityId,// 市id
                    @RequestParam(value = "areaId",required = false) Long areaId,// 区id
                    @RequestParam(value = "coupons",required = false) String coupons, // 优惠券
                    @RequestParam(value = "memberId",required = false) Long memberId,
                    @RequestParam(value = "manualFlag",required = false) Boolean manualFlag,
                    @RequestParam("distributionModeId") Long distributionModeId,
                    @RequestParam(value = "freightFee",required = false) BigDecimal freightFee){
        // 运费模板
        CalcFreightCommand calcFreightCommand = new CalcFreightCommand();

        calcFreightCommand.setProvienceId(provinceId);
        calcFreightCommand.setCityId(cityId);
        calcFreightCommand.setCountyId(areaId);
        calcFreightCommand.setDistributionModeId(distributionModeId);

        // 优惠券
        List<String> codes = null;

        if (Validator.isNotNullOrEmpty(coupons)){
            codes = new ArrayList<String>();
            codes.add(coupons);
        }
        List<ShoppingCartLineCommand> lines = null;

        Set<String> memComboIds = null;

        ShoppingCartCommand shoppingCartCommand = null;
        if (manualFlag != null && manualFlag){
            memberId = null;
            lines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.MANUALBUY_SESSION_SHOPCART);
            shoppingCartCommand = sdkShoppingCartManager.findManualShoppingCart(lines);

            // 手工设置运费
            if (Validator.isNotNullOrEmpty(freightFee) && Validator.isNotNullOrEmpty(shoppingCartCommand)){
                shoppingCartCommand.getSummaryShopCartList().get(0).setOriginShoppingFee(freightFee);
                shoppingCartCommand.getSummaryShopCartList().get(0)
                                .setRealPayAmount(shoppingCartCommand.getSummaryShopCartList().get(0).getRealPayAmount().add(freightFee));
            }

        }else{
            if (Validator.isNotNullOrEmpty(memberId)){
                lines = sdkShoppingCartManager.findShoppingCartLinesByMemberId(memberId, 1);
                memComboIds = getMemComboIds(memberId);
            }else{
                memberId = null;
                lines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);
                if (lines != null && lines.size() > 0){
                    for (ShoppingCartLineCommand shoppingCartLineCommand : lines){
                        shoppingCartLineCommand.setId(null);
                    }
                }
            }
            shoppingCartCommand = sdkShoppingCartCommandBuilder
                            .buildShoppingCartCommand(memberId, lines, calcFreightCommand, codes, memComboIds);
        }

        boolean isCouponBindActive = false;
        if (Validator.isNotNullOrEmpty(codes) && Validator.isNotNullOrEmpty(shoppingCartCommand)){
            List<PromotionSKUDiscAMTBySetting> shareList = sdkPromotionCalculationShareToSKUManager
                            .sharePromotionDiscountToEachLine(shoppingCartCommand, shoppingCartCommand.getCartPromotionBriefList());
            isCouponBindActive = sdkPromotionCalculationShareToSKUManager.checkCouponConsumedInBriefs(shareList, codes.get(0));
        }

        OrderRefreshDataCommand res = new OrderRefreshDataCommand();
        res.setShoppingCartCommand(shoppingCartCommand);
        res.setIsCouponBindActive(isCouponBindActive);
        res.setCustomBaseUrl(customBaseUrl);
        res.setFrontendBaseUrl(frontendBaseUrl);
        res.setSmallSize(smallSize);
        res.setPdpPrefix(pdpPrefix);

        return res;
    }

    /**
     * 验证优惠券
     * 
     * @param model
     * @param request
     * @param response
     * @param couponCode
     * @return
     */
    @RequestMapping(value = "/order/validCoupon.json",method = RequestMethod.POST)
    @ResponseBody
    public Object validCoupon(
                    Model model,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @RequestParam(value = "couponCode",required = true) String couponCode){
        // 先内部后外部 （5.12修改）

        PromotionCouponCode promotionCouponCode = sdkOrderService.validCoupon(couponCode);

        if (promotionCouponCode != null){
            return SUCCESS;
        }else{
            return FAILTRUE;
        }
    }

    /**
     * 查询会员信息json
     * 
     * @param model
     * @param memberId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/member/view.json")
    public Object getMemberPersonalDataCommand(Model model,@RequestParam("memberId") Long memberId){
        // 获取当前用户信息
        MemberPersonalDataCommand memberPersonalDataCommand = memberManager.findMemberById(memberId);
        List<ContactCommand> contacts = sdkMemberManager.findAllContactListByMemberId(memberId);

        OrderRefreshDataCommand orderRefreshDataCommand = new OrderRefreshDataCommand();
        orderRefreshDataCommand.setContacts(contacts);
        orderRefreshDataCommand.setMemberPersonalDataCommand(memberPersonalDataCommand);

        return orderRefreshDataCommand;
    }

    @ResponseBody
    @RequestMapping("/member/findContactDetail.json")
    public Object findContactById(@RequestParam("contactId") Long contactId){
        ContactCommand contact = sdkMemberManager.findContactById(contactId);
        if (contact != null){
            Address country = AddressUtil.getAddressById(contact.getCountryId());
            Address province = AddressUtil.getAddressById(contact.getProvinceId());
            Address city = AddressUtil.getAddressById(contact.getCityId());
            Address area = AddressUtil.getAddressById(contact.getAreaId());
            Address town = AddressUtil.getAddressById(contact.getTownId());
            contact.setCountry(country == null ? "" : country.getName());
            contact.setProvince(province == null ? "" : province.getName());
            contact.setCity(city == null ? "" : city.getName());
            contact.setArea(area == null ? "" : area.getName());
            contact.setTown(town == null ? "" : town.getName());
        }
        return contact;
    }

    /**
     * 保存或修改地址
     * 
     * @return
     */
    @RequestMapping("/account/address/add")
    @ResponseBody
    public Object saveOrUpdateContact(ContactCommand command,HttpServletRequest request,HttpServletResponse response){
        BackWarnEntity SUCCESS = new BackWarnEntity(true, "");

        Long memberId = command.getMemberId();

        if (Validator.isNullOrEmpty(memberId)){
            throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
        }

        MemberCommand memberCommand = sdkMemberManager.findMemberById(memberId);
        if (memberCommand == null){
            throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
        }
        Long contactId = command.getId();
        if (Validator.isNotNullOrEmpty(contactId)){// 修改地址
            ContactCommand con = sdkMemberManager.createOrUpdateContact(command);
            if (con == null){
                throw new BusinessException(ErrorCodes.MEMBER_CONTACT_SAVE_OR_UPDATE);
            }
            SUCCESS.setDescription(con.getId());
        }else{// 新建地址
            List<ContactCommand> contactList = sdkMemberManager.findAllContactListByMemberId(memberId);

            if (Validator.isNullOrEmpty(contactList)){
                command.setIsDefault(Contact.ISDEFAULT);
            }

            if (contactList != null && contactList.size() >= MAX_NUM){
                throw new BusinessException(ErrorCodes.MEMBER_CONTACT_MAX_NUM);
            }

            ContactCommand con = sdkMemberManager.createOrUpdateContact(command);

            if (con == null){
                throw new BusinessException(ErrorCodes.MEMBER_CONTACT_SAVE_OR_UPDATE);
            }
            SUCCESS.setDescription(con.getId());
        }
        return SUCCESS;
    }

    /**
     * 删除收货人
     * 
     * @return
     */
    @RequestMapping("/account/address/delete")
    @ResponseBody
    public Object removeContactById(
                    @RequestParam("contactId") Long contactId,
                    @RequestParam("memberId") Long memberId,
                    HttpServletRequest request,
                    HttpServletResponse response){
        // 验证用户是否存在
        MemberCommand memberCommand = sdkMemberManager.findMemberById(memberId);
        if (memberCommand == null){
            throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
        }
        Integer count = sdkMemberManager.removeContactById(contactId, memberId);
        if (count != 1){
            throw new BusinessException(ErrorCodes.MEMBER_CONTACT_DELETE);
        }
        return SUCCESS;
    }

    /**
     * 删除用户选中的赠品
     * 
     * @param lineId
     * @return
     */
    @RequestMapping(value = "/shopping/cart/deleteGift",method = RequestMethod.POST)
    @ResponseBody
    public Object removeShoppingCartLineGift(@RequestParam("lineId") Long lineId,@RequestParam("memberId") Long memberId){
        Integer resultCount = sdkShoppingCartManager.removeShoppingCartGiftByIdAndMemberId(lineId, memberId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (resultCount > 0){
            resultMap.put("resultCode", Constants.SUCCESS);
        }else{
            resultMap.put("resultCode", Constants.FAILURE);
        }
        return resultMap;
    }

    /**
     * 商品动态属性(用于购物车中商品的选择)
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/item/findItemsSalesPropByItemIds.json")
    @ResponseBody
    public Object findItemSalesProp(@RequestParam("itemIds") Long[] itemIds){
        Map<Long, Map<String, Object>> retMap = new HashMap<Long, Map<String, Object>>();
        if (null != itemIds && itemIds.length > 0){
            for (Long itemId : itemIds){
                Map<String, Object> responseMap = salesOrderManager.findDynamicProperty(itemId);
                retMap.put(itemId, responseMap);
            }
        }
        return retMap;
    }

    /**
     * 通过sku的itemProperties属性获取库存数
     * 
     * @param itemId
     * @param itemProperties
     *            用逗号(,)分割不同的itemPropertiesId
     * @return
     */
    @RequestMapping("/item/findInventory")
    @ResponseBody
    public Object findInventory(
                    @RequestParam("itemId") Long itemId,
                    @RequestParam(required = false,value = "itemProperties") String itemProperties){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("itemId", itemId);
        if (StringUtils.isNotBlank(itemProperties)){
            String[] itemPropStrs = itemProperties.split(",");
            List<Long> itemProps = new ArrayList<Long>();
            for (String str : itemPropStrs){
                itemProps.add(Long.valueOf(str));
            }
            Collections.sort(itemProps);
            Gson gson = new Gson();
            itemProperties = gson.toJson(itemProps);
            paramMap.put("itemProperties", itemProperties);
        }
        SkuCommand skuCommand = sdkItemManager.findInventory(paramMap);

        // 当库存为null是修改为0
        if (skuCommand != null && skuCommand.getAvailableQty() == null){
            skuCommand.setAvailableQty(0);
        }
        return skuCommand;
    }

    /**
     * 修改购物车礼品行
     * 
     * @param skuIds
     * @param lineGroups
     * @param request
     * @return
     */
    @RequestMapping(value = "/shopping/cart/updateGift",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateShoppingCartlineGift(
                    @RequestParam(value = "skuIds[]") Long skuIds[],
                    @RequestParam(value = "lineGroups[]") String[] lineGroups,
                    @RequestParam("promotionId") Long promotionId,
                    @RequestParam("memberId") Long memberId){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (skuIds.length != lineGroups.length){
            resultMap.put("resultCode", Constants.FAILURE);
            return resultMap;
        }
        sdkShoppingCartManager.updateShoppingCartlineGift(skuIds, lineGroups, memberId, promotionId);
        resultMap.put("resultCode", Constants.SUCCESS);
        return resultMap;
    }
}
