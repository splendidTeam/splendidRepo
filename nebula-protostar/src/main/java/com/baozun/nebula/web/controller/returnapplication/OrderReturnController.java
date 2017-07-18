package com.baozun.nebula.web.controller.returnapplication;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.OrderReturnCommand;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.manager.salesorder.ReturnOrderAppManager;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.model.returnapplication.ReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderLineManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationDeliveryManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationLineManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.PtsReturnOrderCommand;
import com.baozun.nebula.web.command.ReturnLineViewCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.returnapplication.form.ReturnOderForm;
import com.feilong.core.Validator;

@Controller
public class OrderReturnController extends BaseController{


    private static final String FORMAT_DATE_MIDST = "yyyyMMdd";

    private Logger logger = LoggerFactory.getLogger(OrderReturnController.class);

    @Autowired
    private SdkReturnApplicationLineManager sdkReturnLineManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private SdkSkuManager sdkSkuManager;

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private SdkOrderLineManager sdkOrderLineManager;

    @Autowired
    private SdkReturnApplicationManager sdkReturnApplicationManager;

    @Autowired
    private SdkReturnApplicationDeliveryManager returnApplicationDeliveryManager;

    @Autowired
    private ReturnOrderAppManager returnOrderAppManager;

    /* public static String[] returnBank={"中国工商银行","中国农业银行","中国银行","中国建设银行","交通银行","中信银行","中国光大银行","华夏银行","中国民生银行","广发银行股份有限公司","平安银行","招商银行","兴业银行","上海浦东发展银行"}; */

    /** 前台地址 */
    @Value("#{meta['frontend.url']}")
    private String frontendBaseUrl = "";

    /**
     * 订单退换货
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/saleOrder/orderReturn.htm")
    public String orderReturn(HttpServletRequest request){
        return "order/order-return";
    }

    /**
     * 退货单列表
     * 
     * @param model
     * @param queryBean
     * @return
     */
    @RequestMapping({ "/saleOrder/returnOrderList.json" })
    @ResponseBody
    public Pagination<ReturnApplication> returnOrderJson(Model model,@QueryBeanParam QueryBean queryBean){
        logger.info("==========returnOrderJson============");
        Sort[] sorts = queryBean.getSorts();
        if ((sorts == null) || (sorts.length == 0)){
            Sort sort = new Sort("application.create_time", "desc");
            sorts = new Sort[1];
            sorts[0] = sort;
        }
        Pagination<ReturnApplication> result = sdkReturnApplicationManager.findReturnByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
        return result;
    }

    /**
     * 退货商品行信息
     */
    @RequestMapping({ "/saleOrder/returnDetail.htm" })
    public String orderDetail(Model model,@RequestParam("id") Long id){
        // 查询退货单关联的订单行
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        List<ReturnLineCommand> soLine = sdkReturnLineManager.findSoReturnLinesByReturnOrderIds(ids);
        ReturnApplication app = sdkReturnApplicationManager.findByApplicationId(id);
        //如果是换货，查询换货物流
        if (ReturnApplication.SO_RETURN_TYPE_EXCHANGE.equals(app.getType())){
            ReturnApplicationDeliveryInfo returnDeliveryInfo = returnApplicationDeliveryManager.findDeliveryInfoByReturnId(app.getId());
            model.addAttribute("deliveryInfo", returnDeliveryInfo);
        }
        model.addAttribute("SoReturnLine", soLine);
        model.addAttribute("SoReturnApplication", app);
        model.addAttribute("frontendBaseUrl", frontendBaseUrl);
        return "/order/return-line";

    }

    /**
     * 创建退货单
     */
    @RequestMapping({ "/saleOrder/createReturnOrder.htm" })
    public String createReturnOrder(Model model){
        return "/order/create-return";

    }

