package com.baozun.nebula.utilities.integration.payment.unionpay;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.PaymentUtil;
import com.feilong.tools.jsonlib.JsonUtil;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.UnionPayBase;

public abstract class AbstractUnionPaymentAdaptor implements PaymentAdaptor{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUnionPaymentAdaptor.class);

    private static final String UNIONPAY_RETRUNCODE_NOTEXSITS = "34";

    private static final String UNIONPAY_RETRUNCODE_RECEIVED = "05";

    private static final String UNIONPAY_RETRUNCODE_SUCCESS = "00";

    private static final String USERPAYING = "USERPAYING";

    private static final String SUCCESS = "SUCCESS";

    private static final String ORDERNOTEXIST = "ORDERNOTEXIST";

    private static final String OTHERSTATUS = "OTHERSTATUS";

    public AbstractUnionPaymentAdaptor(){
        SDKConfig.getConfig().loadProperties(ProfileConfigUtil.findCommonPro("config/unionpay.properties"));
    }

    static{
        SDKConfig.getConfig().loadProperties(ProfileConfigUtil.findCommonPro("config/unionpay.properties"));
    }

    /**
     * 重要：联调测试时请仔细阅读注释！
     * 
     * 产品：跳转网关支付产品<br>
     * 交易：消费：前台跳转，有前台通知应答和后台通知应答<br>
     * 日期： 2015-09<br>
     * 版本： 1.0.0
     * 版权： 中国银联<br>
     * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
     * 提示：该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》，<br>
     * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表)<br>
     * 《全渠道平台接入接口规范 第3部分 文件接口》（对账文件格式说明）<br>
     * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
     * 调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
     * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
     * 2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
     * 交易说明:1）以后台通知或交易状态查询交易确定交易成功,前台通知不能作为判断成功的标准.
     * 2）交易状态查询交易（Form_6_5_Query）建议调用机制：前台类交易建议间隔（5分、10分、30分、60分、120分）发起交易查询，如果查询到结果成功，则不用再查询。（失败，处理中，查询不到订单均可能为中间状态）。也可以建议商户使用payTimeout（支付超时时间），过了这个时间点查询，得到的结果为最终结果。
     */
    @Override
    public PaymentRequest newPaymentRequest(String httpType,Map<String, String> map){
        UnionPaymentRequest unionPaymentRequest = new UnionPaymentRequest();

        // ////////////////////////////////////////////////
        //
        // 报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
        //
        // ////////////////////////////////////////////////

        /** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
        Map<String, String> submitFromData = AcpService.sign(map, SDKConstants.UTF_8_ENCODING);
        // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        LOGGER.info("submitFromData：" + submitFromData);
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, SDKConstants.UTF_8_ENCODING); // 生成自动跳转的Html表单

        LOGGER.info("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
        // 将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过

        LOGGER.info("html:" + html);
        unionPaymentRequest.setRequestHtml(html);
        unionPaymentRequest.setPaymentParameters(submitFromData);
        return unionPaymentRequest;
    }

    /**
     * 重要：联调测试时请仔细阅读注释！
     * 
     * 产品：跳转网关支付产品<br>
     * 交易：消费：前台跳转，有前台通知应答和后台通知应答<br>
     * 日期： 2015-09<br>
     * 版本： 1.0.0
     * 版权： 中国银联<br>
     * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
     * 提示：该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》，<br>
     * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表)<br>
     * 《全渠道平台接入接口规范 第3部分 文件接口》（对账文件格式说明）<br>
     * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
     * 调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
     * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
     * 2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
     * 交易说明:1）以后台通知或交易状态查询交易确定交易成功,前台通知不能作为判断成功的标准.
     * 2）交易状态查询交易（Form_6_5_Query）建议调用机制：前台类交易建议间隔（5分、10分、30分、60分、120分）发起交易查询，如果查询到结果成功，则不用再查询。（失败，处理中，查询不到订单均可能为中间状态）。也可以建议商户使用payTimeout（支付超时时间），过了这个时间点查询，得到的结果为最终结果。
     */
    @Override
    public PaymentRequest newPaymentRequestForMobileCreateDirect(Map<String, String> map){
        UnionPaymentRequest unionPaymentRequest = new UnionPaymentRequest();

        // ////////////////////////////////////////////////
        //
        // 报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
        //
        // ////////////////////////////////////////////////

        /** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
        Map<String, String> submitFromData = AcpService.sign(map, SDKConstants.UTF_8_ENCODING);
        // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, SDKConstants.UTF_8_ENCODING);
        // 生成自动跳转的Html表单

        LOGGER.info("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
        // 将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过

        LOGGER.info("html:" + html);
        unionPaymentRequest.setRequestHtml(html);
        unionPaymentRequest.setPaymentParameters(submitFromData);
        return unionPaymentRequest;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * 重要：联调测试时请仔细阅读注释！
     * 
     * 产品：跳转网关支付产品<br>
     * 功能：前台通知接收处理示例 <br>
     * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
     * 该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》，<br>
     * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表），
     * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
     * 调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
     * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
     * 2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
     * 交易说明：支付成功点击“返回商户”按钮的时候出现的处理页面示例
     */
    @Override
    public PaymentResult getPaymentResult(HttpServletRequest request){

        LOGGER.info("FrontRcvResponse前台接收报文返回开始");
        String encoding = request.getParameter(SDKConstants.param_encoding);
        LOGGER.info("返回报文中encoding=[" + encoding + "]");

        Map<String, String> respParam = getAllRequestParam(request);
        Validate.notEmpty(respParam, "respParam can't be null/empty!");

        // 打印请求报文
        LOGGER.info("getAllRequestParam:{}" + JsonUtil.format(respParam));

        Map<String, String> valideData = null;
        StringBuffer sb = new StringBuffer();

        //---------------------------------------------------------------
        PaymentResult paymentResult = new PaymentResult();
        try{
            Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
            valideData = new HashMap<>(respParam.size());
            while (it.hasNext()){
                Entry<String, String> e = it.next();
                String key = e.getKey();
                String value = e.getValue();

                value = new String(value.getBytes(encoding), encoding);

                sb.append("<tr><td width=\"30%\" align=\"right\">" + key + "(" + key + ")</td><td>" + value + "</td></tr>");
                valideData.put(key, value);
            }
        }catch (UnsupportedEncodingException e){
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setResponseValue(RequestParam.UNIONFAIL);
            paymentResult.setMessage(respParam.get("respMsg"));
            LOGGER.error("getPaymentResult error : {}", e.getMessage());
        }

        //---------------------------------------------------------------

        if (!AcpService.validate(valideData, encoding)){
            sb.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>失败</td></tr>");
            LOGGER.error("验证签名结果[失败].");
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setResponseValue(RequestParam.UNIONSUCCESS);
            paymentResult.setMessage(valideData.get("respMsg"));
        }else{
            sb.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>成功</td></tr>");
            LOGGER.info("验证签名结果[成功].");
            System.out.println(valideData.get("orderId")); // 其他字段也可用类似方式获取
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
            paymentResult.setResponseValue(RequestParam.UNIONSUCCESS);
            paymentResult.setMessage(valideData.get("respMsg"));
        }

        //---------------------------------------------------------------

        request.setAttribute("result", sb.toString());
        LOGGER.info("FrontRcvResponse前台接收报文返回结束");

        paymentResult.setPaymentStatusInformation(buildPaymentServiceReturnCommand(valideData));

        return paymentResult;
    }

    //---------------------------------------------------------------

    /**
     * @param valideData
     * @return
     * @since 5.3.2.17
     */
    private PaymentServiceReturnCommand buildPaymentServiceReturnCommand(Map<String, String> valideData){
        PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
        // 获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。
        paymentServiceReturnCommand.setReturnMsg(valideData.get("respMsg"));
        // 【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
        // 获取后台通知的数据，其他字段也可用类似方式获取
        paymentServiceReturnCommand.setOrderNo(valideData.get("orderId"));
        paymentServiceReturnCommand.setTradeNo(valideData.get("queryId"));

        //since 5.3.2.17  txnAmt
        paymentServiceReturnCommand.setTotalFee(PaymentUtil.toYuanString(valideData.get("txnAmt")));

        return paymentServiceReturnCommand;
    }

    //---------------------------------------------------------------

    /**
     * 重要：联调测试时请仔细阅读注释！
     * 
     * 产品：跳转网关支付产品<br>
     * 功能：后台通知接收处理示例 <br>
     * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
     * 该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》，<br>
     * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表），
     * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
     * 调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
     * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
     * 2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
     * 交易说明：成功的交易才会发送后台通知，建议此交易与交易状态查询交易结合使用确定交易是否成功
     */
    @Override
    public PaymentResult getPaymentResultFromNotification(HttpServletRequest request){
        PaymentResult result = new PaymentResult();
        LOGGER.info("BackRcvResponse接收后台通知开始");

        String encoding = request.getParameter(SDKConstants.param_encoding);
        // 获取银联通知服务器发送的后台通知参数
        Map<String, String> reqParam = getAllRequestParam(request);

        LOGGER.info("" + reqParam);

        Map<String, String> valideData = null;

        try{
            if (null != reqParam && !reqParam.isEmpty()){
                Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
                valideData = new HashMap<String, String>(reqParam.size());
                while (it.hasNext()){
                    Entry<String, String> e = it.next();
                    String key = e.getKey();
                    String value = e.getValue();
                    value = new String(value.getBytes(encoding), encoding);

                    valideData.put(key, value);
                }
            }
        }catch (UnsupportedEncodingException e){
            result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            result.setResponseValue(RequestParam.UNIONFAIL);
            result.setMessage(reqParam.get("respMsg"));
            LOGGER.error("getPaymentResult error : {}", e.getMessage());
        }

        // 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!AcpService.validate(valideData, encoding)){
            LOGGER.info("验证签名结果[失败].");
            // 验签失败，需解决验签问题
            result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            result.setResponseValue(RequestParam.UNIONSUCCESS);
            result.setMessage(valideData.get("respMsg"));

        }else{
            LOGGER.info("验证签名结果[成功].");
            result.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
            result.setResponseValue(RequestParam.UNIONSUCCESS);
            result.setMessage(valideData.get("respMsg"));
        }

        result.setPaymentStatusInformation(buildPaymentServiceReturnCommand(valideData));

        LOGGER.info("BackRcvResponse接收后台通知结束");

        return result;
    }

    //------------------------------------------------------------------------------------------

    /**
     * 重要：联调测试时请仔细阅读注释！
     * 
     * 产品：跳转网关支付产品<br>
     * 交易：交易状态查询交易：只有同步应答 <br>
     * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能及规范性等方面的保障<br>
     * 该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》，<br>
     * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表）<br>
     * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案： 调试过程中的问题或其他问题请在
     * https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
     * 测试过程中产生的6位应答码问题疑问请在https
     * ://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案 2）
     * 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。 交易说明：
     * 1）对前台交易发起交易状态查询
     * ：前台类交易建议间隔（5分、10分、30分、60分、120分）发起交易查询，如果查询到结果成功，则不用再查询。（失败，
     * 处理中，查询不到订单均可能为中间状态）。也可以建议商户使用payTimeout（支付超时时间），过了这个时间点查询，得到的结果为最终结果。
     * 2）对后台交易发起交易状态查询：后台类资金类交易同步返回00，成功银联有后台通知，商户也可以发起
     * 查询交易，可查询N次（不超过6次），每次时间间隔2N秒发起
     * ,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）。 后台类资金类同步返03 04
     * 05响应码及未得到银联响应
     * （读超时）需发起查询交易，可查询N次（不超过6次），每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03
     * ，04，05继续查询，否则终止查询）。
     */
    @Override
    public PaymentResult getOrderInfo(Map<String, String> addition){
        // 银联没有未付款之前的取消接口。所以调用查询接口 如果付款成功/交易已受理 那么取消失败。 其他状态都视为取消成功
        PaymentResult result = new PaymentResult();

        /** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文-------------> **/
        // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> reqData = AcpService.sign(addition, SDKConstants.UTF_8_ENCODING);

        // 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的acpsdk.singleQueryUrl
        String url = SDKConfig.getConfig().getSingleQueryUrl();

        // 这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> rspData = AcpService.post(reqData, url, SDKConstants.UTF_8_ENCODING);

        /** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/
        // 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()){
            if (AcpService.validate(rspData, SDKConstants.UTF_8_ENCODING)){
                LOGGER.info("验证签名成功");
                if ("00".equals(rspData.get("respCode"))){
                    // 如果查询交易成功
                    // 处理被查询交易的应答码逻辑
                    String origRespCode = rspData.get("origRespCode");
                    if ("00".equals(origRespCode)){
                        // 交易成功，更新商户订单状态
                        result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                        result.setMessage(SUCCESS);
                    }else if ("03".equals(origRespCode) || "04".equals(origRespCode) || "05".equals(origRespCode)){
                        // 需再次发起交易状态查询交易
                        result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                        result.setMessage(OTHERSTATUS);
                    }else{
                        // 其他应答码为失败请排查原因
                        result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                        result.setMessage(OTHERSTATUS);
                    }
                }else{
                    // 查询交易本身失败，或者未查到原交易，检查查询交易报文要素
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(OTHERSTATUS);
                }
            }else{
                LOGGER.error("验证签名失败");
                // 检查验证签名失败的原因
                result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                result.setMessage("sign not match");
            }
        }else{
            // 未返回正确的http状态
            LOGGER.error("未获取到返回报文或返回http状态码非200");
            result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            result.setMessage("HTTP Status Code is not 200 !");
        }

        //---------------------------------------------------------------
        String reqMessage = genHtmlResult(reqData);
        String rspMessage = genHtmlResult(rspData);
        LOGGER.warn("</br>请求报文:<br/>" + reqMessage + "<br/>" + "应答报文:</br>" + rspMessage + "");
        PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
        paymentServiceReturnCommand.setOrderNo(rspData.get("orderId"));
        result.setPaymentStatusInformation(paymentServiceReturnCommand);
        return result;
    }

    @Override
    public PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(HttpServletRequest request){
        return null;
    }

    @Override
    public PaymentResult getPaymentResultForMobileAuthAndExecuteASY(HttpServletRequest request){
        return null;
    }

    /**
     * 组装请求，返回报文字符串用于显示
     * 
     * @param data
     * @return
     */
    private static String genHtmlResult(Map<String, String> data){
        Map<String, String> tree = new TreeMap<>();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()){
            Entry<String, String> en = it.next();
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()){
            Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            if ("respCode".equals(key)){
                sf.append("<b>" + key + SDKConstants.EQUAL + value + "</br></b>");
            }else
                sf.append(key + SDKConstants.EQUAL + value + "</br>");
        }
        return sf.toString();
    }

    /**
     * 获取请求参数中所有的信息
     * 
     * @param request
     * @return
     */
    private static Map<String, String> getAllRequestParam(final HttpServletRequest request){
        Map<String, String> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp){
            while (temp.hasMoreElements()){
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                LOGGER.debug("支付回调request temp数据，键：{}；值：{}", en, value);
                if (null == res.get(en) || "".equals(res.get(en))){
                    res.remove(en);
                }
            }
        }
        return res;
    }

    //-------------------------------------------------------------------------------

    @Override
    public String getServiceProvider(){
        return null;
    }

    @Override
    public String getServiceType(){
        return null;
    }

    @Override
    public String getServiceVersion(){
        return null;
    }

    @Override
    public PaymentResult unifiedOrder(Map<String, String> addition){
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.utilities.integration.payment.PaymentAdaptor#
     * getCreateResponseToken()
     */
    @Override
    public PaymentRequest getCreateResponseToken(PaymentRequest paymentRequest){
        return paymentRequest;
    }

    //-------------------------------------------------------------------------------

    @Override
    public boolean isSupportClosePaymentRequest(){
        return true;
    }

    /**
     * 重要：联调测试时请仔细阅读注释！
     * 
     * 产品：跳转网关支付产品<br>
     * 交易：消费撤销：后台资金类交易，有同步应答和后台通知应答<br>
     * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
     * 该接口参考文档位置：open.unionpay.com帮助中心 下载 产品接口规范 《网关支付产品接口规范》<br>
     * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表）<br>
     * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
     * 调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
     * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
     * 2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
     * 交易说明:1）以后台通知或交易状态查询交易（Form_6_5_Query）确定交易成功，建议发起查询交易的机制：可查询N次（不超过6次），每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）
     * 2）消费撤销仅能对当清算日的消费做，必须为全额，一般当日或第二日到账。
     */
    @Override
    public PaymentResult closePaymentRequest(Map<String, String> addition){
        PaymentResult result = new PaymentResult();

        /** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文 **/
        Map<String, String> reqData = AcpService.sign(addition, SDKConstants.UTF_8_ENCODING);
        // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String reqUrl = SDKConfig.getConfig().getBackRequestUrl();
        // 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的acpsdk.backTransUrl

        Map<String, String> rspData = AcpService.post(reqData, reqUrl, SDKConstants.UTF_8_ENCODING);
        // 发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/

        // 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()){
            if (AcpService.validate(rspData, SDKConstants.UTF_8_ENCODING)){
                LOGGER.info("验证签名成功");
                String respCode = rspData.get("respCode");
                if ("00".equals(respCode)){
                    // 交易已受理(不代表交易已成功），等待接收后台通知确定交易成功，也可以主动发起 查询交易确定交易状态。
                    System.out.println("respCode = 00");
                    result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                    result.setMessage(SUCCESS);
                }else if ("03".equals(respCode) || "04".equals(respCode) || "05".equals(respCode)){
                    // 后续需发起交易状态查询交易确定交易状态。
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(OTHERSTATUS);
                }else{
                    // 其他应答码为失败请排查原因
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(OTHERSTATUS);
                }
            }else{
                LOGGER.error("验证签名失败");
                // TODO 检查验证签名失败的原因
                result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                result.setMessage(OTHERSTATUS);
            }
        }else{
            // 未返回正确的http状态
            LOGGER.error("未获取到返回报文或返回http状态码非200");
            result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            result.setMessage("HTTP Status Code is not 200 !");
        }
        String reqMessage = UnionPayBase.genHtmlResult(reqData);
        String rspMessage = UnionPayBase.genHtmlResult(rspData);
        LOGGER.warn("</br>请求报文:<br/>" + reqMessage + "<br/>" + "应答报文:</br>" + rspMessage + "");

        PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
        paymentServiceReturnCommand.setOrderNo(rspData.get("orderId"));
        result.setPaymentStatusInformation(paymentServiceReturnCommand);
        return result;
    }

}
