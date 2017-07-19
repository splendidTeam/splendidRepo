/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.order.builder.subview;

import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.controller.order.viewcommand.InvoiceInfoSubViewCommand;
import com.feilong.core.bean.PropertyUtil;

/**
 * 专门用来构造 {@link InvoiceInfoSubViewCommand}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
@Component("invoiceInfoSubViewCommandBuilder")
public class DefaultInvoiceInfoSubViewCommandBuilder implements InvoiceInfoSubViewCommandBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.subview.InvoiceInfoSubViewCommandBuilder#buildInvoiceInfoSubViewCommand(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public InvoiceInfoSubViewCommand build(SalesOrderCommand salesOrderCommand){
        // 发票信息
        InvoiceInfoSubViewCommand invoiceInfoSubViewCommand = new InvoiceInfoSubViewCommand();

        //5.3.2.18添加对 "taxPayerId":纳税人识别码,"companyAddress":公司地址,"companyPhone":公司电话,"accountBankName":开户银行名称,"accountBankNumber":开户银行账号的转换
        PropertyUtil.copyProperties(
                        invoiceInfoSubViewCommand,
                        salesOrderCommand,
                        "receiptType",
                        "receiptTitle",
                        "receiptContent",
                        "receiptCode",
                        "receiptConsignee",

                        "receiptTelphone",
                        "receiptAddress",
                        "taxPayerId",
                        "companyAddress",
                        "companyPhone",
                        "accountBankName",
                        "accountBankNumber");
        return invoiceInfoSubViewCommand;
    }
}