    /**
     * 退款状态审核
     * 退货状态为 同意退款、拒绝退款、已完成
     * 
     * @param returnOrderCode
     * @param refundStatus
     * @author Huang
     * @return
     */
    @ResponseBody
    @RequestMapping({ "/saleOrder/updateRefund.json" })
    public Object updateFreundStatus(Model model,@RequestParam("returnOrderCode") String returnOrderCode,@RequestParam("state") Integer state,HttpServletRequest request){
        try{
            UserDetails userDetails = this.getUserDetails();
            String lastModifier = userDetails.getUsername();
            sdkReturnApplicationManager.updateRefundType(returnOrderCode, lastModifier, state);
        }catch (Exception e){
            logger.error("==========修改退款状态失败===============");
            FAILTRUE.setDescription("退款审核状态失败");
            return FAILTRUE;
        }
        return SUCCESS;
    }

    /**
     * 退换货审核
     */
    @RequestMapping(value = "/saleOrder/returnExaim.json",method = RequestMethod.POST)
    @ResponseBody
    public Object exaim(
                    Model model,
                    @RequestParam("orderCode") String orderCode,
                    @RequestParam("description") String description,
                    @RequestParam("status") Integer status,
                    @RequestParam("omsCode") String omsCode,
                    @RequestParam("returnAddress") String returnAddress){
        logger.info("============exaim=========");
        UserDetails use = this.getUserDetails();
        try{
            ReturnApplication returnapp = sdkReturnApplicationManager.auditReturnApplication(orderCode, status, description, use.getUsername(), omsCode, returnAddress);
            if (returnapp != null){
                return SUCCESS;
            }
        }catch (Exception e){
            FAILTRUE.setDescription(e.getMessage());
            logger.error("==========退货审核失败======" + e.getMessage());
        }
        return FAILTRUE;

    }

    /**
     * 退货单导出
     * 
     * @param model
     * @param request
     * @param response
     * @param queryBean
     * @return
     * @author jinhui.huang
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/salesorder/return/exportReturn.htm",method = RequestMethod.POST)
    @ResponseBody
    public String exportReturn(Model model,HttpServletRequest request,HttpServletResponse response,@QueryBeanParam QueryBean queryBean){
        String path = "excel/tplt-item-export-import.xls";
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
        String fileName = file.getName();
        OutputStream outputStream = null;

        // HttpServletResponse Header设置
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            outputStream = response.getOutputStream();
            Sort[] sorts = queryBean.getSorts();
            if ((sorts == null) || (sorts.length == 0)){
                Sort sort = new Sort("application.create_time", "desc");
                sorts = new Sort[1];
                sorts[0] = sort;
            }
            List<OrderReturnCommand> returnCommand = sdkReturnApplicationManager.findExpInfo(sorts, queryBean.getParaMap());
            XSSFWorkbook wb = this.setReturnWorkbook(returnCommand);
            if(wb != null){
                wb.write(outputStream);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "json";
    
    }

    /**
     * 导出退货信息，封装XSSFWorkbook数据
     * 
     * @author jinhui.huang
     * 
     */
    private XSSFWorkbook setReturnWorkbook(List<OrderReturnCommand> orderReturnCommandList){

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet1");
        initSheet(sheet);// 初始化sheet，设置列数和每列宽度

        XSSFCellStyle centerStyle = wb.createCellStyle();// 设置为水平居中
        centerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        centerStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        XSSFFont headerFont = (XSSFFont) wb.createFont(); // 创建字体样式
        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
        centerStyle.setFont(headerFont);
        initInputHeader(sheet, centerStyle);

        XSSFCellStyle centerStyleLeft = wb.createCellStyle();// 设置为水平居左
        centerStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        centerStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        int i = 1;
        for (OrderReturnCommand returnCommand : orderReturnCommandList){
            XSSFRow row = sheet.createRow((short) i);

            createCell(row, 0, returnCommand.getPlatformOMSCode(), centerStyleLeft);

            createCell(row, 1, returnCommand.getRefundPayee(), centerStyleLeft);

            createCell(row, 2, returnCommand.getSoOrderCode(), centerStyleLeft);

            createCell(row, 3, returnCommand.getProductName(), centerStyleLeft);

            createCell(row, 4, returnCommand.getCode(), centerStyleLeft);

            createCell(row, 5, returnCommand.getReturnPrice(), centerStyleLeft);

            createCell(row, 6, returnCommand.getQty(), centerStyleLeft);

            // STATUS
            String returnStatus = null;
            if (ReturnApplication.SO_RETURN_STATUS_AUDITING.equals(returnCommand.getStatus())){
                returnStatus = "待审核";
            }
            if (ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(returnCommand.getStatus())){
                returnStatus = "拒绝退货";
            }
            if (ReturnApplication.SO_RETURN_STATUS_TO_DELIVERY.equals(returnCommand.getStatus())){
                returnStatus = "待发货";
            }
            if (ReturnApplication.SO_RETURN_STATUS_DELIVERIED.equals(returnCommand.getStatus())){
                returnStatus = "已发货";
            }
            if (ReturnApplication.SO_RETURN_STATUS_AGREE_REFUND.equals(returnCommand.getStatus())){
                returnStatus = "同意退换货";
            }
            if (ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE.equals(returnCommand.getStatus())){
                returnStatus = "已完成";
            }
            createCell(row, 7, returnStatus, centerStyleLeft);

            // APPROVER
            createCell(row, 8, returnCommand.getApprover(), centerStyleLeft);
            // APPROVERTIME(YYYYMMDD)
            createCell(row, 9, formatDate(returnCommand.getApproveTime(), FORMAT_DATE_MIDST), centerStyleLeft);
            // CREATETIME
            createCell(row, 10, formatDate(returnCommand.getCreateTime(), FORMAT_DATE_MIDST), centerStyleLeft);

            // TRANSCODE
            createCell(row, 11, returnCommand.getTransCode(), centerStyleLeft);

            // Etransname
            createCell(row, 12, returnCommand.getTransName(), centerStyleLeft);
            // Etransname
            createCell(row, 13, returnCommand.getReturnReason(), centerStyleLeft);
             
            createCell(row, 14, returnCommand.getType() == 1 ? "退货" : "换货", centerStyleLeft);
            i++;
        }

        return wb;
    }

    /**
     * 创建单元格
     * 
     * @param row行
     * @param column列位置
     * @param value值
     * @param style样式
     * @author Huang
     */
    private void createCell(XSSFRow row,int column,Object value,XSSFCellStyle style){
        XSSFCell cell = row.createCell(column);
        cell.setCellValue(String.valueOf(value));
        cell.setCellStyle(style);
    }

    /**
     * 
     * @param sheet
     * @param style
     */
    private void initInputHeader(XSSFSheet sheet,XSSFCellStyle style){
        XSSFRow row1 = sheet.createRow((short) 0);
        createCell(row1, 0, "OMS退货编号", style);
        createCell(row1, 1, "登录名", style);
        createCell(row1, 2, "退换货订单平台订单编码", style);
        createCell(row1, 3, "商品名称", style);
        createCell(row1, 4, "商品款号", style);
        createCell(row1, 5, "退款金额", style);
        createCell(row1, 6, "数量", style);
        createCell(row1, 7, "订单状态", style);
        createCell(row1, 8, "审批人", style);
        createCell(row1, 9, "审批时间", style);
        createCell(row1, 10, "退货创建时间", style);
        createCell(row1, 11, "退货快递单号", style);
        createCell(row1, 12, "退货物流公司", style);
        createCell(row1, 13, "退换货原因", style);
        createCell(row1, 14, "退换货类型", style);

    }

    /**
     * 初始化sheet，设置列数和每列宽度
     * 
     * @author Huang
     * @param sheet
     */
    private void initSheet(XSSFSheet sheet){
        sheet.setColumnWidth(0, (short) (5000));
        sheet.setColumnWidth(1, (short) (5000));
        sheet.setColumnWidth(2, (short) (5000));
        sheet.setColumnWidth(3, (short) (5000));
        sheet.setColumnWidth(4, (short) (5000));
        sheet.setColumnWidth(5, (short) (5000));
        sheet.setColumnWidth(6, (short) (5000));
        sheet.setColumnWidth(7, (short) (5000));
        sheet.setColumnWidth(8, (short) (5000));
        sheet.setColumnWidth(9, (short) (5000));
        sheet.setColumnWidth(10, (short) (5000));
        sheet.setColumnWidth(11, (short) (5000));
        sheet.setColumnWidth(12, (short) (5000));
        sheet.setColumnWidth(13, (short) (5000));
        sheet.setColumnWidth(14, (short) (5000));

    }

    /**
     * 使用用户格式格式化日期
     * 
     * @param date日期
     * @param pattern日期格式
     * @author Huang
     * @return
     */
    private String formatDate(Date date,String pattern){
        String returnValue = "";
        if (date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    // 通过订单code查询订单
    @RequestMapping(value = "/order/findOrder.json")
    public String returnOrder(@RequestParam(value = "code") String code,@RequestParam(value = "type") int type,Model model){
        // 查询订单,订单code唯一，因此只需获取第一个元素即可
        SalesOrderCommand saleOrder = orderManager.findOrderByCode(code, 1);
        if (Validator.isNullOrEmpty(saleOrder)){
            model.addAttribute("errorMsg", "订单不存在！");
            return "order/return-errorMsg";
        }
        //未发货无法申请
        if (saleOrder.getLogisticsStatus() != SalesOrder.SALES_ORDER_STATUS_FINISHED && saleOrder.getLogisticsStatus() != SalesOrder.SALES_ORDER_STATUS_DELIVERIED && saleOrder.getLogisticsStatus() != SalesOrder.SALES_ORDER_STATUS_CANCELED
                        && saleOrder.getLogisticsStatus() != SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED){
            model.addAttribute("errorMsg", "订单尚未发货，无法申请退款！");
            return "order/return-errorMsg";
        }
        //未签收无法申请
        if (saleOrder.getLogisticsStatus() == SalesOrder.SALES_ORDER_STATUS_CANCELED || saleOrder.getLogisticsStatus() == SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED){
            model.addAttribute("errorMsg", "订单已取消，无法申请退款！");
            return "order/return-errorMsg";
        }
        //判断订单中是否剩余可退商品
        Integer num = 0;
        // 查到订单对应的退货数量，set进mkMemberOrders对象中
        for (OrderLineCommand line : saleOrder.getOrderLines()){
            // 根据订单行查询退货单数量
            num += sdkReturnApplicationManager.countCompletedAppsByPrimaryLineId(line.getId(),new Integer[]{ ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE });
        }
        if (num >= saleOrder.getQuantity()){
            model.addAttribute("errorMsg", "订单中无可退商品！");
            return "order/return-errorMsg";

        }
        // 再次校验是否有一存在的未完成的退货单
        List<OrderLineCommand> lineCommandList = saleOrder.getOrderLines();

        for (OrderLineCommand line : lineCommandList){
            ReturnApplication app = sdkReturnApplicationManager.findLastApplicationByOrderLineId(line.getId());
            if (null != app && app.getStatus() != 5 && app.getStatus() != 1){
                model.addAttribute("errorMsg", "当前订单尚有一笔未完成的退货单！无法再次申请！");
                return "order/return-errorMsg";
            }
        }

        List<OrderLineCommand> saleOrderLine = saleOrder.getOrderLines();
        List<ReturnLineViewCommand> soReturnLineVoList = new ArrayList<ReturnLineViewCommand>();
        for (OrderLineCommand line : saleOrderLine){
            String properties = line.getSaleProperty();
            List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
            line.setSkuPropertys(propList);

            ReturnLineViewCommand lineVo = new ReturnLineViewCommand();
            if (null != line.getType() && line.getType() != 0){
                Long itemId = line.getItemId();
                //通过itemId查询同款商品的其他尺码
                List<Sku> skuList = skuDao.findSkuByItemId(itemId);
                List<SkuCommand> skuCommandList = new ArrayList<SkuCommand>();
                for (Sku sku : skuList){
                    SkuCommand skuCommand = skuDao.findInventoryById(sku.getId());
                    skuCommand.setProperties(sdkSkuManager.getSkuPros(properties).get(0).getValue());
                    skuCommandList.add(skuCommand);
                }
                lineVo.setChgSkuCommandList(skuCommandList);
                // 查询 当前订单行 已经退过货的商品个数（退换货状态为已完成)
                Integer count = sdkReturnApplicationManager.countCompletedAppsByPrimaryLineId(line.getId(),new Integer[]{ ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE });
                lineVo.setCount(count);
                lineVo.setOrderLineCommand(line);
                soReturnLineVoList.add(lineVo);
            }

        }

        model.addAttribute("soReturnLineVo", soReturnLineVoList);

        model.addAttribute("salesOrder", saleOrder);
        model.addAttribute("type", type);
        // 银联和cod（货到付款）
        if (saleOrder.getPayment() == 1 || saleOrder.getPayment() == 320){
            model.addAttribute("isCod", true);
        }else{
            model.addAttribute("isCod", false);
        }
        return "order/return-application";
    }

    /**
     * 退货
     * 
     * @param request
     * @param model
     * @return
     * @throws MessageException
     */

    @ResponseBody
    @RequestMapping(value = "/order/returnCommit",method = RequestMethod.GET)
    public Object returnCommit(HttpServletRequest request,@ModelAttribute(value = "returnOrderForm") ReturnOderForm returnOrderForm,Model model){

        Date date = new Date();

        SalesOrderCommand saleOrder = orderManager.findOrderById(Long.parseLong(returnOrderForm.getOrderId()), null);

        if (saleOrder == null || saleOrder.getLogisticsStatus() == null || (saleOrder.getLogisticsStatus().compareTo(SalesOrder.SALES_ORDER_STATUS_FINISHED) != 0 && saleOrder.getLogisticsStatus().compareTo(SalesOrder.SALES_ORDER_STATUS_DELIVERIED) != 0)){
            // 信息
            FAILTRUE.setDescription("操作失败！订单物流状态异常！无法申请退货！");
            return FAILTRUE;
        }
        // 再次校验是否有一存在的未完成的退货单
        List<OrderLineCommand> lineCommandList = saleOrder.getOrderLines();

        for (OrderLineCommand line : lineCommandList){
            ReturnApplication app = sdkReturnApplicationManager.findLastApplicationByOrderLineId(line.getId());
            if (Validator.isNotNullOrEmpty(app) && !ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE.equals(app.getStatus()) && !ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(app.getStatus())){
                // 信息
                FAILTRUE.setDescription("操作失败！当前订单尚有一笔未完成的退货单！无法再次申请！");
                return FAILTRUE;
            }
        }

        Integer memoLong = returnOrderForm.getMemo().length();

        if (memoLong > 100){
            // 信息
            // 信息
            FAILTRUE.setDescription("操作失败！退货说明长度不能超过100！");
            return FAILTRUE;
        }
        // 计算总共退回的数量 
        Integer returnTotalNum = 0;
        //去除页面中获得字符串重的制表符
        String dest = "";
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(returnOrderForm.getSumSelected());
        dest = m.replaceAll("");
        String[] sumSelected = dest.split(",");
        for (int i = 0; i < sumSelected.length; i++){
            returnTotalNum += Integer.parseInt(sumSelected[i]);
        }

        Integer count = 0;
        // 查到订单对应的退货数量
        for (OrderLineCommand line : saleOrder.getOrderLines()){
            // 根据订单行查询退货单数量
            count += sdkReturnApplicationManager.countCompletedAppsByPrimaryLineId(line.getId(),new Integer[]{ ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE });
        }
        // 如果原始订单中商品数量减去已退掉的商品数量小于本次需要退的商品数量，退货失败
        if (saleOrder.getQuantity() - count < returnTotalNum){
            // 信息
            FAILTRUE.setDescription("操作失败！退货数量超出限制！");
            return FAILTRUE;
        }
        //保存退货单 
        ReturnApplication soReturnApp = new ReturnApplication();
        soReturnApp.setCreateTime(date);
        soReturnApp.setMemberId(saleOrder.getMemberId());
        soReturnApp.setRemark(returnOrderForm.getMemo());
        soReturnApp.setRefundAccount(returnOrderForm.getAccount());
        soReturnApp.setVersion(date);
        soReturnApp.setRefundBankBranch(returnOrderForm.getBranch());
        soReturnApp.setRefundBank(returnOrderForm.getBank());
        soReturnApp.setType(ReturnApplication.SO_RETURN_TYPE_RETURN);
        soReturnApp.setSoOrderCode(returnOrderForm.getOrderCode());
        soReturnApp.setSoOrderId(Long.parseLong(returnOrderForm.getOrderId()));
        soReturnApp.setReturnApplicationCode("VR" + new Date().getTime());
        soReturnApp.setRefundType(saleOrder.getPayment().toString());// 退款方式
        soReturnApp.setIsNeededReturnInvoice(ReturnApplication.SO_RETURN_NEEDED_RETURNINVOICE);
        soReturnApp.setReturnReason("");
        soReturnApp.setStatus(0);
        if (saleOrder.getMemberId() != null){// 如果是会员 非游客
            soReturnApp.setMemberId(saleOrder.getMemberId());
        }else{// 游客下单把下单 邮箱 作冗余过来
            soReturnApp.setMemberId(-1L);// 游客
        }
        List<ReturnApplicationLine> returnLineList = new ArrayList<ReturnApplicationLine>();

        // 总共的退款金额 
        BigDecimal returnTotalMoney = new BigDecimal(0);
        String[] linedIdSelected = returnOrderForm.getLineIdSelected().split(",");
        String[] reasonSelected = returnOrderForm.getReasonSelected().split(",");
        for (int i = 0; i < linedIdSelected.length; i++){
            ReturnApplicationLine returnLine = new ReturnApplicationLine();
            Long lineId = Long.parseLong(linedIdSelected[i]);
            OrderLine line = sdkOrderLineManager.findByPk(lineId);

            String returnReason = reasonSelected[i].trim();
            returnLine.setReturnReason(returnReason);

            returnLine.setQty(Integer.parseInt(sumSelected[i]));
            returnLine.setSoLineId(lineId);
            returnLine.setMemo(returnOrderForm.getMemo());
            returnLine.setRtnExtentionCode(line.getExtentionCode());
            returnTotalMoney = returnTotalMoney.add(line.getSubtotal().divide(new BigDecimal(line.getCount())).multiply(new BigDecimal(Integer.parseInt(sumSelected[i]))));
            returnLine.setReturnPrice(line.getSubtotal().divide(new BigDecimal(line.getCount())).multiply(new BigDecimal(Integer.parseInt(sumSelected[i]))));
            returnLine.setType(1);
            returnLine.setCreateTime(date);
            returnLineList.add(returnLine);

        }
        soReturnApp.setReturnPrice(returnTotalMoney);
        ReturnApplicationCommand command = new ReturnApplicationCommand();
        command.setReturnApplication(soReturnApp);
        try{
            sdkReturnApplicationManager.createReturnApplication(command, saleOrder);
            return SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            FAILTRUE.setDescription("操作失败！系统异常，请稍候刷新重试！");
            return FAILTRUE;
        }
    }

    /**
     * 跳转到申请退换货列表
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/returnApplicationList.htm")
    public String returnApplicationList(Model model){

        return "/salesorder/return-order-list";
    }

    @RequestMapping(value = "/order/returnApplicationList.json")
    @ResponseBody
    public Pagination<PtsReturnOrderCommand> returnApplicationListJson(Model model,@QueryBeanParam QueryBean queryBean,HttpServletRequest request,HttpServletResponse response){

        Sort[] sorts = queryBean.getSorts();

        if (sorts == null || sorts.length == 0){
            Sort sort = new Sort("sro.create_time", "desc");
            sorts = new Sort[1];
            sorts[0] = sort;
        }

        Pagination<PtsReturnOrderCommand> result = returnOrderAppManager.findReturnApplicationListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());

        return result;
    }

}
